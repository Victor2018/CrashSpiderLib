package com.victor.crash.library;

import android.content.Context;
import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: SpiderCrashHandler.java
 * Author: Victor
 * Date: 2018/12/24 15:55
 * Description:
 * -----------------------------------------------------------------
 */

public class SpiderCrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "SpiderCrashHandler";

    private static SpiderCrashHandler spiderHandler = new SpiderCrashHandler();

    private static Context mContext;

    private SpiderCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static SpiderCrashHandler init(Context context) {
        mContext = context;
        return spiderHandler;
    }

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        CrashMessageInfo model = parseCrash(ex);
        handleException(model);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void handleException(CrashMessageInfo model) {

        Intent intent = new Intent(mContext, CrashActivity.class);
        intent.putExtra(CrashActivity.CRASH_MODEL, model);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }

    private CrashMessageInfo parseCrash(Throwable ex) {
        CrashMessageInfo model = new CrashMessageInfo();
        try {
            model.setEx(ex);
            model.setTime(new Date().getTime());
            if (ex.getCause() != null) {
                ex = ex.getCause();
            }
            model.setExceptionMsg(ex.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            pw.flush();
            String exceptionType = ex.getClass().getName();

            if (ex.getStackTrace() != null && ex.getStackTrace().length > 0) {
                StackTraceElement element = ex.getStackTrace()[0];

                model.setLineNumber(element.getLineNumber());
                model.setClassName(element.getClassName());
                model.setFileName(element.getFileName());
                model.setMethodName(element.getMethodName());
                model.setExceptionType(exceptionType);
            }

            model.setFullException(sw.toString());
        } catch (Exception e) {
            return model;
        }
        return model;
    }
}