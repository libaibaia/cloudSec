package com.common.tencent.product.databases;

import com.common.tencent.product.Base;
import com.domain.Key;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.mongodb.v20190725.MongodbClient;
import com.tencentcloudapi.mongodb.v20190725.models.*;
import com.tencentcloudapi.region.v20220627.models.RegionInfo;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoDb {
    private static HttpProfile httpProfile = new HttpProfile();
    private static ClientProfile clientProfile = new ClientProfile();
    public static List<InstanceDetail> getDBList(Key key) throws TencentCloudSDKException {
        httpProfile.setEndpoint("mongodb.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        Credential credential = Base.createCredential(key);
        RegionInfo[] mongodbs = Base.getRegionList(credential, "mongodb");
        MongodbClient client = new MongodbClient(Base.createCredential(key), "", clientProfile);
        DescribeDBInstancesRequest req = new DescribeDBInstancesRequest();
        List<InstanceDetail> list = new ArrayList<>();
        for (RegionInfo mongodb : mongodbs) {
            client.setRegion(mongodb.getRegion());
            DescribeDBInstancesResponse response = client.DescribeDBInstances(req);
            if (response.getTotalCount() >= 1){
                list.addAll(Arrays.asList(response.getInstanceDetails()));
            }
        }

        return list;
    }

}
