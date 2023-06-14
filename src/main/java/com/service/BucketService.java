package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.Bucket;
import com.domain.Key;

/**
* @author Administrator
* @description 针对表【bucket】的数据库操作Service
* @createDate 2023-04-16 01:14:54
*/
public interface BucketService extends IService<Bucket> {
    void downloadAllFile(Key key, Bucket bucket);
}
