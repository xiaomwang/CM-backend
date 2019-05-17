package org.unamedgroup.conference.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unamedgroup.conference.dao.BuildingRepository;
import org.unamedgroup.conference.entity.Building;
import org.unamedgroup.conference.service.ListBuildingService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/5/17 16:02
 */
@Service
public class BuildingServiceImpl implements ListBuildingService {

    @Autowired
    BuildingRepository buildingRepository;

    @Override
    public List<Building> listBuilding(String address) {
        List<Building> buildings;
        if("-1".equals(address)) {
            buildings = buildingRepository.findAll();
        } else {
            buildings = buildingRepository.findByAddress(address);
        }
        return buildings;
    }

    @Override
    public List<Building> listAddress(Integer buildingID) {
        List<Building> buildings;
        if(buildingID.equals(-1)) {
            buildings = buildingRepository.findAll();
        } else {
            buildings = new ArrayList<>();
            buildings.add(buildingRepository.getBuildingByBuildingID(buildingID));
        }
        return buildings;
    }
}
