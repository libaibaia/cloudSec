package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.Task;
import com.mapper.TaskMapper;
import com.service.TaskService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【task】的数据库操作Service实现
* @createDate 2023-04-29 22:53:00
*/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
    implements TaskService{

}




