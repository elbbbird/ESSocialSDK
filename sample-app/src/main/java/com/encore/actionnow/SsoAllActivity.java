package com.encore.actionnow;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.elbbbird.android.socialsdk.SocialSDK;
import com.elbbbird.android.socialsdk.model.SocialToken;
import com.elbbbird.android.socialsdk.model.SocialUser;
import com.elbbbird.android.socialsdk.otto.BusProvider;
import com.elbbbird.android.socialsdk.otto.SSOBusEvent;
import com.encore.actionnow.app.BaseActivity;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SsoAllActivity extends BaseActivity {

    private static final String TAG = "SsoAllActivity";

    @Bind(R.id.sso_all_btn_login_all)
    Button btnLoginAll;
    @Bind(R.id.sso_all_btn_logout_all)
    Button btnLogoutAll;

    @OnClick(R.id.sso_all_btn_login_all)
    public void login(View view) {
        SocialSDK.setDebugMode(true);
        SocialSDK.init("wx3ecc7ffe590fd845", "1b3f07fa99d82232d360c359f6504980", "1633462674", "1104664609");
        SocialSDK.oauth(SsoAllActivity.this);
    }

    @OnClick(R.id.sso_all_btn_logout_all)
    public void logout(View view) {
        SocialSDK.revoke(SsoAllActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sso_all);
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

        /**************************************************/
        BusProvider.getInstance().register(this);
        /**************************************************/
    }

    @Subscribe
    public void onOauthResult(SSOBusEvent event) {
        switch (event.getType()) {
            case SSOBusEvent.TYPE_GET_TOKEN:
                SocialToken token = event.getToken();
                Log.i(TAG, "onOauthResult#BusEvent.TYPE_GET_TOKEN " + token.toString());
                break;
            case SSOBusEvent.TYPE_GET_USER:
                SocialUser user = event.getUser();
                Log.i(TAG, "onOauthResult#BusEvent.TYPE_GET_USER " + user.toString());
                Toast.makeText(SsoAllActivity.this, "ShareBusEvent.TYPE_GET_USER \n\r" + user.toString(), Toast.LENGTH_SHORT).show();
                break;
            case SSOBusEvent.TYPE_FAILURE:
                Exception e = event.getException();
                Log.i(TAG, "onOauthResult#BusEvent.TYPE_FAILURE " + e.toString());
                break;
            case SSOBusEvent.TYPE_CANCEL:
                Log.i(TAG, "onOauthResult#BusEvent.TYPE_CANCEL");
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
