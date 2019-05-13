package org.unamedgroup.conference.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unamedgroup.conference.dao.ParticipantsRepository;
import org.unamedgroup.conference.entity.Participants;
import org.unamedgroup.conference.service.ManagingAttendeesService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/5/12 22:16
 */
@Service
public class ParticipantsServiceImpl implements ManagingAttendeesService {
    @Autowired
    ParticipantsRepository participantsRepository;

    @Override
    @Transactional
    public Boolean modifyParticipants(String userIds, Integer conferenceID) {
        List<Integer> userIdList = new ArrayList<>();
        String[] userIdArray = userIds.split(",");
        for(int i=0; i<userIdArray.length; i++) {
            userIdList.add(Integer.valueOf(userIdArray[i]));
        }
        try {
            participantsRepository.removeBySequenceID(conferenceID);
            for(Integer userId : userIdList) {
                Participants participants = new Participants();
                participants.setUserID(userId);
                participants.setSequenceID(conferenceID);
                participantsRepository.save(participants);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
