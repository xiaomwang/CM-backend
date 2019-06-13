package org.unamedgroup.conference.entity.temp;

import lombok.Data;

/**
 * @Author: 白振宇
 * @Date： 2019/6/13 14:22
 */
@Data
public class CountMap {
    private Integer value;
    private String name;
    public CountMap(){

    }
    public CountMap(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
}
