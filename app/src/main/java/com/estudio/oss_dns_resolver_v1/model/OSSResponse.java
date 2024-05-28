package com.estudio.oss_dns_resolver_v1.model;

import com.google.gson.annotations.SerializedName;

public class OSSResponse {
    @SerializedName("isSuccess")
    private boolean isSuccess;
    @SerializedName("data")
    private InitModel data;

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setData(InitModel data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public InitModel getData() {
        return data;
    }
}
