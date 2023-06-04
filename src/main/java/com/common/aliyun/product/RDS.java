package com.common.aliyun.product;

import com.aliyun.rds20140815.Client;
import com.aliyun.rds20140815.models.*;
import com.domain.DatabasesInstance;
import com.domain.Key;

import java.util.*;

public class RDS {
    public static String privateType = "Private";
    public static String publicType = "Public";
    private static final com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions().setReadTimeout(50000)
            .setConnectTimeout(50000);

    public static List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> getRDSLists(Key key) throws Exception {
        com.aliyun.rds20140815.Client client = getRdsClient(key);
        com.aliyun.rds20140815.models.DescribeDBInstancesRequest request = new com.aliyun.rds20140815.models.DescribeDBInstancesRequest();
        Map<String, DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRDSRegion> rdsRegion = getRdsRegion(key);
        java.util.List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> dbInstances = new ArrayList<>();
        request.setPageSize(100);

        for (String s : rdsRegion.keySet()) {
            int currentPage = 1;
            while (true){
                request.setPageNumber(currentPage);
                request.setRegionId(rdsRegion.get(s).regionId);
                DescribeDBInstancesResponse describeDBInstancesResponse = client.describeDBInstancesWithOptions(request, runtime);
                if (describeDBInstancesResponse.body.items.DBInstance.size() >= 1){
                    dbInstances.addAll(describeDBInstancesResponse.body.items.DBInstance);
                    currentPage += 1;
                }else break;
            }
        }
        return dbInstances;
    }
    private static Client getRdsClient(Key key) throws Exception {
        com.aliyun.teaopenapi.models.Config config = null;
        if (key.getToken() != null){
            config = new com.aliyun.teaopenapi.models.Config()
                    .setAccessKeyId(key.getSecretid())
                    .setAccessKeySecret(key.getSecretkey()).setSecurityToken(key.getToken());
        } else {
            config = new com.aliyun.teaopenapi.models.Config()
                    .setAccessKeyId(key.getSecretid())
                    .setAccessKeySecret(key.getSecretkey());
        }
        config.endpoint = "rds.aliyuncs.com";
        return new Client(config);
    }

    /**
     * 获取rds 可用region,期间需要去重，因为阿里云返回数据包含重复区域id，以map返回，方便后期使用
     * @param key
     * @return
     * @throws Exception
     */
    private static Map<String,DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRDSRegion> getRdsRegion(Key key) throws Exception {
        Client rdsClient = getRdsClient(key);
        Map<String,DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRDSRegion> map = new HashMap<>();
        com.aliyun.rds20140815.models.DescribeRegionsRequest request = new com.aliyun.rds20140815.models.DescribeRegionsRequest();
        DescribeRegionsResponse describeRegionsResponse = rdsClient.describeRegionsWithOptions(request, runtime);
        for (DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRDSRegion regionsRDSRegion : describeRegionsResponse.body.regions.RDSRegion) {
            if (!map.containsKey(regionsRDSRegion.regionId)){
                map.put(regionsRDSRegion.regionId,regionsRDSRegion);
            }
        }
        return map;
    }

    /**
     * 打开外网访问
     * @param key
     * @param instanceID
     * @throws Exception
     */
    public static Map<String, String> openWan(Key key, DatabasesInstance instanceID) throws Exception {
        com.aliyun.rds20140815.Client client = getRdsClient(key);
        Random random = new Random();
        String port= instanceID.getPort();
        //端口同下
        if (port == null || port.equals("")){
            port = "3306";
        }
        //如果数据库中不存在域名，则说明默认外网域名不存在，此处创建，否则使用目标默认域名，保证目标域名信息不被破坏
        if (instanceID.getDomain() == null || instanceID.getDomain().equals("")){
            instanceID.setDomain("test" + random.nextInt(100));
        }
        com.aliyun.rds20140815.models.AllocateInstancePublicConnectionRequest request = new com.aliyun.rds20140815.models.AllocateInstancePublicConnectionRequest()
                .setDBInstanceId(instanceID.getInstanceId())
                .setPort(port)
                .setConnectionStringPrefix(instanceID.getDomain().split("\\.")[0]);
        AllocateInstancePublicConnectionResponse allocateInstancePublicConnectionResponse = client.allocateInstancePublicConnectionWithOptions(request, runtime);
        String domain = allocateInstancePublicConnectionResponse.body.connectionString;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("domain",domain);
        hashMap.put("port",port);
        return hashMap;
    }

