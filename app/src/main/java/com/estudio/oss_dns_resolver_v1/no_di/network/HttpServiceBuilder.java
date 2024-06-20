package com.estudio.oss_dns_resolver_v1.no_di.network;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class HttpServiceBuilder {

    private String url = "";
    private String method = "";
    private HashMap<String, String> headers;
    private HashMap<String, String> params;
    private Builder builder =  new HttpServiceBuilder.Builder();
    private HttpServiceBuilder(@NonNull Builder builder) {
        this.builder = builder;
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.params = builder.params;
    }

    public Builder newBuilder() {
        return builder;
    }

    public String getURL() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public static class Builder {
        String url = "";
        String method = "";
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();

        public Builder setURL(String url) {
            this.url = url;
            return this;
        }

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder addHeader(String ... header){
            this.headers.put(header[0], header[1]);
            return this;
        }

        public Builder setParams(HashMap<String, String> params){
            this.params = params;
            return this;
        }

        public HttpServiceBuilder build() {
            return new HttpServiceBuilder(this);
        }
    }
}
