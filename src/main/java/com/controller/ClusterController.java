package com.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.domain.Bucket;
import com.domain.Cluster;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;


@RestController
@SaCheckLogin
@RequestMapping("/api/cluster")
public class ClusterController {

    @Autowired
    private ClusterService clusterService;

    @RequestMapping("/lists")
    public SaResult getClusterLists(@RequestParam(required = false) String quick_search,
                                    @RequestParam(value = "page",defaultValue = "1",required = false) Integer page,
                                    @RequestParam(value = "limit",defaultValue = "10",required = false) Integer limit){
        Page<Bucket> objects = PageHelper.startPage(page, limit);
        List<Cluster> list;
        if (quick_search != null){
            QueryWrapper<Cluster> keyQueryWrapper = new QueryWrapper<>();
            keyQueryWrapper.like("key_name",quick_search);
            list = clusterService.list(keyQueryWrapper);
        } else {
            list = clusterService.list();
        }
        return SaResult.ok().set("lists",list).set("total",objects.getTotal());
    }

    @RequestMapping("/kubeConfig")
    public void getKubeConfig(@RequestParam Integer id, HttpServletResponse response) throws IOException {
        File file;
        if (FileUtil.exist("kubeconfig")){
            FileUtil.del("kubeconfig");
        }
        file = FileUtil.newFile("kubeconfig");
        String kubeConfig = clusterService.getKubeConfig(id);
        FileUtil.writeBytes(kubeConfig.getBytes(),file);
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

    @RequestMapping("/openEndpoint")
    public SaResult openEndpoint(@RequestBody Map<String,String> info){
        int clusterId = Integer.parseInt(info.get("clusterId"));
        String s = clusterService.openEndpoint(clusterId);
        return SaResult.ok().set("data",s);
    }
    @RequestMapping("/updateStatus")
    public SaResult updateClusterInfo(@RequestBody Map<String,String> info){
        int clusterId = Integer.parseInt(info.get("clusterId"));
        clusterService.updateStatus(clusterId);
        return SaResult.ok();
    }
    @RequestMapping("/createKubeController")
    public SaResult createCmd(@RequestParam("id") Integer id){
        String cmd = clusterService.createCmd(id);
        return SaResult.ok().set("data",cmd);
    }
}
