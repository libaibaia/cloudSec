package com.common.qiniu.base.model;

public class BucketModel {
    private String name;
    private String region;
    private String[] domain;

    public BucketModel(String name, String region, String[] domain) {
        this.name = name;
        this.region = region;
        this.domain = domain;
    }

    public BucketModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public String[] getDomain() {
        return domain;
    }

    public void setDomain(String[] domain) {
        this.domain = domain;
    }
}
