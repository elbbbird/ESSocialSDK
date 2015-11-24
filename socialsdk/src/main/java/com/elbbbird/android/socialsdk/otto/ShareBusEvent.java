package com.elbbbird.android.socialsdk.otto;

/**
 * Created by zhanghailong-ms on 2015/11/23.
 */
public class ShareBusEvent {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILURE = 1;
    public static final int TYPE_CANCEL = 2;

    private int type;
    private int id;
    private int platform;
    private Exception exception;

    public ShareBusEvent(int type, int platform) {
        this.type = type;
        this.platform = platform;
    }

    public ShareBusEvent(int type, int platform, int id) {
        this.type = type;
        this.platform = platform;
        this.id = id;
    }

    public ShareBusEvent(int type, int platform, Exception exception) {
        this.type = type;
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
