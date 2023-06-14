package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.User;
import com.mapper.UserMapper;
import com.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-04-16 01:14:54
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    public User getUserByID(Integer id){
        return getById(id);
    }
    public User getUserByName(User user){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",user.getUsername());
        User one = getOne(userQueryWrapper);
        if (one != null && one.getPassword().equals(user.getPassword())){
            return one;
        }else {
            return null;
        }
    }

    public List<User> getAllUser(){
        return this.list();
    }
    public Boolean updateUser(User user){
        return this.updateById(user);
    }
    public Boolean addUser(User user){
        User userByName = this.getUserByName(user);
        if (userByName == null){
            this.baseMapper.insert(user);
            return true;
        }
        return false;
    }
    public void delUserById(Integer id){
        this.baseMapper.deleteById(id);
    }
}




