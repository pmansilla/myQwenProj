package com.mob.tools.log;

import com.mob.tools.MobLog;
import java.lang.Thread;

/* loaded from: classes2.dex */
public class MobUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static boolean disable = false;
    private static boolean isDebug = false;
    private static Thread.UncaughtExceptionHandler oriHandler;

    private MobUncaughtExceptionHandler() {
    }

    public static void closeLog() {
        isDebug = false;
    }

    public static void disable() {
        disable = true;
    }

    public static void openLog() {
        isDebug = true;
    }

    public static void register() {
        if (disable) {
            return;
        }
        oriHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new MobUncaughtExceptionHandler());
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        if (isDebug) {
            MobLog.getInstance().wtf(th);
        }
        MobLog.getInstance().crash(th);
        if (oriHandler != null) {
            oriHandler.uncaughtException(thread, th);
        }
    }
}
