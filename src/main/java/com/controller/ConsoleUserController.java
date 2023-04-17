package com.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.Type;
import com.common.aliyun.User;
import com.common.tencent.user.UserPermissionList;
import com.domain.ConsoleUser;
import com.domain.DatabasesInstance;
import com.domain.Key;
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
            if (id.getType().equals(Type.Tencent.toString())){
                UserPermissionList userPermissionList = new UserPermissionList(id);
                try {
                    HashMap<String, String> hashMap = userPermissionList.bindPer();
                    ConsoleUser consoleUser = new ConsoleUser();
                    consoleUser.setUsername(hashMap.get("userName"));
                    consoleUser.setLoginurl("https://cloud.tencent.com/login/subAccount");
                    consoleUser.setOwneruin(hashMap.get("OwnerUin"));
                    consoleUser.setPassword(hashMap.get("password"));
                    consoleUser.setKeyId(id.getId());
                    consoleUser.setUin(hashMap.get("uin"));
                    consoleUserService.insertConsoleUser(consoleUser);
                    return SaResult.ok("创建成功").set("lists",hashMap);
                } catch (TencentCloudSDKException e) {
                    return SaResult.error("创建失败,原因：" + e.getMessage());
                }
            }
            if (id.getType().equals(Type.AliYun.toString())){
                try {
                    Random r = new Random();
                    int i = r.nextInt(100);
                    Map<String,String> username = User.createConsoleUser(id, "test" + i);
                    ConsoleUser consoleUser = new ConsoleUser();
                    consoleUser.setUsername(username.get("name"));
                    consoleUser.setLoginurl(username.get("loginUrl"));
                    consoleUser.setPassword(username.get("password"));
                    consoleUser.setKeyId(id.getId());
                    consoleUserService.insertConsoleUser(consoleUser);
                    return SaResult.ok("创建成功").set("lists",username);
                } catch (Exception e) {
                    return SaResult.error("创建失败,原因：" + e.getMessage());
                }
            }
        }
        return null;
    }

    @RequestMapping("/lists")
    public SaResult getConsoleUser(@RequestParam(required = false) String quick_search){
        List<ConsoleUser> consoleUsers = new ArrayList<>();
        if (quick_search != null){
            QueryWrapper<Key> keyQueryWrapper = new QueryWrapper<>();
            keyQueryWrapper.eq("secretId",quick_search);
            Key one = keyService.getOne(keyQueryWrapper);
            QueryWrapper<ConsoleUser> consoleUserQueryWrapper = new QueryWrapper<>();
            consoleUserQueryWrapper.eq("key_id",one.getId());
            consoleUsers = consoleUserService.list(consoleUserQueryWrapper);
        } else {
            QueryWrapper<Key> keyQueryWrapper = new QueryWrapper<>();
            keyQueryWrapper.eq("create_by_id",Integer.parseInt(StpUtil.getLoginId().toString()));
            List<Key> list = keyService.list(keyQueryWrapper);
            for (Key key : list) {
                consoleUsers.addAll(consoleUserService.getConsoleUser(key.getId()));
            }
        }
        return SaResult.ok().set("lists",consoleUsers);
    }


}
