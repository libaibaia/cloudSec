package com.common.qiniu.base;

import com.domain.Key;
import com.qiniu.util.Auth;

public class BaseAuth {
    public static Auth getAuth(Key key){
        return Auth.create(key.getSecretid(),key.getSecretkey());
    }
}
