package com.utry.openticket.dao;

import com.utry.openticket.model.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDAO {

    UserDO getUser(UserDO user);
}
