package com.service.impl.aliyun;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.aliyun.ecs20140526.models.DescribeInstancesResponseBody;
import com.aliyun.ecs20140526.models.DescribeInvocationResultsResponse;
import com.aliyun.ecs20140526.models.DescribeRegionsResponseBody;
import com.aliyun.oss.model.Bucket;
import com.common.aliyun.Base;
import com.common.aliyun.product.ECS;
import com.common.aliyun.product.OSS;
import com.domain.Instance;
import com.domain.Key;
import com.mapper.InstanceMapper;
import com.service.KeyService;
import com.service.impl.BucketServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AliYunInstanceService {
    @Resource
    private InstanceMapper instanceMapper;
    @Resource
    private KeyService keyService;
    @Resource
    private BucketServiceImpl bucketService;

    public void getInstanceList(Key key){
        Map<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion, List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance>> ecsLists = ECS.getECSLists(key);
        for (Map.Entry<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion,
                List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance>>
                res : ecsLists.entrySet()) {
            for (DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance instance : res.getValue()) {
                DescribeInvocationResultsResponse command = null;
                try {
                    command = ECS.createCommand(Base.createClient(key, res.getKey().regionEndpoint),
                            res.getKey().regionId,"test",ECS.getType(instance.OSType),"whoami",instance.instanceId);
                    Instance instance1 = new Instance();
                    instance1.setInstanceId(instance.instanceId);
                    instance1.setRegion(instance.getRegionId());
                    instance1.setType(instance.getOSType());
                    instance1.setIp(instance.getPublicIpAddress().ipAddress.toString());
                    instance1.setKeyId(key.getId());
                    if (command.getBody().invocation.invocationResults.invocationResult.size() >= 1){
                        instance1.setIsCommand("true");
                        instanceMapper.insert(instance1);
                    }
                    else {
                        instance1.setIsCommand("null");
                        instanceMapper.insert(instance1);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void getBucketLists(Key key){
        List<Bucket> bucketLists = OSS.getBucketLists(key.getSecretid(), key.getSecretkey());
        com.domain.Bucket bucket = new com.domain.Bucket();
        for (Bucket bucketList : bucketLists) {
            bucket.setRegion(bucketList.getRegion());
            bucket.setName(bucket.getName());
            bucket.setCreateById(Integer.parseInt(StpUtil.getLoginId().toString()));
            bucket.setEndPoint(bucket.getName() + "." + bucketList.getExtranetEndpoint());
            bucket.setOwner(bucketList.getOwner().toString());
            bucketService.save(bucket);
        }
    }

    public SaResult bindKeyPair(Integer id, String keyName,String key){
        Instance currentInstance = instanceMapper.selectById(id);
        Integer keyId = currentInstance.getKeyId();
        Key byId = keyService.getById(keyId);
        try {
            ECS.importKey(keyName,key,Base.createClient(byId,"ecs-cn-hangzhou.aliyuncs.com"),
                    currentInstance.getRegion(),currentInstance.getInstanceId());
            currentInstance.setPrivateKey("该资源未存放私钥，请使用生成公钥的服务器登录");
            instanceMapper.updateById(currentInstance);
            return SaResult.ok("创建成功,请刷新");
        } catch (Exception e) {
            return SaResult.error("创建失败，原因：" + e.getMessage());
        }
    }

}
