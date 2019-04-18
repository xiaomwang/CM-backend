package org.unamedgroup.conference.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unamedgroup.conference.dao.BuildingRepository;
import org.unamedgroup.conference.dao.ConferenceRepository;
import org.unamedgroup.conference.dao.RoomRepository;
import org.unamedgroup.conference.entity.Building;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.Room;
import org.unamedgroup.conference.entity.temp.RoomTime;
import org.unamedgroup.conference.service.GeneralService;
import org.unamedgroup.conference.service.GuideQueryService;
import org.unamedgroup.conference.service.QuickCheckService;
import org.unamedgroup.conference.service.RelevanceQueryService;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * RoomService
 *
 * @author liumengxiao
 * @date 2019/03/28
 */

@Component
public class RoomServiceImpl implements QuickCheckService, GuideQueryService, RelevanceQueryService {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    ConferenceRepository conferenceRepository;
    @Autowired
    GeneralService generalService;

    @Override
    public List<Room> getRoomsByBuildingID(Integer buildingID) {
        List<Room> roomList = null;
        try {
            Building building = buildingRepository.getBuildingByBuildingID(buildingID);
            roomList = roomRepository.getRoomsByBuilding(building);
            return roomList;
        } catch (Exception e) {
            System.err.println("根据楼层编号查询房间遇到问题，请核实。详细信息：");
            System.err.println(e.toString());
        }
        return null;
    }


    @Override
    public List<Room> getConferenceList(Building building, Integer roomId) {
        List<Room> rooms = new ArrayList<>();
        /*if-else进行输入判断，若不符合要求则返回null*/
        if (roomId == -1) {
            if (building.getBuildingID() == -1) {
                /*只有日期条件*/
                /*获取会议室和会议信息*/
                rooms = (List<Room>) roomRepository.findAll();
            } else {
                /*有日期条件和楼宇条件*/
                /*获取会议室和会议信息*/
                rooms = (List<Room>) roomRepository.getRoomsByBuilding(building);
            }
        } else if (building.getBuildingID() != -1) {
            /*日期、楼宇、会议室三个条件都有*/
            /*获取会议室和会议信息*/
            rooms.add(roomRepository.getRoomByRoomID(roomId));
        } else {
            /*有日期和会议室条件但是没有楼宇条件*/
            return null;
        }
        return rooms;
    }

