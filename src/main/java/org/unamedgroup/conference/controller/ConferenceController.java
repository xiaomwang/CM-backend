package org.unamedgroup.conference.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.dao.ConferenceRepository;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.temp.FailureInfo;
import org.unamedgroup.conference.entity.temp.SuccessInfo;
import org.unamedgroup.conference.service.MyConferenceService;

import java.util.List;

/**
 * 错误代码使用3xxx
 *
 * @Author: 白振宇
 * @Date： 2019/3/15 14:08
 */

@CrossOrigin
@RestController
@RequestMapping(value = "conference")
public class    ConferenceController {

    @Autowired
    ConferenceRepository conferenceRepository;
    @Autowired
    MyConferenceService myConferenceService;

    /**
     * 预定会议
     * 前端传入一个完整的会议对象，后端写库操作
     *
     * @param conference
     * @return 预定是否成功
     * @author liumengxiao
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public Object orderConference(Conference conference) {
        //身份信息认证
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }

        //会议预定逻辑
        try {
            if (conference == null) {
                return new FailureInfo(3000, "传入的会议参数无效！");
            } else {
                //当前会议时间是否有会，如果无会议，继续
                if (conferenceRepository.getConferencesByStartTimeBeforeAndEndTimeAfter(conference.getStartTime(), conference.getEndTime()).size() == 0) {
                    conferenceRepository.save(conference);
                    return new SuccessInfo("会议预定成功！");
                } else {
                    return new FailureInfo(3001, "当前时段有会，无法预订！");
                }
            }
        } catch (Exception e) {
            System.err.println("设置错误，详情：");
            System.err.println(e.toString());
            return new FailureInfo(3100, "预定会议出现未知错误，详情：" + e.toString());
        }
    }

    @GetMapping(value = "/details")
    public Object getDetatils(Integer userId) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }
        List<Conference> conferenceList = myConferenceService.getMyConferenceList(userId);
        if (conferenceList==null) {
            return new FailureInfo(3101, "个人会议信息详情拉取失败");
        } else {
            return new SuccessInfo(conferenceList);
        }
    }
}
