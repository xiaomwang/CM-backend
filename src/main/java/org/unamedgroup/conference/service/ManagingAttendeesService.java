package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.Participants;

import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/5/12 22:18
 */
public interface ManagingAttendeesService {
    Boolean modifyParticipants(String userIds, Integer conferenceID);
}
