package org.unamedgroup.conference.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.unamedgroup.conference.dao.BuildingRepository;
import org.unamedgroup.conference.entity.Building;
import org.unamedgroup.conference.entity.Room;
import org.unamedgroup.conference.entity.temp.FailureInfo;
import org.unamedgroup.conference.entity.temp.RoomTime;
import org.unamedgroup.conference.entity.temp.SuccessInfo;

import java.util.List;

/**
 * 楼层相关控制器
 * 错误代码使用2xxx
 *
 * @author liumengxiao
 */

@Api(value = "楼宇 API", description = "楼宇操作接口", protocols = "http")
@CrossOrigin
@RestController
@RequestMapping("/building")
public class BuildingController {
    @Autowired
    BuildingRepository buildingRepository;

    @ApiOperation(value = "获取所有楼宇信息api", protocols = "http"
            , produces = "application/json", consumes = "application/json"
            , response = Building.class)
    @RequestMapping(value = "/allBuilding", method = RequestMethod.GET)
    @ResponseBody
    public Object getAllBuilding() {
        return new SuccessInfo(buildingRepository.findAll());
    }

    @ApiOperation(value = "获取楼宇实体api", protocols = "http"
            , produces = "application/json", consumes = "application/json"
            , response = Building.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "buildingID", value = "楼宇编号", required = true, dataType = "int", paramType = "query"),
    })
    @RequestMapping(value = "/buildingObject", method = RequestMethod.GET)
    @ResponseBody
    public Object getBuildingObject(Integer buildingID) {
        try {
            Building building = buildingRepository.getBuildingByBuildingID(buildingID);
            if (building != null) {
                return new SuccessInfo(building);
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            return new FailureInfo(2000, "找不到满足条件的楼宇！");
        }
    }
}
