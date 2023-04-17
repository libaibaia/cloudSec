package com.common.aliyun.product;

import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.Bucket;
import com.domain.Key;

import java.util.List;

public class OSS {
    public static List<Bucket> getBucketLists(String accessKeyId,String accessKeySecret){
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        com.aliyun.oss.OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        return ossClient.listBuckets();
    }
}
