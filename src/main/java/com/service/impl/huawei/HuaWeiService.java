package com.service.impl.huawei;


import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.StrUtil;
import com.common.huawei.ECS;
import com.common.huawei.IAM;
import com.common.huawei.OBS;
import com.common.huawei.RDS;
import com.domain.*;
import com.huaweicloud.sdk.ecs.v2.model.ServerAddress;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import com.huaweicloud.sdk.rds.v3.model.InstanceResponse;
import com.service.DatabasesInstanceService;
import com.service.HuaweiObsRegionService;
import com.service.InstanceService;
import com.service.impl.BucketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class HuaWeiService {

    @Resource
    private HuaweiObsRegionService huaweiObsRegionService;
    @Autowired
    @Lazy
    private DatabasesInstanceService databasesInstanceService;
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
        List<ServerDetail> ecsLists = ECS.getEcsLists(key);
        for (ServerDetail ecsList : ecsLists) {
            Instance instance = new Instance();
            instance.setInstanceId(ecsList.getId());
            instance.setOsName(ecsList.getOsEXTSRVATTRInstanceName());
            instance.setRegion(ecsList.getOsEXTAZAvailabilityZone());
            instance.setOriginalKeyPair(ecsList.getKeyName());
            instance.setIp(getIp(ecsList));
            instance.setIsCommand("null");
            instance.setKeyName(key.getName());
            instance.setKeyId(key.getId());
            instance.setPublicKey(ecsList.getTenantId());
            instance.setPrivateKey("华为云不支持密钥对绑定，点击密钥对将重置密码");
            instance.setType(ecsList.getMetadata().get("os_type"));
            instanceService.save(instance);
        }
        status.decrementAndGet();
    }

    public void getRDSLists(Key key, AtomicInteger status){
        List<InstanceResponse> rdsLists = RDS.getRDSLists(key);
        for (InstanceResponse rdsList : rdsLists) {
            DatabasesInstance databasesInstance = new DatabasesInstance();
            databasesInstance.setPort(rdsList.getPort().toString());
            databasesInstance.setInstanceName(rdsList.getName());
            databasesInstance.setType(rdsList.getDatastore().getType().getValue());
            databasesInstance.setKeyId(key.getId());
            databasesInstance.setKeyName(key.getName());
            databasesInstance.setRegion(rdsList.getRegion());
            databasesInstance.setInstanceId(rdsList.getId());
            databasesInstance.setDomain(getIp(rdsList.getPublicIps()));
            databasesInstanceService.save(databasesInstance);
        }
        status.decrementAndGet();
    }
    private String getIp(List<String> strings){
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string);
            stringBuilder.append("/");
        }
        return stringBuilder.toString();
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

    public SaResult restPassword(Key key, Instance instance, String password){
        String s = ECS.restartPassword(key, instance, password);
        if (StrUtil.isBlank(s)){
            return SaResult.error("重置失败，可能不支持");
        }
        if (instance.getType().equals("Linux")){
            instance.setPrivateKey("root" + ":" + password);
            instanceService.updateById(instance);
        }
        if (instance.getType().equals("Windows")){
            instance.setPrivateKey("Administrator" + ":" + password);
            instanceService.updateById(instance);
        }
        return SaResult.ok("重置成功");
    }

    public String createDBUser(Key key, DatabasesInstance instance, String password, String username){
        if (instance.getType().equals("MySQL")) {
            boolean mysqlUser = RDS.createMysqlUser(instance, key, password, username);
            if (mysqlUser) {
                instance.setUser(username);
                instance.setPassword(password);
                databasesInstanceService.updateById(instance);
                return "ok";
            }
        }
        if (instance.getType().equals("PostgreSQL")){
            boolean postgreSQLUser = RDS.createPostgreSQLUser(instance, key, password, username);
            if (postgreSQLUser) {
                instance.setUser(username);
                instance.setPassword(password);
                databasesInstanceService.updateById(instance);
                return "ok";
            }
        }
        if (instance.getType().equals("SQLServer")){
            boolean sqlServerUser = RDS.createSqlServerUser(instance, key, password, username);
            if (sqlServerUser) {
                instance.setUser(username);
                instance.setPassword(password);
                databasesInstanceService.updateById(instance);
                return "ok";
            }
        }

        return null;
    }

    public Map<String, String> createConsoleUser(Key key, String username, String passwprd){
        return IAM.createIamUser(key, username, passwprd);
    }
}
