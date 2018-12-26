package com.utry.openticket.dao;

import com.utry.openticket.model.UserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDAO {
    /**
     * 获得所有用户信息，不分页
     * @return
     */
    List<UserDO> getUserList();

    List<UserDO> getUserByUsername(String username);

    UserDO getUser(UserDO user);

    int saveUser(UserDO user);

    int deleteUser(Integer id);

    UserDO getUserById(Integer id);

    int updateUser(UserDO user);

    List<Integer> getRoleTypeIdByUserId(Integer userId);
}
