package org.unamedgroup.conference.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * User
 *
 * @author liumengxiao
 * @date 2019/03/12
 */


@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer userID;
    private String password;
    private String realName;
    private String department;
    private String email;
    private String phoneNumber;
    private Integer userGroup;
    private String feature;

    public User() {
    }

    public User(String password, String realName, String department, String email, String phoneNumber, Integer userGroup, String feature) {
        this.password = password;
        this.realName = realName;
        this.department = department;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userGroup = userGroup;
        this.feature = feature;
    }

    public String getPasswordHash() {
        try {
            if (password != null) {
                Integer passwordHash = password.hashCode();
                return passwordHash.toString();
            } else {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            System.err.println("密码进行HASH运算时候遇到问题，请检查。");
            System.err.println(e.toString());
        }
        return null;
    }
}
