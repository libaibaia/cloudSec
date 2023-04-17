package com.common.tencent.product.databases;

import com.common.tencent.product.Base;
import com.domain.Key;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.mariadb.v20170312.MariadbClient;
import com.tencentcloudapi.mariadb.v20170312.models.*;
import com.tencentcloudapi.region.v20220627.models.RegionInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MariaDB {

    private static final HttpProfile httpProfile = new HttpProfile();
    private static final ClientProfile clientProfile = new ClientProfile();
    private static final String[] perList = new String[]{
            "SELECT","INSERT","UPDATE","DELETE","CREATE","DROP",
            "REFERENCES","INDEX","ALTER","CREATE TEMPORARY TABLES",
            "LOCK TABLES","EXECUTE","CREATE VIEW","SHOW VIEW",
            "CREATE ROUTINE","ALTER ROUTINE","EVENT","TRIGGER",
            "SHOW DATABASES","REPLICATION CLIENT","REPLICATION SLAVE"
    };

    /**
     * 获取mariadb列表
     */
    public static List<DBInstance> getMariaDBLists(Key key) throws TencentCloudSDKException {
        List<DBInstance> dbInstances = new ArrayList<>();
        httpProfile.setEndpoint("mariadb.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        Credential credential = Base.createCredential(key);
        RegionInfo[] regionLists = Base.getRegionList(credential, "mariadb");
        MariadbClient client = new MariadbClient(credential, "", clientProfile);
        DescribeDBInstancesRequest req = new DescribeDBInstancesRequest();
        for (RegionInfo regionList : regionLists) {
            client.setRegion(regionList.getRegion());
            DescribeDBInstancesResponse resp = client.DescribeDBInstances(req);
            if (resp.getTotalCount() >= 1){
                dbInstances.addAll(Arrays.asList(resp.getInstances()));
            }
        }
        return dbInstances;
    }

    /*
    关闭外网访问
     */
    public static void closeWan(Key key,String instanceId,String region) throws TencentCloudSDKException {
        httpProfile.setEndpoint("mariadb.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        MariadbClient client = new MariadbClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        CloseDBExtranetAccessRequest req = new CloseDBExtranetAccessRequest();
        req.setInstanceId(instanceId);
        client.CloseDBExtranetAccess(req);
    }

    /**
     * 打开外网访问
     */
    public static void openWan(Key key,String region,String instanceId) throws TencentCloudSDKException {
        httpProfile.setEndpoint("mariadb.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        MariadbClient client = new MariadbClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        OpenDBExtranetAccessRequest req = new OpenDBExtranetAccessRequest();
        req.setInstanceId(instanceId);
        OpenDBExtranetAccessResponse resp = client.OpenDBExtranetAccess(req);
        System.out.println(OpenDBExtranetAccessResponse.toJsonString(resp));
    }

    public static void createUser(Key key,String instanceId,String region,String userName,String password) throws TencentCloudSDKException {
        httpProfile.setEndpoint("mariadb.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        MariadbClient client = new MariadbClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        CreateAccountRequest req = new CreateAccountRequest();
        req.setInstanceId(instanceId);
        req.setHost("%");
        req.setUserName(userName);
        req.setPassword(password);
        client.CreateAccount(req);
        updatePer(key,instanceId,region,userName);
    }
    private static void updatePer(Key key,String instanceId,String region,String userName) throws TencentCloudSDKException {
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("mariadb.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        MariadbClient client = new MariadbClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        GrantAccountPrivilegesRequest req = new GrantAccountPrivilegesRequest();
        req.setHost("%");
        req.setInstanceId(instanceId);
        req.setDbName("*");
        req.setUserName(userName);
        req.setPrivileges(perList);
        GrantAccountPrivilegesResponse grantAccountPrivilegesResponse = client.GrantAccountPrivileges(req);
        System.out.println(grantAccountPrivilegesResponse);
    }

    public static DBInstance[] getDBInstanceInfo(Key key, String instanceId, String region) throws TencentCloudSDKException {
        httpProfile.setEndpoint("mariadb.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        Credential credential = Base.createCredential(key);
        MariadbClient client = new MariadbClient(credential, "", clientProfile);
        client.setRegion(region);
        DescribeDBInstancesRequest req = new DescribeDBInstancesRequest();
        req.setInstanceIds(new String[]{instanceId});
        DescribeDBInstancesResponse resp = client.DescribeDBInstances(req);
        return resp.getInstances();
    }


}