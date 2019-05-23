package org.unamedgroup.conference.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.dao.UserRepository;
import org.unamedgroup.conference.entity.User;
import org.unamedgroup.conference.entity.temp.FailureInfo;
import org.unamedgroup.conference.entity.temp.SuccessInfo;
import org.unamedgroup.conference.entity.temp.UserInfo;
import org.unamedgroup.conference.service.GeneralService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * UserController
 * 错误代码使用8xxx
 *
 * @author liumengxiao
 * @date 2019/05/23
 */

@Api(value = "用户管理 API", description = "用户管理操作接口", protocols = "http")
@CrossOrigin
@RestController
@RequestMapping("/userManage")

public class UserAdminController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    GeneralService generalService;

    @ApiOperation(value = "列出所有用户")
    @RequestMapping(value = "/listAllUsers", method = RequestMethod.GET)
    @ResponseBody
    public Object listAllUsers() {
        //登录有效性验证
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        } else if (generalService.checkUserGroup() == false) {
            return new FailureInfo(-7);
        }

        try {
            List<User> users = userRepository.findAll();
            List<UserInfo> userList = new ArrayList<UserInfo>();
            for (int i = 0; i < users.size(); ++i) {
                userList.add(new UserInfo(users.get(i)));
            }
            return new SuccessInfo(userList);
        } catch (Exception e) {
            return new FailureInfo(8000, "拉取用户列表失败。");
        }
    }

    @ApiOperation(value = "用户邮箱更改")
    @RequestMapping(value = "/modifyEmail", method = RequestMethod.POST)
    @ResponseBody
    public Object modifyEmail(Integer userID, String email) {
        //登录有效性验证
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        } else if (generalService.checkUserGroup() == false) {
            return new FailureInfo(-7);
        }

        try {
            if (generalService.checkEmail(email) == false) {
                return new FailureInfo(8001, "修改的用户邮箱格式非法。");
            }
            User user = userRepository.getUserByUserID(userID);
            user.setEmail(email);
            userRepository.save(user);

            return new SuccessInfo("用户邮箱更新成功！");
        } catch (Exception e) {
            return new FailureInfo(8002, "用户邮箱更新遇到错误，请检查。");
        }
    }

    @ApiOperation(value = "用户手机更改")
    @RequestMapping(value = "/modifyMobile", method = RequestMethod.POST)
    @ResponseBody
    public Object modifyMobile(Integer userID, String phoneNumber) {
        //登录有效性验证
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() == false) {
            return new FailureInfo();
        } else if (generalService.checkUserGroup() == false) {
            return new FailureInfo(-7);
        }

        try {
            if (generalService.checkMoiblePhone(phoneNumber) == false) {
                return new FailureInfo(8003, "修改的用户手机号码格式非法。");
            }
            User user = userRepository.getUserByUserID(userID);
            user.setPhoneNumber(phoneNumber);
            userRepository.save(user);

            return new SuccessInfo("用户手机更新成功！");
        } catch (Exception e) {
            return new FailureInfo(8005, "用户手机更新遇到错误，请检查。");
        }
    }
}