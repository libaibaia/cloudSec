package com.common.aliyun.product;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.ZoneUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.ZipUtil;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.common.Tools;
import com.domain.Key;
import com.domain.Task;
import com.service.TaskService;
import com.service.impl.tencent.TencentInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipFile;

public class OSS {
    private static String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    private static Logger logger = LoggerFactory.getLogger(OSS.class);
    public static List<Bucket> getBucketLists(Key key){
        return getOssClient(key,null).listBuckets();
    }
    public static List<OSSObjectSummary> getFileLists(Key key, com.domain.Bucket bucket){
        com.aliyun.oss.OSS ossClient = getOssClient(key,bucket.getEndPoint());
        ObjectListing objectListing = ossClient.listObjects(bucket.getName());
        return objectListing.getObjectSummaries();
    }
    private static com.aliyun.oss.OSS getOssClient(Key key,String endpoint){
        if (endpoint != null) OSS.endpoint = endpoint;
        com.aliyun.oss.OSS ossClient;
        if (key.getToken() != null && !key.getToken().equals("")){
            ossClient = new OSSClientBuilder().build(OSS.endpoint, key.getSecretid(), key.getSecretkey(),key.getToken());
        }else {
            ossClient = new OSSClientBuilder().build(OSS.endpoint, key.getSecretid(), key.getSecretkey());
        }
         return ossClient;
    }
    public static PutObjectResult uploadFile(Key key, com.domain.Bucket bucket, String objectName, File file){
        com.aliyun.oss.OSS ossClient = getOssClient(key, bucket.getEndPoint());
        FileReader fileReader = new FileReader(file);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket.getName(), objectName, new ByteArrayInputStream(fileReader.readBytes()));
        putObjectRequest.setProcess("true");
        return ossClient.putObject(putObjectRequest);
    }

    public static URL downloadOneFile(Key key, com.domain.Bucket bucket,String objectName){
        // 指定生成的签名URL过期时间，单位为毫秒。
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        com.aliyun.oss.OSS ossClient = getOssClient(key, bucket.getEndPoint());
        URL signedUrl = null;
        // 生成签名URL。
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket.getName(), objectName, HttpMethod.GET);
        Map<String, String> headers = new HashMap<String, String>();
        request.setExpiration(expiration);
        // 将请求头加入到request中。
        request.setHeaders(headers);
        // 通过HTTP GET请求生成签名URL。
        signedUrl = ossClient.generatePresignedUrl(request);
        // 打印签名URL。
        return signedUrl;
    }

    public static Task downloadALLFile(Key key, com.domain.Bucket bucket, Task task){
        com.aliyun.oss.OSS ossClient = getOssClient(key, bucket.getEndPoint());
        List<OSSObjectSummary> lists = getFileLists(key, bucket);
        List<File> files = new ArrayList<>();
        long current = DateUtil.current();
        String path = "../../" + current;
        File dir = FileUtil.mkdir(path);
        for (OSSObjectSummary list : lists) {
            //取消key中的/转换为.，防止路径文件无法存储文件
            File file = FileUtil.newFile(dir + "\\" + list.getKey().replace("/","."));
            ossClient.getObject(new GetObjectRequest(bucket.getName(), list.getKey()), file);
            files.add(file);
        }
        File zipFile = Tools.createZipFile(files, bucket.getName());
        task.setStatus("成功");
        task.setFilename(zipFile.getName());
        task.setFilePath(zipFile.getAbsolutePath());
        FileUtil.del(dir);
        return task;

    }

}
