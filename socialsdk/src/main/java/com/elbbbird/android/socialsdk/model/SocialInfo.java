package com.elbbbird.android.socialsdk.model;

import java.io.Serializable;

/**
 * 社交平台信息
 * Created by zhanghailong-ms on 2015/11/13.
 */
public class SocialInfo implements Serializable {

    private boolean debugMode = false;
    private String wechatAppId = "";
    private String weChatAppSecret = "";
    private String weChatScope = "snsapi_userinfo";
    private String weiboAppKey = "";
    private String weiboRedirectrUrl = "http://www.sina.com";
    private String weiboScope = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    private String qqAppId = "";
    private String qqScope = "all";

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public String getWechatAppId() {
        return wechatAppId;
    }

    public void setWechatAppId(String wechatAppId) {
        this.wechatAppId = wechatAppId;
    }

    public String getWeChatAppSecret() {
        return weChatAppSecret;
    }

    public void setWeChatAppSecret(String weChatAppSecret) {
        this.weChatAppSecret = weChatAppSecret;
    }

    public String getWeChatScope() {
        return weChatScope;
    }

    public void setWeChatScope(String weChatScope) {
        this.weChatScope = weChatScope;
    }

    public String getWeiboAppKey() {
        return weiboAppKey;
    }

    public void setWeiboAppKey(String weiboAppKey) {
        this.weiboAppKey = weiboAppKey;
    }

    public String getWeiboRedirectrUrl() {
        return weiboRedirectrUrl;
    }

    public void setWeiboRedirectrUrl(String weiboRedirectrUrl) {
        this.weiboRedirectrUrl = weiboRedirectrUrl;
    }

    public String getWeiboScope() {
        return weiboScope;
    }

    public void setWeiboScope(String weiboScope) {
        this.weiboScope = weiboScope;
    }

    public String getQqAppId() {
        return qqAppId;
    }

    public void setQqAppId(String qqAppId) {
        this.qqAppId = qqAppId;
    }

    public String getQqScope() {
        return qqScope;
    }

    public void setQqScope(String qqScope) {
        this.qqScope = qqScope;
    }

    public String getUrlForWeChatToken() {
        return "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + getWechatAppId()
                + "&secret="
                + getWeChatAppSecret()
                + "&code=%s&grant_type=authorization_code";
    }

    public String getUrlForWeChatUserInfo() {
        return "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
    }

    public String getUrlForWeChatRefreshToken() {
        return "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="
                + getWechatAppId()
                + "&grant_type=refresh_token&refresh_token=%s";
    }
}
