package com.common;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.PutObjectResult;
import com.common.aliyun.product.OSS;
import com.common.modle.File;
import com.common.qiniu.base.BaseAuth;
import com.common.tencent.product.COS;
import com.domain.Files;
import com.domain.Key;
import com.qcloud.cos.model.COSObjectSummary;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bucket {

    public static List<File> geFileLists(Key key, com.domain.Bucket bucket,String keyWord){
        Type type = Type.valueOf(key.getType());
        List<File> files = new ArrayList<>();
        switch (type){
            case Tencent:
                List<COSObjectSummary> fileLists = COS.getFileLists(key, bucket);
                for (COSObjectSummary fileList : fileLists) {
                    files.add(new File(fileList.getKey(), fileList.getOwner().toString(),
                            "https://"  + bucket.getName() + "." + bucket.getEndPoint()
                                    + "/" + fileList.getKey(), bucket.getName(),bucket.getId()));
                }
                break;
            case AliYun:
                List<OSSObjectSummary> lists = OSS.getFileLists(key, bucket);
                for (OSSObjectSummary list : lists) {
                    files.add(new File(list.getKey(), list.getOwner().toString(),
                            "https://" + bucket.getName() + "." + bucket.getEndPoint()
                                    + "/" + list.getKey(), bucket.getName(),bucket.getId()));
                }
                break;
            case QINiu:
                List<FileListing> fileLists1 = com.common.qiniu.Bucket.getFileLists(bucket, BaseAuth.getAuth(key), keyWord);
                for (FileListing fileListing : fileLists1) {
                    for (FileInfo item : fileListing.items) {
                        files.add(new File(item.key,"","",bucket.getName(),bucket.getId()));
                    }
                }
        }
        return files;
    }

    public static URL createFileUrl(Key key, com.domain.Bucket bucket,String keyName,boolean isHttps) throws MalformedURLException {
        Type type = Type.valueOf(key.getType());
        URL url = null;
        switch (type){
            case Tencent:
                url = COS.downloadOneFile(key, bucket, keyName);
                break;
            case AliYun:
                url = OSS.downloadOneFile(key, bucket, keyName);
                break;
            case QINiu:
                url = new URL(com.common.qiniu.Bucket.downLoadFile(BaseAuth.getAuth(key),bucket,keyName,isHttps));
        }
        return url;
    }

    public static List<Files> uploadFile(Key key, com.domain.Bucket bucket, MultipartFile[] files, String filePath) throws IOException {
        Type type = Type.valueOf(key.getType());
        String today = String.valueOf(DateUtil.current());
        Map<String,String> args = new HashMap<>();
        List<Files> list = new ArrayList<>();
        for (MultipartFile file : files) {
            //解析文件
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String localFilePath = String.format(filePath, StpUtil.getLoginId().toString(), today);
            java.io.File file1 = FileUtil.newFile(localFilePath + fileType);
            FileUtil.writeBytes(file.getBytes(),file1);
            Files f = new Files();
            f.setOriginalFileName(file.getOriginalFilename());
            f.setTpye(fileType);
            f.setUserId(Integer.valueOf(StpUtil.getLoginId().toString()));
            f.setFileSize(String.valueOf(FileUtil.size(file1)));
            args.put("url",f.getFilePath());
            args.put("name",f.getOriginalFileName());
            args.put("size",f.getFileSize());
            args.put("mimetype",file.getContentType());
            args.put("full_url",f.getFilePath());
            args.put("suffix",fileType);
            //上传到存储桶
            switch (type){
                case Tencent:
                    COS.uploadFile(key, bucket, file1, f.getOriginalFileName());
                    f.setFilePath("https://" + bucket.getName() + "." + bucket.getEndPoint() + "." + "/" + f.getOriginalFileName());
                    break;
                case AliYun:
                    PutObjectResult putObjectResult = OSS.uploadFile(key, bucket, f.getOriginalFileName(), file1);
                    String uri = putObjectResult.getResponse().getUri();
                    f.setFilePath(uri);
                    break;
                case QINiu:
                    com.common.qiniu.Bucket.uploadFile(BaseAuth.getAuth(key),bucket,file1.getName(),file1);
                    f.setFilePath("https://" + bucket.getName() + "." + bucket.getEndPoint() + "." + "/" + f.getOriginalFileName());
            }
            list.add(f);
        }
        return list;
    }


}
