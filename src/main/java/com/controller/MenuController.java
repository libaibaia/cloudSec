package com.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.util.SaResult;
import com.domain.Menu;
import com.service.impl.MenuServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RestController
@SaCheckLogin
public class MenuController {
    @Resource
    private MenuServiceImpl menuService;
    @GetMapping("/api")
    public SaResult getMenuList(){
        List<Menu> menus = menuService.menuList();
        HashMap<String, List<Menu>> hashMap = new HashMap<>();
        hashMap.put("menus",menus);
        return SaResult.code(200).setMsg("").setData(hashMap);
    }
    @GetMapping("/api/index/index")
    public SaResult getMenuList1(){
//        List<Menu> menus = menuService.menuList();
//        HashMap<String, List<Menu>> hashMap = new HashMap<>();
//        hashMap.put("menus",menus);
//        return SaResult.code(1).setMsg("").setData(hashMap);
        return SaResult.ok();
    }
}
