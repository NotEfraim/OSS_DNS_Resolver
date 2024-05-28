package com.estudio.oss_dns_resolver_v1.presenter;

import android.app.Application;
import android.content.SharedPreferences;


import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DNSResolver;
import com.estudio.oss_dns_resolver_v1.data.repository.MainRepositoryImpl;
import com.estudio.oss_dns_resolver_v1.domain.repository.MainRepository;
import com.estudio.oss_dns_resolver_v1.model.KConfiguration;
import com.estudio.oss_dns_resolver_v1.no_di.ComponentProvider;
import com.estudio.oss_dns_resolver_v1.no_di.RetrofitUtil;
import com.estudio.oss_dns_resolver_v1.utils.KEventListener;

public class MainAPP extends Application {

    /** Core Configuration */
    private static MainRepository repository;
    private static DNSResolver dnsResolver;
    private static ComponentProvider componentProvider;
    private static CoreLogic coreLogic;
    private static RetrofitUtil retrofitUtil;


    public static void initViewModel (
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
        repository = componentProvider.provideMainRepository(coreLogic);
    }


    public static void setConfiguration(KConfiguration configuration) {
        repository.setConfiguration(configuration);
    }

    public static void initLogic() {
        repository.initLogic();
    }


}