    @Override
    public List<RoomTime> handleRoomTime(String date, Building building, Integer roomId) {
        if (date == null || building == null || roomId == null) {
            return null;
        }
        /*规定正则表达式*/
        String regular = "\\d{4}-\\d{2}-\\d{2}";
        /*根据传入日期计算起始时间和结束时间*/
        String start, end;
        /*日期参数的有限性检验*/
        if (!date.substring(0, 10).matches(regular)) {
            return null;
        } else {
            start = date.substring(0, 10) + " 00:00:00";
        }
        /*传入为日期的其实时间即00:00:00，将其转换为改日会议最后一个时间节点即23:30:00*/
        end = start.replaceAll("00:00:00", "23:30:00");
        /*规定日期转换格式*/
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        /*将字符串起止时间转化为时间戳格式*/
        Timestamp startDate = null, endDate = null;
        try {
            startDate = new Timestamp(format.parse(start).getTime());
            endDate = new Timestamp(format.parse(end).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Room> roomList = getConferenceList(building, roomId);
        if (roomList == null) {
            return null;
        }
        /*遍历会议室并将每个会议室的会议列表分开存放哈希中*/
        Map<Room, List<Conference>> mapStart = new HashMap<>(16);
        Map<Room, List<Conference>> mapEnd = new HashMap<>(16);
        for (Room room : roomList) {
            mapStart.put(room, conferenceRepository.findByRoomAndStatusAndStartTimeBetween(room, 1, startDate, endDate));
            mapEnd.put(room, conferenceRepository.findByRoomAndStatusAndEndTimeBetweenAndStartTimeBefore(room, 1, startDate, endDate, startDate));
        }
        /*用来存放全部会议室占用信息*/
        List<RoomTime> list = new ArrayList<>();
        /*遍历会议列表信息*/
        for (Map.Entry<Room, List<Conference>> m : mapStart.entrySet()) {
            RoomTime roomTime = new RoomTime();
            list.add(timeListUtil(true, m, roomTime));
        }
        int index = 0;
        for (Map.Entry<Room, List<Conference>> m : mapEnd.entrySet()) {
            RoomTime roomTime = list.get(index);
            list.remove(index);
            list.add(index, timeListUtil(false, m, roomTime));
            index++;
        }
        return list;
    }

    @Override
    public List<Building> getAllBuildings() {
        List<Building> buildingList = null;
        try {
            buildingList = buildingRepository.findAll();
            return buildingList;
        } catch (Exception e) {
            System.err.println("查询所有楼层出错！详细信息：");
            System.err.println(e.toString());
        }
        return null;
    }

    private RoomTime timeCutUtil(RoomTime roomTime, String startTime, String endTime) {
        /*将会议持续时间段切为以30分钟为间隔的小时间段，并存入时间段列表*/
        if ("23:30".equals(endTime)) {
            roomTime.setTwentyThreeHalf(true);
        }
        while (!startTime.equals(endTime)) {
            if ("00:00".equals(startTime)) {
                roomTime.setZeroFull(true);
            } else if ("00:30".equals(startTime)) {
                roomTime.setZeroHalf(true);
            } else if ("01:00".equals(startTime)) {
                roomTime.setOneFull(true);
            } else if ("01:30".equals(startTime)) {
                roomTime.setOneHalf(true);
            } else if ("02:00".equals(startTime)) {
                roomTime.setTwoFull(true);
            } else if ("02:30".equals(startTime)) {
                roomTime.setTwoHalf(true);
            } else if ("03:00".equals(startTime)) {
                roomTime.setThreeFull(true);
            } else if ("03:30".equals(startTime)) {
                roomTime.setThreeHalf(true);
            } else if ("04:00".equals(startTime)) {
                roomTime.setFourFull(true);
            } else if ("04:30".equals(startTime)) {
                roomTime.setFourHalf(true);
            } else if ("05:00".equals(startTime)) {
                roomTime.setFiveFull(true);
            } else if ("05:30".equals(startTime)) {
                roomTime.setFiveHalf(true);
            } else if ("06:00".equals(startTime)) {
                roomTime.setSixFull(true);
            } else if ("06:30".equals(startTime)) {
                roomTime.setSixHalf(true);
            } else if ("07:00".equals(startTime)) {
                roomTime.setSevenFull(true);
            } else if ("07:30".equals(startTime)) {
                roomTime.setSevenHalf(true);
            } else if ("08:00".equals(startTime)) {
                roomTime.setEightFull(true);
            } else if ("08:30".equals(startTime)) {
                roomTime.setEightHalf(true);
            } else if ("09:00".equals(startTime)) {
                roomTime.setNineFull(true);
            } else if ("09:30".equals(startTime)) {
                roomTime.setNineHalf(true);
            } else if ("10:00".equals(startTime)) {
                roomTime.setTenFull(true);
            } else if ("10:30".equals(startTime)) {
                roomTime.setTenHalf(true);
            } else if ("11:00".equals(startTime)) {
                roomTime.setElevenFull(true);
            } else if ("11:30".equals(startTime)) {
                roomTime.setElevenHalf(true);
            } else if ("12:00".equals(startTime)) {
                roomTime.setTwelveFull(true);
            } else if ("12:30".equals(startTime)) {
                roomTime.setTwelveHalf(true);
            } else if ("13:00".equals(startTime)) {
                roomTime.setThirteenFull(true);
            } else if ("13:30".equals(startTime)) {
                roomTime.setThirteenHalf(true);
            } else if ("14:00".equals(startTime)) {
                roomTime.setFourteenFull(true);
            } else if ("14:30".equals(startTime)) {
                roomTime.setFourteenHalf(true);
            } else if ("15:00".equals(startTime)) {
                roomTime.setFifteenFull(true);
            } else if ("15:30".equals(startTime)) {
                roomTime.setFifteenHalf(true);
            } else if ("16:00".equals(startTime)) {
                roomTime.setSixteenFull(true);
            } else if ("16:30".equals(startTime)) {
                roomTime.setSixteenHalf(true);
            } else if ("17:00".equals(startTime)) {
                roomTime.setSeventeenFull(true);
            } else if ("17:30".equals(startTime)) {
                roomTime.setSeventeenHalf(true);
            } else if ("18:00".equals(startTime)) {
                roomTime.setEighteenthFull(true);
            } else if ("18:30".equals(startTime)) {
                roomTime.setEighteenthHalf(true);
            } else if ("19:00".equals(startTime)) {
                roomTime.setNineteenFull(true);
            } else if ("19:30".equals(startTime)) {
                roomTime.setNineteenHalf(true);
            } else if ("20:00".equals(startTime)) {
                roomTime.setTwentyFull(true);
            } else if ("20:30".equals(startTime)) {
                roomTime.setTwentyHalf(true);
            } else if ("21:00".equals(startTime)) {
                roomTime.setTwentyOneFull(true);
            } else if ("21:30".equals(startTime)) {
                roomTime.setTwentyOneHalf(true);
            } else if ("22:00".equals(startTime)) {
                roomTime.setTwentyTwoFull(true);
            } else if ("22:30".equals(startTime)) {
                roomTime.setTwentyTwoHalf(true);
            } else if ("23:00".equals(startTime)) {
                roomTime.setTwentyThreeFull(true);
            } else if ("23:30".equals(startTime)) {
                roomTime.setTwentyThreeHalf(true);
            }
            if ("23:30".equals(startTime)) {
                break;
            } else {
                if (startTime.charAt(3) == '3') {
                    int temp = Integer.valueOf(startTime.substring(0, 2)) + 1;
                    startTime = String.format("%02d", temp) + ":00";
                } else {
                    startTime = startTime.substring(0, 3) + "30";
                }
            }
        }
        return roomTime;
    }

    private RoomTime timeListUtil(Boolean judge, Map.Entry<Room, List<Conference>> m, RoomTime roomTime) {

        /*取出键值，为后面覆盖该条信息做准备*/
        Room room = m.getKey();
        /*将会议室信息及占用时间存放到对象里*/

        roomTime.setRoomID(room.getRoomID());
        roomTime.setName(room.getName());
        roomTime.setLocation(room.getLocation());
        roomTime.setCapacity(room.getCapacity());
        roomTime.setCatalogue(room.getCatalogue());
        roomTime.setBuidling(room.getBuilding());
        roomTime.setFlag(room.getFlag());
        /*遍历所取出键值对应的会议列表*/
        for (Conference conference : m.getValue()) {
            /*将获取的会议起止时间转换为字符串类型*/
            String startTime = conference.getStartTime().toString();
            String endTime = conference.getEndTime().toString();
            if (judge) {
                if (!startTime.substring(8, 10).equals(endTime.substring(8, 10))) {
                    endTime = startTime.substring(0, 10) + " 23:30:00";
                }
            } else {
                startTime = endTime.substring(0, 10) + " 00:00:00";
            }
            /*早于30:00的起始时间全部转换为00:00，其他情况转换为30:00，方便后期处理*/
            if (startTime.charAt(14) < '3') {
                startTime = startTime.substring(11, 14) + "00";
            } else {
                startTime = startTime.substring(11, 14) + "30";
            }
            /*与起始时间做相反处理，将截至时间向下取“整”*/
            if ("00:00".equals(endTime.substring(14, 19))) {
                endTime = endTime.substring(11, 16);
            } else if ("30:00".equals(endTime.substring(14, 19))) {
                endTime = endTime.substring(11, 16);
            } else if (endTime.charAt(14) < '3') {
                endTime = endTime.substring(11, 14) + "30";
            } else {
                int temp = Integer.valueOf(endTime.substring(11, 13)) + 1;
                endTime = String.format("%02d", temp) + ":00";
            }
            roomTime = timeCutUtil(roomTime, startTime, endTime);
        }
        return roomTime;
    }

    @Override
    public List<Integer> getFreeRoomIDByDate(Date start, Date end) {
        try {
            List<Conference> conferenceBusyList1 = generalService.getConferencesByDate(start, end);

            //将房间根据房间号映射为Map
            List<Room> roomList = roomRepository.findAll();
            Map<Integer, Room> roomMap = new HashMap<Integer, Room>();
            for (int i = 0; i < roomList.size(); i++) {
                roomMap.put(roomList.get(i).getRoomID(), roomList.get(i));
            }

            //将有会的房间处理掉，然后遍历Map形成返回列表
            for (int i = 0; i < conferenceBusyList1.size(); i++) {
                roomMap.remove(conferenceBusyList1.get(i).getRoom().getRoomID());
            }
            List<Integer> toReturnList = new ArrayList<Integer>();
            for (Map.Entry<Integer, Room> i : roomMap.entrySet()) {
                toReturnList.add(i.getKey());
            }
            return toReturnList;
        } catch (Exception e) {
            System.err.println("出错！");
            System.err.println(e.toString());
        }
        return null;
    }

    @Override
    public List<Room> roomByBuilding(Building building) {
        return roomRepository.getRoomsByBuilding(building);
    }
}
