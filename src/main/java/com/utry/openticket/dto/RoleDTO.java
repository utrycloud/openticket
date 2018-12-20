package com.utry.openticket.dto;

import com.utry.openticket.model.RoleDO;

import java.io.Serializable;

public class RoleDTO extends RoleDO implements Serializable {
    private String roleTypeName;

    public String getRoleTypeName() {
        return roleTypeName;
    }

    public void setRoleTypeName(String roleTypeName) {
        this.roleTypeName = roleTypeName;
    }

    @Override
    public String toString() {
        return super.toString()+"RoleDTO{" +
                "roleTypeName='" + roleTypeName + '\'' +
                '}';
    }
}
