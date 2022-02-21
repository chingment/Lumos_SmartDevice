package com.caterbao.lumos.locals.common;

public class CumstomResultUtil {

    public static CustomResult set(Integer code, String msg, Object object) {
        CustomResult result = new CustomResult();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(object);
        return result;
    }

    public static CustomResult success() {
        return set(1000,"成功",null);
    }

    public static CustomResult success(String msg) {
        return set(1000,msg,null);
    }

    public static CustomResult success(String msg, Object object) {
        return set(1000,msg,object);
    }

    public static CustomResult success(Object object) {
        return set(1000,"成功",object);
    }

    public static CustomResult fail(String msg) {
        return set(2000,msg,null);
    }

    public static CustomResult exception() {
        return set(3000,"异常",null);
    }

    public static CustomResult exception(String msg) {
        return set(3000,msg,null);
    }

    public static CustomResult exception(Object object) {
        return set(3000,"异常",object);
    }
}
