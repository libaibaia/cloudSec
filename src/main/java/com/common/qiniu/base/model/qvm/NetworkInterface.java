package com.common.qiniu.base.model.qvm;

public class NetworkInterface {
    private String network_interface_id;
    private String mac_address;
    private String primary_ip_address;

    public String getNetwork_interface_id() {
        return network_interface_id;
    }

    public void setNetwork_interface_id(String network_interface_id) {
        this.network_interface_id = network_interface_id;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getPrimary_ip_address() {
        return primary_ip_address;
    }

    public void setPrimary_ip_address(String primary_ip_address) {
        this.primary_ip_address = primary_ip_address;
    }
}
