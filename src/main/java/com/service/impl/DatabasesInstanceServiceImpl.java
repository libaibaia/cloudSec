package com.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.Tools;
import com.common.tencent.product.databases.*;
import com.domain.DatabasesInstance;
import com.domain.Key;
import com.service.DatabasesInstanceService;
import com.mapper.DatabasesInstanceMapper;
import com.service.KeyService;
import com.tencentcloudapi.cdb.v20170320.models.InstanceInfo;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.mariadb.v20170312.models.DBInstance;
import com.tencentcloudapi.mongodb.v20190725.models.InstanceDetail;
import com.tencentcloudapi.postgres.v20170312.models.AccountInfo;
import com.tencentcloudapi.postgres.v20170312.models.DBInstanceNetInfo;
import com.tencentcloudapi.redis.v20180412.models.InstanceSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【databases_instance】的数据库操作Service实现
* @createDate 2023-04-16 01:14:54
*/
@Service
public class DatabasesInstanceServiceImpl extends ServiceImpl<DatabasesInstanceMapper, DatabasesInstance>
    implements DatabasesInstanceService{

    private Logger logger = LoggerFactory.getLogger(DatabasesInstanceServiceImpl.class);
    public static final String redis = "redis";
    public static final String mysql = "mysql";
    public static final String postgres = "postgres";
    public static final String mariaDB = "MariaDB";
    public static final String mongoDb = "MongoDb";
    public static final String sqlServer = "sqlServer";
    
    @Resource
    @Lazy
    private KeyService keyService;

    public void addInstance(DatabasesInstance databases){
        this.getBaseMapper().insert(databases);
    }
    public void delInstanceByKeyId(Integer key_id){
        QueryWrapper<DatabasesInstance> databasesQueryWrapper = new QueryWrapper<>();
        databasesQueryWrapper.eq("key_id",key_id);
        this.getBaseMapper().delete(databasesQueryWrapper);
    }
    public DatabasesInstance getInstanceById(Integer id){
        return this.getBaseMapper().selectById(id);
    }
    public List<DatabasesInstance> getInstanceList(String key){
        QueryWrapper<DatabasesInstance> databasesQueryWrapper = new QueryWrapper<>();
        databasesQueryWrapper.eq("secretId",key);
        return this.getBaseMapper().selectList(databasesQueryWrapper);
    }
    private void getMysqlList(Key key){
        Mysql mysql = new Mysql();
        List<InstanceInfo> mysqlLists = mysql.getMysqlLists(key);
        for (InstanceInfo mysqlList : mysqlLists) {
            DatabasesInstance databases = new DatabasesInstance(mysqlList.getInstanceId(),mysqlList.getInstanceName(),mysqlList.getWanDomain(),
                    mysqlList.getRegion(),mysqlList.getWanPort().toString(),key.getId(),DatabasesInstanceServiceImpl.mysql);
            addInstance(databases);
            logger.info("添加mysql实例" + databases.getInstanceId());
        }
    }
    //创建mysql用户
    public String createDBUser(Integer id,String userName,String password) throws TencentCloudSDKException {
        return createUser(id,userName,password);
    }
    public void getSqlServerList(Key key) {
        List<com.tencentcloudapi.sqlserver.v20180328.models.DBInstance> dbLists;
        try {
            dbLists = SqlServer.getDBLists(key);
            for (com.tencentcloudapi.sqlserver.v20180328.models.DBInstance dbList : dbLists) {
                DatabasesInstance databases = new DatabasesInstance(dbList.getInstanceId(),dbList.getName(),dbList.getDnsPodDomain(),
                        dbList.getRegion(),dbList.getTgwWanVPort().toString(),key.getId(), sqlServer);
                addInstance(databases);
            }
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }
    public void getRedisLists(Key key){
        try {
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
                addInstance(databases);
            }
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }

    private void getMongoDbList(Key key){
        try {
            List<InstanceDetail> dbList = MongoDb.getDBList(key);
            for (InstanceDetail instanceDetail : dbList) {
                DatabasesInstance databases = new DatabasesInstance(instanceDetail.getInstanceId(),instanceDetail.getInstanceName(),null,instanceDetail.getRegion(),
                        "0",key.getId(),mongoDb);
                addInstance(databases);
            }
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }
    //获取Mariadb
    private void getMariadbList(Key key){
        try {
            List<DBInstance> mariaDBLists = MariaDB.getMariaDBLists(key);
            for (DBInstance mariaDBList : mariaDBLists) {
                DatabasesInstance mariadb = new DatabasesInstance(mariaDBList.getInstanceId(), mariaDBList.getInstanceName(), mariaDBList.getWanDomain(),
                        mariaDBList.getRegion(), mariaDBList.getWanPort().toString(), key.getId(), mariaDB);
                addInstance(mariadb);
            }
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }
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
            addInstance(databases);
        }
    }

    private void updateUser(DatabasesInstance databasesInstance,String username,String password){
        databasesInstance.setUser(username);
        databasesInstance.setPassword(password);
        this.getBaseMapper().updateById(databasesInstance);
    }
    private void updateWan(DatabasesInstance databasesInstance,String domain,String port){
        databasesInstance.setDomain(domain);
        databasesInstance.setPort(port);
        this.getBaseMapper().updateById(databasesInstance);
    }

    /**
     * 根据类型选择创建用户的逻辑
     * @param id
     * @param userName
     * @param password
     * @throws TencentCloudSDKException
     */
    private String createUser(Integer id,String userName,String password) throws TencentCloudSDKException {
        DatabasesInstance databasesById = getInstanceById(id);
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

    //关闭外网访问
    public String closeWan(Integer instanceId) throws TencentCloudSDKException {
        DatabasesInstance databasesById = getInstanceById(instanceId);
        Key keyByID = keyService.getById(databasesById.getKeyId());
        String message = null;
        if (Integer.parseInt(StpUtil.getLoginId().toString()) == keyByID.getCreateById()){
            switch (databasesById.getType()){
                case mysql:
                    Mysql.closeWan(keyByID, databasesById.getRegion(), databasesById.getInstanceId());
                    updateWan(databasesById,"","");
                    break;
                case mariaDB:
                    MariaDB.closeWan(keyByID,databasesById.getInstanceId(),databasesById.getRegion());
                    updateWan(databasesById,"","");
                    break;
                case redis:
                    Redis.closeWan(keyByID,databasesById.getInstanceId(),databasesById.getRegion());
                    updateWan(databasesById,"","");
                    break;
                case postgres:
                    PostgreSQL.closeWan(keyByID,databasesById.getRegion(),databasesById.getInstanceId());
                    updateWan(databasesById,"","");
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
    //打开外网访问
    public String openWan(Integer instanceId) throws TencentCloudSDKException {
        DatabasesInstance databasesById = getInstanceById(instanceId);
        Key keyByID = keyService.getById(databasesById.getKeyId());
        String message = null;
        if (Integer.parseInt(StpUtil.getLoginId().toString()) == keyByID.getCreateById()){
            switch (databasesById.getType()){
                case mysql:
                    Mysql.openWan(keyByID, databasesById.getRegion(), databasesById.getInstanceId());
                    //此处休眠是为了等待腾讯云更新状态
                    Tools.executorService.submit(() -> {
                        try {
                            Thread.sleep(1000 * 30);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        InstanceInfo[] mysql1 = Mysql.getMysql(keyByID, databasesById.getRegion(), databasesById.getInstanceId());
                        for (InstanceInfo instanceInfo : mysql1) {
                            String wanDomain = instanceInfo.getWanDomain();
                            Long wanPort = instanceInfo.getWanPort();
                            updateWan(databasesById,wanDomain, String.valueOf(wanPort));
                        }
                    });
                    break;
                case mariaDB:
                    MariaDB.openWan(keyByID, databasesById.getRegion(), databasesById.getInstanceId());
                    //此处休眠是为了等待腾讯云更新状态
                    Tools.executorService.submit(() -> {
                        try {
                            Thread.sleep(1000 * 30);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        DBInstance[] dbInstanceInfo;
                        try {
                            dbInstanceInfo = MariaDB.getDBInstanceInfo(keyByID, databasesById.getInstanceId(), databasesById.getRegion());
                        } catch (TencentCloudSDKException e) {
                            throw new RuntimeException(e);
                        }
                        for (DBInstance dbInstance : dbInstanceInfo) {
                            String wanDomain = dbInstance.getWanDomain();
                            Long wanPort = dbInstance.getWanPort();
                            updateWan(databasesById,wanDomain, String.valueOf(wanPort));
                        }
                    });
                    break;
                case redis:
                    Redis.openWan(keyByID,databasesById.getInstanceId(),databasesById.getRegion());
                    //此处休眠是为了等待腾讯云更新状态
                    Tools.executorService.submit(() -> {
                        try {
                            Thread.sleep(1000 * 30);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        InstanceSet[] dbInstanceInfo1;
                        try {
                            dbInstanceInfo1 = Redis.getDBInstanceInfo(keyByID, databasesById.getInstanceId(), databasesById.getRegion());
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
                            updateWan(databasesById,address, String.valueOf(port));
                        }
                    });
                    break;
                case postgres:
                    PostgreSQL.openWan(keyByID,databasesById.getInstanceId(),databasesById.getRegion());
                    //此处休眠是为了等待腾讯云更新状态
                    Tools.executorService.submit(() -> {
                        try {
                            Thread.sleep(1000 * 30);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        com.tencentcloudapi.postgres.v20170312.models.DBInstance[] dbInstanceInfo2;
                        try {
                            dbInstanceInfo2 = PostgreSQL.getDBInstanceInfo(keyByID, databasesById.getInstanceId(), databasesById.getRegion());
                        } catch (TencentCloudSDKException e) {
                            throw new RuntimeException(e);
                        }
                        for (com.tencentcloudapi.postgres.v20170312.models.DBInstance dbInstance : dbInstanceInfo2) {
                            DBInstanceNetInfo[] dbInstanceNetInfo = dbInstance.getDBInstanceNetInfo();
                            for (DBInstanceNetInfo instanceNetInfo : dbInstanceNetInfo) {
                                if (!instanceNetInfo.getStatus().equals("closed") && instanceNetInfo.getNetType().equals("public")){
                                    String address = instanceNetInfo.getAddress();
                                    Long port = instanceNetInfo.getPort();
                                    updateWan(databasesById,address, String.valueOf(port));
                                }
                            }
                        }
                    });
                    break;
                case sqlServer:
                    message = "sqlserver目前不支持关闭开启";
                    break;
                default:
                    message = "未知类型或" + databasesById.getType() + "不支持此操作";
            }
        }
        return message;
    }
    public void getDatabasesLists(Key key){
        getMysqlList(key);
        getMariadbList(key);
        getSqlServerList(key);
        getRedisLists(key);
        getPostgresList(key);
        getMongoDbList(key);
    }
    public List<Map<String, String>> getUserLists(Integer id){
        DatabasesInstance instanceByID = this.getBaseMapper().selectById(id);
        Key keyByID = keyService.getById(instanceByID.getKeyId());
        try {
            Map<String,String> map = new HashMap<>();
            List<Map<String,String>> list = new ArrayList<>();
            ArrayList<AccountInfo> userLists = PostgreSQL.getUserLists(keyByID, instanceByID.getRegion(), instanceByID.getInstanceId());
            if (userLists != null){
                for (AccountInfo userList : userLists) {
                    map.put("label",userList.getUserName());
                    map.put("value",userList.getUserName());
                    list.add(map);
                }
            }

            return list;
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
    }
}




