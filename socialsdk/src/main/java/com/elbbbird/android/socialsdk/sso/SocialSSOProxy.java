package com.elbbbird.android.socialsdk.sso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.elbbbird.android.socialsdk.R;
import com.elbbbird.android.socialsdk.SocialSDK;
import com.elbbbird.android.socialsdk.model.SocialInfo;
import com.elbbbird.android.socialsdk.model.SocialToken;
import com.elbbbird.android.socialsdk.model.SocialUser;
import com.elbbbird.android.socialsdk.otto.BusProvider;
import com.elbbbird.android.socialsdk.otto.SSOBusEvent;
import com.elbbbird.android.socialsdk.sso.qq.QQSSOProxy;
import com.elbbbird.android.socialsdk.sso.wechat.IWXCallback;
import com.elbbbird.android.socialsdk.sso.wechat.WeChatSSOProxy;
import com.elbbbird.android.socialsdk.sso.weibo.User;
import com.elbbbird.android.socialsdk.sso.weibo.WeiboSSOProxy;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 社交授权proxy
 * Created by zhanghailong-ms on 2015/11/16.
 */
public class SocialSSOProxy {

    private static final String TAG = "SocialSSOProxy";

    private static boolean DEBUG = SocialSDK.isDebugModel();

    private static SocialUser user;

    public static void setUser(Context context, SocialUser user) {
        SocialSSOProxy.user = user;
        SocialUserKeeper.writeSocialUser(context, user);
    }

    public static SocialUser getUser(Context context) {
        if (user == null || TextUtils.isEmpty(user.getToken().getOpenId()))
            user = SocialUserKeeper.readSocialUser(context);

        return user;
    }

    public static void removeUser(Context context) {
        SocialUserKeeper.clear(context);
        user = null;
    }

    /**
     * 判断token是否过期
     *
     * @param context context
     * @return 是否过期
     */
    public static boolean isTokenValid(Context context) {
        return getUser(context).isTokenValid();
    }

