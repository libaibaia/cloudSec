package com.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName key
 */
@TableName(value ="key")
public class Key implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String secretId;

    /**
     * 
     */
    private String secretKey;

    /**
     * 
     */
    private Integer createById;

    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private String token;

    /**
     * 
     */
    private String isTemporary;

    /**
     * 
     */
    private Date expirationTime;

    /**
     * 
     */
    private String bucketName;

    /**
     * 
     */
    private String taskStatus;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private String remark;

    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    /**
     * 
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateTime;

    /**
     * 
     */
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
    public String getSecretId() {
        return secretId;
    }

    /**
     * 
     */
    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    /**
     * 
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * 
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
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
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * 
     */
    public void setUpdateTime(String updateTime) {
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
}