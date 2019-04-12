package org.unamedgroup.conference.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.temp.FailureInfo;
import org.unamedgroup.conference.entity.temp.SuccessInfo;
import org.unamedgroup.conference.service.DevicesControlService;

/**
 * DeviceController
 * 错误代码使用4xxx
 *
 * @author liumengxiao
 * @date 2019/04/03
 */

@CrossOrigin
@RestController
@RequestMapping(value = "devices")
public class DeviceController {
    @Autowired
    DevicesControlService devicesControl;

    @RequestMapping(value = "/door", method = RequestMethod.POST)
    @ResponseBody
    public Object openDoor(Integer userID) {
        if (devicesControl.openDoor(userID) == true) {
            return new SuccessInfo(true);
        } else {
            return new FailureInfo(4000, "开门失败，请重试！");
        }
    }
}

