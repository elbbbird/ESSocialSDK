package com.elbbbird.android.socialsdk.sso.qq;

import android.app.Activity;
import android.content.Context;

import com.elbbbird.android.socialsdk.model.SocialToken;
import com.elbbbird.android.socialsdk.sso.SocialSSOProxy;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

/**
 * QQ授权proxy
 * <p>
 * Created by zhanghailong-ms on 2015/11/17.
 */
public class QQSSOProxy {

    private static Tencent tencent;

    public static Tencent getTencentInstance(Context context, String appId) {
        if (tencent == null) {
            tencent = Tencent.createInstance(appId, context);
        }

        return tencent;
    }

    public static void login(Context context, String appId, String scope, IUiListener listener) {
        Tencent tencent = getTencentInstance(context, appId);
        if (!SocialSSOProxy.isTokenValid(context)) {
            tencent.login((Activity) context, scope, listener);
        }
    }

    public static void logout(Context context, String appId) {
        Tencent tencent = getTencentInstance(context, appId);
        if (SocialSSOProxy.isTokenValid(context)) {
            tencent.logout(context);
        }

        QQSSOProxy.tencent = null;
    }

    public static void getUserInfo(Context context, String appId, SocialToken token, IUiListener listener) {
        //这里只是为了在刷新用户信息的时候，设置一下全局变量。场景：APP升级安装，没有重新登录过程，调用该接口刷新用户信息，
        //如果没有该过程刷新Global.getContext()等常量，会出现空指针
        getTencentInstance(context, appId);

        if (SocialSSOProxy.isTokenValid(context)) {
            UserInfo info = new UserInfo(context, parseToken(appId, token));
            info.getUserInfo(listener);
        }
    }

    private static QQToken parseToken(String appId, SocialToken socialToken) {
        QQToken token = new QQToken(appId);
        //3600是随意定义的，不影响token的使用
        token.setAccessToken(socialToken.getToken(), "3600");
        token.setOpenId(socialToken.getOpenId());

        return token;
    }
}
