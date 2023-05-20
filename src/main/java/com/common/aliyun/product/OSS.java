package com.common.aliyun.product;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.ecs20140526.models.DescribeRegionsResponseBody;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.aliyun.sdk.service.oss20190517.AsyncClient;
import com.aliyun.sdk.service.oss20190517.models.DescribeRegionsRequest;
import com.aliyun.sdk.service.oss20190517.models.DescribeRegionsResponse;
import com.aliyun.sdk.service.oss20190517.models.RegionInfo;
import com.common.Tools;
import com.common.aliyun.Base;
import com.domain.Key;
import com.domain.Task;
import darabonba.core.client.ClientOverrideConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OSS {
    private static String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    private static Logger logger = LoggerFactory.getLogger(OSS.class);
    public static List<Bucket> getBucketLists(Key key){
        List<Bucket> buckets = getOssClient(key, null).listBuckets();
        List<Bucket> buckets1 = new ArrayList<>();
        ArrayList<Bucket> mergedList = new ArrayList<>();
        if (!StrUtil.isBlank(key.getBucketName())){
            String[] bucketName = Tools.getBucketName(key.getBucketName());
            try {
                for (String s : bucketName) {
                    DescribeRegionsResponse regionLists = getRegionLists(key);
                    for (RegionInfo regionInfo : regionLists.getBody().getRegionInfos()) {
                        com.aliyun.oss.OSS ossClient = getOssClient(key, "https://" + regionInfo.getInternetEndpoint());
                        boolean b = ossClient.doesBucketExist(s);
                        if (b){
                            Bucket bucket1 = new Bucket();
                            bucket1.setRegion(regionInfo.getRegion());
                            bucket1.setName(s);
                            bucket1.setExtranetEndpoint(regionInfo.getInternetEndpoint());
                            bucket1.setOwner(new Owner());
                            buckets1.add(bucket1);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        mergedList.addAll(buckets);
        //去重
        for (Bucket element : buckets1) {
            boolean isExist = false;
            // 查找元素中是否有相同 ID 的元素
            for (Bucket mergedElement : mergedList) {
                if (mergedElement.getName().equals(element.getName()) && mergedElement.getExtranetEndpoint().equals(element.getExtranetEndpoint())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                mergedList.add(element);
            }
        }

        return mergedList;
    }

    private static DescribeRegionsResponse getRegionLists(Key key) throws ExecutionException, InterruptedException {
        // Configure Credentials authentication information, including ak, secret, token
        StaticCredentialProvider provider;
        if (!StrUtil.isBlank(key.getToken())){
            provider = StaticCredentialProvider.create(Credential.builder()
                    .accessKeyId(key.getSecretid())
                    .accessKeySecret(key.getSecretkey())
                    .securityToken(key.getToken()) // use STS token
                    .build());
        }else {
            provider = StaticCredentialProvider.create(Credential.builder()
                    .accessKeyId(key.getSecretid())
                    .accessKeySecret(key.getSecretkey())
                    .build());
        }


        AsyncClient client = AsyncClient.builder()
                .region("cn-zhangjiakou")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("oss-cn-zhangjiakou.aliyuncs.com")
                )
                .build();

        DescribeRegionsRequest describeRegionsRequest = DescribeRegionsRequest.builder()
                .build();

        CompletableFuture<DescribeRegionsResponse> response = client.describeRegions(describeRegionsRequest);
        DescribeRegionsResponse resp = response.get();
        client.close();
        return resp;
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
