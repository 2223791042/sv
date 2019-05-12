package guet.sgc.sv.controller;

import guet.sgc.sv.VO.ResultVO;
import guet.sgc.sv.entity.User;
import guet.sgc.sv.service.UserService;
import guet.sgc.sv.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/changePwd")
    public String changePwd(HttpSession session, Model model){
        User saveUser = (User) session.getAttribute("user");
        model.addAttribute("username", saveUser.getUsername());
        return "changePwd";
    }

    @ResponseBody
    @PutMapping("/changePwd/{username}")
    public ResultVO changePwd(@PathVariable("username")String username, String oldPassword, User user, HttpSession session, Model model){
        User saveUser = (User) session.getAttribute("user");
        if (!username.equals(saveUser.getUsername())){
            return ResultVOUtil.fail("不能修改当前用户");
        }
        if (!oldPassword.equals(saveUser.getPassword())){
            return ResultVOUtil.fail("原密码错误");
        }
        try{
            saveUser.setPassword(user.getPassword());
            userService.editUser(saveUser);
            model.addAttribute("user", saveUser);
            return ResultVOUtil.success("修改成功");
        }catch (Exception e){
            return ResultVOUtil.fail("修改失败");
        }
    }

    @ResponseBody
    @PostMapping("/login")
    public ResultVO login(String verifyCode, User user, HttpSession session){
        String saveVerifyCode = session.getAttribute("verifyCode").toString();
        if (!verifyCode.toUpperCase().equals(saveVerifyCode.toUpperCase())){
            return ResultVOUtil.fail("验证码错误，请重新输入");
        }
        User saveUser = userService.getUser(user.getUsername(), user.getUserType());
        if (saveUser == null){
            return ResultVOUtil.fail("账号不存在");
        }
        if (!saveUser.getPassword().equals(user.getPassword())){
            return ResultVOUtil.fail("密码错误");
        }
        session.setAttribute("user", saveUser);
        return ResultVOUtil.success("登录成功");
    }

    @GetMapping("/login")
    public String login(HttpSession session, Model model){
        User saveUser = (User) session.getAttribute("user");
        if (saveUser.getUserType().equals("student")){
            model.addAttribute("studentId", saveUser.getUsername());
            return "student";
        }
        if (saveUser.getUserType().equals("stuManager")){
            model.addAttribute("username", saveUser.getUsername());
            return "stuManager";
        }
        if (saveUser.getUserType().equals("admin")){
            model.addAttribute("username", saveUser.getUsername());
            return "admin";
        }
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "index";
    }
}
