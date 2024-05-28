package com.estudio.oss_dns_resolver_v1.data.utils;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.estudio.oss_dns_resolver_v1.utils.Constants;
import com.estudio.oss_dns_resolver_v1.utils.Utils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpInterceptor implements Interceptor {

    private final String TAG = "HttpInterceptor";
    private SharedPreferences sharedPreferences;

    public HttpInterceptor(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String status = sharedPreferences.getString(Constants.PROCESS_KEY, "");

        /* Catch if the process is empty */
        if(status.isEmpty()) {
            Log.d(TAG, "Status is empty!!");
            return chain.proceed(request);
        }

        Log.d(TAG, "==== Status: "+status+" ====");
        Request newRequest;

        /* Validate the process */
        if(status.equals(Process_Enum.OSS_PROCESS.name())){
            newRequest = ossResponseBuilder(request);
        } else if (status.equals(Process_Enum.YUMING_PROCESS.name())) {
            newRequest = yumingResponseBuilder(request);
        } else {
            newRequest = chain.request();
        }

        /* Execute to get the Response */
        Response response = chain.proceed(newRequest);

        /* Log the Response */
        String responseBodyString = response.body() != null ? response.peekBody(2048).string() : "";
        Log.d(TAG, "Http Interceptor: \nURL:"+newRequest.url()+"\nHeaders: "+newRequest.headers()+"\nResponse: "+responseBodyString);


        return response;
    }

    private Request ossResponseBuilder(Request request) {
        String newUrl = sharedPreferences.getString(Constants.OSS_URL_KEY, "");
        String host_header = sharedPreferences.getString(Constants.OSS_HEADER_HOST, "");

        /* Change the url */
        Request.Builder newRequest = request.newBuilder();
        newRequest.url(newUrl);

        /* Add the host header if encrypted */
        if(!host_header.isEmpty()){ newRequest.addHeader("Host", host_header); }

        /* Add required headers */
        newRequest.addHeader("dev", "2");
        newRequest.addHeader("version", "3.4.15.2");

        return newRequest.build();
    }

    private Request yumingResponseBuilder(Request request) {
        String newHost = sharedPreferences.getString(Constants.BASE_URL_KEY, "");
        String host_header = sharedPreferences.getString(Constants.OSS_HEADER_HOST, "");

        /* Change the host */
        HttpUrl newBuilder = request.url().newBuilder()
                .host(Utils.getURLHost(newHost))
                .scheme(Utils.getURLScheme(newHost))
                .port(Utils.getURLPort(newHost))
                .build();

        /* Apply the new host in request */
        Request.Builder newRequest = request.newBuilder();
        newRequest.url(newBuilder);

        /* Add the host header if encrypted */
        if(!host_header.isEmpty()){ newRequest.addHeader("Host", host_header); }

        /* Add required headers */
        newRequest.addHeader("dev", "2");
        newRequest.addHeader("version", "3.4.15.2");

        return newRequest.build();
    }
}
