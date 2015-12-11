package com.encore.actionnow;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.encore.actionnow.app.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SocialActivity extends BaseActivity {

    @Bind(R.id.main_btn_sso)
    Button btnSso;
    @Bind(R.id.main_btn_share)
    Button btnShare;
    @Bind(R.id.main_btn_share_all)
    Button btnShareAll;
    @Bind(R.id.main_btn_sso_all)
    Button btnSsoAll;

    @OnClick({R.id.main_btn_share, R.id.main_btn_sso, R.id.main_btn_share_all, R.id.main_btn_sso_all})
    public void startActivity(View view) {

        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.main_btn_sso:
                intent.setClass(SocialActivity.this, SsoActivity.class);
                break;

            case R.id.main_btn_share:
                intent.setClass(SocialActivity.this, ShareActivity.class);
                break;

            case R.id.main_btn_share_all:
                intent.setClass(SocialActivity.this, ShareAllActivity.class);
                break;

            case R.id.main_btn_sso_all:
                intent.setClass(SocialActivity.this, SsoAllActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    }

}
