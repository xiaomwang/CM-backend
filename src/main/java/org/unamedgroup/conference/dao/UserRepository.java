package org.unamedgroup.conference.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unamedgroup.conference.entity.User;

import java.util.List;

/**
 * 用户相关 Repository
 *
 * @author liumengxiao
 */

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * 根据用户ID返回用户实体
     *
     * @param userId 用户ID
     * @return 用户实体
     */
    User getUserByUserID(Integer userId);

    /**
     * 返回所有用户
     *
     * @return 所有用户
     */
    @Override
    List<User> findAll();

    /**
     * 根据用户的提交的手机号返回用户实体
     *
     * @param phoneNumber 用户手机号
     * @return 用户实体
     */
    User getUserByPhoneNumber(String phoneNumber);

    /**
     * 根据传入的部分姓名匹配用户实体
     *
     * @param realName 用户的真实姓名（部分）
     * @return 用户实体列表
     */
    List<User> findUsersByRealNameLike(String realName);

    /**
     * 通过电子邮箱返回用户实体
     *
     * @param email 电子邮箱
     * @return 用户实体
     */
    User getUserByEmail(String email);
}
