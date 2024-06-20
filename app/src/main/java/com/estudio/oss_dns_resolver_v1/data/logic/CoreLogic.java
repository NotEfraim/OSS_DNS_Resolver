package com.estudio.oss_dns_resolver_v1.data.logic;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.estudio.oss_dns_resolver_v1.data.logic.default_url.BasicDefaultURL;
import com.estudio.oss_dns_resolver_v1.data.logic.default_url.EncryptedDefaultURL;
import com.estudio.oss_dns_resolver_v1.data.logic.oss.BasicOSSLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.oss.EncryptedOSSLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.yuming.BasicYuming;
import com.estudio.oss_dns_resolver_v1.data.logic.yuming.EncryptedYuming;
import com.estudio.oss_dns_resolver_v1.data.utils.SharePrefManager;
import com.estudio.oss_dns_resolver_v1.model.KConfiguration;
import com.estudio.oss_dns_resolver_v1.model.OSSResponse;
import com.estudio.oss_dns_resolver_v1.model.YumingResponse;
import com.estudio.oss_dns_resolver_v1.utils.Constants;
import com.estudio.oss_dns_resolver_v1.utils.KEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoreLogic {

    public static final String TAG = CoreLogic.class.getSimpleName();

    /** Commons */
    private LifecycleOwner lifecycleOwner;
    private static List<String> ossList = Collections.emptyList();
    private static List<String> yumingList = Collections.emptyList();
    private String defaultUrl = "";
    private static SharedPreferences sharedPreferences;
    private static CoreLogic instance;
    private static KEventListener kEventListener;
    private static SharePrefManager sharePrefManager;


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
    private static final MutableLiveData<Integer> _liveDataProgress = new MutableLiveData<>(0);
    private static final LiveData<Integer> liveDataProgress = _liveDataProgress;

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
            sharePrefManager = new SharePrefManager(sharedPreferences);
            instance = new CoreLogic();
        }

        return instance;
    }

    /* Configuration */
    public void setConfiguration(KConfiguration configuration) {
        this.defaultUrl = configuration.getDefaultUrl();
        this.lifecycleOwner = configuration.getLifecycleOwner();
        ossList = configuration.getOssList();
        yumingList = configuration.getYumingList();
        kEventListener = configuration.getKEventListener();

        /* Set DNS Key and ID */
        sharePrefManager.SET_DNS_ID(configuration.getDnsId());
        sharePrefManager.SET_DNS_KEY(configuration.getDnsKey());

        /* Set AGENT Key and VERSION */
        sharePrefManager.SET_AGENT(configuration.getAgent());
        sharePrefManager.SET_VERSION(configuration.getVersion());
    }

    public void initLogic() {
        USE_ENCRYPTED_OSS();

        /* Start to observe the progress */
        observeProgress();
    }

    private void USE_ENCRYPTED_OSS(){
        /* Start the Encrypted OSS Logic */
        encryptedOSSLogic.start(ossList);

        /* Observe the response */
        liveDataEncryptedOSS.observe( this.lifecycleOwner , response -> {
            if(response == null) return;

            /* Get the response */
            if(response.isSuccess()){

                /* OSS Success */
                kEventListener.onOSSSuccess(response);

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

                /* OSS Success */
                kEventListener.onOSSSuccess(response);

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
                /* Yuming Success */
                kEventListener.onYumingSuccess(response);
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
                /* Yuming Success */
                kEventListener.onYumingSuccess(response);
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
                /* Yuming Success */
                kEventListener.onYumingSuccess(response);
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
                /* Yuming Success */
                kEventListener.onYumingSuccess(response);
            } else {
                kEventListener.onAllFailed();
            }
        });
    }

    private void observeProgress(){
        liveDataProgress.observe(lifecycleOwner, progress -> {
            if(progress == 0) return;
            kEventListener.onProgressChanged(progress);
        });
    }

    public static void updateProgress(int progress){
        _liveDataProgress.postValue(progress);
    }

    public static int currentProgress() {
        return liveDataProgress.getValue() != null ? liveDataProgress.getValue() : 0;
    }
}
