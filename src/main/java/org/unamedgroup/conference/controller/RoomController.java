package org.unamedgroup.conference.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.entity.Room;
import org.unamedgroup.conference.entity.temp.FailureInfo;
import org.unamedgroup.conference.entity.temp.SuccessInfo;
import org.unamedgroup.conference.service.GuideQueryService;
import org.unamedgroup.conference.service.QuickCheckService;

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

@CrossOrigin
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    QuickCheckService quickCheckService;
    @Autowired
    GuideQueryService guideQueryService;

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

    @GetMapping(value = "list")
    public Object getList(String date, Integer buildingID, Integer roomID) {
        if (quickCheckService.handleRoomTime(date, buildingID, roomID) == null) {
            return new FailureInfo(6001, "处理房间填充失败！");
        } else {
            return new SuccessInfo(quickCheckService.handleRoomTime(date, buildingID, roomID));
        }

    }
}
