package com.victor.crash;

import android.app.Application;

import com.victor.crash.library.SpiderCrashHandler;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: App.java
 * Author: Victor
 * Date: 2018/12/24 15:55
 * Description:
 * -----------------------------------------------------------------
 */

public class App extends Application {
    private static App instance;

    public App() {
        instance = this;
    }

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //放在其他库初始化前
        SpiderCrashHandler.init(this);
    }

}
