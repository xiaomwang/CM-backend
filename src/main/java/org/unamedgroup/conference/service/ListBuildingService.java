package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.Building;

import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/5/17 16:02
 */
public interface ListBuildingService {
    /**
     * 根据地址返回楼宇信息（若果地址值为-1则返回全部楼宇信息）
     * @param address 楼宇地址信息
     * @return 楼宇信息
     */
    List<Building> listBuilding(String address);

    /**
     * 根据楼宇id返回楼宇信息（如果楼宇id值为-1则返回全部楼宇信息）
     * @param buildingID 楼宇id
     * @return 地址列表
     */
    List<String> listAddress(Integer buildingID);
}
