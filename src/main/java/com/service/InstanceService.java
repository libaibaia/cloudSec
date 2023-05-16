package com.service;

import com.domain.Instance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【instance】的数据库操作Service
* @createDate 2023-04-16 01:14:54
*/
public interface InstanceService extends IService<Instance> {
    List<Instance> getInstanceList(List<Integer> akId);
}
