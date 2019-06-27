package org.unamedgroup.conference.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Room
 *
 * @author liumengxiao
 * @date 2019/03/12
 */

@Entity
@Data
@Table(name = "room")
public class Room implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer roomID;
    private String name;
    private String location;
    private Integer capacity;
    private String catalogue;
    private Integer flag;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "building", referencedColumnName = "buildingid")
    private Building building;
}
