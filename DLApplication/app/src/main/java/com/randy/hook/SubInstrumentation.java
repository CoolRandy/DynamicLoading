package com.randy.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by randy on 2016/11/14.
 */
public class SubInstrumentation extends Instrumentation {

    Instrumentation mBase;

    public SubInstrumentation(Instrumentation base) {
        mBase = base;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {

        Log.e("TAG", "randy到此一游! requestCode=" + requestCode);

        //调用原始的execStartActivity方法
       try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                    "execStartActivity",Context.class,IBinder.class,IBinder.class,Activity.class,
                    Intent.class,int.class,Bundle.class);
            execStartActivity.setAccessible(true);
            return (ActivityResult)execStartActivity.invoke(mBase,who,contextThread,token,target,
                                                    intent,requestCode,options);
        }catch (Exception e){
            //实际项目中不要轻易抛异常，如果异常没有处理者，就可能导致应用崩溃
            throw new RuntimeException("do not support!");
        }
    }
}
