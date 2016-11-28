package com.randy.hook;

import android.app.Instrumentation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by randy on 2016/11/15.
 *
 */
public class HookHelper {

    public static void attachContext() throws Exception{

        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);
        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation)mInstrumentationField.get(currentActivityThread);

        Instrumentation mSubInstrumentation = new SubInstrumentation(mInstrumentation);
        //给指定对象currentActivityThread设置域mInstrumentationField的值为mSubInstrumentation
        //如此便实现了偷梁换柱
        mInstrumentationField.set(currentActivityThread, mSubInstrumentation);
    }
}
