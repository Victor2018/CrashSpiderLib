package com.victor.crash.spider.lib

import android.app.Application
import com.victor.crash.spider.library.SpiderCrashHandler

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: App
 * Author: Victor
 * Date: 2025/12/12 17:33
 * Description: 
 * -----------------------------------------------------------------
 */

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        SpiderCrashHandler(this)
    }
}