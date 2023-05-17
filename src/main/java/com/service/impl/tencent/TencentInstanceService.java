package com.service.impl.tencent;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.LogAnnotation;
import com.common.Tools;
import com.common.Type;
import com.common.tencent.product.cvm.CVM;
import com.common.tencent.product.databases.*;
import com.domain.DatabasesInstance;
import com.domain.Key;
import com.mapper.DatabasesInstanceMapper;
import com.mapper.InstanceMapper;
import com.service.impl.DatabasesInstanceServiceImpl;
import com.service.impl.KeyServiceImpl;
import com.tencentcloudapi.cdb.v20170320.models.InstanceInfo;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.models.CreateKeyPairResponse;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import com.tencentcloudapi.mariadb.v20170312.models.DBInstance;
import com.tencentcloudapi.mongodb.v20190725.models.InstanceDetail;
import com.tencentcloudapi.postgres.v20170312.models.DBInstanceNetInfo;
import com.tencentcloudapi.redis.v20180412.models.InstanceSet;
import com.tencentcloudapi.tat.v20201028.models.InvocationTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.service.impl.DatabasesInstanceServiceImpl.*;


@Service
public class TencentInstanceService {
    private static Logger logger = LoggerFactory.getLogger(TencentInstanceService.class);
    @Resource
    private InstanceMapper instanceMapper;
    @Resource
    private KeyServiceImpl keyService;
    @Resource
    @Lazy
    private DatabasesInstanceMapper databasesInstanceMapper;

