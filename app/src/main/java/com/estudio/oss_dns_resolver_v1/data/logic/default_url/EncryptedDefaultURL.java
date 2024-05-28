package com.estudio.oss_dns_resolver_v1.data.logic.default_url;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DNSResolver;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DnsCallback;
import com.estudio.oss_dns_resolver_v1.data.utils.ApiRawCall;
import com.estudio.oss_dns_resolver_v1.data.utils.BaseApiObserver;
import com.estudio.oss_dns_resolver_v1.data.utils.Process_Enum;
import com.estudio.oss_dns_resolver_v1.model.InitActModel;
import com.estudio.oss_dns_resolver_v1.model.YumingResponse;
import com.estudio.oss_dns_resolver_v1.utils.Constants;
import com.estudio.oss_dns_resolver_v1.utils.Utils;
import com.google.gson.Gson;

import java.net.HttpURLConnection;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EncryptedDefaultURL {

    private static final String TAG = CoreLogic.TAG;
    private static DNSResolver dnsResolver;
    private static ApiRawCall apiRawCall;
    private static SharedPreferences sharedPreferences;
    private static EncryptedDefaultURL instance;

    Gson gson = new Gson();
    private String defaultURL = "";
    private YumingResponse yumingResponse = new YumingResponse();
    private final MutableLiveData<YumingResponse> _response = new MutableLiveData<>();
    public LiveData<YumingResponse> response = _response;

    private EncryptedDefaultURL() {}


    public static EncryptedDefaultURL getInstance(
            DNSResolver resolver,
            ApiRawCall rawCall,
            SharedPreferences preferences
    ) {
        dnsResolver = resolver;
        apiRawCall = rawCall;
        sharedPreferences = preferences;

        if (instance == null) {
            instance = new EncryptedDefaultURL();
        }

        return instance;
    }

    public void start(String defaultUrl){
        this.defaultURL = defaultUrl;

        Log.d(TAG, "================================");

        /* Set Status */
        sharedPreferences.edit().putString(Constants.PROCESS_KEY, Process_Enum.YUMING_PROCESS.name()).apply();

        /* Reset DNS Header */
        sharedPreferences.edit().putString(Constants.OSS_HEADER_HOST, "").apply();

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

                /* Add DNS Header */
                sharedPreferences.edit().putString(Constants.OSS_HEADER_HOST, Utils.getURLHost(defaultURL)).apply();

                /* Add BASE URL */
                sharedPreferences.edit().putString(Constants.BASE_URL_KEY, result).apply();

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

        apiRawCall.initAct(encryptionResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseApiObserver<InitActModel>() {

                    @Override
                    public void onNext(InitActModel initActModel) {

                        Log.d(TAG, "==== Default URL CALL Success! ==== \nResponse:" + gson.toJson(initActModel));

                        if(initActModel.getCode() == HttpURLConnection.HTTP_OK || initActModel.getCode() == 0) {
                            yumingResponse.setSuccess(true);
                            yumingResponse.setData(initActModel);
                            _response.postValue(yumingResponse);
                        } else {
                            /* return fail and null */
                            _response.postValue(yumingResponse);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "====Default URL CALL ERROR ! ==== \nResponse:" + e.getLocalizedMessage());
                        /* return fail and null */
                        _response.postValue(yumingResponse);
                    }

                });
    }


}
