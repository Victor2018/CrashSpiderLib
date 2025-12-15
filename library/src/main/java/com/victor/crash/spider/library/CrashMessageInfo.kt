package com.victor.crash.spider.library

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: CrashMessageInfo
 * Author: Victor
 * Date: 2025/12/12 16:57
 * Description: 
 * -----------------------------------------------------------------
 */

import java.io.Serializable

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: CrashMessageInfo.kt
 * Author: Victor
 * Date: 2018/12/24 15:55
 * Description: crash 信息实体
 * -----------------------------------------------------------------
 */

class CrashMessageInfo : Serializable {

    /**
     * 崩溃主体信息
     */
    var ex: Throwable? = null

    /**
     * 包名，暂时未使用
     */
    var packageName: String? = null

    /**
     * 崩溃主信息
     */
    var exceptionMsg: String? = null

    /**
     * 崩溃类名
     */
    var className: String? = null

    /**
     * 崩溃文件名
     */
    var fileName: String? = null

    /**
     * 崩溃方法
     */
    var methodName: String? = null

    /**
     * 崩溃行数
     */
    var lineNumber: Int = 0

    /**
     * 崩溃类型
     */
    var exceptionType: String? = null

    /**
     * 全部信息
     */
    var fullException: String? = null

    /**
     * 崩溃时间
     */
    var time: Long = 0

    /**
     * 设备信息
     */
    var device: DeviceInfo = DeviceInfo()

    override fun toString(): String {
        return "CrashMessageInfo{" +
                "ex=$ex" +
                ", packageName='$packageName'" +
                ", exceptionMsg='$exceptionMsg'" +
                ", className='$className'" +
                ", fileName='$fileName'" +
                ", methodName='$methodName'" +
                ", lineNumber=$lineNumber" +
                ", exceptionType='$exceptionType'" +
                ", fullException='$fullException'" +
                ", time=$time" +
                ", device=$device" +
                '}'
    }

}