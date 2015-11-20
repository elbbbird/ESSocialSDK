package com.elbbbird.android.socialsdk.sso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.elbbbird.android.socialsdk.R;
import com.elbbbird.android.socialsdk.SocialSDK;
import com.elbbbird.android.socialsdk.model.SocialInfo;
import com.elbbbird.android.socialsdk.model.SocialToken;
import com.elbbbird.android.socialsdk.model.SocialUser;
import com.elbbbird.android.socialsdk.otto.BusEvent;
import com.elbbbird.android.socialsdk.otto.BusProvider;
import com.tencent.connect.common.Constants;

/**
 * 社交授权activity
 * <p/>
 * Created by zhanghailong-ms on 2015/11/20.
 */
public class SocialOauthActivity extends Activity {

    private static final String TAG = "SocialOauthActivity";
    private static boolean DEBUG = SocialSDK.isDebugModel();

    private SocialInfo info;
    private LinearLayout llWeibo;
    private LinearLayout llWeChat;
    private LinearLayout llQQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_social_oauth);

        info = (SocialInfo) getIntent().getExtras().getSerializable("info");

        llWeibo = (LinearLayout) findViewById(R.id.social_oauth_ll_weibo);
        llWeibo.setOnClickListener(weiboClickListener);
        llWeChat = (LinearLayout) findViewById(R.id.social_oauth_ll_wechat);
        llWeChat.setOnClickListener(wechatClickListener);
        llQQ = (LinearLayout) findViewById(R.id.login_dialog_ll_qq);
        llQQ.setOnClickListener(qqClickListener);

    }

    ISocialOauthCallback callback = new ISocialOauthCallback() {
        @Override
        public void onGetTokenSuccess(final SocialToken token) {
            if (DEBUG)
                Log.i(TAG, "onGetTokenSuccess" + token.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BusProvider.getInstance().post(new BusEvent(BusEvent.TYPE_GET_TOKEN, token));
                }
            });

        }

        @Override
        public void onGetUserSuccess(final SocialUser user) {
            if (DEBUG)
                Log.i(TAG, "onGetUserSuccess# " + user.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BusProvider.getInstance().post(new BusEvent(BusEvent.TYPE_GET_USER, user));
                }
            });
            finish();
        }

        @Override
        public void onFailure(final Exception e) {
            if (DEBUG)
                Log.i(TAG, "onFailure# " + e.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BusProvider.getInstance().post(new BusEvent(BusEvent.TYPE_FAILURE, e));
                }
            });
            finish();
        }

        @Override
        public void onCancel() {
            if (DEBUG)
                Log.i(TAG, "onCancel#");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BusProvider.getInstance().post(new BusEvent(BusEvent.TYPE_CANCEL));
                }
            });
            finish();
        }
    };

    View.OnClickListener weiboClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SocialSSOProxy.loginWeibo(SocialOauthActivity.this, info, callback);
        }
    };

    View.OnClickListener wechatClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SocialSSOProxy.loginWeChat(SocialOauthActivity.this, info, callback);
        }
    };

    View.OnClickListener qqClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SocialSSOProxy.loginQQ(SocialOauthActivity.this, info, callback);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialSDK.oauthWeiboCallback(SocialOauthActivity.this, requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            SocialSDK.oauthQQCallback(requestCode, resultCode, data);
        }
    }
}
