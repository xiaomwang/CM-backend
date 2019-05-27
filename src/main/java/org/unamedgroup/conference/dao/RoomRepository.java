package org.unamedgroup.conference.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unamedgroup.conference.entity.Building;
import org.unamedgroup.conference.entity.Room;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {
//    List<Room> getRoomsByFlag(Integer flag);

    /**
     * 根据楼宇查找楼宇内所有的会议室
     * 应王宇辰前端快速查找要求
     *
     * @param building
     * @return 会议室列表
     */
    List<Room> getRoomsByBuilding(Building building);

    /**
     * 根据楼层位置信息返回可用会议室
     * 应王宇辰前端快速查找要求
     *
     * @param location
     * @return 会议室列表
     */
    List<Room> getRoomsByLocation(String location);

    /**
     * 根据房间名得到房间实体
     *
     * @param name
     * @return 房间实体
     */
    Room getRoomByName(String name);

    /**
     * 根据房间号得到房间实体
     *
     * @param roomID
     * @return 房间实体
     */
    Room getRoomByRoomID(Integer roomID);

    /**
     * 返回所有房间
     * 请慎用此方法！
     *
     * @return 所有房间列表
     */
    @Override
    List<Room> findAll();

    List<Room> getRoomsByBuilding(Integer building);

    /**
     * 去重查询所有的会议室类型
     * @return 会议室类型列表
     */
    @Query(value = "select distinct catalogue from Room ")
    List<String> findDistinctCatalogue();
}
