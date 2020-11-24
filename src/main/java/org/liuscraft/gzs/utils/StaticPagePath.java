package org.liuscraft.gzs.utils;

public class StaticPagePath {
    public static String home = "index"; // 首页
    public static class Admin { // 后台
        public static String home = "admin/index"; // 后台首页
        public static String home_ = "/admin"; // 后台首页
        public static String login = "admin/login"; // 后台登录
    }

    public static String redirect(String url){
        return "redirect:"+url;
    }
}
