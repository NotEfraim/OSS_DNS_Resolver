package com.estudio.oss_dns_resolver_v1.no_di.network;

public interface ServiceCallBack {
    void onResponse(String response);
    void onFailure(String error);
}
