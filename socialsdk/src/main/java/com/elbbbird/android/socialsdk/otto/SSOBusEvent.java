package com.elbbbird.android.socialsdk.otto;

import com.elbbbird.android.socialsdk.model.SocialToken;
import com.elbbbird.android.socialsdk.model.SocialUser;

/**
 * Otto Bus Event
 * Created by zhanghailong-ms on 2015/11/20.
 */
public class SSOBusEvent {

    public static final int TYPE_GET_TOKEN = 0;
    public static final int TYPE_GET_USER = 1;
    public static final int TYPE_FAILURE = 2;
    public static final int TYPE_CANCEL = 3;

    private int type;
    private SocialUser user;
    private SocialToken token;
    private Exception exception;

    public SSOBusEvent(int type) {
        this.type = type;
    }

    public SSOBusEvent(int type, SocialUser user) {
        this.type = type;
        this.user = user;
    }

    public SSOBusEvent(int type, SocialToken token) {
        this.type = type;
        this.token = token;
    }

    public SSOBusEvent(int type, Exception exception) {
        this.type = type;
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
