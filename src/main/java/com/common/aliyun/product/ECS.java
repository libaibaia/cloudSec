package com.common.aliyun.product;

import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.*;
import com.aliyun.teautil.models.RuntimeOptions;
import com.common.aliyun.Base;
import com.domain.Key;
import java.util.*;

public class ECS {
    private static final RuntimeOptions runtime = new RuntimeOptions();

    /**
     * 获取实例列表
     *
     * @return 实例列表
     */
    public static Map<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion, List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance>> getECSLists(Key key){
        Map<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion,List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance>> map =
                new HashMap<>();
        try {
            List<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion> regionInfo = Base.getRegionInfo(key, "ecs-cn-hangzhou.aliyuncs.com");
            for (DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion des : regionInfo) {
                Client client = Base.createClient(key,des.getRegionEndpoint());
                DescribeInstancesRequest request = new DescribeInstancesRequest();
                request.setPageSize(100);
                request.setRegionId(des.regionId);
                int page = 1;
                while (true){
                    DescribeInstancesResponse describeInstancesResponse = client.describeInstancesWithOptions(request, runtime);
                    if (describeInstancesResponse.getBody().instances.instance.size() >= 1){
                        map.put(des,describeInstancesResponse.getBody().instances.instance);
                        page += 1;
                        request.setPageNumber(page);
                    }else break;
                }
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //此处endpoint为ecs-cn-hangzhou.aliyuncs.com
    public static void importKey(String keyName,String key,Client client,String regionId,String instanceId) throws Exception {
        ImportKeyPairRequest importKeyPairRequest = new ImportKeyPairRequest();
        importKeyPairRequest.setKeyPairName(keyName);
        importKeyPairRequest.setRegionId(regionId);
        importKeyPairRequest.setPublicKeyBody(key);
        client.importKeyPairWithOptions(importKeyPairRequest, runtime);
        bindKeyPair(client,regionId,keyName,instanceId);
        RebootInstanceRequest rebootInstanceRequest = new com.aliyun.ecs20140526.models.RebootInstanceRequest();
        rebootInstanceRequest.setInstanceId(instanceId);
        client.rebootInstanceWithOptions(rebootInstanceRequest, runtime);
    }
    //绑定密钥，需要密钥所在的区域id，密钥名称，要绑定的实例id
    public static void bindKeyPair(Client client,String regionId,String keyName,String instanceId) throws Exception {
        AttachKeyPairRequest attachKeyPairRequest = new AttachKeyPairRequest();
        attachKeyPairRequest.setKeyPairName(keyName);
        attachKeyPairRequest.setRegionId(regionId);
        attachKeyPairRequest.setInstanceIds("[" + "\"" + instanceId + "\"" + "]");
        client.attachKeyPairWithOptions(attachKeyPairRequest, runtime);
    }

    //创建云助手命令并获取执行结果
    public static DescribeInvocationResultsResponse createCommand(Client client, String regionID, String name, String type, String command, String instanceId) throws Exception {
        CreateCommandRequest createCommandRequest = new CreateCommandRequest();
        createCommandRequest.setName(name);
        createCommandRequest.setCommandContent(Base64.getEncoder().encodeToString(command.getBytes()));
        createCommandRequest.setType(type);
        createCommandRequest.setRegionId(regionID);
        CreateCommandResponse commandWithOptions = client.createCommandWithOptions(createCommandRequest, runtime);
        String s = execCommand(client, regionID, commandWithOptions.getBody().commandId, instanceId);
        //此处因为阿里云原因，执行后结果输出比较慢，需要sleep十秒
        Thread.sleep(10000);
        return getExecResult(client,s,regionID,instanceId);
    }
    //执行命令
    private static String execCommand(Client client,String regionId,String commandId,String instanceID) throws Exception {
        InvokeCommandRequest invokeCommandRequest = new InvokeCommandRequest();
        invokeCommandRequest.setCommandId(commandId);
        invokeCommandRequest.setRegionId(regionId);
        invokeCommandRequest.setInstanceId(Collections.singletonList(instanceID));
        InvokeCommandResponse invokeCommandResponse = client.invokeCommandWithOptions(invokeCommandRequest, runtime);
        return invokeCommandResponse.getBody().getInvokeId();
    }
    //获取执行结果
    private static DescribeInvocationResultsResponse getExecResult(Client client, String command,String regionId,String instanceId) throws Exception {
        DescribeInvocationResultsRequest request = new DescribeInvocationResultsRequest();
        request.setInvokeId(command);
        request.setRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setIncludeHistory(true);
        return client.describeInvocationResultsWithOptions(request, runtime);
    }

    public static String getType(String osType){
        if (osType.equalsIgnoreCase("windows")){
            return "RunBatScript";
        }
        if (osType.equalsIgnoreCase("linux")){
            return "RunShellScript";
        }
        return null;
    }

}
