package com.service;

import com.domain.Instance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.Key;

import java.util.List;

/**
* @author Administrator
* @description 针对表【instance】的数据库操作Service
* @createDate 2023-05-17 23:14:12
*/
public interface InstanceService extends IService<Instance> {
    List<Instance> getInstanceList(List<Key> akId);
}
