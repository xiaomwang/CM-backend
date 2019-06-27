package org.unamedgroup.conference.entity.temp;

import lombok.Data;

import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/6/13 10:23
 */
@Data
public class UserConferenceCase {
    List<String> nameList;
    List<Integer> passList;
    List<Integer> partList;
    List<Integer> rejectList;
    List<Integer> cancelList;
}
