package com.utry.openticket.dto;

/**
 * @Description : 自定义列信息DTO
 * @author : LVDING
 * @version : 1.0
 * @date : 2018/07/26
 */

import java.io.Serializable;
import java.util.List;

public class TicketFieldDTO2 implements Serializable {
    private String field;
    private String title;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
