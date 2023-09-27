package com.common.huawei;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.domain.Key;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.*;
import com.huaweicloud.sdk.iam.v3.region.IamRegion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IAM {
    public static KeystoneListAuthDomainsResponse getUserInfo(Key key){

        IamClient client = IamClient.newBuilder()
                .withCredential(getBaseAuth(key))
                .withRegion(IamRegion.valueOf("ap-southeast-1"))
                .build();
        KeystoneListAuthDomainsRequest request = new KeystoneListAuthDomainsRequest();

        try {
            return client.keystoneListAuthDomains(request);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    private static ICredential getBaseAuth(Key key){
        ICredential auth;
        if (StrUtil.isBlank(key.getToken())){
            auth = new GlobalCredentials()
                    .withAk(key.getSecretId())
                    .withSk(key.getSecretKey());
        }else {
            auth = new GlobalCredentials()
                    .withAk(key.getSecretId())
                    .withSk(key.getSecretKey())
                    .withSecurityToken(key.getToken());
        }
        return auth;
    }

    private static List<KeystoneGroupResult> getUserGroup(Key key){
        IamClient client = IamClient.newBuilder()
                .withCredential(getBaseAuth(key))
                .withRegion(IamRegion.valueOf("ap-southeast-1"))
                .build();
        KeystoneListGroupsRequest request = new KeystoneListGroupsRequest();
        try {
            KeystoneListGroupsResponse keystoneListGroupsResponse = client.keystoneListGroups(request);
            return keystoneListGroupsResponse.getGroups();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void checkIsExist(Key key){
        IamClient client = IamClient.newBuilder()
                .withCredential(getBaseAuth(key))
                .withRegion(IamRegion.valueOf("ap-southeast-1"))
                .build();
        KeystoneListGroupsRequest request = new KeystoneListGroupsRequest();
        client.keystoneListGroups(request).getGroups();
    }


    public static Map<String, String> createIamUser(Key key, String username, String password){
        IamClient client = IamClient.newBuilder()
                .withCredential(getBaseAuth(key))
                .withRegion(IamRegion.valueOf("ap-southeast-1"))
                .build();
        CreateUserRequest request = new CreateUserRequest();
        CreateUserRequestBody body = new CreateUserRequestBody();
        CreateUserOption userbody = new CreateUserOption();
        KeystoneListAuthDomainsResponse userInfo = getUserInfo(key);
        Map<String,String> user = new HashMap<>();
        CreateUserResponse user1 = null;
        List<KeystoneGroupResult> userGroup = getUserGroup(key);
        /***
         * 此处使用两种方式创建
         * 1. 使用推荐API进行创建，需要能调用这个接口查询IAM用户可以访问的帐号详情，否则调用else中的接口
         */
        if (!ObjectUtil.isNull(userInfo.getDomains())){
            userbody.withAccessMode("default")
                    .withName(username)
                    .withDomainId(userInfo.getDomains().get(0).getId())
                    .withPassword(password);
            body.withUser(userbody);
            request.withBody(body);
            try {
                user1 = client.createUser(request);
                user.put("name",username);
                user.put("password",password);
                user.put("url","https://auth.huaweicloud.com/authui/login?id=" + userInfo.getDomains().get(0).getName());
                user.put("ownerUin",userInfo.getDomains().get(0).getId());
                assert userGroup != null;
                for (KeystoneGroupResult keystoneGroupResult : userGroup) {
                    if (!ObjectUtil.isNull(user1)){
                        addGroup(key,keystoneGroupResult.getId(),user1.getUser().getId());
                    }
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        //  2. 使用非推荐的API创建，创建后无法获取主账号信息，因此先获取管理员用户的domainid，将用户放在管理员的账号下面
        else {
            List<KeystoneListUsersResult> userLists = getUserLists(key);
            assert userLists != null;
            for (KeystoneListUsersResult userList : userLists) {
                if (userList.getDescription().contains("administrator") || userList.getDescription().contains("Enterprise")){
                    KeystoneCreateUserRequest newRequest = new KeystoneCreateUserRequest();
                    KeystoneCreateUserRequestBody newBody = new KeystoneCreateUserRequestBody();
                    KeystoneCreateUserOption newUserBody = new KeystoneCreateUserOption();
                    newUserBody.withName(username)
                            .withPassword(password);
                    newBody.withUser(newUserBody);
                    newRequest.withBody(newBody);
                    newUserBody.withDomainId(userList.getDomainId());
                    try {
                        KeystoneCreateUserResponse keystoneCreateUserResponse = client.keystoneCreateUser(newRequest);
                        user.put("name",username);
                        user.put("password",password);
                        user.put("url","https://auth.huaweicloud.com/authui/login?id=" + userList.getName());
                        user.put("ownerUin",userInfo.getDomains().get(0).getId());
                        for (KeystoneGroupResult keystoneGroupResult : userGroup) {
                            addGroup(key,keystoneGroupResult.getId(),keystoneCreateUserResponse.getUser().getId());
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return user;

    }

    private static boolean addGroup(Key key, String groupId, String userId){
        IamClient client = IamClient.newBuilder()
                .withCredential(getBaseAuth(key))
                .withRegion(IamRegion.valueOf("ap-southeast-1"))
                .build();
        KeystoneAddUserToGroupRequest request = new KeystoneAddUserToGroupRequest();
        request.withGroupId(groupId);
        request.withUserId(userId);
        try {
            client.keystoneAddUserToGroup(request);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    private static List<KeystoneListUsersResult> getUserLists(Key key){
        IamClient client = IamClient.newBuilder()
                .withCredential(getBaseAuth(key))
                .withRegion(IamRegion.valueOf("ap-southeast-1"))
                .build();
        KeystoneListUsersRequest request = new KeystoneListUsersRequest();
        try {
            KeystoneListUsersResponse keystoneListUsersResponse = client.keystoneListUsers(request);
            return keystoneListUsersResponse.getUsers();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
