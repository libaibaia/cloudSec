package com.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * 
 * @TableName databases_instance
 */
@TableName(value ="databases_instance")
public class DatabasesInstance implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String instanceId;

    /**
     * 
     */
    private String instanceName;

    /**
     * 
     */
    private String domain;

    /**
     * 
     */
    private String region;

    /**
     * 
     */
    private String port;

    /**
     * 
     */
    private Integer keyId;

    /**
     * 
     */
    private String user;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private String whitelist;

    /**
     * 
     */
    private String keyName;

    public DatabasesInstance() {
    }

    public DatabasesInstance(String instanceId, String instanceName, String domain, String region, String port, Integer keyId, String user, String password, String type, String keyName) {
        this.instanceId = instanceId;
        this.instanceName = instanceName;
        this.domain = domain;
        this.region = region;
        this.port = port;
        this.keyId = keyId;
        this.user = user;
        this.password = password;
        this.type = type;
        this.keyName = keyName;
    }

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
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * 
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * 
     */
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * 
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * 
     */
    public String getDomain() {
        return domain;
    }

    /**
     * 
     */
    public void setDomain(String domain) {
        this.domain = domain;
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
    public String getPort() {
        return port;
    }

    /**
     * 
     */
    public void setPort(String port) {
        this.port = port;
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
    public String getUser() {
        return user;
    }

    /**
     * 
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     */
    public String getType() {
        return type;
    }

    /**
     * 
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     */
    public String getWhitelist() {
        return whitelist;
    }

    /**
     * 
     */
    public void setWhitelist(String whitelist) {
        this.whitelist = whitelist;
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
        DatabasesInstance other = (DatabasesInstance) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInstanceId() == null ? other.getInstanceId() == null : this.getInstanceId().equals(other.getInstanceId()))
            && (this.getInstanceName() == null ? other.getInstanceName() == null : this.getInstanceName().equals(other.getInstanceName()))
            && (this.getDomain() == null ? other.getDomain() == null : this.getDomain().equals(other.getDomain()))
            && (this.getRegion() == null ? other.getRegion() == null : this.getRegion().equals(other.getRegion()))
            && (this.getPort() == null ? other.getPort() == null : this.getPort().equals(other.getPort()))
            && (this.getKeyId() == null ? other.getKeyId() == null : this.getKeyId().equals(other.getKeyId()))
            && (this.getUser() == null ? other.getUser() == null : this.getUser().equals(other.getUser()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getWhitelist() == null ? other.getWhitelist() == null : this.getWhitelist().equals(other.getWhitelist()))
            && (this.getKeyName() == null ? other.getKeyName() == null : this.getKeyName().equals(other.getKeyName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInstanceId() == null) ? 0 : getInstanceId().hashCode());
        result = prime * result + ((getInstanceName() == null) ? 0 : getInstanceName().hashCode());
        result = prime * result + ((getDomain() == null) ? 0 : getDomain().hashCode());
        result = prime * result + ((getRegion() == null) ? 0 : getRegion().hashCode());
        result = prime * result + ((getPort() == null) ? 0 : getPort().hashCode());
        result = prime * result + ((getKeyId() == null) ? 0 : getKeyId().hashCode());
        result = prime * result + ((getUser() == null) ? 0 : getUser().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getWhitelist() == null) ? 0 : getWhitelist().hashCode());
        result = prime * result + ((getKeyName() == null) ? 0 : getKeyName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", instanceId=").append(instanceId);
        sb.append(", instanceName=").append(instanceName);
        sb.append(", domain=").append(domain);
        sb.append(", region=").append(region);
        sb.append(", port=").append(port);
        sb.append(", keyId=").append(keyId);
        sb.append(", user=").append(user);
        sb.append(", password=").append(password);
        sb.append(", type=").append(type);
        sb.append(", whitelist=").append(whitelist);
        sb.append(", keyName=").append(keyName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}