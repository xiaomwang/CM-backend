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
    @Column(name="ROWID")
    private String rowId;
    private Integer sequenceID;
    private Integer userID;
}
