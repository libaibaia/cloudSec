package com.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName key
 */
@TableName(value ="`key`")
public class Key implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    @ExcelProperty("id")
    private Integer id;

    /**
     * 
     */
    @ExcelProperty("secretid")
    private String secretid;

    /**
     * 
     */
    @ExcelProperty("secretkey")
    private String secretkey;

    /**
     * 
     */
    @ExcelProperty("createById")
    private Integer createById;

    /**
     * 
     */
    @ExcelProperty("类型")
    private String type;

    /**
     * 
     */
    @ExcelProperty("token")
    private String token;

    /**
     * 
     */
    @ExcelProperty("isTemporary")
    private String isTemporary;

    /**
     * 
     */
    @ExcelProperty("expirationTime")
    private Date expirationTime;

    /**
     * 
     */
    @ExcelProperty("bucketName")
    private String bucketName;

    /**
     * 
     */
    @ExcelProperty("taskStatus")
    private String taskStatus;

    /**
     * 
     */
    @ExcelProperty("status")
    private String status;

    /**
     * 
     */
    @ExcelProperty("remark")
    private String remark;

    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT)
    @ExcelProperty("createTime")
    private Date createTime;

    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelProperty("updateTime")
    private Date updateTime;

    /**
     * 
     */
    @ExcelProperty("name")
    private String name;

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
    public String getSecretid() {
        return secretid;
    }

    /**
     * 
     */
    public void setSecretid(String secretid) {
        this.secretid = secretid;
    }

    /**
     * 
     */
    public String getSecretkey() {
        return secretkey;
    }

    /**
     * 
     */
    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
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
    public String getToken() {
        return token;
    }

    /**
     * 
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 
     */
    public String getIsTemporary() {
        return isTemporary;
    }

    /**
     * 
     */
    public void setIsTemporary(String isTemporary) {
        this.isTemporary = isTemporary;
    }

    /**
     * 
     */
    public Date getExpirationTime() {
        return expirationTime;
    }

    /**
     * 
     */
    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * 
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * 
     */
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * 
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    /**
     * 
     */
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * 
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        Key other = (Key) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSecretid() == null ? other.getSecretid() == null : this.getSecretid().equals(other.getSecretid()))
            && (this.getSecretkey() == null ? other.getSecretkey() == null : this.getSecretkey().equals(other.getSecretkey()))
            && (this.getCreateById() == null ? other.getCreateById() == null : this.getCreateById().equals(other.getCreateById()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getToken() == null ? other.getToken() == null : this.getToken().equals(other.getToken()))
            && (this.getIsTemporary() == null ? other.getIsTemporary() == null : this.getIsTemporary().equals(other.getIsTemporary()))
            && (this.getExpirationTime() == null ? other.getExpirationTime() == null : this.getExpirationTime().equals(other.getExpirationTime()))
            && (this.getBucketName() == null ? other.getBucketName() == null : this.getBucketName().equals(other.getBucketName()))
            && (this.getTaskStatus() == null ? other.getTaskStatus() == null : this.getTaskStatus().equals(other.getTaskStatus()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSecretid() == null) ? 0 : getSecretid().hashCode());
        result = prime * result + ((getSecretkey() == null) ? 0 : getSecretkey().hashCode());
        result = prime * result + ((getCreateById() == null) ? 0 : getCreateById().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getToken() == null) ? 0 : getToken().hashCode());
        result = prime * result + ((getIsTemporary() == null) ? 0 : getIsTemporary().hashCode());
        result = prime * result + ((getExpirationTime() == null) ? 0 : getExpirationTime().hashCode());
        result = prime * result + ((getBucketName() == null) ? 0 : getBucketName().hashCode());
        result = prime * result + ((getTaskStatus() == null) ? 0 : getTaskStatus().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", secretid=").append(secretid);
        sb.append(", secretkey=").append(secretkey);
        sb.append(", createById=").append(createById);
        sb.append(", type=").append(type);
        sb.append(", token=").append(token);
        sb.append(", isTemporary=").append(isTemporary);
        sb.append(", expirationTime=").append(expirationTime);
        sb.append(", bucketName=").append(bucketName);
        sb.append(", taskStatus=").append(taskStatus);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", name=").append(name);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}