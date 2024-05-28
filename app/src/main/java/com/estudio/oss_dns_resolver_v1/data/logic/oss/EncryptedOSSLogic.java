package com.estudio.oss_dns_resolver_v1.data.logic.oss;

import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DNSResolver;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DnsCallback;
import com.estudio.oss_dns_resolver_v1.data.utils.ApiRawCall;
import com.estudio.oss_dns_resolver_v1.data.utils.BaseApiObserver;
import com.estudio.oss_dns_resolver_v1.data.utils.Process_Enum;
import com.estudio.oss_dns_resolver_v1.model.InitModel;
import com.estudio.oss_dns_resolver_v1.model.OSSResponse;
import com.estudio.oss_dns_resolver_v1.utils.Constants;
import com.estudio.oss_dns_resolver_v1.utils.RSAUtil;
import com.estudio.oss_dns_resolver_v1.utils.Utils;
import com.google.gson.Gson;
import java.util.Collections;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class EncryptedOSSLogic {
    private static final String TAG = CoreLogic.TAG;
    private static DNSResolver dnsResolver;
    private static ApiRawCall apiRawCall;
    private static SharedPreferences sharedPreferences;
    private static EncryptedOSSLogic instance;

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

    private EncryptedOSSLogic() {}

    public static EncryptedOSSLogic getInstance(
            DNSResolver resolver,
            ApiRawCall rawCall,
            SharedPreferences pref
    ){
        dnsResolver = resolver;
        apiRawCall =  rawCall;
        sharedPreferences = pref;

        if(instance == null){
            instance = new EncryptedOSSLogic();
        }
        return instance;
    }

    public void start(List<String> ossList) {
        this.ossList = ossList;

        Log.d(TAG, "================================");

        PHASE_1_COUNTER = 0;
        OSS_SIZE = ossList.size();

        /* Set Status */
        sharedPreferences.edit().putString(Constants.PROCESS_KEY, Process_Enum.OSS_PROCESS.name()).apply();

        /* Reset DNS Header and Base Url */
        sharedPreferences.edit().putString(Constants.OSS_HEADER_HOST, "").apply();
        sharedPreferences.edit().putString(Constants.BASE_URL_KEY, "").apply();

        /* Phase 1 Start */
        Phase1_ENCRYPT_THE_OSS();
    }

    private void Phase1_ENCRYPT_THE_OSS(){

        if(PHASE_1_COUNTER >= OSS_SIZE) {
            if(!ossResponse.isSuccess()){
                Log.d(TAG, "==== ALL OSS ENCRYPTION FAILED! ====");
                _initModel.postValue(ossResponse);
            }
            return;
        }

        /* Set the current oss url */
        current_oss_url = ossList.get(PHASE_1_COUNTER);

        /* Encrypt the OSS url */
        dnsResolver.encryptURL(current_oss_url, new DnsCallback() {
            @Override
            public void onFinishEncryption(String result) {

                Log.d(TAG, "==== OSS Encryption Success! ====\n"+
                        "Index: "+PHASE_1_COUNTER+
                        "\nOLD URL: "+Utils.getURLHost(current_oss_url)+
                        "\nEncrypted URL: "+result);

                /* Add OSS Header */
                sharedPreferences.edit().putString(Constants.OSS_HEADER_HOST, Utils.getURLHost(current_oss_url)).apply();

                /* Call the encrypted OSS */
                Phase2_GET_YUMING_LIST(result);

            }

            @Override
            public void onFailEncryption() {

                Log.d(TAG, "==== Encryption Failed! ==== \nURL: "+current_oss_url);

                /* Increase the counter */
                PHASE_1_COUNTER ++;

                /* Recursive call */
                Phase1_ENCRYPT_THE_OSS();
            }
        });
    }

    private void Phase2_GET_YUMING_LIST(String encryptionResult) {

        /* OSS Raw Call */
        apiRawCall.getMethodCall(encryptionResult)
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
                            Log.d(TAG, " ==== OSS CALL Success! ==== \nResponse:" + responseData);

                        } catch (Exception e) {
                            Log.d(TAG, " ==== OSS CALL Exception ====" + e.getMessage());
                        }

                        /* Exit the recursion */
                        PHASE_1_COUNTER = OSS_SIZE;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, " ==== OSS CALL ERROR ! ==== \nResponse:" + e.getLocalizedMessage());
                        /* Increase the counter */
                        PHASE_1_COUNTER++;
                        /* Recursive call */
                        Phase1_ENCRYPT_THE_OSS();
                    }

                });
    }
}
