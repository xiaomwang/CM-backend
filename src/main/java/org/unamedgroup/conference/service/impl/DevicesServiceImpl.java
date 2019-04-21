package org.unamedgroup.conference.service.impl;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unamedgroup.conference.dao.ConferenceRepository;
import org.unamedgroup.conference.dao.RoomRepository;
import org.unamedgroup.conference.dao.UserRepository;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.Room;
import org.unamedgroup.conference.entity.User;
import org.unamedgroup.conference.service.DevicesShowService;


import java.util.Date;
import java.util.List;

/**
 * MeetingService
 *
 * @author liumengxiao
 * @date 2019/03/28
 */

@Component
public class DevicesServiceImpl implements DevicesShowService {
    @Autowired
    ConferenceRepository conferenceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    GeneralServiceImpl generalService;

    @Override
    public Integer getCurrentConferenceID(Integer roomID) {
        List<Conference> conferenceList = null;
        Date current = new Date();

        try {
            conferenceList = generalService.getConferencesByDate(current, current);
            for (int i = 0; i < conferenceList.size(); i = i + 1) {
                Conference conference = conferenceList.get(i);
                if (roomID.equals(conference.getRoom().getRoomID()) && conference.getStatus() == 1) {
                    return conference.getConferenceID();
                }
            }
        } catch (Exception e) {
            e.toString();
        }
        return null;
    }

    @Override
    public String getApplicantName(Integer conferenceID) {
        try {
            Conference conference = conferenceRepository.getConferenceByConferenceID(conferenceID);
            User user = userRepository.getUserByUserID(conference.getUser());
//            User user = conference.getUser();
            return user.getRealName();
        } catch (Exception e) {
            System.err.println("查询申请者错误，详细信息：");
            System.err.println(e.toString());
        }
        return null;
    }

    @Override
    public Date[] getConferenceSchedule(Integer conferenceID) {
        try {
            Date[] date = new Date[2];
            Conference conference = conferenceRepository.getConferenceByConferenceID(conferenceID);
            date[0] = conference.getStartTime();
            date[1] = conference.getEndTime();
            return date;
        } catch (Exception e) {
            System.err.println("读取时间出错，详细信息：");
            System.err.println(e.toString());
        }
        return null;
    }

    @Override
    public Integer getConferenceRoomStatus(Integer roomID) {
        try {
            Room room = roomRepository.getRoomByRoomID(roomID);
            return room.getFlag();
        } catch (Exception e) {
            System.err.println("读取会议室状态错误，详细信息");
            System.err.println(e.toString());
        }
        return null;
    }

    @Override
    public String getConferenceSubject(Integer conferenceID) {
        try {
            Conference conference = conferenceRepository.getConferenceByConferenceID(conferenceID);
            return conference.getSubject();
        } catch (Exception e) {
            System.err.println("读取会议室主题出错，详细信息：");
            System.err.println(e.toString());
        }
        return null;
    }


}
