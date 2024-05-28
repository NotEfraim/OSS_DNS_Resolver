package com.estudio.oss_dns_resolver_v1.no_di;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import com.estudio.oss_dns_resolver_v1.data.utils.HttpInterceptor;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    private static SharedPreferences sharedPreferences;
    private static RetrofitUtil instance;
    private final long TIME_OUT = 10000L;
    private RetrofitUtil() {}

    public static RetrofitUtil getInstance(SharedPreferences preferences) {
        sharedPreferences = preferences;

        if(instance == null) {
            instance = new RetrofitUtil();
        }
        return instance;
    }

    public OkHttpClient provideOkhttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpInterceptor(sharedPreferences))
                .callTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .sslSocketFactory(provideSSLSocketFactory(provideTrustManagers()), (X509TrustManager) provideTrustManagers()[0])
                .build();
    }

    public GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    public RxJava3CallAdapterFactory provideRxJava3CallAdapterFactory() {
        return RxJava3CallAdapterFactory.create();
    }

    public Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("https://google.com")
                .client(provideOkhttpClient())
                .addConverterFactory(provideGsonConverterFactory())
                .addCallAdapterFactory(provideRxJava3CallAdapterFactory())
                .build();
    }

    public ApiService apiService(){
        return provideRetrofit().create(ApiService.class);
    }

    public TrustManager[] provideTrustManagers() {
        return new TrustManager[] {
                new X509TrustManager() {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {

                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
    }

    public SSLSocketFactory provideSSLSocketFactory(TrustManager[] trustAllCerts){
        SSLSocketFactory ssfFactory = null;
        try{
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        }catch (Exception e){
            Log.e("SSL", "Error while setting up SSL");
        }

        return ssfFactory;
    }
}
