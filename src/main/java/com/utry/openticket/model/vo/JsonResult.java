package com.utry.openticket.model.vo;

import java.io.Serializable;

public class JsonResult implements Serializable {
    private int code;
    private Object data;
    private String msg;

    public static JsonResult success(){
        return new JsonResult(200);
    }
    public static JsonResult success(Object data){
        return new JsonResult(200,data);
    }

    public JsonResult(){

    }
    public JsonResult(int code){
        this.code=code;
    }
    public JsonResult(int code,String message){
        this.code=code;
        this.msg=message;
    }
    public JsonResult(int code,Object data){
        this.code=code;
        this.data=data;
    }
    public JsonResult(int code,Object data,String message){
        this.code=code;
        this.data=data;
        this.msg=message;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
