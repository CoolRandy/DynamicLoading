package com.randy.ams_pms_hook;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by randy on 2016/12/4.
 */
public final class HookHelper {

    public static void hookActivityManager(){

        try {
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);
            Object gDefault = gDefaultField.get(null);

            //对于4.x版本以上个Default是一个单例
            Class<?> singleton = Class.forName("android.util.Singleton");
            Field mInstanceField = singleton.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            //根据gDefault的单例获取到原始的IActivityManager对象
            Object rawIActivityManager = mInstanceField.get(gDefault);

            //创建该对象的代理对象，然后替换相应字段，执行我们定义的内容
            Class<?> iActivityManagerService = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                            new Class<?>[]{ iActivityManagerService},
                            new HookHandler(rawIActivityManager));
            mInstanceField.set(gDefault, proxy);

        }catch (Exception e){
//            e.printStackTrace();
            throw new RuntimeException("Hook field", e);
        }
    }
}
