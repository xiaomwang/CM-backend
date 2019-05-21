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
    public List<String> listAddress(Integer buildingID) {
        List<String> addressList;
        if(buildingID.equals(-1)) {
            addressList = buildingRepository.findDistinctAddress();
        } else {
            addressList = new ArrayList<>();
            addressList.add(buildingRepository.getBuildingByBuildingID(buildingID).getAddress());
        }
        return addressList;
    }
}
