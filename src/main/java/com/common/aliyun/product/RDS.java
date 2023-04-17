package com.common.aliyun.product;

import com.aliyun.rds20140815.Client;
import com.aliyun.rds20140815.models.DescribeRegionsResponse;
import com.aliyun.rds20140815.models.DescribeRegionsResponseBody;
import com.domain.Key;

import java.util.List;

public class RDS {
    public static void getRDSLists(Key key) throws Exception {
        com.aliyun.rds20140815.Client client = getRdsClient(key);
        com.aliyun.rds20140815.models.DescribePriceRequest describePriceRequest = new com.aliyun.rds20140815.models.DescribePriceRequest();
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        client.describePriceWithOptions(describePriceRequest, runtime);
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
     * 获取rds 可用region
     * @param key
     * @return
     * @throws Exception
     */
    private static List<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRDSRegion> getRdsRegion(Key key) throws Exception {
        Client rdsClient = getRdsClient(key);
        com.aliyun.rds20140815.models.DescribeRegionsRequest describeRegionsRequest = new com.aliyun.rds20140815.models.DescribeRegionsRequest();
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        DescribeRegionsResponse describeRegionsResponse = rdsClient.describeRegionsWithOptions(describeRegionsRequest, runtime);
        return describeRegionsResponse.body.regions.RDSRegion;
    }


}
