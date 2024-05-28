package com.estudio.oss_dns_resolver_v1.domain.repository;


import androidx.lifecycle.LiveData;

import com.estudio.oss_dns_resolver_v1.model.KConfiguration;
import com.estudio.oss_dns_resolver_v1.model.OSSResponse;
import com.estudio.oss_dns_resolver_v1.model.YumingResponse;

import java.util.List;

public interface MainRepository {
    void setConfiguration(KConfiguration configuration);
    void initLogic();
}
