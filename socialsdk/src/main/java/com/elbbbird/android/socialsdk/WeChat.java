package com.elbbbird.android.socialsdk;

import android.content.Context;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 之所以抽取WeChat类，是因为IWXAPI需要在SSO授权和分享同时用到
 * Created by zhanghailong-ms on 2015/11/24.
 */
public class WeChat {

    private static IWXAPI api;

    public static IWXAPI getIWXAPIInstance(Context context, String appId) {
        if (null == api) {
            api = WXAPIFactory.createWXAPI(context, appId, true);
            api.registerApp(appId);
        }

        return api;
    }

    public static IWXAPI getIWXAPIInstance() {
        return api;
    }
}
