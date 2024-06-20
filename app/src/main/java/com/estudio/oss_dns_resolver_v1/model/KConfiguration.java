package com.estudio.oss_dns_resolver_v1.model;

import androidx.lifecycle.LifecycleOwner;

import com.estudio.oss_dns_resolver_v1.utils.KEventListener;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class KConfiguration implements Serializable {

    // Fields
    private String dnsKey = "";
    private String dnsId = "";
    private String agent = "";
    private String version = "";
    private String defaultUrl = "";
    private List<String> ossList = Collections.emptyList();
    private List<String> yumingList = Collections.emptyList();
    private LifecycleOwner lifecycleOwner;

    private KEventListener kEventListener;

    public String getDnsKey() {
        return dnsKey;
    }

    public String getDnsId() {
        return dnsId;
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public String getAgent() {
        return agent;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getOssList() {
        return ossList;
    }

    public List<String> getYumingList() {
        return yumingList;
    }

    public LifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }

    public KEventListener getKEventListener() {
        return kEventListener;
    }

    private KConfiguration(Builder builder){
        this.dnsKey = builder.dnsKey;
        this.dnsId = builder.dnsId;
        this.defaultUrl = builder.defaultUrl;
        this.ossList = builder.ossList;
        this.yumingList = builder.yumingList;
        this.lifecycleOwner = builder.lifecycleOwner;
        this.kEventListener = builder.kEventListener;
        this.agent = builder.agent;
        this.version = builder.version;

    }

    public static class Builder {
        private String dnsKey = "";
        private String dnsId = "";
        private String agent = "";
        private String version = "";
        private String defaultUrl = "";
        private List<String> ossList = Collections.emptyList();
        private List<String> yumingList = Collections.emptyList();
        private LifecycleOwner lifecycleOwner;
        private KEventListener kEventListener;

        public Builder dnsId(String dnsID) {
            this.dnsId = dnsID;
            return this;
        }

        public Builder dnsKey(String dnsKey) {
            this.dnsKey = dnsKey;
            return this;
        }

        public Builder agent(String agent) {
            this.agent = agent;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder defaultUrl(String defaultUrl) {
            this.defaultUrl = defaultUrl;
            return this;
        }

        public Builder ossList(List<String> ossList) {
            this.ossList = ossList;
            return this;
        }

        public Builder yumingList(List<String> yumingList) {
            this.yumingList = yumingList;
            return this;
        }

        public Builder lifecycleOwner(LifecycleOwner lifecycleOwner) {
            this.lifecycleOwner = lifecycleOwner;
            return this;
        }

        public Builder kEventListener(KEventListener kEventListener) {
            this.kEventListener = kEventListener;
            return this;
        }

        public KConfiguration build() {
            return new KConfiguration(this);
        }
    }
}
