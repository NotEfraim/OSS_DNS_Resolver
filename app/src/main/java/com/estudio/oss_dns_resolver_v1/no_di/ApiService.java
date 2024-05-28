package com.estudio.oss_dns_resolver_v1.no_di;

import com.estudio.oss_dns_resolver_v1.model.InitActModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/v1")
    Observable<ResponseBody> getMethodCall();
    @POST("/v1")
    Observable<ResponseBody> postMethodCall();
    @POST("/platform-ns/v1.0/init")
    Observable<InitActModel> initAct();
}
