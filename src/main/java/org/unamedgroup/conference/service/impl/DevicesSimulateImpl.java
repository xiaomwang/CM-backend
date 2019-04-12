package org.unamedgroup.conference.service.impl;

import org.springframework.stereotype.Component;
import org.unamedgroup.conference.service.DevicesControlService;

/**
 * DevicesControlSimulateImpl
 *
 * @author liumengxiao
 * @date 2019/04/03
 */

@Component
public class DevicesSimulateImpl implements DevicesControlService {
    @Override
    public Boolean openDoor(Integer userID) {
        return true;
    }
}
