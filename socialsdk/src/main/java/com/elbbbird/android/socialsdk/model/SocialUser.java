package com.elbbbird.android.socialsdk.model;

/**
 * oauth用户信息
 * <p>
 * Created by zhanghailong-ms on 2015/11/16.
 */
public class SocialUser {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_WEIBO = 1;
    public static final int TYPE_WECHAT = 2;
    public static final int TYPE_QQ = 3;

    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    private int type;
    private String name;
    private String avatar;
    private int gender;
    private String desc;
    private SocialToken token;

    public SocialUser() {
    }

    public SocialUser(int type, String name, String avatar, int gender, SocialToken token) {
        this.type = type;
        this.name = name;
        this.avatar = avatar;
        this.gender = gender;
        this.token = token;
    }

    public SocialUser(int type, String name, String avatar, int gender, String desc, SocialToken token) {
        this.type = type;
        this.name = name;
        this.avatar = avatar;
        this.gender = gender;
        this.desc = desc;
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public SocialToken getToken() {
        return token;
    }

    public void setToken(SocialToken token) {
        this.token = token;
    }

    public boolean isTokenValid() {
        return getToken().getToken() != null && System.currentTimeMillis() < getToken().getExpiresTime();
    }

    @Override
    public String toString() {
        return "SocialUser: type=" + type + ", name=" + name + ", avatar=" + avatar + ", gender=" + gender + ", desc=" + desc
                + ", token=" + token.getToken();
    }
}
