package com.estudio.oss_dns_resolver_v1.data.logic.yuming;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DNSResolver;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DnsCallback;
import com.estudio.oss_dns_resolver_v1.data.utils.SharePrefManager;
import com.estudio.oss_dns_resolver_v1.model.InitActModel;
import com.estudio.oss_dns_resolver_v1.model.YumingResponse;
import com.estudio.oss_dns_resolver_v1.no_di.network.HttpService;
import com.estudio.oss_dns_resolver_v1.no_di.network.HttpServiceBuilder;
import com.estudio.oss_dns_resolver_v1.no_di.network.ServiceCallBack;
import com.estudio.oss_dns_resolver_v1.utils.Utils;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

public class EncryptedYuming {

    private static final String TAG = CoreLogic.TAG;
    private static DNSResolver dnsResolver;
    private static SharePrefManager sharePrefManager;
    private static EncryptedYuming instance;
    private List<String> yumingList = Collections.emptyList();
    private static int LIST_SIZE = 0;
    private String current_url = "";
    private YumingResponse yumingResponse = new YumingResponse();

    private final MutableLiveData<YumingResponse> _response = new MutableLiveData<>();
    public LiveData<YumingResponse> response = _response;

    /** Phase 1 Variables */
    private static int PHASE_1_COUNTER = 0;

    private EncryptedYuming() {}

    public static EncryptedYuming getInstance(
            DNSResolver resolver,
            SharedPreferences preferences
    ){
        dnsResolver = resolver;
        sharePrefManager = new SharePrefManager(preferences);

        if(instance == null){
            instance = new EncryptedYuming();
        }

        return instance;
    }

    public void start(List<String> yumingList){
        this.yumingList = yumingList;

        Log.d(TAG, "================================");

        PHASE_1_COUNTER = 0;
        LIST_SIZE = yumingList.size();

        /* Phase 1 Start */
        Phase1_ENCRYPT_THE_YUMING();
    }


    private void Phase1_ENCRYPT_THE_YUMING(){

        if(PHASE_1_COUNTER >= LIST_SIZE) {
            if(!yumingResponse.isSuccess()){
                Log.d(TAG, "==== ALL YUMING CALL FAILED! ====");
                _response.postValue(yumingResponse);
            }
            return;
        }

        /* Set the current oss url */
        current_url = yumingList.get(PHASE_1_COUNTER);

        /* Encrypt the OSS url */
        dnsResolver.encryptURL(current_url, new DnsCallback() {
            @Override
            public void onFinishEncryption(String result) {

                Log.d(TAG, "==== Yuming Encryption Success! ====\n"+
                        "Index: "+PHASE_1_COUNTER+
                        "\nOLD URL: "+ current_url+
                        "\nEncrypted URL: "+result);

                /* Call the encrypted OSS */
                Phase2_TEST_YUMING(result);

            }

            @Override
            public void onFailEncryption() {

                Log.d(TAG, "==== Encryption Failed! ==== \nURL: "+current_url);

                /* Increase the counter */
                PHASE_1_COUNTER ++;

                /* Recursive call */
                Phase1_ENCRYPT_THE_YUMING();
            }
        });
    }

    private void Phase2_TEST_YUMING(String encryptionResult) {

        StringBuilder hostHeader = new StringBuilder();
        hostHeader.append(Utils.getURLHost(current_url));
        int port = Utils.getURLPort(current_url);

        if(port != 80) {
            hostHeader.append(":");
            hostHeader.append(port);
        }

        HttpServiceBuilder builder = new HttpServiceBuilder.Builder()
                .setURL(encryptionResult+"/platform-ns/v1.0/init")
                .setMethod("POST")
                .addHeader("dev", "2")
                .addHeader("agent", sharePrefManager.GET_AGENT())
                .addHeader("version", sharePrefManager.GET_VERSION())
                .addHeader("host", hostHeader.toString())
                .build();

        new HttpService(builder, new ServiceCallBack() {
            @Override
            public void onResponse(String response) {
                /* Save Success Yuming */
                sharePrefManager.SET_RESOLVED_YUMING(encryptionResult);

                Log.d(TAG, "==== YUMING CALL Success! ==== \nResponse:" + new Gson().toJson(response));
                InitActModel initActModel = new Gson().fromJson(response, InitActModel.class);
                yumingResponse.setFinalUrl(encryptionResult);
                yumingResponse.setSuccess(true);
                yumingResponse.setData(initActModel);
                _response.postValue(yumingResponse);
                /* Exit the recursion */
                PHASE_1_COUNTER = LIST_SIZE;
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "==== YUMING CALL ERROR ! ==== \nResponse:" + error);
                /* Increase the counter */
                PHASE_1_COUNTER++;
                /* Recursive call */
                Phase1_ENCRYPT_THE_YUMING();
            }

        });

    }

}
