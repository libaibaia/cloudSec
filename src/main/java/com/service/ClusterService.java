package com.service;

import com.domain.Cluster;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【cluster】的数据库操作Service
* @createDate 2023-07-29 20:26:23
*/
public interface ClusterService extends IService<Cluster> {
    String getKubeConfig(Integer clusterId);
    String openEndpoint(Integer clusterId);
    void updateStatus(Integer clusterId);
    String createCmd(Integer id);
    void removeByKeyID(Integer id);
}
