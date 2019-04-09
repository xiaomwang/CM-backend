package org.unamedgroup.conference.service.impl;

import org.unamedgroup.conference.service.DevicesControlService;

/**
 * DevicesControlSimulateImpl
 *
 * @author liumengxiao
 * @date 2019/04/03
 */

public class DevicesSimulateImpl implements DevicesControlService {
    @Override
    public Boolean openDoor() {
        return true;
    }
}
