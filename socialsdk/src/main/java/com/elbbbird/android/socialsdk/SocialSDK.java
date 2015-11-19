package com.elbbbird.android.socialsdk;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.elbbbird.android.socialsdk.model.SocialInfo;
import com.elbbbird.android.socialsdk.sso.SocialSSOProxy;

/**
 * Created by zhanghailong-ms on 2015/11/13.
 * 社交SDK
 */
public class SocialSDK {

    private static SocialInfo info = new SocialInfo();

    /**
     * 设置调试模式
     *
     * @param debug 是否调试模式
     */
    public static void setDebugMode(boolean debug) {
        info.setDebugMode(debug);
    }

    /**
     * 判断是否是debug模式
     *
     * @return 是否debug
     */
    public static boolean isDebugModel() {
        return info.isDebugMode();
    }

    /**
     * 初始化微信
     *
     * @param id     app id
     * @param secret 密钥
     */
    public static void initWeChat(String id, String secret) {
        info.setWechatAppId(id);
        info.setWeChatAppSecret(secret);
    }

    /**
     * 初始化微信
     *
     * @param id     app id
     * @param secret 密钥
     * @param scope  oauth权限
     */
    public static void initWeChat(String id, String secret, String scope) {
        info.setWechatAppId(id);
        info.setWeChatAppSecret(secret);
        info.setWeChatScope(scope);
    }

    /**
     * 初始化微博
     *
     * @param key app key
     */
    public static void initWeibo(String key) {
        info.setWeiboAppKey(key);
    }

    /**
     * 初始化微博
     *
     * @param key         app key
     * @param redirectUrl 服务器回调地址
     */
    public static void initWeibo(String key, String redirectUrl) {
        info.setWeiboAppKey(key);
        info.setWeiboRedirectrUrl(redirectUrl);
    }

    /**
     * 初始化微博
     *
     * @param key         app key
     * @param redirectUrl 服务器回调地址
     * @param scope       oauth权限
     */
    public static void initWeibo(String key, String redirectUrl, String scope) {
        info.setWeiboAppKey(key);
        info.setWeiboRedirectrUrl(redirectUrl);
        info.setWeiboScope(scope);
    }

    /**
     * 初始化QQ
     *
     * @param id app id
     */
    public static void initQQ(String id) {
        info.setQqAppId(id);
    }

    /**
     * 初始化QQ
     *
     * @param id    app id
     * @param scope oauth权限范围
     */
    public static void initQQ(String id, String scope) {
        info.setQqAppId(id);
        info.setQqScope(scope);
    }

    public static void oauth() {

    }

    public static void oauthWeChat(Context context) {
        if (!TextUtils.isEmpty(info.getWechatAppId()) && !TextUtils.isEmpty(info.getWeChatAppSecret()))
            SocialSSOProxy.loginWeChat(context, info);
    }

    public static void revokeWeChat(Context context) {
        SocialSSOProxy.logoutWeChat(context);
    }

    public static void oauthWeibo(Context context) {
        if (!TextUtils.isEmpty(info.getWeiboAppKey()))
            SocialSSOProxy.loginWeibo(context, info);
    }

    public static void revokeWeibo(Context context) {
        SocialSSOProxy.logoutWeibo(context, info);
    }

    public static void oauthWeiboCallback(Context context, int requestCode, int resultCode, Intent data) {
        SocialSSOProxy.loginWeiboCallback(context, info, requestCode, resultCode, data);
    }

    public static void oauthQQ(Context context) {
        if (!TextUtils.isEmpty(info.getQqAppId()))
            SocialSSOProxy.loginQQ(context, info);
    }

    public static void revokeQQ(Context context) {
        SocialSSOProxy.logoutQQ(context);
    }

    public static void oauthQQCallback(int requestCode, int resultCode, Intent data) {
        SocialSSOProxy.loginQQCallback(requestCode, resultCode, data);
    }

    public static void shareTo() {

    }

    public static void shareToWeChat() {

    }

    public static void shareToWeibo() {

    }

    public static void shareToQQ() {

    }

}
