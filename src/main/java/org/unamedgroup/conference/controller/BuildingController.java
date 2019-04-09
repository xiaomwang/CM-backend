package org.unamedgroup.conference.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.dao.BuildingRepository;
import org.unamedgroup.conference.entity.Building;

import java.util.List;

/**
 * 楼层相关控制器
 *
 * @author liumengxiao
 */

@CrossOrigin
@RestController
@RequestMapping("/building")
public class BuildingController {
    @Autowired
    BuildingRepository buildingRepository;

    @RequestMapping(value = "/getAllBuilding", method = RequestMethod.GET)
    @ResponseBody
    public List<Building> getAllBuilding() {
        return buildingRepository.findAll();
    }
}
