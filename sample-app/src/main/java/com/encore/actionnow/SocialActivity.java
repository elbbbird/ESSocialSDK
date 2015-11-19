package com.encore.actionnow;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.elbbbird.android.socialsdk.SocialSDK;
import com.tencent.connect.common.Constants;

public class SocialActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLoginWeibo;
    private Button btnLogoutWeibo;
    private Button btnLoginWeChat;
    private Button btnLogoutWeChat;
    private Button btnLoginQQ;
    private Button btnLogoutQQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLoginWeibo = (Button) findViewById(R.id.btn_login_weibo);
        btnLoginWeibo.setOnClickListener(this);
        btnLogoutWeibo = (Button) findViewById(R.id.btn_logout_weibo);
        btnLogoutWeibo.setOnClickListener(this);
        btnLoginWeChat = (Button) findViewById(R.id.btn_login_wechat);
        btnLoginWeChat.setOnClickListener(this);
        btnLogoutWeChat = (Button) findViewById(R.id.btn_logout_wechat);
        btnLogoutWeChat.setOnClickListener(this);
        btnLoginQQ = (Button) findViewById(R.id.btn_login_qq);
        btnLoginQQ.setOnClickListener(this);
        btnLogoutQQ = (Button) findViewById(R.id.btn_logout_qq);
        btnLogoutQQ.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                throw new RuntimeException("this is a test of BugHDDDDDD.");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_weibo:
                SocialSDK.setDebugMode(true);
                SocialSDK.initWeibo("1633462674");
                SocialSDK.oauthWeibo(SocialActivity.this);
                break;
            case R.id.btn_logout_weibo:
                SocialSDK.destroyWeibo(SocialActivity.this);
                break;
            case R.id.btn_login_wechat:
                SocialSDK.setDebugMode(true);
                SocialSDK.initWeChat("wx3ecc7ffe590fd845", "1b3f07fa99d82232d360c359f6504980");
                SocialSDK.oauthWeChat(SocialActivity.this);
                break;
            case R.id.btn_logout_wechat:
                SocialSDK.destroyWeChat(SocialActivity.this);
                break;
            case R.id.btn_login_qq:
                SocialSDK.setDebugMode(true);
                SocialSDK.initQQ("1104664609");
                SocialSDK.oauthQQ(SocialActivity.this);
                break;
            case R.id.btn_logout_qq:
                SocialSDK.destroyQQ(SocialActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialSDK.oauthWeiboCallback(SocialActivity.this, requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            SocialSDK.oauthQQCallback(requestCode, resultCode, data);
        }
    }
}
