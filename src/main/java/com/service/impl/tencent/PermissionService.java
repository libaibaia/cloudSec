package com.service.impl.tencent;


import com.aliyun.ram20150501.models.ListPoliciesForUserResponseBody;
import com.common.LogAnnotation;
import com.common.Type;
import com.common.aliyun.User;
import com.common.tencent.user.UserPermissionList;
import com.domain.Key;
import com.service.KeyService;
import com.tencentcloudapi.cam.v20190116.models.AttachPolicyInfo;
import com.tencentcloudapi.cam.v20190116.models.ListAttachedUserPoliciesResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermissionService {

    @Resource
    private KeyService keyService;

    @LogAnnotation(title = "获取用户权限列表")
    public List<Map> getUserPermList(Integer id, Integer loginId) throws Exception {
        Map<String,String> res = new HashMap<>();
        ArrayList<Map> objects = new ArrayList<>();
        Key byId = keyService.getById(id);
        Type t = Type.valueOf(byId.getType());
        if (loginId.equals(byId.getCreateById())){
            switch (t){
                case Tencent:
                UserPermissionList userPermissionList = new UserPermissionList(byId);
                ListAttachedUserPoliciesResponse response = userPermissionList.listAttachedUserPolicies();
                    for (AttachPolicyInfo attachPolicyInfo : response.getList()) {
                        res.put("policyName",attachPolicyInfo.getPolicyName());
                        res.put("remark",attachPolicyInfo.getRemark());
                    }
                    break;
                case AliYun:
                    ListPoliciesForUserResponseBody.ListPoliciesForUserResponseBodyPolicies responseBodyPolicies = User.ListPoliciesForUser(byId);
                    for (ListPoliciesForUserResponseBody.ListPoliciesForUserResponseBodyPoliciesPolicy re : responseBodyPolicies.policy) {
                        res.put("policyName",re.getPolicyName());
                        res.put("remark",re.getDescription());
                    }
                    break;
            }
            objects.add(res);
        }
        return objects;
    }

}
