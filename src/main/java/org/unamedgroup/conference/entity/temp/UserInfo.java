package org.unamedgroup.conference.entity.temp;

import lombok.Data;
import org.unamedgroup.conference.entity.User;

@Data
public class UserInfo {
    public Integer userID;
    private String realName;
    private String department;
    private String email;
    private String phoneNumber;

    public UserInfo(User user) {
        this.userID = user.getUserID();
        this.realName = user.getRealName();
        this.department = user.getDepartment();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
    }
}
