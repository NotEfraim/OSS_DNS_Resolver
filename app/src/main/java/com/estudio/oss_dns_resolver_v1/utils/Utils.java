package com.estudio.oss_dns_resolver_v1.utils;

import okhttp3.HttpUrl;

public class Utils {

    public static String getURLScheme(String url){
        HttpUrl httpUrl = HttpUrl.parse(url);
        if(httpUrl != null){
            return httpUrl.scheme();
        }
        return "";
    }

    public static int getURLPort(String url){
        HttpUrl httpUrl = HttpUrl.parse(url);
        if(httpUrl != null){
            return httpUrl.port();
        }
        return 80;
    }

    public static String getURLHost(String url){
        HttpUrl httpUrl = HttpUrl.parse(url);
        if(httpUrl != null){
            return httpUrl.host();
        }
        return "";
    }

    public static String replaceURLHost(String url, String newHost){

        HttpUrl oldHttpUrl = HttpUrl.parse(url);

        if(oldHttpUrl == null)
            return "";

        HttpUrl newHttpUrl = oldHttpUrl.newBuilder()
                .host(newHost)
                .scheme(oldHttpUrl.scheme())
                .port(oldHttpUrl.port())
                .build();

        return String.valueOf(newHttpUrl.url());
    }
}
