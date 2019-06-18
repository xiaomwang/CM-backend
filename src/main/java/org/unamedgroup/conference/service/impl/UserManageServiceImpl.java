package org.unamedgroup.conference.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unamedgroup.conference.dao.UserRepository;
import org.unamedgroup.conference.entity.User;
import org.unamedgroup.conference.service.UserManageService;

/**
 * GetUserInfoImpl
 *
 * @author liumengxiao
 * @date 2019/03/12
 */

@Component
public class UserManageServiceImpl implements UserManageService {
    @Autowired
    UserRepository userRepository;

    @Override
    public String getUserFeature(Integer userID) {
        try {
            User user = userRepository.getUserByUserID(userID);
            return user.getFeature();
        } catch (Exception e) {
            System.out.println("读取用户人脸特征信息时出现异常，详细如下："+ e.toString());
            e.toString();
        }
        return null;
    }

    @Override
    public void setUserFeature(Integer userID, String feature) {
        try {
            User user = userRepository.getUserByUserID(userID);
            user.setFeature(feature);
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println("设置用户人脸特征信息时出现异常，详细如下：");
            e.toString();
        }
        return;
    }

    @Override
    public Integer register(String password, String realName, String department, String email, String phoneNumber, Integer userGroup) {
        try {
            // 初始化注册时候建立一个没有人脸特征的对象
            User newUser = new User(password, realName, department, email, phoneNumber, userGroup, null);
            // 取得该用户的用户ID并返回
            Integer userID = newUser.userID;
            return userID;
        } catch (Exception e) {
            System.out.println("新建用户时发生异常，详细如下：");
            e.toString();
        }
        return null;
    }


}
