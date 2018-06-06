package com.example.yhadmin.takephotodemo;

/*
 *  @项目名：  yihua-mobile-app-mvms155 
 *  @包名：    com.yihuacomputer.android.util
 *  @文件名:   CameraUtile
 *  @创建者:   YHAdmin
 *  @创建时间:  2018/3/1 10:38
 *  @描述：    相机工具类
 */

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

public class CameraUtile {
    /**
     * 调用相机功能
     *
     * @param
     */
    public static Uri getUriForFile(File file, Context context)
    {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri    uri    = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//兼容Android 7.0
            uri = FileProvider.getUriForFile(context,
                                             context.getApplicationContext().getPackageName() + ".fileprovider",
                                             file);

        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

}
