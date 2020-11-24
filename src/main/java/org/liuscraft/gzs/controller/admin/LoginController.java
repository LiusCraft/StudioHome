package org.liuscraft.gzs.controller.admin;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.liuscraft.gzs.mapper.HomeSettingMapper;
import org.liuscraft.gzs.pojo.HomeSetting;
import org.liuscraft.gzs.utils.StaticPagePath;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("admin")
public class LoginController {

    @Resource
    HomeSettingMapper settingMapper;

    @GetMapping("outLogin")
    public String outLogin(HttpServletRequest request){
        request.getSession().removeAttribute("TOKEN");
        return StaticPagePath.redirect(StaticPagePath.Admin.login);
    }

    @GetMapping("login")
    public String login(HttpServletRequest request, Model model){
        if(request.getSession().getAttribute("TOKEN") != null){
            return StaticPagePath.redirect(StaticPagePath.Admin.home_);
        }
        return StaticPagePath.Admin.login;
    }

    @PostMapping("login")
    public String login(HttpServletRequest request, Model model,
                        @RequestParam String userName, @RequestParam String passWord) {
        HttpSession session = request.getSession();
        if (session.getAttribute("TOKEN") != null) {
            return StaticPagePath.redirect(StaticPagePath.Admin.home_);
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("home_key", "user");
        HomeSetting homeSetting = settingMapper.selectOne(wrapper);
        JSONObject user = JSON.parseObject(homeSetting.getHomeValue());
        if (userName.equals(user.getString("userName")) && passWord.equals(user.getString("passWord"))) {
            session.setAttribute("TOKEN", System.currentTimeMillis());
            return StaticPagePath.redirect(StaticPagePath.Admin.home_);
        }
        return StaticPagePath.Admin.login;
    }
}
