package com.common.qiniu.qvm;

public enum QvmBaseUrl {
    QvmRegionListUrl("/v1/cm/region", "GET"),
    QvmInstanceList("/v1/vm/instance", "GET"),
    ImportKeyPairUrl("/v1/vm/keypair", "POST"),
    BindKeyPair("/v1/vm/keypair/%s/attach", "POST"),
    RebootInstance("/v1/vm/instance/%s/reboot","POST");
    private String url;
    private String method;

    private QvmBaseUrl(String url, String method) {

        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

}
