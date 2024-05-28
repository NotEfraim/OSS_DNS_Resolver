package com.estudio.oss_dns_resolver_v1.presenter;

import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DNSResolver;
import com.estudio.oss_dns_resolver_v1.data.logic.oss.EncryptedOSSLogic;
import com.estudio.oss_dns_resolver_v1.data.repository.MainRepositoryImpl;
import com.estudio.oss_dns_resolver_v1.domain.repository.MainRepository;
import com.estudio.oss_dns_resolver_v1.model.KConfiguration;
import com.estudio.oss_dns_resolver_v1.no_di.ApiService;
import com.estudio.oss_dns_resolver_v1.no_di.ComponentProvider;
import com.estudio.oss_dns_resolver_v1.no_di.RetrofitUtil;


public class MainViewModel extends ViewModel {

    private MainRepository repository;
    private DNSResolver dnsResolver;
    private ComponentProvider componentProvider;
    private CoreLogic coreLogic;
    private RetrofitUtil retrofitUtil;

    public void initViewModel (
            DNSResolver resolver,
            SharedPreferences preferences

    ) {
        dnsResolver = resolver;

        /* Instance of RetrofitUtil */
        retrofitUtil = RetrofitUtil.getInstance(preferences);

        /* Instance of ComponentProvider */
        componentProvider = ComponentProvider.getInstance(
                retrofitUtil.apiService(),
                dnsResolver,
                preferences
        );

        /* Instance of CoreLogic */
        coreLogic =  componentProvider.provideCoreLogic();

        /* Instance of MainRepository */
        repository = new MainRepositoryImpl(coreLogic);
    }

    public void setConfiguration(KConfiguration configuration) {
        repository.setConfiguration(configuration);
    }

    public void initLogic() {
        repository.initLogic();
    }

}
