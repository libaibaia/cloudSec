package com.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * 
 * @TableName huawei_ecs_region
 */
@TableName(value ="huawei_ecs_region")
public class HuaweiEcsRegion implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private String regionName;

    /**
     * 
     */
    private String regionId;

    /**
     * 
     */
    private String endpoint;

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
    public String getRegionName() {
        return regionName;
    }

    /**
     * 
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * 
     */
    public String getRegionId() {
        return regionId;
    }

    /**
     * 
     */
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    /**
     * 
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * 
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
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
        HuaweiEcsRegion other = (HuaweiEcsRegion) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRegionName() == null ? other.getRegionName() == null : this.getRegionName().equals(other.getRegionName()))
            && (this.getRegionId() == null ? other.getRegionId() == null : this.getRegionId().equals(other.getRegionId()))
            && (this.getEndpoint() == null ? other.getEndpoint() == null : this.getEndpoint().equals(other.getEndpoint()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRegionName() == null) ? 0 : getRegionName().hashCode());
        result = prime * result + ((getRegionId() == null) ? 0 : getRegionId().hashCode());
        result = prime * result + ((getEndpoint() == null) ? 0 : getEndpoint().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", regionName=").append(regionName);
        sb.append(", regionId=").append(regionId);
        sb.append(", endpoint=").append(endpoint);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}