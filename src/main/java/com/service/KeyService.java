package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.Key;

import java.io.File;

/**
* @author Administrator
* @description 针对表【key】的数据库操作Service
* @createDate 2023-05-14 13:14:02
*/
public interface KeyService extends IService<Key> {
    File ExportKeyExcel();
}
