package com.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.aliyun.product.OSS;
import com.domain.Bucket;
import com.domain.Files;
import com.domain.Key;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.service.BucketService;
import com.service.impl.FilesServiceImpl;
import com.service.impl.KeyServiceImpl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@SaCheckLogin
@RequestMapping("/api/bucket")
public class BucketController {

    //用户id+时间戳
    private String filePath = "./%s/%s";
    @Resource
    private BucketService bucketService;
    @Resource
    private KeyServiceImpl keyService;
    @Resource
    private FilesServiceImpl filesService;

    @RequestMapping("/lists")
    public SaResult getBucketList(@RequestParam(required = false) String quick_search,
                                  @RequestParam(value = "page",defaultValue = "1",required = false) Integer page,
                                  @RequestParam(value = "limit",defaultValue = "10",required = false) Integer limit){
        List<Bucket> list = new ArrayList<>();
        Page<Bucket> objects = PageHelper.startPage(page, limit);
        if (quick_search != null){
            QueryWrapper<Key> keyQueryWrapper = new QueryWrapper<>();
            keyQueryWrapper.like("name",quick_search);
            Key one = keyService.getOne(keyQueryWrapper);
            if (one != null){
                QueryWrapper<Bucket> bucketQueryWrapper = new QueryWrapper<>();
                bucketQueryWrapper.eq("key_id",one.getId());
                list = bucketService.list(bucketQueryWrapper);
            }
        } else {
            list = bucketService.list();
        }
        return SaResult.ok().set("lists",list).set("total",objects.getTotal());
    }

    @RequestMapping("file/lists")
    public SaResult fileLists(@RequestParam("id") Integer id,String keyWord ){
        Bucket bucket = bucketService.getById(id);
        Key key = keyService.getById(bucket.getKeyId());
        return SaResult.ok().set("lists", com.common.Bucket.geFileLists(key,bucket,keyWord));
    }

    @RequestMapping("file/download")
    public SaResult downloadFile(@RequestBody Map<String,String> args){
        String id = args.get("bucketId");
        Bucket bucketId = bucketService.getById(id);
        Key key = keyService.getById(bucketId.getKeyId());
        String keyName = args.get("key");
        try {
            return SaResult.ok().set("url", com.common.Bucket.createFileUrl(key,bucketId,keyName,false).toString());
        } catch (MalformedURLException e) {
            return SaResult.error("创建下载链接失败");
        }
    }


    @RequestMapping("file/download/all")
    public SaResult downloadAllFile(@RequestBody Map<String,String> args){
        String id = args.get("bucketId");
        Bucket bucketId = bucketService.getById(id);
        Key key = keyService.getById(bucketId.getKeyId());
        bucketService.downloadAllFile(key,bucketId);
        return SaResult.ok("创建下载任务成功，查看文件列表下载");
    }
    @RequestMapping("file/upload")
    public SaResult uploadFile(@RequestParam("file") MultipartFile[] files,@RequestParam("id") Integer id){
        Map<String,String> args = new HashMap<>();
        Bucket bucket = bucketService.getById(id);
        Key key = keyService.getById(bucket.getKeyId());
        List<Files> filesList = null;
        try {
            filesList = com.common.Bucket.uploadFile(key, bucket, files, filePath);
            for (Files files1 : filesList) {
                filesService.save(files1);
            }
        } catch (IOException e) {
            return SaResult.ok("上传失败，原因：" + e.getMessage());
        }
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("file",args);
        return SaResult.ok("上传成功").setData(hashMap);
    }
}
