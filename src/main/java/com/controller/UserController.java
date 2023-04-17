package com.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.domain.User;
import com.service.impl.BucketServiceImpl;
import com.service.impl.DatabasesInstanceServiceImpl;
import com.service.impl.KeyServiceImpl;
import com.service.impl.UserServiceImpl;
import com.service.impl.tencent.TencentInstanceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@SaCheckLogin
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserServiceImpl userService;
    @Resource
    private KeyServiceImpl keyService;
    @Resource
    private BucketServiceImpl bucketService;
    @Resource
    private TencentInstanceService tencentInstanceService;
    @Resource
    private DatabasesInstanceServiceImpl databasesInstanceService;

    @RequestMapping("/")
    public SaResult test(){
        return SaResult.ok();
    }

    @PostMapping("/login")
    @SaIgnore
    public SaResult login(@RequestBody User user){
        User u = userService.getUserByName(user);
        if (u != null && user.getPassword().equals(u.getPassword())){
            StpUtil.login(u.getId());
            SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
            return SaResult.data(saTokenInfo).setCode(200).setMsg("登录成功").set("routePath","/admin");
        }else {
            return SaResult.error("登录失败");
        }
    }
    @PostMapping("/add")
    public SaResult addUser(@RequestBody User user){
        Boolean aBoolean = userService.addUser(user);
        if (aBoolean){
            return SaResult.ok().setMsg("添加成功");
        }
        return SaResult.error("添加失败").setCode(0);
    }

    @PostMapping("/update")
    public SaResult updateUser(@RequestBody User user){
        Boolean aBoolean = userService.updateUser(user);
        if (aBoolean){
            return SaResult.ok("修改成功");
        }
        return SaResult.error("修改失败");
    }
    @GetMapping("/update")
    public SaResult getUpdateUser(@RequestParam("id") Integer id){
        User userByID = userService.getUserByID(id);
        return SaResult.ok("获取成功").set("row",userByID);
    }
    @GetMapping("/lists")
    public SaResult getUser(){
        List<User> allUser = userService.getAllUser();
        return SaResult.ok().set("lists",allUser).set("total",allUser.size());
    }
    @RequestMapping(value = "/del",method = {RequestMethod.DELETE,RequestMethod.POST,RequestMethod.GET})
    public SaResult deleteUser(@RequestParam("ids") String ids) throws UnsupportedEncodingException {
        String decode = URLDecoder.decode(ids,"UTF-8");
        List<Integer> collect = Arrays.stream(decode.split(","))
                .map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        for (Integer integer : collect) {
            userService.delUserById(integer);
        }
        return SaResult.ok("删除成功");
    }

}
