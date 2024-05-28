package com.estudio.oss_dns_resolver_v1.data.logic.yuming;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.utils.ApiRawCall;
import com.estudio.oss_dns_resolver_v1.data.utils.BaseApiObserver;
import com.estudio.oss_dns_resolver_v1.data.utils.Process_Enum;
import com.estudio.oss_dns_resolver_v1.model.InitActModel;
import com.estudio.oss_dns_resolver_v1.model.YumingResponse;
import com.estudio.oss_dns_resolver_v1.utils.Constants;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BasicYuming {

    private static final String TAG = CoreLogic.TAG;
    private static ApiRawCall apiRawCall;
    private static SharedPreferences sharedPreferences;
    private static BasicYuming instance;

    Gson gson = new Gson();
    private List<String> yumingList = Collections.emptyList();
    private static int LIST_SIZE = 0;
    private String current_url = "";
    private YumingResponse yumingResponse = new YumingResponse();

    private final MutableLiveData<YumingResponse> _response = new MutableLiveData<>();
    public LiveData<YumingResponse> response = _response;

    /** Phase 1 Variables */
    private static int PHASE_1_COUNTER = 0;

    private BasicYuming() {}

    public static BasicYuming getInstance(
            ApiRawCall rawCall,
            SharedPreferences preferences
    ){
        apiRawCall = rawCall;
        sharedPreferences = preferences;

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

        /* Set Status */
        sharedPreferences.edit().putString(Constants.PROCESS_KEY, Process_Enum.YUMING_PROCESS.name()).apply();

        /* Reset DNS Header */
        sharedPreferences.edit().putString(Constants.OSS_HEADER_HOST, "").apply();

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

        /* Add BASE URL */
        sharedPreferences.edit().putString(Constants.BASE_URL_KEY, current_url).apply();

        apiRawCall.initAct(current_url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseApiObserver<InitActModel>() {

                    @Override
                    public void onNext(InitActModel initActModel) {

                        Log.d(TAG, "==== YUMING CALL Success! ==== \nResponse:" + gson.toJson(initActModel));

                        if(initActModel.getCode() == HttpURLConnection.HTTP_OK || initActModel.getCode() == 0) {
                            yumingResponse.setFinalUrl(current_url);
                            yumingResponse.setSuccess(true);
                            yumingResponse.setData(initActModel);
                            _response.postValue(yumingResponse);
                            /* Exit the recursion */
                            PHASE_1_COUNTER = LIST_SIZE;
                        } else {
                            /* Increase the counter */
                            PHASE_1_COUNTER++;
                            /* Recursive call */
                            Phase1_TEST_YUMING();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==== YUMING CALL ERROR ! ==== \nResponse:" + e.getLocalizedMessage());
                        /* Increase the counter */
                        PHASE_1_COUNTER++;
                        /* Recursive call */
                        Phase1_TEST_YUMING();
                    }

                });


    }

}
