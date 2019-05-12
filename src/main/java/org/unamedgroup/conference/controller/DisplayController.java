package org.unamedgroup.conference.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.entity.temp.FailureInfo;
import org.unamedgroup.conference.entity.temp.MachineEntity;
import org.unamedgroup.conference.entity.temp.RoomTime;
import org.unamedgroup.conference.entity.temp.SuccessInfo;
import org.unamedgroup.conference.service.DevicesShowService;
import org.unamedgroup.conference.service.GeneralService;

import java.util.Date;

/**
 * DisplayController
 * 错误代码使用5xxx
 *
 * @author liumengxiao
 * @date 2019/03/28
 */

@Api(value = "显示屏 API", description = "显示屏操作接口", protocols = "http")
@CrossOrigin
@RestController
@RequestMapping("/machine")
public class DisplayController {

    @Autowired
    DevicesShowService devicesShowService;
    @Autowired
    GeneralService generalService;

    @ApiOperation(value = "获取显示屏信息api", protocols = "http"
            , produces = "application/json", consumes = "application/json"
            , response = MachineEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomID", value = "房间编号", required = true, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public Object getAllInfo(Integer roomID) {
        try {
            MachineEntity machineEntity = new MachineEntity();
            Integer conferenceID = devicesShowService.getCurrentConferenceID(roomID);
            machineEntity.setName(generalService.getRoomNameByID(roomID));
            machineEntity.setApplicantName(devicesShowService.getApplicantName(conferenceID));
            machineEntity.setMeetingSchedule(devicesShowService.getConferenceSchedule(conferenceID));
            machineEntity.setConferenceRoomStatus(devicesShowService.getConferenceRoomStatus(roomID));
            machineEntity.setConferenceSubject(devicesShowService.getConferenceSubject(conferenceID));
            return new SuccessInfo(machineEntity);
        } catch (Exception e) {
            System.err.println(e.toString());
            return new FailureInfo(5000, "获取会议室信息失败，请稍后重试！");
        }
    }

}
