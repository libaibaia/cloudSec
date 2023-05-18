package com.common.qiniu;

import cn.hutool.core.util.StrUtil;
import com.common.qiniu.base.BaseAuth;
import com.common.qiniu.base.model.BucketModel;
import com.domain.Key;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.*;
import com.qiniu.storage.model.BucketInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;

import java.io.File;
import java.util.*;

public class Bucket {
    private static final Configuration configuration = new Configuration();

    public static String downLoadFile(Auth auth, com.domain.Bucket bucket, String keyName, boolean useHttps) {
        String endPoint = bucket.getEndPoint();
        String[] domainLists = endPoint.substring(1, endPoint.length() - 1).split(",");
        Random random = new Random();
        String randomElement = domainLists[random.nextInt(domainLists.length)];
        DownloadUrl url = new DownloadUrl(randomElement, useHttps, keyName);
        try {
            String s = url.buildURL(auth, System.currentTimeMillis() / 1000 + 3600);
            return s;
        } catch (QiniuException e) {
            return "创建下载链接失败";
        }
    }

    public static void downAllFile(){

    }

    public static List<BucketModel>  getBucketLists(Auth auth){
        List<BucketModel> bucketModels = new ArrayList<>();
        BucketManager bucketManager = new BucketManager(auth,configuration);
        try {
            String[] buckets = bucketManager.buckets();
            for (String bucket : buckets) {
                BucketInfo bucketInfo = bucketManager.getBucketInfo(bucket);
                String[] domainList = bucketManager.domainList(bucket);
                BucketModel bucketModel = new BucketModel(bucket, bucketInfo.getRegion(), domainList);
                bucketModels.add(bucketModel);
            }
        } catch (QiniuException e) {
            System.out.println(e.getMessage());
        }
        return bucketModels;
    }
    public static boolean uploadFile(Auth auth, com.domain.Bucket bucket, String fileName, File file) throws QiniuException {
        String token = auth.uploadToken(bucket.getName());
        UploadManager uploadManager = new UploadManager(configuration);
        Response put = uploadManager.put(file, StrUtil.isBlankIfStr(fileName) ? "" : file.getName(), token);
        if (put.statusCode == 200) {
            System.out.println(put.bodyString());
            return true;
        }
        return false;
    }

    public static List<FileListing> getFileLists(com.domain.Bucket bucket,Auth auth,String prefix){
        BucketManager bucketManager = new BucketManager(auth,configuration);
        List<FileListing> listings = new ArrayList<>();
        while (true){
            try {
                FileListing fileListing = bucketManager.listFiles(bucket.getName(), StrUtil.isBlankIfStr(prefix) ? "" : prefix, "", 1000, "");
                listings.add(fileListing);
                if (fileListing.isEOF()){
                    return listings;
                }
            } catch (QiniuException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
