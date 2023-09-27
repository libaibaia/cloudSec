package com.common.aliyun;

import cn.hutool.core.util.StrUtil;
import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.DescribeRegionsRequest;
import com.aliyun.ecs20140526.models.DescribeRegionsResponse;
import com.aliyun.ecs20140526.models.DescribeRegionsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.domain.Key;

import java.util.ArrayList;
import java.util.List;

public class Base {

    /**
     * 创建客户端凭证
     * @param endPoint endPoint
     * @return 客户端凭证
     * @throws Exception 异常信息
     */
    public static Client createClient(Key key, String endPoint) throws Exception {
        Config config;
        if (!StrUtil.isBlank(key.getToken())){
            config = new Config().setAccessKeyId(key.getSecretId()).setAccessKeySecret(key.getSecretId()).setSecurityToken(key.getToken());
        }else {
            config = new Config().setAccessKeyId(key.getSecretId()).setAccessKeySecret(key.getSecretKey());
        }
        config.endpoint = endPoint;
        return new Client(config);
    }

    /**
     *  获取可用区域
     * @return 区域信息
     * @throws Exception 异常
     */
    public static List<DescribeRegionsResponseBody.
            DescribeRegionsResponseBodyRegionsRegion> getRegionInfo(Key key, String endPoint) throws Exception {
        DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
        RuntimeOptions runtime = new RuntimeOptions();
        Client client = Base.createClient(key, endPoint);
        DescribeRegionsResponse response = client.describeRegionsWithOptions(describeRegionsRequest, runtime);
        return new ArrayList<>(response.getBody().getRegions().getRegion());
    }

}
