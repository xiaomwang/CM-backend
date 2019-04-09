package org.unamedgroup.conference.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.dao.ConferenceRepository;
import org.unamedgroup.conference.dao.UserRepository;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.User;

import java.util.List;

/**
 * UserController
 *
 * @author liumengxiao
 * @date 2019/03/12
 */

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConferenceRepository conferenceRepository;

    @RequestMapping(value = "/myConferences", method = RequestMethod.GET)
    @ResponseBody
    public List<Conference> getMyConferences(Integer userID) {
        List<Conference> myConferences;
        try {
            myConferences = conferenceRepository.getConferencesByUser(userID);
            return myConferences;
        } catch (Exception e) {
            System.err.println("根据用户信息获取会议信息错误，请核实。详细信息：");
            System.err.println(e.toString());
        }
        return null;
    }

}
