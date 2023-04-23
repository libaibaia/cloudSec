package com.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.Tools;
import com.common.Type;
import com.common.tencent.product.databases.*;
import com.domain.DatabasesInstance;
import com.domain.Key;
import com.service.DatabasesInstanceService;
import com.mapper.DatabasesInstanceMapper;
import com.service.KeyService;
import com.service.impl.aliyun.AliYunInstanceService;
import com.service.impl.tencent.TencentInstanceService;
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
    @Resource
    private TencentInstanceService tencentInstanceService;
    @Resource
    private AliYunInstanceService aliYunInstanceService;

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

    //创建mysql用户
    public String createDBUser(Integer id,String userName,String password) throws TencentCloudSDKException {
        return createUser(id,userName,password);
    }


    /**
     * 根据类型选择创建用户的逻辑
     * @param id
     * @param userName
     * @param password
     * @throws TencentCloudSDKException
     */
    private String createUser(Integer id,String userName,String password) {
        DatabasesInstance databasesById = getInstanceById(id);
        Key keyByID = keyService.getById(databasesById.getKeyId());
        String message = null;
        if (keyByID.getType().equals(Type.Tencent.toString())){
            try {
                tencentInstanceService.createUser(keyByID,databasesById,userName,password);
            } catch (TencentCloudSDKException e) {
                message = e.getMessage();
            }
        }if (keyByID.getType().equals(Type.AliYun.toString())){
        }

        return message;
    }

    //关闭外网访问
    public String closeWan(Integer instanceId){
        String message = null;
        DatabasesInstance databasesInstance = this.baseMapper.selectById(instanceId);
        Key byId = keyService.getById(databasesInstance.getKeyId());
        if (byId.getType().equals(Type.Tencent.toString())){
            try {
                tencentInstanceService.closeWan(byId,databasesInstance);
            } catch (TencentCloudSDKException e) {
                message = e.getMessage();
            }
        }if (byId.getType().equals(Type.AliYun.toString())){
            try {
                aliYunInstanceService.closeWan(byId,databasesInstance);
            } catch (Exception e) {
                message = e.getMessage();
            }
        }
        return message;
    }
    //打开外网访问
    public String openWan(Integer instanceId){
        String message = null;
        DatabasesInstance databasesInstance = this.baseMapper.selectById(instanceId);
        Key byId = keyService.getById(databasesInstance.getKeyId());
        if (byId.getType().equals(Type.Tencent.toString())){
            try {
                tencentInstanceService.openWan(byId,databasesInstance);
            } catch (TencentCloudSDKException e) {
                message = e.getMessage();
            }
        }if (byId.getType().equals(Type.AliYun.toString())){
            try {
                aliYunInstanceService.openWan(byId,databasesInstance);
            } catch (Exception e) {
                message = e.getMessage();
            }
        }
        return message;
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




