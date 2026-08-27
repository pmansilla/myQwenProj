package com.mob.tools.log;

import android.content.Context;
import android.os.Process;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.HashMap;

/* loaded from: classes2.dex */
public abstract class NLog {
    private static LogCollector defaultCollector;
    private static boolean disable;
    private static HashMap<String, NLog> loggers = new HashMap<>();
    private LogCollector collector;

    static {
        MobUncaughtExceptionHandler.register();
    }

    public static void disable() {
        disable = true;
    }

    public static NLog getInstance(final String str) {
        NLog nLog;
        synchronized (loggers) {
            nLog = loggers.get(str);
            if (nLog == null) {
                nLog = new NLog() { // from class: com.mob.tools.log.NLog.1
                    @Override // com.mob.tools.log.NLog
                    protected String getSDKTag() {
                        return str;
                    }
                };
                loggers.put(str, nLog);
            }
        }
        return nLog;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static NLog getInstanceForSDK(String str, boolean z) {
        return getInstance(str);
    }

    private String getStackTraceString(Throwable th) {
        if (th == null) {
            return "";
        }
        for (Throwable th2 = th; th2 != null; th2 = th2.getCause()) {
            try {
                if (th2 instanceof UnknownHostException) {
                    return "";
                }
            } catch (Throwable th3) {
                return th3 instanceof OutOfMemoryError ? "getStackTraceString oom" : th3.getMessage();
            }
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        printWriter.flush();
        String stringWriter2 = stringWriter.toString();
        stringWriter.close();
        return stringWriter2;
    }

    private int println(int i, int i2, String str) {
        String str2;
        try {
            str2 = Process.myPid() + "-" + Process.myTid() + SQLBuilder.PARENTHESES_LEFT + Thread.currentThread().getName() + ") " + str;
        } catch (Throwable unused) {
            str2 = str;
        }
        String sDKTag = getSDKTag();
        LogCollector logCollector = this.collector == null ? defaultCollector : this.collector;
        if (logCollector == null) {
            return 0;
        }
        logCollector.log(sDKTag, i, i2, null, str2);
        return 0;
    }

    public static void setCollector(String str, LogCollector logCollector) {
        getInstance(str).setCollector(logCollector);
    }

    public static void setContext(Context context) {
    }

    public static <Collector extends LogCollector> Collector setDefaultCollector(Collector collector) {
        defaultCollector = collector;
        return collector;
    }

    public final int crash(Throwable th) {
        if (disable) {
            return 0;
        }
        return println(6, 1, getStackTraceString(th));
    }

    public final int d(Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return println(3, 0, obj2);
    }

    public final int d(Throwable th) {
        if (disable) {
            return 0;
        }
        return println(3, 0, getStackTraceString(th));
    }

    public final int d(Throwable th, Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        StringBuilder sb = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        sb.append(obj2);
        sb.append('\n');
        sb.append(getStackTraceString(th));
        return println(3, 0, sb.toString());
    }

    public final int e(Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return println(6, 0, obj2);
    }

    public final int e(Throwable th) {
        if (disable) {
            return 0;
        }
        return println(6, 0, getStackTraceString(th));
    }

    public final int e(Throwable th, Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        StringBuilder sb = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        sb.append(obj2);
        sb.append('\n');
        sb.append(getStackTraceString(th));
        return println(6, 0, sb.toString());
    }

    protected abstract String getSDKTag();

    public final int i(Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return println(4, 0, obj2);
    }

    public final int i(Throwable th) {
        if (disable) {
            return 0;
        }
        return println(4, 0, getStackTraceString(th));
    }

    public final int i(Throwable th, Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        StringBuilder sb = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        sb.append(obj2);
        sb.append('\n');
        sb.append(getStackTraceString(th));
        return println(4, 0, sb.toString());
    }

    public final int sdkErr(Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return println(6, 3, obj2);
    }

    public final int sdkErr(Throwable th) {
        if (disable) {
            return 0;
        }
        return println(6, 3, getStackTraceString(th));
    }

    public final int sdkErr(Throwable th, Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        StringBuilder sb = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        sb.append(obj2);
        sb.append('\n');
        sb.append(getStackTraceString(th));
        return println(6, 3, sb.toString());
    }

    public NLog setCollector(LogCollector logCollector) {
        this.collector = logCollector;
        return this;
    }

    public final int v(Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return println(2, 0, obj2);
    }

    public final int v(Throwable th) {
        if (disable) {
            return 0;
        }
        return println(2, 0, getStackTraceString(th));
    }

    public final int v(Throwable th, Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        StringBuilder sb = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        sb.append(obj2);
        sb.append('\n');
        sb.append(getStackTraceString(th));
        return println(2, 0, sb.toString());
    }

    public final int w(Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return println(5, 0, obj2);
    }

    public final int w(String str) {
        if (disable) {
            return 0;
        }
        return println(5, 0, str);
    }

    public final int w(Throwable th) {
        if (disable) {
            return 0;
        }
        return println(5, 0, getStackTraceString(th));
    }

    public final int w(Throwable th, Object obj, Object... objArr) {
        if (disable) {
            return 0;
        }
        String obj2 = obj.toString();
        StringBuilder sb = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        sb.append(obj2);
        sb.append('\n');
        sb.append(getStackTraceString(th));
        return println(5, 0, sb.toString());
    }

    public int wtf(Throwable th) {
        if (disable) {
            return 0;
        }
        return println(6, 0, getStackTraceString(th));
    }
}
