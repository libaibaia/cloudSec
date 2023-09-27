package com.common.tencent.product;

import cn.hutool.core.util.StrUtil;
import com.domain.Key;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.region.v20220627.RegionClient;
import com.tencentcloudapi.region.v20220627.models.DescribeRegionsRequest;
import com.tencentcloudapi.region.v20220627.models.DescribeRegionsResponse;
import com.tencentcloudapi.region.v20220627.models.RegionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Base {
    private static Logger logger = LoggerFactory.getLogger(Base.class);
    private static final String regionEndPoint = "region.tencentcloudapi.com";
    /***
     * 获取对应产品可用区域列表，用于获取对应区域的cvm
     * @return 可用区列表
     */
    public static RegionInfo[] getRegionList(Credential cred, String product) throws TencentCloudSDKException {
        //请求参数，设置请求域
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(regionEndPoint);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        RegionClient client = new RegionClient(cred, "", clientProfile);
        DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
        //设置产品
        describeRegionsRequest.setProduct(product);
        DescribeRegionsResponse describeRegionsResponse = client.DescribeRegions(describeRegionsRequest);
        return describeRegionsResponse.getRegionSet();
    }
    public static Credential createCredential(Key key){
        Credential credential = null;
        if (!StrUtil.isBlank(key.getToken())){
            credential = new Credential(key.getSecretId(),key.getSecretKey(),key.getToken());
        }
        else {
            credential = new Credential(key.getSecretId(),key.getSecretKey());
        }
        return credential;
    }

}
