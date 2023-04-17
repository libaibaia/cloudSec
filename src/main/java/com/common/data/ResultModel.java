package com.common.data;

import java.util.Map;

public class ResultModel {
    public static final Integer successCode = 200;
    public static final Integer failedCode = 500;
    private Map<?,?> data;

    public Map<?,?> getData() {
        return data;
    }

    public void setData(Map<?,?> data) {
        this.data = data;
    }

    private String message;
    private boolean success;
    private Integer code;

    public ResultModel(String message, boolean success, Integer code) {
        this.message = message;
        this.success = success;
        this.code = code;
    }

    public ResultModel(Map<?, ?> data, String message, boolean success, Integer code) {
        this.data = data;
        this.message = message;
        this.success = success;
        this.code = code;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
