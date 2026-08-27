package com.tencent.bugly.proguard;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.LongCompanionObject;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class ab extends Thread {
    private boolean a = false;
    private boolean b = false;
    private List<aa> c = new ArrayList();
    private List<ac> d = new ArrayList();
    private ArrayList<aa> e = new ArrayList<>();

    private void a(Handler handler, long j) {
        if (handler == null) {
            x.e("addThread handler should not be null", new Object[0]);
            return;
        }
        String name = handler.getLooper().getThread().getName();
        for (int i = 0; i < this.c.size(); i++) {
            try {
                if (this.c.get(i).d().equals(handler.getLooper().getThread().getName())) {
                    x.e("addThread fail ,this thread has been added in monitor queue", new Object[0]);
                    return;
                }
            } catch (Exception e) {
                x.b(e);
            }
        }
        this.c.add(new aa(handler, name, BootloaderScanner.TIMEOUT));
    }

    private int e() {
        int i = 0;
        int i2 = 0;
        while (i < this.c.size()) {
            try {
                int max = Math.max(i2, this.c.get(i).c());
                i++;
                i2 = max;
            } catch (Exception e) {
                x.b(e);
            }
        }
        return i2;
    }

    public final void a() {
        a(new Handler(Looper.getMainLooper()), BootloaderScanner.TIMEOUT);
    }

    public final void a(ac acVar) {
        if (this.d.contains(acVar)) {
            x.c("addThreadMonitorListeners fail ,this threadMonitorListener has been added in monitor queue", new Object[0]);
        } else {
            this.d.add(acVar);
        }
    }

    public final void a(boolean z) {
        this.b = true;
    }

    public final void b() {
        for (int i = 0; i < this.c.size(); i++) {
            try {
                if (this.c.get(i).d().equals(Looper.getMainLooper().getThread().getName())) {
                    x.c("remove handler::%s", this.c.get(i));
                    this.c.remove(i);
                }
            } catch (Exception e) {
                x.b(e);
                return;
            }
        }
    }

    public final void b(ac acVar) {
        this.d.remove(acVar);
    }

    public final boolean c() {
        this.a = true;
        if (!isAlive()) {
            return false;
        }
        try {
            interrupt();
        } catch (Exception e) {
            x.b(e);
        }
        return true;
    }

    public final boolean d() {
        if (isAlive()) {
            return false;
        }
        try {
            start();
            return true;
        } catch (Exception e) {
            x.b(e);
            return false;
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        while (!this.a) {
            for (int i = 0; i < this.c.size(); i++) {
                try {
                    this.c.get(i).a();
                } catch (Exception e) {
                    x.b(e);
                } catch (OutOfMemoryError e2) {
                    x.b(e2);
                }
            }
            long uptimeMillis = SystemClock.uptimeMillis();
            for (long j = 2000; j > 0 && !isInterrupted(); j = 2000 - (SystemClock.uptimeMillis() - uptimeMillis)) {
                sleep(j);
            }
            int e3 = e();
            if (e3 != 0 && e3 != 1) {
                this.e.clear();
                for (int i2 = 0; i2 < this.c.size(); i2++) {
                    aa aaVar = this.c.get(i2);
                    if (aaVar.b()) {
                        this.e.add(aaVar);
                        aaVar.a(LongCompanionObject.MAX_VALUE);
                    }
                }
                NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
                if (nativeCrashHandler == null || !nativeCrashHandler.isEnableCatchAnrTrace()) {
                    x.c("do not enable jni mannual dump anr trace", new Object[0]);
                } else {
                    nativeCrashHandler.dumpAnrNativeStack();
                    x.c("jni mannual dump anr trace", new Object[0]);
                }
                int i3 = 0;
                while (true) {
                    if (this.b) {
                        break;
                    }
                    x.c("do not enable anr continue check", new Object[0]);
                    sleep(2000L);
                    i3++;
                    if (i3 == 15) {
                        this.e.clear();
                        break;
                    }
                }
                for (int i4 = 0; i4 < this.e.size(); i4++) {
                    aa aaVar2 = this.e.get(i4);
                    for (int i5 = 0; i5 < this.d.size(); i5++) {
                        x.e("main thread blocked,now begin to upload anr stack", new Object[0]);
                        this.d.get(i5).a(aaVar2);
                        this.b = false;
                    }
                }
            }
        }
    }
}
