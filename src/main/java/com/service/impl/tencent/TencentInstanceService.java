package com.service.impl.tencent;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.Type;
import com.common.tencent.product.cvm.CVM;
import com.domain.Key;
import com.mapper.InstanceMapper;
import com.service.impl.KeyServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.models.CreateKeyPairResponse;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import com.tencentcloudapi.tat.v20201028.models.InvocationTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;


@Service
public class TencentInstanceService {
    private static Logger logger = LoggerFactory.getLogger(TencentInstanceService.class);
    @Resource
    private InstanceMapper instanceMapper;
    @Resource
    private KeyServiceImpl keyService;

    public void getInstanceList(Key key){
        CVM getBasePermissionList = new CVM(key);
        Map<Instance, List<InvocationTask>> result = null;
        String message = null;
        try {
            result = getBasePermissionList.getResult();
            for (Map.Entry<Instance, List<InvocationTask>> instanceListEntry : result.entrySet()) {
                List<InvocationTask> invocationTasks = result.get(instanceListEntry.getKey());
                for (InvocationTask invocationTask : invocationTasks) {
                    Instance instance1 = instanceListEntry.getKey();
                    com.domain.Instance instance = new com.domain.Instance();
                    instance.setInstanceId(instance1.getInstanceId());
                    instance.setIp(Arrays.toString(instance1.getPublicIpAddresses()));
                    instance.setType(instance1.getOsName());
                    instance.setKeyId(key.getId());
                    instance.setRegion(instance1.any().get("region").toString());
                    instance.setIsCommand((invocationTask.getCommandId().equals("null"))?"null":"true");
                    instanceMapper.insert(instance);
                    logger.info("成功添加一个资源，id:" + invocationTask.getInstanceId());
                }
            }
        } catch (TencentCloudSDKException e) {
            logger.error(e.getMessage());
        }
    }

    public SaResult bindKeyPair(Integer id, String keyName, Integer currentLoginID){
        com.domain.Instance currentInstance = instanceMapper.selectById(id);
        Integer keyId = currentInstance.getKeyId();
        Key key1 = keyService.getById(keyId);
        if (key1.getType().equals(Type.Tencent.toString())){
            CVM cvm = new CVM(key1);
            if (key1.getCreateById().equals(currentLoginID)){
                try {
                    CreateKeyPairResponse keyPair = cvm.createKeyPair(keyName);
                    cvm.bindKeyPair(currentInstance,keyPair.getKeyPair());
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("keyName",keyName);
                    hashMap.put("publicKey",keyPair.getKeyPair().getPublicKey());
                    hashMap.put("privateKey",keyPair.getKeyPair().getPrivateKey());
                    currentInstance.setPrivateKey(keyPair.getKeyPair().getPrivateKey());
                    currentInstance.setPublicKey(keyPair.getKeyPair().getPublicKey());
                    instanceMapper.updateById(currentInstance);
                    return SaResult.ok("添加成功，请刷新页面");
                } catch (TencentCloudSDKException e) {
                    return SaResult.error("添加失败，原因：" + e.getMessage());
                }
            }
        }
        return SaResult.error("非当前用户创建");
    }
    public List<com.domain.Instance> getInstanceList(List<Integer> akId){
        List<com.domain.Instance> list = new ArrayList<>();
        for (Integer integer : akId) {
            QueryWrapper<com.domain.Instance> instanceQueryWrapper = new QueryWrapper<>();
            instanceQueryWrapper.eq("key_id",integer);
            List<com.domain.Instance> instances = instanceMapper.selectList(instanceQueryWrapper);
            if (instances != null) list.addAll(instances);
        }
        return list;
    }
    public void delInstanceByKeyId(Integer key_id){
        QueryWrapper<com.domain.Instance> instanceQueryWrapper = new QueryWrapper<>();
        instanceQueryWrapper.eq("key_id",key_id);
        instanceMapper.delete(instanceQueryWrapper);
    }
    public com.domain.Instance getInstanceByID(Integer id){
        return instanceMapper.selectById(id);
    }

}
