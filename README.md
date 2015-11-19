# ESSocialSDK [![Build Status](https://travis-ci.org/ElbbbirdStudio/ESSocialSDK.svg?branch=master)](https://travis-ci.org/ElbbbirdStudio/ESSocialSDK)
社交登录，分享SDK。支持微信、微博、QQ登录，微信好友、微信朋友圈、微博、QQ好友、QQ空间分享。

## Gradle
```groovy
compile 'com.elbbbird.android:socialsdk:0.1.0@aar'
```

## 使用指南

### Debug模式
```java
SocialSDK.setDebugMode(true);
```
默认`false`

### SSO登录功能

#### 微博
- oauth
```java
SocialSDK.initWeibo("app_key");
SocialSDK.oauthWeibo(context);
```
- revoke
```java
SocialSDK.revokeWeibo(context);
```

#### 微信
- WXEntryActivity

  - 创建包名：package_name.wxapi
  - 在该包名下创建类`WXEntryActivity`继承自`WXCallbackActivity`

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

#### QQ
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

- revoke
```java
SocialSDK.revokeQQ(context);
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
