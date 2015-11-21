package com.encore.actionnow.app;

import android.app.Application;

/**
 * Created by zhanghailong-ms on 2015/11/13.
 * <p>
 * 自定义Application
 */
public class SocialSDKApplication extends Application {

    @Override
    public void onCreate() {

        initBugHD();
        super.onCreate();
    }

    /**
     * 初始化BugHD，实时监控APP的崩溃
     */
    private void initBugHD() {
    }
}
