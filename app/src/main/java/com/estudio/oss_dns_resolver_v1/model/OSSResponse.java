package com.estudio.oss_dns_resolver_v1.model;


import java.io.Serializable;

public class OSSResponse implements Serializable {
    private boolean isSuccess;
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
