package org.liuscraft.gzs.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.liuscraft.gzs.mapper.HomeSettingMapper;
import org.liuscraft.gzs.mapper.MemberMapper;
import org.liuscraft.gzs.mapper.SlideMapper;
import org.liuscraft.gzs.pojo.HomeSetting;
import org.liuscraft.gzs.pojo.Link;
import org.liuscraft.gzs.pojo.Member;
import org.liuscraft.gzs.pojo.Slide;
import org.liuscraft.gzs.utils.StaticPagePath;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("admin")
public class HomeController {

    @Resource
    SlideMapper slideMapper;

    @Resource
    HomeSettingMapper homeSettingMapper;

    @Resource
    MemberMapper memberMapper;

    @GetMapping
    public String index(HttpServletRequest request, Model redirectAttributes){
        // 获取幻灯片
        List<Slide> slideList = slideMapper.selectList(null);
        redirectAttributes.addAttribute("slideList", slideList);
        // 获取导航栏
        JSONArray links = JSON.parseArray(homeSettingMapper.selectById(2).getHomeValue());
        redirectAttributes.addAttribute("links", links);
        // 获取成员
        List<Member> memberList = memberMapper.selectList(null);
        redirectAttributes.addAttribute("memberList", memberList);

        return StaticPagePath.Admin.home;
    }

    @PostMapping("slideUpdate")
    public String slideUpdate(RedirectAttributes redirectAttributes, Slide slide){
        redirectAttributes.addFlashAttribute("ok", false);

        if (slide.getId() != null){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("id", slide.getId());
            if(slideMapper.update(slide, wrapper) == 0){
                redirectAttributes.addFlashAttribute("message", "更新幻灯片失败!");
            }else{
                redirectAttributes.addFlashAttribute("ok", true);
                redirectAttributes.addFlashAttribute("message", "更新幻灯片成功!");
            }
        }else{
            if(slideMapper.insert(slide) == 0){
                redirectAttributes.addFlashAttribute("message", "添加幻灯片失败!");
            }else {
                redirectAttributes.addFlashAttribute("ok", true);
                redirectAttributes.addFlashAttribute("message", "添加幻灯片成功!");
            }
        }

        return StaticPagePath.redirect(StaticPagePath.Admin.home_);
    }

    @GetMapping("slideDelete")
    public String slideDelete(RedirectAttributes redirectAttributes, Integer id){
        redirectAttributes.addFlashAttribute("ok", false);
        if(slideMapper.deleteById(id) == 0){
            redirectAttributes.addFlashAttribute("message", "删除幻灯片失败!");
        }else{
            redirectAttributes.addFlashAttribute("message", "删除幻灯片成功!");
        }
        return StaticPagePath.redirect(StaticPagePath.Admin.home_);
    }

    @PostMapping("linkUpdate")
    public String linkUpdate(RedirectAttributes redirectAttributes, Integer id, Link link){
        JSONObject linkJSON = (JSONObject)JSONObject.toJSON(link);
        HomeSetting linkSetting = homeSettingMapper.selectById(2);
        JSONArray links = JSON.parseArray(linkSetting.getHomeValue());
        if (id>0){
            links.set(id, linkJSON);
        }else {
            links.add(linkJSON);
        }
        linkSetting.setHomeValue(links.toString());
        homeSettingMapper.updateById(linkSetting);
        return StaticPagePath.redirect(StaticPagePath.Admin.home_);
    }

    @GetMapping("linkDelete")
    public String linkDelete(RedirectAttributes redirectAttributes, Integer id){
        HomeSetting linkSetting = homeSettingMapper.selectById(2);
        JSONArray links = JSON.parseArray(linkSetting.getHomeValue());
        if(links.remove(id.intValue()) != null){
            linkSetting.setHomeValue(links.toString());
            homeSettingMapper.updateById(linkSetting);
            redirectAttributes.addFlashAttribute("ok", true);
            redirectAttributes.addFlashAttribute("message", "删除成功");
        }else{
            redirectAttributes.addFlashAttribute("ok", false);
            redirectAttributes.addFlashAttribute("message", "删除失败");
        }
        return StaticPagePath.redirect(StaticPagePath.Admin.home_);
    }

    @PostMapping("memberUpdate")
    public String memberUpdate(RedirectAttributes redirectAttributes, Member member){
        if (member.getId() != null){
            if(memberMapper.updateById(member) == 0){
                redirectAttributes.addFlashAttribute("ok", false);
                redirectAttributes.addFlashAttribute("message", "更新成员失败!");

            }else{
                redirectAttributes.addFlashAttribute("ok", true);
                redirectAttributes.addFlashAttribute("message", "更新成员成功!");
            }
        }else {
            if(memberMapper.insert(member) == 0){
                redirectAttributes.addFlashAttribute("ok", false);
                redirectAttributes.addFlashAttribute("message", "添加成员失败!");
            }else{
                redirectAttributes.addFlashAttribute("ok", true);
                redirectAttributes.addFlashAttribute("message", "添加成员成功!");
            }
        }
        return StaticPagePath.redirect(StaticPagePath.Admin.home_);
    }

    @GetMapping("memberDelete")
    public String memberDelete(RedirectAttributes redirectAttributes, Integer id){
        redirectAttributes.addFlashAttribute("ok", false);
        if(memberMapper.deleteById(id) == 0){
            redirectAttributes.addFlashAttribute("message", "删除成员失败!");
        }else{
            redirectAttributes.addFlashAttribute("message", "删除成员成功!");
        }
        return StaticPagePath.redirect(StaticPagePath.Admin.home_);
    }
}


