package com.service.impl.aliyun;


import cn.dev33.satoken.util.SaResult;
import com.aliyun.ecs20140526.models.DescribeInstancesResponseBody;
import com.aliyun.ecs20140526.models.DescribeInvocationResultsResponse;
import com.aliyun.ecs20140526.models.DescribeRegionsResponseBody;
import com.aliyun.oss.model.Bucket;
import com.aliyun.rds20140815.models.DescribeDBInstanceIPArrayListResponseBody;
import com.aliyun.rds20140815.models.DescribeDBInstanceNetInfoResponse;
import com.aliyun.rds20140815.models.DescribeDBInstanceNetInfoResponseBody;
import com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody;
import com.common.LogAnnotation;
import com.common.aliyun.Base;
import com.common.aliyun.product.ECS;
import com.common.aliyun.product.OSS;
import com.common.aliyun.product.RDS;
import com.domain.DatabasesInstance;
import com.domain.Instance;
import com.domain.Key;
import com.mapper.DatabasesInstanceMapper;
import com.mapper.InstanceMapper;
import com.service.KeyService;
import com.service.impl.BucketServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AliYunInstanceService {
    private static Logger logger = LoggerFactory.getLogger(AliYunInstanceService.class);
    @Resource
    private InstanceMapper instanceMapper;
    @Resource
    private KeyService keyService;
    @Resource
    private BucketServiceImpl bucketService;
    @Resource
    private DatabasesInstanceMapper databasesInstanceMapper;

    @LogAnnotation(title = "获取阿里云服务器实例")
    public void getInstanceList(Key key, AtomicInteger status){
        Map<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion, List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance>> ecsLists = ECS.getECSLists(key);
        for (Map.Entry<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion,
                List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance>>
                res : ecsLists.entrySet()) {
            for (DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance instance : res.getValue()) {
                DescribeInvocationResultsResponse command = null;
                try {
                    command = ECS.createCommand(Base.createClient(key, res.getKey().regionEndpoint),
                            res.getKey().regionId,"test",ECS.getType(instance.OSType),"whoami",instance.instanceId);
                } catch (Exception e) {
                    break;
                }
                Instance instance1 = new Instance();
                instance1.setInstanceId(instance.instanceId);
                instance1.setRegion(instance.getRegionId());
                instance1.setType(instance.getOSType());
                instance1.setIp(instance.getPublicIpAddress().ipAddress.toString());
                instance1.setKeyId(key.getId());
                instance1.setOriginalKeyPair(instance.getKeyPairName());
                instance1.setOsName(instance.getOSName());
                if (command.getBody().invocation.invocationResults.invocationResult.size() >= 1){
                    instance1.setIsCommand("true");
                    instanceMapper.insert(instance1);
                }
                else {
                    instance1.setIsCommand("null");
                    instanceMapper.insert(instance1);
                }
                logger.info("添加资产:" + instance1.getInstanceId());
            }
        }
        status.decrementAndGet();
    }

    public void createUser(Key key,DatabasesInstance databasesInstance,String username,String password) throws Exception {
        RDS.createRDSUser(key,databasesInstance,username,password);
        databasesInstance.setUser(username);
        databasesInstance.setPassword(password);
        RDS.ModifyWhitelist(key,databasesInstance,false);
        databasesInstanceMapper.updateById(databasesInstance);
    }

    @LogAnnotation(title = "获取RDS列表")
    public void getRdsLists(Key key, AtomicInteger status){
        try {
            addDBInstance(RDS.getRDSLists(key),key);
        } catch (Exception e) {
            logger.error("获取rds异常" + e.getMessage());
        }
        status.decrementAndGet();
    }
    @LogAnnotation(title = "关闭外网访问")
    public void closeWan(Key key,DatabasesInstance databasesInstance) throws Exception {
        RDS.closeWan(key,databasesInstance);
        RDS.ModifyWhitelist(key,databasesInstance,true);
        databasesInstanceMapper.updateById(databasesInstance);
    }
    @LogAnnotation(title = "打开外网访问")
    public void openWan(Key key,DatabasesInstance databasesInstance) throws Exception {
        Map<String, String> res = RDS.openWan(key, databasesInstance);
        List<DescribeDBInstanceIPArrayListResponseBody.DescribeDBInstanceIPArrayListResponseBodyItemsDBInstanceIPArray> rdsWhitelist = RDS.getRDSWhitelist(key, databasesInstance);
        for (DescribeDBInstanceIPArrayListResponseBody.DescribeDBInstanceIPArrayListResponseBodyItemsDBInstanceIPArray dbInstanceIPArray : rdsWhitelist) {
            if (dbInstanceIPArray.DBInstanceIPArrayName.equals("default")){
                databasesInstance.setWhitelist(dbInstanceIPArray.securityIPList);
                databasesInstanceMapper.updateById(databasesInstance);
            }
        }
        databasesInstance.setDomain(res.get("domain") );
        databasesInstance.setPort(res.get("port"));
        databasesInstanceMapper.updateById(databasesInstance);
    }

    private void addDBInstance(List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> rdsLists,Key key) throws Exception {
        for (DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance rds : rdsLists) {
            DescribeDBInstanceNetInfoResponse describeDBInstanceNetInfoResponse = RDS.descInstanceNetType(key, rds.DBInstanceId);
            DatabasesInstance databasesInstance = new DatabasesInstance();
            databasesInstance.setInstanceId(rds.DBInstanceId);
            databasesInstance.setRegion(rds.regionId);
            databasesInstance.setKeyId(key.getId());
            databasesInstance.setInstanceName(rds.getDBInstanceId());
            databasesInstance.setType(rds.getEngine());
            for (DescribeDBInstanceNetInfoResponseBody.DescribeDBInstanceNetInfoResponseBodyDBInstanceNetInfosDBInstanceNetInfo res :
                    describeDBInstanceNetInfoResponse.body.DBInstanceNetInfos.DBInstanceNetInfo) {
                if (res.IPType.equals(RDS.publicType)){
                    databasesInstance.setDomain(res.connectionString);
                    databasesInstance.setPort(res.port);
                }
            }
            databasesInstanceMapper.insert(databasesInstance);
        }
    }

    @LogAnnotation(title = "获取存储桶")
    public void getBucketLists(Key key,AtomicInteger status){
        List<Bucket> bucketLists = OSS.getBucketLists(key);
        if (bucketLists.size() >= 1){
            for (Bucket bucketList : bucketLists) {
                com.domain.Bucket bucket = new com.domain.Bucket();
                bucket.setRegion(bucketList.getRegion());
                bucket.setName(bucketList.getName());
                bucket.setCreateById(key.getCreateById());
                bucket.setKeyId(key.getId());
                bucket.setEndPoint(bucketList.getExtranetEndpoint());
                bucket.setOwner(bucketList.getOwner().toString());
                bucketService.save(bucket);
            }
        }
        status.decrementAndGet();

    }
    @LogAnnotation(title = "绑定密钥对")
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
