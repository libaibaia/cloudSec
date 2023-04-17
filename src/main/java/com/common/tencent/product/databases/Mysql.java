package com.common.tencent.product.databases;

import com.common.tencent.product.Base;
import com.domain.Key;
import com.tencentcloudapi.cdb.v20170320.CdbClient;
import com.tencentcloudapi.cdb.v20170320.models.*;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.region.v20220627.models.RegionInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mysql {
    private static final HttpProfile httpProfile = new HttpProfile();
    private static final ClientProfile clientProfile = new ClientProfile();

    private static final String[] GlobalPrivileges = new String[]{
            "SELECT","INSERT","UPDATE","DELETE","CREATE", "PROCESS", "DROP","REFERENCES",
            "INDEX","ALTER","SHOW DATABASES","CREATE TEMPORARY TABLES","LOCK TABLES",
            "EXECUTE","CREATE VIEW","SHOW VIEW","CREATE ROUTINE","ALTER ROUTINE","EVENT",
            "TRIGGER","CREATE USER","RELOAD","REPLICATION CLIENT","REPLICATION SLAVE"
    };
    private static final String[] DatabasePrivileges = new String[]{
            "SELECT","INSERT","UPDATE","DELETE","CREATE", "DROP","REFERENCES","INDEX",
            "ALTER","CREATE TEMPORARY TABLES","LOCK TABLES","EXECUTE","CREATE VIEW","CREATE ROUTINE","ALTER ROUTINE","EVENT","TRIGGER"
    };
    private static final String[] TablePrivileges = new String[]{
            "SELECT","INSERT","UPDATE","DELETE","CREATE", "DROP","REFERENCES","INDEX","ALTER","CREATE VIEW","SHOW VIEW", "TRIGGER"
    };

    /**
     * 遍历可用区域，获取实例信息
     * @return 实例列表
     */
    public List<InstanceInfo> getMysqlLists(Key key){
        List<InstanceInfo> res = new ArrayList<>();
        try{
            httpProfile.setEndpoint("cdb.tencentcloudapi.com");
            clientProfile.setHttpProfile(httpProfile);
            Credential credential = Base.createCredential(key);
            RegionInfo[] cdbs = Base.getRegionList(credential, "cdb");
            CdbClient client = new CdbClient(credential, "", clientProfile);
            DescribeDBInstancesRequest req = new DescribeDBInstancesRequest();
            for (RegionInfo cdb : cdbs) {
                client.setRegion(cdb.getRegion());
                DescribeDBInstancesResponse resp = client.DescribeDBInstances(req);
                if (resp.getItems().length >= 1){
                    res.addAll(Arrays.asList(resp.getItems()));
                }
            }
        } catch (TencentCloudSDKException e) {
            System.out.println(e);
        }
        return res;
    }
    //创建用户
    public static void createMysqlUser(Key key,String region,String instanceId,String userName,String password) throws TencentCloudSDKException {
        httpProfile.setEndpoint("cdb.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        Credential credential = Base.createCredential(key);
        CdbClient client = new CdbClient(credential, "", clientProfile);
        client.setRegion(region);
        CreateAccountsRequest req = new CreateAccountsRequest();
        req.setInstanceId(instanceId);
        Account account = new Account();
        account.setUser(userName);
        account.setHost("%");
        req.setAccounts(new Account[]{account});
        req.setPassword(password);
        CreateAccountsResponse resp = client.CreateAccounts(req);
        //创建后修改权限
        updatePerm(credential,account,instanceId,region);
    }

    //获取权限
    private static DescribeAccountPrivilegesResponse getPerList(Credential credential) throws TencentCloudSDKException {
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("cdb.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        CdbClient client = new CdbClient(credential, "", clientProfile);
        DescribeAccountPrivilegesRequest req = new DescribeAccountPrivilegesRequest();
        return client.DescribeAccountPrivileges(req);
    }

    //修改权限
    private static ModifyAccountPrivilegesResponse updatePerm(Credential credential,Account user,String instanceId,String region) throws TencentCloudSDKException {
        httpProfile.setEndpoint("cdb.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        CdbClient client = new CdbClient(credential, "", clientProfile);
        client.setRegion(region);
        ModifyAccountPrivilegesRequest req = new ModifyAccountPrivilegesRequest();
        req.setGlobalPrivileges(GlobalPrivileges);
        req.setInstanceId(instanceId);
        req.setAccounts(new Account[]{user});
        return client.ModifyAccountPrivileges(req);
    }

    /**
     * 开启外网访问
     * @param key
     * @param region
     * @param instanceId
     * @throws TencentCloudSDKException
     */
    public static void openWan(Key key,String region,String instanceId) throws TencentCloudSDKException {
        httpProfile.setEndpoint("cdb.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        CdbClient client = new CdbClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        OpenWanServiceRequest req = new OpenWanServiceRequest();
        req.setInstanceId(instanceId);
        OpenWanServiceResponse resp = client.OpenWanService(req);
        System.out.println(OpenWanServiceResponse.toJsonString(resp));
    }

    /**
     * 查看指定数据库信息，主要为了更新外网访问后更新外网端口及IP
     *
     * @param key
     * @param region
     * @param instanceId
     * @return
     */
    public static InstanceInfo[] getMysql(Key key, String region, String instanceId){
        try{
            httpProfile.setEndpoint("cdb.tencentcloudapi.com");
            clientProfile.setHttpProfile(httpProfile);
            Credential credential = Base.createCredential(key);
            CdbClient client = new CdbClient(credential, "", clientProfile);
            client.setRegion(region);
            DescribeDBInstancesRequest req = new DescribeDBInstancesRequest();
            req.setInstanceIds(new String[]{instanceId});
            DescribeDBInstancesResponse resp = client.DescribeDBInstances(req);
            return resp.getItems();
        } catch (TencentCloudSDKException e) {
            System.out.println(e);
        }
        return new InstanceInfo[0];
    }

    /**
     * 关闭外网访问
     * @param key
     * @param region
     * @param instanceId
     * @throws TencentCloudSDKException
     */
    public static void closeWan(Key key,String region,String instanceId) throws TencentCloudSDKException {
        httpProfile.setEndpoint("cdb.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        CdbClient client = new CdbClient(Base.createCredential(key), "", clientProfile);
        client.setRegion(region);
        CloseWanServiceRequest req = new CloseWanServiceRequest();
        req.setInstanceId(instanceId);
        client.CloseWanService(req);
    }

}
