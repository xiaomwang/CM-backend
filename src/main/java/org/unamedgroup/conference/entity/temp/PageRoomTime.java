package org.unamedgroup.conference.entity.temp;

import lombok.Data;

import java.util.List;

/**
 * @Author: 白振宇
 * @Date： 2019/6/4 20:59
 */
@Data
public class PageRoomTime {
    Integer total;
    List<RoomTime> roomTimeList;

    public PageRoomTime(Integer total, List<RoomTime> roomTimeList) {
        this.total = total;
        this.roomTimeList = roomTimeList;
    }
}
