package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.Node;
import com.domain.Menu;
import com.mapper.MenuMapper;
import com.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【menu(菜单和权限规则表)】的数据库操作Service实现
* @createDate 2023-04-16 01:14:54
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{
    public List<Menu> menuList(){
        Node node = new Node();
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        return node.builTree(this.getBaseMapper().selectList(queryWrapper));
    }
}




