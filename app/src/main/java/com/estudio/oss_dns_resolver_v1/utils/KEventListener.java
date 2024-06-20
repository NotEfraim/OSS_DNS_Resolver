package com.estudio.oss_dns_resolver_v1.utils;

import com.estudio.oss_dns_resolver_v1.model.OSSResponse;
import com.estudio.oss_dns_resolver_v1.model.YumingResponse;

public interface KEventListener {
    default void onProgressChanged(int progress){}
    default void onOSSSuccess(OSSResponse ossResponse){}
    default void onYumingSuccess(YumingResponse yumingResponse){}
    default void onAllFailed() {}
}