    /**
     * 登录微博
     *
     * @param context context
     * @param info    社交信息
     */
    public static void loginWeibo(final Context context, final SocialInfo info) {
        if (DEBUG)
            Log.i(TAG, "SocialSSOProxy.loginWeibo");
        WeiboSSOProxy.login(context, info, new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                if (DEBUG)
                    Log.i(TAG, "SocialSSOProxy.loginWeibo#login onComplete");
                final String token = bundle.getString("access_token");
                final String expiresIn = bundle.getString("expires_in", "0");
                final String code = bundle.getString("code");
                final String openId = bundle.getString("uid");
                final SocialToken socialToken = new SocialToken(openId, token, "", Long.valueOf(expiresIn));
                if (DEBUG)
                    Log.i(TAG, "social token info: code=" + code + ", token=" + socialToken.toString());
                getUser(context).setToken(socialToken);
                BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_GET_TOKEN, SSOBusEvent.PLATFORM_WEIBO, socialToken));
                WeiboSSOProxy.getUserInfo(context, info, socialToken, new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        if (DEBUG)
                            Log.i(TAG, "SocialSSOProxy.loginWeibo#getUserInfo onComplete, \n\r" + s);
                        User user = User.parse(s);
                        if (user == null) {
                            BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_FAILURE, SSOBusEvent.PLATFORM_WEIBO, new Exception("Sina user parse error.")));
                            return;
                        }
                        int gender = SocialUser.GENDER_UNKNOWN;
                        if ("f".equals(user.gender))
                            gender = SocialUser.GENDER_FEMALE;
                        else if ("m".equals(user.gender))
                            gender = SocialUser.GENDER_MALE;
                        SocialUser socialUser = new SocialUser(SocialUser.TYPE_WEIBO,
                                user.name, user.profile_image_url, gender, user.description, socialToken);
                        if (DEBUG)
                            Log.i(TAG, socialUser.toString());
                        setUser(context, socialUser);
                        BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_GET_USER, SSOBusEvent.PLATFORM_WEIBO, socialUser));
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        if (DEBUG)
                            Log.i(TAG, "SocialSSOProxy.loginWeibo#getUserInfo onWeiboException, e=" + e.toString());
                        BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_FAILURE, SSOBusEvent.PLATFORM_WEIBO, e));
                    }
                });
            }

            @Override
            public void onWeiboException(WeiboException e) {
                if (DEBUG)
                    Log.i(TAG, "SocialSSOProxy.loginWeibo#login onWeiboException, e=" + e.toString());
                BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_FAILURE, SSOBusEvent.PLATFORM_WEIBO, e));
            }

            @Override
            public void onCancel() {
                if (DEBUG)
                    Log.i(TAG, "SocialSSOProxy.loginWeibo#login onCancel");
                BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_CANCEL, SSOBusEvent.PLATFORM_WEIBO));
            }
        });
    }

    /**
     * 登录微博
     *
     * @param context context
     * @param info    社交信息
     */
    public static void logoutWeibo(final Context context, SocialInfo info) {
        if (DEBUG)
            Log.i(TAG, "SocialSSOProxy.logoutWeibo");

        WeiboSSOProxy.logout(context, info, getUser(context).getToken(), new RequestListener() {
            @Override
            public void onComplete(String s) {
                if (DEBUG)
                    Log.i(TAG, "SocialSSOProxy.logoutWeibo#onComplete, s=" + s);
                removeUser(context);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                if (DEBUG)
                    Log.i(TAG, "SocialSSOProxy.logoutWeibo#onWeiboException, e=" + e.toString());
            }
        });
    }

    /**
     * 微博登录状态回调接口
     *
     * @param context     context
     * @param info        社交信息
     * @param requestCode request
     * @param resultCode  result
     * @param data        data
     */
    public static void loginWeiboCallback(Context context, SocialInfo info, int requestCode, int resultCode, Intent data) {
        if (WeiboSSOProxy.getSsoHandler(context, info) != null)
            WeiboSSOProxy.getSsoHandler(context, info).authorizeCallBack(requestCode, resultCode, data);
    }

    /**
     * 登录微信
     *
     * @param context context
     * @param info    社交信息
     */
    public static void loginWeChat(final Context context, final SocialInfo info) {
        if (DEBUG)
            Log.i(TAG, "SocialSSOProxy.loginWeChat");
        WeChatSSOProxy.login(context, new IWXCallback() {
            @Override
            public void onGetCodeSuccess(String code) {
                if (DEBUG)
                    Log.i(TAG, "SocialSSOProxy.loginWeChat onGetCodeSuccess, code=" + code);
                WeChatSSOProxy.getToken(code, info.getUrlForWeChatToken());
            }

            @Override
            public void onGetTokenSuccess(SocialToken token) {
                if (DEBUG)
                    Log.i(TAG, "SocialSSOProxy.loginWeChat onGetCodeSuccess, token=" + token.toString());
                getUser(context).setToken(token);
                BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_GET_TOKEN, SSOBusEvent.PLATFORM_WECHAT, token));
                WeChatSSOProxy.getUserInfo(context, info.getUrlForWeChatUserInfo(), token);
            }

            @Override
            public void onGetUserInfoSuccess(SocialUser user) {
                if (DEBUG)
                    Log.i(TAG, "SocialSSOProxy.loginWeChat onGetUserSuccess, user=" + user.toString());
                setUser(context, user);
                BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_GET_USER, SSOBusEvent.PLATFORM_WECHAT, user));
            }

            @Override
            public void onFailure(Exception e) {
                if (DEBUG)
                    Log.i(TAG, "SocialSSOProxy.loginWeChat onFailure");
                BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_FAILURE, SSOBusEvent.PLATFORM_WECHAT, e));
            }

            @Override
            public void onCancel() {
                if (DEBUG)
                    Log.i(TAG, "SocialSSOProxy.loginWeChat onCancel");
                BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_CANCEL, SSOBusEvent.PLATFORM_WECHAT));
            }
        }, info);
    }


    /**
     * 登出微信
     *
     * @param context context
     */
    public static void logoutWeChat(Context context) {
        if (DEBUG)
            Log.i(TAG, "SocialSSOProxy.logoutWeChat");
        removeUser(context);
    }

    private static Context context;
    private static SocialInfo info;
    private static IUiListener qqLoginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if (DEBUG)
                Log.i(TAG, "SocialSSOProxy.loginQQ onComplete, info=" + o.toString());
            try {
                JSONObject info = new JSONObject(o.toString());
                final String openId = info.getString("openid");
                final String token = info.getString("access_token");
                final long expiresIn = info.getLong("expires_in");
                final SocialToken socialToken = new SocialToken(openId, token, "", expiresIn);
                getUser(context).setToken(socialToken);
                BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_GET_TOKEN, SSOBusEvent.PLATFORM_QQ, socialToken));
                QQSSOProxy.getUserInfo(context, SocialSSOProxy.info.getQqAppId(), socialToken, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        if (DEBUG)
                            Log.i(TAG, "SocialSSOProxy.loginQQ#getToken onComplete info=" + o.toString());
                        try {
                            JSONObject user = new JSONObject(o.toString());
                            String name = user.getString("nickname");
                            String iconUrl = user.getString("figureurl_qq_2").replace("\\", "");
                            int gender = SocialUser.GENDER_UNKNOWN;
                            if ("女".equals(user.getString("gender")))
                                gender = SocialUser.GENDER_FEMALE;
                            else if ("男".equals(user.getString("gender")))
                                gender = SocialUser.GENDER_MALE;
                            SocialUser socialUser = new SocialUser(SocialUser.TYPE_QQ,
                                    name, iconUrl, gender, socialToken);
                            if (DEBUG)
                                Log.i(TAG, "SocialSSOProxy.loginQQ#getToken onComplete user=" + socialUser.toString());
                            setUser(context, socialUser);
                            BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_GET_USER, SSOBusEvent.PLATFORM_QQ, socialUser));
                        } catch (JSONException e) {
                            BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_FAILURE, SSOBusEvent.PLATFORM_QQ, e));
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {
                        if (DEBUG)
                            Log.i(TAG, "SocialSSOProxy.loginQQ#getToken onError errorCode=" + uiError.errorCode
                                    + ", errorMsg=" + uiError.errorMessage + ", errorDetail=" + uiError.errorDetail);
                        BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_FAILURE, SSOBusEvent.PLATFORM_QQ,
                                new Exception(uiError.errorCode + "#" + uiError.errorMessage + "#" + uiError.errorDetail)));
                    }

                    @Override
                    public void onCancel() {
                        if (DEBUG)
                            Log.i(TAG, "SocialSSOProxy.loginQQ#getToken onCancel");
                        BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_CANCEL, SSOBusEvent.PLATFORM_QQ));
                    }
                });
            } catch (JSONException e) {
                BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_FAILURE, SSOBusEvent.PLATFORM_QQ, e));
            }
        }

        @Override
        public void onError(UiError uiError) {
            if (DEBUG)
                Log.i(TAG, "SocialSSOProxy.loginQQ onError");
            BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_FAILURE, SSOBusEvent.PLATFORM_QQ,
                    new Exception(uiError.errorCode + "#" + uiError.errorMessage + "#" + uiError.errorDetail)));
        }

        @Override
        public void onCancel() {
            if (DEBUG)
                Log.i(TAG, "SocialSSOProxy.loginQQ onCancel");
            BusProvider.getInstance().post(new SSOBusEvent(SSOBusEvent.TYPE_CANCEL, SSOBusEvent.PLATFORM_QQ));
        }
    };

    /**
     * 登录QQ
     *
     * @param context context
     * @param info    社交信息
     */
    public static void loginQQ(Context context, SocialInfo info) {
        if (DEBUG)
            Log.i(TAG, "SocialSSOProxy.loginQQ");
        SocialSSOProxy.context = context;
        SocialSSOProxy.info = info;
        QQSSOProxy.login(context, info.getQqAppId(), info.getQqScope(), qqLoginListener);
    }

    /**
     * 登出QQ
     *
     * @param context context
     */
    public static void logoutQQ(Context context, SocialInfo socialInfo) {
        if (DEBUG)
            Log.i(TAG, "SocialSSOProxy.logoutQQ");
        if (!TextUtils.isEmpty(socialInfo.getQqAppId()))
            QQSSOProxy.logout(context, socialInfo.getQqAppId());
        removeUser(context);
    }

    /**
     * QQ登录状态回调
     *
     * @param requestCode request
     * @param resultCode  result
     * @param data        data
     */
    public static void loginQQCallback(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, qqLoginListener);
    }

    public static void login(Context context, SocialInfo info) {
        if (DEBUG)
            Log.i(TAG, "SocialSSOProxy.login");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        intent.putExtras(bundle);
        intent.setClass(context, SocialOauthActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.es_snack_in, 0);
    }
}
