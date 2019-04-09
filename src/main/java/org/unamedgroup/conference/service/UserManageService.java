package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.User;
import org.springframework.stereotype.Component;


/**
 * UserInfo: 用户的信息相关业务逻辑处理器
 *
 * @author liumengxiao
 * @date 2019/03/12
 */
public interface UserManageService {

    /**
     * 返回用户的生物特征信息
     *
     * @param userID
     * @return String
     */
    public String getUserFeature(Integer userID);

    /**
     * 设置用户的生物特征信息
     *
     * @param userID
     * @param feature
     */
    public void setUserFeature(Integer userID, String feature);

    /**
     * 新用户通过表单（无人脸信息）注册接口
     *
     * @param password
     * @param realName
     * @param department
     * @param email
     * @param phoneNumber
     * @param userGroup
     * @return userID
     */
    public Integer register(String password, String realName, String department, String email, String phoneNumber, Integer userGroup);
}
