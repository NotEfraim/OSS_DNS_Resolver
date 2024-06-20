package com.estudio.oss_dns_resolver_v1.data.utils;

import android.content.SharedPreferences;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.estudio.oss_dns_resolver_v1.utils.Constants;

import java.nio.charset.StandardCharsets;

public class SharePrefManager {

    private SharedPreferences sharedPreferences;

    public SharePrefManager(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public String GET_DNS_ID(){
        return sharedPreferences.getString(Constants.DNS_ID_KEY, "");
    }

    public String GET_DNS_KEY(){
        return sharedPreferences.getString(Constants.DNS_KEY, "");
    }

    public String GET_VERSION(){
        return sharedPreferences.getString(Constants.VERSION_KEY, "");
    }

    public String GET_AGENT(){
        return sharedPreferences.getString(Constants.AGENT_KEY, "");
    }
    public String GET_RESOLVED_YUMING(){
        return sharedPreferences.getString(Constants.YUMING_KEY, "");
    }
    public String GET_RESOLVED_OSS(){
        return sharedPreferences.getString(Constants.OSS_KEY, "");
    }
    public String GET_HEADER_HOST(){
        return sharedPreferences.getString(Constants.HEADER_HOST_KEY, "");
    }

    public void SET_DNS_ID(String value){
        sharedPreferences.edit().putString(Constants.DNS_ID_KEY, value).apply();
    }

    public void SET_DNS_KEY(String value){
        sharedPreferences.edit().putString(Constants.DNS_KEY, value).apply();
    }

    public void SET_VERSION(String value){
        sharedPreferences.edit().putString(Constants.VERSION_KEY, value).apply();
    }

    public void SET_AGENT(String value){
        sharedPreferences.edit().putString(Constants.AGENT_KEY, value).apply();
    }
    public void SET_RESOLVED_YUMING(String value){
        sharedPreferences.edit().putString(Constants.YUMING_KEY, value).apply();
    }
    public void SET_RESOLVED_OSS(String value){
        sharedPreferences.edit().putString(Constants.OSS_KEY, value).apply();
    }
    public void SET_HEADER_HOST(String value){
        sharedPreferences.edit().putString(Constants.HEADER_HOST_KEY, value).apply();
    }

}
