//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.Type;
import com.common.tencent.product.databases.PostgreSQL;
import com.domain.DatabasesInstance;
import com.domain.Key;
import com.mapper.DatabasesInstanceMapper;
import com.service.DatabasesInstanceService;
import com.service.KeyService;
import com.service.impl.aliyun.AliYunInstanceService;
import com.service.impl.huawei.HuaWeiService;
import com.service.impl.tencent.TencentInstanceService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.postgres.v20170312.models.AccountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabasesInstanceServiceImpl extends ServiceImpl<DatabasesInstanceMapper, DatabasesInstance> implements DatabasesInstanceService {
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
    @Autowired
    private HuaWeiService huaWeiService;

    public DatabasesInstanceServiceImpl() {
    }

    public void delInstanceByKeyId(Integer key_id) {
        QueryWrapper<DatabasesInstance> databasesQueryWrapper = new QueryWrapper();
        databasesQueryWrapper.eq("key_id", key_id);
        this.getBaseMapper().delete(databasesQueryWrapper);
    }

    public DatabasesInstance getInstanceById(Integer id) {
        return this.getBaseMapper().selectById(id);
    }

    public List<DatabasesInstance> getInstanceList(String key) {
        QueryWrapper<DatabasesInstance> databasesQueryWrapper = new QueryWrapper();
        databasesQueryWrapper.eq("secretId", key);
        return this.getBaseMapper().selectList(databasesQueryWrapper);
    }

    public String createDBUser(Integer id, String userName, String password) throws Exception {
        return this.createUser(id, userName, password);
    }

    private String createUser(Integer id, String userName, String password) throws Exception {
        DatabasesInstance databasesById = this.getInstanceById(id);
        Key keyByID = this.keyService.getById(databasesById.getKeyId());
        String message = null;
        if (keyByID.getType().equals(Type.Tencent.toString())) {
            try {
                this.tencentInstanceService.createUser(databasesById, userName, password);
            } catch (TencentCloudSDKException e) {
                message = e.getMessage();
            }
        }
        if (keyByID.getType().equals(Type.AliYun.toString())) {
            this.aliYunInstanceService.createUser(keyByID, databasesById, userName, password);
        }
        if (keyByID.getType().equals(Type.HUAWEI.toString())){
            message = huaWeiService.createDBUser(keyByID, databasesById, password, userName);
        }
        else message = "不支持该类型的操作";
        return message;
    }

    public String closeWan(Integer instanceId) {
        String message = null;
        DatabasesInstance databasesInstance = this.baseMapper.selectById(instanceId);
        Key byId = this.keyService.getById(databasesInstance.getKeyId());
        if (byId.getType().equals(Type.Tencent.toString())) {
            try {
                this.tencentInstanceService.closeWan(byId, databasesInstance);
            } catch (TencentCloudSDKException e) {
                message = e.getMessage();
            }
        }

        if (byId.getType().equals(Type.AliYun.toString())) {
            try {
                this.aliYunInstanceService.closeWan(byId, databasesInstance);
            } catch (Exception e) {
                message = e.getMessage();
            }
        }
        else message = "不支持该类型得操作";

        return message;
    }

    public String openWan(Integer instanceId) {
        String message = null;
        DatabasesInstance databasesInstance = this.baseMapper.selectById(instanceId);
        Key byId = this.keyService.getById(databasesInstance.getKeyId());
        if (byId.getType().equals(Type.Tencent.toString())) {
            try {
                this.tencentInstanceService.openWan(byId, databasesInstance);
            } catch (TencentCloudSDKException e) {
                message = e.getMessage();
            }
        }

        if (byId.getType().equals(Type.AliYun.toString())) {
            try {
                this.aliYunInstanceService.openWan(byId, databasesInstance);
            } catch (Exception e) {
                message = e.getMessage();
            }
        }
        else {
            message = "不支持该类型的操作";
        }

        return message;
    }

    public List<Map<String, String>> getUserLists(Integer id) {
        DatabasesInstance instanceByID = this.getBaseMapper().selectById(id);
        Key keyByID = this.keyService.getById(instanceByID.getKeyId());

        try {
            Map<String, String> map = new HashMap<>();
            List<Map<String, String>> list = new ArrayList<>();
            ArrayList<AccountInfo> userLists = PostgreSQL.getUserLists(keyByID, instanceByID.getRegion(), instanceByID.getInstanceId());
            assert userLists != null;
            for (AccountInfo userList : userLists) {
                map.put("label", userList.getUserName());
                map.put("value", userList.getUserName());
                list.add(map);
            }
            return list;
            } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
        }
}
