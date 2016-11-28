package com.randy.alipay.dlapplication.host;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by randy on 2016/9/2.
 * 代理activity
 */
public class ProxyActivity extends Activity {

    private static final String TAG = ProxyActivity.class.getSimpleName();

    public static final String EXTRA_DEX_PATH = "extra.dex.path";

    public static final String FROM = "extra.from";

    public static final int FROM_EXTERNAL = 0;

    public static final int FROM_INTERNAL = 1;


    public static final String EXTRA_CLASS = "extra.class";

    private String mClass;

    private String mDexPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClass = getIntent().getStringExtra(EXTRA_CLASS);
        mDexPath = getIntent().getStringExtra(EXTRA_DEX_PATH);

        Log.e(TAG, "mClass: " + mClass + ", mDexPath: " + mDexPath);
        //mClass: null, mDexPath: /mnt/sdcard/DynamicLoadHost/plugin.apk

        if (null == mClass){
            launchActivity();
        }else {
            launchActivity(mClass);
        }
    }

    private void launchActivity(){

        PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(mDexPath, 1);
        if (packageInfo.activities != null && (packageInfo.activities.length > 0)){

            String activityName = packageInfo.activities[0].name;
            mClass = activityName;
            Log.e(TAG, "mClass is: " + mClass);
            //mClass is: com.randy.alipay.dlapplication.client.MainActivity
            launchActivity(mClass);
        }
    }

    private void launchActivity(final String className){
        Log.e(TAG, "start launch activity, className is: " + className);

        File dexOutputDir = this.getDir("dex", 0);
        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(mDexPath, dexOutputPath, null, classLoader);

        try {
            //通过类加载器去加载apk中activity的类，并实例化一个对象
            Class<?> localClass = dexClassLoader.loadClass(className);
            Constructor<?> localConstructor = localClass.getConstructor(new Class[]{});
            Object instance = localConstructor.newInstance(new Object[]{});
            Log.e(TAG, "instance is: " + instance);
            //然后通过反射的方式去调用这个方法的setProxy方法和onCreate方法
            //setProxy方法的作用是将activity内部的执行全部交给宿主程序中的代理activity去执行
            Method setProxy = localClass.getMethod("setProxy", new Class[] {Activity.class});
            setProxy.setAccessible(true);
            setProxy.invoke(instance, new Object[]{this});
            //onCreate是activity执行的入口，当setProxy执行以后就调用onCreate方法，进而调起activity
            Method onCreate = localClass.getDeclaredMethod("onCreate", new Class[] {Bundle.class});
            onCreate.setAccessible(true);
            Bundle bundle = new Bundle();
            bundle.putInt(FROM, FROM_EXTERNAL);
            onCreate.invoke(instance, new Object[]{bundle});
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
