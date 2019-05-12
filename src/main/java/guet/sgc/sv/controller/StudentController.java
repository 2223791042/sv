package guet.sgc.sv.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import guet.sgc.sv.VO.ResultVO;
import guet.sgc.sv.VO.TableListVO;
import guet.sgc.sv.entity.User;
import guet.sgc.sv.entity.Vacate;
import guet.sgc.sv.service.VacateService;
import guet.sgc.sv.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Value("${fileRootPath}")
    private String fileRootPath;

    @Autowired
    private VacateService vacateService;

    @GetMapping("/vacate")
    public String vacate(){
        return "studentVacate-add";
    }

    @ResponseBody
    @GetMapping("/vacateList")
    public TableListVO<Vacate> vacateList(int page, int limit, HttpServletRequest request, HttpSession session){
        //开启分页查询
        PageHelper.startPage(page, limit);
        User saveUser = (User)session.getAttribute("user");
        List<Vacate> vacateList = vacateService.getVacateList(saveUser.getUsername());
        PageInfo<Vacate> pageInfo = new PageInfo<>(vacateList);
        return new TableListVO<>(pageInfo.getTotal(), vacateList);
    }

    @ResponseBody
    @PostMapping("/vacate")
    public ResultVO vacateAdd(@RequestParam("file")MultipartFile file,
                              Vacate vacate, HttpSession session){
        User saveUser = (User) session.getAttribute("user");
        if (!saveUser.getUsername().equals(vacate.getStudentId())){
            return ResultVOUtil.fail("对不起，您无权限进行此操作，请核对您的学号是否有误");
        }
        //判断是否是晚7-9点
        Date date = new Date();
        if (date.getHours() >= 19 && date.getHours() <= 21){
            return ResultVOUtil.fail("该时间段禁止提交");
        }
        try{
            Vacate saveVacate = vacateService.getVacate(saveUser.getUsername(), new Date());
            if (saveVacate != null){
                return ResultVOUtil.fail("提交次数上限，一天只能提交一次，如果信息有误，请和管理员联系");
            }
            if(!file.isEmpty()){
                String fileName = file.getOriginalFilename();
                String folderPath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+ "static/vacate/file/"+saveUser.getUsername();
                folderPath = URLDecoder.decode(folderPath, "UTF-8");
                File folder = new File(folderPath);
                if(!folder.exists()){
                    folder.mkdirs();
                }
                File dest = new File(folderPath +"/" + fileName);
                file.transferTo(dest);
                String fileUrl = fileRootPath + "/vacate/file/"+saveUser.getUsername()+"/"+fileName;
                vacate.setLeaveFile(fileUrl);
            }
            vacate.setSubmitTime(new Date());
            vacate.setLeaveStatus(0);
            vacateService.saveVacate(vacate);
            return ResultVOUtil.success("提交成功");
        }catch (Exception e){
            return ResultVOUtil.fail("提交失败");
        }
    }

}
