package org.unamedgroup.conference.entity.temp;


import lombok.Data;

import java.util.Date;

/**
 * MachineEntity
 *
 * @author liumengxiao
 * @date 2019/03/30
 */
@Data
public class MachineEntity {
    private String name;
    private String applicantName;
    private Date[] meetingSchedule;
    private Integer conferenceRoomStatus;
    private String conferenceSubject;
}
