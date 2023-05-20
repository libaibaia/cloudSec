package com.common.qiniu.qvm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.*;
import com.common.qiniu.base.RegionInfo;
import com.common.qiniu.base.model.error.ErrorException;
import com.common.qiniu.base.model.error.ErrorResponse;
import com.common.qiniu.base.model.qvm.InstanceInfoResponse;
import com.common.qiniu.base.model.qvm.KeyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.util.Auth;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Qvm {
    public static final String BaseUrl = "https://api-qvm.qiniu.com";

    public static RegionInfo getRegionList(Auth auth) throws JsonProcessingException {
        String s = auth.signQiniuAuthorization(BaseUrl + QvmBaseUrl.QvmRegionListUrl.getUrl(),
                QvmBaseUrl.QvmRegionListUrl.getMethod(),null,"");
        HttpRequest request = HttpRequest.get(BaseUrl +
                        QvmBaseUrl.QvmRegionListUrl.getUrl())
                .auth("Qiniu " + s);
        HttpResponse execute = request.execute();
        isSuccess(execute);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(execute.body(), RegionInfo.class);
    }


    /**
     * 获取实例信息
     * @param auth 认证
     * @return 实例模型对象
     * @throws JsonProcessingException
     */
    public static List<InstanceInfoResponse> getInstanceLists(Auth auth) throws Exception {

        Map<String,Object> args = new HashMap<>();
        int page = 1;
        args.put("size",100);
        args.put("page",page);
        List<InstanceInfoResponse> responses = new ArrayList<>();
        while (true){
            HttpRequest request = HttpRequest.get(BaseUrl + QvmBaseUrl.QvmInstanceList.getUrl())
                    .form(args);
            String s = auth.signQiniuAuthorization(request.getUrl() + "?" + "size=" + args.get("size") + "&" + "page=" + args.get("page"),
                    QvmBaseUrl.QvmRegionListUrl.getMethod(),null,"");
                    request.auth("Qiniu " + s).setHttpProxy("127.0.0.1",8080);
            HttpResponse execute = request.execute();
            ObjectMapper objectMapper = new ObjectMapper();
            //如果响应不为200则代表失败，失败抛出异常，其中包含错误信息
            isSuccess(execute);
            InstanceInfoResponse instanceInfoResponse = objectMapper.readValue(execute.body(), InstanceInfoResponse.class);
            if (!ObjectUtil.isNull(instanceInfoResponse.getData()) && instanceInfoResponse.getData().size() >= 1){
                responses.add(instanceInfoResponse);
                page += 1;
                args.put("page",page);
            }else break;
        }
        return responses;
    }

    public static void bindKeyPair(Auth auth, String keyName, String instanceId, String regionId) throws Exception {
        String url = BaseUrl +  String.format(QvmBaseUrl.BindKeyPair.getUrl(), keyName);
        System.out.println(url);
        String body = "{\"instances\":[{\"instance_id\":\"" + instanceId + "\",\"region_id\":\"" + regionId + "\"}]}";
        //生成签名
        String s = auth.signQiniuAuthorization(url,
                QvmBaseUrl.BindKeyPair.getMethod(),
                body.getBytes(),"text/plain;charset=UTF-8");
        HttpResponse execute = HttpRequest.post(url)
                .body(body)
                .auth("Qiniu " + s).header("Content-Type", "text/plain;charset=UTF-8")
                .execute();
        isSuccess(execute);
    }

    public static KeyResponse createKeyPair(Auth auth, String keyName, String instanceId, String regionId) throws Exception {
        String url = BaseUrl + QvmBaseUrl.ImportKeyPairUrl.getUrl();
        String s1 = "{\"key_pair_name\":" + "\"" + keyName + "\"" + "}";
        //生成签名
        String s = auth.signQiniuAuthorization(url,
                QvmBaseUrl.ImportKeyPairUrl.getMethod(),
                s1.getBytes(), "text/plain;charset=UTF-8");

        HttpResponse execute = HttpRequest.post(url).body(s1)
                .auth("Qiniu " + s)
                .header("Content-Type", "text/plain;charset=UTF-8")
                .execute();
        ObjectMapper objectMapper = new ObjectMapper();
        isSuccess(execute);
        KeyResponse keyResponse = objectMapper.readValue(execute.body(), KeyResponse.class);
        bindKeyPair(auth,keyResponse.getData().getKey_pair_name(),instanceId,regionId);
        rebootInstance(auth,instanceId,regionId);
        return keyResponse;
    }

    private static void isSuccess(HttpResponse response) throws JsonProcessingException {
        if (response.getStatus() != HttpStatus.HTTP_OK){
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponse errorResponse = objectMapper.readValue(response.body(), ErrorResponse.class);
            ErrorException errorException = new ErrorException(errorResponse.getError().getMessage());
            errorException.setErrorException(errorResponse);
            throw errorException;
        }
    }
    private static void rebootInstance(Auth auth, String instance,String region){
        String url = BaseUrl + String.format(QvmBaseUrl.RebootInstance.getUrl(), instance);
        String s1 = "{\"region_id\":" + "\"" + region + "\"" + "}";
        //生成签名
        String s = auth.signQiniuAuthorization(url,
                QvmBaseUrl.RebootInstance.getMethod(),
                s1.getBytes(), "text/plain;charset=UTF-8");

        HttpRequest.post(url).body(s1)
                .auth("Qiniu " + s)
                .header("Content-Type", "text/plain;charset=UTF-8")
                .execute();
    }

}
