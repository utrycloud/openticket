package com.utry.openticket.model;

import java.io.Serializable;

public class PermissionDO implements Serializable {
    private Integer id;
    private String name;
    private String uri;
    private Integer pid;
    private Integer funcOrder;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getFuncOrder() {
        return funcOrder;
    }

    public void setFuncOrder(Integer funcOrder) {
        this.funcOrder = funcOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PermissionDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", pid=" + pid +
                ", funcOrder=" + funcOrder +
                ", description='" + description + '\'' +
                '}';
    }
}
