package com.common.qiniu.base.model.qvm;

import java.util.List;

public class SecurityGroupIds {
    private List<String> security_group_id;
    private String security_group_name;

    public List<String> getSecurity_group_id() {
        return security_group_id;
    }

    public void setSecurity_group_id(List<String> security_group_id) {
        this.security_group_id = security_group_id;
    }

    public String getSecurity_group_name() {
        return security_group_name;
    }

    public void setSecurity_group_name(String security_group_name) {
        this.security_group_name = security_group_name;
    }
}
