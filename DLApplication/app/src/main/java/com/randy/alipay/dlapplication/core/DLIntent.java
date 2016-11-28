package com.randy.alipay.dlapplication.core;

import android.content.Intent;

/**
 * Created by randy on 2016/9/4.
 * 常规启动activity是采用startActivity(intent)的方式，这里为了实现自己代理的效果对Intent进行了自己的封装
 */
public class DLIntent extends Intent{

    //封装插件包以及类名相关信息
    private String mPluginPackage;
    private String mPluginClass;

    public DLIntent() {
        super();
    }

    public DLIntent(String pluginPackage, String pluginClass) {
        super();
        mPluginPackage = pluginPackage;
        mPluginClass = pluginClass;
    }

    public DLIntent(String pluginPackage, Class<?> clazz){
        super();
        mPluginClass = clazz.getName();
        mPluginPackage = pluginPackage;
    }


}
