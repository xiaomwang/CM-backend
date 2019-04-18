package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.Room;

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
    public List<Integer> getFreeRoomIDByDate(Date start, Date end);
}
