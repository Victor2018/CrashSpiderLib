package com.victor.crash.spider.library

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.util.Date


/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: SpiderCrashHandler
 * Author: Victor
 * Date: 2025/12/12 16:55
 * Description: 
 * -----------------------------------------------------------------
 */

class SpiderCrashHandler(val context: Context?): Thread.UncaughtExceptionHandler {

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, ex: Throwable) {
        Log.e("SpiderCrashHandler","uncaughtException()......")
        val model = parseCrash(ex)
        handleException(model)
        Process.killProcess(Process.myPid())
    }

    private fun handleException(model: CrashMessageInfo?) {
        Log.e("SpiderCrashHandler","handleException()......")
        val intent = Intent(context, CrashSpiderActivity::class.java)
        intent.putExtra(Constants.INTENT_DATA_KEY, model)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }

    private fun parseCrash(ex: Throwable): CrashMessageInfo {
        var ex = ex
        val model = CrashMessageInfo()
        try {
            model.ex = ex
            model.time = Date().time
            if (ex.cause != null) {
                ex = ex.cause!!
            }
            model.exceptionMsg = ex.message
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            ex.printStackTrace(pw)
            pw.flush()
            val exceptionType = ex.javaClass.getName()

            if (ex.getStackTrace() != null && ex.getStackTrace().size > 0) {
                val element = ex.getStackTrace()[0]

                model.lineNumber = element.lineNumber
                model.className = element.className
                model.fileName = element.fileName
                model.methodName = element.methodName
                model.exceptionType = exceptionType
            }

            model.fullException = sw.toString()
        } catch (e: Exception) {
            return model
        }
        return model
    }
}