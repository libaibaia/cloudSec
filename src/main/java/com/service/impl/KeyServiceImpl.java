package com.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aliyun.ecs20140526.models.DescribeRegionsResponseBody;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.LogAnnotation;
import com.common.Tools;
import com.common.Type;
import com.common.aliyun.Base;
import com.common.huawei.OBS;
import com.domain.Key;
import com.service.KeyService;
import com.mapper.KeyMapper;
import com.service.impl.aliyun.AliYunInstanceService;
import com.service.impl.huawei.HaWeiService;
import com.service.impl.qiniu.QiNiuService;
import com.service.impl.tencent.TencentInstanceService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
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
    @Autowired
    private ExecutorService executorService;

    @Resource
    @Lazy
    private QiNiuService qiNiuService;
    @Resource
    private HaWeiService haWeiService;
    @Resource
    @Lazy
    private BucketServiceImpl bucketService;
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
            one.setToken(StrUtil.isBlank(key.getToken()) ? "" : key.getToken());
            one.setTaskStatus("");
            b = updateById(one);
            if (key.getStatus().equals("0")){
                one.setTaskStatus("检测中");
                b = updateById(one);
                executorService.submit(() -> execute(one));
            }
        }else {
            if (key.getStatus().equals("0")){
                key.setTaskStatus("检测中");
                executorService.submit(() -> execute(key));
            }
            b = save(key);
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

                executorService.submit(() -> {
                    aliYunInstanceService.getInstanceList(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                executorService.submit(() -> {
                    aliYunInstanceService.getBucketLists(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                executorService.submit(() -> {
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
                executorService.submit(() -> {
                    tencentInstanceService.getInstanceList(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                executorService.submit(() -> {
                        bucketService.getBucketList(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                executorService.submit(() -> {
                    tencentInstanceService.getDBLists(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                break;
            }
            case QINiu:{
                Integer defaultValue = 2;
                AtomicInteger detectProgress = new AtomicInteger(defaultValue);
                executorService.execute(() -> {
                    qiNiuService.getInstanceList(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                executorService.execute(() -> {
                    qiNiuService.getBucketList(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                break;
            }
            case HUAWEI:
            {
                Integer defaultValue = 1;
                AtomicInteger detectProgress = new AtomicInteger(defaultValue);
                executorService.execute(() -> {
                    haWeiService.getBucketLists(key,detectProgress);
                    updateStatus(detectProgress,key,keyService,defaultValue);
                });
                break;
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




