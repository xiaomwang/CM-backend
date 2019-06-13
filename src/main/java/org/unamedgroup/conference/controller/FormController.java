package org.unamedgroup.conference.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unamedgroup.conference.entity.temp.FailureInfo;
import org.unamedgroup.conference.entity.temp.SuccessInfo;
import org.unamedgroup.conference.service.FormService;

/**
 * @Author: 白振宇
 * @Date： 2019/6/13 11:12
 */
@RestController
@RequestMapping(value = "/form")
public class FormController {
    @Autowired
    FormService formService;
    @GetMapping(value = "/users/conference")
    public Object usersConference() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new FailureInfo();
        }
        try {
            return new SuccessInfo(formService.userConferenceCase());
        } catch (Exception e) {
            e.printStackTrace();
            return new FailureInfo(9001, "获取用户会议情况报表失败！");
        }
    }
}
