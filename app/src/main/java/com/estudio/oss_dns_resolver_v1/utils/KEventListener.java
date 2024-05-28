package com.estudio.oss_dns_resolver_v1.utils;

import com.estudio.oss_dns_resolver_v1.model.KConfiguration;

public interface KEventListener {
    void addKConfiguration(KConfiguration configuration);
    void onProgressChanged(int progress);
    void onAllFailed();
}
