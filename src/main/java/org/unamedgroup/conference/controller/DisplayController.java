package org.unamedgroup.conference.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.entity.temp.MachineEntity;
import org.unamedgroup.conference.service.DevicesShowService;
import org.unamedgroup.conference.service.GeneralService;

import java.util.Date;

/**
 * DisplayController
 *
 * @author liumengxiao
 * @date 2019/03/28
 */

@CrossOrigin
@RestController
@RequestMapping("/machine")
public class DisplayController {

    @Autowired
    DevicesShowService devicesShowService;
    @Autowired
    GeneralService generalService;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public MachineEntity getAllInfo(Integer roomID) {
        try {
            MachineEntity machineEntity = new MachineEntity();
            Integer conferenceID = devicesShowService.getCurrentConferenceID(roomID);
            machineEntity.setName(generalService.getRoomNameByID(roomID));
            machineEntity.setApplicantName(devicesShowService.getApplicantName(conferenceID));
            machineEntity.setMeetingSchedule(devicesShowService.getConferenceSchedule(conferenceID));
            machineEntity.setConferenceRoomStatus(devicesShowService.getConferenceRoomStatus(roomID));
            machineEntity.setConferenceSubject(devicesShowService.getConferenceSubject(conferenceID));
            return machineEntity;
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return null;
    }

}