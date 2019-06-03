package org.unamedgroup.conference.controller;

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
import org.unamedgroup.conference.service.GeneralService;
import org.unamedgroup.conference.service.MyConferenceService;

import java.util.List;

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
            // 根据token确定当前登录用户
            UserInfo userInfo = new UserInfo(generalService.getLoginUser());
            // 返回当前用户的信息（通过新的构造型，避免返回密码等信息）
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
            // 根据用户ID检索对应的会议并返回
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
            // 根据用户提交的手机号码作为索引定位到唯一用户
            User user = userRepository.getUserByPhoneNumber(phoneNumber);
            // 定位不到则返回手机号码对应的用户不存在
            if (user == null) {
                return new FailureInfo(-1, "用户名不存在！");
            }
            //用户名密码是否匹配
            if (User.getPasswordHash(password).equals(user.getPassword())) {
                // 密码匹配则返回一个token作为身份凭证
                String token = JWTUtil.generateToken(phoneNumber, User.getPasswordHash(password));
                return new SuccessInfo(token);
            } else {
                // 否则返回用户名密码不匹配
                return new FailureInfo(-1, "用户名密码不匹配！");
            }
        } catch (Exception e) {
            System.err.println("用户名密码验证出现异常！详细信息：");
            System.err.println(e.toString());
        }
        return new FailureInfo();
    }

//    @RequestMapping(value = "/test", method = RequestMethod.GET)
//    @ResponseBody
//    public Object test() {
//        Subject subject = SecurityUtils.getSubject();
//        if (subject.isAuthenticated() == false) {
//            return new FailureInfo();
//        }
//        return new SuccessInfo("success!");
//    }

    @ApiOperation(value = "获取用户ID")
    @RequestMapping(value = "/userID", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserID() {
        //登录有效性验证
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }

        try {
            // 获取当前登录用户，并返回其ID
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
        // 登录有效性校验
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }
        // 根据姓名获取用户
        List list = myConferenceService.getUserListByName(realName);
        // 为空则匹配失败
        if (list == null) {
            return new FailureInfo(7002, "根据姓名匹配用户失败！");
        } else {
            // 不为空则返回该对象
            return new SuccessInfo(list);
        }
    }

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    public Object signup(String password, String realName, String department, String email, String phoneNumber, Integer userGroup) {
        try {
            // 邮箱是否已经注册
            if (userRepository.getUserByEmail(email) != null) {
                return new FailureInfo(7103, "该邮箱已注册，不可使用！");
            }
            // 邮箱格式正则匹配格式
            if (generalService.checkEmail(email) != true) {
                return new FailureInfo(7102, "邮箱格式不正确");
            }
            // 手机号码是否注册
            if (userRepository.getUserByPhoneNumber(phoneNumber) != null) {
                return new FailureInfo(7105, "该手机号码已注册，不可使用！");
            }
            // 手机号码正则匹配格式
            if (generalService.checkMoiblePhone(phoneNumber) != true) {
                return new FailureInfo(7104, "手机号码格式不正确");
            }

            User newUser = new User(User.getPasswordHash(password), realName, department, email, phoneNumber, userGroup, null);
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
        // 通过是否是32位简单验证是否是MD5加密之后的结果
        if (oldPassword.length() != 32 || newPassword.length() != 32) {
            return new FailureInfo(7006, "原密码格式不正确。");
        }

        try {
            // 获取当前登录用户
            User user = generalService.getLoginUser();
            // 检验旧密码是否正确，如果不正确返回提示
            if (user.getPassword().equals(User.getPasswordHash(oldPassword)) == false) {
                return new FailureInfo(7003, "原密码不正确。");
            }
            // 通过检验的话新密码加入数据库
            user.setPassword(User.getPasswordHash(newPassword));
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
        // 登录有效性验证
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        }
        // 邮箱和电话标记，分别标注输入的是否是当前的手机/邮箱
        // 用来判断是否更改数据库。
        Boolean emailFlag = true;
        Boolean phoneNumberFlag = true;

        try {
            User user = generalService.getLoginUser();
            // 判断是否有所变更
            if (user.getPhoneNumber().equals(phoneNumber)) {
                phoneNumberFlag = false;
            }
            if (user.getEmail().equals(email)) {
                emailFlag = false;
            }
            // 是当前用户则没有不提示已注册
            // 不是当前用户且能找到当前邮箱用户，则提示邮箱已注册
            if (emailFlag && userRepository.getUserByEmail(email) != null) {
                return new FailureInfo(7103, "该邮箱已注册，不可使用！");
            }
            if (generalService.checkEmail(email) != true) {
                return new FailureInfo(7102, "邮箱格式不正确");
            }
            // 邮箱写入对象属性
            user.setEmail(email);

            // 是当前用户则没有不提示已注册
            // 不是当前用户且能找到当前手机用户，则提示邮箱已注册
            if (phoneNumberFlag && userRepository.getUserByPhoneNumber(phoneNumber) != null) {
                return new FailureInfo(7105, "该手机号码已注册，不可使用！");
            }
            if (generalService.checkMoiblePhone(phoneNumber) != true) {
                return new FailureInfo(7104, "手机号码格式不正确");
            }
            // 手机写入对象属性
            user.setPhoneNumber(phoneNumber);
            userRepository.save(user);
            return new SuccessInfo("个人信息更新成功！");
        } catch (Exception e) {
            System.err.println("个人信息更新失败！");
            System.err.println(e.toString());
            return new FailureInfo(7005, "个人信息更新遇到未知错误。");
        }
    }

    @ApiOperation(value = "获取所有的用户组")
    @RequestMapping(value = "/userGroup", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserGroup() {
        try {
            return new SuccessInfo(User.USERGROUP);
        } catch (Exception e) {
            return new FailureInfo(7006, "获取用户组失败。");
        }

    }

}
