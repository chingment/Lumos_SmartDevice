package com.caterbao.lumos.locals.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomResult<T>  {
    private Integer code;
    private String msg;

    //设置当NULL时不返回
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public CustomResult<T> fail(String msg) {
        this.msg = msg;
        this.code = 2000;
        this.data = null;
        return this;
    }

    public CustomResult<T> fail(int code, String msg) {
        this.msg = msg;
        this.code = code;
        this.data = null;
        return this;
    }


    public CustomResult<T> success(String msg) {
        this.msg = msg;
        this.code = 1000;
        this.data = null;
        return this;
    }

    public CustomResult<T> success(String msg, T data) {
        this.msg = msg;
        this.code = 1000;
        this.data = data;
        return this;
    }

//    public static CustomResult set(Integer code, String msg, Object object) {
//        CustomResult result = new CustomResult();
//        result.setCode(code);
//        result.setMsg(msg);
//        result.setData(object);
//        return result;
//    }
//
//    public static CustomResult success() {
//        return CustomResult.set(1000,"成功",null);
//    }
//
//    public static CustomResult success(String msg) {
//        return CustomResult.set(1000,msg,null);
//    }
//
//    public static CustomResult success(String msg, Object object) {
//        return CustomResult.set(1000,msg,object);
//    }
//
//    public static CustomResult success(Object object) {
//        return CustomResult.set(1000,"成功",object);
//    }
//
//    public static CustomResult fail(String msg) {
//        return CustomResult.set(2000,msg,null);
//    }
//
//    public static CustomResult exception() {
//        return CustomResult.set(3000,"异常",null);
//    }
//
//    public static CustomResult exception(String msg) {
//        return CustomResult.set(3000,msg,null);
//    }
//
//    public static CustomResult exception(Object object) {
//        return CustomResult.set(3000,"异常",object);
//    }

    public String toJSONString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return null;
        }
    }
}
