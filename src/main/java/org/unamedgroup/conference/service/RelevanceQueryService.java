package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.Building;
import org.unamedgroup.conference.entity.Room;

import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/4/14 22:57
 */
public interface RelevanceQueryService {
    List<Room> roomByBuilding(Building buildingID);
}
