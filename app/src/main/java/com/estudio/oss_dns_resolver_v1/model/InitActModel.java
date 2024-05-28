package com.estudio.oss_dns_resolver_v1.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InitActModel extends BaseModel implements Serializable {

    @SerializedName("data")
    private InitActInfo data = new InitActInfo();
    public InitActInfo getData() {
        return data;
    }

    public void setData(InitActInfo data) {
        this.data = data;
    }

    /**
     * customer_url (string, optional): 客服地址 ,
     * down_url (string, optional): 下载地址 ,
     * has_new (boolean, optional): 是否有新版本 ,
     * head_img_url (string, optional): 头像地址 ,
     * latest_fore (string, optional): 1=强更0=不强更 ,
     * latest_version (string, optional): 最新版本号 ,
     * live_config (RspLiveConfig, optional): 直播间配置 ,
     * idle (integer, optional): 心跳时间 ,
     * update_text (string, optional): 更新内容
     */
    public class InitActInfo {
        @SerializedName("customer_url")
        private String customer_url;
        @SerializedName("customer_url2")
        private String customer_url2;
        @SerializedName("down_url")
        private String down_url;
        @SerializedName("web_url")
        private String web_url;
        @SerializedName("has_new")
        private boolean has_new;
        @SerializedName("latest_fore")
        private String latest_fore;
        @SerializedName("latest_version")
        private String latest_version;
        @SerializedName("live_config")
        private LiveConfig live_config=new LiveConfig();
        @SerializedName("idle")
        private int idle;
        @SerializedName("login_verif_url")
        private String login_verif_url;
        @SerializedName("update_text")
        private String update_text;
        @SerializedName("captchaId")
        private String captchaId;
        @SerializedName("action_switch")
        private String action_switch;
        @SerializedName("action_sms_switch")
        private Boolean action_sms_switch;
        @SerializedName("product_id")
        private String product_id;
        @SerializedName("defaultUrl")
        private String defaultUrl;
        @SerializedName("defaultOss")
        private String defaultOss;
        @SerializedName("defaultCustUrl")
        private String defaultCustUrl;
        @SerializedName("dnsId")
        private String dnsId;
        @SerializedName("dnsKey")
        private String dnsKey;

        public String getCustomer_url2() {
            return customer_url2;
        }

        public void setCustomer_url2(String customer_url2) {
            this.customer_url2 = customer_url2;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getAction_switch() {
            return action_switch;
        }

        public Boolean getAction_sms_switch() {
            return action_sms_switch;
        }

        public void setAction_sms_switch(Boolean action_sms_switch) {
            this.action_sms_switch = action_sms_switch;
        }

        public void setAction_switch(String action_switch) {
            this.action_switch = action_switch;
        }

        public String getCaptchaId() {
            return captchaId;
        }

        public void setCaptchaId(String captchaId) {
            this.captchaId = captchaId;
        }

        public String getCustomer_url() {
            return customer_url;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public void setCustomer_url(String customer_url) {
            this.customer_url = customer_url;
        }

        public String getDown_url() {
            return down_url;
        }

        public void setDown_url(String down_url) {
            this.down_url = down_url;
        }

        public boolean isHas_new() {
            return has_new;
        }

        public void setHas_new(boolean has_new) {
            this.has_new = has_new;
        }

        public String getLatest_fore() {
            return latest_fore;
        }

        public void setLatest_fore(String latest_fore) {
            this.latest_fore = latest_fore;
        }

        public String getLatest_version() {
            return latest_version;
        }

        public void setLatest_version(String latest_version) {
            this.latest_version = latest_version;
        }

        public LiveConfig getLive_config() {
            return live_config;
        }

        public void setLive_config(LiveConfig live_config) {
            this.live_config = live_config;
        }

        public String getLogin_verif_url() {
            return login_verif_url;
        }

        public void setLogin_verif_url(String login_verif_url) {
            this.login_verif_url = login_verif_url;
        }

        public String getUpdate_text() {
            return update_text;
        }

        public void setUpdate_text(String update_text) {
            this.update_text = update_text;
        }

        public int getIdle() {
            return idle;
        }

        public void setIdle(int idle) {
            this.idle = idle;
        }

        public String getDefaultUrl() {
            return defaultUrl;
        }

        public void setDefaultUrl(String defaultUrl) {
            this.defaultUrl = defaultUrl;
        }

        public String getDefaultOss() {
            return defaultOss;
        }

        public void setDefaultOss(String defaultOss) {
            this.defaultOss = defaultOss;
        }

        public String getDefaultCustUrl() {
            return defaultCustUrl;
        }

        public void setDefaultCustUrl(String defaultCustUrl) {
            this.defaultCustUrl = defaultCustUrl;
        }

        public String getDnsId() {
            return dnsId;
        }

        public void setDnsId(String dnsId) {
            this.dnsId = dnsId;
        }

        public String getDnsKey() {
            return dnsKey;
        }

        public void setDnsKey(String dnsKey) {
            this.dnsKey = dnsKey;
        }
    }

    /**
     * danmu_fee (integer, optional): 弹幕费用 ,
     * diamond_name (string, optional): 钻石名称 ,
     * gen_tou_url (string, optional): 跟投url ,
     * heCai6 (HeCai6Color, optional): 6合彩色 ,
     * jr_user_level (integer, optional): 金光一闪等级 ,
     * on_line_group_id (string, optional): 在线群ID ,
     * send_msg_lv (integer, optional): 发言等级限制 ,
     * tim_sdkappid (string, optional): IM腾讯ID
     */
    public class LiveConfig {
        @SerializedName("jr_user_level")
        private int jr_user_level;
        @SerializedName("on_line_group_id")
        private String on_line_group_id;
        @SerializedName("gen_tou_url")
        private String gen_tou_url;
        @SerializedName("send_msg_lv")
        private int send_msg_lv;
        @SerializedName("livePayPreview")
        private int livePayPreview;
        @SerializedName("tim_sdkappid")
        private String tim_sdkappid;
        @SerializedName("heCai6")
        private LiuHeCaiModel heCai6=new LiuHeCaiModel();
        @SerializedName("danmu_fee")
        private double danmu_fee;
        @SerializedName("diamond_name")
        private String diamond_name;
        @SerializedName("identifier")
        private String identifier;
        @SerializedName("wheel_pool_group_id")
        private String wheel_pool_group_id;

        public int getJr_user_level() {
            return jr_user_level;
        }

        public void setJr_user_level(int jr_user_level) {
            this.jr_user_level = jr_user_level;
        }

        public String getOn_line_group_id() {
            return on_line_group_id;
        }

        public void setOn_line_group_id(String on_line_group_id) {
            this.on_line_group_id = on_line_group_id;
        }

        public int getSend_msg_lv() {
            return send_msg_lv;
        }

        public void setSend_msg_lv(int send_msg_lv) {
            this.send_msg_lv = send_msg_lv;
        }

        public String getTim_sdkappid() {
            return tim_sdkappid;
        }

        public void setTim_sdkappid(String tim_sdkappid) {
            this.tim_sdkappid = tim_sdkappid;
        }

        public LiuHeCaiModel getHeCai6() {
            return heCai6;
        }

        public void setHeCai6(LiuHeCaiModel heCai6) {
            this.heCai6 = heCai6;
        }

        public String getGen_tou_url() {
            return gen_tou_url;
        }

        public void setGen_tou_url(String gen_tou_url) {
            this.gen_tou_url = gen_tou_url;
        }

        public int getLivePayPreview() {
            return livePayPreview;
        }

        public void setLivePayPreview(int livePayPreview) {
            this.livePayPreview = livePayPreview;
        }
        public double getDanmu_fee() {
            return danmu_fee;
        }

        public void setDanmu_fee(double danmu_fee) {
            this.danmu_fee = danmu_fee;
        }

        public String getDiamond_name() {
            return diamond_name;
        }

        public void setDiamond_name(String diamond_name) {
            this.diamond_name = diamond_name;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }
        public String getWheel_pool_group_id() {
            return wheel_pool_group_id;
        }

        public void setWheel_pool_group_id(String wheel_pool_group_id) {
            this.wheel_pool_group_id = wheel_pool_group_id;
        }
    }
}
