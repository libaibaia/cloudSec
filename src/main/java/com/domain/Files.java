package com.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * @TableName files
 */
@TableName(value ="files")
public class Files implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    @JsonProperty
    private String filePath;

    /**
     * 
     */
    private String tpye;

    /**
     * 
     */
    private String originalFileName;

    /**
     * 
     */
    private String fileSize;

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
    public Integer getUserId() {
        return userId;
    }

    /**
     * 
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 
     */
    public String getTpye() {
        return tpye;
    }

    /**
     * 
     */
    public void setTpye(String tpye) {
        this.tpye = tpye;
    }

    /**
     * 
     */
    public String getOriginalFileName() {
        return originalFileName;
    }

    /**
     * 
     */
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    /**
     * 
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * 
     */
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
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
        Files other = (Files) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getFilePath() == null ? other.getFilePath() == null : this.getFilePath().equals(other.getFilePath()))
            && (this.getTpye() == null ? other.getTpye() == null : this.getTpye().equals(other.getTpye()))
            && (this.getOriginalFileName() == null ? other.getOriginalFileName() == null : this.getOriginalFileName().equals(other.getOriginalFileName()))
            && (this.getFileSize() == null ? other.getFileSize() == null : this.getFileSize().equals(other.getFileSize()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getFilePath() == null) ? 0 : getFilePath().hashCode());
        result = prime * result + ((getTpye() == null) ? 0 : getTpye().hashCode());
        result = prime * result + ((getOriginalFileName() == null) ? 0 : getOriginalFileName().hashCode());
        result = prime * result + ((getFileSize() == null) ? 0 : getFileSize().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", filePath=").append(filePath);
        sb.append(", tpye=").append(tpye);
        sb.append(", originalFileName=").append(originalFileName);
        sb.append(", fileSize=").append(fileSize);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}