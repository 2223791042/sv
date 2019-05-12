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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private VacateService vacateService;

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

    @GetMapping("/vacate/{leaveId}")
    public String vacateLook(@PathVariable("leaveId")Long leaveId, Model model){
        Vacate vacate = vacateService.getVacate(leaveId);
        model.addAttribute("vacate", vacate);
        return "vacate-look";
    }

    @ResponseBody
    @GetMapping("/vacateList")
    public TableListVO<Vacate> vacateList(int page, int limit, HttpServletRequest request){
        //开启分页查询
        PageHelper.startPage(page, limit);
        String classId = request.getParameter("classId");
        String studentId = request.getParameter("studentId");
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
        List<Vacate> vacateList = vacateService.getVacateList(classId, studentId, startTime, endTime);
        PageInfo<Vacate> pageInfo = new PageInfo<>(vacateList);
        return new TableListVO<>(pageInfo.getTotal(), vacateList);
    }

    @ResponseBody
    @DeleteMapping("/vacateDelBatch/{id_str}")
    public ResultVO vacateDelBatch(@PathVariable("id_str") String id_str){
        try{
            vacateService.delBatchVacate(id_str);
            return ResultVOUtil.success("删除成功");
        }catch (Exception e){
            return ResultVOUtil.fail("删除失败");
        }
    }

}
