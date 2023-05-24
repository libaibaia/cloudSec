package com.service.impl.huawei;


import com.common.huawei.OBS;
import com.domain.Bucket;
import com.domain.HuaweiObsRegion;
import com.domain.Key;
import com.obs.services.model.ObsBucket;
import com.service.HuaweiObsRegionService;
import com.service.impl.BucketServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class HaWeiService {

    @Resource
    private HuaweiObsRegionService huaweiObsRegionService;
    @Resource
    private BucketServiceImpl bucketService;
    public void getBucketLists(Key key, AtomicInteger status){
        List<HuaweiObsRegion> list = huaweiObsRegionService.list();
        List<ObsBucket> bucketLists = OBS.getBucketLists(key, "obs.cn-north-4.myhuaweicloud.com", list.toArray(new HuaweiObsRegion[0]));
        for (ObsBucket bucketList : bucketLists) {
            Bucket bucket = new Bucket();
            bucket.setEndPoint("obs." + bucketList.getLocation() + ".myhuaweicloud.com");
            bucket.setName(bucketList.getBucketName());
            bucket.setOwner(bucket.getOwner());
            bucket.setRegion("");
            bucket.setKeyId(key.getId());
            bucket.setCreateById(key.getCreateById());
            bucketService.save(bucket);
        }
        status.decrementAndGet();
    }
}
