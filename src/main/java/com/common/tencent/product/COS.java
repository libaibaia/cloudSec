package com.common.tencent.product;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.common.Tools;
import com.domain.Key;
import com.domain.Task;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.IOUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.region.v20220627.models.RegionInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;


@Slf4j
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
        ClientConfig clientConfig = new ClientConfig();
        COSClient cosClient = new COSClient(getCred(key), clientConfig);
        List<Bucket> buckets = new ArrayList<>();
        List<Bucket> buckets1 = new ArrayList<>();
        List<Bucket> res = new ArrayList<>();

        try {
            buckets = cosClient.listBuckets();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        //不具有列出存储桶权限时使用存储桶名称查看是否具有操作权限
        if (!StrUtil.isBlank(key.getBucketName())) {
            RegionInfo[] region = new RegionInfo[0];
            try {
                region = Base.getRegionList(Base.createCredential(key), "COS");
            } catch (TencentCloudSDKException e) {
                log.info(e.getMessage());
            }
            String[] bucketName = Tools.getBucketName(key.getBucketName());
            //遍历对应区域及名称获取存储桶
            for (String s : bucketName) {
                for (RegionInfo regionInfo : region) {
                    clientConfig.setRegion(new Region(regionInfo.getRegion()));
                    try {
                        boolean b = cosClient.doesBucketExist(s);
                        if(b) {
                            Bucket bucket1 = new Bucket();
                            bucket1.setLocation(String.format(cosEndPoint, s,regionInfo.getRegionName()));
                            bucket1.setName(s);
                            buckets.add(bucket1);
                            buckets1.add(bucket1);
                            for (Bucket bucket : buckets) {
                                if (bucket.getName().equals(s) && bucket.getLocation().equals(regionInfo.getRegion())){
                                    break;
                                }else {

                                }
                            }
                        }
                    }catch (CosServiceException exception){
                        log.info("检测存储桶" + s + "出现错误当前检测区域" + regionInfo.getRegion());
                        log.info("异常原因" + exception.getErrorMessage() + "错误码" + exception.getErrorCode());
                    }catch (CosClientException ignored){
                        log.info("检测存储桶" + s + "出现错误当前检测区域" + regionInfo.getRegion());
                        log.info("异常原因" + ignored.getMessage() + "错误码" + ignored.getErrorCode() + ignored.getLocalizedMessage());
                    }
                }
            }
        }

        //去重
        res.addAll(buckets);
        for (Bucket element : buckets1) {
            boolean isExist = false;
            // 查找元素中是否有相同 ID 的元素
            for (Bucket mergedElement : res) {
                if (mergedElement.getName().equals(element.getName()) && mergedElement.getLocation().equals(element.getLocation())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                res.add(element);
            }
        }


        return res;
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
        List<COSObjectSummary> fileLists = getFileLists(key, bucket,null);
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
        FileUtil.del(dir);
        return task;
    }


    public static List<COSObjectSummary> getFileLists(Key key, com.domain.Bucket bucket,String keyWord){
        List<COSObjectSummary> list = new ArrayList<>();
        ClientConfig clientConfig = new ClientConfig(new Region(bucket.getRegion()));
        COSClient cosclient = new COSClient(getCred(key), clientConfig);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        if (!StrUtil.isBlank(keyWord)) listObjectsRequest.setPrefix(keyWord);
        listObjectsRequest.setBucketName(bucket.getName());
        listObjectsRequest.setMaxKeys(Tools.maxBucketNum);
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
