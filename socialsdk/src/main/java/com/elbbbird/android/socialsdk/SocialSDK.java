package com.elbbbird.android.socialsdk;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.elbbbird.android.socialsdk.model.SocialInfo;
import com.elbbbird.android.socialsdk.model.SocialShareScene;
import com.elbbbird.android.socialsdk.share.SocialShareProxy;
import com.elbbbird.android.socialsdk.sso.SocialInfoKeeper;
import com.elbbbird.android.socialsdk.sso.SocialSSOProxy;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

/**
 * 社交SDK
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

    /**
     * 初始化微信，微博，QQ三个平台信息
     *
     * @param weChatId 微信id
     * @param weiboKey 微博 id
     * @param qqId     QQ id
     */
    public static void init(String weChatId, String weiboKey, String qqId) {
        info.setWechatAppId(weChatId);
        info.setWeiboAppKey(weiboKey);
        info.setQqAppId(qqId);
    }

    /**
     * 初始化微信，微博，QQ三个平台信息
     *
     * @param weChatId     微信id
     * @param weChatSecret 微信 secret
     * @param weiboKey     微博 id
     * @param qqId         QQ id
     */
    public static void init(String weChatId, String weChatSecret, String weiboKey, String qqId) {
        info.setWechatAppId(weChatId);
        info.setWeChatAppSecret(weChatSecret);
        info.setWeiboAppKey(weiboKey);
        info.setQqAppId(qqId);
    }

    /**
     * 初始化微信，微博，QQ三个平台信息
     *
     * @param weChatId         微信id
     * @param weChatSecret     微信 secret
     * @param weiboKey         微博 id
     * @param weiboRedirectUrl 微博回调地址
     * @param qqId             QQ id
     */
    public static void init(String weChatId, String weChatSecret, String weiboKey, String weiboRedirectUrl, String qqId) {
        info.setWechatAppId(weChatId);
        info.setWeChatAppSecret(weChatSecret);
        info.setWeiboAppKey(weiboKey);
        info.setWeiboRedirectrUrl(weiboRedirectUrl);
        info.setQqAppId(qqId);
    }

    /**
     * 一键登录，显示SDK默认登录界面
     *
     * @param context context
     */
    public static void oauth(Context context) {
        if (!TextUtils.isEmpty(info.getWechatAppId())
                && !TextUtils.isEmpty(info.getWeChatAppSecret())
                && !TextUtils.isEmpty(info.getWeiboAppKey())
                && !TextUtils.isEmpty(info.getQqAppId())) {
            SocialInfoKeeper.writeSocialInfo(context, info);
            SocialSSOProxy.login(context, info);
        }
    }

    /**
     * 一键解除授权
     *
     * @param context context
     */
    public static void revoke(Context context) {
        revokeWeibo(context);
        revokeQQ(context);
        revokeWeChat(context);
    }

    /**
     * 授权微信
     *
     * @param context context
     */
    public static void oauthWeChat(Context context) {
        if (!TextUtils.isEmpty(info.getWechatAppId()) && !TextUtils.isEmpty(info.getWeChatAppSecret())) {
            SocialInfoKeeper.writeSocialInfo(context, info);
            SocialSSOProxy.loginWeChat(context, info);
        }
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
     * @param context context
     */
    public static void oauthWeibo(Context context) {
        if (!TextUtils.isEmpty(info.getWeiboAppKey())) {
            SocialInfoKeeper.writeSocialInfo(context, info);
            SocialSSOProxy.loginWeibo(context, info);
        }
    }

    /**
     * 移除微博授权
     *
     * @param context context
     */
    public static void revokeWeibo(Context context) {
        info = SocialInfoKeeper.readSocialInfo(context);
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
     * @param context context
     */
    public static void oauthQQ(Context context) {
        if (!TextUtils.isEmpty(info.getQqAppId())) {
            SocialInfoKeeper.writeSocialInfo(context, info);
            SocialSSOProxy.loginQQ(context, info);
        }
    }

    /**
     * 移除QQ授权
     *
     * @param context context
     */
    public static void revokeQQ(Context context) {
        info = SocialInfoKeeper.readSocialInfo(context);
        SocialSSOProxy.logoutQQ(context, info);
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

    /**
     * 一键分享，默认UI
     *
     * @param context context
     * @param scene   社会化分享数据
     */
    public static void shareTo(Context context, SocialShareScene scene) {
        SocialShareProxy.share(context, info, scene);
    }

    /**
     * 分享到微信
     *
     * @param context context
     * @param appId   app id
     * @param scene   社会化分享数据
     */
    public static void shareToWeChat(Context context, String appId, SocialShareScene scene) {
        SocialShareProxy.shareToWeChat(context, appId, scene);
    }

    /**
     * 分享到微信朋友圈
     *
     * @param context context
     * @param scene   社会化分享数据
     */
    public static void shareToWeChatTimeline(Context context, String appId, SocialShareScene scene) {
        SocialShareProxy.shareToWeChatTimeline(context, appId, scene);
    }

    /**
     * 分享到微博
     *
     * @param context context
     * @param appKey  appkey
     * @param scene   社会化分享数据
     */
    public static void shareToWeibo(Context context, String appKey, SocialShareScene scene) {
        SocialShareProxy.shareToWeibo(context, appKey, "", scene);
    }

    /**
     * 分享到QQ
     *
     * @param context context
     * @param scene   社会化分享数据
     */
    public static void shareToQQ(Context context, String appId, SocialShareScene scene) {
        SocialShareProxy.shareToQQ(context, appId, scene);
    }

    /**
     * 分享到QQ空间
     *
     * @param context context
     * @param scene   社会化分享数据
     */
    public static void shareToQZone(Context context, String appId, SocialShareScene scene) {
        SocialShareProxy.shareToQZone(context, appId, scene);
    }

    /**
     * QQ分享回调
     *
     * @param requestCode request
     * @param resultCode  result
     * @param data        data
     */
    public static void shareToQCallback(int requestCode, int resultCode, Intent data) {
        SocialShareProxy.shareToQCallback(requestCode, resultCode, data);
    }

    public static void shareToWeiboCallback(Intent intent, IWeiboHandler.Response response) {
        SocialShareProxy.shareToWeiboCallback(intent, response);
    }

}
