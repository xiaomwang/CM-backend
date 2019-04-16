package org.unamedgroup.conference.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unamedgroup.conference.dao.ConferenceRepository;
import org.unamedgroup.conference.dao.ParticipantsRepository;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.Participants;
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

    @Override
    public List<Conference> getConferencesByProposer(int userId) {
        return conferenceRepository.getConferencesByUserAndStatus(userId, 1);
    }

    @Override
    public List<Conference> getConferencesByParticipant(int userId) {
        List<Conference> conferenceList = new ArrayList<>();
        List<Participants> participants = participantsRepository.findByUserID(userId);
        for(Participants participant : participants) {
            conferenceList.add(conferenceRepository.getConferenceByParticipantSequenceAndStatus(participant.getSequenceID(), 1));
        }
        return conferenceList;
    }

//    @Override
//    public List<Conference> getMyConferenceList(int userId) {
//        List<Conference> listProposer = getConferencesByProposer(userId);
//        List<Conference> listParticipant = getConferencesByParticipant(userId);
//        if(listParticipant!=null) {
//            listProposer.addAll(listProposer.size(),listParticipant);
//        }
//        return listProposer;
//    }

    @Override
    public List<Conference> getMyConferenceList(int userId) {
        List<Integer> participants = participantsRepository.findSequenceIDByUserID(userId);
        List<Conference> conferenceList = conferenceRepository.findMyConference(1, userId, participants);
        return conferenceList;
    }

    @Override
    public List<Conference> getNotStartConferenceList(Integer status) {
        return conferenceRepository.getConferencesByStatus(0);
    }
}
