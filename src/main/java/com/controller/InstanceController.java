package com.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.aliyun.ecs20140526.models.DescribeInvocationResultsResponse;
import com.aliyun.ecs20140526.models.DescribeInvocationResultsResponseBody;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.Type;
import com.common.aliyun.Base;
import com.common.aliyun.product.ECS;
import com.common.tencent.product.cvm.CVM;
import com.domain.Instance;
import com.domain.Key;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.service.impl.InstanceServiceImpl;
import com.service.impl.KeyServiceImpl;
import com.service.impl.tencent.TencentInstanceService;
import com.tencentcloudapi.region.v20220627.models.RegionInfo;
import com.tencentcloudapi.tat.v20201028.models.DescribeInvocationTasksResponse;
import com.tencentcloudapi.tat.v20201028.models.InvocationTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;


@RestController
@SaCheckLogin
@RequestMapping("/api/instance")
public class InstanceController {
    private Logger logger = LoggerFactory.getLogger(InstanceController.class);

    @Resource
    private KeyServiceImpl keyService;
    @Resource
    private InstanceServiceImpl instanceService;


//    /**
//     * 遍历密钥对应的区域中的cvm列表
//     * @return 结果集合
//     */
    @RequestMapping("/lists")
    public SaResult getInstanceLists(@RequestParam(required = false) String quick_search,
                                     @RequestParam(value = "page",defaultValue = "1",required = false) Integer page,
                                     @RequestParam(value = "limit",defaultValue = "10",required = false) Integer limit){
        List<Key> keys = keyService.getKeysByCreateId(Integer.parseInt(StpUtil.getLoginId().toString()));
        Page<Instance> objects = PageHelper.startPage(page, limit);
        List<Instance> instanceList = new ArrayList<>();
        if (quick_search != null){
            QueryWrapper<Key> keyQueryWrapper = new QueryWrapper<>();
            keyQueryWrapper.like("name",quick_search);
            List<Key> list = keyService.list(keyQueryWrapper);
            if (list.size() >= 1){
                instanceList = instanceService.getInstanceList(list);
            }
        }
        else {
            instanceList = instanceService.getInstanceList(keys);
        }
        return  SaResult.ok("获取成功").set("lists",instanceList).set("total",objects.getTotal());
    }
    @RequestMapping("/exec")
    public SaResult execCommand(@RequestBody Map<String,String> info) {
        Instance instance = instanceService.getById(Integer.parseInt(info.get("id")));
        if (instance != null){
            Key key = keyService.getById(instance.getKeyId());
            StringBuilder builder = new StringBuilder();
            if (key.getType().equals(Type.Tencent.toString())){
                if (key.getCreateById() == Integer.parseInt(StpUtil.getLoginId().toString())){
                    CVM cvm = new CVM(key);
                    com.tencentcloudapi.cvm.v20170312.models.Instance current_instance = new com.tencentcloudapi.cvm.v20170312.models.Instance();
                    current_instance.setInstanceId(instance.getInstanceId());
                    RegionInfo regionInfo = new RegionInfo();
                    regionInfo.setRegion(instance.getRegion());
                    Map<com.tencentcloudapi.cvm.v20170312.models.Instance, DescribeInvocationTasksResponse> execArgs =
                            cvm.getOutCommandPut(info.get("execArgs"), new com.tencentcloudapi.cvm.v20170312.models.Instance[]{current_instance}, regionInfo);
                    for (com.tencentcloudapi.cvm.v20170312.models.Instance instance1 : execArgs.keySet()) {
                        for (InvocationTask invocationTask : execArgs.get(instance1).getInvocationTaskSet()) {
                            String taskStatus;
                            if (invocationTask.getTaskResult() != null){
                                taskStatus = invocationTask.getTaskResult().getOutput();
                                byte[] decode = Base64.getDecoder().decode(taskStatus);
                                builder.append(new String(decode)).append('\n');
                            }
                        }
                    }
                }
            }
            if (key.getType().equals(Type.AliYun.toString())){
                try {
                    DescribeInvocationResultsResponse command = ECS.createCommand(Base.createClient(key, "ecs-cn-hangzhou.aliyuncs.com"), instance.getRegion(),
                            "test", ECS.getType(instance.getType()), info.get("execArgs"), instance.getInstanceId());
                    for (DescribeInvocationResultsResponseBody.DescribeInvocationResultsResponseBodyInvocationInvocationResultsInvocationResult result : command.getBody().invocation.invocationResults.invocationResult) {
                        byte[] decode = Base64.getDecoder().decode(result.output.getBytes());
                        builder.append(new String(decode));
                        builder.append("\n");
                    }
                } catch (Exception e) {
                    return SaResult.error("执行失败，原因：" + e.getMessage());
                }
            }
            if (builder.toString().equals("")){
                builder.append("结果为空,可能目标状态异常");
            }
            return SaResult.ok().set("execResult",builder);
        }
        return SaResult.error("执行失败,实例错误或无权限");
    }
    /**
     * 创建密钥对，需要有实例完全控制权限
     */
    @RequestMapping("/pair")
    public SaResult createKeyPair(@RequestBody Map<String,String> params)
    {
        int id = Integer.parseInt(params.get("id"));
        String keyName = params.get("keyName");
        String key = params.get("key");
        return instanceService.bindKeyPair(id,keyName,key);
    }

    @RequestMapping("/restoreKey")
    public SaResult originalKeyPair(@RequestParam("id") Integer id){
        Instance byId = instanceService.getById(id);
        try {
            instanceService.restoreKey(byId);
        } catch (Exception e) {
            return SaResult.error(e.getMessage());
        }
        return SaResult.ok("成功");
    }
}
