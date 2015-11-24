package com.elbbbird.android.socialsdk.sso.wechat;

import android.content.Context;

import com.elbbbird.android.socialsdk.WeChat;
import com.elbbbird.android.socialsdk.model.SocialInfo;
import com.elbbbird.android.socialsdk.model.SocialToken;
import com.elbbbird.android.socialsdk.model.SocialUser;
import com.elbbbird.android.socialsdk.sso.SocialSSOProxy;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 微信授权proxy
 * <p>
 * Created by zhanghailong-ms on 2015/11/17.
 */
public class WeChatSSOProxy {

    private static IWXCallback callback;

    public static void login(Context context, IWXCallback callback, SocialInfo info) {
        if (!SocialSSOProxy.isTokenValid(context)) {
            WeChatSSOProxy.callback = callback;
            SendAuth.Req req = new SendAuth.Req();
            req.scope = info.getWeChatScope();
            WeChat.getIWXAPIInstance(context, info.getWechatAppId()).sendReq(req);
        }
    }

    public static void authComplete(SendAuth.Resp resp) {

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                callback.onGetCodeSuccess(resp.code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                callback.onCancel();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                callback.onFailure(new Exception("BaseResp.ErrCode.ERR_AUTH_DENIED"));
                break;
        }
    }

    public static void getToken(final String code, final String getTokenUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(String.format(getTokenUrl, code));
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConn.getInputStream());

                    String result = inputStream2String(in);
                    try {
                        JSONObject info = new JSONObject(result);
                        //当前token的有效市场只有7200s，需要利用refresh_token去获取新token，考虑当前需要利用token的只有获取用户信息，手动设置token超时为30天
                        SocialToken token = new SocialToken(info.getString("openid"), info.getString("access_token"), info.getString("refresh_token"), /*info.getLong("expires_in")*/ 30 * 24 * 60 * 60);
                        callback.onGetTokenSuccess(token);
                    } catch (JSONException e) {
                        callback.onFailure(e);
                    }
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            }
        }).start();

    }

    public static void getUserInfo(final Context context, final String getUserInfoUrl, final SocialToken token) {
        if (SocialSSOProxy.isTokenValid(context)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(String.format(getUserInfoUrl, token.getToken(), token.getOpenId()));
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        InputStream in = new BufferedInputStream(urlConn.getInputStream());

                        String result = inputStream2String(in);
                        try {
                            JSONObject info = new JSONObject(result);
                            String name = info.getString("nickname");
                            int gender = info.getInt("sex");
                            String icon = info.getString("headimgurl");

                            SocialUser user = new SocialUser(SocialUser.TYPE_WECHAT,
                                    name, icon, gender, token);

                            callback.onGetUserInfoSuccess(user);
                        } catch (JSONException e) {
                            callback.onFailure(e);
                        }
                    } catch (Exception e) {
                        callback.onFailure(e);
                    }
                }
            }).start();
        }
    }

    private static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }
}
