package com.common.tencent.product;

import com.domain.Key;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.Bucket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class COS {

    public static String cosEndPoint = "%s.cos.%s.myqcloud.com";

    /*
     * 遍历存储桶
     * @param secretId
     * @param secretKey
     * @param token
     * @return 桶列表
     */
    public List<Bucket> getCosList(Key key){
        Map<String,String> result = new HashMap<>();
        COSCredentials cred = null;
        if (key.getToken() != null && !key.getToken().equals("")){
            cred = new BasicSessionCredentials(key.getSecretid(), key.getSecretkey(), key.getToken());
        }
        else {
            cred = new BasicCOSCredentials(key.getSecretid(), key.getSecretkey());
        }
        ClientConfig clientConfig = new ClientConfig();
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient.listBuckets();
    }

}
