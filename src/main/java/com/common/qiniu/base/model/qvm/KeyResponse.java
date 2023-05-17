package com.common.qiniu.base.model.qvm;

import java.util.List;

public class KeyResponse {
    private KeyPair data;
    private String request_id;


    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public KeyPair getData() {
        return data;
    }

    public void setData(KeyPair data) {
        this.data = data;
    }
}
