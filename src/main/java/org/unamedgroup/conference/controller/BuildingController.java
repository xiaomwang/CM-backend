package org.unamedgroup.conference.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.dao.BuildingRepository;
import org.unamedgroup.conference.entity.Building;
import org.unamedgroup.conference.entity.temp.SuccessInfo;

import java.util.List;

/**
 * 楼层相关控制器
 * 错误代码使用2xxx
 *
 * @author liumengxiao
 */

@CrossOrigin
@RestController
@RequestMapping("/building")
public class BuildingController {
    @Autowired
    BuildingRepository buildingRepository;

    @RequestMapping(value = "/allBuilding", method = RequestMethod.GET)
    @ResponseBody
    public Object getAllBuilding() {
        return new SuccessInfo(buildingRepository.findAll());
    }
}
