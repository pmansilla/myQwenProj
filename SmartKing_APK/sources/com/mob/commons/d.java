package com.mob.commons;

import android.os.Handler;
import android.os.Message;
import com.mob.MobCommunicator;
import com.mob.MobSDK;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.MobLog;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ResHelper;
import io.reactivex.annotations.SchedulerSupport;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/* compiled from: DyLogUploader.java */
/* loaded from: classes.dex */
public class d implements Handler.Callback {
    private static final String a = j.b("dfe.mic.mob.com/drl");
    private static d b;
    private MobCommunicator c;
    private Handler f;
    private String g;
    private SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
    private HashMap<String, Object> e = new HashMap<>();
    private int h = -1;

    private d() {
        this.g = null;
        try {
            this.g = UUID.randomUUID().toString();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        this.f = MobHandlerThread.newHandler("d", this);
    }

    public static synchronized d a() {
        d dVar;
        synchronized (d.class) {
            if (b == null) {
                b = new d();
            }
            dVar = b;
        }
        return dVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x004e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0042 A[Catch: all -> 0x0015, TRY_ENTER, TRY_LEAVE, TryCatch #2 {all -> 0x0015, blocks: (B:7:0x0009, B:9:0x0010, B:39:0x0019, B:22:0x0036, B:31:0x0042), top: B:6:0x0009 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String a(java.lang.Throwable r4) {
        /*
            r3 = this;
            if (r4 != 0) goto L5
            java.lang.String r4 = ""
            return r4
        L5:
            r0 = 0
            r1 = r4
        L7:
            if (r1 == 0) goto L19
            boolean r2 = r1 instanceof java.net.UnknownHostException     // Catch: java.lang.Throwable -> L15 java.lang.Throwable -> L17
            if (r2 == 0) goto L10
            java.lang.String r4 = ""
            return r4
        L10:
            java.lang.Throwable r1 = r1.getCause()     // Catch: java.lang.Throwable -> L15 java.lang.Throwable -> L17
            goto L7
        L15:
            r4 = move-exception
            goto L4c
        L17:
            r4 = move-exception
            goto L36
        L19:
            java.io.StringWriter r1 = new java.io.StringWriter     // Catch: java.lang.Throwable -> L15 java.lang.Throwable -> L17
            r1.<init>()     // Catch: java.lang.Throwable -> L15 java.lang.Throwable -> L17
            java.io.PrintWriter r0 = new java.io.PrintWriter     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L34
            r0.<init>(r1)     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L34
            r4.printStackTrace(r0)     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L34
            r0.flush()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L34
            java.lang.String r4 = r1.toString()     // Catch: java.lang.Throwable -> L31 java.lang.Throwable -> L34
            r1.close()     // Catch: java.lang.Throwable -> L30
        L30:
            return r4
        L31:
            r4 = move-exception
            r0 = r1
            goto L4c
        L34:
            r4 = move-exception
            r0 = r1
        L36:
            boolean r1 = r4 instanceof java.lang.OutOfMemoryError     // Catch: java.lang.Throwable -> L15
            if (r1 == 0) goto L42
            java.lang.String r4 = "getStackTraceString oom"
            if (r0 == 0) goto L41
            r0.close()     // Catch: java.lang.Throwable -> L41
        L41:
            return r4
        L42:
            java.lang.String r4 = r4.getMessage()     // Catch: java.lang.Throwable -> L15
            if (r0 == 0) goto L4b
            r0.close()     // Catch: java.lang.Throwable -> L4b
        L4b:
            return r4
        L4c:
            if (r0 == 0) goto L51
            r0.close()     // Catch: java.lang.Throwable -> L51
        L51:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.d.a(java.lang.Throwable):java.lang.String");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private synchronized void a(int i, Throwable th, String str) {
        try {
            if (th == null) {
                MobLog.getInstance().d(str, new Object[0]);
            } else {
                MobLog.getInstance().d(th);
            }
            if (b.ad()) {
                return;
            }
            Message message = new Message();
            message.what = 1;
            Object[] objArr = new Object[4];
            objArr[0] = Long.valueOf(System.currentTimeMillis());
            if (th == null) {
                th = str;
            }
            objArr[1] = th;
            objArr[2] = Integer.valueOf(i);
            objArr[3] = Integer.valueOf(b());
            message.obj = objArr;
            this.f.sendMessage(message);
        } catch (Throwable th2) {
            throw th2;
        }
    }

    private boolean a(HashMap<String, Object> hashMap) {
        try {
            return b(hashMap);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            try {
                return b(hashMap);
            } catch (Throwable th2) {
                MobLog.getInstance().d(th2);
                return false;
            }
        }
    }

    private synchronized int b() {
        return this.h;
    }

    private boolean b(HashMap<String, Object> hashMap) throws Throwable {
        if (hashMap == null || hashMap.isEmpty()) {
            return true;
        }
        HashMap<String, Object> at = b.at();
        at.put("errors", hashMap);
        e();
        HashMap hashMap2 = (HashMap) this.c.requestSynchronized(at, a, false);
        return hashMap2 == null || hashMap2.isEmpty();
    }

    private void c() {
        if (b.ad()) {
            return;
        }
        long j = !f() ? 120000L : 0L;
        if (j > 0) {
            this.f.sendEmptyMessageDelayed(2, j);
        } else {
            this.f.sendEmptyMessageDelayed(2, 10000L);
        }
    }

    private void c(HashMap<String, Object> hashMap) {
        try {
            d(hashMap);
        } catch (Throwable th) {
            try {
                d(hashMap);
            } catch (Throwable unused) {
                MobLog.getInstance().d(th);
            }
        }
    }

    private void d() {
        boolean z;
        File[] listFiles;
        if (this.e.size() > 0) {
            z = a(this.e);
            if (!z) {
                c(this.e);
            }
            this.e.clear();
        } else {
            z = true;
        }
        if (z) {
            File g = g();
            if (!g.exists() || !g.isDirectory() || (listFiles = g.listFiles()) == null || listFiles.length <= 0) {
                return;
            }
            for (File file : listFiles) {
                if (a((HashMap<String, Object>) ResHelper.readObjectFromFile(file.getAbsolutePath())) && !file.delete()) {
                    file.delete();
                }
            }
        }
    }

    private void d(HashMap<String, Object> hashMap) throws Throwable {
        File[] listFiles;
        File g = g();
        if (!g.exists() || !g.isDirectory()) {
            g.mkdirs();
        }
        File file = new File(g, ".dyl_0");
        if (file.exists() && (listFiles = g.listFiles()) != null && listFiles.length > 0) {
            file = new File(g, ".dyl_0");
            int i = 0;
            while (file.exists()) {
                i++;
                file = new File(g, ".dyl_" + i);
            }
        }
        ResHelper.saveObjectToFile(file.getPath(), hashMap);
    }

    private void e() {
        if (this.c == null) {
            this.c = new MobCommunicator(1024, "ab0a0a6473d1891d388773574764b239d4ad80cb2fd3a83d81d03901c1548c13fee7c9692c326e6682b239d4c5d0021d1b607642c47ec29f10b0602908c3e6c9", "23c3c8cb41c47dd288cc7f4c218fbc7c839a34e0a0d1b2130e87b7914936b120a2d6570ee7ac66282328d50f2acfd82f2259957c89baea32547758db05de9cd7c6822304c8e45742f24bbbe41c1e12f09e18c6fab4d078065f2e5aaed94c900c66e8bbf8a120eefa7bd1fb52114d529250084f5f6f369ed4ce9645978dd30c51");
        }
    }

    private boolean f() {
        String networkType = DeviceHelper.getInstance(MobSDK.getContext()).getNetworkType();
        return (networkType == null || SchedulerSupport.NONE.equals(networkType)) ? false : true;
    }

    private File g() {
        return new File(ResHelper.getDataCache(MobSDK.getContext()), ".dyl");
    }

    public synchronized void a(int i) {
        this.h = i;
        if (i != 1 && i != 4 && i != 17 && i != 18 && i != 19 && i != 20) {
            if (i == 10) {
            }
        }
        a(8, null, "ld vr " + i);
    }

    public synchronized void a(int i, Throwable th) {
        a(i, th, null);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        try {
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        switch (message.what) {
            case 1:
                if (this.e.size() > 10) {
                    c(this.e);
                    this.e.clear();
                }
                Object[] objArr = (Object[]) message.obj;
                this.e.put("sd", this.g);
                ArrayList arrayList = (ArrayList) this.e.get("list");
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                HashMap hashMap = new HashMap();
                hashMap.put("ct", objArr[0]);
                hashMap.put("mg", "[" + this.d.format(objArr[0]) + "][" + objArr[2] + "][" + objArr[3] + "] " + (objArr[1] instanceof Throwable ? a((Throwable) objArr[1]) : String.valueOf(objArr[1])));
                hashMap.put("et", objArr[2]);
                hashMap.put("po", objArr[3]);
                arrayList.add(hashMap);
                this.e.put("list", arrayList);
                c();
                return false;
            case 2:
                this.f.removeMessages(2);
                if (b.aq()) {
                    d();
                }
                return false;
            default:
                return false;
        }
    }
}
