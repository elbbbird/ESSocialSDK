package com.elbbbird.android.socialsdk.sso.weibo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.elbbbird.android.socialsdk.SocialSDK;
import com.elbbbird.android.socialsdk.model.SocialInfo;
import com.elbbbird.android.socialsdk.model.SocialToken;
import com.elbbbird.android.socialsdk.sso.SocialSSOProxy;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.net.RequestListener;

/**
 * 微博授权proxy
 * Created by zhanghailong-ms on 2015/11/16.
 */
public class WeiboSSOProxy {

    private static final boolean DEBUG = SocialSDK.isDebugModel();
    private static final String TAG = "WeiboSSOProxy";

    private static SsoHandler ssoHandler;
    private static AuthInfo authInfo;

    public static SsoHandler getSsoHandler(Context context, SocialInfo info) {
        if (ssoHandler == null)
            ssoHandler = new SsoHandler((Activity) context, getAuthInfo(context, info.getWeiboAppKey(), info.getWeiboRedirectrUrl(), info.getWeiboScope()));

        return ssoHandler;
    }

    private static AuthInfo getAuthInfo(Context context, String key, String redirectUrl, String scope) {
        if (authInfo == null || !key.equals(authInfo.getAppKey()))
            authInfo = new AuthInfo(context, key, redirectUrl, scope);

        return authInfo;
    }

    public static void login(Context context, SocialInfo info, WeiboAuthListener listener) {
        if (!SocialSSOProxy.isTokenValid(context)) {
            getSsoHandler(context, info).authorize(listener);
        }
    }

    public static void logout(Context context, SocialInfo info, SocialToken token, RequestListener listener) {
        if (SocialSSOProxy.isTokenValid(context)) {
            LogoutAPI logout = new LogoutAPI(context, info.getWeiboAppKey(), parseToken(token));
            logout.logout(listener);
        }

        ssoHandler = null;
        authInfo = null;
    }

    public static void getUserInfo(Context context, SocialInfo info, SocialToken token, RequestListener listener) {
        if (DEBUG)
            Log.i(TAG, "getUserInfo");
        if (SocialSSOProxy.isTokenValid(context)) {
            if (DEBUG)
                Log.i(TAG, "getUserInfo#isTokenValid true");
            UsersAPI usersAPI = new UsersAPI(context, info.getWeiboAppKey(), parseToken(token));
            usersAPI.show(Long.parseLong(token.getOpenId()), listener);
        } else {
            if (DEBUG)
                Log.i(TAG, "getUserInfo#isTokenValid false");
        }
    }

    private static Oauth2AccessToken parseToken(SocialToken socialToken) {
        Oauth2AccessToken token = new Oauth2AccessToken();
        token.setUid(socialToken.getOpenId());
        token.setExpiresTime(socialToken.getExpiresTime());
        token.setToken(socialToken.getToken());

        return token;
    }
}
