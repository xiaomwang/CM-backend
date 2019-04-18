package org.unamedgroup.conference.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.dao.RoomRepository;
import org.unamedgroup.conference.entity.Building;
import org.unamedgroup.conference.entity.Room;
import org.unamedgroup.conference.entity.temp.FailureInfo;
import org.unamedgroup.conference.entity.temp.RoomTime;
import org.unamedgroup.conference.entity.temp.SuccessInfo;
import org.unamedgroup.conference.service.GuideQueryService;
import org.unamedgroup.conference.service.QuickCheckService;
import org.unamedgroup.conference.service.RelevanceQueryService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * RoomController
 * 错误代码使用6xxx
 *
 * @author liumengxiao
 * @date 2019/03/12
 */

@Api(value = "会议室操作 API", description = "会议室操作 API", position = 100, protocols = "http")
@CrossOrigin
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    QuickCheckService quickCheckService;
    @Autowired
    GuideQueryService guideQueryService;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    RelevanceQueryService relevanceQueryService;

    @RequestMapping(value = "/free", method = RequestMethod.GET)
    @ResponseBody
    public Object getFreeRoom(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
        if (guideQueryService.getFreeRoomIDByDate(start, end) == null) {
            return new FailureInfo(6000, "没有房间空闲或查询失败！请核实！");
        } else {
            return new SuccessInfo(guideQueryService.getFreeRoomIDByDate(start, end));
        }
    }

    @RequestMapping(value = "/freeRoomNumber", method = RequestMethod.GET)
    @ResponseBody
    public Object getFreeRoomNumberToday() {
        Date current = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        // 日期后移一天
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        // 小时置零
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        // 分置零
        calendar.set(Calendar.MINUTE, 0);
        // 秒置零
        calendar.set(Calendar.SECOND, 0);
        // 毫秒置零
        calendar.set(Calendar.MILLISECOND, 0);


        if (guideQueryService.getFreeRoomIDByDate(current, calendar.getTime()) == null) {
            return new FailureInfo(6000, "没有房间空闲或查询失败！请核实！");
        } else {
            return new SuccessInfo(guideQueryService.getFreeRoomIDByDate(current, calendar.getTime()).size());
        }
    }

    @ApiOperation(value = "快速查看会议室api", protocols = "http"
            , produces = "application/json", consumes = "application/json"
            , response = RoomTime.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "date", value = "日期", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "buildingID", value = "楼宇编号", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "roomID", value = "房间编号", required = false, dataType = "int", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功")
    })
    @GetMapping(value = "list")
    public Object getList(String date, Integer buildingID, Integer roomID) {
        List<RoomTime> roomTimeList = quickCheckService.handleRoomTime(date, buildingID, roomID);
        if (roomTimeList == null) {
            return new FailureInfo(6001, "处理房间填充失败！");
        } else {
            return new SuccessInfo(roomTimeList);
        }
    }

    @GetMapping(value = "list/pre")
    public Object listPre(Integer buildingID, Integer roomID) {
        List<Room> roomList = quickCheckService.getConferenceList(buildingID, roomID);
        if(roomList == null) {
            return new FailureInfo(6002, "处理房间预处理失败！");
        } else {
            return new SuccessInfo(roomList);
        }
    }

    @GetMapping(value = "list/building")
    public Object listByBuilding(Building building) {
        List<Room> roomList = relevanceQueryService.roomByBuilding(building);
        if(roomList == null) {
            return new FailureInfo(6003, "获取会议室失败!");
        } else {
            return new SuccessInfo(roomList);
        }
    }

    @RequestMapping(value = "/roomObject", method = RequestMethod.GET)
    @ResponseBody
    public Object getRoomObject(Integer roomID) {
        try {
            Room room = roomRepository.getRoomByRoomID(roomID);
            if (room != null) {
                return new SuccessInfo(room);
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            return new FailureInfo(6002, "找不到满足条件的房间！");
        }
    }
}
