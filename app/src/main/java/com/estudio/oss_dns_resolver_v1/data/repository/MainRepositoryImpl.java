package com.estudio.oss_dns_resolver_v1.data.repository;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.domain.repository.MainRepository;
import com.estudio.oss_dns_resolver_v1.model.KConfiguration;

public class MainRepositoryImpl implements MainRepository {

    private final CoreLogic coreLogic;

    public MainRepositoryImpl(CoreLogic coreLogic){ this.coreLogic = coreLogic; }

    @Override
    public void setConfiguration(KConfiguration configuration) {
        coreLogic.setConfiguration(configuration);
    }

    @Override
    public void initLogic() {
        coreLogic.initLogic();
    }
}
