package org.unamedgroup.conference.controller;

import com.auth0.jwt.JWT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.unamedgroup.conference.entity.temp.UserInfo;
import org.unamedgroup.conference.security.JWTUtil;
import org.unamedgroup.conference.security.UnauthorizedException;
import org.unamedgroup.conference.service.GeneralService;
import org.unamedgroup.conference.service.MyConferenceService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UserController
 * 错误代码使用7xxx
 *
 * @author liumengxiao
 * @date 2019/03/12
 */

@Api(value = "用户 API", description = "用户操作接口", protocols = "http")
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ConferenceRepository conferenceRepository;
    @Autowired
    MyConferenceService myConferenceService;
    @Autowired
    GeneralService generalService;

    @ApiOperation(value = "获取当前用户的详细用户信息")
    @RequestMapping(value = "/currentUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public Object currentUserInfo() {
        //登录有效性验证
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }

        try {
            UserInfo userInfo = new UserInfo(generalService.getLoginUser());
            return new SuccessInfo(userInfo);
        } catch (Exception e) {
            return new FailureInfo(7003, "获取用户详细信息失败。");
        }

    }

    @ApiOperation(value = "我的会议信息api")
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

    @ApiOperation(value = "用户登录api")
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

    @ApiOperation(value = "获取用户IDapi")
    @RequestMapping(value = "/userID", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserID() {
        //登录有效性验证
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }

        try {
            User user = generalService.getLoginUser();
            return new SuccessInfo(user.getUserID());
        } catch (Exception e) {
            return new FailureInfo(-2, "获取用户ID信息失败。");
        }
    }

    @ApiOperation(value = "用户姓名模糊匹配api")
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserListByName(String realName) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }

        List list = myConferenceService.getUserListByName(realName);
        if (list == null) {
            return new FailureInfo(7002, "根据姓名匹配用户失败！");
        } else {
            return new SuccessInfo(list);
        }
    }

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    public Object signup(String password, String realName, String department, String email, String phoneNumber, Integer userGroup) {
        try {
            // 密码相关的逻辑验证
            if (password == null || password.equals("")) {
                return new FailureInfo(7100, "密码为空，请重试！");
            }
            if (password.length() < 6) {
                return new FailureInfo(7101, "密码长度小于6，请更换一个长一点的密码。");
            }


            if (userRepository.getUserByEmail(email) != null) {
                return new FailureInfo(7103, "该邮箱已注册，不可使用！");
            }
            if (generalService.checkEmail(email) != true) {
                return new FailureInfo(7102, "邮箱格式不正确");
            }

            if (userRepository.getUserByPhoneNumber(phoneNumber) != null) {
                return new FailureInfo(7105, "该手机号码已注册，不可使用！");
            }
            if (generalService.checkMoiblePhone(phoneNumber) != true) {
                return new FailureInfo(7104, "手机号码格式不正确");
            }

            User newUser = new User(password, realName, department, email, phoneNumber, userGroup, null);
            userRepository.save(newUser);

            return new SuccessInfo(newUser.getUserID());

        } catch (Exception e) {
            System.err.println("遇到错误，请核实：");
            System.err.println(e.toString());
            return new FailureInfo(7005, "注册遇到未知错误，请通知管理员检查系统日志，谢谢！");
        }
    }


    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ResponseBody
    public Object changePassword(String oldPassword, String newPassword) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }

        try {
            User user = generalService.getLoginUser();
            if (user.getPassword().equals(oldPassword) == false) {
                return new FailureInfo(7003, "原密码不正确。");
            }

            if (newPassword.length() < 6) {
                return new FailureInfo(7101, "密码长度小于6，请更换一个长一点的密码。");
            }

            user.setPassword(newPassword);
            userRepository.save(user);
            return new SuccessInfo("密码修改成功！");
        } catch (Exception e) {
            System.err.println("密码修改失败！");
            System.err.println(e.toString());
            return new FailureInfo(7004, "密码修改遇到未知错误。");
        }
    }

    @ApiOperation(value = "更新用户个人信息")
    @RequestMapping(value = "/currentUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object updateCurrentUserInfo(String email, String phoneNumber) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }

        try {
            User user = generalService.getLoginUser();
            if (userRepository.getUserByEmail(email) != null) {
                return new FailureInfo(7103, "该邮箱已注册，不可使用！");
            }
            if (generalService.checkEmail(email) != true) {
                return new FailureInfo(7102, "邮箱格式不正确");
            }
            user.setEmail(email);
            if (userRepository.getUserByPhoneNumber(phoneNumber) != null) {
                return new FailureInfo(7105, "该手机号码已注册，不可使用！");
            }
            if (generalService.checkMoiblePhone(phoneNumber) != true) {
                return new FailureInfo(7104, "手机号码格式不正确");
            }
            user.setPhoneNumber(phoneNumber);
            userRepository.save(user);
            return new SuccessInfo("个人信息更新成功！");
        } catch (Exception e) {
            System.err.println("个人信息更新失败！");
            System.err.println(e.toString());
            return new FailureInfo(7005, "个人信息更新遇到未知错误。");
        }
    }
}