    @LogAnnotation(title = "获取腾讯云服务器实例列表")
    public void getInstanceList(Key key, AtomicInteger status){
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
                    instance.setOriginalKeyPair(Arrays.toString(instance1.getLoginSettings().getKeyIds()));
                    instance.setOsName(instance1.getOsName());
                    instanceMapper.insert(instance);
                    logger.info("成功添加一个资源，id:" + invocationTask.getInstanceId());
                }
            }
        } catch (TencentCloudSDKException e) {
            logger.error(e.getMessage());
        }
        status.decrementAndGet();
    }

    @LogAnnotation(title = "绑定密钥")
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



    @LogAnnotation(title = "关闭外网访问")
    public String closeWan(Key key,DatabasesInstance databasesInstance) throws TencentCloudSDKException {
        String message = null;
        if (Integer.parseInt(StpUtil.getLoginId().toString()) == key.getCreateById()){
            switch (databasesInstance.getType()){
                case mysql:
                    Mysql.closeWan(key, databasesInstance.getRegion(), databasesInstance.getInstanceId());
                    updateWan(databasesInstance,"","");
                    break;
                case mariaDB:
                    MariaDB.closeWan(key,databasesInstance.getInstanceId(),databasesInstance.getRegion());
                    updateWan(databasesInstance,"","");
                    break;
                case redis:
                    Redis.closeWan(key,databasesInstance.getInstanceId(),databasesInstance.getRegion());
                    updateWan(databasesInstance,"","");
                    break;
                case postgres:
                    PostgreSQL.closeWan(key,databasesInstance.getRegion(),databasesInstance.getInstanceId());
                    updateWan(databasesInstance,"","");
                    break;
                case sqlServer:
                    message = "sqlserver目前不支持关闭开启";
                    break;
                default:
                    message = "未找到匹配类型,关闭失败";
            }
        }
        return message;
    }
    @LogAnnotation(title = "获取SQLserver列表")
    public void getSqlServerList(Key key){
        List<com.tencentcloudapi.sqlserver.v20180328.models.DBInstance> dbLists;
        try {
            dbLists = SqlServer.getDBLists(key);
            for (com.tencentcloudapi.sqlserver.v20180328.models.DBInstance dbList : dbLists) {
                DatabasesInstance databases = new DatabasesInstance(dbList.getInstanceId(),dbList.getName(),dbList.getDnsPodDomain(),
                        dbList.getRegion(),dbList.getTgwWanVPort().toString(),key.getId(), sqlServer);

                databasesInstanceMapper.insert(databases);
            }
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateWan(DatabasesInstance databasesInstance,String domain,String port){
        databasesInstance.setDomain(domain);
        databasesInstance.setPort(port);
        databasesInstanceMapper.updateById(databasesInstance);
    }
    @LogAnnotation(title = "创建数据库用户")
    private String createUser(Integer id,String userName,String password) throws TencentCloudSDKException {
        DatabasesInstance databasesById = databasesInstanceMapper.selectById(id);
        Key keyByID = keyService.getById(databasesById.getKeyId());
        String message = null;
        if (Integer.parseInt(StpUtil.getLoginId().toString()) == keyByID.getCreateById()){
            switch (databasesById.getType()){
                case mysql:
                    Mysql.createMysqlUser(keyByID,databasesById.getRegion(),databasesById.getInstanceId(),userName,password);
                    updateUser(databasesById,userName,password);
                    break;
                case mariaDB:
                    MariaDB.createUser(keyByID,databasesById.getInstanceId(),databasesById.getRegion(),userName,password);
                    updateUser(databasesById,userName,password);
                    break;
                case sqlServer:
                    SqlServer.createUser(keyByID,userName,password,databasesById.getInstanceId(),databasesById.getRegion());
                    updateUser(databasesById,userName,password);
                    break;
                case redis:
                    Redis.createUser(keyByID,databasesById.getInstanceId(),databasesById.getRegion(),userName,password);
                    updateUser(databasesById,userName,password);
                    break;
                case postgres:
                    PostgreSQL.updatePassword(keyByID,databasesById.getInstanceId(),databasesById.getRegion(),userName,password);
                    updateUser(databasesById,userName,password);
                    break;
                case mongoDb:
                    message = "当前数据库不支持创建用户";
            }
        }
        return message;
    }

    public void updateUser(DatabasesInstance databasesInstance,String username,String password){
        databasesInstance.setUser(username);
        databasesInstance.setPassword(password);
        databasesInstanceMapper.updateById(databasesInstance);
    }

    @LogAnnotation(title = "打开外网访问")
    public String openWan(Key key,DatabasesInstance databasesInstance) throws TencentCloudSDKException {
        String message = null;
        if (Integer.parseInt(StpUtil.getLoginId().toString()) == key.getCreateById()){
            switch (databasesInstance.getType()){
                case mysql:
                    Mysql.openWan(key, databasesInstance.getRegion(), databasesInstance.getInstanceId());
                    //此处休眠是为了等待腾讯云更新状态
                    Tools.executorService.submit(() -> {
                        try {
                            Thread.sleep(1000 * 30);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        InstanceInfo[] mysql1 = Mysql.getMysql(key, databasesInstance.getRegion(), databasesInstance.getInstanceId());
                        for (InstanceInfo instanceInfo : mysql1) {
                            String wanDomain = instanceInfo.getWanDomain();
                            Long wanPort = instanceInfo.getWanPort();
                            updateWan(databasesInstance,wanDomain, String.valueOf(wanPort));
                        }
                    });
                    break;
                case mariaDB:
                    MariaDB.openWan(key, databasesInstance.getRegion(), databasesInstance.getInstanceId());
                    //此处休眠是为了等待腾讯云更新状态
                    Tools.executorService.submit(() -> {
                        try {
                            Thread.sleep(1000 * 30);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        DBInstance[] dbInstanceInfo;
                        try {
                            dbInstanceInfo = MariaDB.getDBInstanceInfo(key, databasesInstance.getInstanceId(), databasesInstance.getRegion());
                        } catch (TencentCloudSDKException e) {
                            throw new RuntimeException(e);
                        }
                        for (DBInstance dbInstance : dbInstanceInfo) {
                            String wanDomain = dbInstance.getWanDomain();
                            Long wanPort = dbInstance.getWanPort();
                            updateWan(databasesInstance,wanDomain, String.valueOf(wanPort));
                        }
                    });
                    break;
                case redis:
                    Redis.openWan(key,databasesInstance.getInstanceId(),databasesInstance.getRegion());
                    //此处休眠是为了等待腾讯云更新状态
                    Tools.executorService.submit(() -> {
                        try {
                            Thread.sleep(1000 * 30);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        InstanceSet[] dbInstanceInfo1;
                        try {
                            dbInstanceInfo1 = Redis.getDBInstanceInfo(key, databasesInstance.getInstanceId(), databasesInstance.getRegion());
                        } catch (TencentCloudSDKException e) {
                            throw new RuntimeException(e);
                        }
                        for (InstanceSet instanceSet : dbInstanceInfo1) {
                            String address = null;
                            long port = 0L;
                            String wanAddress = instanceSet.getWanAddress();
                            if (wanAddress.contains(":"))  {
                                String[] split = instanceSet.getWanAddress().split(":");
                                address = split[0];
                                port = Long.parseLong(split[1]);
                            }
                            updateWan(databasesInstance,address, String.valueOf(port));
                        }
                    });
                    break;
                case postgres:
                    PostgreSQL.openWan(key,databasesInstance.getInstanceId(),databasesInstance.getRegion());
                    //此处休眠是为了等待腾讯云更新状态
                    Tools.executorService.submit(() -> {
                        try {
                            Thread.sleep(1000 * 30);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        com.tencentcloudapi.postgres.v20170312.models.DBInstance[] dbInstanceInfo2;
                        try {
                            dbInstanceInfo2 = PostgreSQL.getDBInstanceInfo(key, databasesInstance.getInstanceId(), databasesInstance.getRegion());
                        } catch (TencentCloudSDKException e) {
                            throw new RuntimeException(e);
                        }
                        for (com.tencentcloudapi.postgres.v20170312.models.DBInstance dbInstance : dbInstanceInfo2) {
                            DBInstanceNetInfo[] dbInstanceNetInfo = dbInstance.getDBInstanceNetInfo();
                            for (DBInstanceNetInfo instanceNetInfo : dbInstanceNetInfo) {
                                if (!instanceNetInfo.getStatus().equals("closed") && instanceNetInfo.getNetType().equals("public")){
                                    String address = instanceNetInfo.getAddress();
                                    Long port = instanceNetInfo.getPort();
                                    updateWan(databasesInstance,address, String.valueOf(port));
                                }
                            }
                        }
                    });
                    break;
                case sqlServer:
                    message = "sqlserver目前不支持关闭开启";
                    break;
                default:
                    message = "未知类型或" + databasesInstance.getType() + "不支持此操作";
            }
        }
        return message;
    }
    /**
     * 根据类型选择创建用户的逻辑
     * @param userName
     * @param password
     * @throws TencentCloudSDKException
     */
    @LogAnnotation(title = "创建数据库用户")
    public void createUser(DatabasesInstance databasesInstance,String userName,String password) throws TencentCloudSDKException {
        createUser(databasesInstance.getId(),userName,password);
    }

    @LogAnnotation(title = "获取Redis数据库")
    public void getRedisLists(Key key) throws TencentCloudSDKException {
        List<InstanceSet> redisLists = Redis.getRedisLists(key);
        for (InstanceSet redisList : redisLists) {
            String address = "";
            long port = 0L;
            String wanAddress = redisList.getWanAddress();
            if (wanAddress.contains(":"))  {
                String[] split = redisList.getWanAddress().split(":");
                address = split[0];
                port = Long.parseLong(split[1]);
            }
            DatabasesInstance databases = new DatabasesInstance(redisList.getInstanceId(),redisList.getInstanceName(),
                    address,redisList.getRegion(), String.valueOf(port),key.getId(),redis);
            databasesInstanceMapper.insert(databases);
        }
    }

    @LogAnnotation(title = "获取MongoDb数据库")
    private void getMongoDbList(Key key) throws TencentCloudSDKException {
        List<InstanceDetail> dbList = MongoDb.getDBList(key);
        for (InstanceDetail instanceDetail : dbList) {
            DatabasesInstance databases = new DatabasesInstance(instanceDetail.getInstanceId(),instanceDetail.getInstanceName(),null,instanceDetail.getRegion(),
                    "0",key.getId(),mongoDb);
            databasesInstanceMapper.insert(databases);
        }

    }
    //获取Mariadb
    @LogAnnotation(title = "获取Mariadb")
    private void getMariadbList(Key key){
        try {
            List<DBInstance> mariaDBLists = MariaDB.getMariaDBLists(key);
            for (DBInstance mariaDBList : mariaDBLists) {
                DatabasesInstance mariadb = new DatabasesInstance(mariaDBList.getInstanceId(), mariaDBList.getInstanceName(), mariaDBList.getWanDomain(),
                        mariaDBList.getRegion(), mariaDBList.getWanPort().toString(), key.getId(), mariaDB);
                databasesInstanceMapper.insert(mariadb);
            }
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }
    @LogAnnotation(title = "获取Postgres")
    private void getPostgresList(Key key){
        List<com.tencentcloudapi.postgres.v20170312.models.DBInstance> postgreSQLList = PostgreSQL.getPostgreSQLList(key);
        for (com.tencentcloudapi.postgres.v20170312.models.DBInstance dbInstance : postgreSQLList) {
            DBInstanceNetInfo[] dbInstanceNetInfo = dbInstance.getDBInstanceNetInfo();
            String address = null;
            Long port = 0L;
            for (DBInstanceNetInfo instanceNetInfo : dbInstanceNetInfo) {
                if (instanceNetInfo.getNetType().equals("public") && !instanceNetInfo.getStatus().equals("closed")){
                    address = instanceNetInfo.getAddress();
                    port = instanceNetInfo.getPort();
                }
            }
            DatabasesInstance databases = new DatabasesInstance(dbInstance.getDBInstanceId(),dbInstance.getDBInstanceName(),address,dbInstance.getRegion(),String.valueOf(port),
                    key.getId(),postgres);
            databasesInstanceMapper.insert(databases);
        }
    }
    @LogAnnotation(title = "获取mysql")
    private void getMysqlList(Key key){
        Mysql mysql = new Mysql();
        List<InstanceInfo> mysqlLists = mysql.getMysqlLists(key);
        for (InstanceInfo mysqlList : mysqlLists) {
            DatabasesInstance databases = new DatabasesInstance(mysqlList.getInstanceId(),mysqlList.getInstanceName(),mysqlList.getWanDomain(),
                    mysqlList.getRegion(),mysqlList.getWanPort().toString(),key.getId(),DatabasesInstanceServiceImpl.mysql);
            databasesInstanceMapper.insert(databases);
            logger.info("添加mysql实例" + databases.getInstanceId());
        }
    }

    public void getDBLists(Key key, AtomicInteger status){
        try {
            getRedisLists(key);
            getMongoDbList(key);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
        getMysqlList(key);
        getMariadbList(key);
        getSqlServerList(key);
        getPostgresList(key);
        status.decrementAndGet();
    }
}
