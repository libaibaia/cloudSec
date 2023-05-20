package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.LogAnnotation;
import com.common.Tools;
import com.common.Type;
import com.common.aliyun.product.OSS;
import com.common.tencent.product.COS;
import com.domain.Bucket;
import com.domain.Key;
import com.domain.Task;
import com.service.BucketService;
import com.mapper.BucketMapper;
import com.service.TaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
* @author Administrator
* @description 针对表【bucket】的数据库操作Service实现
* @createDate 2023-04-16 01:14:54
*/
@Service
public class BucketServiceImpl extends ServiceImpl<BucketMapper, Bucket>
    implements BucketService{
    @Resource
    private BucketMapper bucketMapper;
    @Resource
    private TaskService taskService;

    @LogAnnotation(title = "获取存储桶")
    public void getBucketList(Key key, AtomicInteger status){
        COS cos = new COS();
        QueryWrapper<Bucket> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("key_id",key.getId());
        bucketMapper.delete(queryWrapper);
        List<com.qcloud.cos.model.Bucket> cosList = cos.getCosList(key);
        for (com.qcloud.cos.model.Bucket bucket : cosList) {
            Bucket bucket1 = new Bucket();
            bucket1.setEndPoint(bucket.getBucketType() + "." + bucket.getLocation() + "." +"myqcloud.com");
            bucket1.setName(bucket.getName());
            bucket1.setCreateById(key.getCreateById());
            bucket1.setOwner(bucket.getOwner().toString());
            bucket1.setKeyId(key.getId());
            bucket1.setRegion(bucket.getLocation());
            bucketMapper.insert(bucket1);
        }
        status.decrementAndGet();
    }

    public void downloadAllFile(Key key, Bucket bucket){
        Task task = new Task();
        task.setUserId(key.getCreateById());
        task.setStatus("创建中");
        task.setBucket(bucket.getName() + "." + bucket.getEndPoint());
        taskService.save(task);
        Tools.executorService.submit(() -> {
            Type type = Type.valueOf(key.getType());
            Task task1 = null;
            switch (type){
                case Tencent:
                    task1 = COS.downloadAllFile(key,bucket,task);
                    break;
                case AliYun:
                    task1 = OSS.downloadALLFile(key, bucket, task);
                    break;
                case QINiu:
                    task1 = com.common.qiniu.Bucket.downAllFile(key, bucket, task);
                    break;
            }
            taskService.updateById(task1);
        });
    }

    public List<Bucket> getCosList(Integer loginId){
        QueryWrapper<Bucket> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_by_id",loginId);
        return bucketMapper.selectList(queryWrapper);
    }
    public void removeByKeyId(Integer id){
        QueryWrapper<Bucket> bucketQueryWrapper = new QueryWrapper<>();
        bucketQueryWrapper.eq("key_id",id);
        remove(bucketQueryWrapper);
    }
}




