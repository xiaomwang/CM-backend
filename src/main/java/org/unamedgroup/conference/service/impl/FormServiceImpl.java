package org.unamedgroup.conference.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unamedgroup.conference.dao.ConferenceRepository;
import org.unamedgroup.conference.dao.ParticipantsRepository;
import org.unamedgroup.conference.dao.UserRepository;
import org.unamedgroup.conference.entity.User;
import org.unamedgroup.conference.entity.temp.CountResult;
import org.unamedgroup.conference.entity.temp.UserConferenceCase;
import org.unamedgroup.conference.service.FormService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/6/13 10:26
 */
@Service
public class FormServiceImpl implements FormService {
    @Autowired
    ParticipantsRepository participantsRepository;
    @Autowired
    ConferenceRepository conferenceRepository;
    @Autowired
    UserRepository userRepository;
    @Override
    public UserConferenceCase userConferenceCase() {
        UserConferenceCase userConferenceCase = new UserConferenceCase();
        List<User> userList = userRepository.findAll();
        List<String> nameList = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();
        for(User user : userList) {
            nameList.add(user.getRealName());
            idList.add(user.getUserID());
        }
        int length = nameList.size();
        List<CountResult> passList = conferenceRepository.countPass();
        List<CountResult> partList = participantsRepository.countPart();
        List<CountResult> rejectList = conferenceRepository.countReject();
        List<CountResult> cancelList = conferenceRepository.countCancel();
        List<Integer> passes = new ArrayList<>();
        List<Integer> parts = new ArrayList<>();
        List<Integer> rejects = new ArrayList<>();
        List<Integer> cancels = new ArrayList<>();
        for(int i=0; i<length; i++) {
            passes.add(0);
            parts.add(0);
            rejects.add(0);
            cancels.add(0);
        }
        for(CountResult pass : passList) {
            int index = idList.indexOf(pass.getUser());
            passes.add(index, pass.getCount());
        }
        for(CountResult part : partList) {
            int index = idList.indexOf(part.getUser());
            parts.add(index, part.getCount());
        }
        for(CountResult reject : rejectList) {
            int index = idList.indexOf(reject.getUser());
            rejects.add(index, reject.getCount());
        }
        for(CountResult cancel : cancelList) {
            int index = idList.indexOf(cancel.getUser());
            cancels.add(index, cancel.getCount());
        }
        userConferenceCase.setNameList(nameList);
        userConferenceCase.setPassList(passes);
        userConferenceCase.setPartList(parts);
        userConferenceCase.setRejectList(rejects);
        userConferenceCase.setCancelList(cancels);
        return userConferenceCase;
    }
}
