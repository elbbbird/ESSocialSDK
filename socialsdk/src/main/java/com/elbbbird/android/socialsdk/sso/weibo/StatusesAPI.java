/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elbbbird.android.socialsdk.sso.weibo;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * @author SINA
 * @since 2014-03-03
 */
public class StatusesAPI extends AbsOpenAPI {

    public static final int FEATURE_ALL = 0;
    public static final int FEATURE_ORIGINAL = 1;
    public static final int FEATURE_PICTURE = 2;
    public static final int FEATURE_VIDEO = 3;
    public static final int FEATURE_MUSICE = 4;

    public static final int AUTHOR_FILTER_ALL = 0;
    public static final int AUTHOR_FILTER_ATTENTIONS = 1;
    public static final int AUTHOR_FILTER_STRANGER = 2;

    public static final int SRC_FILTER_ALL = 0;
    public static final int SRC_FILTER_WEIBO = 1;
    public static final int SRC_FILTER_WEIQUN = 2;

    public static final int TYPE_FILTER_ALL = 0;
    public static final int TYPE_FILTER_ORIGAL = 1;

    /**
     * API URL
     */
    private static final String API_BASE_URL = API_SERVER + "/statuses";

    private static final int READ_API_FRIENDS_TIMELINE = 0;
    private static final int READ_API_MENTIONS = 1;
    private static final int WRITE_API_UPDATE = 2;
    private static final int WRITE_API_REPOST = 3;
    private static final int WRITE_API_UPLOAD = 4;
    private static final int WRITE_API_UPLOAD_URL_TEXT = 5;

    private static final SparseArray<String> sAPIList = new SparseArray<String>();

    static {
        sAPIList.put(READ_API_FRIENDS_TIMELINE, API_BASE_URL + "/friends_timeline.json");
        sAPIList.put(READ_API_MENTIONS, API_BASE_URL + "/mentions.json");
        sAPIList.put(WRITE_API_REPOST, API_BASE_URL + "/repost.json");
        sAPIList.put(WRITE_API_UPDATE, API_BASE_URL + "/update.json");
        sAPIList.put(WRITE_API_UPLOAD, API_BASE_URL + "/upload.json");
        sAPIList.put(WRITE_API_UPLOAD_URL_TEXT, API_BASE_URL + "/upload_url_text.json");
    }

    public StatusesAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    public void friendsTimeline(long since_id, long max_id, int count, int page, boolean base_app,
                                int featureType, boolean trim_user, RequestListener listener) {
        WeiboParameters params =
                buildTimeLineParamsBase(since_id, max_id, count, page, base_app, trim_user, featureType);
        requestAsync(sAPIList.get(READ_API_FRIENDS_TIMELINE), params, HTTPMETHOD_GET, listener);
    }

    public void mentions(long since_id, long max_id, int count, int page, int authorType, int sourceType,
                         int filterType, boolean trim_user, RequestListener listener) {
        WeiboParameters params = buildMentionsParams(since_id, max_id, count, page, authorType, sourceType, filterType, trim_user);
        requestAsync(sAPIList.get(READ_API_MENTIONS), params, HTTPMETHOD_GET, listener);
    }

    public void update(String content, String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        requestAsync(sAPIList.get(WRITE_API_UPDATE), params, HTTPMETHOD_POST, listener);
    }

    public void upload(String content, Bitmap bitmap, String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        params.put("pic", bitmap);
        requestAsync(sAPIList.get(WRITE_API_UPLOAD), params, HTTPMETHOD_POST, listener);
    }

    public void uploadUrlText(String status, String imageUrl, String pic_id, String lat, String lon,
                              RequestListener listener) {
        WeiboParameters params = buildUpdateParams(status, lat, lon);
        params.put("url", imageUrl);
        params.put("pic_id", pic_id);
        requestAsync(sAPIList.get(WRITE_API_UPLOAD_URL_TEXT), params, HTTPMETHOD_POST, listener);
    }

    public String friendsTimelineSync(long since_id, long max_id, int count, int page, boolean base_app, int featureType,
                                      boolean trim_user) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page, base_app,
                trim_user, featureType);
        return requestSync(sAPIList.get(READ_API_FRIENDS_TIMELINE), params, HTTPMETHOD_GET);
    }

    public String mentionsSync(long since_id, long max_id, int count, int page,
                               int authorType, int sourceType, int filterType, boolean trim_user) {
        WeiboParameters params = buildMentionsParams(since_id, max_id, count, page, authorType, sourceType, filterType, trim_user);
        return requestSync(sAPIList.get(READ_API_MENTIONS), params, HTTPMETHOD_GET);
    }

    public String updateSync(String content, String lat, String lon) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        return requestSync(sAPIList.get(WRITE_API_UPDATE), params, HTTPMETHOD_POST);
    }

    public String uploadSync(String content, Bitmap bitmap, String lat, String lon) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        params.put("pic", bitmap);
        return requestSync(sAPIList.get(WRITE_API_UPLOAD), params, HTTPMETHOD_POST);
    }

    public String uploadUrlTextSync(String status, String imageUrl, String pic_id, String lat, String lon) {
        WeiboParameters params = buildUpdateParams(status, lat, lon);
        params.put("url", imageUrl);
        params.put("pic_id", pic_id);
        return requestSync(sAPIList.get(WRITE_API_UPLOAD_URL_TEXT), params, HTTPMETHOD_POST);
    }

    private WeiboParameters buildTimeLineParamsBase(long since_id, long max_id, int count, int page,
                                                    boolean base_app, boolean trim_user, int featureType) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("since_id", since_id);
        params.put("max_id", max_id);
        params.put("count", count);
        params.put("page", page);
        params.put("base_app", base_app ? 1 : 0);
        params.put("trim_user", trim_user ? 1 : 0);
        params.put("feature", featureType);
        return params;
    }

    private WeiboParameters buildUpdateParams(String content, String lat, String lon) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("status", content);
        if (!TextUtils.isEmpty(lon)) {
            params.put("long", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            params.put("lat", lat);
        }
        return params;
    }

    private WeiboParameters buildMentionsParams(long since_id, long max_id, int count, int page,
                                                int authorType, int sourceType, int filterType, boolean trim_user) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("since_id", since_id);
        params.put("max_id", max_id);
        params.put("count", count);
        params.put("page", page);
        params.put("filter_by_author", authorType);
        params.put("filter_by_source", sourceType);
        params.put("filter_by_type", filterType);
        params.put("trim_user", trim_user ? 1 : 0);

        return params;
    }
}
