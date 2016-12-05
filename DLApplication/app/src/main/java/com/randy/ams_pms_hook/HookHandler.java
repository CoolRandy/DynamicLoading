package com.randy.ams_pms_hook;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by randy on 2016/12/4.
 */
public class HookHandler implements InvocationHandler {

    private static final String TAG = "HookHandler";

    Object mBase;

    public HookHandler(Object base) {
        mBase = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Log.e(TAG, "you have been hooked by randy!");
        Log.e(TAG, "method: " + method.getName() + " called with args: " + Arrays.toString(args));
        return method.invoke(mBase, args);
    }
}
