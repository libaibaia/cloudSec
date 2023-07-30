package com.common.tencent.product.tke;

import com.common.tencent.product.Base;
import com.domain.Key;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.region.v20220627.models.RegionInfo;
import com.tencentcloudapi.tke.v20180525.TkeClient;
import com.tencentcloudapi.tke.v20180525.models.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class TKE {

    private static final HttpProfile httpProfile = new HttpProfile();
    private static final ClientProfile clientProfile = new ClientProfile();
    public static final String product = "tke";
    private static final String network = "{\"InternetAccessible\":{\"InternetChargeType\":\"TRAFFIC_POSTPAID_BY_HOUR\",\"InternetMaxBandwidthOut\":10}}";

    /**
     * 获取集群列表
     *
     * @param key
     * @return
     */
    public static List<com.domain.Cluster> getEKSClustersLists(Key key){
        List<com.domain.Cluster> clusters = new ArrayList<>();
        try {
            RegionInfo[] tkeRegionLists = Base.getRegionList(Base.createCredential(key), product);
            for (RegionInfo regionInfo : tkeRegionLists) {
                httpProfile.setEndpoint("tke.tencentcloudapi.com");
                clientProfile.setHttpProfile(httpProfile);
                long limit = 100L;
                long offset = 0;
                DescribeClustersRequest req = new DescribeClustersRequest();
                req.setLimit(limit);
                while (true){
                    DescribeClustersResponse resp = getTkeClient(key,regionInfo.getRegion()).DescribeClusters(req);
                    if (resp.getClusters().length >= 1){
                        clusters.addAll(createDomain(resp.getClusters(), key, regionInfo));
                        offset += limit;
                        req.setOffset(offset);
                    }else break;
                }
            }
        } catch (Exception e) {
           log.error(e.getMessage());
        }
        return clusters;
    }

    //获取kubeConfig
    public static String getEKSClustersKubeConfig(Key key, com.domain.Cluster cluster){
        httpProfile.setEndpoint("tke.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        try {
            DescribeClusterKubeconfigRequest req = new DescribeClusterKubeconfigRequest();
            req.setClusterId(cluster.getClusterId());
            DescribeClusterKubeconfigResponse resp = getTkeClient(key,cluster.getRegion()).DescribeClusterKubeconfig(req);
            return resp.getKubeconfig();
        } catch (TencentCloudSDKException e) {
            log.error(e.getMessage());
            return "";
        }
    }


    /***
     * 获取集群访问端点
     * @param key
     * @param cluster
     * @return
     */
    public static DescribeClusterEndpointsResponse getClusterEndpoints(Key key, com.domain.Cluster cluster){
        httpProfile.setEndpoint("tke.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        TkeClient tkeClient = getTkeClient(key, cluster.getRegion());
        DescribeClusterEndpointsRequest req = new DescribeClusterEndpointsRequest();
        req.setClusterId(cluster.getClusterId());
        try {
            DescribeClusterEndpointsResponse resp = tkeClient.DescribeClusterEndpoints(req);
            System.out.println(resp.getClusterExternalEndpoint());
            return resp;
        } catch (TencentCloudSDKException e) {
            log.error(e.getMessage());
        }
        return null;
    }



    private static TkeClient getTkeClient(Key key, String region){
        Credential credential = Base.createCredential(key);
        return new TkeClient(credential,region, clientProfile);
    }

    //转换为数据库格式
    private static List<com.domain.Cluster> createDomain(Cluster[] clusters, Key key, RegionInfo regionInfo){
        List<com.domain.Cluster> res = new ArrayList<>();
        for (Cluster cluster : clusters) {
            com.domain.Cluster newCluster = new com.domain.Cluster();
            newCluster.setClusterId(cluster.getClusterId());
            newCluster.setClusterName(cluster.getClusterName());
            newCluster.setClusterOs(cluster.getClusterOs());
            newCluster.setClusterDescription(cluster.getClusterDescription());
            newCluster.setClusterType(cluster.getClusterType());
            newCluster.setClusterVersion(cluster.getClusterVersion());
            newCluster.setContainerRuntime(cluster.getContainerRuntime());
            newCluster.setKeyId(key.getId());
            newCluster.setKeyName(key.getName());
            newCluster.setRegion(regionInfo.getRegion());
            newCluster.setNetworkInfo(getNetWorkStr(cluster.getClusterNetworkSettings()));
            DescribeClusterEndpointsResponse clusterEndpoints = getClusterEndpoints(key,newCluster);
            if (clusterEndpoints != null) {
                newCluster.setEndpointSecurityGroup(clusterEndpoints.getSecurityGroup());
                String stringBuilder = "外网地址:" + clusterEndpoints.getClusterExternalEndpoint() +
                        "\n" +
                        "内网地址:" + clusterEndpoints.getClusterIntranetEndpoint();
                newCluster.setEndpointInfo(stringBuilder);
            }
            res.add(newCluster);
        }
        return res;
    }

    /**
     * 添加白名单,需要开通外网访问
     * @param key
     * @param cluster
     * @return
     */
    public static String modifyClusterEndpointSP(Key key, com.domain.Cluster cluster){
        TkeClient tkeClient = getTkeClient(key, cluster.getRegion());
        httpProfile.setEndpoint("tke.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        ModifyClusterEndpointSPRequest req = new ModifyClusterEndpointSPRequest();
        req.setClusterId(cluster.getClusterId());
        String[] securityPolicies = {"0.0.0.0/0"};
        req.setSecurityPolicies(securityPolicies);
        req.setSecurityGroup(cluster.getEndpointSecurityGroup());
        try {
            ModifyClusterEndpointSPResponse resp = tkeClient.ModifyClusterEndpointSP(req);
            return "成功";
        } catch (TencentCloudSDKException e) {
            log.error(e.getMessage());
            return "失败，原因:" + e.getMessage();
        }
    }

    public static DescribeClusterEndpointVipStatusResponse getClusterEndpointVipStatus(Key key, com.domain.Cluster cluster) throws TencentCloudSDKException {
        httpProfile.setEndpoint("tke.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        TkeClient tkeClient = getTkeClient(key, cluster.getRegion());
        DescribeClusterEndpointVipStatusRequest req = new DescribeClusterEndpointVipStatusRequest();
        req.setClusterId(cluster.getClusterId());
        return tkeClient.DescribeClusterEndpointVipStatus(req);
    }

    /***
     * 打开外网访问
     * @param key
     * @param cluster
     * @throws TencentCloudSDKException
     */

    public static void openEndpoint(Key key, com.domain.Cluster cluster) throws TencentCloudSDKException {
        httpProfile.setEndpoint("tke.tencentcloudapi.com");
        clientProfile.setHttpProfile(httpProfile);
        TkeClient tkeClient = getTkeClient(key, cluster.getRegion());
        CreateClusterEndpointRequest req = new CreateClusterEndpointRequest();
        req.setClusterId(cluster.getClusterId());
        req.setIsExtranet(true);
        req.setSecurityGroup(cluster.getEndpointSecurityGroup());
        req.setExtensiveParameters(network);
        tkeClient.CreateClusterEndpoint(req);
    }

    private static String getNetWorkStr(ClusterNetworkSettings clusterNetworkSettings){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(clusterNetworkSettings);
        } catch (JsonProcessingException e) {
           return "";
        }
    }

}
