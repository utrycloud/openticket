package com.utry.openticket.dao;

import com.utry.openticket.model.UserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDAO {
    /**
     * 获得用户list
     * @return
     */
    List<UserDO> getUserList();

    UserDO getUser(UserDO user);
}
