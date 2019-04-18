package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.Building;
import org.unamedgroup.conference.entity.Room;
import org.unamedgroup.conference.entity.temp.RoomTime;

import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/4/4 9:04
 */
public interface QuickCheckService {
    /**
     * 获取符合快速查看的会议列表
     *
     * @param building   建筑信息
     * @param roomId     会议室编号
     * @return 会议列表
     */
    List<Room> getConferenceList(Building building, Integer roomId);

    /**
     * 将获取的会议室及其列表处理成会议室的占用时间段
     *
     * @param date       日期限制
     * @param building   楼宇信息
     * @param roomId     会议室编号
     * @return 占用时间段列表
     */
    List<RoomTime> handleRoomTime(String date, Building building, Integer roomId);

    /**
     * 返回所有的楼宇
     *
     * @return 楼宇列表
     */
    List<Building> getAllBuildings();

    /**
     * 根据楼宇编号返回该楼宇所有的会议室列表
     *
     * @param buildingID 楼宇编号
     * @return 所有的房间列表
     */
    List<Room> getRoomsByBuildingID(Integer buildingID);
}
