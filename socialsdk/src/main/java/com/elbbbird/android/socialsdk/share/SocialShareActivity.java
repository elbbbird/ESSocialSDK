package com.elbbbird.android.socialsdk.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.elbbbird.android.socialsdk.R;
import com.elbbbird.android.socialsdk.SocialSDK;
import com.elbbbird.android.socialsdk.model.SocialInfo;
import com.elbbbird.android.socialsdk.model.SocialShareScene;
import com.elbbbird.android.socialsdk.otto.BusProvider;
import com.elbbbird.android.socialsdk.otto.ShareBusEvent;
import com.elbbbird.android.socialsdk.view.ShareButton;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.common.Constants;

/**
 * 一键社会化分享
 * Created by zhanghailong-ms on 2015/11/23.
 */
public class SocialShareActivity extends Activity implements IWeiboHandler.Response {

    private SocialInfo info;
    private SocialShareScene scene;

    private ShareButton sbWechat;
    private ShareButton sbWeChatTimeline;
    private ShareButton sbWeibo;
    private ShareButton sbQQ;
    private ShareButton sbQZone;
    private ShareButton sbMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.es_activity_social_share);

        getWindow().setGravity(Gravity.BOTTOM);

        info = (SocialInfo) getIntent().getExtras().getSerializable("info");
        scene = (SocialShareScene) getIntent().getExtras().getSerializable("scene");

        initViews();
    }

    private void initViews() {
        sbWechat = (ShareButton) findViewById(R.id.social_share_sb_wechat);
        sbWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scene.setType(SocialShareScene.SHARE_TYPE_WECHAT);
                SocialSDK.shareToWeChat(SocialShareActivity.this, info.getWechatAppId(), scene);
            }
        });
        sbWeChatTimeline = (ShareButton) findViewById(R.id.social_share_sb_wechat_timeline);
        sbWeChatTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialSDK.shareToWeChatTimeline(SocialShareActivity.this, info.getWechatAppId(), scene);
                scene.setType(SocialShareScene.SHARE_TYPE_WECHAT_TIMELINE);
            }
        });
        sbWeibo = (ShareButton) findViewById(R.id.social_share_sb_weibo);
        sbWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scene.setType(SocialShareScene.SHARE_TYPE_WEIBO);
                SocialSDK.shareToWeibo(SocialShareActivity.this, info.getWeiboAppKey(), scene);
            }
        });
        sbQQ = (ShareButton) findViewById(R.id.social_share_sb_qq);
        sbQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scene.setType(SocialShareScene.SHARE_TYPE_QQ);
                SocialSDK.shareToQQ(SocialShareActivity.this, info.getQqAppId(), scene);
            }
        });
        sbQZone = (ShareButton) findViewById(R.id.social_share_sb_qzone);
        sbQZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scene.setType(SocialShareScene.SHARE_TYPE_QZONE);
                SocialSDK.shareToQZone(SocialShareActivity.this, info.getQqAppId(), scene);
            }
        });
        sbMore = (ShareButton) findViewById(R.id.social_share_sb_more);
        sbMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scene.setType(SocialShareScene.SHARE_TYPE_DEFAULT);
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, scene.getTitle() + "\n\r" + scene.getUrl());
                share.putExtra(Intent.EXTRA_TITLE, scene.getTitle());
                share.putExtra(Intent.EXTRA_SUBJECT, scene.getDesc());
                startActivity(share);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.es_snack_out);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (scene.getType() == SocialShareScene.SHARE_TYPE_WEIBO) {
            SocialSDK.shareToWeiboCallback(intent, this);
            finish();
        }
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
                BusProvider.getInstance().post(new ShareBusEvent(ShareBusEvent.TYPE_FAILURE, scene.getType(), new Exception("WBConstants.ErrorCode.ERR_FAIL: "
                        + baseResponse.errMsg)));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_QZONE_SHARE || requestCode == Constants.REQUEST_QQ_SHARE) {
            SocialSDK.shareToQCallback(requestCode, resultCode, data);
            finish();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (scene.getType() == SocialShareScene.SHARE_TYPE_WECHAT || scene.getType() == SocialShareScene.SHARE_TYPE_WECHAT_TIMELINE) {
            finish();
        }
    }

}
