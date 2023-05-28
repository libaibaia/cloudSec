package com.common.huawei;

import cn.hutool.core.util.StrUtil;
import com.domain.Key;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.model.ListServersDetailsRequest;
import com.huaweicloud.sdk.ecs.v2.model.ListServersDetailsResponse;
import com.huaweicloud.sdk.ecs.v2.model.ServerAddress;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import com.huaweicloud.sdk.ecs.v2.region.EcsRegion;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.AuthProjectResult;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListAuthProjectsRequest;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListAuthProjectsResponse;
import com.huaweicloud.sdk.iam.v3.region.IamRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Ecs {
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

    private static ICredential getBaseICredential(Key key, String projectId){
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
                        res.addAll(response.getServers());
                        page += 1;
                    }else break;
                }
            }
        }

        return res;
    }

}
