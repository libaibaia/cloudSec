package com.service.impl.huawei;


import com.common.huawei.Ecs;
import com.common.huawei.OBS;
import com.domain.Bucket;
import com.domain.HuaweiObsRegion;
import com.domain.Instance;
import com.domain.Key;
import com.huaweicloud.sdk.ecs.v2.model.ServerAddress;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import com.service.HuaweiObsRegionService;
import com.service.InstanceService;
import com.service.impl.BucketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class HaWeiService {

    @Resource
    private HuaweiObsRegionService huaweiObsRegionService;
    @Resource
    private BucketServiceImpl bucketService;
    @Autowired
    @Lazy
    InstanceService instanceService;
    public void getBucketLists(Key key, AtomicInteger status){
        List<HuaweiObsRegion> list = huaweiObsRegionService.list();
        List<Bucket> bucketLists = OBS.getBucketLists(key, "obs.cn-north-4.myhuaweicloud.com", list.toArray(new HuaweiObsRegion[0]));
        bucketService.saveOrUpdateBatch(bucketLists);
        status.decrementAndGet();
    }
    public void getInstanceLists(Key key, AtomicInteger status){
        List<ServerDetail> ecsLists = Ecs.getEcsLists(key);
        for (ServerDetail ecsList : ecsLists) {
            Instance instance = new Instance();
            instance.setInstanceId(ecsList.getId());
            instance.setOsName(ecsList.getOsEXTSRVATTRInstanceName());
            instance.setRegion(ecsList.getOsEXTAZAvailabilityZone());
            instance.setOriginalKeyPair(ecsList.getKeyName());
            instance.setIp(getIp(ecsList));
            instance.setIsCommand("null");
            instance.setKeyId(key.getId());
            instance.setType(ecsList.getMetadata().get("os_type"));
            instanceService.save(instance);
        }
        status.decrementAndGet();
    }
    private String getIp(ServerDetail ecsList){
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : ecsList.getAddresses().keySet()) {
            List<ServerAddress> serverAddresses = ecsList.getAddresses().get(s);
            for (ServerAddress serverAddress : serverAddresses) {
                stringBuilder.append(serverAddress.getOsEXTIPSType());
                stringBuilder.append(":");
                stringBuilder.append(serverAddress.getAddr());
                stringBuilder.append("/");
            }
        }

        return stringBuilder.toString();
    }
}
