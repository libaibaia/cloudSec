package com.common.aws;

import com.common.PasswordGenerator;
import com.domain.Key;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.*;

import java.util.HashMap;
import java.util.Map;

public class Iam {
    /**
     * 创建用户
     * @param key
     * @param userName
     * @param password
     * @return
     */
    public static Map<String, String> createUser(Key key, String userName, String password){
        try {
            IamClient iamClient = IamClient
                    .builder()
                    .region(Region.AWS_GLOBAL)
                    .credentialsProvider(S3.getBaseAuth(key))
                    .build();
            CreateUserRequest request = CreateUserRequest
                    .builder()
                    .userName(userName)
                    .build();
            CreateUserResponse user = iamClient.createUser(request);
            System.out.println(user);
            return createLoginProfile(iamClient,getUser(key).user(),user.user(),password);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }


    /**
     * 添加控制台登录配置
     * @param iamClient
     * @param user
     * @param newUser
     * @param password
     * @return
     */
    public static Map<String,String> createLoginProfile(IamClient iamClient,User user,User newUser,String password){
        Map<String,String> result = new HashMap<>();
        try {
            CreateLoginProfileRequest request = CreateLoginProfileRequest
                    .builder()
                    .userName(newUser.userName())
                    .password(password)
                    .build();
            iamClient.createLoginProfile(request);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("修改用户登录配置失败");
        }
        boolean b = addAttachUserPolicy(iamClient, user, newUser);
        if (b){
            result.put("username",newUser.userName());
            result.put("password",password);
            String[] split = user.arn().split(":");
            result.put("id",split[4]);
        }
        return result;
    }

    /**
     * 添加管理员策略
     * @param iamClient
     * @param user
     * @param newUser
     * @return
     */
    public static boolean addAttachUserPolicy(IamClient iamClient, User user,User newUser){
        try {
            AttachUserPolicyRequest request = AttachUserPolicyRequest
                    .builder()
                    .userName(newUser.userName())
                    .policyArn("arn:aws:iam::aws:policy/AdministratorAccess")
                    .build();
            AttachUserPolicyResponse attachUserPolicyResponse = iamClient.attachUserPolicy(request);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    /*
    获取用户详情
     */
    public static GetUserResponse getUser(Key key){
        IamClient build = IamClient
                .builder()
                .region(Region.AWS_GLOBAL)
                .credentialsProvider(S3.getBaseAuth(key))
                .build();
        return build.getUser();
    }
}
