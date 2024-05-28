package com.estudio.oss_dns_resolver_v1.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseModel implements Serializable {
    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return getCode() == 0;
    }

}