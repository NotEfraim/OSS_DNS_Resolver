package com.estudio.oss_dns_resolver_v1.utils;

import android.util.Base64;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class Utils {

    public static String getURLScheme(String url){
        if(url.contains("https")) return "https";
        else if(url.contains("http")) return "http";
        return "";
    }

    public static int getURLPort(String url){

        try {

            String relURL = getURLHost(url);
            String toSplitURL = url.split(relURL)[1];

            if(toSplitURL.contains(":")){
                String[] split = toSplitURL.split("/");
                return Integer.parseInt(split[0].substring(1));
            }

        }catch (Exception ignored){}


        return 80;
    }

    public static String getURLHost(String strURL){
        String host = null;
        try {
            URL url = new URL(strURL);
            host = url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return host;
    }

    public static String replaceURLHost(String url, String newHost){

        String oldHttpUrl = getURLHost(url);

        if(oldHttpUrl == null) return "";

        String scheme = getURLScheme(url);

        String[] split = url.split(oldHttpUrl);

        System.out.println(Arrays.toString(split));

        return scheme +
                "://" +
                newHost +
                split[1];
    }
}
