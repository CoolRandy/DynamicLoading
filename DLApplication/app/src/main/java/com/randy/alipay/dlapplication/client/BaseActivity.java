package com.randy.alipay.dlapplication.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by randy on 2016/9/3.
 */
public class BaseActivity extends Activity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    public static final String FROM = "extra.from";

    public static final int FROM_EXTERNAL = 0;

    public static final int FROM_INTERNAL = 1;

    public static final String EXTRA_DEX_PATH = "extra.dex.path";

    public static final String EXTRA_CLASS = "extra.class";

    public static final String PROXY_VIEW_ACTION = "com.randy.alipay.dlapplication.host.VIEW";

    public static final String DEX_PATH = "/mnt/sdcard/DynamicLoadHost/plugin.apk";

    protected Activity mProxyActivity;

    protected int mFrom = FROM_INTERNAL;

    public void setProxy(Activity proxyActivity){
        Log.e(TAG, "setProxy: proxy activity: " + proxyActivity);
        mProxyActivity = proxyActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "execute BaseActivity onCreate");
        if (savedInstanceState != null){
            mFrom = savedInstanceState.getInt(FROM, FROM_INTERNAL);
        }
        Log.e(TAG, "mFrom is: " + mFrom);
        if (mFrom == FROM_INTERNAL){
            super.onCreate(savedInstanceState);
            mProxyActivity = this;
        }
        Log.e(TAG, "onCreate from: " + mFrom);
    }

    protected void startActivityByProxy(String className){

        if (mProxyActivity == this){

            Intent intent = new Intent();
            intent.setClassName(this, className);
            this.startActivity(intent);
        }else {
            Intent intent = new Intent(PROXY_VIEW_ACTION);
            intent.putExtra(EXTRA_DEX_PATH, DEX_PATH);
            intent.putExtra(EXTRA_CLASS, className);
            mProxyActivity.startActivity(intent);
        }
    }

    @Override
    public void setContentView(View view) {
        if (mProxyActivity == this) {
            super.setContentView(view);
        }else {
            mProxyActivity.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mProxyActivity == this) {
            super.setContentView(view, params);
        }else {
            mProxyActivity.setContentView(view, params);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mProxyActivity == this) {
            super.setContentView(layoutResID);
        }else {
            mProxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        if (mProxyActivity == this) {
            super.addContentView(view, params);
        }else {
            mProxyActivity.addContentView(view, params);
        }
    }

}
