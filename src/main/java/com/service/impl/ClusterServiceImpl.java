package com.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.tencent.product.VPC;
import com.common.tencent.product.tke.TKE;
import com.domain.Cluster;
import com.domain.Key;
import com.service.ClusterService;
import com.mapper.ClusterMapper;
import com.service.KeyService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.tke.v20180525.models.DescribeClusterEndpointVipStatusResponse;
import com.tencentcloudapi.tke.v20180525.models.DescribeClusterEndpointsResponse;
import com.tencentcloudapi.vpc.v20170312.models.CreateDefaultSecurityGroupResponse;
import com.tencentcloudapi.vpc.v20170312.models.CreateSecurityGroupWithPoliciesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【cluster】的数据库操作Service实现
* @createDate 2023-07-29 20:26:23
*/
@Service
@Slf4j
public class ClusterServiceImpl extends ServiceImpl<ClusterMapper, Cluster>
    implements ClusterService{
    @Autowired
    private KeyService keyService;

    @Override
    public String getKubeConfig(Integer clusterId){
        Cluster cluster = getById(clusterId);
        Key key = keyService.getById(cluster.getKeyId());
        return TKE.getEKSClustersKubeConfig(key,cluster);
    }

    @Override
    public String openEndpoint(Integer clusterId){
        Cluster cluster = getById(clusterId);
        Key key = keyService.getById(cluster.getKeyId());
        try {
            if (StrUtil.isBlank(cluster.getEndpointSecurityGroup())){
                CreateDefaultSecurityGroupResponse securityGroup = VPC.createSecurityGroup(key, cluster.getRegion());
                cluster.setEndpointSecurityGroup(securityGroup.getSecurityGroup().getSecurityGroupId());
                updateById(cluster);
                TKE.openEndpoint(key,cluster);
                //监控开启是否成功，如果成功更新集群信息
                //此处腾讯云存在bug，返回状态不正确，就先休眠更新状态吧，前端可以手动更新
                checkStatus(cluster,key);
            }else {
                String s = TKE.modifyClusterEndpointSP(key, cluster);
            }
            return "执行成功,请稍后刷新";
        } catch (TencentCloudSDKException e) {
            log.error(e.getMessage());
            return "打开失败，原因：" + e.getMessage();
        }
    }

    @Override
    public void updateStatus(Integer clusterId) {
        Cluster cluster = getById(clusterId);
        Key key = keyService.getById(cluster.getKeyId());
        TKE.modifyClusterEndpointSP(key, cluster);
        DescribeClusterEndpointsResponse clusterEndpoints = TKE.getClusterEndpoints(key, cluster);
        if (clusterEndpoints != null) {
            cluster.setEndpointSecurityGroup(clusterEndpoints.getSecurityGroup());
            String stringBuilder = "外网地址:" + clusterEndpoints.getClusterExternalEndpoint() +
                    "\n" +
                    "内网地址:" + clusterEndpoints.getClusterIntranetEndpoint();
            cluster.setEndpointInfo(stringBuilder);
            updateById(cluster);
        }
    }

    private void checkStatus(Cluster cluster,Key key){
        new Thread(() -> {
            try {
                Thread.sleep(30000);
                TKE.modifyClusterEndpointSP(key, cluster);
                DescribeClusterEndpointsResponse clusterEndpoints = TKE.getClusterEndpoints(key, cluster);
                if (clusterEndpoints != null) {
                    cluster.setEndpointSecurityGroup(clusterEndpoints.getSecurityGroup());
                    String stringBuilder = "外网地址:" + clusterEndpoints.getClusterExternalEndpoint() +
                            "\n" +
                            "内网地址:" + clusterEndpoints.getClusterIntranetEndpoint();
                    cluster.setEndpointInfo(stringBuilder);
                    updateById(cluster);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }).start();
    }
    /*
    创建集群接管命令
    生成sh脚本，下载kubectl/kubeconfig及安装
     */
    @Override
    public String createCmd(Integer id){
        Cluster cluster = getById(id);
        String clusterVersion = cluster.getClusterVersion();
        Key key = keyService.getById(cluster.getKeyId());
        String kubeConfig = getKubeConfig(id);
        DescribeClusterEndpointsResponse clusterEndpoints = TKE.getClusterEndpoints(key, cluster);
        String ip = "";
        if (clusterEndpoints != null) {
            System.out.println(clusterEndpoints.getClusterIntranetEndpoint());
            ip = "https://" + clusterEndpoints.getClusterExternalEndpoint();
        }
        return "curl -LO \"https://storage.googleapis.com/kubernetes-release/release/v" + clusterVersion + "/bin/linux/amd64/kubectl\"\n" +
                "mv kubectl /usr/local/bin/\n" +
                "cat <<EOF > kubeconfig\n" +
                kubeConfig +
                "EOF\n" +
                 "sed -i 's#https://.*#" + ip +"#' kubeconfig\n" +
                "chmod +x /usr/local/bin/kubectl\n" +
                "export KUBECONFIG=$(pwd)/kubeconfig\n\n" +
                "kubectl get nodes\n";
    }

    @Override
    public void removeByKeyID(Integer id) {
        QueryWrapper<Cluster> queryWrapper = new
                QueryWrapper<Cluster>().eq("key_id", id);
        this.getBaseMapper().delete(queryWrapper);
    }


}




