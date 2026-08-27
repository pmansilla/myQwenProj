package com.mob.mobapm.e;

import com.mob.MobSDK;
import com.mob.tools.utils.DeviceHelper;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class c {
    public static String a(Throwable th) {
        if (th == null) {
            return "";
        }
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            printWriter.flush();
            String stringWriter2 = stringWriter.toString();
            stringWriter.close();
            return stringWriter2;
        } catch (Throwable th2) {
            return th2 instanceof OutOfMemoryError ? "getStackTraceString oom" : th2.getMessage();
        }
    }

    public static Map<String, Object> a(StackTraceElement[] stackTraceElementArr) {
        HashMap hashMap = new HashMap();
        try {
            StringBuilder sb = new StringBuilder();
            if (stackTraceElementArr != null && stackTraceElementArr.length > 0) {
                int i = 0;
                int i2 = 0;
                while (true) {
                    if (i >= stackTraceElementArr.length - 1) {
                        break;
                    }
                    if (i2 >= 100) {
                        sb.append("\t... ");
                        sb.append(stackTraceElementArr.length - i);
                        sb.append(" more");
                        break;
                    }
                    i2++;
                    sb.append("\tat " + stackTraceElementArr[i] + "\n");
                    i++;
                    if (stackTraceElementArr[i].toString().contains(DeviceHelper.getInstance(MobSDK.getContext()).getPackageName()) && !hashMap.containsKey("desc")) {
                        hashMap.put("desc", stackTraceElementArr[i].toString());
                    }
                }
            }
            hashMap.put("stack", sb);
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().d("APM: get anr stack error: " + th, new Object[0]);
        }
        return hashMap;
    }
}
