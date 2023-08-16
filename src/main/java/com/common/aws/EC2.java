package com.common.aws;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.common.PasswordGenerator;
import com.domain.Instance;
import com.domain.Key;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EC2 {
    private static final List<software.amazon.awssdk.regions.Region> region = software.amazon.awssdk.regions.Region.regions();

    public static Ec2Client getEc2Client(Key key, Region region){
        return Ec2Client.builder()
                .region(region)
                .credentialsProvider(S3.getBaseAuth(key))
                .build();
    }

    public static List<AvailabilityZone> getRegion(Key key){
        List<AvailabilityZone> availabilityZones = new ArrayList<>();
        for (Region region1 : region) {
           try {
               DescribeAvailabilityZonesResponse zonesResponse =
                       getEc2Client(key, region1).describeAvailabilityZones();
               for (AvailabilityZone zone : zonesResponse.availabilityZones()) {
                   availabilityZones.add(zone);
               }
           }catch (Exception e){
               System.out.println(region1.id() + "下未找到可用区域");
           }
        }
        return availabilityZones;
    }

    public static void checkExist(Key key){
        DescribeAvailabilityZonesResponse zonesResponse =
                getEc2Client(key, region.get(0)).describeAvailabilityZones();
        System.out.println(zonesResponse.availabilityZones());
    }




    public static List<Instance> getInstanceLists(Key key){
        boolean done = false;
        String nextToken = null;
        List<Instance> list = new ArrayList<>();
        for (Region region1 : region) {
            try {
                Ec2Client ec2Client = getEc2Client(key, region1);
                do {
                    DescribeInstancesRequest request = DescribeInstancesRequest.builder().maxResults(6).nextToken(nextToken).build();
                    DescribeInstancesResponse response = ec2Client.describeInstances(request);
                    if (!response.reservations().isEmpty()){
                        for (Reservation reservation : response.reservations()) {
                            for (software.amazon.awssdk.services.ec2.model.Instance instance : reservation.instances()) {
                                log.info("获取到实例：" + instance.instanceId());
                                Instance local_instance = new Instance();
                                local_instance.setInstanceId(instance.instanceId());
                                local_instance.setRegion(region1.id());
                                local_instance.setKeyId(key.getId());
                                local_instance.setOriginalKeyPair(instance.keyName());
                                local_instance.setType(StringUtils.isEmpty(instance.platformAsString()) ? "Linux" : instance.platformAsString());
                                local_instance.setOsName(instance.instanceType().name());
                                local_instance.setIsCommand("null");
                                local_instance.setIp(instance.publicIpAddress());
                                local_instance.setKeyName(key.getName());
                                list.add(local_instance);
                            }
                        }
                    }
                    nextToken = response.nextToken();
                } while (nextToken != null);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return list;
    }



}
