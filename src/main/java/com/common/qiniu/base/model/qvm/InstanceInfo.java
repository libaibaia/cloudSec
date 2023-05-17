package com.common.qiniu.base.model.qvm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class InstanceInfo {
    private String _id;
    private long uid;
    private String instance_id;
    private ImageInfo image_info;
    private String vlan_id;
    private String zone_id;
    private String spot_strategy;
    private String stopped_mode;
    private String serial_number;
    private boolean io_optimized;
    private int memory;
    private int cpu;
    private boolean device_available;
    private String sale_cycle;
    private double spot_price_limit;
    private String auto_release_time;
    private String instance_name;
    private String description;
    private String resource_group_id;
    private String os_type;
    private String os_name;
    private String host_name;
    private String instance_type;
    private String instance_type_family;
    private String status;
    private String cluster_id;
    private boolean recyclable;
    private String region_id;
    private String gpu_spec;
    private int gpu_amount;
    private String vnc_passwd;
    private String key_pair_name;
    private String instance_charge_type;
    private String instance_network_type;
    private String expired_time;
    private NetworkInterfaces network_interfaces;
    private VpcAttributes vpc_attributes;
    private SecurityGroupIds security_group_ids;
    private OperationLocks operation_locks;
    private InnerIpAddress inner_ip_address;
    private EipAddress eip_address;
    private PublicIpAddress public_ip_address;
    private String human_spec;
    private boolean enable_assign_ipv6;
    private List<String> tags;
    private String cost_charge_type;
    private String cost_charge_mode;
    private String updated_at;
    private String created_at;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getInstance_id() {
        return instance_id;
    }

    public void setInstance_id(String instance_id) {
        this.instance_id = instance_id;
    }

    public ImageInfo getImage_info() {
        return image_info;
    }

    public void setImage_info(ImageInfo image_info) {
        this.image_info = image_info;
    }

    public String getVlan_id() {
        return vlan_id;
    }

    public void setVlan_id(String vlan_id) {
        this.vlan_id = vlan_id;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getSpot_strategy() {
        return spot_strategy;
    }

    public void setSpot_strategy(String spot_strategy) {
        this.spot_strategy = spot_strategy;
    }

    public String getStopped_mode() {
        return stopped_mode;
    }

    public void setStopped_mode(String stopped_mode) {
        this.stopped_mode = stopped_mode;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public boolean isIo_optimized() {
        return io_optimized;
    }

    public void setIo_optimized(boolean io_optimized) {
        this.io_optimized = io_optimized;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public boolean isDevice_available() {
        return device_available;
    }

    public void setDevice_available(boolean device_available) {
        this.device_available = device_available;
    }

    public String getSale_cycle() {
        return sale_cycle;
    }

    public void setSale_cycle(String sale_cycle) {
        this.sale_cycle = sale_cycle;
    }

    public double getSpot_price_limit() {
        return spot_price_limit;
    }

    public void setSpot_price_limit(double spot_price_limit) {
        this.spot_price_limit = spot_price_limit;
    }

    public String getAuto_release_time() {
        return auto_release_time;
    }

    public void setAuto_release_time(String auto_release_time) {
        this.auto_release_time = auto_release_time;
    }

    public String getInstance_name() {
        return instance_name;
    }

    public void setInstance_name(String instance_name) {
        this.instance_name = instance_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResource_group_id() {
        return resource_group_id;
    }

    public void setResource_group_id(String resource_group_id) {
        this.resource_group_id = resource_group_id;
    }

    public String getOs_type() {
        return os_type;
    }

    public void setOs_type(String os_type) {
        this.os_type = os_type;
    }

    public String getOs_name() {
        return os_name;
    }

    public void setOs_name(String os_name) {
        this.os_name = os_name;
    }

    public String getHost_name() {
        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public String getInstance_type() {
        return instance_type;
    }

    public void setInstance_type(String instance_type) {
        this.instance_type = instance_type;
    }

    public String getInstance_type_family() {
        return instance_type_family;
    }

    public void setInstance_type_family(String instance_type_family) {
        this.instance_type_family = instance_type_family;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }

    public boolean isRecyclable() {
        return recyclable;
    }

    public void setRecyclable(boolean recyclable) {
        this.recyclable = recyclable;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getGpu_spec() {
        return gpu_spec;
    }

    public void setGpu_spec(String gpu_spec) {
        this.gpu_spec = gpu_spec;
    }

    public int getGpu_amount() {
        return gpu_amount;
    }

    public void setGpu_amount(int gpu_amount) {
        this.gpu_amount = gpu_amount;
    }

    public String getVnc_passwd() {
        return vnc_passwd;
    }

    public void setVnc_passwd(String vnc_passwd) {
        this.vnc_passwd = vnc_passwd;
    }

    public String getKey_pair_name() {
        return key_pair_name;
    }

    public void setKey_pair_name(String key_pair_name) {
        this.key_pair_name = key_pair_name;
    }

    public String getInstance_charge_type() {
        return instance_charge_type;
    }

    public void setInstance_charge_type(String instance_charge_type) {
        this.instance_charge_type = instance_charge_type;
    }

    public String getInstance_network_type() {
        return instance_network_type;
    }

    public void setInstance_network_type(String instance_network_type) {
        this.instance_network_type = instance_network_type;
    }

    public String getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(String expired_time) {
        this.expired_time = expired_time;
    }

    public NetworkInterfaces getNetwork_interfaces() {
        return network_interfaces;
    }

    public void setNetwork_interfaces(NetworkInterfaces network_interfaces) {
        this.network_interfaces = network_interfaces;
    }

    public VpcAttributes getVpc_attributes() {
        return vpc_attributes;
    }

    public void setVpc_attributes(VpcAttributes vpc_attributes) {
        this.vpc_attributes = vpc_attributes;
    }

    public SecurityGroupIds getSecurity_group_ids() {
        return security_group_ids;
    }

    public void setSecurity_group_ids(SecurityGroupIds security_group_ids) {
        this.security_group_ids = security_group_ids;
    }

    public OperationLocks getOperation_locks() {
        return operation_locks;
    }

    public void setOperation_locks(OperationLocks operation_locks) {
        this.operation_locks = operation_locks;
    }

    public InnerIpAddress getInner_ip_address() {
        return inner_ip_address;
    }

    public void setInner_ip_address(InnerIpAddress inner_ip_address) {
        this.inner_ip_address = inner_ip_address;
    }

    public EipAddress getEip_address() {
        return eip_address;
    }

    public void setEip_address(EipAddress eip_address) {
        this.eip_address = eip_address;
    }

    public PublicIpAddress getPublic_ip_address() {
        return public_ip_address;
    }

    public void setPublic_ip_address(PublicIpAddress public_ip_address) {
        this.public_ip_address = public_ip_address;
    }

    public String getHuman_spec() {
        return human_spec;
    }

    public void setHuman_spec(String human_spec) {
        this.human_spec = human_spec;
    }

    public boolean isEnable_assign_ipv6() {
        return enable_assign_ipv6;
    }

    public void setEnable_assign_ipv6(boolean enable_assign_ipv6) {
        this.enable_assign_ipv6 = enable_assign_ipv6;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCost_charge_type() {
        return cost_charge_type;
    }

    public void setCost_charge_type(String cost_charge_type) {
        this.cost_charge_type = cost_charge_type;
    }

    public String getCost_charge_mode() {
        return cost_charge_mode;
    }

    public void setCost_charge_mode(String cost_charge_mode) {
        this.cost_charge_mode = cost_charge_mode;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    // getters and setters
}
