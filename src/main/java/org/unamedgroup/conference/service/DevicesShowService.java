package org.unamedgroup.conference.service;

import org.unamedgroup.conference.entity.Conference;

import java.util.Date;

/**
 * 会议相关业务逻辑处理器
 *
 * @author liumengxiao
 * @date 2019.4.3
 */
public interface DevicesShowService {
    /**
     * 根据房间号，得到当前时间当前房间正在举行的会议编号。
     * 若当前没有会议则返回空指针
     *
     * @param roomID
     * @return 会议ID
     */
    public Integer getCurrentConferenceID(Integer roomID);

    /**
     * 根据会议ID得到会议申请者姓名
     *
     * @param conferenceID
     * @return 会议申请者姓名
     */
    public String getApplicantName(Integer conferenceID);

    /**
     * 根据会议ID得到会议开始和结束时间（Date数组类型返回）
     *
     * @param conferenceID
     * @return 会议开始和结束时间数组
     */
    public Date[] getConferenceSchedule(Integer conferenceID);

    /**
     * 根据房间号得到会议房间状态（正在清扫、会议中、维护等）
     *
     * @param roomID
     * @return 整数表示房间状态
     */
    public Integer getConferenceRoomStatus(Integer roomID);

    /**
     * 根据会议编号返回会议主题（会议名）
     *
     * @param conferenceID
     * @return 会议主题
     */
    public String getConferenceSubject(Integer conferenceID);
}
