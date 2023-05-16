package com.common.tencent.product;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.domain.Key;
import com.domain.Task;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class COS {

    public static String cosEndPoint = "%s.cos.%s.myqcloud.com";

    /*
     * 遍历存储桶
     * @param secretId
     * @param secretKey
     * @param token
     * @return 桶列表
     */
    public List<Bucket> getCosList(Key key){
        Map<String,String> result = new HashMap<>();
        ClientConfig clientConfig = new ClientConfig();
        COSClient cosClient = new COSClient(getCred(key), clientConfig);
        return cosClient.listBuckets();
    }
    private static COSCredentials getCred(Key key){
        COSCredentials cred = null;
        if (key.getToken() != null && !key.getToken().equals("")){
            cred = new BasicSessionCredentials(key.getSecretid(), key.getSecretkey(), key.getToken());
        }
        else {
            cred = new BasicCOSCredentials(key.getSecretid(), key.getSecretkey());
        }
        return cred;
    }
    public static PutObjectResult uploadFile(Key key, com.domain.Bucket bucket, File file, String keyName){
        ClientConfig clientConfig = new ClientConfig(new Region(bucket.getRegion()));
        COSClient cosclient = new COSClient(getCred(key), clientConfig);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket.getName(), keyName, file);
        PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
        return putObjectResult;
    }

    public static URL downloadOneFile(Key key, com.domain.Bucket bucket, String keyName){
        ClientConfig clientConfig = new ClientConfig(new Region(bucket.getRegion()));
        COSClient cosclient = new COSClient(getCred(key), clientConfig);
        Date expirationDate = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
        HttpMethodName method = HttpMethodName.GET;
        URL url = cosclient.generatePresignedUrl(bucket.getName(), keyName, expirationDate, method);
        cosclient.shutdown();
        System.out.println(url);
        return url;
    }

    public static Task downloadAllFile(Key key, com.domain.Bucket bucket, Task task){
        ClientConfig clientConfig = new ClientConfig(new Region(bucket.getRegion()));
        COSClient cosclient = new COSClient(getCred(key), clientConfig);
        List<COSObjectSummary> fileLists = getFileLists(key, bucket);
        long current = DateUtil.current();
        String path = "../../" + current;
        File dir = FileUtil.mkdir(path);
        for (COSObjectSummary fileList : fileLists) {
            //取消key中的/转换为.，防止路径文件无法存储文件

            COSObjectInputStream cosObjectInput = null;
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucket.getName(), fileList.getKey());
            try {
                COSObject cosObject = cosclient.getObject(getObjectRequest);
                cosObjectInput = cosObject.getObjectContent();
                File file = FileUtil.newFile(dir + "\\" + fileList.getKey().replace("/","."));
                byte[] bytes = null;
                try {
                    bytes = IOUtils.toByteArray(cosObjectInput);
                    FileUtil.writeBytes(bytes,file);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 用完流之后一定要调用 close()
                    cosObjectInput.close();
                }
            }catch (CosClientException | IOException e){
                System.out.println(e.getMessage());
                break;
            }
        }
        File zip = ZipUtil.zip(dir.getPath(), FileUtil.createTempFile(bucket.getName(),".zip", true).getPath());
        task.setStatus("成功");
        task.setFilename(zip.getName());
        task.setFilePath(zip.getAbsolutePath());
        return task;
    }


    public static List<COSObjectSummary> getFileLists(Key key, com.domain.Bucket bucket){
        List<COSObjectSummary> list = new ArrayList<>();
        ClientConfig clientConfig = new ClientConfig(new Region(bucket.getRegion()));
        COSClient cosclient = new COSClient(getCred(key), clientConfig);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucket.getName());
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        do {
            try {
                objectListing = cosclient.listObjects(listObjectsRequest);
            } catch (CosClientException e) {
                e.printStackTrace();
                break;
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            list.addAll(objectListing.getObjectSummaries());
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
        return list;
    }

}
