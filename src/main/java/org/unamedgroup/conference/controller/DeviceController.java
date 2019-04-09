package org.unamedgroup.conference.controller;

import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.entity.Conference;

/**
 * DeviceController
 *
 * @author liumengxiao
 * @date 2019/04/03
 */

@CrossOrigin
@RestController
@RequestMapping(value = "devices")
public class DeviceController {

    @RequestMapping(value = "/door", method = RequestMethod.POST)
    @ResponseBody
    public Boolean openDoor(Integer userID) {
        return openDoor(userID);
    }
}

