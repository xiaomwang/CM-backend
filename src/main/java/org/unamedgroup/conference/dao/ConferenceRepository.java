package org.unamedgroup.conference.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.User;

import java.util.Date;
import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/3/14 8:12
 */
@Repository
public interface ConferenceRepository extends CrudRepository<Conference, Integer> {
    /**
     * 根据开始日期和会议状态以及会议室ID查找所有会议
     * @param id 会议室ID
     * @param status 会议状态
     * @param start 开始时间
     * @param end 结束时间
     * @return 返回会议列表
     */
    List<Conference> findByRoomAndStatusAndStartTimeBetween(Integer id, Integer status, Date start, Date end);

    @Override
    Conference save(Conference conference);

  
    /**
     * 返回两个时间点之间的会议列表
     *
     * @param startTime
     * @param endTime
     * @return List<Conference>
     * @author: liumengxiao
     */
    List<Conference> getConferencesByStartTimeBeforeAndEndTimeAfter(Date startTime, Date endTime);

    /**
     * 根据会议ID得到会议实体
     *
     * @param conferenceID
     * @return 会议室体
     */
    Conference getConferenceByConferenceID(Integer conferenceID);

    /**
     * 根据用户ID来获取用户所有的会议
     * @param user
     * @return 会议列表
     */
    List<Conference> getConferencesByUser(Integer user);

    /**
     * 根据结束日期和会议状态以及会议室ID查找所有会议
     * @param id 会议室ID
     * @param status 会议状态
     * @param start 开始时间
     * @param end 结束时间
     * @param date 当前日期
     * @return 会议列表
     */
    List<Conference> findByRoomAndStatusAndEndTimeBetweenAndStartTimeBefore(Integer id, Integer status, Date start, Date end, Date date);

    /**
     * 根据参与者序列号ID查询会议信息
     * @param participantSequence 参与者序列号
     * @return 会议信息
     */
    Conference getConferenceByParticipantSequence(Integer participantSequence);
}
