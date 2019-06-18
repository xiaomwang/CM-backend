package org.unamedgroup.conference.entity.temp;

import lombok.Data;

/**
 * @Author: 白振宇
 * @Date： 2019/6/13 15:06
 */
@Data
public class UserConferenceOne {
    private CountMap apply;
    private CountMap pass;
    private CountMap reject;
    private CountMap cancel;
    private CountMap part;
}
