package com.elbbbird.android.socialsdk.otto;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * To post from any thread (main or background) and receive on the main thread
 * Created by zhanghailong-ms on 2015/11/24.
 */
public class MainThreadBus extends Bus {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }
}
