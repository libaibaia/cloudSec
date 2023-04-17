package com.exception;

import com.common.data.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice
public class ExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultModel exceptionHandler(Exception e){
        logger.error(e.getMessage());
        return new ResultModel(e.getMessage(),false,ResultModel.failedCode);
    }
}
