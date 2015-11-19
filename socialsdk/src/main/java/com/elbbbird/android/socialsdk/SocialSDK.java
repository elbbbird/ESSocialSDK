package com.elbbbird.android.socialsdk;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.elbbbird.android.socialsdk.model.SocialInfo;
import com.elbbbird.android.socialsdk.sso.ISocialOauthCallback;
import com.elbbbird.android.socialsdk.sso.SocialSSOProxy;

/**
 * 社交SDK
 * <p>
 * Created by zhanghailong-ms on 2015/11/13.
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

    /**
     * 授权微信
     *
     * @param context  context
     * @param callback 回调接口
     */
    public static void oauthWeChat(Context context, ISocialOauthCallback callback) {
        if (!TextUtils.isEmpty(info.getWechatAppId()) && !TextUtils.isEmpty(info.getWeChatAppSecret()))
            SocialSSOProxy.loginWeChat(context, info, callback);
    }

    /**
     * 移除微信授权
     *
     * @param context context
     */
    public static void revokeWeChat(Context context) {
        SocialSSOProxy.logoutWeChat(context);
    }

    /**
     * 微博授权
     *
     * @param context  context
     * @param callback 回调接口
     */
    public static void oauthWeibo(Context context, ISocialOauthCallback callback) {
        if (!TextUtils.isEmpty(info.getWeiboAppKey()))
            SocialSSOProxy.loginWeibo(context, info, callback);
    }

    /**
     * 移除微博授权
     *
     * @param context context
     */
    public static void revokeWeibo(Context context) {
        SocialSSOProxy.logoutWeibo(context, info);
    }

    /**
     * 微博授权回调
     *
     * @param context     contxt
     * @param requestCode request
     * @param resultCode  result
     * @param data        data
     */
    public static void oauthWeiboCallback(Context context, int requestCode, int resultCode, Intent data) {
        SocialSSOProxy.loginWeiboCallback(context, info, requestCode, resultCode, data);
    }

    /**
     * QQ授权
     *
     * @param context  context
     * @param callback 回调接口
     */
    public static void oauthQQ(Context context, ISocialOauthCallback callback) {
        if (!TextUtils.isEmpty(info.getQqAppId()))
            SocialSSOProxy.loginQQ(context, info, callback);
    }

    /**
     * 移除QQ授权
     *
     * @param context context
     */
    public static void revokeQQ(Context context) {
        SocialSSOProxy.logoutQQ(context);
    }

    /**
     * QQ授权回调
     *
     * @param requestCode request
     * @param resultCode  result
     * @param data        data
     */
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
