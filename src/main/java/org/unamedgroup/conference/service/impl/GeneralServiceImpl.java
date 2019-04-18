package org.unamedgroup.conference.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unamedgroup.conference.dao.ConferenceRepository;
import org.unamedgroup.conference.dao.RoomRepository;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.Room;
import org.unamedgroup.conference.service.GeneralService;

import java.util.Date;
import java.util.List;

/**
 * GeneralServiceImpl
 *
 * @author liumengxiao
 * @date 2019/04/09
 */

@Component
public class GeneralServiceImpl implements GeneralService {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ConferenceRepository conferenceRepository;

    @Override
    public String getRoomNameByID(Integer roomID) {
        String name = null;
        try {
            Room room = roomRepository.getRoomByRoomID(roomID);
            name = room.getName();
            return name;
        } catch (Exception e) {
            System.err.println("根据ID查询房间名错误，详细错误：");
            System.err.println(e.toString());
        }
        return null;
    }

    @Override
    public List<Conference> getConferencesByDate(Date start, Date end) {
        try {
            // 当前会议时间是否有会，如果无会议，继续
            List<Conference> conferenceList1 = conferenceRepository.findConferencesByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(start, start);
            List<Conference> conferenceList2 = conferenceRepository.findConferencesByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(end, end);
            List<Conference> conferenceList3 = conferenceRepository.findConferencesByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(start, end);
            List<Conference> conferenceList4 = conferenceRepository.findConferencesByStartTimeGreaterThanEqualAndEndTimeLessThanEqual(start, end);

            // 去重去并集
            conferenceList2.removeAll(conferenceList1);
            conferenceList1.addAll(conferenceList2);
            conferenceList3.removeAll(conferenceList1);
            conferenceList1.addAll(conferenceList3);
            conferenceList4.removeAll(conferenceList1);
            conferenceList1.addAll(conferenceList4);

            return conferenceList1;
        } catch (Exception e) {
            System.err.println("根据时间段查询会议遇到问题！详情：" + e.toString());
            return null;
        }
    }


}
