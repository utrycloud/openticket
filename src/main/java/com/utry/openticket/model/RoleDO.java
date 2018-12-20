package com.utry.openticket.model;

import java.io.Serializable;

public class RoleDO implements Serializable {
    private Integer id;
    private String name;
    private Integer roleTypeId;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoleTypeId() {
        return roleTypeId;
    }

    public void setRoleTypeId(Integer roleTypeId) {
        this.roleTypeId = roleTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RoleDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roleTypeId=" + roleTypeId +
                ", description='" + description + '\'' +
                '}';
    }
}
