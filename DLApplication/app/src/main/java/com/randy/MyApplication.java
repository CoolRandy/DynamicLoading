package com.randy;

import android.app.Application;
import android.content.Context;

import com.randy.hook.HookHelper;

/**
 * Created by randy on 2016/11/16.
 * 此项目是采用博客http://weishu.me/2016/01/28/understand-plugin-framework-overview/进行学习
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //如果需要Hook Activity类中的startActivity，可以直接在Application中的attachBaseContext中执行
        try {
            HookHelper.attachContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
