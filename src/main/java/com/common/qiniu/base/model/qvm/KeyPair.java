package com.common.qiniu.base.model.qvm;

import com.common.qiniu.base.model.error.ErrorModel;

public class KeyPair {
    private String id;
    private String key_pair_name;
    private String key_pair_finger_print;
    private String public_key_body;
    private String private_key_body;
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey_pair_name() {
        return key_pair_name;
    }

    public void setKey_pair_name(String key_pair_name) {
        this.key_pair_name = key_pair_name;
    }

    public String getKey_pair_finger_print() {
        return key_pair_finger_print;
    }

    public void setKey_pair_finger_print(String key_pair_finger_print) {
        this.key_pair_finger_print = key_pair_finger_print;
    }

    public String getPublic_key_body() {
        return public_key_body;
    }

    public void setPublic_key_body(String public_key_body) {
        this.public_key_body = public_key_body;
    }

    public String getPrivate_key_body() {
        return private_key_body;
    }

    public void setPrivate_key_body(String private_key_body) {
        this.private_key_body = private_key_body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
