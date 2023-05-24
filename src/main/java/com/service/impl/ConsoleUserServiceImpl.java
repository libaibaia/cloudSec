package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.ConsoleUser;
import com.domain.Key;
import com.service.ConsoleUserService;
import com.mapper.ConsoleUserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【console_user】的数据库操作Service实现
 * @createDate 2023-04-16 01:14:54
 */
@Service
public class ConsoleUserServiceImpl extends ServiceImpl<ConsoleUserMapper, ConsoleUser>
        implements ConsoleUserService{


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
}




