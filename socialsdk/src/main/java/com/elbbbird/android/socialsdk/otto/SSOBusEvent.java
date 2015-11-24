package com.elbbbird.android.socialsdk.otto;

import com.elbbbird.android.socialsdk.model.SocialToken;
import com.elbbbird.android.socialsdk.model.SocialUser;

/**
 * Otto Bus Event
 * Created by zhanghailong-ms on 2015/11/20.
 */
public class SSOBusEvent {

    public static final int PLATFORM_DEFAULT = 0;
    public static final int PLATFORM_WEIBO = 1;
    public static final int PLATFORM_WECHAT = 2;
    public static final int PLATFORM_QQ = 3;

    public static final int TYPE_GET_TOKEN = 0;
    public static final int TYPE_GET_USER = 1;
    public static final int TYPE_FAILURE = 2;
    public static final int TYPE_CANCEL = 3;

    private int type;
    private int platform;
    private SocialUser user;
    private SocialToken token;
    private Exception exception;

    public SSOBusEvent(int type, int platform) {
        this.type = type;
        this.platform = platform;
    }

    public SSOBusEvent(int type, int platform, SocialUser user) {
        this.type = type;
        this.platform = platform;
        this.user = user;
    }

    public SSOBusEvent(int type, int platform, SocialToken token) {
        this.type = type;
        this.platform = platform;
        this.token = token;
    }

    public SSOBusEvent(int type, int platform, Exception exception) {
        this.type = type;
        this.platform = platform;
        this.exception = exception;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SocialUser getUser() {
        return user;
    }

    public void setUser(SocialUser user) {
        this.user = user;
    }

    public SocialToken getToken() {
        return token;
    }

    public void setToken(SocialToken token) {
        this.token = token;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
