package com.common.aws;


import cn.hutool.core.util.StrUtil;
import com.domain.Bucket;
import com.domain.Key;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class S3 {

    /*
    初始化存储桶所有区域
     */
    private static final List<Region> regions = Region.regions();
    public static AwsCredentialsProvider getBaseAuth(Key key){
        AwsCredentialsProvider credentialsProvider;
        if (StrUtil.isBlank(key.getToken())){
            credentialsProvider = StaticCredentialsProvider
                    .create(AwsBasicCredentials.create(key.getSecretid(), key.getSecretkey()));
        }else {
            credentialsProvider = StaticCredentialsProvider.create(
                            AwsSessionCredentials.create(
                                    key.getSecretid(),
                                    key.getSecretkey(),
                                    key.getToken()));

        }
        return credentialsProvider;
    }

    public static Set<Bucket> getBucketList(Key key){
        Set<Bucket> list = new HashSet<>();
        for (Region region : regions) {
            S3Client s3Client = S3Client.builder()
                    .credentialsProvider(getBaseAuth(key))
                    .region(region)
                    .build();
            try {
                ListBucketsResponse listBucketsResponse = s3Client.listBuckets();
                if (!listBucketsResponse.buckets().isEmpty()){
                    for (software.amazon.awssdk.services.s3.model.Bucket bucket : listBucketsResponse.buckets()) {
                        Bucket bucket1 = new Bucket();
                        bucket1.setName(bucket.name());
                        bucket1.setRegion(region.id());
                        list.add(bucket1);
                    }
                }
            }catch (Exception e){
                System.out.println("当前不存在存储桶，区域：" + region.id());
            }
        }
        return list;
    }

    public static void getFileLists(Key key, Bucket bucket){
        S3Client s3Client = S3Client.builder()
                .credentialsProvider(getBaseAuth(key))
                .region(Region.of(bucket.getRegion()))
                .build();
        ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(bucket.getName())
                .build();
        ListObjectsResponse res = s3Client.listObjects(listObjects);
        List<S3Object> objects = res.contents();
        for (S3Object object : objects) {
            System.out.println("文件名---：" + object.key());
            System.out.println("文件名---：" + object.size() / 1024);
        }
    }

    public static void main(String[] args) {
        Key key = new Key();
//        key.setSecretid("AKIAVERX2TF6ZHGPJV5S1");
//        key.setSecretkey("MLZyis4pnGF35/E2osdAv1eGcbsZiMHMyHAmDdtk1");
        key.setSecretid("AKIAJMHS2B77BWZWWPQMQ1");
        key.setSecretkey("hbYXf4JC2PbDf4akPKkPsllQYJHMrYbkqZBPbH7dt");
        Set<Bucket> bucketList = getBucketList(key);
        System.out.println(bucketList);
    }
}
