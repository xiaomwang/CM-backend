package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.Conference;

import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/4/11 11:53
 */
public interface ConferenceManageService {
    /**
     * 查询处于未开始状态的会议
     * @param status 会议状态
     * @return
     */
    List<Conference> getNotStartConferenceList(Integer status);

    /**
     * 查询所有会议信息
     *
     * @param pageCurrent 当前页码
     * @param pageSize    页容量
     * @return 所有会议信息列表
     */
    List<Conference> getPageConferenceList(Integer pageCurrent, Integer pageSize);

    /**
     * 查询所有会议信息总条数
     * @return 会议信息总条数
     */
    Integer getPageConferenceTotal();
}
