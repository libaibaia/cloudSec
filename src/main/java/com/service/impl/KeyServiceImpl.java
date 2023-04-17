package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.Tools;
import com.common.Type;
import com.domain.Key;
import com.service.KeyService;
import com.mapper.KeyMapper;
import com.service.impl.aliyun.AliYunInstanceService;
import com.service.impl.tencent.TencentInstanceService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Administrator
* @description 针对表【key】的数据库操作Service实现
* @createDate 2023-04-16 01:14:54
*/
@Service
public class KeyServiceImpl extends ServiceImpl<KeyMapper, Key>
    implements KeyService{

    @Resource
    @Lazy
    private AliYunInstanceService aliYunInstanceService;
    @Resource
    @Lazy
    private TencentInstanceService tencentInstanceService;
    @Resource
    @Lazy
    private BucketServiceImpl bucketService;
    @Resource
    @Lazy
    private DatabasesInstanceServiceImpl databasesInstanceService;
    public List<Key> getKeysByCreateId(Integer id){
        QueryWrapper<Key> keyQueryWrapper = new QueryWrapper<>();
        keyQueryWrapper.eq("create_by_id",id);
        return list(keyQueryWrapper);
    }
    public boolean saveKey(Key key){
        QueryWrapper<Key> keyQueryWrapper = new QueryWrapper<>();
        keyQueryWrapper.eq("secretId",key.getSecretid());
        Key one = getOne(keyQueryWrapper);
        if (one != null){
            one.setSecretid(key.getSecretid());
            one.setSecretkey(key.getSecretkey());
            updateById(one);
            Tools.executorService.submit(() -> execute(key));
            return true;
        }else {
            boolean save = save(key);
            Tools.executorService.submit(() -> execute(key));
            return save;
        }
    }
    public void execute(Key key){
        Type type = Type.valueOf(key.getType());
        switch (type){
            case AliYun:
            {
                aliYunInstanceService.getInstanceList(key);
                aliYunInstanceService.getBucketLists(key);
            }
            case Tencent:
            {
                tencentInstanceService.getInstanceList(key);
                bucketService.getBucketList(key);
                databasesInstanceService.getDatabasesLists(key);
            }
        }
    }
}




