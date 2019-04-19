package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.temp.ReturnUser;

import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/4/11 8:19
 */
public interface MyConferenceService {
    /**
     * 根据申请人查询相关会议信息
     *
     * @param userId 申请人工号
     * @return 该员工申请的会议信息
     */
    List<Conference> getConferencesByProposer(int userId);

    /**
     * 根据参与者查询相关会议信息
     *
     * @param userId 参与者工号
     * @return 该员工参与的会议信息
     */
    List<Conference> getConferencesByParticipant(int userId);

    /**
     * 查询用户的所有相关会议信息
     *
     * @param userId      用户工号
     * @param pageCurrent 当前页码
     * @param pageSize    页容量
     * @return 该员工的所有相关会议信息列表
     */
    List<Conference> getMyConferenceList(int userId, Integer pageCurrent, Integer pageSize);

    /**
     * 查询用户所有的相关会议的总数
     *
     * @param userId 用户id
     * @return 相关会议总数
     */
    Integer getMyConferenceTotal(int userId);

    /**
     * 根据传入的部分姓名匹配用户实体
     *
     * @param realName 用户的真实姓名（部分）
     * @return 用户实体列表
     */
    public List<ReturnUser> getUserListByName(String realName);
}
