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
    public static DescribeInvocationResultsResponse createCommand(Client client, String regionID, String name, String type, String command, String instanceId,boolean isWait) throws Exception {
        CreateCommandRequest createCommandRequest = new CreateCommandRequest();
        Random random = new Random();
        //随机名称，防止重复
        createCommandRequest.setName(name + random.nextInt(10));
        createCommandRequest.setCommandContent(Base64.getEncoder().encodeToString(command.getBytes()));
        createCommandRequest.setType(type);
        createCommandRequest.setRegionId(regionID);
        CreateCommandResponse commandWithOptions = client.createCommandWithOptions(createCommandRequest, runtime);
        String s = execCommand(client, regionID, commandWithOptions.getBody().commandId, instanceId);
        return getExecResult(client,s,regionID,instanceId,isWait);
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
    private static DescribeInvocationResultsResponse getExecResult(Client client, String command,String regionId,String instanceId,boolean isWaitOotPut) throws Exception {
        DescribeInvocationResultsRequest request = new DescribeInvocationResultsRequest();
        request.setInvokeId(command);
        request.setRegionId(regionId);
        request.setInstanceId(instanceId);
        request.setIncludeHistory(true);

        if (isWaitOotPut){
            DescribeInvocationResultsResponse describeInvocationResultsResponse = client.describeInvocationResultsWithOptions(request, runtime);
            for (DescribeInvocationResultsResponseBody.DescribeInvocationResultsResponseBodyInvocationInvocationResultsInvocationResult result : describeInvocationResultsResponse.getBody().getInvocation().getInvocationResults().getInvocationResult()) {
                if (result.getCommandId().equals(command)){
                    while (true){
                        if(result.getInvocationStatus().equals("Timeout") || result.getInvocationStatus().equals("Error")
                        || result.getInvocationStatus().equals("Failed") || result.getInvocationStatus().equals("Failed")){
                            result.setOutput(Base64.getEncoder().encodeToString("执行失败".getBytes()));
                            return describeInvocationResultsResponse;
                        }
                        if (result.getInvocationStatus().equals("Running")){
                            continue;
                        }
                        if (result.getInvocationStatus().equals("Success")) return describeInvocationResultsResponse;
                        else break;
                    }
                }
            }
        }
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
