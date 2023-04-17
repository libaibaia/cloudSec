package com.common.tencent.product.cvm;

import com.common.tencent.product.Base;
import com.domain.Key;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.*;
import com.tencentcloudapi.region.v20220627.models.RegionInfo;
import com.tencentcloudapi.tat.v20201028.TatClient;
import com.tencentcloudapi.tat.v20201028.models.*;
import com.tencentcloudapi.tat.v20201028.models.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.Charset;
import java.util.*;



/* 如下为腾讯云tat权限配置，如果要限定某台主机有执行命令权限需要加入资源六段式command
{
    "version": "2.0",
    "statement": [
        {
            "effect": "allow",
            "action": [
                "tat:DescribeInvocations",
                "tat:DescribeInvocationTasks",
                "tat:*"
            ],
            "resource": [
                "qcs::cvm:ap-nanjing:uin/uid:instance/ins-xx",
                "qcs::tat:ap-nanjing:uin/uid:command/*"
            ]
        }
    ]
}
 */

/*
cvm产品相关
 */
public class CVM {
    protected final String cvmEndPoint = "cvm.tencentcloudapi.com";

    private Key key;
    private HttpProfile httpProfile = new HttpProfile();

    /*
     资源级接口：此类型接口支持对某一个具体特定的资源进行授权。
     操作级接口：此类型接口不支持对某一个特定的资源进行授权。授权时策略语法若限定了具体的资源，CAM会判断此接口不在授权范围，判断为无权限。
     也就是说这个接口不能按照特定的资源进行权限细化
     */
    private static final Logger logger = LoggerFactory.getLogger(CVM.class);

    public CVM(Key key) {
        this.key = key;
    }

    public CVM() {
    }

