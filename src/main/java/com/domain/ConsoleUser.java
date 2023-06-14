package com.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * 
 * @TableName console_user
 */
@TableName(value ="console_user")
public class ConsoleUser implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String owneruin;

    /**
     * 
     */
    private String uin;

    /**
     * 
     */
    private Integer keyId;

    /**
     * 
     */
    private String loginurl;

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
    public String getUsername() {
        return username;
    }

    /**
     * 
     */
    public void setUsername(String username) {
        this.username = username;
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
    public String getOwneruin() {
        return owneruin;
    }

    /**
     * 
     */
    public void setOwneruin(String owneruin) {
        this.owneruin = owneruin;
    }

    /**
     * 
     */
    public String getUin() {
        return uin;
    }

    /**
     * 
     */
    public void setUin(String uin) {
        this.uin = uin;
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
    public String getLoginurl() {
        return loginurl;
    }

    /**
     * 
     */
    public void setLoginurl(String loginurl) {
        this.loginurl = loginurl;
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
        ConsoleUser other = (ConsoleUser) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getOwneruin() == null ? other.getOwneruin() == null : this.getOwneruin().equals(other.getOwneruin()))
            && (this.getUin() == null ? other.getUin() == null : this.getUin().equals(other.getUin()))
            && (this.getKeyId() == null ? other.getKeyId() == null : this.getKeyId().equals(other.getKeyId()))
            && (this.getLoginurl() == null ? other.getLoginurl() == null : this.getLoginurl().equals(other.getLoginurl()))
            && (this.getKeyName() == null ? other.getKeyName() == null : this.getKeyName().equals(other.getKeyName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getOwneruin() == null) ? 0 : getOwneruin().hashCode());
        result = prime * result + ((getUin() == null) ? 0 : getUin().hashCode());
        result = prime * result + ((getKeyId() == null) ? 0 : getKeyId().hashCode());
        result = prime * result + ((getLoginurl() == null) ? 0 : getLoginurl().hashCode());
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
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", owneruin=").append(owneruin);
        sb.append(", uin=").append(uin);
        sb.append(", keyId=").append(keyId);
        sb.append(", loginurl=").append(loginurl);
        sb.append(", keyName=").append(keyName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}