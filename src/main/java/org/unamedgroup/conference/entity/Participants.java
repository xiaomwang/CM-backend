package org.unamedgroup.conference.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Participants
 *
 * @author liumengxiao
 * @date 2019/04/07
 */

@Entity
@Data
@Table(name = "participant")
public class Participants {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer sequenceID;
    private Integer userID;
}
