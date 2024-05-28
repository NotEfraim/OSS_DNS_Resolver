package com.estudio.oss_dns_resolver_v1.presenter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.estudio.oss_dns_resolver_v1.data.logic.dns.DNSResolver;
import com.estudio.oss_dns_resolver_v1.model.KConfiguration;
import com.estudio.oss_dns_resolver_v1.utils.KEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ComponentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

//        List<String> yumingList = Arrays.asList(
//                "https://fdsafdsf.oifeoqu.cn:7390",
//                "https://eat.haolaia.xyz:7391",
//                "https://bbtad.ibm-rational.com:7391",
//                "https://api9.xaxau.com"
//        );

//        KConfiguration kConfiguration = new KConfiguration.Builder()
//                .dnsId("23920")
//                .dnsKey("50r9b4oI")
//                .defaultUrl("https://api9.xaxau.com")
//                .lifecycleOwner(this)
//                .kEventListener(OSS_DNS_APP.kEventListener)
//                .ossList(ossList)
//                .build();


        List<String> ossList = Arrays.asList(
                "https://bato.lllrrq.com:7360/09/error",
                "https://bato09.lllkkj.com/09/error",
                "https://console-k3mo.ks3-cn-shanghai.ksyuncs.com/09/error",
                "http://bato.sudataoss.com/09/error",
                "http://bato.sudataossob.com/09/error"
        );

//        KEventListener kEventListener = new KEventListener() {
//            @Override
//            public void addKConfiguration(KConfiguration configuration) {
//
//            }
//
//            @Override
//            public void onProgressChanged(int progress) {
//
//            }
//
//            @Override
//            public void onAllFailed() {
//
//            }
//        };

//        MainAPP.setKEventListener(kEventListener);

//        KConfiguration kConfiguration = new KConfiguration.Builder()
//                .dnsId("23920")
//                .dnsKey("50r9b4oI")
//                .defaultUrl("https://api9.xaxau.com")
//                .lifecycleOwner(this)
//                .kEventListener(MainAPP.getkEventListener())
//                .ossList(ossList)
//                .build();


//        MainAPP.setKConfiguration(kConfiguration);

//        Log.d("CoreLogic", "init: ");

//        SharedPreferences sharedPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
//        DNSResolver dnsResolver = DNSResolver.getInstance(sharedPreferences, this);
//        viewModel.initViewModel(dnsResolver, sharedPreferences);
//        viewModel.setConfiguration(MainAPP.getKConfiguration());
//        viewModel.initLogic();
    }
}
