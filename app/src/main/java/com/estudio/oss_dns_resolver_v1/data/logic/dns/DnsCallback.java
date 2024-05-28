package com.estudio.oss_dns_resolver_v1.data.logic.dns;

public interface DnsCallback {
    void onFinishEncryption(String result);
    void onFailEncryption();
}
