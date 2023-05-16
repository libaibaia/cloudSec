package com.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.domain.Files;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.service.impl.FilesServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Resource
    private FilesServiceImpl filesService;

    @RequestMapping("lists")
    public SaResult getFiles(@RequestParam(value = "page",defaultValue = "1",required = false) Integer page,
                             @RequestParam(value = "limit",defaultValue = "10",required = false) Integer limit){
        Page<Files> objects = PageHelper.startPage(page, limit);
        QueryWrapper<Files> filesQueryWrapper = new QueryWrapper<>();
        filesQueryWrapper.eq("user_id", StpUtil.getLoginId().toString());
        List<Files> list = filesService.list(filesQueryWrapper);
        return SaResult.ok().set("lists",list).set("total",objects.getTotal());
    }
}
