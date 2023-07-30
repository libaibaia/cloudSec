package com.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * 
 * @TableName cluster
 */
@TableName(value ="cluster")
public class Cluster implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String clusterId;

    /**
     * 
     */
    private String clusterName;

    /**
     * 
     */
    private String clusterDescription;

    /**
     * 
     */
    private String clusterOs;

    /**
     * 
     */
    private String clusterType;

    /**
     * 
     */
    private String clusterVersion;

    /**
     * 
     */
    private String networkInfo;

    /**
     * 
     */
    private String containerRuntime;

    /**
     * 
     */
    private String region;

    /**
     * 
     */
    private Integer keyId;

    /**
     * 
     */
    private String keyName;

    /**
     * 
     */
    private String endpointInfo;

    /**
     * 
     */
    private String endpointSecurityGroup;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     */
    public String getClusterId() {
        return clusterId;
    }

    /**
     * 
     */
    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    /**
     * 
     */
    public String getClusterName() {
        return clusterName;
    }

    /**
     * 
     */
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    /**
     * 
     */
    public String getClusterDescription() {
        return clusterDescription;
    }

    /**
     * 
     */
    public void setClusterDescription(String clusterDescription) {
        this.clusterDescription = clusterDescription;
    }

    /**
     * 
     */
    public String getClusterOs() {
        return clusterOs;
    }

    /**
     * 
     */
    public void setClusterOs(String clusterOs) {
        this.clusterOs = clusterOs;
    }

    /**
     * 
     */
    public String getClusterType() {
        return clusterType;
    }

    /**
     * 
     */
    public void setClusterType(String clusterType) {
        this.clusterType = clusterType;
    }

    /**
     * 
     */
    public String getClusterVersion() {
        return clusterVersion;
    }

    /**
     * 
     */
    public void setClusterVersion(String clusterVersion) {
        this.clusterVersion = clusterVersion;
    }

    /**
     * 
     */
    public String getNetworkInfo() {
        return networkInfo;
    }

    /**
     * 
     */
    public void setNetworkInfo(String networkInfo) {
        this.networkInfo = networkInfo;
    }

    /**
     * 
     */
    public String getContainerRuntime() {
        return containerRuntime;
    }

    /**
     * 
     */
    public void setContainerRuntime(String containerRuntime) {
        this.containerRuntime = containerRuntime;
    }

    /**
     * 
     */
    public String getRegion() {
        return region;
    }

    /**
     * 
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 
     */
    public Integer getKeyId() {
        return keyId;
    }

    /**
     * 
     */
    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    /**
     * 
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * 
     */
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    /**
     * 
     */
    public String getEndpointInfo() {
        return endpointInfo;
    }

    /**
     * 
     */
    public void setEndpointInfo(String endpointInfo) {
        this.endpointInfo = endpointInfo;
    }

    /**
     * 
     */
    public String getEndpointSecurityGroup() {
        return endpointSecurityGroup;
    }

    /**
     * 
     */
    public void setEndpointSecurityGroup(String endpointSecurityGroup) {
        this.endpointSecurityGroup = endpointSecurityGroup;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Cluster other = (Cluster) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getClusterId() == null ? other.getClusterId() == null : this.getClusterId().equals(other.getClusterId()))
            && (this.getClusterName() == null ? other.getClusterName() == null : this.getClusterName().equals(other.getClusterName()))
            && (this.getClusterDescription() == null ? other.getClusterDescription() == null : this.getClusterDescription().equals(other.getClusterDescription()))
            && (this.getClusterOs() == null ? other.getClusterOs() == null : this.getClusterOs().equals(other.getClusterOs()))
            && (this.getClusterType() == null ? other.getClusterType() == null : this.getClusterType().equals(other.getClusterType()))
            && (this.getClusterVersion() == null ? other.getClusterVersion() == null : this.getClusterVersion().equals(other.getClusterVersion()))
            && (this.getNetworkInfo() == null ? other.getNetworkInfo() == null : this.getNetworkInfo().equals(other.getNetworkInfo()))
            && (this.getContainerRuntime() == null ? other.getContainerRuntime() == null : this.getContainerRuntime().equals(other.getContainerRuntime()))
            && (this.getRegion() == null ? other.getRegion() == null : this.getRegion().equals(other.getRegion()))
            && (this.getKeyId() == null ? other.getKeyId() == null : this.getKeyId().equals(other.getKeyId()))
            && (this.getKeyName() == null ? other.getKeyName() == null : this.getKeyName().equals(other.getKeyName()))
            && (this.getEndpointInfo() == null ? other.getEndpointInfo() == null : this.getEndpointInfo().equals(other.getEndpointInfo()))
            && (this.getEndpointSecurityGroup() == null ? other.getEndpointSecurityGroup() == null : this.getEndpointSecurityGroup().equals(other.getEndpointSecurityGroup()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getClusterId() == null) ? 0 : getClusterId().hashCode());
        result = prime * result + ((getClusterName() == null) ? 0 : getClusterName().hashCode());
        result = prime * result + ((getClusterDescription() == null) ? 0 : getClusterDescription().hashCode());
        result = prime * result + ((getClusterOs() == null) ? 0 : getClusterOs().hashCode());
        result = prime * result + ((getClusterType() == null) ? 0 : getClusterType().hashCode());
        result = prime * result + ((getClusterVersion() == null) ? 0 : getClusterVersion().hashCode());
        result = prime * result + ((getNetworkInfo() == null) ? 0 : getNetworkInfo().hashCode());
        result = prime * result + ((getContainerRuntime() == null) ? 0 : getContainerRuntime().hashCode());
        result = prime * result + ((getRegion() == null) ? 0 : getRegion().hashCode());
        result = prime * result + ((getKeyId() == null) ? 0 : getKeyId().hashCode());
        result = prime * result + ((getKeyName() == null) ? 0 : getKeyName().hashCode());
        result = prime * result + ((getEndpointInfo() == null) ? 0 : getEndpointInfo().hashCode());
        result = prime * result + ((getEndpointSecurityGroup() == null) ? 0 : getEndpointSecurityGroup().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", clusterId=").append(clusterId);
        sb.append(", clusterName=").append(clusterName);
        sb.append(", clusterDescription=").append(clusterDescription);
        sb.append(", clusterOs=").append(clusterOs);
        sb.append(", clusterType=").append(clusterType);
        sb.append(", clusterVersion=").append(clusterVersion);
        sb.append(", networkInfo=").append(networkInfo);
        sb.append(", containerRuntime=").append(containerRuntime);
        sb.append(", region=").append(region);
        sb.append(", keyId=").append(keyId);
        sb.append(", keyName=").append(keyName);
        sb.append(", endpointInfo=").append(endpointInfo);
        sb.append(", endpointSecurityGroup=").append(endpointSecurityGroup);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}