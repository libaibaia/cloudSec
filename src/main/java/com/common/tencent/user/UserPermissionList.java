package com.common.tencent.user;

import com.common.tencent.product.Base;
import com.domain.Key;
import com.tencentcloudapi.cam.v20190116.CamClient;
import com.tencentcloudapi.cam.v20190116.models.*;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserPermissionList{
    private static final Logger logger = LoggerFactory.getLogger(UserPermissionList.class);
     private HttpProfile httpProfile = new HttpProfile();
     private ClientProfile clientProfile = new ClientProfile();
     private GetUserAppIdResponse userAppId;
     protected Key key;

     private CamClient getCamClient(String region){
         return new CamClient(Base.createCredential(key), "", clientProfile);
     }

    /***
     * 获取用户权限边界,需要配置了用户边界，否则返回为null
     */
    private GetUserPermissionBoundaryResponse getUserPermissionBoundary(){
        httpProfile.setEndpoint("cam.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        GetUserPermissionBoundaryRequest req = new GetUserPermissionBoundaryRequest();
        req.setTargetUin(Long.valueOf(getUserUin().getUin()));
        try {
            GetUserPermissionBoundaryResponse getUserPermissionBoundaryResponse = getCamClient("").GetUserPermissionBoundary(req);
            logger.info(GetUserPermissionBoundaryRequest.toJsonString(getUserPermissionBoundaryResponse));
            return getUserPermissionBoundaryResponse;
        } catch (TencentCloudSDKException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /***
     * 获取用户uin
     * @return uin
     */
    private GetUserAppIdResponse getUserUin(){
        httpProfile.setEndpoint("cam.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        GetUserAppIdRequest req = new GetUserAppIdRequest();
        try {
            return getCamClient("").GetUserAppId(req);
        } catch (TencentCloudSDKException e) {
            logger.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delAccessKey(String accessKey) throws TencentCloudSDKException {
        httpProfile.setEndpoint("cam.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        DeleteAccessKeyRequest req = new DeleteAccessKeyRequest();
        req.setAccessKeyId(accessKey);
        DeleteAccessKeyResponse resp = getCamClient("").DeleteAccessKey(req);
    }

    /**
     * 创建新的密钥，此处需要密钥数量不能大于腾讯云规定数量
     * @return 成功返回key，失败返回null
     */
    public CreateAccessKeyResponse createAccessKey(){
        httpProfile.setEndpoint("cam.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        ListAccessKeysResponse listAccessKeysResponse = listAccessKey();
        if (listAccessKeysResponse.getAccessKeys().length >= 2){
            CreateAccessKeyResponse createAccessKeyResponse = new CreateAccessKeyResponse();
            List<String> s = new ArrayList<>();
            StringBuilder message = new StringBuilder();
            for (AccessKey accessKey : listAccessKeysResponse.getAccessKeys()) {
                message.append(accessKey.getAccessKeyId());
            }
            createAccessKeyResponse.setRequestId("创建出错原因：目标密钥数量大于2，密钥内容如下:" + new String(message));
            return createAccessKeyResponse;
        }
        CreateAccessKeyRequest req = new CreateAccessKeyRequest();
        try {
            return getCamClient("").CreateAccessKey(req);
        } catch (TencentCloudSDKException e) {
            logger.error(e.getMessage());
            CreateAccessKeyResponse createAccessKeyResponse = new CreateAccessKeyResponse();
            createAccessKeyResponse.setRequestId("创建出错原因：" + e.getMessage());
            return createAccessKeyResponse;
        }
    }

    /**
     * 列出访问密钥
     * @return 密钥列表
     */
    public ListAccessKeysResponse listAccessKey(){
        httpProfile.setEndpoint("cam.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        ListAccessKeysRequest req = new ListAccessKeysRequest();
        ListAccessKeysResponse resp = null;
        try {
            return getCamClient("").ListAccessKeys(req);
        } catch (TencentCloudSDKException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 获取策略列表
     */
    public void getPoliciesList(){
        httpProfile.setEndpoint("cam.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        ListPoliciesRequest req = new ListPoliciesRequest();
        ListPoliciesResponse resp = null;
        try {
            resp = getCamClient("").ListPolicies(req);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
        // 输出json格式的字符串回包
        System.out.println(ListPoliciesResponse.toJsonString(resp));
    }

    private HashMap<String, String> createSubUser() throws TencentCloudSDKException {
        httpProfile.setEndpoint("cam.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        AddUserRequest req = new AddUserRequest();
        GetUserAppIdResponse owner = getOwner();
        HashMap<String,String> hashMap = new HashMap<>();
        if (owner != null){
            req.setName(UUID.randomUUID().toString());
            req.setConsoleLogin(1L);
            req.setUseApi(1L);
            req.setRemark("测试用户");
            AddUserResponse user = getCamClient("").AddUser(req);
            hashMap.put("userName",user.getName());
            hashMap.put("OwnerUin",owner.getOwnerUin());
            hashMap.put("password",user.getPassword());
            hashMap.put("message","success");
            hashMap.put("uin",user.getUin().toString());
            return hashMap;
        }

        return hashMap;
    }

    /**
     * 创建并绑定权限到账户
     * @return 结果
     * @throws TencentCloudSDKException 创建失败的信息
     */
    public HashMap<String, String> bindPer() throws TencentCloudSDKException {
        httpProfile.setEndpoint("cam.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        AttachUserPolicyRequest req = new AttachUserPolicyRequest();
        AttachUserPolicyResponse resp = null;

        HashMap<String, String> subUser = createSubUser();
        String uin = subUser.get("uin");
        try {
            AttachPolicyInfo[] list = listAttachedUserPolicies().getList();
            for (AttachPolicyInfo attachPolicyInfo : list) {
                if (list.length > 10){
                    Thread.sleep(100);
                }
                req.setPolicyId(attachPolicyInfo.getPolicyId());
                req.setAttachUin(Long.valueOf(uin));
                getCamClient("").AttachUserPolicy(req);
            }
        } catch (TencentCloudSDKException | InterruptedException e) {
            subUser.put("message","创建失败，原因：" + e.getMessage());
        }
        return subUser;
    }

    /**
     * 列出所有绑定的策略需要有cam权限
     * @return 结果
     */
    public ListAttachedUserPoliciesResponse listAttachedUserPolicies(){
        httpProfile.setEndpoint("cam.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        ListAttachedUserPoliciesRequest req = new ListAttachedUserPoliciesRequest();
        req.setTargetUin(Long.valueOf(getUserUin().getUin()));
        ListAttachedUserPoliciesResponse resp = null;
        try {
            return getCamClient("ap-chongqing").ListAttachedUserPolicies(req);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }

    }

    private GetUserAppIdResponse getOwner(){
        httpProfile.setEndpoint("cam.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        GetUserAppIdRequest req = new GetUserAppIdRequest();
        GetUserAppIdResponse resp = null;
        try {
            resp = getCamClient("").GetUserAppId(req);
            return resp;
        } catch (TencentCloudSDKException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public UserPermissionList(Key key) {
        this.key = key;
    }

    public void test(){
    }
}
