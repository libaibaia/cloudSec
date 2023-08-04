package com.controller;


import java.util.concurrent.TimeUnit;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.common.Tools;
import com.common.Type;
import com.domain.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.service.BucketService;
import com.service.ClusterService;
import com.service.InstanceService;
import com.service.impl.BucketServiceImpl;
import com.service.impl.ConsoleUserServiceImpl;
import com.service.impl.DatabasesInstanceServiceImpl;
import com.service.impl.KeyServiceImpl;
import com.service.impl.tencent.PermissionService;
import com.service.impl.tencent.TencentInstanceService;
import com.tencentcloudapi.cam.v20190116.models.AttachPolicyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static sun.font.CreatedFontTracker.MAX_FILE_SIZE;

@RestController
@SaCheckLogin
@RequestMapping("/api/ak")
public class KeyController {

    @Resource
    private KeyServiceImpl keyService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private InstanceService instanceService;
    @Autowired
    private ExecutorService executorService;

    @Resource
    private DatabasesInstanceServiceImpl databasesInstanceService;
    @Resource
    private BucketServiceImpl bucketService;
    @Resource
    private ConsoleUserServiceImpl consoleUserService;
    @Autowired
    @Lazy
    private ClusterService clusterService;

    @RequestMapping(value = "/lists",method = {RequestMethod.GET,RequestMethod.OPTIONS})
    public SaResult getSecretList(@RequestParam(value = "page",defaultValue = "1",required = false) Integer page,
                                  @RequestParam(value = "limit",defaultValue = "10",required = false) Integer limit,
                                  @RequestParam(value = "quick_search",required = false,defaultValue = "*") String searchKey){
        QueryWrapper<Key> queryWrapper = new QueryWrapper<>();
        if (!searchKey.equals("*")) queryWrapper.like("name",searchKey);
        queryWrapper.eq("create_by_id",StpUtil.getLoginId());
        Page<Object> objects = PageHelper.startPage(page, limit);
        List<Key> list = keyService.list(queryWrapper);
        return SaResult.ok("获取成功").set("lists",list).set("total",objects.getTotal());
    }

    //获取厂商类型
    @RequestMapping(value = "/secret/type",method = {RequestMethod.GET,RequestMethod.OPTIONS})
    public SaResult getSecretType(){
        Type[] values = Type.values();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("list",values);
        return SaResult.ok("获取成功").setData(hashMap);
    }
    @RequestMapping(value = "/add",method = {RequestMethod.POST})
    public SaResult addSecret(@RequestBody Key key){
        key.setCreateById(Integer.parseInt(StpUtil.getLoginId().toString()));
        if (keyService.saveKey(key))return SaResult.ok("添加完成");
        return SaResult.error("添加失败，名称重复");
    }

    @DeleteMapping("/del")
    public SaResult delSecret(@RequestParam("ids") String ids) throws UnsupportedEncodingException {
        String decode = URLDecoder.decode(ids,"UTF-8");
        List<Integer> collect = Arrays.stream(decode.split(","))
                .map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        for (Integer integer : collect) {
            keyService.removeById(integer);
            databasesInstanceService.delInstanceByKeyId(integer);
            bucketService.removeByKeyId(integer);
            consoleUserService.removeByKeyId(integer);
        }
        return SaResult.ok("删除成功");
    }
    @GetMapping("/update")
    public SaResult updateSecret(@RequestParam("id") Integer id){
        Key key = keyService.getById(id);
        return SaResult.ok().set("row",key);
    }
    @PostMapping("/update")
    public SaResult updateSecret(@RequestBody Key key){
        boolean aBoolean = keyService.updateById(key);
        if (aBoolean) return SaResult.ok("更新成功");
        return SaResult.error("更新失败");
    }

    /*
    获取权限列表
     */
    @RequestMapping("/perm")
    public SaResult getPermList(@RequestBody Map<String,String> id) throws Exception {
        List<Map> id1 = permissionService.getUserPermList(Integer.parseInt(id.get("id")), Integer.parseInt(StpUtil.getLoginId().toString()));
        return SaResult.ok().set("lists",id1);
    }
    @RequestMapping("/restart")
    public SaResult restartAkSk(@RequestBody Map<String,String> args){
        //插入之前删除之前存在的实例及cos信息
        String id = args.get("id");
        Key key = keyService.getById(Integer.parseInt(id));
        if (key.getCreateById().equals(Integer.parseInt(StpUtil.getLoginId().toString()))){
            cleanInstanceInfo(key);
            key.setCreateById(Integer.parseInt(StpUtil.getLoginId().toString()));
            executorService.submit(() -> keyService.execute(key));
            return SaResult.ok("更新成功，稍后刷新");
        }
        return SaResult.error("更新失败");
    }

    @GetMapping("/start")
    public SaResult startScan(){
        Object loginId = StpUtil.getLoginId();
        QueryWrapper<Key> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_by_id",loginId);
        List<Key> list = keyService.list(queryWrapper);
        for (Key key : list) {
            cleanInstanceInfo(key);
            key.setTaskStatus("等待检测");
            executorService.submit(() -> keyService.execute(key));
        }
        return SaResult.ok("启动成功");
    }

    @GetMapping("/stop")
    public SaResult stopTask(){
        new Thread(() -> {
            getNewThreadPool();
            List<Key> list = keyService.list();
            for (Key key : list) {
                key.setTaskStatus("停止检测");
                keyService.updateById(key);
                cleanInstanceInfo(key);
            }
        });
        return SaResult.ok("正在停止中");
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        File file = keyService.ExportKeyExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headers.getFirst(HttpHeaders.CONTENT_DISPOSITION));
        response.setHeader(HttpHeaders.CONTENT_TYPE, headers.getFirst(HttpHeaders.CONTENT_TYPE));
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        StreamingResponseBody responseBody = outputStream -> {
            try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        };
        responseBody.writeTo(response.getOutputStream());
        response.flushBuffer();
    }

    @PostMapping("/import")
    public SaResult importExcel(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            SaResult.error("文件不能为空");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            SaResult.error("文件大小不允许");
        }
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newFileName = UUID.randomUUID().toString() + "." + suffix;
        String filePath = "/tmp/" + newFileName;
        if (!"csv".equals(suffix) && !"xlsx".equals(suffix) && !"xls".equals(suffix)) {
            SaResult.error("文件类型不允许");
        }
        File newFile = new File(filePath);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            SaResult.error("上传失败");
        }

        try {
            File excel = FileUtil.writeFromStream(file.getInputStream(), newFile);
            List<Key> keys = Tools.readExcel(excel);
            keyService.saveBatch(keys);
        } catch (IOException e) {
            return SaResult.error("解析失败");
        }
        return SaResult.ok("上传成功");
    }

    private void cleanInstanceInfo(Key key) {
        QueryWrapper<Instance> instanceQueryWrapper = new QueryWrapper<>();
        instanceQueryWrapper.eq("key_id", key.getId());
        instanceService.remove(instanceQueryWrapper);
        QueryWrapper<Bucket> bucketQueryWrapper = new QueryWrapper<>();
        bucketQueryWrapper.eq("key_id", key.getId());
        bucketService.remove(bucketQueryWrapper);
        databasesInstanceService.delInstanceByKeyId(key.getId());
        consoleUserService.remove(new QueryWrapper<ConsoleUser>().eq("key_id", key.getId()));
        clusterService.remove(new QueryWrapper<Cluster>().eq("key_id", key.getId()));
    }

    private void getNewThreadPool(){
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("无法终止执行的任务");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        executorService = Executors.newFixedThreadPool(10);
    }

}
