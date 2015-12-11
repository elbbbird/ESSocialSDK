# ESSocialSDK [![Build Status](https://travis-ci.org/ElbbbirdStudio/ESSocialSDK.svg?branch=master)](https://travis-ci.org/ElbbbirdStudio/ESSocialSDK)
社交登录授权，分享SDK   
支持微信、微博、QQ登录授权   
微信好友、微信朋友圈、微博、QQ好友、QQ空间分享以及系统默认分享

## Gradle

```groovy
compile 'com.elbbbird.android:socialsdk:0.2.0@aar'
```

## Debug模式
```java
SocialSDK.setDebugMode(true); //默认false
```

## 社交平台SSO授权功能

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
public void onOauthResult(BusEvent event) {
    switch (event.getType()) {
        case BusEvent.TYPE_GET_TOKEN:
            SocialToken token = event.getToken();
            Log.i(TAG, "onOauthResult#BusEvent.TYPE_GET_TOKEN " + token.toString());
            break;
        case BusEvent.TYPE_GET_USER:
            SocialUser user = event.getUser();
            Log.i(TAG, "onOauthResult#BusEvent.TYPE_GET_USER " + user.toString());
            break;
        case BusEvent.TYPE_FAILURE:
            Exception e = event.getException();
            Log.i(TAG, "onOauthResult#BusEvent.TYPE_FAILURE " + e.toString());
            break;
        case BusEvent.TYPE_CANCEL:
            Log.i(TAG, "onOauthResult#BusEvent.TYPE_CANCEL");
            break;
    }
}
```

### 微博授权
- 配置微博后台回调地址   
SDK的默认回调地址为`http://www.sina.com`，需要在微博后台配置，否则会提示回调地址错误。   
如果在`SocialSDK.initWeibo()`方法自定义了回调地址，需要在后台配置为相应地址。
- oauth
```java
SocialSDK.initWeibo("app_key");
SocialSDK.oauthWeibo(context);
```
- onActivityResult
```java
SocialSDK.oauthWeiboCallback(context, requestCode, resultCode, data);
```

- revoke
```java
SocialSDK.revokeWeibo(context);
```

### 微信授权

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
```

- oauth
```java
SocialSDK.initWeChat("app_id", "app_secret");
SocialSDK.oauthWeChat(context);
```

- revoke
```java
SocialSDK.revokeWeChat(context);
```

### QQ授权
- AndroidManifest.xml
```xml
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

- oauth
```java
SocialSDK.initQQ(app_id);
SocialSDK.oauthQQ(context);
```

- onActivityResult
```java
if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
    SocialSDK.oauthQQCallback(requestCode, resultCode, data);
}
```

- revoke
```java
SocialSDK.revokeQQ(context);
```

### SDK默认授权界面，展示全平台授权接口
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

- oauth
```java
SocialSDK.init("wechat_app_id", "wechat_app_secret", "weibo_app_id", "qq_app_id");
SocialSDK.oauth(context);
```

- revoke
```java
SocialSDK.revoke(context);
```

## 社交平台分享功能

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

## FAQ

- 关于三个平台的账号   
微博应用程序注册完成后，需要在后台配置测试账号，包名，签名信息，然后开始测试；   
微信应用程序注册后，需要配置包名和签名，并提交审核通过，可以获得分享权限。SSO登录权限需要开发者认证。（保护费不到位，测试都不能做）   
QQ需要在后台配置测试账号才能SSO登录。

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
