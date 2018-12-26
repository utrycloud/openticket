package com.utry.openticket.model.vo;

import com.utry.openticket.model.RoleDO;
import com.utry.openticket.model.UserDO;

import java.util.List;

public class UserVO extends UserDO {
    private List<RoleDO> roles;

    public UserVO(UserDO userDO){
        super(userDO.getId(),userDO.getUsername(),userDO.getPassword(),
                userDO.getRealName(),userDO.getTel(), userDO.getEmail(),
                userDO.getCreateTime(),userDO.getUpdateTime());
    }

    //多个角色间用 英文逗号(,) 隔开
    public String getRole(){
        StringBuilder result=new StringBuilder();
        for(RoleDO role:roles){
            result.append(role.getName()).append(",");
        }
        if(result.length()>0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    public List<RoleDO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDO> roles) {
        this.roles = roles;
    }
}
