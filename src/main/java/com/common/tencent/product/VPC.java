package com.common.tencent.product;

import com.domain.Key;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vpc.v20170312.VpcClient;
import com.tencentcloudapi.vpc.v20170312.models.*;

public class VPC {
    private static final HttpProfile httpProfile = new HttpProfile();

    private static final ClientProfile clientProfile = new ClientProfile();

    public static CreateDefaultSecurityGroupResponse createSecurityGroup(Key key, String region) throws TencentCloudSDKException {
        Credential credential = Base.createCredential(key);
        httpProfile.setEndpoint("vpc.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        VpcClient client = new VpcClient(credential, region, clientProfile);
        CreateDefaultSecurityGroupRequest req = new CreateDefaultSecurityGroupRequest();
        CreateDefaultSecurityGroupResponse resp = client.CreateDefaultSecurityGroup(req);
        return resp;
    }
}
