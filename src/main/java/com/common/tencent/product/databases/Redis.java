package com.common.tencent.product.databases;

import com.common.tencent.product.Base;
import com.domain.Key;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.redis.v20180412.RedisClient;
import com.tencentcloudapi.redis.v20180412.models.*;
import com.tencentcloudapi.region.v20220627.models.RegionInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Redis {
    private static final HttpProfile httpProfile = new HttpProfile();
    private static final ClientProfile clientProfile = new ClientProfile();
    public static List<InstanceSet> getRedisLists(Key key) throws TencentCloudSDKException {
        Credential credential = Base.createCredential(key);
        RegionInfo[] redis = Base.getRegionList(credential, "redis");
        httpProfile.setEndpoint("redis.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        RedisClient client = new RedisClient(credential, "", clientProfile);
        DescribeInstancesRequest req = new DescribeInstancesRequest();
        List<InstanceSet> instanceSets = new ArrayList<>();
        for (RegionInfo redi : redis) {
            client.setRegion(redi.getRegion());
            DescribeInstancesResponse resp = client.DescribeInstances(req);
            if (resp.getTotalCount() >= 1){
                instanceSets.addAll(Arrays.asList(resp.getInstanceSet()));
            }
        }

        return instanceSets;
    }
    public static void openWan(Key key,String instanceID,String region) throws TencentCloudSDKException {
        httpProfile.setEndpoint("redis.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        RedisClient client = new RedisClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        AllocateWanAddressRequest req = new AllocateWanAddressRequest();
        req.setInstanceId(instanceID);
        AllocateWanAddressResponse resp = client.AllocateWanAddress(req);
    }
    public static void closeWan(Key key,String instanceId,String region) throws TencentCloudSDKException {
        httpProfile.setEndpoint("redis.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        RedisClient client = new RedisClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        ReleaseWanAddressRequest req = new ReleaseWanAddressRequest();
        req.setInstanceId(instanceId);
        client.ReleaseWanAddress(req);
    }
    public static void createUser(Key key,String instanceID,String region,String username,String password) throws TencentCloudSDKException {
        httpProfile.setEndpoint("redis.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        RedisClient client = new RedisClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        CreateInstanceAccountRequest req = new CreateInstanceAccountRequest();
        req.setAccountName(username);
        req.setReadonlyPolicy(new String[]{"master"});
        req.setPrivilege("rw");
        req.setInstanceId(instanceID);
        req.setAccountPassword(password);
        client.CreateInstanceAccount(req);
    }

    public static InstanceSet[] getDBInstanceInfo(Key key, String instanceID, String region) throws TencentCloudSDKException {
        Credential credential = Base.createCredential(key);
        httpProfile.setEndpoint("redis.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        RedisClient client = new RedisClient(credential, "", clientProfile);
        client.setRegion(region);
        DescribeInstancesRequest req = new DescribeInstancesRequest();
        req.setInstanceId(instanceID);
        List<InstanceSet> instanceSets = new ArrayList<>();
        DescribeInstancesResponse resp = client.DescribeInstances(req);

        return resp.getInstanceSet();
    }

}
