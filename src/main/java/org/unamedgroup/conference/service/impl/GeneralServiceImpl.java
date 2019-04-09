package org.unamedgroup.conference.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unamedgroup.conference.dao.RoomRepository;
import org.unamedgroup.conference.entity.Room;
import org.unamedgroup.conference.service.GeneralService;

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
}
