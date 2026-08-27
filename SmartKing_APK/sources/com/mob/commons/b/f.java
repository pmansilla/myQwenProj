package com.mob.commons.b;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.SystemClock;
import android.text.TextUtils;
import java.security.MessageDigest;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* compiled from: IdSupplier.java */
/* loaded from: classes.dex */
public abstract class f {
    protected Context a;
    protected String b;
    private boolean c = false;
    private boolean d = false;
    private String e = null;
    private String f = null;
    private String g = null;
    private String h = null;

    /* compiled from: IdSupplier.java */
    /* loaded from: classes.dex */
    private static class a {
        private static final char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        /* JADX INFO: Access modifiers changed from: private */
        public static String b(String str) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(str.getBytes("UTF-8"));
                byte[] digest = messageDigest.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    sb.append(a[(b & 240) >> 4]);
                    sb.append(a[b & 15]);
                }
                return sb.toString();
            } catch (Throwable th) {
                com.mob.commons.b.c.a().a(th);
                return str;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: IdSupplier.java */
    /* loaded from: classes.dex */
    public class b implements ServiceConnection {
        boolean a;
        private final BlockingQueue<IBinder> c;

        private b() {
            this.a = false;
            this.c = new LinkedBlockingQueue();
        }

        public IBinder a(long j) throws InterruptedException {
            if (this.a) {
                throw new IllegalStateException();
            }
            this.a = true;
            BlockingQueue<IBinder> blockingQueue = this.c;
            if (j <= 0) {
                j = 1500;
            }
            return blockingQueue.poll(j, TimeUnit.MILLISECONDS);
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.c.put(iBinder);
            } catch (Throwable unused) {
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    /* compiled from: IdSupplier.java */
    /* loaded from: classes.dex */
    public static class c {
        boolean a;
        String b;
        String c;
        String d;
        String e;
    }

    public f(Context context) {
        this.a = context;
        this.b = context.getPackageName();
    }

    private synchronized long a(Intent intent) {
        long elapsedRealtime;
        long elapsedRealtime2 = SystemClock.elapsedRealtime();
        try {
            c c2 = c();
            if (c2 == null) {
                c2 = a(this.a, intent);
            }
            if (c2 != null) {
                this.d = c2.a;
                this.e = c2.b;
                this.f = c2.c;
                this.g = c2.d;
                this.h = c2.e;
            }
        } catch (Throwable th) {
            com.mob.commons.b.c.a().a(th);
        }
        elapsedRealtime = SystemClock.elapsedRealtime() - elapsedRealtime2;
        com.mob.commons.b.c.a().a("usedTime: " + elapsedRealtime);
        return elapsedRealtime;
    }

    private c a(Context context, Intent intent) throws Throwable {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new Throwable("unable to invoke in main thread!");
        }
        b bVar = new b();
        if (intent != null) {
            try {
                if (context.bindService(intent, bVar, 1)) {
                    IBinder a2 = bVar.a(d());
                    if (a2 != null) {
                        c a3 = a(a2);
                        try {
                            context.unbindService(bVar);
                        } catch (Throwable th) {
                            com.mob.commons.b.c.a().a(th);
                        }
                        return a3;
                    }
                    throw new Throwable("get binder " + intent.getComponent() + " failed!");
                }
            } catch (Throwable th2) {
                try {
                    context.unbindService(bVar);
                } catch (Throwable th3) {
                    com.mob.commons.b.c.a().a(th3);
                }
                throw th2;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("bind service ");
        sb.append(intent == null ? "null" : intent.getComponent());
        sb.append(" failed!");
        throw new Throwable(sb.toString());
    }

    private synchronized long j() {
        if (this.c) {
            return -1L;
        }
        long a2 = a(a());
        this.c = true;
        return a2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:34:0x005f A[Catch: Throwable -> 0x0062, TRY_LEAVE, TryCatch #6 {Throwable -> 0x0062, blocks: (B:39:0x005a, B:34:0x005f), top: B:38:0x005a }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x005a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int a(java.lang.String r5, android.os.IBinder r6, java.lang.String r7, int r8) {
        /*
            r4 = this;
            r0 = 0
            r1 = 0
            android.os.Parcel r2 = android.os.Parcel.obtain()     // Catch: java.lang.Throwable -> L2b android.os.RemoteException -> L2e
            android.os.Parcel r3 = android.os.Parcel.obtain()     // Catch: java.lang.Throwable -> L24 android.os.RemoteException -> L28
            r2.writeInterfaceToken(r7)     // Catch: java.lang.Throwable -> L22 android.os.RemoteException -> L29
            r6.transact(r8, r2, r3, r0)     // Catch: java.lang.Throwable -> L22 android.os.RemoteException -> L29
            r3.readException()     // Catch: java.lang.Throwable -> L22 android.os.RemoteException -> L29
            int r6 = r3.readInt()     // Catch: java.lang.Throwable -> L22 android.os.RemoteException -> L29
            if (r3 == 0) goto L1c
            r3.recycle()     // Catch: java.lang.Throwable -> L21
        L1c:
            if (r2 == 0) goto L21
            r2.recycle()     // Catch: java.lang.Throwable -> L21
        L21:
            return r6
        L22:
            r5 = move-exception
            goto L26
        L24:
            r5 = move-exception
            r3 = r1
        L26:
            r1 = r2
            goto L58
        L28:
            r3 = r1
        L29:
            r1 = r2
            goto L2f
        L2b:
            r5 = move-exception
            r3 = r1
            goto L58
        L2e:
            r3 = r1
        L2f:
            com.mob.commons.b.c r6 = com.mob.commons.b.c.a()     // Catch: java.lang.Throwable -> L57
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L57
            r7.<init>()     // Catch: java.lang.Throwable -> L57
            java.lang.String r8 = "getIntValue: "
            r7.append(r8)     // Catch: java.lang.Throwable -> L57
            r7.append(r5)     // Catch: java.lang.Throwable -> L57
            java.lang.String r5 = " failed! (remoteException)"
            r7.append(r5)     // Catch: java.lang.Throwable -> L57
            java.lang.String r5 = r7.toString()     // Catch: java.lang.Throwable -> L57
            r6.a(r5)     // Catch: java.lang.Throwable -> L57
            if (r3 == 0) goto L51
            r3.recycle()     // Catch: java.lang.Throwable -> L56
        L51:
            if (r1 == 0) goto L56
            r1.recycle()     // Catch: java.lang.Throwable -> L56
        L56:
            return r0
        L57:
            r5 = move-exception
        L58:
            if (r3 == 0) goto L5d
            r3.recycle()     // Catch: java.lang.Throwable -> L62
        L5d:
            if (r1 == 0) goto L62
            r1.recycle()     // Catch: java.lang.Throwable -> L62
        L62:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.b.f.a(java.lang.String, android.os.IBinder, java.lang.String, int):int");
    }

    protected Intent a() {
        return null;
    }

    protected c a(IBinder iBinder) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String a(String str, IBinder iBinder, String str2, int i, String... strArr) {
        Parcel parcel;
        Parcel parcel2;
        Parcel parcel3;
        try {
            try {
                parcel = Parcel.obtain();
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            parcel = null;
            parcel2 = null;
        }
        try {
            parcel3 = Parcel.obtain();
            try {
                parcel.writeInterfaceToken(str2);
                if (strArr != null && strArr.length > 0) {
                    for (String str3 : strArr) {
                        parcel.writeString(str3);
                    }
                }
                iBinder.transact(i, parcel, parcel3, 0);
                parcel3.readException();
                String readString = parcel3.readString();
                if (parcel3 != null) {
                    try {
                        parcel3.recycle();
                    } catch (Throwable unused) {
                    }
                }
                if (parcel != null) {
                    parcel.recycle();
                }
                return readString;
            } catch (Throwable th3) {
                th = th3;
                com.mob.commons.b.c.a().a("getStringValue: " + str + " failed! " + th.getMessage());
                if (parcel3 != null) {
                    try {
                        parcel3.recycle();
                    } catch (Throwable unused2) {
                        return null;
                    }
                }
                if (parcel != null) {
                    parcel.recycle();
                }
                return null;
            }
        } catch (Throwable th4) {
            th = th4;
            parcel3 = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void a(String str) {
        this.e = str;
    }

    public synchronized String b() {
        j();
        return this.f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void b(String str) {
        this.f = str;
    }

    protected c c() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void c(String str) {
        this.h = str;
    }

    protected long d() {
        return 2500L;
    }

    public synchronized String e() {
        j();
        return this.e;
    }

    public synchronized String f() {
        j();
        return this.g;
    }

    public synchronized String g() {
        j();
        return this.h;
    }

    public synchronized boolean h() {
        j();
        return this.d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String i() {
        if (TextUtils.isEmpty(this.b)) {
            return "";
        }
        return a.b("0x1008611" + this.b + "0xdzfdweiwu");
    }
}
