package com.common.huawei;

import cn.hutool.core.util.StrUtil;
import com.domain.DatabasesInstance;
import com.domain.Key;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.iam.v3.model.AuthProjectResult;
import com.huaweicloud.sdk.rds.v3.RdsClient;
import com.huaweicloud.sdk.rds.v3.model.*;
import com.huaweicloud.sdk.rds.v3.region.RdsRegion;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RDS {

    /***
     * 获取数据库列表
     * @param key
     * @return
     */
    public static List<InstanceResponse> getRDSLists(Key key){
        List<AuthProjectResult> projectId = ECS.getProjectId(key);
        List<InstanceResponse> res = new ArrayList<>();
        for (AuthProjectResult authProjectResult : projectId) {
            ICredential baseICredential = ECS.getBaseICredential(key, authProjectResult.getId());
            if (StrUtil.contains(authProjectResult.getName(),"-")){
                String s = StrUtil.subBefore(authProjectResult.getName(), "_", false);
                Region region = null;
                try {
                    region = RdsRegion.valueOf(s);
                    if (region != null){
                        RdsClient client = RdsClient.newBuilder()
                                .withCredential(baseICredential)
                                .withRegion(region)
                                .build();
                        ListInstancesRequest request = new ListInstancesRequest();
                        ListInstancesResponse listInstancesResponse = client.listInstances(request);
                        if (!listInstancesResponse.getInstances().isEmpty()) res.addAll(listInstancesResponse.getInstances());
                    }
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return res;
    }

    /***
     * 获取数据库列表，用于赋值权限
     * @param instance
     * @param key
     * @return
     */
    private static List<DatabaseForCreation> getDBLists(DatabasesInstance instance, Key key){
        RdsClient client = RdsClient.newBuilder()
                .withCredential(ECS.getBaseICredential(key, instance.getWhitelist()))
                .withRegion(RdsRegion.valueOf(instance.getRegion()))
                .build();
        List<DatabaseForCreation> list = new ArrayList<>();
        ListDatabasesRequest request = new ListDatabasesRequest();
        request.withInstanceId(instance.getInstanceId());
        int limit = 100;
        int page = 1;
        while (true){
            request.setLimit(limit);
            request.setPage(page);
            try {
                ListDatabasesResponse response = client.listDatabases(request);
                if (response.getDatabases().size() >= 1 ){
                    list.addAll(response.getDatabases());
                    page += 1;
                }else break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return list;
    }

    public static boolean createSqlServerUser(DatabasesInstance instance, Key key, String password, String username){
        RdsClient client = RdsClient.newBuilder()
                .withCredential(ECS.getBaseICredential(key, instance.getWhitelist()))
                .withRegion(RdsRegion.valueOf(instance.getRegion()))
                .build();
        CreateSqlserverDbUserRequest request = new CreateSqlserverDbUserRequest();
        request.withInstanceId(instance.getInstanceId());
        SqlserverUserForCreation body = new SqlserverUserForCreation();
        body.withPassword(password);
        body.withName(username);
        request.withBody(body);
        try {
            client.createSqlserverDbUser(request);
            setSqlServerUserPrivilege(instance,key,username);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 授权用户权限
     * @param instance
     * @param key
     * @param username
     */
    public static void setSqlServerUserPrivilege(DatabasesInstance instance, Key key,String username){
        List<SqlserverDatabaseForDetail> sqlServerDBName = getSqlServerDBName(instance, key);
        RdsClient client = RdsClient.newBuilder()
                .withCredential(ECS.getBaseICredential(key, instance.getWhitelist()))
                .withRegion(RdsRegion.valueOf(instance.getRegion()))
                .build();
        AllowSqlserverDbUserPrivilegeRequest request = new AllowSqlserverDbUserPrivilegeRequest();
        request.withInstanceId(instance.getInstanceId());
        SqlserverGrantRequest body = new SqlserverGrantRequest();
        List<SqlserverUserWithPrivilege> listbodyUsers = new ArrayList<>();
        listbodyUsers.add(
                new SqlserverUserWithPrivilege()
                        .withName(username)
        );
        body.withUsers(listbodyUsers);
        request.withBody(body);
        for (SqlserverDatabaseForDetail sqlserverDatabaseForDetail : sqlServerDBName) {
            body.withDbName(sqlserverDatabaseForDetail.getName());
            try {
                client.allowSqlserverDbUserPrivilege(request);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    public static List<SqlserverDatabaseForDetail> getSqlServerDBName(DatabasesInstance instance, Key key){
        RdsClient client = RdsClient.newBuilder()
                .withCredential(ECS.getBaseICredential(key, instance.getWhitelist()))
                .withRegion(RdsRegion.valueOf(instance.getRegion()))
                .build();
        ListSqlserverDatabasesRequest request = new ListSqlserverDatabasesRequest();
        request.withInstanceId(instance.getInstanceId());
        int page = 1;
        int limit = 100;
        List<SqlserverDatabaseForDetail> list = new ArrayList<>();
        while (true){
            request.withPage(page);
            request.withLimit(limit);
            try {
                ListSqlserverDatabasesResponse response1 = client.listSqlserverDatabases(request);
                if (!response1.getDatabases().isEmpty()){
                    list.addAll(response1.getDatabases());
                    page += 1;
                }else break;
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return list;
    }

    public static boolean createMysqlUser(DatabasesInstance instance, Key key, String password, String username){
        RdsClient client = RdsClient.newBuilder()
                .withCredential(ECS.getBaseICredential(key, instance.getWhitelist()))
                .withRegion(RdsRegion.valueOf(instance.getRegion()))
                .build();
        List<DatabaseForCreation> dbLists = getDBLists(instance, key);
        CreateDbUserRequest request = new CreateDbUserRequest();
        UserForCreation body = new UserForCreation();
        List<String> listbodyHosts = new ArrayList<>();
        List<DatabaseWithPrivilegeObject> listbodyDatabases = new ArrayList<>();
        for (DatabaseForCreation dbList : dbLists) {
            listbodyDatabases.add(
                    new DatabaseWithPrivilegeObject()
                            .withName(dbList.getName())
                            .withReadonly(false)
            );
        }
        body.withDatabases(listbodyDatabases);
        listbodyHosts.add("%");
        body.withHosts(listbodyHosts);
        body.withPassword(password);
        body.withName(username);
        request.withBody(body)
                .withInstanceId(instance.getInstanceId());
        try {
            client.createDbUser(request);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean createPostgreSQLUser(DatabasesInstance instance, Key key, String password, String username){
        RdsClient client = RdsClient.newBuilder()
                .withCredential(ECS.getBaseICredential(key, instance.getWhitelist()))
                .withRegion(RdsRegion.valueOf(instance.getRegion()))
                .build();
        List<PostgresqlListDatabase> dbNameLists = getDBNameLists(instance, key);
        CreatePostgresqlDbUserRequest request = new CreatePostgresqlDbUserRequest();
        request.setInstanceId(instance.getInstanceId());
        PostgresqlUserForCreation body = new PostgresqlUserForCreation();
        body.withName(username);
        body.withPassword(password);
        request.withBody(body);
        try {
            client.createPostgresqlDbUser(request);
            return setUserPrivilege(instance, key, username);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean setUserPrivilege(DatabasesInstance instance, Key key, String username){
        RdsClient client = RdsClient.newBuilder()
                .withCredential(ECS.getBaseICredential(key, instance.getWhitelist()))
                .withRegion(RdsRegion.valueOf("ap-southeast-1"))
                .build();
        SetDatabaseUserPrivilegeRequest request = new SetDatabaseUserPrivilegeRequest();
        request.withInstanceId(instance.getInstanceId());
        SetDatabaseUserPrivilegeReqV3 body = new SetDatabaseUserPrivilegeReqV3();
        body.withReadonly(false);
        body.withUserName(username);
        body.withAllUsers(false);
        request.withBody(body);
        try {
            client.setDatabaseUserPrivilege(request);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static List<PostgresqlListDatabase> getDBNameLists(DatabasesInstance instance, Key key){
        RdsClient client = RdsClient.newBuilder()
                .withCredential(ECS.getBaseICredential(key, instance.getWhitelist()))
                .withRegion(RdsRegion.valueOf(instance.getRegion()))
                .build();
        ListPostgresqlDatabasesRequest request = new ListPostgresqlDatabasesRequest();
        request.withInstanceId(instance.getInstanceId());
        List<PostgresqlListDatabase> list = new ArrayList<>();
        int page = 1;
        int limit = 100;
        while (true){
            request.withPage(page);
            request.withLimit(limit);
            try {
                ListPostgresqlDatabasesResponse response = client.listPostgresqlDatabases(request);
                if (!response.getDatabases().isEmpty()) {
                    list.addAll(response.getDatabases());
                    page += 1;
                }else break;
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return list;
    }

    private static List<Region> getRegionLists(){
        List<Region> regions = new ArrayList<>();
        try {
            Field[] fields = RdsRegion.class.getDeclaredFields();
            for (Field field : fields) {
                if (Region.class.isAssignableFrom(field.getType())) {
                    regions.add((Region)field.get(null));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return regions;
    }

}
