package com.elbbbird.android.socialsdk.model;

/**
 * oauth token信息
 * <p>
 * Created by zhanghailong-ms on 2015/11/16.
 */
public class SocialToken {

    private String openId;
    private String token;
    private String refreshToken;
    private long expiresTime;

    public SocialToken() {
    }

    public SocialToken(String openId, String token, String refreshToken, long expiresTime) {
        this.openId = openId;
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresTime = System.currentTimeMillis() + expiresTime * 1000L;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    @Override
    public String toString() {
        return "SocialToken# openId=" + openId + ", token=" + token + ", refreshToken=" + refreshToken + ", expiresTime=" + expiresTime;
    }
}
