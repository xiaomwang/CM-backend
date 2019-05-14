package org.unamedgroup.conference.dao;

        import org.springframework.data.jpa.repository.Modifying;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Repository;
        import org.springframework.transaction.annotation.Transactional;
        import org.unamedgroup.conference.entity.Conference;
        import org.unamedgroup.conference.entity.Room;

        import java.util.Date;
        import java.util.List;

/**
 * @Author: 白振宇、zhoutao
 * @Date： 2019/3/14 8:12
 */
@Repository
public interface ConferenceRepository extends CrudRepository<Conference, Integer> {
    /**
     * 根据开始日期和会议状态以及会议室ID查找所有会议
     *
     * @param id     会议室信息
     * @param status 会议状态
     * @param start  开始时间
     * @param end    结束时间
     * @return 返回会议列表
     */
    List<Conference> findByRoomAndStatusAndStartTimeBetween(Room id, Integer status, Date start, Date end);

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
     * @param id     会议室信息
     * @param status 会议状态
     * @param start  开始时间
     * @param end    结束时间
     * @param date   当前日期
     * @return 会议列表
     */
    List<Conference> findByRoomAndStatusAndEndTimeBetweenAndStartTimeBefore(Room id, Integer status, Date start, Date end, Date date);

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
     * 确定特定时间是否有会的数据库查询系列（1）
     * ST<= date & date <=ET
     * ST<= date1 & date1 <=ET
     * ST<= date & date1 <=ET
     *
     * @param date  时间点
     * @param date1 时间点1
     * @return 会议列表
     */
    List<Conference> findConferencesByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Date date, Date date1);

    /**
     * 确定特定时间是否有会的数据库查询系列（2）
     * ST>= date & date1 >=ET
     *
     * @param date  时间点
     * @param date1 时间点1
     * @return 会议列表
     */
    List<Conference> findConferencesByStartTimeGreaterThanEqualAndEndTimeLessThanEqual(Date date, Date date1);

    /**
     * 根据会议状态获取会议信息列表
     *
     * @param status 会议状态
     * @return 相应会议状态的会议信息列表
     */
    List<Conference> getConferencesByStatus(Integer status);

    /**
     * 按照升序查询与用户相关的所有会议
     *
     * @param status              会议状态
     * @param user                用户id
     * @param participantSequence 与会人序列
     * @param pageNumber          起始索引
     * @param pageSize            每页容量
     * @return 会议列表
     */
    @Modifying
    @Query(value = "select * from conference c where c.status = ?1 and (c.user = ?2 or c.participant_sequence in (?3)) order by c.start_time ASC limit ?4,?5", nativeQuery = true)
    List<Conference> findMyConference(Integer status, Integer user, List<Integer> participantSequence, Integer pageNumber, Integer pageSize);

    @Transactional
    @Query(value = "select count(*) from conference c where c.status = ?1 and (c.user = ?2 or c.participant_sequence in (?3))", nativeQuery = true)
    Integer countMyConference(Integer status, Integer user, List<Integer> participantSequence);

    /**
     * 根据会议室、会议状态、时间点查看会议, 用于人脸识别
     *
     * @param id
     * @param status
     * @param date
     * @return
     */
    @Modifying
    @Query(value = "select * from conference c where c.room = ?1 and c.status = ?2 and c.start_time between (?3) and  (?4)", nativeQuery = true)
    List<Conference> findByRoomAndStatusAndDate(Room id, Integer status, Date now, Date date);


    /**
     * 根据房间信息以及开始时间结束时间查询（状态为通过的）
     * @param room 房间信息
     * @param status 会议状态
     * @param startStart 开始时间区间开始
     * @param startEnd 开始时间区间结束
     * @param endStart 结束时间区间开始
     * @param endEnd 结束时间区间结束
     * @return 会议信息列表
     */
    List<Conference> findByRoomAndStatusAndStartTimeBetweenAndEndTimeBetween(Room room, Integer status, Date startStart, Date startEnd, Date endStart, Date endEnd);

}
