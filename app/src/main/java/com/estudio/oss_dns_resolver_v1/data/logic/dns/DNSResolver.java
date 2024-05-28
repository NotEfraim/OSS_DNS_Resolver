package com.estudio.oss_dns_resolver_v1.data.logic.dns;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.estudio.oss_dns_resolver_v1.utils.Constants;
import com.estudio.oss_dns_resolver_v1.utils.Utils;
import com.tencent.msdk.dns.DnsConfig;
import com.tencent.msdk.dns.MSDKDnsResolver;

public class DNSResolver {
    private static final String TAG = "DNSResolver";
    private static DNSResolver instance;

    private DNSResolver(){}

    public static DNSResolver getInstance(SharedPreferences sharedPreferences, Context context){

        String dnsID = sharedPreferences.getString(Constants.DNS_ID_KEY, "");
        String dnsKey = sharedPreferences.getString(Constants.DNS_KEY, "");

        if(dnsID.isEmpty() || dnsKey.isEmpty()){
            Log.d(TAG, "Method Name : DNSResolver -> Dns id or Dns key is empty");
        } else {
            DnsConfig config = new DnsConfig.Builder()
                    .dnsId(dnsID)
                    .dnsKey(dnsKey)
                    .desHttp()
                    .logLevel(Log.VERBOSE)
                    .build();

            MSDKDnsResolver.getInstance().init(context, config);
        }

        if(instance == null){
            instance = new DNSResolver();
        }

        return instance;
    }

    public void encryptURL(String url, DnsCallback dnsCallback){

        try {

            String host = Utils.getURLHost(url);

            String[] resolveAddress = MSDKDnsResolver.getInstance().getAddrByName(host).split(";");
            String newHost = resolveAddress[0];
            StringBuilder result = new StringBuilder();

            if(!newHost.equals("0")){
                result.append(Utils.replaceURLHost(url, newHost));
                dnsCallback.onFinishEncryption(result.toString());
            }

            else {
                dnsCallback.onFailEncryption();
                Log.d(TAG, "Method Name : encryptURL -> DNS Fail");
            }

        }catch (Exception e){
            Log.d(TAG, "Method Name : encryptURL -> Exception : " + e.getMessage());
            dnsCallback.onFailEncryption();

        }
    }
}
