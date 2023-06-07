package com.common.huawei;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.common.Tools;
import com.common.modle.OssFileLists;
import com.domain.Bucket;
import com.domain.HuaweiObsRegion;
import com.domain.Key;
import com.domain.Task;
import com.obs.services.ObsClient;
import com.obs.services.model.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * 华为对象存储
 */
public class OBS {

    /**
     * 获取客户端凭证
     * @param key
     * @param endpoint
     * @return
     */
    private static ObsClient getObsClient(Key key,String endpoint){
        if (StrUtil.isBlank(key.getToken()))
            return new ObsClient(key.getSecretid(), key.getSecretkey(), endpoint);
        else
            return new ObsClient(key.getSecretid(), key.getSecretkey(),key.getToken(), endpoint);
    }

    /*
     * @param key
     * @param endpoint
     * @return
     */
    public static List<Bucket> getBucketLists(Key key, String endpoint, HuaweiObsRegion[] region){
        ObsClient obsClient = getObsClient(key, endpoint);
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        listBucketsRequest.setQueryLocation(true);
        List<ObsBucket> obsBuckets = new ArrayList<>();
        try {
            obsBuckets = obsClient.listBuckets(listBucketsRequest);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        List<ObsBucket> obsBucketList = new ArrayList<>();
        List<ObsBucket> res = new ArrayList<>();
        if (!StrUtil.isBlank(key.getBucketName())){
            String[] bucketName = Tools.getBucketName(key.getBucketName());
            for (String s : bucketName) {
                for (HuaweiObsRegion re : region) {
                    ObsClient obsClient1 = getObsClient(key, String.format("obs.%s.myhuaweicloud.com", re.getRegionId()));
                    try {
                        boolean b = obsClient1.headBucket(s);
                        if (b){
                            ObsBucket obsBucket = new ObsBucket();
                            obsBucket.setBucketName(s);
                            obsBucket.setLocation(re.getRegionId());
                            obsBucketList.add(obsBucket);
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

        res.addAll(obsBuckets);
        //去重
        for (ObsBucket element : obsBucketList) {
            boolean isExist = false;
            // 查找元素中是否有相同 ID 的元素
            for (ObsBucket mergedElement : res) {
                if (mergedElement.getBucketName().equals(element.getBucketName()) &&
                        mergedElement.getLocation().equals(element.getLocation())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                res.add(element);
            }
        }

        List<Bucket> buckets = new ArrayList<>();
        for (ObsBucket re : res) {
            ObsClient obsClient1 = getObsClient(key, "obs." + re.getLocation() + ".myhuaweicloud.com");
            BucketStorageInfo bucketStorageInfo = obsClient1.getBucketStorageInfo(re.getBucketName());
            Bucket bucket = new Bucket();
            bucket.setEndPoint("obs." + re.getLocation() + ".myhuaweicloud.com");
            bucket.setName(re.getBucketName());
            bucket.setOwner(bucket.getOwner());
            bucket.setRegion("");
            bucket.setKeyId(key.getId());
            bucket.setCreateById(key.getCreateById());
            buckets.add(bucket);
        }
        return buckets;
    }


    public static boolean uploadFile(Key key, File file, Bucket bucket){
        ObsClient obsClient = getObsClient(key, bucket.getEndPoint());
        PutObjectResult putObjectResult = null;
        try {
            putObjectResult = obsClient.putObject(bucket.getName(), file.getName(), file);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static String getFileUrl(Key key,Bucket bucket,String keyName){
        ObsClient obsClient = getObsClient(key, bucket.getEndPoint());
        long expireSeconds = 3600L;
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
        request.setBucketName(bucket.getName());
        request.setObjectKey(keyName);
        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        System.out.println(response.getSignedUrl());
        return response.getSignedUrl();
    }

    public static List<ObsObject> getFileLists(Key key, Bucket bucket, String keyWord){
        ObsClient obsClient = getObsClient(key, bucket.getEndPoint());
        ObjectListing result;
        ListObjectsRequest request = new ListObjectsRequest(bucket.getName());
        List<ObsObject> res = new ArrayList<>();
        request.setPrefix(keyWord);
        request.setMaxKeys(1000);
        try {
            do{
                result = obsClient.listObjects(request);
                res.addAll(result.getObjects());
                request.setMarker(result.getNextMarker());
            }while(result.isTruncated());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return res;
        }
        return res;
    }
    public static List<ObsObject> getAllFileLists(Key key, Bucket bucket){
        ObsClient obsClient = getObsClient(key, bucket.getEndPoint());
        ObjectListing result;
        ListObjectsRequest request = new ListObjectsRequest(bucket.getName());
        List<ObsObject> res = new ArrayList<>();
        try {
            do{
                result = obsClient.listObjects(request);
                res.addAll(result.getObjects());
                request.setMarker(result.getNextMarker());
            }while(result.isTruncated());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return res;
        }
        return res;
    }


    public static Task getALLFileByExcel(Key key, Bucket bucket, Task task) throws IOException {
        List<ObsObject> fileLists = getAllFileLists(key, bucket);
        List<OssFileLists> lists = new ArrayList<>();
        for (ObsObject fileList : fileLists) {
            lists.add(new OssFileLists(fileList.getObjectKey(),
                    bucket.getEndPoint() + "/" + fileList.getObjectKey(),
                    fileList.getMetadata().getContentLength() / 1024,fileList.getMetadata().getLastModified()));
        }
        File tempFile = FileUtil.createTempFile(String.valueOf(System.currentTimeMillis()), ".xlsx", true);
        OssFileLists.createFile(lists,tempFile);
        task.setStatus("成功");
        task.setFilename(tempFile.getName());
        task.setFilePath(tempFile.getAbsolutePath());
        return task;
    }

}
