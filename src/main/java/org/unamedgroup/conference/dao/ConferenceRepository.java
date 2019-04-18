package org.unamedgroup.conference.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unamedgroup.conference.entity.Conference;

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
     *
     * @param id     会议室ID
     * @param status 会议状态
     * @param start  开始时间
     * @param end    结束时间
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
     *
     * @param user
     * @return 会议列表
     */
    List<Conference> getConferencesByUser(Integer user);

    /**
     * 根据结束日期和会议状态以及会议室ID查找所有会议
     *
     * @param id     会议室ID
     * @param status 会议状态
     * @param start  开始时间
     * @param end    结束时间
     * @param date   当前日期
     * @return 会议列表
     */
    List<Conference> findByRoomAndStatusAndEndTimeBetweenAndStartTimeBefore(Integer id, Integer status, Date start, Date end, Date date);

    /**
     * 根据参与者序列号ID查询会议信息
     *
     * @param participantSequence 参与者序列号
     * @param status              会议状态
     * @return 会议信息
     */
    Conference getConferenceByParticipantSequenceAndStatus(Integer participantSequence, Integer status);

    /**
     * 根据用户ID以及会议状态来获取用户所有的会议
     *
     * @param user   用户ID
     * @param status 会议状态
     * @return 会议列表
     */
    List<Conference> getConferencesByUserAndStatus(Integer user, Integer status);


    /**
     * 会议中是否包括某个时间点（或小时间段）
     * 主要用来检测空闲会议室使用
     *
     * @param date  时间段开始时间/时间点
     * @param date1 时间段结束时间/时间点
     * @return 会议列表
     */
    List<Conference> findConferencesByStartTimeBeforeAndEndTimeAfter(Date date, Date date1);

    /**
     * 查询某个时间但是否在会议的时间内（闭区间）
     *
     * @param date 时间点
     * @param date_same 时间点，根据JPA要求要传两个参数
     * @return
     */
    List<Conference> findConferencesByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Date date, Date date_same);


    /**
     * 根据会议状态获取会议信息列表
     *
     * @param status 会议状态
     * @return 相应会议状态的会议信息列表
     */
    List<Conference> getConferencesByStatus(Integer status);

    /**
     * 按照升序查询与用户相关的所有会议
     * @param status 会议状态
     * @param user 用户id
     * @param participantSequence 与会人序列
     * @param pageNumber 起始索引
     * @param pageSize 每页容量
     * @return 会议列表
     */
    @Modifying
    @Query(value = "select * from conference c where c.status = ?1 and (c.user = ?2 or c.participant_sequence in (?3)) order by c.start_time ASC limit ?4,?5", nativeQuery = true)
    List<Conference> findMyConference(Integer status, Integer user, List<Integer> participantSequence, Integer pageNumber, Integer pageSize);

}
