package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.temp.UserConferenceCase;
import org.unamedgroup.conference.entity.temp.UserConferenceOne;

/**
 * @Author: 白振宇
 * @Date： 2019/6/13 10:26
 */
public interface FormService {
    UserConferenceCase userConferenceCase();
    UserConferenceOne userConferenceOne(Integer user);
}