    /**
     * 关闭外网访问
     * @param key
     * @throws Exception
     */
    public static void closeWan(Key key, DatabasesInstance databasesInstance) throws Exception {
        com.aliyun.rds20140815.Client client = getRdsClient(key);
        com.aliyun.rds20140815.models.ReleaseInstancePublicConnectionRequest request = new com.aliyun.rds20140815.models.ReleaseInstancePublicConnectionRequest();
        request.setDBInstanceId(databasesInstance.getInstanceId());
        request.setCurrentConnectionString(databasesInstance.getDomain());
        client.releaseInstancePublicConnectionWithOptions(request, runtime);
    }

    /**
     * 查看实例网络链接详情
     * @param key
     * @param instanceID
     * @return
     * @throws Exception
     */
    public static DescribeDBInstanceNetInfoResponse descInstanceNetType(Key key, String instanceID) throws Exception {
        com.aliyun.rds20140815.Client client = getRdsClient(key);
        com.aliyun.rds20140815.models.DescribeDBInstanceNetInfoRequest request =
                new com.aliyun.rds20140815.models.DescribeDBInstanceNetInfoRequest();
        request.setDBInstanceId(instanceID);
        return client.describeDBInstanceNetInfoWithOptions(request, runtime);
    }

    public static void createRDSUser(Key key,DatabasesInstance databasesInstance,String username,String password) throws Exception {
        Client rdsClient = getRdsClient(key);
        com.aliyun.rds20140815.models.CreateAccountRequest request =
                new com.aliyun.rds20140815.models.CreateAccountRequest();
        request.setDBInstanceId(databasesInstance.getInstanceId());
        request.setAccountName(username);
        request.setAccountType("Super");
        request.setAccountPassword(password);
        try {
            rdsClient.createAccountWithOptions(request, runtime);
        } catch (Exception e) {
            request.setAccountType("Normal");
            rdsClient.createAccountWithOptions(request, runtime);
        }
    }

    //获取白名单，存放在数据库，主要保证开启外网创建了数据库后能还原
    public static List<DescribeDBInstanceIPArrayListResponseBody.DescribeDBInstanceIPArrayListResponseBodyItemsDBInstanceIPArray> getRDSWhitelist(Key key, DatabasesInstance databasesInstance) throws Exception {
        com.aliyun.rds20140815.models.DescribeDBInstanceIPArrayListRequest request =
                new com.aliyun.rds20140815.models.DescribeDBInstanceIPArrayListRequest();
        Client rdsClient = getRdsClient(key);
        request.setDBInstanceId(databasesInstance.getInstanceId());
        DescribeDBInstanceIPArrayListResponse response = rdsClient.describeDBInstanceIPArrayListWithOptions(request, runtime);
        return response.body.items.DBInstanceIPArray;
    }

    public static void ModifyWhitelist(Key key,DatabasesInstance databasesInstance,Boolean isClose) throws Exception {
        Client rdsClient = getRdsClient(key);
        com.aliyun.rds20140815.models.ModifySecurityIpsRequest request = new com.aliyun.rds20140815.models.ModifySecurityIpsRequest();
        request.setDBInstanceId(databasesInstance.getInstanceId());
        //设置白名单，所有地址可访问
        if (!isClose){
            request.setSecurityIps(databasesInstance.getWhitelist() + ",0.0.0.0/0");
        }else {
            request.setSecurityIps(databasesInstance.getWhitelist());
        }
        rdsClient.modifySecurityIpsWithOptions(request, runtime);
    }

}
