package com.estudio.oss_dns_resolver_v1.data.logic.default_url;

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
import com.google.gson.Gson;

public class EncryptedDefaultURL {

    private static final String TAG = CoreLogic.TAG;
    private static DNSResolver dnsResolver;
    private static SharePrefManager sharePrefManager;
    private static EncryptedDefaultURL instance;

    private String defaultURL = "";
    private YumingResponse yumingResponse = new YumingResponse();
    private final MutableLiveData<YumingResponse> _response = new MutableLiveData<>();
    public LiveData<YumingResponse> response = _response;

    private EncryptedDefaultURL() {}


    public static EncryptedDefaultURL getInstance(
            DNSResolver resolver,
            SharedPreferences preferences
    ) {
        dnsResolver = resolver;
        sharePrefManager = new SharePrefManager(preferences);

        if (instance == null) {
            instance = new EncryptedDefaultURL();
        }

        return instance;
    }

    public void start(String defaultUrl){
        this.defaultURL = defaultUrl;

        Log.d(TAG, "================================");

        /* Phase 1 Start */
        Phase1_ENCRYPT_THE_YUMING();
    }

    private void Phase1_ENCRYPT_THE_YUMING(){

        /* Encrypt the OSS url */
        dnsResolver.encryptURL(defaultURL, new DnsCallback() {
            @Override
            public void onFinishEncryption(String result) {

                Log.d(TAG, "==== Default URL Encryption Success! ====\n"+
                        "OLD URL: "+ defaultURL+
                        "\nEncrypted URL: "+result);

                /* Call the encrypted OSS */
                Phase2_TEST_YUMING(result);

            }

            @Override
            public void onFailEncryption() {

                Log.d(TAG, "==== Default URL Encryption Failed! ==== \nURL: "+defaultURL);

                /* Call the encrypted OSS */
                Phase2_TEST_YUMING(defaultURL);
            }
        });
    }

    private void Phase2_TEST_YUMING(String encryptionResult) {

        HttpServiceBuilder builder = new HttpServiceBuilder.Builder()
                .setURL(encryptionResult)
                .setMethod("POST")
                .addHeader("dev", "2")
                .addHeader("agent", sharePrefManager.GET_AGENT())
                .addHeader("version", sharePrefManager.GET_VERSION())
                .build();

        new HttpService(builder, new ServiceCallBack() {
            @Override
            public void onResponse(String response) {
                /* Save Success Yuming */
                sharePrefManager.SET_RESOLVED_YUMING(encryptionResult);

                Log.d(TAG, "==== Default URL CALL Success! ==== \nResponse:" + new Gson().toJson(response));
                InitActModel initActModel = new Gson().fromJson(response, InitActModel.class);
                yumingResponse.setSuccess(true);
                yumingResponse.setData(initActModel);
                _response.postValue(yumingResponse);
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "====Default URL CALL ERROR ! ==== \nResponse:" + error);
                /* return fail and null */
                _response.postValue(yumingResponse);
            }
        });

    }


}
