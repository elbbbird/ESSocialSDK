package com.elbbbird.android.socialsdk.sso;

import com.elbbbird.android.socialsdk.model.SocialToken;
import com.elbbbird.android.socialsdk.model.SocialUser;

/**
 * 社交授权回调接口
 * <p>
 * Created by zhanghailong-ms on 2015/11/19.
 */
public interface ISocialOauthCallback {

    void onGetTokenSuccess(SocialToken token);

    void onGetUserSuccess(SocialUser user);

    void onFailure(Exception e);

    void onCancel();
}
