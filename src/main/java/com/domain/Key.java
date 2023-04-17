package com.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private Integer id;

    /**
     * 
     */
    private String secretid;

    /**
     * 
     */
    private String secretkey;

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
            && (this.getExpirationTime() == null ? other.getExpirationTime() == null : this.getExpirationTime().equals(other.getExpirationTime()));
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
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}