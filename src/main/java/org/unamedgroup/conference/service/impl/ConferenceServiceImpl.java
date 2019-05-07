package org.unamedgroup.conference.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unamedgroup.conference.dao.ConferenceRepository;
import org.unamedgroup.conference.dao.ParticipantsRepository;
import org.unamedgroup.conference.dao.UserRepository;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.Participants;
import org.unamedgroup.conference.entity.User;
import org.unamedgroup.conference.entity.temp.ReturnUser;
import org.unamedgroup.conference.service.ConferenceManageService;
import org.unamedgroup.conference.service.MyConferenceService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/4/11 8:43
 */
@Service
public class ConferenceServiceImpl implements MyConferenceService, ConferenceManageService {
    @Autowired
    ConferenceRepository conferenceRepository;
    @Autowired
    ParticipantsRepository participantsRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public List<Conference> getConferencesByProposer(int userId) {
        return conferenceRepository.getConferencesByUserAndStatus(userId, 1);
    }

    @Override
    public List<Conference> getConferencesByParticipant(int userId) {
        List<Conference> conferenceList = new ArrayList<>();
        List<Participants> participants = participantsRepository.findByUserID(userId);
        for(Participants participant : participants) {
//            Conference conference = conferenceRepository.getConferenceByConferenceID(participant.getSequenceID());
//            if (conference.getStatus() == 1){
//                conferenceList.add(conference);
//            }
            conferenceList.add(conferenceRepository.getConferenceByConferenceIDAndStatus(participant.getSequenceID(), 1));
        }
        return conferenceList;
    }

    @Override
    public List<Conference> getMyConferenceList(int userId, Integer pageCurrent, Integer pageSize) {
        List<Integer> participants = participantsRepository.findSequenceIDByUserID(userId);
        if(participants.size()==0) {
            participants.add(-1);
        }
        List<Conference> conferenceList = conferenceRepository.findMyConference(1, userId, participants, (pageCurrent-1)*pageSize, pageSize);
        return conferenceList;
    }

    @Override
    public Integer getMyConferenceTotal(int userId) {
        List<Integer> participants = participantsRepository.findSequenceIDByUserID(userId);
        if(participants.size()==0) {
            participants.add(-1);
        }
        return conferenceRepository.countMyConference(1, userId, participants);
    }

    @Override
    public List<Conference> getNotStartConferenceList(Integer status) {
        return conferenceRepository.getConferencesByStatus(0);
    }


    @Override
    public List<ReturnUser> getUserListByName(String realName) {
        try {
            List<User> userList = userRepository.findUsersByRealNameLike("%" + realName + "%");
            if (userList.size() == 0) {
                return null;
            }
            List<ReturnUser> returnUsersList = new ArrayList<ReturnUser>();
            for (int i = 0; i < userList.size(); i++) {
                ReturnUser returnUser = new ReturnUser(userList.get(i));
                returnUsersList.add(returnUser);
            }
            return returnUsersList;
        } catch (Exception e) {
            System.err.println("根据姓名匹配用户遇到错误，详细信息：" + e.toString());
            return null;
        }
    }

}
