package org.unamedgroup.conference.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.unamedgroup.conference.entity.Building;
import org.unamedgroup.conference.entity.Room;
import org.unamedgroup.conference.service.GuideQueryService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author: 白振宇
 * @Date： 2019/5/15 10:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoomServiceImplTest {

    @Autowired
    GuideQueryService guideQueryService;

    @Test
    public void screenRoomList() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse("2019-05-21 20:00:00");
        Date end = sdf.parse("2019-05-21 23:00:00");
        Room room = new Room();
        Building building = new Building();
        building.setAddress("-1");
        building.setBuildingID(-1);
        room.setBuilding(building);
        room.setLocation("-1");
        room.setCapacity(-1);
        room.setCatalogue("-1");
        List<Room> roomList = guideQueryService.screenRoomList(room);
        System.out.println(guideQueryService.sortRoomByFreeIndex(roomList, start, end));
    }
}