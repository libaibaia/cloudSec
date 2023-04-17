package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.tencent.product.COS;
import com.domain.Bucket;
import com.domain.Key;
import com.service.BucketService;
import com.mapper.BucketMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public void getBucketList(Key key){
        COS cos = new COS();
        QueryWrapper<Bucket> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("key_id",key.getId());
        bucketMapper.delete(queryWrapper);
        List<com.qcloud.cos.model.Bucket> cosList = cos.getCosList(key);
        for (com.qcloud.cos.model.Bucket bucket : cosList) {
            Bucket bucket1 = new Bucket();
            bucket1.setEndPoint(String.format(COS.cosEndPoint, bucket.getName(),bucket.getLocation()));
            bucket1.setName(bucket.getName());
            bucket1.setCreateById(key.getCreateById());
            bucket1.setOwner(bucket.getOwner().toString());
            bucket1.setKeyId(key.getId());
            bucket1.setRegion(bucket.getLocation());
            bucketMapper.insert(bucket1);
        }
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




