package com.service.impl.tencent;


import com.common.tencent.user.UserPermissionList;
import com.domain.Key;
import com.service.KeyService;
import com.tencentcloudapi.cam.v20190116.models.AttachPolicyInfo;
import com.tencentcloudapi.cam.v20190116.models.ListAttachedUserPoliciesResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PermissionService {

    @Resource
    private KeyService keyService;

    public AttachPolicyInfo[] getUserPermList(Integer id,Integer loginId){
        Key byId = keyService.getById(id);
        if (loginId.equals(byId.getCreateById())){
            UserPermissionList userPermissionList = new UserPermissionList(byId);
            ListAttachedUserPoliciesResponse listAttachedUserPoliciesResponse = userPermissionList.listAttachedUserPolicies();
            return listAttachedUserPoliciesResponse.getList();
        }
        return null;
    }

}
