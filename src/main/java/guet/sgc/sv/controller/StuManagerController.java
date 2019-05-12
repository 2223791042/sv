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
import org.springframework.ui.Model;
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
@RequestMapping("/stuManager")
public class StuManagerController {
    @Autowired
    private VacateService vacateService;

    @Value("${fileRootPath}")
    private String fileRootPath;

    @GetMapping("/vacate")
    public String vacate(){
        return "vacate-add";
    }

    @ResponseBody
    @DeleteMapping("/vacate/{leaveId}")
    public ResultVO vacateDel(@PathVariable("leaveId")Long leaveId){
        try{
            vacateService.delVacate(leaveId);
            return ResultVOUtil.success("删除成功");
        }catch (Exception e){
            return ResultVOUtil.fail("删除失败");
        }
    }

    @ResponseBody
    @PostMapping("/vacate")
    public ResultVO vacateAdd(@RequestParam("file")MultipartFile file,
                              Vacate vacate, HttpSession session){
        User saveUser = (User) session.getAttribute("user");
        if (!vacate.getStudentId().contains(saveUser.getUsername())){
            return ResultVOUtil.fail("对不起，无权限提交，请检查学号是否正确");
        }
        try{
            if(!file.isEmpty()){
                String fileName = file.getOriginalFilename();
                String folderPath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+ "static/vacate/file/"+vacate.getStudentId();
                folderPath = URLDecoder.decode(folderPath, "UTF-8");
                File folder = new File(folderPath);
                if(!folder.exists()){
                    folder.mkdirs();
                }
                File dest = new File(folderPath +"/" + fileName);
                file.transferTo(dest);
                String fileUrl = fileRootPath + "/vacate/file/"+vacate.getStudentId()+"/"+fileName;
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

    @GetMapping("/vacate/{leaveId}")
    public String vacateEdit(@PathVariable("leaveId")Long leaveId, Model model){
        Vacate vacate = vacateService.getVacate(leaveId);
        model.addAttribute("vacate", vacate);
        return "vacate-edit";
    }

    @ResponseBody
    @PutMapping("/vacate/{leaveId}")
    public ResultVO vacateEdit(@PathVariable("leaveId")Long leaveId, Vacate vacate, @RequestParam("file")MultipartFile file){
        vacate.setLeaveId(leaveId);
        try {
            if(!file.isEmpty()){
                String fileName = file.getOriginalFilename();
                String folderPath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+ "static/vacate/file";
                folderPath = URLDecoder.decode(folderPath, "UTF-8");
                File folder = new File(folderPath);
                if(!folder.exists()){
                    folder.mkdirs();
                }
                File dest = new File(folderPath +"/" + fileName);
                file.transferTo(dest);
                String fileUrl = fileRootPath + "/vacate/file/"+fileName;
                vacate.setLeaveFile(fileUrl);
            }
            vacate.setSubmitTime(new Date());
            vacateService.editVacate(vacate);
            vacate.setLeaveStatus(0);
            return ResultVOUtil.success("修改成功");
        }catch (Exception e){
            return ResultVOUtil.fail("修改失败");
        }
    }

    @ResponseBody
    @GetMapping("/vacateList")
    public TableListVO<Vacate> vacateList(int page, int limit, HttpServletRequest request, HttpSession session){
        //开启分页查询
        PageHelper.startPage(page, limit);
        //加入时间（开始-结束）
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = null;
        Date endTime = null;
        try{
            if (request.getParameter("startTime") != null && !request.getParameter("startTime").trim().equals("")){
                startTime = simpleDateFormat.parse(request.getParameter("startTime"));
            }
            if (request.getParameter("endTime") != null && !request.getParameter("endTime").trim().equals("")){
                endTime = simpleDateFormat.parse(request.getParameter("endTime"));
                endTime = new Date(endTime.getTime()+86400000-1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        User saveUser = (User)session.getAttribute("user");
        List<Vacate> vacateList = vacateService.getVacateList(saveUser.getUsername(), startTime, endTime);
        PageInfo<Vacate> pageInfo = new PageInfo<>(vacateList);
        return new TableListVO<>(pageInfo.getTotal(), vacateList);
    }
}
