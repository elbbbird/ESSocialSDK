package com.elbbbird.android.socialsdk.sso.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * 微信授权，分享回调activity
 * <p>
 * Created by zhanghailong-ms on 2015/7/11.
 */

public class WXCallbackActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        IWXAPI api = WeChatSSOProxy.getIWXAPIInstance();
        if (null != api)
            api.handleIntent(intent, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        IWXAPI api = WeChatSSOProxy.getIWXAPIInstance();
        if (null != api)
            api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof SendAuth.Resp)
            WeChatSSOProxy.authComplete((SendAuth.Resp) resp);
        else if (resp instanceof SendMessageToWX.Resp) {
            //TODO wechat share
//            WeixinShareAgent.shareComplete((SendMessageToWX.Resp) resp);
        }

        finish();
    }


}
