package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.Room;
import org.unamedgroup.conference.entity.temp.PageRoom;

import java.util.List;
import java.util.Set;

/**
 * @Author: 白振宇
 * @Date： 2019/5/27 11:49
 */
public interface RoomManageService {
    /**
     * 查询房间页信息
     * @param pageCurrent 当前页码
     * @param pageSize 页容量
     * @return 当前页房间信息
     */
    List<Room> getPageRoomInfo(Integer pageCurrent, Integer pageSize);

    /**
     * 获取房间信息总条数
     * @return 房间信息总条数
     */
    Integer totalPageRomInfo();

    /**
     * 按房间ID删除会议室记录
     * @param roomID 会议室编号
     * @return 删除会议室条数
     */
    Integer deleteRoomByRoomID(Integer roomID);

    /**
     * 修改或添加会议室信息
     * @param room 会议室信息
     * @return 修改后的会议室信息
     */
    Room modifyRoom(Room room);

    /**
     * 房间信息全字段模糊匹配
     * @param params 参数（多个参数，以空格划分）
     * @return 房间信息集合
     */
    Set<Room> allFuzzyMatching(String params);

    /**
     * 将会议室集合分页处理
     * @param roomSet
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    PageRoom pageRoomSet(Set<Room> roomSet, Integer pageCurrent, Integer pageSize);
}
