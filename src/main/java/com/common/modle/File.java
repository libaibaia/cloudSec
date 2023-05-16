package com.common.modle;

public class File {
    private String key;
    private String owner;
    private String url;
    private String bucketName;
    private Integer bucketId;



    public File() {
    }

    public File(String key, String owner, String url, String bucketName,Integer bucketId) {
        this.key = key;
        this.owner = owner;
        this.url = url;
        this.bucketId = bucketId;
        this.bucketName = bucketName;
    }
    public Integer getBucketId() {
        return bucketId;
    }

    public void setBucketId(Integer bucketId) {
        this.bucketId = bucketId;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
