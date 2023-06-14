package com.common.aws;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.common.modle.OssFileLists;
import com.domain.Bucket;
import com.domain.Key;
import com.domain.Task;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;


import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

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

    public static List<Bucket> getBucketList(Key key){
        List<Region> all_regions = Region.regions();
        List<Bucket> list = new ArrayList<>();

        for (Region region : all_regions) {
            S3Client s3Client = S3Client.builder()
                    .credentialsProvider(getBaseAuth(key))
                    .region(region)
                    .build();
            try {
                ListBucketsResponse listBucketsResponse = s3Client.listBuckets();
                if (!listBucketsResponse.buckets().isEmpty()) {
                    for (software.amazon.awssdk.services.s3.model.Bucket bucket : listBucketsResponse.buckets()) {
                        Bucket b = new Bucket();
                        b.setName(bucket.name());
                        //获取存储桶区域
                        GetBucketLocationRequest request = GetBucketLocationRequest
                                .builder()
                                .bucket(bucket.name())
                                .build();
                        String regionStr = s3Client.getBucketLocation(request).locationConstraintAsString();
                        String s = StrUtil.isBlank(regionStr) ? Region.US_EAST_1.id() : regionStr;
                        b.setKeyId(key.getId());
                        b.setCreateById(key.getCreateById());
                        b.setRegion(s);
                        b.setKeyName(key.getName());
                        b.setEndPoint(String.format("%s.s3-%s.amazonaws.com", bucket.name(),s));
                        list.add(b);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        //去重
        return list.stream()
                .map(Bucket::getName)
                .distinct()
                .map(
                        name -> list.stream()
                        .filter(item -> item.getName().equals(name))
                        .findFirst()
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    /*
    获取文件列表
     */

    public static List<S3Object> getFileLists(Key key, Bucket bucket,String keyWord){
        try {
            S3Client s3Client = S3Client.builder()
                    .credentialsProvider(getBaseAuth(key))
                    .region(Region.of(bucket.getRegion()))
                    .build();
            ListObjectsRequest listObjects;
            if (StrUtil.isBlank(keyWord)){
                 listObjects = ListObjectsRequest
                        .builder()
                        .bucket(bucket.getName())
                        .build();
            }
            else {
                listObjects = ListObjectsRequest
                        .builder()
                        .prefix(keyWord)
                        .bucket(bucket.getName())
                        .build();
            }

            ListObjectsResponse res = s3Client.listObjects(listObjects);
            List<S3Object> objects = res.contents();
            return objects;
        }catch (Exception e){
            return null;
        }
    }

    public static List<S3Object> getAllFileLists(Key key, Bucket bucket){
        try {
            S3Client s3Client = S3Client.builder()
                    .credentialsProvider(getBaseAuth(key))
                    .region(Region.of(bucket.getRegion()))
                    .build();
            List<S3Object> res = new ArrayList<>();

            ListObjectsV2Request listObjectsReqManual = ListObjectsV2Request.builder()
                    .bucket(bucket.getName())
                    .maxKeys(1000)
                    .build();
            ListObjectsV2Response listObjectsResp = s3Client.listObjectsV2(listObjectsReqManual);

            boolean done = false;
            while (!done) {

                res.addAll(listObjectsResp.contents());

                if (listObjectsResp.nextContinuationToken() == null) {
                    done = true;
                }

                listObjectsReqManual = listObjectsReqManual.toBuilder()
                        .continuationToken(listObjectsResp.nextContinuationToken())
                        .build();
                listObjectsResp = s3Client.listObjectsV2(listObjectsReqManual);
            }
            return res;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static Task exportExcel(Key key, Bucket bucket, Task task){
        List<S3Object> allFileLists = getAllFileLists(key, bucket);
        List<OssFileLists> lists = new ArrayList<>();
        for (S3Object f : allFileLists) {
            lists.add(new OssFileLists(f.key(),
                    bucket.getEndPoint() + "/" + f.key(),
                    f.size() / 1024,new Date(f.lastModified().getEpochSecond() * 1000)));
        }
        File tempFile = FileUtil.createTempFile(String.valueOf(System.currentTimeMillis()), ".xlsx", true);
        OssFileLists.createFile(lists,tempFile);
        task.setStatus("成功");
        task.setFilename(tempFile.getName());
        task.setFilePath(tempFile.getAbsolutePath());
        return task;
    }


    public static void uploadFile(Key key, Bucket bucket, String keyName, File file){

        // 创建 S3Client 对象
        S3Client s3 = S3Client.builder()
                .region(Region.of(bucket.getRegion()))
                .credentialsProvider(getBaseAuth(key))
                .build();

        // 读取文件创建requestBody
        RequestBody requestBody = RequestBody.fromFile(file);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket.getName())
                .key(keyName)
                .build();
        s3.putObject(request, requestBody);
    }


    public static URL createUrl(Key key, Bucket bucket, String keyName){
        try {
            S3Presigner presigner = S3Presigner.builder()
                    .credentialsProvider(getBaseAuth(key))
                    .region(Region.of(bucket.getRegion()))
                    .build();
            Duration expiration = Duration.ofMinutes(10);
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(expiration)
                    .getObjectRequest(builder -> builder
                            .bucket(bucket.getName())
                            .key(keyName))
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(presignRequest);
            URL url = presignedGetObjectRequest.url();
            presigner.close();
            return url;
        }catch (Exception e){
            return null;
        }
    }

}
