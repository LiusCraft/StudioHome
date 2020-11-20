package org.liuscraft.gzs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.tomcat.util.json.JSONParser;
import org.liuscraft.gzs.mapper.*;
import org.liuscraft.gzs.pojo.BbsDiscussionTag;
import org.liuscraft.gzs.pojo.BbsDiscussions;
import org.liuscraft.gzs.pojo.BbsTags;
import org.liuscraft.gzs.pojo.HomeSetting;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping
public class HomeController {

    @Resource
    BbsDiscussionTagMapper bbsDiscussionTagMapper;

    @Resource
    BbsDiscussionsMapper bbsDiscussionsMapper;

    @Resource
    BbsTagsMapper bbsTagsMapper;

    @Resource
    HomeSettingMapper homeSettingMapper;

    @Resource
    MemberMapper memberMapper;

    @GetMapping
    public String index(Model model){
        HashMap<String, Object> homeSettingMap = new HashMap<>();
        List<HomeSetting> homeSettingList = homeSettingMapper.selectList(null);
        List<String> tagIdList = new ArrayList<>();
        for (HomeSetting homeSetting : homeSettingList) {
            homeSettingMap.put(homeSetting.getHomeKey(), homeSetting.getHomeValue());
            switch (homeSetting.getHomeKey()) {
                case "tagIds":
                    Collections.addAll(tagIdList, homeSetting.getHomeValue().split(","));
                    break;
                case "links":
                    homeSettingMap.put(homeSetting.getHomeKey(), JSON.parseArray(homeSetting.getHomeValue()));
                    break;
            }
        }
        // 查询标签的名称
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id", tagIdList.toArray());
        List<BbsTags> bbsTagsList = bbsTagsMapper.selectList(wrapper);
        wrapper = new QueryWrapper();
        wrapper.in("tag_id", tagIdList.toArray());
        List<BbsDiscussionTag> bbsDiscussionTags = bbsDiscussionTagMapper.selectList(wrapper);
        List<Integer> discussionIdList = new ArrayList<>();
        for (BbsDiscussionTag bbsDiscussionTag : bbsDiscussionTags) {
            discussionIdList.add(bbsDiscussionTag.getDiscussionId());
        }
        // 查询文章标题
        List<BbsDiscussions> bbsDiscussionsList = new ArrayList<>();
        if (discussionIdList.size()>0) {
            wrapper = new QueryWrapper();
            wrapper.in("id", discussionIdList.toArray());
            bbsDiscussionsList = bbsDiscussionsMapper.selectList(wrapper);
        }
        model.addAttribute("bbsDiscussionsList", bbsDiscussionsList);
        model.addAttribute("bbsTagsList", bbsTagsList);
        model.addAttribute("homeSetting", homeSettingMap);
        return "index";
    }
}
