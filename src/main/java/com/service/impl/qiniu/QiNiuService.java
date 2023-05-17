package com.service.impl.qiniu;

import cn.dev33.satoken.util.SaResult;
import com.common.LogAnnotation;
import com.common.qiniu.base.BaseAuth;
import com.common.qiniu.base.model.qvm.InstanceInfo;
import com.common.qiniu.base.model.qvm.InstanceInfoResponse;
import com.common.qiniu.base.model.qvm.KeyResponse;
import com.common.qiniu.qvm.Qvm;
import com.domain.Instance;
import com.domain.Key;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.qiniu.util.Auth;
import com.service.impl.InstanceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Service
public class QiNiuService {


    @Resource
    @Lazy
    private InstanceServiceImpl instanceService;

    @LogAnnotation(title = "检测七牛云服务器")
    public void getInstanceList(Key key, AtomicInteger status){
        Auth auth = BaseAuth.getAuth(key.getSecretid(), key.getSecretkey());
        try {
            InstanceInfoResponse instanceLists = Qvm.getInstanceLists(auth);
            for (InstanceInfo datum : instanceLists.getData()) {
                Instance instance = new Instance();
                instance.setInstanceId(datum.getInstance_id());
                instance.setType(datum.getOs_type());
                instance.setIp(datum.getEip_address().getIp_address());
                instance.setRegion(datum.getRegion_id());
                instance.setOriginalKeyPair(datum.getKey_pair_name());
                instance.setIsCommand("七牛暂不支持执行命令");
                instance.setKeyId(key.getId());
                instance.setOsName(datum.getOs_name());
                instanceService.getBaseMapper().insert(instance);
            }
        } catch (Exception e) {
            log.info("获取七牛云服务器出错原因：" + e.getMessage());
        }
        status.decrementAndGet();
    }

    public SaResult bindKeyPair(Key key, String keyName, Instance instance){
        try {
            KeyResponse keyResponse = Qvm.createKeyPair(BaseAuth.getAuth(key.getSecretid(), key.getSecretkey()), keyName, instance.getInstanceId(), instance.getRegion());
            instance.setPrivateKey(keyResponse.getData().getPrivate_key_body());
            instance.setPublicKey(keyResponse.getData().getPublic_key_body());
            instanceService.updateById(instance);
        } catch (Exception e) {
            return SaResult.error(e.getMessage());
        }
        return SaResult.ok();

    }
}
