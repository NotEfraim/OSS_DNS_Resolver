package com.estudio.oss_dns_resolver_v1.presenter;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DNSResolver;
import com.estudio.oss_dns_resolver_v1.data.utils.SharePrefManager;
import com.estudio.oss_dns_resolver_v1.domain.repository.MainRepository;
import com.estudio.oss_dns_resolver_v1.model.KConfiguration;
import com.estudio.oss_dns_resolver_v1.no_di.ComponentProvider;
import com.estudio.oss_dns_resolver_v1.no_di.network.HttpService;
import com.estudio.oss_dns_resolver_v1.no_di.network.HttpServiceBuilder;
import com.estudio.oss_dns_resolver_v1.no_di.network.ServiceCallBack;

public class MainAPP extends Application {

    /** Core Configuration */
    private static MainRepository repository;
    private static DNSResolver dnsResolver;
    private static ComponentProvider componentProvider;
    private static CoreLogic coreLogic;
    private static SharePrefManager sharePrefManager;

    public static void initViewModel (
            DNSResolver resolver,
            SharedPreferences preferences

    ) {
        dnsResolver = resolver;
        sharePrefManager = new SharePrefManager(preferences);

        /* Instance of ComponentProvider */
        componentProvider = ComponentProvider.getInstance(
                dnsResolver,
                preferences
        );

        /* Instance of CoreLogic */
        coreLogic =  componentProvider.provideCoreLogic();

        /* Instance of MainRepository */
        repository = componentProvider.provideMainRepository(coreLogic);
    }

    public static String getFinalURL() {
        return sharePrefManager.GET_RESOLVED_YUMING();
    }

    public static String getFinalOSS() {
        return sharePrefManager.GET_RESOLVED_OSS();
    }

    public static String getHeaderHost() {
        return sharePrefManager.GET_HEADER_HOST();
    }

    public static void setConfiguration(KConfiguration configuration) {
        repository.setConfiguration(configuration);
    }

    public static void initLogic() {
        repository.initLogic();
    }

}
