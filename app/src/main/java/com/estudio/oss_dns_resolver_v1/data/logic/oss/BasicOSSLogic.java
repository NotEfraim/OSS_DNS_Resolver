package com.estudio.oss_dns_resolver_v1.data.logic.oss;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.utils.SharePrefManager;
import com.estudio.oss_dns_resolver_v1.model.InitModel;
import com.estudio.oss_dns_resolver_v1.model.OSSResponse;
import com.estudio.oss_dns_resolver_v1.no_di.network.HttpService;
import com.estudio.oss_dns_resolver_v1.no_di.network.HttpServiceBuilder;
import com.estudio.oss_dns_resolver_v1.no_di.network.ServiceCallBack;
import com.estudio.oss_dns_resolver_v1.utils.Constants;
import com.estudio.oss_dns_resolver_v1.utils.RSAUtil;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

public class BasicOSSLogic {

    private static final String TAG = CoreLogic.TAG;
    private static SharePrefManager sharePrefManager;
    private static BasicOSSLogic instance;

    /** Common Variables */

    private final OSSResponse ossResponse = new OSSResponse();
    private List<String> ossList = Collections.emptyList();
    private static int OSS_SIZE = 0;
    private String current_oss_url = "";
    private final MutableLiveData<OSSResponse> _initModel = new MutableLiveData<>();
    public MutableLiveData<OSSResponse> initModel = _initModel;

    /** Phase 1 Variables */
    private static int PHASE_1_COUNTER = 0;

    private BasicOSSLogic() {}

    public static BasicOSSLogic getInstance(
            SharedPreferences preferences
    ){
        sharePrefManager = new SharePrefManager(preferences);

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

        HttpServiceBuilder builder = new HttpServiceBuilder.Builder()
                .setURL(current_oss_url)
                .setMethod("GET")
                .addHeader("dev", "2")
                .addHeader("agent", sharePrefManager.GET_AGENT())
                .addHeader("version", sharePrefManager.GET_VERSION())
                .build();


        /* OSS Raw Call */
        new HttpService(builder, new ServiceCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    /* Save Success OSS */
                    sharePrefManager.SET_RESOLVED_OSS(current_oss_url);

                    String responseData = RSAUtil.decryptNew(response, RSAUtil.getPrivateKey(Constants.privateKey));
                    InitModel model = new Gson().fromJson(responseData, InitModel.class);
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
            public void onFailure(String error) {
                Log.d(TAG, "==== OSS CALL ERROR ! ==== \nResponse:" + error);
                /* Increase the counter */
                PHASE_1_COUNTER++;
                /* Recursive call */
                Phase1_OSS_RAW_CALL();
            }
        });

    }

}
