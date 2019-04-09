package org.unamedgroup.conference.service;

/**
 * 通用服务类
 * 提供一些可能被公共调用的方法
 *
 * @author liumengxiao
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
}
