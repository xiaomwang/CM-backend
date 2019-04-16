package org.unamedgroup.conference.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
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
}
