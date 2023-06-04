package com.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.PasswordGenerator;
import com.common.Tools;
import com.common.Type;
import com.common.aliyun.User;
import com.common.tencent.user.UserPermissionList;
import com.domain.ConsoleUser;
import com.domain.Key;
import com.service.ConsoleUserService;
import com.mapper.ConsoleUserMapper;
import com.service.impl.huawei.HuaWeiService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Administrator
 * @description 针对表【console_user】的数据库操作Service实现
 * @createDate 2023-04-16 01:14:54
 */
@Service
public class ConsoleUserServiceImpl extends ServiceImpl<ConsoleUserMapper, ConsoleUser>
        implements ConsoleUserService{

    @Autowired
    HuaWeiService huaWeiService;

    public void insertConsoleUser(ConsoleUser consoleUser){
        this.getBaseMapper().insert(consoleUser);
    }
    public List<ConsoleUser> getConsoleUser(Integer key_id){
        QueryWrapper<ConsoleUser> consoleUserQueryWrapper = new QueryWrapper<>();
        consoleUserQueryWrapper.eq("key_id",key_id);
        return list(consoleUserQueryWrapper);
    }
    public void removeByKeyId(Integer id){
        QueryWrapper<ConsoleUser> consoleUserQueryWrapper = new QueryWrapper<>();
        consoleUserQueryWrapper.eq("key_id",id);
        remove(consoleUserQueryWrapper);
    }
    public List<ConsoleUser> consoleUserListById(List<Key> keys){

        List<ConsoleUser> consoleUsers = new ArrayList<>();
        for (Key key : keys) {
            QueryWrapper<ConsoleUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("key_id", key.getId());
            System.out.println(queryWrapper.getCustomSqlSegment());
            List<ConsoleUser> list = this.getBaseMapper().selectList(queryWrapper);
            if (!list.isEmpty()) consoleUsers.addAll(list);
        }
        return consoleUsers;
    }
    public SaResult createUser(Key key){
        Type type = Type.valueOf(key.getType());
        Random r = new Random();
        int i = r.nextInt(100);
        switch (type){
            case HUAWEI:
                Map<String, String> huaWeiServiceConsoleUser = huaWeiService.createConsoleUser(key, "test" + i, PasswordGenerator.generatePassword());
                if (!huaWeiServiceConsoleUser.isEmpty()){
                    ConsoleUser huaweiConsoleUser = new ConsoleUser();
                    huaweiConsoleUser.setUsername(huaWeiServiceConsoleUser.get("name"));
                    huaweiConsoleUser.setLoginurl(huaWeiServiceConsoleUser.get("url"));
                    huaweiConsoleUser.setOwneruin(huaWeiServiceConsoleUser.get("ownerUin"));
                    huaweiConsoleUser.setPassword(huaWeiServiceConsoleUser.get("password"));
                    huaweiConsoleUser.setKeyId(key.getId());
                    huaweiConsoleUser.setUin("");
                    insertConsoleUser(huaweiConsoleUser);
                    return SaResult.ok("创建成功");
                }
                return SaResult.error("创建失败");
            case Tencent:
                UserPermissionList userPermissionList = new UserPermissionList(key);
                try {
                    HashMap<String, String> hashMap = userPermissionList.bindPer();
                    ConsoleUser tencentConsoleUser = new ConsoleUser();
                    tencentConsoleUser.setUsername(hashMap.get("userName"));
                    tencentConsoleUser.setLoginurl("https://cloud.tencent.com/login/subAccount");
                    tencentConsoleUser.setOwneruin(hashMap.get("OwnerUin"));
                    tencentConsoleUser.setPassword(hashMap.get("password"));
                    tencentConsoleUser.setKeyId(key.getId());
                    tencentConsoleUser.setUin(hashMap.get("uin"));
                    insertConsoleUser(tencentConsoleUser);
                    return SaResult.ok("创建成功").set("lists",hashMap);
                } catch (TencentCloudSDKException e) {
                    return SaResult.error("创建失败,原因：" + e.getMessage());
                }
            case AliYun:
                try {
                    Map<String,String> username = User.createConsoleUser(key, "test" + i);
                    ConsoleUser ailiYunConsoleUser = new ConsoleUser();
                    ailiYunConsoleUser.setUsername(username.get("name"));
                    ailiYunConsoleUser.setLoginurl(username.get("loginUrl"));
                    ailiYunConsoleUser.setPassword(username.get("password"));
                    ailiYunConsoleUser.setKeyId(key.getId());
                    insertConsoleUser(ailiYunConsoleUser);
                    return SaResult.ok("创建成功").set("lists",username);
                } catch (Exception e) {
                    return SaResult.error("创建失败,原因：" + e.getMessage());
                }

        }
        return null;
    }
}




