package org.unamedgroup.conference.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.unamedgroup.conference.entity.Participants;

import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/4/11 9:15
 */
@Repository
public interface ParticipantsRepository extends CrudRepository<Participants, Integer> {
    /**
     * 根据用户查询参与者序列号
     * @param userId 用户工号
     * @return 参与者序列号列表
     */
    List<Participants> findByUserID(Integer userId);

    @Query(value = "select p.sequenceID from Participants p where p.userID=?1")
    List<Integer> findSequenceIDByUserID(Integer userId);

    /**
     * 根据会议室id删除参与者记录
     * @param conferenceID 会议室id
     * @return 删除条数个数
     */
    @Transactional
    Integer removeBySequenceID(Integer conferenceID);

    /**
     * 添加会议室id和对应的参与者id
     * @param participants 参与者对象
     * @return 保存记录条数
     */
    @Override
    @Transactional
    Participants save(Participants participants);

    /**
     * 查询会议与会人信息
     * @param conferenceID 会议编号
     * @return 与会人信息列表
     */
    List<Participants> findBySequenceID(Integer conferenceID);
}
