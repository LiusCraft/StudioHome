package org.liuscraft.gzs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.tomcat.util.json.JSONParser;
import org.liuscraft.gzs.mapper.*;
import org.liuscraft.gzs.pojo.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    SlideMapper slideMapper;

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
        HashMap<Integer, Integer[]> tagIds = new HashMap<>();
        for (BbsDiscussionTag bbsDiscussionTag : bbsDiscussionTags) {
            List<Integer> discussionIdList = new ArrayList<>();
            discussionIdList.add(bbsDiscussionTag.getDiscussionId());
            if (tagIds.get(bbsDiscussionTag.getTagId())!=null){
                discussionIdList.addAll(Arrays.asList(tagIds.get(bbsDiscussionTag.getTagId())));
            }
            //System.out.println(discussionIdList.size());
            Integer[] discussionIds = new Integer[discussionIdList.size()];
            discussionIdList.toArray(discussionIds);
            tagIds.put(bbsDiscussionTag.getTagId(), discussionIds);
        }
        // 查询文章标题
        List<HashMap<String, Object>> bbsList = new ArrayList<>();
        for (Map.Entry<Integer, Integer[]> integerEntry : tagIds.entrySet()) {
            HashMap<String, Object> bbs = new HashMap<>();
            List<BbsDiscussions> bbsDiscussionsList = new ArrayList<>();
            bbs.put("tagId", integerEntry.getKey());
            if (integerEntry.getValue().length>0) {

                wrapper = new QueryWrapper();
                Integer[] ids = new Integer[integerEntry.getValue().length];
                Arrays.asList(integerEntry.getValue()).toArray(ids);
                wrapper.in("id", ids);
                bbsDiscussionsList = bbsDiscussionsMapper.selectList(wrapper);
            }
            bbs.put("discussionsList", bbsDiscussionsList);
            bbsList.add(bbs);
        }
        // 查询成员
        List<Member> memberList = memberMapper.selectList(null);
        List<Slide> slideList = slideMapper.selectList(null);

        model.addAttribute("bbsList", bbsList);
        model.addAttribute("bbsTagsList", bbsTagsList);
        model.addAttribute("homeSetting", homeSettingMap);
        model.addAttribute("memberList", memberList);
        model.addAttribute("slideList", slideList);
        return "index";
    }
}
