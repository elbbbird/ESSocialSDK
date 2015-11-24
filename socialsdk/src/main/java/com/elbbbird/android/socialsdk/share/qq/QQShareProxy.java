package com.elbbbird.android.socialsdk.share.qq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

/**
 * QQ分享Proxy
 * Created by zhanghailong-ms on 2015/11/24.
 */
public class QQShareProxy {

    private static Tencent tencent;

    private static Tencent getInstance(Context context, String appId) {
        if (tencent == null)
            tencent = Tencent.createInstance(appId, context);

        return tencent;
    }

    public static void shareToQQ(Context context, String appId, String title, String summary, String url,
                                 String imageUrl, String appName, IUiListener listener) {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
        Tencent tencent = getInstance(context, appId);
        tencent.shareToQQ((Activity) context, params, listener);
    }

    public static void shareToQZone(Context context, String appId, String title, String summary, String url,
                                    String imageUrl, IUiListener listener) {
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
        ArrayList<String> imgs = new ArrayList<String>();
        imgs.add(imageUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgs);
        Tencent tencent = getInstance(context, appId);
        tencent.shareToQzone((Activity) context, params, listener);
    }
}
