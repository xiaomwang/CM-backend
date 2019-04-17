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
public class ConferenceController {

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
                // 当前会议时间是否有会，如果无会议，继续
                List<Conference> conferenceList1 = conferenceRepository.findConferencesByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(conference.getStartTime(), conference.getStartTime());
                List<Conference> conferenceList2 = conferenceRepository.findConferencesByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(conference.getEndTime(), conference.getEndTime());
                List<Conference> conferenceList3 = conferenceRepository.findConferencesByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(conference.getStartTime(), conference.getEndTime());
                // 去重去并集
                conferenceList2.removeAll(conferenceList1);
                conferenceList1.addAll(conferenceList2);
                conferenceList3.removeAll(conferenceList1);
                conferenceList1.addAll(conferenceList3);

                // 如果当前时间段没有会议直接写数据库预定成功
                if (conferenceList1.size() != 0) {
                    // 如果当前时间段有回忆则检查这些会议是否都被驳回，如果有未被驳回的无法约定
                    for (int i = 0; i < conferenceList1.size(); i++) {
                        if (conferenceList1.get(i).getStatus() == 1) {
                            return new FailureInfo(3001, "当前时段有会，无法预订！");
                        }
                    }
                }
                conferenceRepository.save(conference);
                return new SuccessInfo("会议预定成功！");
            }
        } catch (Exception e) {
            System.err.println("设置错误，详情：");
            System.err.println(e.toString());
            return new FailureInfo(3100, "预定会议出现未知错误，详情：" + e.toString());
        }
    }

    @GetMapping(value = "/details")
    public List<Conference> getDetatils(Integer userId) {
        return myConferenceService.getMyConferenceList(userId);
    }
}
