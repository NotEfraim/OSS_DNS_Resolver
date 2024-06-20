package com.estudio.oss_dns_resolver_v1.data.logic.yuming;

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
import com.estudio.oss_dns_resolver_v1.utils.Constants;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

public class BasicYuming {

    private static final String TAG = CoreLogic.TAG;
    private static SharePrefManager sharePrefManager;
    private static BasicYuming instance;
    private List<String> yumingList = Collections.emptyList();
    private static int LIST_SIZE = 0;
    private String current_url = "";
    private YumingResponse yumingResponse = new YumingResponse();

    private final MutableLiveData<YumingResponse> _response = new MutableLiveData<>();
    public LiveData<YumingResponse> response = _response;

    /** Phase 1 Variables */
    private static int PHASE_1_COUNTER = 0;

    private BasicYuming() {}

    public static BasicYuming getInstance(SharedPreferences preferences){
        sharePrefManager = new SharePrefManager(preferences);

        if(instance == null){
            instance = new BasicYuming();
        }

        return instance;
    }

    public void start(List<String> yumingList){
        this.yumingList = yumingList;

        Log.d(TAG, "================================");

        PHASE_1_COUNTER = 0;
        LIST_SIZE = yumingList.size();

        /* Phase 1 Start */
        Phase1_TEST_YUMING();
    }

    private void Phase1_TEST_YUMING(){

        if(PHASE_1_COUNTER >= LIST_SIZE) {
            if(!yumingResponse.isSuccess()){
                Log.d(TAG, " ==== ALL YUMING CALL FAILED! ====");
                _response.postValue(yumingResponse);
            }
            return;
        }

        /* Set the current oss url */
        current_url = yumingList.get(PHASE_1_COUNTER);

        HttpServiceBuilder builder = new HttpServiceBuilder.Builder()
                .setURL(current_url+"/platform-ns/v1.0/init")
                .setMethod("POST")
                .addHeader("dev", "2")
                .addHeader("agent", sharePrefManager.GET_AGENT())
                .addHeader("version", sharePrefManager.GET_VERSION())
                .build();

        new HttpService(builder, new ServiceCallBack() {
            @Override
            public void onResponse(String response) {
                /* Save Success Yuming */
                sharePrefManager.SET_RESOLVED_YUMING(current_url);

                /* Reset Header Host */
                sharePrefManager.SET_HEADER_HOST("");

                /* Update Progress */
                CoreLogic.updateProgress(120);

                Log.d(TAG, "==== YUMING CALL Success! ==== \nResponse:" + new Gson().toJson(response));
                InitActModel initActModel = new Gson().fromJson(response, InitActModel.class);
                yumingResponse.setFinalUrl(current_url);
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
                Phase1_TEST_YUMING();
                /* Update Progress */
                int currentProgress = CoreLogic.currentProgress();
                int progress = (currentProgress + (100 / (yumingList.size() * 2) ) );
                CoreLogic.updateProgress(progress);

            }
        });
    }

}
