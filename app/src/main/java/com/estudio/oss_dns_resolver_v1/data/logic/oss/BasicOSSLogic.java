package com.estudio.oss_dns_resolver_v1.data.logic.oss;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.utils.ApiRawCall;
import com.estudio.oss_dns_resolver_v1.data.utils.BaseApiObserver;
import com.estudio.oss_dns_resolver_v1.data.utils.Process_Enum;
import com.estudio.oss_dns_resolver_v1.model.InitModel;
import com.estudio.oss_dns_resolver_v1.model.OSSResponse;
import com.estudio.oss_dns_resolver_v1.utils.Constants;
import com.estudio.oss_dns_resolver_v1.utils.RSAUtil;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class BasicOSSLogic {

    private static final String TAG = CoreLogic.TAG;
    private static ApiRawCall apiRawCall;
    private static SharedPreferences sharedPreferences;
    private static BasicOSSLogic instance;

    /** Common Variables */

    private final OSSResponse ossResponse = new OSSResponse();
    Gson gson = new Gson();
    private List<String> ossList = Collections.emptyList();
    private static int OSS_SIZE = 0;
    private String current_oss_url = "";
    private final MutableLiveData<OSSResponse> _initModel = new MutableLiveData<>();
    public MutableLiveData<OSSResponse> initModel = _initModel;

    /** Phase 1 Variables */
    private static int PHASE_1_COUNTER = 0;

    private BasicOSSLogic() {}

    public static BasicOSSLogic getInstance(
            ApiRawCall rawCall,
            SharedPreferences preferences
    ){
        apiRawCall = rawCall;
        sharedPreferences = preferences;

        if(instance == null){
            instance = new BasicOSSLogic();
        }
        return instance;

    }

    public void start(List<String> ossList) {
        this.ossList = ossList;

        Log.d(TAG, "================================");

        OSS_SIZE = ossList.size();
        PHASE_1_COUNTER = 0;

        /* Set Status */
        sharedPreferences.edit().putString(Constants.PROCESS_KEY, Process_Enum.OSS_PROCESS.name()).apply();

        /* Reset OSS Header */
        sharedPreferences.edit().putString(Constants.OSS_HEADER_HOST, "").apply();

        Phase1_OSS_RAW_CALL();
    }

    private void Phase1_OSS_RAW_CALL() {

        if(PHASE_1_COUNTER >= OSS_SIZE) {
            if(!ossResponse.isSuccess()){
                Log.d(TAG, "==== ALL OSS BASIC FAILED! ====");
                _initModel.postValue(ossResponse);
            }
            return;
        }

        /* Set the current oss url */
        current_oss_url = ossList.get(PHASE_1_COUNTER);

        /* OSS Raw Call */
        apiRawCall.getMethodCall(current_oss_url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseApiObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody response) {
                        try {
                            String responseData = RSAUtil.decryptNew(response.string(), RSAUtil.getPrivateKey(Constants.privateKey));
                            InitModel model = gson.fromJson(responseData, InitModel.class);
                            ossResponse.setSuccess(true);
                            ossResponse.setData(model);
                            _initModel.postValue(ossResponse);
                            Log.d(TAG, "==== OSS CALL Success! ====\n" +
                                    "Index: "+PHASE_1_COUNTER+
                                    "URL: " + current_oss_url +
                                    "\nResponse:" + responseData);

                        } catch (Exception e) {
                            Log.d(TAG, "==== OSS CALL Exception ====" + e.getMessage());
                        }

                        /* Exit the recursion */
                        PHASE_1_COUNTER = OSS_SIZE;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==== OSS CALL ERROR ! ==== \nResponse:" + e.getLocalizedMessage());
                        /* Increase the counter */
                        PHASE_1_COUNTER++;
                        /* Recursive call */
                        Phase1_OSS_RAW_CALL();
                    }

                });
    }

}
