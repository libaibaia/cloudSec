package com.common.huawei;

import cn.hutool.core.util.StrUtil;
import com.domain.Instance;
import com.domain.Key;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.model.*;
import com.huaweicloud.sdk.ecs.v2.region.EcsRegion;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.AuthProjectResult;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListAuthProjectsRequest;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListAuthProjectsResponse;
import com.huaweicloud.sdk.iam.v3.region.IamRegion;
import com.huaweicloud.sdk.rds.v3.model.DatabaseForCreation;
import com.huaweicloud.sdk.rds.v3.region.RdsRegion;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ECS {
    private static ICredential getICredential(Key key){
        ICredential auth;
        if (!StrUtil.isBlank(key.getToken())){
            auth = new GlobalCredentials()
                    .withAk(key.getSecretid())
                    .withSk(key.getSecretkey())
                    .withSecurityToken(key.getToken());
        }else {
            auth = new GlobalCredentials()
                    .withAk(key.getSecretid())
                    .withSk(key.getSecretkey());
        }
        return auth;
    }

    public static ICredential getBaseICredential(Key key, String projectId){
        ICredential auth;
        if (!StrUtil.isBlank(key.getToken())){
            auth = new BasicCredentials()
                    .withAk(key.getSecretid())
                    .withSk(key.getSecretkey())
                    .withProjectId(projectId)
                    .withSecurityToken(key.getToken());
        }else {
            auth = new BasicCredentials()
                    .withAk(key.getSecretid())
                    .withSk(key.getSecretkey())
                    .withProjectId(projectId);
        }
        return auth;
    }
    public static List<AuthProjectResult> getProjectId(Key key){
        IamClient client = IamClient.newBuilder()
                .withCredential(getICredential(key))
                .withRegion(IamRegion.CN_EAST_3)
                .build();
        KeystoneListAuthProjectsRequest request = new KeystoneListAuthProjectsRequest();
        KeystoneListAuthProjectsResponse response = client.keystoneListAuthProjects(request);
        return response.getProjects();
    }

    public static List<ServerDetail> getEcsLists(Key key){
        List<AuthProjectResult> projectId = getProjectId(key);
        List<ServerDetail> res = new ArrayList<>();
        for (AuthProjectResult authProjectResult : projectId) {
            if (StrUtil.contains(authProjectResult.getName(),"-")){
                String s = StrUtil.subBefore(authProjectResult.getName(), "_", false);
                ICredential baseICredential = getBaseICredential(key, authProjectResult.getId());
                EcsClient client = EcsClient
                        .newBuilder()
                        .withCredential(baseICredential)
                        .withRegion(EcsRegion.valueOf(s))
                        .build();
                int page = 1;
                while (true){
                    ListServersDetailsRequest request = new ListServersDetailsRequest();
                    request.setLimit(1000);
                    request.setOffset(page);
                    ListServersDetailsResponse response = client.listServersDetails(request);
                    if (response.getServers().size() >= 1){
                        for (ServerDetail server : response.getServers()) {
                            server.setOsEXTAZAvailabilityZone(s);
                        }
                        res.addAll(response.getServers());
                        page += 1;
                    }else break;
                }
            }
        }

        return res;
    }

    private static List<Region> getRegionLists(){
        List<Region> regions = new ArrayList<>();
        try {
            Field[] fields = EcsRegion.class.getDeclaredFields();
            for (Field field : fields) {
                if (Region.class.isAssignableFrom(field.getType())) {
                    regions.add((Region)field.get(null));
                    System.out.println(field.getName());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return regions;
    }

    public static String restartPassword(Key key, Instance instance, String password){
        ICredential iCredential = getBaseICredential(key,instance.getPublicKey());
        EcsClient client = EcsClient.newBuilder()
                .withCredential(iCredential)
                .withRegion(EcsRegion.valueOf(instance.getRegion()))
                .build();
        ResetServerPasswordRequest request = new ResetServerPasswordRequest();
        request.withServerId(instance.getInstanceId());
        ResetServerPasswordRequestBody body = new ResetServerPasswordRequestBody();
        ResetServerPasswordOption resetpasswordbody = new ResetServerPasswordOption();
        resetpasswordbody.withNewPassword(password)
                .withIsCheckPassword(true);
        body.withResetPassword(resetpasswordbody);
        request.withBody(body);
        try {
            client.resetServerPassword(request);
            rebootServer(key,instance);
            return password;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public static void rebootServer(Key key, Instance instance){
        EcsClient client = EcsClient.newBuilder()
                .withCredential(getBaseICredential(key,instance.getPublicKey()))
                .withRegion(EcsRegion.valueOf(instance.getRegion()))
                .build();
        BatchRebootServersRequest request = new BatchRebootServersRequest();
        BatchRebootServersRequestBody body = new BatchRebootServersRequestBody();
        List<ServerId> listRebootServers = new ArrayList<>();
        listRebootServers.add(
                new ServerId()
                        .withId(instance.getInstanceId())
        );
        BatchRebootSeversOption rebootbody = new BatchRebootSeversOption();
        rebootbody.withServers(listRebootServers)
                .withType(BatchRebootSeversOption.TypeEnum.fromValue("SOFT"));
        body.withReboot(rebootbody);
        request.withBody(body);
        try {
            client.batchRebootServers(request);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
