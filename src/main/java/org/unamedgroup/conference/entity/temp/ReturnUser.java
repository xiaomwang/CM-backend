package org.unamedgroup.conference.entity.temp;

import lombok.Data;
import org.unamedgroup.conference.entity.User;


@Data
public class ReturnUser {
    private Integer userID;
    private String realName;
    private String phoneNumber;

    public ReturnUser(User user) {
        this.userID = user.getUserID();
        this.realName = user.getRealName();
        this.phoneNumber = user.getPhoneNumber();
    }
}
