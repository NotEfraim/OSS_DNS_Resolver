package com.estudio.oss_dns_resolver_v1.presenter;

import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DNSResolver;
import com.estudio.oss_dns_resolver_v1.data.repository.MainRepositoryImpl;
import com.estudio.oss_dns_resolver_v1.domain.repository.MainRepository;
import com.estudio.oss_dns_resolver_v1.model.KConfiguration;
import com.estudio.oss_dns_resolver_v1.no_di.ComponentProvider;


public class MainViewModel extends ViewModel {

    private MainRepository repository;
    private DNSResolver dnsResolver;
    private ComponentProvider componentProvider;
    private CoreLogic coreLogic;

    public void initViewModel (
            DNSResolver resolver,
            SharedPreferences preferences

    ) {
        dnsResolver = resolver;

        /* Instance of ComponentProvider */
        componentProvider = ComponentProvider.getInstance(
                dnsResolver,
                preferences
        );

    }

    public void setConfiguration(KConfiguration configuration) {
        repository.setConfiguration(configuration);
    }

    public void initLogic() {
        /* Instance of CoreLogic */
        coreLogic =  componentProvider.provideCoreLogic();

        /* Instance of MainRepository */
        repository = new MainRepositoryImpl(coreLogic);

        repository.initLogic();
    }

}
