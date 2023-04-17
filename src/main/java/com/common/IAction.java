package com.common;


import com.common.data.ResultModel;
import com.tencentcloudapi.common.Credential;

@FunctionalInterface
public interface IAction {
    ResultModel execute(Credential obj);

}
