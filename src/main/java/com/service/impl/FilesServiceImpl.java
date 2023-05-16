package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.Files;
import com.service.FilesService;
import com.mapper.FilesMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【files】的数据库操作Service实现
* @createDate 2023-04-26 00:24:04
*/
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files>
    implements FilesService{

}




