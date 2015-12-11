package com.encore.actionnow;

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
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareAllActivity extends BaseActivity {

    private static final String TAG = "ShareAllActivity";

    private SocialShareScene scene = new SocialShareScene(0, "演技派", SocialShareScene.SHARE_TYPE_WECHAT, "Android 开源社会化登录 SDK，支持微信，微博， QQ",
            "像友盟， ShareSDK 等平台也提供类似的 SDK ，之所以造轮子是因为这些平台的 SDK 内部肯定会带有数据统计功能，不想给他们共享数据。",
            "http://cdn.v2ex.co/gravatar/becb0d5c59469a34a54156caef738e90?s=73&d=retro", "http://www.v2ex.com/t/238165");

    @Bind(R.id.share_all_btn_share_all)
    Button btnShareAll;

    @OnClick(R.id.share_all_btn_share_all)
    public void share(View view) {
        SocialSDK.setDebugMode(true);
        SocialSDK.init("wx3ecc7ffe590fd845", "1633462674", "1104664609");
        SocialSDK.shareTo(ShareAllActivity.this, scene);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_all);
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

}
