package com.elbbbird.android.socialsdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by zhanghailong-ms on 2015/4/7.
 */
public class SocialUtils {

    /**
     * 从网络获取图片数据
     *
     * @param url 地址
     * @return 图片数据
     */
    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);

        return data;
    }

    /**
     * 从输入流获取数据
     *
     * @param is 输入流
     * @return 数据
     */
    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 生成时间戳
     *
     * @param type 类型
     * @return 时间戳
     */
    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 压缩数据
     *
     * @param data 数据
     * @param size 最大值
     * @return 压缩后数据
     */
    public static byte[] compressBitmap(byte[] data, float size) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bitmap == null || getSizeOfBitmap(bitmap) <= size) {
            return data;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int quality = 100;
        while ((baos.toByteArray().length / 1024f) > size) {
            quality -= 5;
            baos.reset();
            if (quality <= 0) {
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        byte[] byteData = baos.toByteArray();
        return byteData;
    }

    /**
     * 获取bitmap长度
     *
     * @param bitmap bitmap
     * @return 长度
     */
    private static long getSizeOfBitmap(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        long length = baos.toByteArray().length / 1024;
        return length;
    }

    /**
     * 是否安装微信
     *
     * @param context context
     * @return 是否安装
     */
    public static boolean isInstalledWeChat(Context context) {
        return isPackageInstalled(context, "com.tencent.mm");
    }

    /**
     * 是否安装QQ
     *
     * @param context context
     * @return 是否安装
     */
    public static boolean isInstalledQQ(Context context) {
        return isPackageInstalled(context, "com.tencent.mobileqq");
    }

    /**
     * 是否安装了该包名的app
     *
     * @param context context
     * @param pkgName 包名
     * @return 是否安装
     */
    private static boolean isPackageInstalled(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pkgInfo = pm.getPackageInfo(pkgName, 0);
            return pkgInfo != null && pkgInfo.applicationInfo.enabled;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * 获取微信，朋友圈分享默认icon
     *
     * @param context context
     * @return 数据
     */
    public static byte[] getDefaultShareImage(Context context) {

        return bmpToByteArray(BitmapFactory.decodeResource(context.getResources(), R.drawable.es_icon_default), true);
    }
}
