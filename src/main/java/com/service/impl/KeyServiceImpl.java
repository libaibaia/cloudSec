package com.service.impl;

import com.aliyun.ecs20140526.models.DescribeRegionsResponseBody;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.LogAnnotation;
import com.common.Tools;
import com.common.Type;
import com.common.aliyun.Base;
import com.domain.Key;
import com.service.KeyService;
import com.mapper.KeyMapper;
import com.service.impl.aliyun.AliYunInstanceService;
import com.service.impl.qiniu.QiNiuService;
import com.service.impl.tencent.TencentInstanceService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.coyote.http11.Constants.a;

/**
 * @author Administrator
 * @description 针对表【key】的数据库操作Service实现
 * @createDate 2023-04-16 01:14:54
 */
@Service
@Slf4j
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
    private QiNiuService qiNiuService;
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
    public Boolean saveKey(Key key){
        QueryWrapper<Key> keyQueryWrapper = new QueryWrapper<>();
        keyQueryWrapper.eq("secretId",key.getSecretid());
        Key one = getOne(keyQueryWrapper);
        Boolean b;
        key.setTaskStatus("未检测");
        if (one != null){
            one.setSecretid(key.getSecretid());
            one.setSecretkey(key.getSecretkey());
            key.setTaskStatus("");
            b = updateById(one);
        }else {
            b = save(key);
        }
        if (key.getStatus().equals("0")){
            key.setTaskStatus("检测中");
            b = updateById(key);
            Tools.executorService.submit(() -> execute(key));
        }
        return b;
    }

    public void execute(Key key){
        Type type = Type.valueOf(key.getType());
        key.setTaskStatus("检测中");
        this.updateById(key);
        KeyService keyService = this;
        switch (type){
            case AliYun:

            {
                Integer defaultValue = 3;
                try {
                    Base.getRegionInfo(key, "ecs-cn-hangzhou.aliyuncs.com");
                } catch (Exception e) {
                    key.setTaskStatus("检测失败原因：" + e.getMessage());
                    this.updateById(key);
                    return;
                }
                AtomicInteger detectProgress = new AtomicInteger(3);
                Tools.executorService.submit(() -> {
                    aliYunInstanceService.getInstanceList(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                Tools.executorService.submit(() -> {
                    aliYunInstanceService.getBucketLists(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                Tools.executorService.submit(() -> {
                    aliYunInstanceService.getRdsLists(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });

                break;
            }
            case Tencent:
            {
                Integer defaultValue = 3;
                try {
                    com.common.tencent.product.Base.getRegionList(com.common.tencent.product.Base.createCredential(key),"cvm");
                } catch (TencentCloudSDKException e) {
                    key.setTaskStatus("检测失败原因：" + e.getMessage());
                    this.updateById(key);
                    return;
                }
                AtomicInteger detectProgress = new AtomicInteger(3);
                Tools.executorService.submit(() -> {
                    tencentInstanceService.getInstanceList(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                Tools.executorService.submit(() -> {
                        bucketService.getBucketList(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                Tools.executorService.submit(() -> {
                    tencentInstanceService.getDBLists(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
            }
            case QINiu:{
                Integer defaultValue = 1;
                AtomicInteger detectProgress = new AtomicInteger(1);
                Tools.executorService.execute(() -> {
                    qiNiuService.getInstanceList(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
            }
        }
    }

    public void updateStatus(AtomicInteger detectProgress,Key key,KeyService keyService,Integer defaultValue){
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        if (detectProgress.get() <= 0) {
            key.setTaskStatus("检测完成");
            log.info("检测key{}结束",key.getSecretid());
        }
        else {
            String format = numberFormat.format((float)(3 - detectProgress.get()) / (float) defaultValue * 100);
            key.setTaskStatus(format + "%");
        }
        keyService.updateById(key);
    }

}




