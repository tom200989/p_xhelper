package com.xnet.xnet;

import android.app.Application;

import org.xutils.x;

/*
 * Created by qianli.ma on 2019/1/9 0009.
 */
public class XApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false);
    }
}
