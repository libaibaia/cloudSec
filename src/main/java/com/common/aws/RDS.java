package com.common.aws;

import com.domain.DatabasesInstance;
import com.domain.Key;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.DBInstance;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesResponse;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class RDS {

    private static final List<Region> regions = Region.regions();

    public static List<DatabasesInstance> getRdsLists(Key key){
        List<DatabasesInstance> result = new ArrayList<>();
        for (Region region : regions) {
            RdsClient rdsClient = RdsClient.builder()
                    .region(region)
                    .credentialsProvider(S3.getBaseAuth(key))
                    .build();
            try {
                DescribeDbInstancesResponse response = rdsClient.describeDBInstances();
                List<DBInstance> instanceList = response.dbInstances();
                for (DBInstance dbInstance : instanceList) {
                    log.info("获取数据库" + dbInstance.dbName());
                    DatabasesInstance databasesInstance = new DatabasesInstance();
                    databasesInstance.setDomain(dbInstance.endpoint().address());
                    databasesInstance.setPort(dbInstance.endpoint().port().toString());
                    databasesInstance.setRegion(region.id());
                    databasesInstance.setType(dbInstance.engine());
                    databasesInstance.setInstanceId(dbInstance.dbInstanceIdentifier());
                    databasesInstance.setKeyId((Integer) key.getId());
                    databasesInstance.setInstanceName(dbInstance.dbName());
                    databasesInstance.setKeyName(key.getName());
                    result.add(databasesInstance);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

}
