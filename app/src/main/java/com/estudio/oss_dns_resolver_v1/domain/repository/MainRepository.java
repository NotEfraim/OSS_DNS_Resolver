package com.estudio.oss_dns_resolver_v1.domain.repository;


import com.estudio.oss_dns_resolver_v1.model.KConfiguration;

public interface MainRepository {
    void setConfiguration(KConfiguration configuration);
    void initLogic();
}