    /**
     * 遍历所有区域中存在的云服务器
     * @return 云服务器列表
     */
    public Map<RegionInfo, Instance[]> getCvmList() throws TencentCloudSDKException {
        Map<RegionInfo, Instance[]> map = new HashMap<>();
        httpProfile.setEndpoint(cvmEndPoint);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        RegionInfo[] regionInfos = Base.getRegionList(Base.createCredential(key),"cvm");

        if (regionInfos != null){
            for (RegionInfo regionInfo : regionInfos) {
                String region = regionInfo.getRegion();
                CvmClient cvmClient = new CvmClient(Base.createCredential(key),region,clientProfile);
                DescribeInstancesRequest req = new DescribeInstancesRequest();
                try {
                    DescribeInstancesResponse describeInstancesResponse = cvmClient.DescribeInstances(req);
                    Instance[] instanceSet = describeInstancesResponse.getInstanceSet();
                    if (instanceSet.length >= 1){
                        map.put(regionInfo,instanceSet);
                    }
                } catch (TencentCloudSDKException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return map;
    }
    /*
    执行实例命令，在这之前需要提供凭证，实例id，执行的命令及区域参数，需要有tat操作权限
     */
    public RunCommandResponse execCommand(Credential credential, String command, String[] instanceId, RegionInfo regionInfo){
        httpProfile.setEndpoint("tat.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        TatClient client = new TatClient(credential, regionInfo.getRegion(), clientProfile);
        RunCommandRequest req = new RunCommandRequest();
        req.setInstanceIds(instanceId);
        client.setRegion(regionInfo.getRegion());
        req.setContent(Base64.getEncoder().encodeToString(command.getBytes(Charset.defaultCharset())));
        try {
            return client.RunCommand(req);
        } catch (TencentCloudSDKException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public Map<Instance, DescribeInvocationTasksResponse> getOutCommandPut(String command, Instance[] instanceId, RegionInfo regionInfo){
        //用于接受执行命令后的响应对象，为了方便判断是否执行成功，然后获取命令id，获取输出
        Map<Instance,RunCommandResponse> runCommandResponseMap = new HashMap<>();
        //用于遍历上面命令id存放获取的执行结果。为了方便获取结果，此处将实例id传入单个参数，不直接执行所有的实例
        Map<Instance,DescribeInvocationTasksResponse> describeInvocationTasksResponse = new HashMap<>();
        //此处遍历主要是为了保证返回的结果是有标识的，插入数据库时可以通过判断setCommandId是否为空来判断命令是否执行成功，如果命令id为空
        // 则返回null，数据库中可以标识不能执行命令
        for (Instance instance : instanceId) {
            RunCommandResponse runCommandResponse = execCommand(Base.createCredential(key), command, new String[]{instance.getInstanceId()}, regionInfo);
            RunCommandResponse runCommandResponse1 = new RunCommandResponse();
            if (runCommandResponse == null){
                runCommandResponse1.setCommandId("null");
                runCommandResponseMap.put(instance,runCommandResponse1);
            }
            else {
                runCommandResponseMap.put(instance,runCommandResponse);
            }
        }

        httpProfile.setEndpoint("tat.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        TatClient client = new TatClient(Base.createCredential(key),regionInfo.getRegion(), clientProfile);
        DescribeInvocationTasksRequest req = new DescribeInvocationTasksRequest();
        String commandId = null;

        //遍历执行命令后的结果id，获取执行结果，如果id不为空则执行成功
        for (Instance instance : runCommandResponseMap.keySet()) {
            RunCommandResponse runCommandResponse = runCommandResponseMap.get(instance);
            if (!runCommandResponse.getCommandId().equals("null")){
                commandId = runCommandResponse.getCommandId();
                //根据命令id筛选获取执行结果
                Filter filter = new Filter();
                filter.setName("command-id");
                filter.setValues(new String[]{commandId});
                req.setFilters(new Filter[]{filter});
                req.setHideOutput(false);
                try {
                    describeInvocationTasksResponse.put(instance,client.DescribeInvocationTasks(req));
                } catch (TencentCloudSDKException e) {
                    logger.error(e.getMessage());
                }
            }else {
                DescribeInvocationTasksResponse d = new DescribeInvocationTasksResponse();
                InvocationTask invocationTask = new InvocationTask();
                invocationTask.setCommandId("null");
                d.setInvocationTaskSet(new InvocationTask[]{invocationTask});
                describeInvocationTasksResponse.put(instance,d);
            }
        }
        return describeInvocationTasksResponse;
    }

    //测试
    public static void main(String[] args) throws TencentCloudSDKException {
    }

    /*
    初始添加凭证会执行这个方法，用于检测那些区域的服务器可以执行命令
     */
    public Map<Instance, List<InvocationTask>> getResult() throws TencentCloudSDKException {
        Map<RegionInfo, Instance[]> cvmList = getCvmList();
        Map<Instance,List<InvocationTask>> re = new HashMap<>();
        if (cvmList.size() >= 1){
            List<Instance> list = new ArrayList<>();
            List<InvocationTask> des = new ArrayList<>();
            for (RegionInfo regionInfo : cvmList.keySet()) {
                Instance[] instances = cvmList.get(regionInfo);
                list.addAll(Arrays.asList(instances));
                Map<Instance, DescribeInvocationTasksResponse> whoami = getOutCommandPut("whoami", list.toArray(new Instance[list.size()]), regionInfo);
                for (Map.Entry<Instance, DescribeInvocationTasksResponse> execRes : whoami.entrySet()) {
                    for (InvocationTask invocationTask : whoami.get(execRes.getKey()).getInvocationTaskSet()) {
                        execRes.getKey().set("region",regionInfo.getRegion());
                        re.put(execRes.getKey(),Arrays.asList(whoami.get(execRes.getKey()).getInvocationTaskSet()));
                    }
                }
//
//                for (DescribeInvocationTasksResponse describeInvocationTasksResponse : whoami) {
//                    des.addAll(Arrays.asList(describeInvocationTasksResponse.getInvocationTaskSet()));
//                    re.put(regionInfo,des);
//                }
//                re.put(regionInfo,des);
            }
        }
        return re;
    }

    /**
     * 创建密钥对，默认项目为id为0,返回创建后的密钥对信息，此时创建在项目中，需要绑定到实例才能使用
     */
    public CreateKeyPairResponse createKeyPair(String keyName) throws TencentCloudSDKException {
        httpProfile.setEndpoint("cvm.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        CvmClient client = new CvmClient(Base.createCredential(key), "ap-beijing", clientProfile);
        CreateKeyPairRequest req = new CreateKeyPairRequest();
        req.setProjectId(0L);
        req.setKeyName(keyName);
        CreateKeyPairResponse resp = null;
        return client.CreateKeyPair(req);
    }

    /*
    此功能会强制关机云服务器，且密码登录会被取消
     */
    public String bindKeyPair(com.domain.Instance instance, KeyPair keyPair) throws TencentCloudSDKException {
        httpProfile.setEndpoint("cvm.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        CvmClient client = new CvmClient(Base.createCredential(key), instance.getRegion() , clientProfile);
        AssociateInstancesKeyPairsRequest req = new AssociateInstancesKeyPairsRequest();
        req.setInstanceIds(new String[]{instance.getInstanceId()});
        req.setKeyIds(new String[]{keyPair.getKeyId()});
        req.setForceStop(true);
        AssociateInstancesKeyPairsResponse resp = client.AssociateInstancesKeyPairs(req);
        return resp.getRequestId();
    }

}
