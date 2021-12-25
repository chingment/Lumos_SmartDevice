package com.caterbao.lumos.locals.common.web;


import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

public class BaseExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResult IllegalArgumentException(IllegalArgumentException ex) {
        return CustomResult.exception("BAD_REQUEST");
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomResult noHandlerFoundException(Exception ex) {
        return CustomResult.exception("请求不存在");
    }

    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomResult unknownException(Exception ex) {

        return CustomResult.exception("服务器内部错误");
    }
}
