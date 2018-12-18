package com.utry.openticket.dao;

import com.utry.openticket.model.PermissionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IPermissionDAO {
    /**
     * 获得所有权限
     * @return
     */
    List<PermissionDO> getPermissionList();

    /**
     * 获得用户权限
     * @return
     */
    List<PermissionDO> getUserPermissions(Integer userId);
}
