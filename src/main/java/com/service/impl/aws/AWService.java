package com.service.impl.aws;


import com.common.aws.EC2;
import com.common.aws.RDS;
import com.common.aws.S3;
import com.domain.Bucket;
import com.domain.DatabasesInstance;
import com.domain.Instance;
import com.domain.Key;
import com.service.BucketService;
import com.service.DatabasesInstanceService;
import com.service.InstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class AWService {

    @Autowired
    @Lazy
    private BucketService bucketService;
    @Autowired
    @Lazy
    private DatabasesInstanceService databasesInstanceService;
    @Autowired
    @Lazy
    private InstanceService instanceService;
    public void getBucketLists(Key key, AtomicInteger status){
        List<Bucket> bucketList = S3.getBucketList(key);
        log.info("添加资源数量" + bucketList.size());
        bucketService.saveBatch(bucketList);
        status.decrementAndGet();
    }

    public void getInstanceLists(Key key,AtomicInteger status){
        List<Instance> instanceLists = EC2.getInstanceLists(key);
        instanceService.saveBatch(instanceLists);
        log.info("添加资源数量" + instanceLists.size());
        status.decrementAndGet();
    }

    public void getRdsLists(Key key,AtomicInteger status){
        List<DatabasesInstance> rdsLists = RDS.getRdsLists(key);
        log.info(String.format("成功添加%s个资源", rdsLists.size()));
        databasesInstanceService.saveBatch(rdsLists);
        status.decrementAndGet();
    }
}
