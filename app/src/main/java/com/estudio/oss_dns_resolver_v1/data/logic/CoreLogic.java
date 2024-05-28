package com.estudio.oss_dns_resolver_v1.data.logic;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.estudio.oss_dns_resolver_v1.data.logic.default_url.BasicDefaultURL;
import com.estudio.oss_dns_resolver_v1.data.logic.default_url.EncryptedDefaultURL;
import com.estudio.oss_dns_resolver_v1.data.logic.oss.BasicOSSLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.oss.EncryptedOSSLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.yuming.BasicYuming;
import com.estudio.oss_dns_resolver_v1.data.logic.yuming.EncryptedYuming;
import com.estudio.oss_dns_resolver_v1.model.KConfiguration;
import com.estudio.oss_dns_resolver_v1.model.OSSResponse;
import com.estudio.oss_dns_resolver_v1.model.YumingResponse;
import com.estudio.oss_dns_resolver_v1.utils.Constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoreLogic {

    public static final String TAG = CoreLogic.class.getSimpleName();

    /** Commons */
    private LifecycleOwner lifecycleOwner;
    private List<String> ossList = Collections.emptyList();
    private List<String> yumingList = Collections.emptyList();
    private String defaultUrl = "";
    private static SharedPreferences sharedPreferences;
    private static CoreLogic instance;


    /** Logic Managers */
    private static EncryptedOSSLogic encryptedOSSLogic;
    private static BasicOSSLogic basicOSSLogic;
    private static EncryptedYuming encryptedYumingLogic;
    private static BasicYuming basicYumingLogic;
    private static EncryptedDefaultURL encryptedDefaultURL;
    private static BasicDefaultURL basicDefaultURL;

    /** Live Data's */
    private static LiveData<OSSResponse> liveDataEncryptedOSS;
    private static LiveData<OSSResponse> liveDataBasicOSS;
    private static LiveData<YumingResponse> liveDataEncryptedYuming;
    private static LiveData<YumingResponse> liveDataBasicYuming;
    private static LiveData<YumingResponse> liveDataEncryptedDefaultURL;
    private static LiveData<YumingResponse> liveDataBasicDefaultURL;

    private CoreLogic(){

    }

    public static CoreLogic getInstance(
            SharedPreferences   preferences,
            EncryptedOSSLogic   a,
            BasicOSSLogic       b,
            EncryptedYuming     c,
            BasicYuming         d,
            EncryptedDefaultURL e,
            BasicDefaultURL     f
    ) {
        sharedPreferences =     preferences;
        encryptedOSSLogic =     a;
        basicOSSLogic =         b;
        encryptedYumingLogic =  c;
        basicYumingLogic =      d;
        encryptedDefaultURL =   e;
        basicDefaultURL =       f;

        /* Live Data */
        liveDataEncryptedOSS = encryptedOSSLogic.initModel;
        liveDataBasicOSS = basicOSSLogic.initModel;
        liveDataEncryptedYuming = encryptedYumingLogic.response;
        liveDataBasicYuming = basicYumingLogic.response;
        liveDataEncryptedDefaultURL = encryptedDefaultURL.response;
        liveDataBasicDefaultURL = basicDefaultURL.response;

        if (instance == null) {
            instance = new CoreLogic();
        }

        return instance;
    }

    /* Configuration */
    public void setConfiguration(KConfiguration configuration) {
        this.ossList = configuration.getOssList();
        this.yumingList = configuration.getYumingList();
        this.defaultUrl = configuration.getDefaultUrl();
        this.lifecycleOwner = configuration.getLifecycleOwner();

        /* Set DNS Key and ID */
        sharedPreferences.edit().putString(Constants.DNS_KEY, configuration.getDnsKey()).apply();
        sharedPreferences.edit().putString(Constants.DNS_ID_KEY, configuration.getDnsId()).apply();
    }
    public void setOssList(List<String> ossList){
        this.ossList = ossList;
    }

    public void setYumingList(List<String> yumingList){
        this.yumingList = yumingList;
    }

    public void setDefaultUrl(String url){
        this.defaultUrl = url;
    }

    public void initLogic() {
        USE_ENCRYPTED_OSS();
    }

    private void USE_ENCRYPTED_OSS(){
        /* Start the Encrypted OSS Logic */
        encryptedOSSLogic.start(ossList);

        /* Observe the response */
        liveDataEncryptedOSS.observe( this.lifecycleOwner , response -> {
            if(response == null) return;

            /* Get the response */
            if(response.isSuccess()){

                /* Get the yuming list */
                yumingList = Arrays.asList(response.getData().getYuming().split(","));

                /* Encrypt the yuming list */
                USE_ENCRYPTED_YUMING();
            } else {

                /* If the response is not success, use basic */
                USE_BASIC_OSS();

            }
        });
    }

    private void USE_BASIC_OSS(){

        /* Start the Encrypted OSS Logic */
        basicOSSLogic.start(ossList);

        /* Observe the response */
        liveDataBasicOSS.observe( this.lifecycleOwner, response -> {
            if(response == null) return;

            /* Get the response */
            if(response.isSuccess()){

                /* Get the yuming list */
                yumingList = Arrays.asList(response.getData().getYuming().split(","));

                /* Encrypt the yuming list */
                USE_ENCRYPTED_YUMING();
            } else {

                /* If the basic oss response is not success, use default url */
                USE_ENCRYPTED_DEFAULT_URL();
            }

        });

    }

    private void USE_ENCRYPTED_YUMING(){

        /* Start the Encrypted Yuming Logic */
        encryptedYumingLogic.start(yumingList);

        /* Observe the response */
        liveDataEncryptedYuming.observe( this.lifecycleOwner, response -> {
            if(response == null) return;

            /* Get the response */
            if(response.isSuccess()){

                //  Todo save the final url and pass response
                Log.d(TAG, "Final URL: " + response.getFinalUrl());

            } else {
                /* If the encrypted yuming response is not success, use basic */
                USE_BASIC_YUMING();
            }
        });
    }

    private void USE_BASIC_YUMING(){
        /* Start the Basic Yuming Logic */
        basicYumingLogic.start(yumingList);

        /* Observe the response */
        liveDataBasicYuming.observe( this.lifecycleOwner, response -> {
            if(response == null) return;

            /* Get the response */
            if(response.isSuccess()){
                //  Todo save the final url and pass response
            } else {

                /* If the basic yuming response is not success, use default url */
                USE_ENCRYPTED_DEFAULT_URL();
            }
        });
    }

    private void USE_ENCRYPTED_DEFAULT_URL() {

        /* Start the Encrypted OSS Logic */
        encryptedDefaultURL.start(defaultUrl);

        /* Observe the response */
        liveDataEncryptedDefaultURL.observe( this.lifecycleOwner, response -> {
            if(response == null) return;

            /* Get the response */
            if(response.isSuccess()){
                // Todo save the final url and pass response
            } else {

                /* If the encrypted default url response is not success, use basic default url */
                USE_BASIC_DEFAULT_URL();

            }
        });
    }

    private void USE_BASIC_DEFAULT_URL() {
        /* Start the Encrypted OSS Logic */
        basicDefaultURL.start(defaultUrl);

        /* Observe the response */
        liveDataBasicDefaultURL.observe( this.lifecycleOwner, response -> {
            if(response == null) return;

            /* Get the response */
            if(response.isSuccess()){
                // Todo save the final url and pass response
            } else {
                // Todo show error
            }
        });
    }
}
