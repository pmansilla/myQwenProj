package com.tencent.bugly.proguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class n {
    public static final long a = System.currentTimeMillis();
    private static n b;
    private Context c;
    private SharedPreferences f;
    private Map<Integer, Map<String, m>> e = new HashMap();
    private String d = com.tencent.bugly.crashreport.common.info.a.b().d;

    private n(Context context) {
        this.c = context;
        this.f = context.getSharedPreferences("crashrecord", 0);
    }

    public static synchronized n a() {
        n nVar;
        synchronized (n.class) {
            nVar = b;
        }
        return nVar;
    }

    public static synchronized n a(Context context) {
        n nVar;
        synchronized (n.class) {
            if (b == null) {
                b = new n(context);
            }
            nVar = b;
        }
        return nVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized <T extends List<?>> void a(int i, T t) {
        ObjectOutputStream objectOutputStream;
        ObjectOutputStream objectOutputStream2;
        if (t == null) {
            return;
        }
        try {
            File dir = this.c.getDir("crashrecord", 0);
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            objectOutputStream = null;
            try {
                try {
                    objectOutputStream2 = new ObjectOutputStream(new FileOutputStream(new File(dir, sb.toString())));
                } catch (Throwable th) {
                    th = th;
                }
            } catch (IOException e) {
                e = e;
            }
        } catch (Exception unused) {
            x.e("writeCrashRecord error", new Object[0]);
        }
        try {
            objectOutputStream2.writeObject(t);
            objectOutputStream2.close();
        } catch (IOException e2) {
            e = e2;
            objectOutputStream = objectOutputStream2;
            e.printStackTrace();
            x.a("open record file error", new Object[0]);
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
        } catch (Throwable th2) {
            th = th2;
            objectOutputStream = objectOutputStream2;
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean b(int i) {
        try {
            List<m> c = c(i);
            if (c == null) {
                return false;
            }
            long currentTimeMillis = System.currentTimeMillis();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (m mVar : c) {
                if (mVar.b != null && mVar.b.equalsIgnoreCase(this.d) && mVar.d > 0) {
                    arrayList.add(mVar);
                }
                if (mVar.c + DateUtils.MILLIS_PER_DAY < currentTimeMillis) {
                    arrayList2.add(mVar);
                }
            }
            Collections.sort(arrayList);
            if (arrayList.size() < 2) {
                c.removeAll(arrayList2);
                a(i, (int) c);
                return false;
            }
            if (arrayList.size() <= 0 || ((m) arrayList.get(arrayList.size() - 1)).c + DateUtils.MILLIS_PER_DAY >= currentTimeMillis) {
                return true;
            }
            c.clear();
            a(i, (int) c);
            return false;
        } catch (Exception unused) {
            x.e("isFrequentCrash failed", new Object[0]);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0052, code lost:
    
        if (r6 == null) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0046, code lost:
    
        r6.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0044, code lost:
    
        if (r6 == null) goto L31;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v4, types: [boolean] */
    /* JADX WARN: Type inference failed for: r6v6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized <T extends java.util.List<?>> T c(int r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            r0 = 0
            r1 = 0
            java.io.File r2 = new java.io.File     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
            android.content.Context r3 = r5.c     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
            java.lang.String r4 = "crashrecord"
            java.io.File r3 = r3.getDir(r4, r1)     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
            r4.<init>()     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
            r4.append(r6)     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
            java.lang.String r6 = r4.toString()     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
            r2.<init>(r3, r6)     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
            boolean r6 = r2.exists()     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
            if (r6 != 0) goto L24
            monitor-exit(r5)
            return r0
        L24:
            java.io.ObjectInputStream r6 = new java.io.ObjectInputStream     // Catch: java.lang.Throwable -> L39 java.lang.ClassNotFoundException -> L3c java.io.IOException -> L4a
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L39 java.lang.ClassNotFoundException -> L3c java.io.IOException -> L4a
            r3.<init>(r2)     // Catch: java.lang.Throwable -> L39 java.lang.ClassNotFoundException -> L3c java.io.IOException -> L4a
            r6.<init>(r3)     // Catch: java.lang.Throwable -> L39 java.lang.ClassNotFoundException -> L3c java.io.IOException -> L4a
            java.lang.Object r2 = r6.readObject()     // Catch: java.lang.ClassNotFoundException -> L3d java.io.IOException -> L4b java.lang.Throwable -> L55
            java.util.List r2 = (java.util.List) r2     // Catch: java.lang.ClassNotFoundException -> L3d java.io.IOException -> L4b java.lang.Throwable -> L55
            r6.close()     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
            monitor-exit(r5)
            return r2
        L39:
            r2 = move-exception
            r6 = r0
            goto L56
        L3c:
            r6 = r0
        L3d:
            java.lang.String r2 = "get object error"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L55
            com.tencent.bugly.proguard.x.a(r2, r3)     // Catch: java.lang.Throwable -> L55
            if (r6 == 0) goto L65
        L46:
            r6.close()     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
            goto L65
        L4a:
            r6 = r0
        L4b:
            java.lang.String r2 = "open record file error"
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L55
            com.tencent.bugly.proguard.x.a(r2, r3)     // Catch: java.lang.Throwable -> L55
            if (r6 == 0) goto L65
            goto L46
        L55:
            r2 = move-exception
        L56:
            if (r6 == 0) goto L5b
            r6.close()     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
        L5b:
            throw r2     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
        L5c:
            r6 = move-exception
            goto L67
        L5e:
            java.lang.String r6 = "readCrashRecord error"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L5c
            com.tencent.bugly.proguard.x.e(r6, r1)     // Catch: java.lang.Throwable -> L5c
        L65:
            monitor-exit(r5)
            return r0
        L67:
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.n.c(int):java.util.List");
    }

    public final void a(int i, final int i2) {
        final int i3 = 1004;
        w.a().a(new Runnable() { // from class: com.tencent.bugly.proguard.n.1
            @Override // java.lang.Runnable
            public final void run() {
                m mVar;
                try {
                    if (TextUtils.isEmpty(n.this.d)) {
                        return;
                    }
                    List<m> c = n.this.c(i3);
                    if (c == null) {
                        c = new ArrayList();
                    }
                    if (n.this.e.get(Integer.valueOf(i3)) == null) {
                        n.this.e.put(Integer.valueOf(i3), new HashMap());
                    }
                    if (((Map) n.this.e.get(Integer.valueOf(i3))).get(n.this.d) == null) {
                        mVar = new m();
                        mVar.a = i3;
                        mVar.g = n.a;
                        mVar.b = n.this.d;
                        mVar.f = com.tencent.bugly.crashreport.common.info.a.b().k;
                        mVar.e = com.tencent.bugly.crashreport.common.info.a.b().f;
                        mVar.c = System.currentTimeMillis();
                        mVar.d = i2;
                        ((Map) n.this.e.get(Integer.valueOf(i3))).put(n.this.d, mVar);
                    } else {
                        mVar = (m) ((Map) n.this.e.get(Integer.valueOf(i3))).get(n.this.d);
                        mVar.d = i2;
                    }
                    ArrayList arrayList = new ArrayList();
                    boolean z = false;
                    for (m mVar2 : c) {
                        if (mVar2.g == mVar.g && mVar2.b != null && mVar2.b.equalsIgnoreCase(mVar.b)) {
                            z = true;
                            mVar2.d = mVar.d;
                        }
                        if ((mVar2.e != null && !mVar2.e.equalsIgnoreCase(mVar.e)) || ((mVar2.f != null && !mVar2.f.equalsIgnoreCase(mVar.f)) || mVar2.d <= 0)) {
                            arrayList.add(mVar2);
                        }
                    }
                    c.removeAll(arrayList);
                    if (!z) {
                        c.add(mVar);
                    }
                    n.this.a(i3, (int) c);
                } catch (Exception unused) {
                    x.e("saveCrashRecord failed", new Object[0]);
                }
            }
        });
    }

    public final synchronized boolean a(final int i) {
        boolean z;
        boolean z2 = true;
        try {
            z = this.f.getBoolean(i + "_" + this.d, true);
        } catch (Exception unused) {
        }
        try {
            w.a().a(new Runnable() { // from class: com.tencent.bugly.proguard.n.2
                @Override // java.lang.Runnable
                public final void run() {
                    boolean b2 = n.this.b(i);
                    n.this.f.edit().putBoolean(i + "_" + n.this.d, !b2).commit();
                }
            });
            return z;
        } catch (Exception unused2) {
            z2 = z;
            x.e("canInit error", new Object[0]);
            return z2;
        }
    }
}
