package com.bf.image.exception;

import com.bf.image.domin.ResultJson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice//标注未全局异常处理
public class GlobalExceptionHandle {

    /**
     * 处理自定义异常
     * @param request
     * @param e
     */
    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public ResultJson customRuntimeException(HttpServletRequest request, CustomException e){
        return ResultJson.fail(e.getMessage());
    }


    /**
     * 处理其他异常
     * @param request
     * @param e
     */
//    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultJson defaultErrorHandler(HttpServletRequest request, Exception e){
        return ResultJson.fail(e.getMessage());
    }


}
