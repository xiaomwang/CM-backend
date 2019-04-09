package org.unamedgroup.conference.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * BuildingController
 *
 * @author liumengxiao
 * @date 2019/03/12
 */

@Entity
@Data
@Table(name = "building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer buildingID;
    private String name;
    private String address;
    private Integer manager;
}
