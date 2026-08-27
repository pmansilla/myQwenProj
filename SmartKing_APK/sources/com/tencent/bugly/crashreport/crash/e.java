package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.lang.Thread;
import java.util.HashMap;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class e implements Thread.UncaughtExceptionHandler {
    private static String h;
    private static final Object i = new Object();
    private Context a;
    private b b;
    private com.tencent.bugly.crashreport.common.strategy.a c;
    private com.tencent.bugly.crashreport.common.info.a d;
    private Thread.UncaughtExceptionHandler e;
    private Thread.UncaughtExceptionHandler f;
    private boolean g = false;
    private int j;

    public e(Context context, b bVar, com.tencent.bugly.crashreport.common.strategy.a aVar, com.tencent.bugly.crashreport.common.info.a aVar2) {
        this.a = context;
        this.b = bVar;
        this.c = aVar;
        this.d = aVar2;
    }

    private static String a(Throwable th, int i2) {
        if (th == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            if (th.getStackTrace() != null) {
                for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                    if (i2 > 0 && sb.length() >= i2) {
                        sb.append("\n[Stack over limit size :" + i2 + " , has been cutted !]");
                        return sb.toString();
                    }
                    sb.append(stackTraceElement.toString());
                    sb.append("\n");
                }
            }
        } catch (Throwable th2) {
            x.e("gen stack error %s", th2.toString());
        }
        return sb.toString();
    }

    private static boolean a(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        if (uncaughtExceptionHandler == null) {
            return true;
        }
        String name = uncaughtExceptionHandler.getClass().getName();
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            if (name.equals(className) && "uncaughtException".equals(methodName)) {
                return false;
            }
        }
        return true;
    }

    private static boolean a(Thread thread) {
        synchronized (i) {
            if (h != null && thread.getName().equals(h)) {
                return true;
            }
            h = thread.getName();
            return false;
        }
    }

    private CrashDetailBean b(Thread thread, Throwable th, boolean z, String str, byte[] bArr) {
        String a;
        if (th == null) {
            x.d("We can do nothing with a null throwable.", new Object[0]);
            return null;
        }
        boolean m = c.a().m();
        String str2 = (m && z) ? " This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful![Bugly]" : "";
        if (m && z) {
            x.e("This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful!", new Object[0]);
        }
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.g();
        crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.e();
        crashDetailBean.E = com.tencent.bugly.crashreport.common.info.b.i();
        crashDetailBean.F = this.d.k();
        crashDetailBean.G = this.d.j();
        crashDetailBean.H = this.d.l();
        crashDetailBean.w = z.a(this.a, c.e, (String) null);
        crashDetailBean.y = y.a();
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(crashDetailBean.y == null ? 0 : crashDetailBean.y.length);
        x.a("user log size:%d", objArr);
        crashDetailBean.b = z ? 0 : 2;
        crashDetailBean.e = this.d.h();
        crashDetailBean.f = this.d.k;
        crashDetailBean.g = this.d.q();
        crashDetailBean.m = this.d.g();
        String name = th.getClass().getName();
        String b = b(th, 1000);
        if (b == null) {
            b = "";
        }
        Object[] objArr2 = new Object[2];
        objArr2[0] = Integer.valueOf(th.getStackTrace().length);
        objArr2[1] = Boolean.valueOf(th.getCause() != null);
        x.e("stack frame :%d, has cause %b", objArr2);
        String stackTraceElement = th.getStackTrace().length > 0 ? th.getStackTrace()[0].toString() : "";
        Throwable th2 = th;
        while (th2 != null && th2.getCause() != null) {
            th2 = th2.getCause();
        }
        if (th2 == null || th2 == th) {
            crashDetailBean.n = name;
            crashDetailBean.o = b + str2;
            if (crashDetailBean.o == null) {
                crashDetailBean.o = "";
            }
            crashDetailBean.p = stackTraceElement;
            a = a(th, c.f);
            crashDetailBean.q = a;
        } else {
            crashDetailBean.n = th2.getClass().getName();
            crashDetailBean.o = b(th2, 1000);
            if (crashDetailBean.o == null) {
                crashDetailBean.o = "";
            }
            if (th2.getStackTrace().length > 0) {
                crashDetailBean.p = th2.getStackTrace()[0].toString();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append(":");
            sb.append(b);
            sb.append("\n");
            sb.append(stackTraceElement);
            sb.append("\n......");
            sb.append("\nCaused by:\n");
            sb.append(crashDetailBean.n);
            sb.append(":");
            sb.append(crashDetailBean.o);
            sb.append("\n");
            a = a(th2, c.f);
            sb.append(a);
            crashDetailBean.q = sb.toString();
        }
        crashDetailBean.r = System.currentTimeMillis();
        crashDetailBean.u = z.a(crashDetailBean.q.getBytes());
        try {
            crashDetailBean.z = z.a(c.f, false);
            crashDetailBean.A = this.d.d;
            crashDetailBean.B = thread.getName() + SQLBuilder.PARENTHESES_LEFT + thread.getId() + SQLBuilder.PARENTHESES_RIGHT;
            crashDetailBean.z.put(crashDetailBean.B, a);
            crashDetailBean.I = this.d.s();
            crashDetailBean.h = this.d.p();
            crashDetailBean.i = this.d.B();
            crashDetailBean.M = this.d.a;
            crashDetailBean.N = this.d.a();
            if (z) {
                this.b.d(crashDetailBean);
            } else {
                boolean z2 = str != null && str.length() > 0;
                boolean z3 = bArr != null && bArr.length > 0;
                if (z2) {
                    crashDetailBean.O = new HashMap(1);
                    crashDetailBean.O.put("UserData", str);
                }
                if (z3) {
                    crashDetailBean.U = bArr;
                }
            }
            crashDetailBean.Q = this.d.z();
            crashDetailBean.R = this.d.A();
            crashDetailBean.S = this.d.t();
            crashDetailBean.T = this.d.y();
        } catch (Throwable th3) {
            x.e("handle crash error %s", th3.toString());
        }
        return crashDetailBean;
    }

    private static String b(Throwable th, int i2) {
        if (th.getMessage() == null) {
            return "";
        }
        if (th.getMessage().length() <= 1000) {
            return th.getMessage();
        }
        return th.getMessage().substring(0, 1000) + "\n[Message over limit size:1000, has been cutted!]";
    }

    public final synchronized void a() {
        if (this.j >= 10) {
            x.a("java crash handler over %d, no need set.", 10);
            return;
        }
        this.g = true;
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler != null) {
            if (getClass().getName().equals(defaultUncaughtExceptionHandler.getClass().getName())) {
                return;
            }
            if ("com.android.internal.os.RuntimeInit$UncaughtHandler".equals(defaultUncaughtExceptionHandler.getClass().getName())) {
                x.a("backup system java handler: %s", defaultUncaughtExceptionHandler.toString());
                this.f = defaultUncaughtExceptionHandler;
                this.e = defaultUncaughtExceptionHandler;
            } else {
                x.a("backup java handler: %s", defaultUncaughtExceptionHandler.toString());
                this.e = defaultUncaughtExceptionHandler;
            }
        }
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.j++;
        x.a("registered java monitor: %s", toString());
    }

    public final synchronized void a(StrategyBean strategyBean) {
        if (strategyBean != null) {
            if (strategyBean.e != this.g) {
                x.a("java changed to %b", Boolean.valueOf(strategyBean.e));
                if (strategyBean.e) {
                    a();
                    return;
                }
                b();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:76:0x01b9, code lost:
    
        if (r8.f != null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x01bd, code lost:
    
        com.tencent.bugly.proguard.x.e("crashreport last handle start!", new java.lang.Object[0]);
        com.tencent.bugly.proguard.x.e("current process die", new java.lang.Object[0]);
        android.os.Process.killProcess(android.os.Process.myPid());
        java.lang.System.exit(1);
        com.tencent.bugly.proguard.x.e("crashreport last handle end!", new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x01dc, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x01fb, code lost:
    
        if (r8.f != null) goto L23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void a(java.lang.Thread r9, java.lang.Throwable r10, boolean r11, java.lang.String r12, byte[] r13) {
        /*
            Method dump skipped, instructions count: 603
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.e.a(java.lang.Thread, java.lang.Throwable, boolean, java.lang.String, byte[]):void");
    }

    public final synchronized void b() {
        this.g = false;
        x.a("close java monitor!", new Object[0]);
        if (Thread.getDefaultUncaughtExceptionHandler().getClass().getName().contains("bugly")) {
            x.a("Java monitor to unregister: %s", toString());
            Thread.setDefaultUncaughtExceptionHandler(this.e);
            this.j--;
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public final void uncaughtException(Thread thread, Throwable th) {
        synchronized (i) {
            a(thread, th, true, null, null);
        }
    }
}
