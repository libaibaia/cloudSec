package com.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.Type;
import com.domain.Instance;
import com.domain.Key;
import com.service.InstanceService;
import com.mapper.InstanceMapper;
import com.service.impl.aliyun.AliYunInstanceService;
import com.service.impl.tencent.TencentInstanceService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author Administrator
* @description 针对表【instance】的数据库操作Service实现
* @createDate 2023-04-16 01:14:54
*/
@Service
public class InstanceServiceImpl extends ServiceImpl<InstanceMapper, Instance>
    implements InstanceService{
    @Resource
    private TencentInstanceService tencentInstanceService;
    @Resource
    private AliYunInstanceService aliYunInstanceService;
    @Resource
    @Lazy
    private KeyServiceImpl keyService;

    public SaResult bindKeyPair(Integer id, String keyName, String key){
        Instance byId = getById(id);
        Integer keyId = byId.getKeyId();
        Type type = Type.valueOf(keyService.getById(keyId).getType());
        switch (type){
            case Tencent:
                return tencentInstanceService.bindKeyPair(id,keyName, Integer.parseInt(StpUtil.getLoginId().toString()));
            case AliYun:
                return aliYunInstanceService.bindKeyPair(id,keyName,key);
            default:
                return SaResult.error("未知类型，绑定失败");
        }

    }
}




