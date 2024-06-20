//package com.estudio.oss_dns_resolver_v1.data.utils;
//
//import android.content.SharedPreferences;
//
//import com.estudio.oss_dns_resolver_v1.no_di.ApiService;
//import com.estudio.oss_dns_resolver_v1.model.InitActModel;
//import com.estudio.oss_dns_resolver_v1.utils.Constants;
//
//public class ApiRawCall {
//    private static ApiService apiService;
//    private static SharedPreferences sharedPreferences;
//    private static ApiRawCall instance;
//
//    public static ApiRawCall getInstance(
//            ApiService service,
//            SharedPreferences preferences
//    ){
//        apiService = service;
//        sharedPreferences = preferences;
//
//        if (instance == null){
//            instance = new ApiRawCall();
//        }
//
//        return instance;
//    }
//
//    public Observable<ResponseBody> getMethodCall(String url) {
//        sharedPreferences.edit().putString(Constants.OSS_URL_KEY, url).apply();
//        return apiService.getMethodCall();
//    }
//
//    public Observable<InitActModel> initAct(String url) {
//        sharedPreferences.edit().putString(Constants.YUMING_URL_KEY, url).apply();
//        return apiService.initAct();
//    }
//}
