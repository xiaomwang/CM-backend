package org.unamedgroup.conference.entity.temp;

import lombok.Data;
import org.unamedgroup.conference.entity.Room;

import java.util.List;
import java.util.Set;

/**
 * @Author: 白振宇
 * @Date： 2019/5/30 12:06
 */
@Data
public class PageRoom {
    Integer total;
    Set<Room> roomSet;

    public PageRoom(Integer total, Set<Room> roomSet) {
        this.total = total;
        this.roomSet.containsAll(roomSet);
    }
}
