package com.elbbbird.android.socialsdk.sso.wechat;

import com.elbbbird.android.socialsdk.model.SocialToken;
import com.elbbbird.android.socialsdk.model.SocialUser;

/**
 * Created by zhanghailong-ms on 2015/7/11.
 */
public interface IWXCallback {
    void onGetCodeSuccess(String code);

    void onGetTokenSuccess(SocialToken token);

    void onGetUserInfoSuccess(SocialUser user);

    void onFailure();

    void onCancel();
}
