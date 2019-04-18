package org.unamedgroup.conference.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Conference
 *
 * @author liumengxiao
 * @date 2019/03/12
 */

@Entity
@Data
@Table(name = "conference")
public class Conference implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer conferenceID;
    private String subject;
    private Integer room;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    private Integer user;
    private Integer number;
    private Integer status;
    @GeneratedValue
    private Integer participantSequence;
}
