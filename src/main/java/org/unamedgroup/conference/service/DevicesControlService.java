package org.unamedgroup.conference.service;

/**
 * 设备控制相关接口业务逻辑处理器
 *
 * @author liumengxiao
 * @date 2019-04-03
 */
public interface DevicesControlService {
    /**
     * 用户调用此方法，中控向设备控制器发送信号开门
     * 根据设备控制器控制情况返回开门是否成功
     *
     * @param userID 用户ID
     * @return 开门结果
     */
    public Boolean openDoor(Integer userID);
}
