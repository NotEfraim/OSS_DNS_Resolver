package com.estudio.oss_dns_resolver_v1.model;


public class YumingResponse {
    private boolean isSuccess;
    private String finalUrl;
    private InitActModel data;

    public String getFinalUrl() {
        return finalUrl;
    }

    public void setFinalUrl(String finalUrl) {
        this.finalUrl = finalUrl;
    }
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public InitActModel getData() {
        return data;
    }

    public void setData(InitActModel data) {
        this.data = data;
    }
}
