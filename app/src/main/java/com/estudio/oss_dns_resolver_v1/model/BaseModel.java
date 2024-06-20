package com.estudio.oss_dns_resolver_v1.model;



import java.io.Serializable;

public class BaseModel implements Serializable {
    private int code;
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