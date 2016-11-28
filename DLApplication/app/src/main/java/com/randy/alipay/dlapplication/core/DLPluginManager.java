package com.randy.alipay.dlapplication.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;

/**
 * Created by randy on 2016/9/3.
 */
public class DLPluginManager {

    private Context mContext;

    private String mNativeLibDir = null;

    private static DLPluginManager mInstance;

    public DLPluginManager(Context context) {
        mContext = context.getApplicationContext();
        mNativeLibDir = mContext.getDir("pluginlib", Context.MODE_PRIVATE).getAbsolutePath();
    }

    public static DLPluginManager getInstance(Context context){

        if (null == mInstance){

            synchronized (DLPluginManager.class){

                if (null == mInstance){
                    mInstance = new DLPluginManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 根据apk文件路径触发资源文件调用
     * @param dexPath
     * @return 可能为null
     */
    private AssetManager createAssetManager(String dexPath){

        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param assetManager
     * @return
     */
    private Resources createResources(AssetManager assetManager) {
        Resources superRes = mContext.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        return resources;
    }
}
