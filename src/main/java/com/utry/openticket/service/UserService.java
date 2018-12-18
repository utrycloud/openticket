package com.utry.openticket.service;

import com.utry.openticket.dao.UserDAO;
import com.utry.openticket.dto.UserDTO;
import com.utry.openticket.model.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public UserDO  getUser(UserDTO user) {
        UserDO userDO = new UserDO();
        userDO.setUsername(user.getUsername());
        userDO.setPassword(user.getPassword());
        userDO = userDAO.getUser(userDO);
        return userDO;
    }

    public List<UserDO> getUserList(){
        return userDAO.getUserList();
    }
}
