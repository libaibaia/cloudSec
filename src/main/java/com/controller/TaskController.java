package com.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.domain.Task;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.service.TaskService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@SaCheckLogin
@RequestMapping("/api/task")
public class TaskController {

    @Resource
    private TaskService taskService;

    @RequestMapping("/lists")
    public SaResult taskLists(@RequestParam(value = "page",defaultValue = "1",required = false) Integer page,
                              @RequestParam(value = "limit",defaultValue = "10",required = false) Integer limit){
        Page<Task> objects = PageHelper.startPage(page, limit);
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("user_id", StpUtil.getLoginId().toString());
        List<Task> list = taskService.list(taskQueryWrapper);
        return SaResult.ok().set("lists",list).set("total",objects.getTotal());
    }
    @RequestMapping("/download")
    public void download(@RequestParam Integer id,HttpServletResponse response) throws IOException {
        Task byId = taskService.getById(id);
        File file = new File(byId.getFilePath());
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


    @DeleteMapping("/del")
    public SaResult delTask(@RequestParam("ids") String ids){
        String decode = null;
        try {
            decode = URLDecoder.decode(ids,"UTF-8");
            List<Integer> collect = Arrays.stream(decode.split(","))
                    .map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            for (Integer integer : collect) {
                FileUtil.del(taskService.getById(integer).getFilePath());
                taskService.removeById(integer);
            }
        } catch (UnsupportedEncodingException e) {
            return SaResult.error("删除失败");
        }
        return SaResult.ok("删除成功");
    }
}
