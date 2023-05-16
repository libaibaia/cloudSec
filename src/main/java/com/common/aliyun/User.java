package com.common.aliyun;

import com.aliyun.ims20190815.Client;
import com.aliyun.ims20190815.models.CreateLoginProfileRequest;
import com.aliyun.ims20190815.models.CreateUserRequest;
import com.aliyun.ims20190815.models.GetUserResponse;
import com.aliyun.ram20150501.models.ListPoliciesForUserResponse;
import com.aliyun.ram20150501.models.ListPoliciesForUserResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teaopenapi.models.OpenApiRequest;
import com.aliyun.teaopenapi.models.Params;
import com.aliyun.teautil.models.RuntimeOptions;
import com.domain.Key;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {
    public static final String loginUrl = "https://signin.aliyun.com/login.htm?callback=https%3A%2F%2Fram.console.aliyun.com%2Fusers%2Fcreate#/main";
    public static Map createConsoleUser(Key key, String username) throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setDisplayName(username);
        String s = username + "@" + getDefaultDomain(key);
        createUserRequest.setUserPrincipalName(s);
        com.aliyun.ims20190815.Client client = new Client(getIamClient(key,null));
        RuntimeOptions runtime = new RuntimeOptions();
        client.createUserWithOptions(createUserRequest, runtime);
        String console = createConsole(s, client, runtime);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name",s);
        hashMap.put("loginUrl",loginUrl);
        hashMap.put("password",console);
        updatePolicy(key,username);
        return hashMap;
    }
    public static String getDefaultDomain(Key key) throws Exception {
        Config config;
        if (key.getToken() != null) config = new Config().setAccessKeyId(key.getSecretid()).setAccessKeySecret(key.getSecretkey()).setSecurityToken(key.getToken());
        else config = new Config().setAccessKeyId(key.getSecretid()).setAccessKeySecret(key.getSecretkey());
        config.endpoint = "ims.aliyuncs.com";
        Client client = new Client(config);
        Params params = new com.aliyun.teaopenapi.models.Params()
                // 接口名称
                .setAction("GetDefaultDomain")
                // 接口版本
                .setVersion("2019-08-15")
                // 接口协议
                .setProtocol("HTTPS")
                // 接口 HTTP 方法
                .setMethod("POST")
                .setAuthType("AK")
                .setStyle("RPC")
                // 接口 PATH
                .setPathname("/")
                // 接口请求体内容格式
                .setReqBodyType("json")
                // 接口响应体内容格式
                .setBodyType("json");
        RuntimeOptions runtime = new RuntimeOptions();
        OpenApiRequest request = new OpenApiRequest();
        Map<String, ?> stringMap = client.callApi(params, request, runtime);
        Map body = (Map) stringMap.get("body");
        return (String) body.get("DefaultDomainName");
    }

    private static String createConsole(String username,Client client,RuntimeOptions runtime) throws Exception {
        String s = UUID.randomUUID().toString();
        CreateLoginProfileRequest createLoginProfileRequest = new com.aliyun.ims20190815.models.CreateLoginProfileRequest()
                .setUserPrincipalName(username)
                .setPassword(s);
        client.createLoginProfileWithOptions(createLoginProfileRequest, runtime);
        return s;
    }

    public static Config getIamClient(Key key,String endPoint){
        com.aliyun.teaopenapi.models.Config config;
        if (key.getToken() != null) config = new com.aliyun.teaopenapi.models.Config().setAccessKeyId(key.getSecretid()).setAccessKeySecret(key.getSecretkey()).setSecurityToken(key.getToken());
        else {
            config = new com.aliyun.teaopenapi.models.Config().setAccessKeyId(key.getSecretid()).setAccessKeySecret(key.getSecretkey());
        }
        // 访问的域名
        if (endPoint == null)config.endpoint = "ims.aliyuncs.com";
        else if (endPoint.equals("ram"))config.endpoint = "ram.aliyuncs.com";

        return config;
    }
    /*
    更新权限，管理权限
     */
    public static void updatePolicy(Key key,String username) throws Exception {
        com.aliyun.ram20150501.Client client = new com.aliyun.ram20150501.Client(getIamClient(key,"ram"));
        com.aliyun.ram20150501.models.AttachPolicyToUserRequest attachPolicyToUserRequest = new com.aliyun.ram20150501.models.AttachPolicyToUserRequest();
        attachPolicyToUserRequest.setUserName(username);
        attachPolicyToUserRequest.setPolicyName("AdministratorAccess");
        attachPolicyToUserRequest.setPolicyType("System");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        client.attachPolicyToUserWithOptions(attachPolicyToUserRequest, runtime);
    }

    //获取用户信息
    public static ListPoliciesForUserResponseBody.ListPoliciesForUserResponseBodyPolicies ListPoliciesForUser(Key key) throws Exception {
        com.aliyun.ims20190815.models.GetUserRequest getUserRequest = new com.aliyun.ims20190815.models.GetUserRequest()
                .setUserAccessKeyId(key.getSecretid());
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        Client client = new Client(getIamClient(key, null));
        GetUserResponse userWithOptions = client.getUserWithOptions(getUserRequest, runtime);
        String username = userWithOptions.getBody().user.getUserPrincipalName().split("@")[0];
        com.aliyun.ram20150501.models.ListPoliciesForUserRequest request = new com.aliyun.ram20150501.models.ListPoliciesForUserRequest()
                .setUserName(username);
        com.aliyun.ram20150501.Client ram = new com.aliyun.ram20150501.Client(getIamClient(key, "ram"));
        ListPoliciesForUserResponse listPoliciesForUserResponse = ram.listPoliciesForUserWithOptions(request, runtime);
        return listPoliciesForUserResponse.body.policies;
    }

}
