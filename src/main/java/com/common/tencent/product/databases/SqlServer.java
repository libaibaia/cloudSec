package com.common.tencent.product.databases;

import com.common.tencent.product.Base;
import com.domain.Key;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.region.v20220627.models.RegionInfo;
import com.tencentcloudapi.sqlserver.v20180328.SqlserverClient;
import com.tencentcloudapi.sqlserver.v20180328.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlServer {
    private static final HttpProfile httpProfile = new HttpProfile();
    private static final ClientProfile clientProfile = new ClientProfile();
    public static List<DBInstance> getDBLists(Key key) throws TencentCloudSDKException {
        List<DBInstance> list = new ArrayList<>();
        httpProfile.setEndpoint("sqlserver.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        Credential credential = Base.createCredential(key);
        RegionInfo[] sqlservers = Base.getRegionList(credential, "sqlserver");
        DescribeDBInstancesRequest req = new DescribeDBInstancesRequest();
        SqlserverClient client = new SqlserverClient(credential, "", clientProfile);
        for (RegionInfo sqlserver : sqlservers) {
            client.setRegion(sqlserver.getRegion());
            DescribeDBInstancesResponse resp = client.DescribeDBInstances(req);
            if (resp.getTotalCount() >= 1){
                list.addAll(Arrays.asList(resp.getDBInstances()));
            }
        }
        return list;
    }

    public static void createUser(Key key, String username, String password, String instanceID, String region) throws TencentCloudSDKException {
        httpProfile.setEndpoint("sqlserver.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        SqlserverClient client = new SqlserverClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        CreateAccountRequest req = new CreateAccountRequest();
        AccountCreateInfo accountCreateInfo = new AccountCreateInfo();
        accountCreateInfo.setUserName(username);
        accountCreateInfo.setPassword(password);
        accountCreateInfo.setIsAdmin(true);
        req.setAccounts(new AccountCreateInfo[]{accountCreateInfo});
        req.setInstanceId(instanceID);
        client.CreateAccount(req);
    }
    
}
