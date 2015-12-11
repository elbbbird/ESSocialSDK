package com.encore.actionnow;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.elbbbird.android.socialsdk.SocialSDK;
import com.elbbbird.android.socialsdk.model.SocialShareScene;
import com.elbbbird.android.socialsdk.otto.BusProvider;
import com.elbbbird.android.socialsdk.otto.ShareBusEvent;
import com.encore.actionnow.app.BaseActivity;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.squareup.otto.Subscribe;
import com.tencent.connect.common.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity implements IWeiboHandler.Response {

    private static final String TAG = "ShareActivity";

    private SocialShareScene scene = new SocialShareScene(0, "演技派", SocialShareScene.SHARE_TYPE_WECHAT, "Android 开源社会化登录 SDK，支持微信，微博， QQ",
            "像友盟， ShareSDK 等平台也提供类似的 SDK ，之所以造轮子是因为这些平台的 SDK 内部肯定会带有数据统计功能，不想给他们共享数据。",
            "http://cdn.v2ex.co/gravatar/becb0d5c59469a34a54156caef738e90?s=73&d=retro", "http://www.v2ex.com/t/238165");

    @Bind(R.id.share_btn_share_qq)
    Button btnShareQQ;
    @Bind(R.id.share_btn_share_qzone)
    Button btnShareQZone;
    @Bind(R.id.share_btn_share_wechat)
    Button btnShareWeChat;
    @Bind(R.id.share_btn_share_wechat_timeline)
    Button btnShareWeChatTimeline;
    //disable，与SDK内部SocialShareActivity intent-filter冲突
    @Bind(R.id.share_btn_share_weibo)
    Button btnShareWeibo;

    @OnClick({R.id.share_btn_share_qq, R.id.share_btn_share_qzone, R.id.share_btn_share_wechat, R.id.share_btn_share_wechat_timeline, R.id.share_btn_share_weibo})
    public void share(View view) {
        switch (view.getId()) {
            case R.id.share_btn_share_qq:
                SocialSDK.setDebugMode(true);
                SocialSDK.shareToQQ(ShareActivity.this, "1104664609", scene);
                break;
            case R.id.share_btn_share_qzone:
                SocialSDK.setDebugMode(true);
                SocialSDK.shareToQZone(ShareActivity.this, "1104664609", scene);
                break;
            case R.id.share_btn_share_wechat:
                SocialSDK.setDebugMode(true);
                SocialSDK.shareToWeChat(ShareActivity.this, "wx3ecc7ffe590fd845", scene);
                break;
            case R.id.share_btn_share_wechat_timeline:
                SocialSDK.setDebugMode(true);
                SocialSDK.shareToWeChatTimeline(ShareActivity.this, "wx3ecc7ffe590fd845", scene);
                break;
            case R.id.share_btn_share_weibo:
                SocialSDK.setDebugMode(true);
                SocialSDK.shareToWeibo(ShareActivity.this, "1633462674", scene);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*********************************************/
        BusProvider.getInstance().register(this);
        /*********************************************/
    }

    @Subscribe
    public void onShareResult(ShareBusEvent event) {
        switch (event.getType()) {
            case ShareBusEvent.TYPE_SUCCESS:
                Log.i(TAG, "onShareResult#ShareBusEvent.TYPE_SUCCESS " + event.getId());
                break;
            case ShareBusEvent.TYPE_FAILURE:
                Exception e = event.getException();
                Log.i(TAG, "onShareResult#ShareBusEvent.TYPE_FAILURE " + e.toString());
                break;
            case ShareBusEvent.TYPE_CANCEL:
                Log.i(TAG, "onShareResult#ShareBusEvent.TYPE_CANCEL");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        /*********************************************/
        BusProvider.getInstance().unregister(this);
        /*********************************************/
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        SocialSDK.shareToWeiboCallback(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_SUCCESS, scene.getType(), scene.getId()));
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_CANCEL, scene.getType()));
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_FAILURE, scene.getType(), new Exception("WBConstants.ErrorCode.ERR_FAIL")));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_QZONE_SHARE || requestCode == Constants.REQUEST_QQ_SHARE) {
            SocialSDK.shareToQCallback(requestCode, resultCode, data);
        }

    }
}
