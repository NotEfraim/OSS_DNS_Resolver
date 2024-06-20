package com.estudio.oss_dns_resolver_v1.data.logic.default_url;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.utils.SharePrefManager;
import com.estudio.oss_dns_resolver_v1.model.InitActModel;
import com.estudio.oss_dns_resolver_v1.model.YumingResponse;
import com.estudio.oss_dns_resolver_v1.no_di.network.HttpService;
import com.estudio.oss_dns_resolver_v1.no_di.network.HttpServiceBuilder;
import com.estudio.oss_dns_resolver_v1.no_di.network.ServiceCallBack;
import com.google.gson.Gson;

public class BasicDefaultURL {

    private static final String TAG = CoreLogic.TAG;
    private static SharePrefManager sharePrefManager;
    private static BasicDefaultURL instance;
    private String defaultURL = "";
    private YumingResponse yumingResponse = new YumingResponse();
    private final MutableLiveData<YumingResponse> _response = new MutableLiveData<>();
    public LiveData<YumingResponse> response = _response;

    private BasicDefaultURL() {}

    public static BasicDefaultURL getInstance(
            SharedPreferences preferences
    ){
        sharePrefManager = new SharePrefManager(preferences);

        if(instance == null){
            instance = new BasicDefaultURL();
        }

        return instance;
    }

    public void start(String defaultUrl){
        this.defaultURL = defaultUrl;

        Log.d(TAG, "================================");

        /* Phase 1 Start */
        Phase1_TEST_YUMING(defaultURL);
    }

    private void Phase1_TEST_YUMING(String encryptionResult) {

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
                sharePrefManager.SET_RESOLVED_YUMING(defaultURL);

                /* Reset Header Host */
                sharePrefManager.SET_HEADER_HOST("");

                /* Update Progress */
                CoreLogic.updateProgress(120);

                Log.d(TAG, " ==== YUMING CALL Success! ==== \nResponse:" + new Gson().toJson(response));
                InitActModel initActModel = new Gson().fromJson(response, InitActModel.class);
                yumingResponse.setSuccess(true);
                yumingResponse.setData(initActModel);
                _response.postValue(yumingResponse);
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, " ==== YUMING CALL ERROR ! ==== \nResponse:" + error);
                /* return fail and null */
                _response.postValue(yumingResponse);
                /* Update Progress */
                CoreLogic.updateProgress(90);
            }
        });

    }



}
