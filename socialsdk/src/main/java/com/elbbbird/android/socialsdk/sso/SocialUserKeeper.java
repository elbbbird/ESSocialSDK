package com.elbbbird.android.socialsdk.sso;

import android.content.Context;
import android.content.SharedPreferences;

import com.elbbbird.android.socialsdk.model.SocialToken;
import com.elbbbird.android.socialsdk.model.SocialUser;

/**
 * 用户信息持久化
 * <p>
 * Created by zhanghailong-ms on 2015/11/16.
 */
public class SocialUserKeeper {

    private static final String PREFERENCE_NAME = "es_social_user";

    private static final String KEY_TYPE = "type";
    private static final String KEY_OPENID = "open_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EXPIRES_TIME = "expires_time";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_SIGNATURE = "signature";

    /**
     * 持久化用户信息
     *
     * @param context context
     * @param user    用户信息
     */
    protected static void writeSocialUser(Context context, SocialUser user) {
        if (user == null || context == null)
            return;

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_TYPE, user.getType());
        editor.putString(KEY_OPENID, user.getToken().getOpenId());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_AVATAR, user.getAvatar());
        editor.putInt(KEY_GENDER, user.getGender());
        editor.putString(KEY_TOKEN, user.getToken().getToken());
        editor.putString(KEY_REFRESH_TOKEN, user.getToken().getRefreshToken());
        editor.putLong(KEY_EXPIRES_TIME, user.getToken().getExpiresTime());
        editor.putString(KEY_SIGNATURE, user.getDesc());
        editor.commit();
    }

    /**
     * 读取用户信息
     *
     * @param context context
     * @return 用户信息
     */
    protected static SocialUser readSocialUser(Context context) {
        if (context == null)
            return null;

        SocialUser user = new SocialUser();
        SocialToken token = new SocialToken();
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        user.setType(preferences.getInt(KEY_TYPE, 0));
        user.setName(preferences.getString(KEY_NAME, ""));
        user.setAvatar(preferences.getString(KEY_AVATAR, ""));
        user.setGender(preferences.getInt(KEY_GENDER, 0));
        user.setDesc(preferences.getString(KEY_SIGNATURE, ""));
        token.setOpenId(preferences.getString(KEY_OPENID, ""));
        token.setToken(preferences.getString(KEY_TOKEN, ""));
        token.setRefreshToken(preferences.getString(KEY_REFRESH_TOKEN, ""));
        token.setExpiresTime(preferences.getLong(KEY_EXPIRES_TIME, 0));
        user.setToken(token);

        return user;
    }

    /**
     * 清除用户信息
     *
     * @param context context
     */
    protected static void clear(Context context) {
        if (null == context)
            return;

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
