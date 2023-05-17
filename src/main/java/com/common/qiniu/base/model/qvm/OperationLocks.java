package com.common.qiniu.base.model.qvm;

import java.util.List;

public class OperationLocks {
    private List<String> lock_reason;

    public List<String> getLock_reason() {
        return lock_reason;
    }

    public void setLock_reason(List<String> lock_reason) {
        this.lock_reason = lock_reason;
    }
}
