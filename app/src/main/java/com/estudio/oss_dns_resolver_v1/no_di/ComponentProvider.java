package com.estudio.oss_dns_resolver_v1.no_di;

import android.content.SharedPreferences;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.default_url.BasicDefaultURL;
import com.estudio.oss_dns_resolver_v1.data.logic.default_url.EncryptedDefaultURL;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DNSResolver;
import com.estudio.oss_dns_resolver_v1.data.logic.oss.BasicOSSLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.oss.EncryptedOSSLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.yuming.BasicYuming;
import com.estudio.oss_dns_resolver_v1.data.logic.yuming.EncryptedYuming;
import com.estudio.oss_dns_resolver_v1.data.repository.MainRepositoryImpl;
import com.estudio.oss_dns_resolver_v1.data.utils.ApiRawCall;
import com.estudio.oss_dns_resolver_v1.domain.repository.MainRepository;

public class ComponentProvider {

    private static DNSResolver dnsResolver;
    private static SharedPreferences sharedPreferences;
    private static ComponentProvider instance;
    private static ApiService apiService;


    public static ComponentProvider getInstance(
            ApiService service,
            DNSResolver resolver ,
            SharedPreferences preferences
    ) {
        apiService = service;
        dnsResolver = resolver;
        sharedPreferences = preferences;

        if (instance == null) {
            instance = new ComponentProvider();
        }
        return instance;
    }

    public EncryptedOSSLogic provideEncryptedOSSLogic() {
        return EncryptedOSSLogic.getInstance(dnsResolver ,provideApiRawCall(), sharedPreferences);
    }

    public BasicOSSLogic provideBasicOSSLogic(){
        return BasicOSSLogic.getInstance(provideApiRawCall(), sharedPreferences);
    }

    public EncryptedYuming provideEncryptedYuming() {
        return EncryptedYuming.getInstance(dnsResolver, provideApiRawCall(), sharedPreferences);
    }

    public BasicYuming provideBasicYuming(){
        return BasicYuming.getInstance(provideApiRawCall(), sharedPreferences);
    }

    public EncryptedDefaultURL provideEncryptedDefaultURL() {
        return EncryptedDefaultURL.getInstance(dnsResolver, provideApiRawCall(), sharedPreferences);
    }

    public BasicDefaultURL provideBasicDefaultURL() {
        return BasicDefaultURL.getInstance(provideApiRawCall(), sharedPreferences);
    }

    public CoreLogic provideCoreLogic() {
        return CoreLogic.getInstance(
                sharedPreferences,
                provideEncryptedOSSLogic(),
                provideBasicOSSLogic(),
                provideEncryptedYuming(),
                provideBasicYuming(),
                provideEncryptedDefaultURL(),
                provideBasicDefaultURL()
        );
    }

    public ApiRawCall provideApiRawCall() {
        return ApiRawCall.getInstance(apiService, sharedPreferences);
    }

    public MainRepository provideMainRepository(
            CoreLogic coreLogic
    ){
        return new MainRepositoryImpl(coreLogic);
    }

}
