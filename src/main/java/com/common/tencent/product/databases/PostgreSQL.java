package com.common.tencent.product.databases;

import com.common.tencent.product.Base;
import com.domain.Key;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.postgres.v20170312.PostgresClient;
import com.tencentcloudapi.postgres.v20170312.models.*;
import com.tencentcloudapi.region.v20220627.models.RegionInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostgreSQL {
    private static final HttpProfile httpProfile = new HttpProfile();
    private static final ClientProfile clientProfile = new ClientProfile();

    /**
     * 遍历区域中实例
     * @return
     * @throws TencentCloudSDKException
     */
    public static List<DBInstance> getPostgreSQLList(Key key){
        Credential credential = Base.createCredential(key);
        List<DBInstance> dbInstances = new ArrayList<>();
        httpProfile.setEndpoint("postgres.tencentcloudapi.com");
        RegionInfo[] postgresRegionLists = new RegionInfo[0];
        try {
            postgresRegionLists = Base.getRegionList(credential, "postgres");
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
        clientProfile.setHttpProfile(httpProfile);
        DescribeDBInstancesRequest req = new DescribeDBInstancesRequest();
        PostgresClient client = new PostgresClient(credential, "", clientProfile);
        for (RegionInfo postgresRegionList : postgresRegionLists) {
            client.setRegion(postgresRegionList.getRegion());
            DescribeDBInstancesResponse resp = null;
            try {
                resp = client.DescribeDBInstances(req);
            } catch (TencentCloudSDKException e) {
                continue;
            }
            if (resp.getTotalCount() >= 1){
                dbInstances.addAll(Arrays.asList(resp.getDBInstanceSet()));
            }
        }
        return dbInstances;
    }

    public static void openWan(Key key,String instanceId,String region) throws TencentCloudSDKException {
        httpProfile.setEndpoint("postgres.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        PostgresClient client = new PostgresClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        OpenDBExtranetAccessRequest req = new OpenDBExtranetAccessRequest();
        req.setDBInstanceId(instanceId);
        client.OpenDBExtranetAccess(req);
    }
    public static DBInstance[] getDBInstanceInfo(Key key, String instanceId, String region) throws TencentCloudSDKException {
        Credential credential = Base.createCredential(key);
        httpProfile.setEndpoint("postgres.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        DescribeDBInstancesRequest req = new DescribeDBInstancesRequest();
        Filter filter = new Filter();
        filter.setName("db-instance-id");
        filter.setValues(new String[]{instanceId});
        req.setFilters(new Filter[]{filter});
        PostgresClient client = new PostgresClient(credential, "", clientProfile);
        client.setRegion(region);
        DescribeDBInstancesResponse response = client.DescribeDBInstances(req);
        return response.getDBInstanceSet();
    }

    public static void closeWan(Key key,String region,String instanceId) throws TencentCloudSDKException {
        httpProfile.setEndpoint("postgres.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        PostgresClient client = new PostgresClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        CloseDBExtranetAccessRequest req = new CloseDBExtranetAccessRequest();
        req.setDBInstanceId(instanceId);
        client.CloseDBExtranetAccess(req);
    }

    public static ArrayList<AccountInfo> getUserLists(Key key, String region, String instanceID) throws TencentCloudSDKException {
        httpProfile.setEndpoint("postgres.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        PostgresClient client = new PostgresClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        DescribeAccountsRequest req = new DescribeAccountsRequest();
        req.setDBInstanceId(instanceID);
        DescribeAccountsResponse resp = client.DescribeAccounts(req);
        if (resp.getTotalCount() >= 1){
            return new ArrayList<>(Arrays.asList(resp.getDetails()));
        }
        return null;
    }

    public static void updatePassword(Key key,String instanceID,String region,String username,String password) throws TencentCloudSDKException {
        httpProfile.setEndpoint("postgres.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        PostgresClient client = new PostgresClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        ResetAccountPasswordRequest req = new ResetAccountPasswordRequest();
        req.setUserName(username);
        req.setDBInstanceId(instanceID);
        req.setPassword(password);
        ResetAccountPasswordResponse resp = client.ResetAccountPassword(req);
    }
}
