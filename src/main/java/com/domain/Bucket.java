package com.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * 
 * @TableName bucket
 */
@TableName(value ="bucket")
public class Bucket implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String region;

    /**
     * 
     */
    private String endPoint;

    /**
     * 
     */
    private String owner;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer keyId;

    /**
     * 
     */
    private Integer createById;

    /**
     * 
     */
    private String keyName;

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
    public String getEndPoint() {
        return endPoint;
    }

    /**
     * 
     */
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * 
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * 
     */
    public void setName(String name) {
        this.name = name;
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
    public Integer getCreateById() {
        return createById;
    }

    /**
     * 
     */
    public void setCreateById(Integer createById) {
        this.createById = createById;
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
        Bucket other = (Bucket) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRegion() == null ? other.getRegion() == null : this.getRegion().equals(other.getRegion()))
            && (this.getEndPoint() == null ? other.getEndPoint() == null : this.getEndPoint().equals(other.getEndPoint()))
            && (this.getOwner() == null ? other.getOwner() == null : this.getOwner().equals(other.getOwner()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getKeyId() == null ? other.getKeyId() == null : this.getKeyId().equals(other.getKeyId()))
            && (this.getCreateById() == null ? other.getCreateById() == null : this.getCreateById().equals(other.getCreateById()))
            && (this.getKeyName() == null ? other.getKeyName() == null : this.getKeyName().equals(other.getKeyName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRegion() == null) ? 0 : getRegion().hashCode());
        result = prime * result + ((getEndPoint() == null) ? 0 : getEndPoint().hashCode());
        result = prime * result + ((getOwner() == null) ? 0 : getOwner().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getKeyId() == null) ? 0 : getKeyId().hashCode());
        result = prime * result + ((getCreateById() == null) ? 0 : getCreateById().hashCode());
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
        sb.append(", region=").append(region);
        sb.append(", endPoint=").append(endPoint);
        sb.append(", owner=").append(owner);
        sb.append(", name=").append(name);
        sb.append(", keyId=").append(keyId);
        sb.append(", createById=").append(createById);
        sb.append(", keyName=").append(keyName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}