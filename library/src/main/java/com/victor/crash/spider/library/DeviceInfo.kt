package com.victor.crash.spider.library

import android.os.Build
import java.io.Serializable

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: DeviceInfo
 * Author: Victor
 * Date: 2025/12/15 8:27
 * Description: 
 * -----------------------------------------------------------------
 */

class DeviceInfo: Serializable {
    //设备名
    val model: String = Build.MODEL

    //设备厂商
    val brand: String = Build.BRAND

    //系统版本号
    val version: String = Build.VERSION.SDK_INT.toString()

    override fun toString(): String {
        return "Device{" +
                "model='$model'" +
                ", brand='$brand'" +
                ", version='$version'" +
                '}'
    }
}