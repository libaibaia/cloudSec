package com.common.qiniu.base.model.qvm;

public class EipAddress {
    private String ip_address;
    private String allocation_id;
    private String internet_charge_type;
    private boolean is_support_unassociate;
    private int bandwidth;

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getAllocation_id() {
        return allocation_id;
    }

    public void setAllocation_id(String allocation_id) {
        this.allocation_id = allocation_id;
    }

    public String getInternet_charge_type() {
        return internet_charge_type;
    }

    public void setInternet_charge_type(String internet_charge_type) {
        this.internet_charge_type = internet_charge_type;
    }

    public boolean isIs_support_unassociate() {
        return is_support_unassociate;
    }

    public void setIs_support_unassociate(boolean is_support_unassociate) {
        this.is_support_unassociate = is_support_unassociate;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }
}
