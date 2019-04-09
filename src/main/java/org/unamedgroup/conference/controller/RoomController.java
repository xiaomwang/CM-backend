package org.unamedgroup.conference.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.entity.Room;
import org.unamedgroup.conference.service.GuideQueryService;
import org.unamedgroup.conference.service.QuickCheckService;

import java.util.Date;
import java.util.List;

/**
 * RoomController
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
    public List<Integer> getFreeRoom(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
        return guideQueryService.getFreeRoomIDByDate(start, end);
    }

    @GetMapping(value = "list")
    public List getList(String date, Integer buildingID, Integer roomID) {
        return quickCheckService.handleRoomTime(date, buildingID, roomID);
    }
}
