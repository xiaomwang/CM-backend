package org.unamedgroup.conference.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.dao.ConferenceRepository;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.service.MyConferenceService;

import java.util.List;

/**
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
    public Conference orderConference(Conference conference) {
        Boolean status = false;
        try {
            if (conference == null) {
                throw new NullPointerException();
            } else {
                //当前会议时间是否有会，如果无会议，继续
                if (conferenceRepository.getConferencesByStartTimeBeforeAndEndTimeAfter(conference.getStartTime(), conference.getEndTime()).size() == 0) {
                    conferenceRepository.save(conference);
                    status = true;
                } else {
                    System.err.println("当前时间段有会！");
//                    return false;
                }
            }
//            return status;
            return conference;
        } catch (Exception e) {
            System.err.println("设置错误，详情：");
            System.err.println(e.toString());
        }
//        return status;
        return conference;
    }

    @GetMapping(value = "/details")
    public List<Conference> getDetatils(Integer userId) {
        return myConferenceService.getMyConferenceList(userId);
    }
}
