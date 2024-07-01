package com.common.aliyun.product;

import com.aliyun.cs20151215.Client;
import com.aliyun.cs20151215.models.DescribeClustersV1Response;
import com.common.aliyun.Base;
import com.domain.Key;

public class ACK {
    com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();

    public static com.aliyun.cs20151215.Client createClient(Key key) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(key.getSecretId())
                .setAccessKeySecret(key.getSecretKey());
        // Endpoint 请参考 https://api.aliyun.com/product/CS
        config.endpoint = "cs.cn-beijing.aliyuncs.com";
        return new com.aliyun.cs20151215.Client(config);
    }

    public void getClustgerLists(Key key){
        try {
            Client client = createClient(key);
            com.aliyun.cs20151215.models.DescribeClustersV1Request describeClustersV1Request = new com.aliyun.cs20151215.models.DescribeClustersV1Request();
            java.util.Map<String, String> headers = new java.util.HashMap<>();
            DescribeClustersV1Response describeClustersV1Response = client.describeClustersV1WithOptions(describeClustersV1Request, headers, runtime);
            describeClustersV1Response.body.getClusters().forEach(cluster -> {

            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
