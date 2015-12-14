# ESSocialSDK

[![Build Status](https://travis-ci.org/ElbbbirdStudio/ESSocialSDK.svg?branch=master)](https://travis-ci.org/ElbbbirdStudio/ESSocialSDK)
[![version](https://img.shields.io/badge/version-0.2.0-brightgreen.svg)]()
[![LICENSE](https://img.shields.io/badge/license-Apache%202-blue.svg)]()
[![forks](https://img.shields.io/github/forks/ElbbbirdStudio/ESSocialSDK.svg)]()
[![stars](https://img.shields.io/github/stars/ElbbbirdStudio/ESSocialSDK.svg)]()


社交登录授权，分享SDK   
支持微信、微博、QQ登录授权   
微信好友、微信朋友圈、微博、QQ好友、QQ空间分享以及系统默认分享   

## 说明
每单个平台全部提供文档说明，内容较多，易混淆集成过程，所以，这里只提供一键登录，一键分享文档，使用默认UI。   
如果需要单个平台的详细集成文档，参考[README_Detail.md](https://github.com/ElbbbirdStudio/ESSocialSDK/blob/master/README_Detail.md)。   
默认UI效果截图：   
![一键登录](https://raw.githubusercontent.com/ElbbbirdStudio/ESSocialSDK/master/screenshots/oauth_all.png)
![一键分享](https://raw.githubusercontent.com/ElbbbirdStudio/ESSocialSDK/master/screenshots/share_all.png)

## Gradle
```groovy
compile 'com.elbbbird.android:socialsdk:0.2.0@aar'
```

## Debug模式
```java
SocialSDK.setDebugMode(true); //默认false
```

## 项目配置
- 配置微博后台回调地址   
SDK的默认回调地址为`http://www.sina.com`，需要在微博后台配置，否则会提示回调地址错误。   
如果在`SocialSDK.init()`方法自定义了回调地址，需要在后台配置为相应地址。

- WXEntryActivity   
创建包名：`package_name.wxapi`   
在该包名下创建类`WXEntryActivity`继承自`WXCallbackActivity`   

```java
package com.encore.actionnow.wxapi;
public class WXEntryActivity extends WXCallbackActivity {

}
```

- AndroidManifest.xml   
```xml
<activity
    android:name=".wxapi.WXEntryActivity"
    android:configChanges="keyboardHidden|orientation|screenSize"
    android:exported="true"
    android:screenOrientation="portrait"
    android:theme="@android:style/Theme.Translucent.NoTitleBar" />
<activity
    android:name="com.tencent.tauth.AuthActivity"
    android:launchMode="singleTask"
    android:noHistory="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <data android:scheme="tencentXXXXXXXXX" />
    </intent-filter>
</activity>
```
**以上配置中的`XXXXXXXXX`换成app_id.**


## 一键登录授权功能（微博，微信，QQ）

### 授权结果回调   
SDK使用了[Otto](http://square.github.io/otto/)作为事件库，用以组件通信。
在调用`SocialSDK.oauth()`接口`Activity`的`onCreate()`方法内添加   
```java
BusProvider.getInstance().register(this);
```
在该`Activity`的`onDestroy()`方法添加   
```java
@Override
protected void onDestroy() {
    BusProvider.getInstance().unregister(this);
    super.onDestroy();
}
```
添加回调接口   
```java
@Subscribe
public void onOauthResult(SSOBusEvent event) {
    switch (event.getType()) {
        case SSOBusEvent.TYPE_GET_TOKEN:
            SocialToken token = event.getToken();
            Log.i(TAG, "onOauthResult#BusEvent.TYPE_GET_TOKEN " + token.toString());
            break;
        case SSOBusEvent.TYPE_GET_USER:
            SocialUser user = event.getUser();
            Log.i(TAG, "onOauthResult#BusEvent.TYPE_GET_USER " + user.toString());
            break;
        case SSOBusEvent.TYPE_FAILURE:
            Exception e = event.getException();
            Log.i(TAG, "onOauthResult#BusEvent.TYPE_FAILURE " + e.toString());
            break;
        case SSOBusEvent.TYPE_CANCEL:
            Log.i(TAG, "onOauthResult#BusEvent.TYPE_CANCEL");
            break;
    }
}
```

### Oauth
```java
SocialSDK.init("wechat_app_id", "wechat_app_secret", "weibo_app_id", "qq_app_id");
SocialSDK.oauth(context);
```

### Revoke
```java
SocialSDK.revoke(context);
```

## 一键分享功能（微博，微信，朋友圈，QQ，QQ空间）

### SDK中`SocialShareScene`的定义
```java
/**
 * 社会化分享数据类
 */
public class SocialShareScene implements Serializable {

    public static final int SHARE_TYPE_DEFAULT = 0;
    public static final int SHARE_TYPE_WEIBO = 1;
    public static final int SHARE_TYPE_WECHAT = 2;
    public static final int SHARE_TYPE_WECHAT_TIMELINE = 3;
    public static final int SHARE_TYPE_QQ = 4;
    public static final int SHARE_TYPE_QZONE = 5;

    /**
     * @param id        分享唯一标识符，可随意指定，会在分享结果ShareBusEvent中返回
     * @param appName   分享到QQ时需要指定，会在分享弹窗中显示该字段
     * @param type      分享类型
     * @param title     标题
     * @param desc      简短描述
     * @param thumbnail 缩略图网址
     * @param url       WEB网址
     */
    public SocialShareScene(int id, String appName, int type, String title, String desc, String thumbnail, String url) {
        this.id = id;
        this.appName = appName;
        this.type = type;
        this.title = title;
        this.desc = desc;
        this.thumbnail = thumbnail;
        this.url = url;
    }

    public SocialShareScene(int id, String appName, String title, String desc, String thumbnail, String url) {
    ....
}
```

**一键分享需要调用第二个构造函数，type类型在SDK内部自动指定**

### 分享结果回调

```java
@Subscribe
public void onShareResult(ShareBusEvent event) {
    switch (event.getType()) {
        case ShareBusEvent.TYPE_SUCCESS:
            Log.i(TAG, "onShareResult#ShareBusEvent.TYPE_SUCCESS " + event.getId());
            break;
        case ShareBusEvent.TYPE_FAILURE:
            Exception e = event.getException();
            Log.i(TAG, "onShareResult#ShareBusEvent.TYPE_FAILURE " + e.toString());
            break;
        case ShareBusEvent.TYPE_CANCEL:
            Log.i(TAG, "onShareResult#ShareBusEvent.TYPE_CANCEL");
            break;
    }
}
```

### ShareTo
```java
SocialSDK.setDebugMode(true);
SocialSDK.init("wechat_app_id", "weibo_app_id", "qq_app_id");
SocialSDK.shareTo(context, scene);
```

## FAQ

- 关于三个平台的账号   
微博应用程序注册完成后，需要在后台配置测试账号，包名，签名信息，然后开始测试；   
微信应用程序注册后，需要配置包名和签名，并提交审核通过，可以获得分享权限。SSO登录权限需要开发者认证。（保护费不到位，测试都不能做）   
QQ需要在后台配置测试账号才能SSO登录。

- 如果只需要SDK中SSO授权和分享功能其中之一，`项目配置`是否有删减？   
集成任何其中一个功能都需要做这些配置。

- 是否需要配置权限？   
SDK已经在aar中添加三个平台需要的权限，以下   
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

- ProGrard代码混淆

```
##微信
-keep class com.tencent.mm.sdk.** {*;}

##微博
-keep public class com.sina.weibo.** {*;}
-keep public class com.sina.sso.** {*;}

##otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}
```

## LICENSE

    Copyright 2015 The ESSocialSDK authors

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
