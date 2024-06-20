package com.estudio.oss_dns_resolver_v1.data.logic.oss;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.estudio.oss_dns_resolver_v1.data.logic.CoreLogic;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DNSResolver;
import com.estudio.oss_dns_resolver_v1.data.logic.dns.DnsCallback;
import com.estudio.oss_dns_resolver_v1.data.utils.Process_Enum;
import com.estudio.oss_dns_resolver_v1.data.utils.SharePrefManager;
import com.estudio.oss_dns_resolver_v1.model.InitModel;
import com.estudio.oss_dns_resolver_v1.model.OSSResponse;
import com.estudio.oss_dns_resolver_v1.no_di.network.HttpService;
import com.estudio.oss_dns_resolver_v1.no_di.network.HttpServiceBuilder;
import com.estudio.oss_dns_resolver_v1.no_di.network.ServiceCallBack;
import com.estudio.oss_dns_resolver_v1.utils.Constants;
import com.estudio.oss_dns_resolver_v1.utils.RSAUtil;
import com.estudio.oss_dns_resolver_v1.utils.Utils;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

public class EncryptedOSSLogic {
    private static final String TAG = CoreLogic.TAG;
    private static DNSResolver dnsResolver;
    private static SharePrefManager sharePrefManager;
    private static EncryptedOSSLogic instance;

    /** Common Variables */

    private final OSSResponse ossResponse = new OSSResponse();
    private List<String> ossList = Collections.emptyList();
    private static int OSS_SIZE = 0;
    private String current_oss_url = "";
    private final MutableLiveData<OSSResponse> _initModel = new MutableLiveData<>();
    public MutableLiveData<OSSResponse> initModel = _initModel;

    /** Phase 1 Variables */
    private static int PHASE_1_COUNTER = 0;

    private EncryptedOSSLogic() {}

    public static EncryptedOSSLogic getInstance(DNSResolver resolver, SharedPreferences pref){
        dnsResolver = resolver;
        sharePrefManager = new SharePrefManager(pref);

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


        StringBuilder hostHeader = new StringBuilder();
        hostHeader.append(Utils.getURLHost(current_oss_url));
        int port = Utils.getURLPort(current_oss_url);

        if(port != 80) {
            hostHeader.append(":");
            hostHeader.append(port);
        }

        /* OSS Raw Call */
        HttpServiceBuilder builder = new HttpServiceBuilder.Builder()
                .setURL(encryptionResult)
                .setMethod("GET")
                .addHeader("dev", "2")
                .addHeader("agent", sharePrefManager.GET_AGENT())
                .addHeader("version", sharePrefManager.GET_VERSION())
                .addHeader("host", hostHeader.toString())
                .build();

        new HttpService(builder, new ServiceCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    /* Save Success OSS */
                    sharePrefManager.SET_RESOLVED_OSS(encryptionResult);

                    String responseData = RSAUtil.decryptNew(response, RSAUtil.getPrivateKey(Constants.privateKey));
                    InitModel model = new Gson().fromJson(responseData, InitModel.class);
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
            public void onFailure(String error) {
                Log.d(TAG, " ==== OSS CALL ERROR ! ==== \nResponse:" + error);
                /* Increase the counter */
                PHASE_1_COUNTER++;
                /* Recursive call */
                Phase1_ENCRYPT_THE_OSS();
            }
        });

    }
}
