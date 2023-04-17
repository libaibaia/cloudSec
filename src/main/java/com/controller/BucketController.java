package com.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.domain.Bucket;
import com.domain.Key;
import com.service.BucketService;
import com.service.impl.KeyServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@SaCheckLogin
@RequestMapping("/api/bucket")
public class BucketController {

    @Resource
    private BucketService bucketService;
    @Resource
    private KeyServiceImpl keyService;

    @RequestMapping("/lists")
    public SaResult getCosList(@RequestParam(required = false) String quick_search){
        List<Bucket> list = new ArrayList<>();
        if (quick_search != null){
            QueryWrapper<Key> keyQueryWrapper = new QueryWrapper<>();
            Key one = keyService.getOne(keyQueryWrapper);
            if (one != null){
                QueryWrapper<Bucket> bucketQueryWrapper = new QueryWrapper<>();
                bucketQueryWrapper.eq("key_id",one.getId());
                list = bucketService.list(bucketQueryWrapper);
            }
        } else {
            list = bucketService.list();
        }
        return SaResult.ok().set("lists",list);
    }
}
