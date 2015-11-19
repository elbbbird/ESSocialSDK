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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * @author SINA
 * @since 2013-11-22
 */
public class Status {

    public String created_at;
    public String id;
    public String mid;
    public String idstr;
    public String text;
    public String source;
    public boolean favorited;
    public boolean truncated;
    public String in_reply_to_status_id;
    public String in_reply_to_user_id;
    public String in_reply_to_screen_name;
    public String thumbnail_pic;
    public String bmiddle_pic;
    public String original_pic;
    public Geo geo;
    public User user;
    public Status retweeted_status;
    public int reposts_count;
    public int comments_count;
    public int attitudes_count;
    public int mlevel;
    /**
     */
    public Visible visible;
    public ArrayList<String> pic_urls;
    //public Ad ad;
    
    public static Status parse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return Status.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static Status parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        
        Status status = new Status();
        status.created_at       = jsonObject.optString("created_at");
        status.id               = jsonObject.optString("id");
        status.mid              = jsonObject.optString("mid");
        status.idstr            = jsonObject.optString("idstr");
        status.text             = jsonObject.optString("text");
        status.source           = jsonObject.optString("source");
        status.favorited        = jsonObject.optBoolean("favorited", false);
        status.truncated        = jsonObject.optBoolean("truncated", false);
        
        // Have NOT supported
        status.in_reply_to_status_id   = jsonObject.optString("in_reply_to_status_id");
        status.in_reply_to_user_id     = jsonObject.optString("in_reply_to_user_id");
        status.in_reply_to_screen_name = jsonObject.optString("in_reply_to_screen_name");
        
        status.thumbnail_pic    = jsonObject.optString("thumbnail_pic");
        status.bmiddle_pic      = jsonObject.optString("bmiddle_pic");
        status.original_pic     = jsonObject.optString("original_pic");
        status.geo              = Geo.parse(jsonObject.optJSONObject("geo"));
        status.user             = User.parse(jsonObject.optJSONObject("user"));
        status.retweeted_status = Status.parse(jsonObject.optJSONObject("retweeted_status"));
        status.reposts_count    = jsonObject.optInt("reposts_count");
        status.comments_count   = jsonObject.optInt("comments_count");
        status.attitudes_count  = jsonObject.optInt("attitudes_count");
        status.mlevel           = jsonObject.optInt("mlevel", -1);    // Have NOT supported
        status.visible          = Visible.parse(jsonObject.optJSONObject("visible"));
        
        JSONArray picUrlsArray = jsonObject.optJSONArray("pic_urls");
        if (picUrlsArray != null && picUrlsArray.length() > 0) {
            int length = picUrlsArray.length();
            status.pic_urls = new ArrayList<String>(length);
            JSONObject tmpObject = null;
            for (int ix = 0; ix < length; ix++) {
                tmpObject = picUrlsArray.optJSONObject(ix);
                if (tmpObject != null) {
                    status.pic_urls.add(tmpObject.optString("thumbnail_pic"));
                }
            }
        }
        
        //status.ad = jsonObject.optString("ad", "");
        
        return status;
    }
}
