package com.elbbbird.android.socialsdk.share;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.elbbbird.android.socialsdk.R;

/**
 * 一键社会化分享
 * Created by zhanghailong-ms on 2015/11/23.
 */
public class SocialShareActivity extends Activity implements View.OnClickListener {

    private LinearLayout space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_social_share);

        initViews();
    }

    private void initViews() {
        space = (LinearLayout) findViewById(R.id.social_share_space);
        space.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == space.getId()) {
            finish();
        }
    }
}
