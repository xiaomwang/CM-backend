package org.unamedgroup.conference.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.unamedgroup.conference.entity.Conference;
import org.unamedgroup.conference.entity.User;

import java.util.Date;
import java.util.List;

/**
 * 通用服务类
 * 提供一些可能被公共调用的方法
 *
 * @author liumengxiao、zhoutao
 * @date 2019年04月09日
 */
public interface GeneralService {
    /**
     * 根据会议室房间的ID号查询房间名
     *
     * @param roomID 房间ID
     * @return 房间名
     */
    public String getRoomNameByID(Integer roomID);

    /**
     * 输入一段时间，如果某场会议在这段时间内开，则返回这场会议。
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 会议列表
     */
    public List<Conference> getConferencesByDate(Date start, Date end);

    /**
     * 根据时间点以及地点查看会议
     *
     * @param roomID
     * @param date
     * @return 会议列表
     */
    public List<Conference> getConferencesByLocationAndDate(Integer roomID, Date now, Date date);

    /**
     * 返回当前登录用户
     *
     * @return 当前登录的用户
     */
    public User getLoginUser();

    /**
     * Email有效项检查
     *
     * @param email 电子邮箱
     * @return 有效性检查结果
     */
    public Boolean checkEmail(String email);

    /**
     * 电话号码有效性检查
     *
     * @param phoneNumber 中国大陆手机号码
     * @return 有效性检查结果
     */
    public Boolean checkMoiblePhone(String phoneNumber);

    /**
     * 检查用户所属用户组，返回用户是否有权管理其他用户
     *
     * @return 检查结果
     */
    public Boolean isAdmin();

}
