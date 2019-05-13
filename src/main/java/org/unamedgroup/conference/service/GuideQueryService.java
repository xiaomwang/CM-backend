package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.Room;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * 向导查询相关业务
 *
 * @author liumengxiao
 * @date 2019-04-09
 */

public interface GuideQueryService {
    /**
     * 根据起止时间段查询该时间段的空闲会议室
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 会议室列表
     */
    public List<Room> getFreeRoomIDByDate(Date start, Date end);

    /**
     * 查询所有会议室信息
     * @return 所有会议室信息列表
     *
     */
    List<Room> getAllRoom();

    /**
     * 按房间在时间区间内房间的空闲指数进行排序，每半个小时空闲累积一个空闲指数
     * @param start 开始时间
     * @param end 结束时间
     * @param roomList 房间列表
     * @return 排序后的房间信息列表
     */
    List<Room> sortRoomByFreeIndex(List<Room> roomList, Date start, Date end);
}
