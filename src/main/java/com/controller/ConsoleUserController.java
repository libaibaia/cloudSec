package com.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.Type;
import com.common.aliyun.User;
import com.common.aws.Iam;
import com.common.tencent.user.UserPermissionList;
import com.domain.ConsoleUser;
import com.domain.DatabasesInstance;
import com.domain.Key;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.service.impl.ConsoleUserServiceImpl;
import com.service.impl.KeyServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@SaCheckLogin
@RequestMapping("/api/console")
public class ConsoleUserController {

    @Resource
    private KeyServiceImpl keyService;
    @Resource
    ConsoleUserServiceImpl consoleUserService;
    @RequestMapping("/binduser")
    public SaResult createConsoleUser(@RequestBody Map<String,String> args){
        Integer loginId = Integer.parseInt(StpUtil.getLoginId().toString()) ;
        Key id = keyService.getById(Integer.parseInt(args.get("id")));
        if (loginId.equals(id.getCreateById())){
            return consoleUserService.createUser(id);
        }
        return null;
    }

    @RequestMapping("/lists")
    public SaResult getConsoleUser(@RequestParam(required = false) String quick_search,@RequestParam(value = "page",defaultValue = "1",required = false)
    Integer page,@RequestParam(value = "limit",defaultValue = "10",required = false) Integer limit){
        List<ConsoleUser> consoleUsers = new ArrayList<>();
        Page<ConsoleUser> objects = PageHelper.startPage(page, limit);
        QueryWrapper<ConsoleUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("key_name",queryWrapper);
        if (quick_search != null){
            consoleUsers = consoleUserService.list(queryWrapper);
        } else {
            consoleUsers = consoleUserService.list();
        }
        return SaResult.ok().set("lists",consoleUsers).set("total",objects.getTotal());
    }

    @RequestMapping("/del")
    public SaResult del(@RequestParam(value = "ids",required = true) String id){
        ConsoleUser consoleUser = consoleUserService.getById(id);
        Key key = keyService.getById(consoleUser.getKeyId());
        String type = key.getType();
        switch (Type.valueOf(type))
        {
            case Tencent:
                UserPermissionList userPermissionList = new UserPermissionList(key);
                try {
                    userPermissionList.delUser(consoleUser.getUsername());
                    consoleUserService.removeById(consoleUser.getId());
                    return SaResult.ok();
                } catch (TencentCloudSDKException e) {
                    return SaResult.error(e.getMessage());
                }
            case AliYun:
                try {
                    User.deleteUser(key,consoleUser.getUsername());
                    consoleUserService.removeById(consoleUser.getId());
                    return SaResult.ok();
                } catch (Exception e) {
                    return SaResult.error(e.getMessage());
                }
            case AWS:
                try {
                    Iam.delUser(consoleUser.getUsername(),key);
                    consoleUserService.removeById(consoleUser.getId());
                    return SaResult.ok();
                } catch (Exception e) {
                    return SaResult.error(e.getMessage());
                }
            default:
                return SaResult.error("当前不支持");
        }
    }



}
