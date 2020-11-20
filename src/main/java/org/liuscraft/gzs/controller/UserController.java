package org.liuscraft.gzs.controller;

import org.liuscraft.gzs.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("User")
public class UserController {

    @GetMapping("Login")
    public String login(Model model){
        User user = new User();
        user.setUserNick("小米");
        user.setPassWord("123123");
        user.setEmail("xiaomi@qq.com");
        model.addAttribute("user",user);
        return "index";
    }
}
