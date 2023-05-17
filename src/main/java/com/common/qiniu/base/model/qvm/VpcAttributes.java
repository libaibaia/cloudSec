package com.common.qiniu.base.model.qvm;

public class VpcAttributes {
    private String nat_ip_address;
    private PrivateIpAddress private_ip_address;
    private String v_switch_id;
    private String vpc_id;
    private String vpc_name;
    private String v_switch_name;

    public String getNat_ip_address() {
        return nat_ip_address;
    }

    public void setNat_ip_address(String nat_ip_address) {
        this.nat_ip_address = nat_ip_address;
    }

    public PrivateIpAddress getPrivate_ip_address() {
        return private_ip_address;
    }

    public void setPrivate_ip_address(PrivateIpAddress private_ip_address) {
        this.private_ip_address = private_ip_address;
    }

    public String getV_switch_id() {
        return v_switch_id;
    }

    public void setV_switch_id(String v_switch_id) {
        this.v_switch_id = v_switch_id;
    }

    public String getVpc_id() {
        return vpc_id;
    }

    public void setVpc_id(String vpc_id) {
        this.vpc_id = vpc_id;
    }

    public String getVpc_name() {
        return vpc_name;
    }

    public void setVpc_name(String vpc_name) {
        this.vpc_name = vpc_name;
    }

    public String getV_switch_name() {
        return v_switch_name;
    }

    public void setV_switch_name(String v_switch_name) {
        this.v_switch_name = v_switch_name;
    }
}
