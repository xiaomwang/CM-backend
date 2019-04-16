package org.unamedgroup.conference.controller;

import com.auth0.jwt.JWT;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.dao.ConferenceRepository;
import org.unamedgroup.conference.dao.UserRepository;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.User;
import org.unamedgroup.conference.entity.temp.FailureInfo;
import org.unamedgroup.conference.entity.temp.SuccessInfo;
import org.unamedgroup.conference.security.JWTUtil;
import org.unamedgroup.conference.security.UnauthorizedException;

import java.util.List;

/**
 * UserController
 * 错误代码使用7xxx
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
    public Object getMyConferences(Integer userID) {
        //登录有效性验证
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }

        List<Conference> myConferences;
        try {
            myConferences = conferenceRepository.getConferencesByUser(userID);
            return new SuccessInfo(myConferences);
        } catch (Exception e) {
            System.err.println("根据用户信息获取会议信息错误，请核实。详细信息：");
            System.err.println(e.toString());
            return new FailureInfo(7000, "根据用户信息获取会议信息错误，请核实!");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(String phoneNumber, String password) {
        try {
            User user = userRepository.getUserByPhoneNumber(phoneNumber);
            if (user == null) {
                return new FailureInfo(-1, "用户名不存在！");
            }
            //用户名密码是否匹配
            if (password.equals(user.getPassword())) {
                String token = JWTUtil.generateToken(phoneNumber, user.getPasswordHash());
                return new SuccessInfo(token);
            } else {
                return new FailureInfo(-1, "用户名密码不匹配！");
            }
        } catch (Exception e) {
            System.err.println("用户名密码验证出现异常！详细信息：");
            System.err.println(e.toString());
        }
        return new FailureInfo();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public Object test() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }
        return new SuccessInfo("success!");
    }

    @RequestMapping(value = "/userID", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserID() {
        String token = null;
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated() == true) {
                token = subject.getPrincipal().toString();
            } else {
                throw new UnauthorizedException();
            }
        } catch (Exception e) {
            return new FailureInfo(-2, e.toString());
        }
        User user = userRepository.getUserByPhoneNumber(JWTUtil.getPhoneNumber(token));
        return new SuccessInfo(user.getUserID());
    }
}
