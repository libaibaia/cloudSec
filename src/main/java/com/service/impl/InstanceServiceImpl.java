package com.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.Type;
import com.common.aliyun.Base;
import com.common.aliyun.product.ECS;
import com.common.qiniu.base.BaseAuth;
import com.common.qiniu.qvm.Qvm;
import com.common.tencent.product.COS;
import com.common.tencent.product.cvm.CVM;
import com.domain.Instance;
import com.domain.Key;
import com.service.InstanceService;
import com.mapper.InstanceMapper;
import com.service.impl.aliyun.AliYunInstanceService;
import com.service.impl.qiniu.QiNiuService;
import com.service.impl.tencent.TencentInstanceService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.models.KeyPair;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    private QiNiuService qiNiuService;
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
            case QINiu:
                return qiNiuService.bindKeyPair(keyService.getById(keyId),keyName,byId);
            default:
                return SaResult.error("未知类型，绑定失败");
        }
    }
    public List<Instance> getInstanceList(List<Integer> akId){
        List<com.domain.Instance> list = new ArrayList<>();
        for (Integer integer : akId) {
            QueryWrapper<Instance> instanceQueryWrapper = new QueryWrapper<>();
            instanceQueryWrapper.eq("key_id",integer);
            List<com.domain.Instance> instances = this.baseMapper.selectList(instanceQueryWrapper);
            if (instances != null) list.addAll(instances);
        }
        return list;
    }

    public void restoreKey(Instance instance) throws Exception {
        Key byId = keyService.getById(instance.getKeyId());
        Type type = Type.valueOf(byId.getType());
        switch (type){
            case Tencent:
                CVM cvm = new CVM(byId);
                KeyPair keyPair = new KeyPair();
                keyPair.setKeyId(instance.getOriginalKeyPair());
                cvm.bindKeyPair(instance,keyPair);
                instance.setPrivateKey("");
                instance.setPublicKey("");
                break;
            case AliYun:
                ECS.bindKeyPair(Base.createClient(byId,
                                "ecs-cn-hangzhou.aliyuncs.com"),
                        instance.getRegion(),
                        instance.getOriginalKeyPair(),
                        instance.getInstanceId());
                instance.setPrivateKey("");
                instance.setPublicKey("");
                break;
            case QINiu:
                Qvm.bindKeyPair(BaseAuth.getAuth(byId.getSecretid(),
                                byId.getSecretkey()),
                        instance.getOriginalKeyPair(),
                        instance.getInstanceId(),
                        instance.getRegion());
                instance.setPrivateKey("");
                instance.setPublicKey("");
                this.updateById(instance);
                break;
        }
    }
}




