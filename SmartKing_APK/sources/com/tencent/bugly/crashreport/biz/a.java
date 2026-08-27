package com.tencent.bugly.crashreport.biz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.List;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class a {
    private Context a;
    private long b;
    private int c;
    private boolean d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BUGLY */
    /* renamed from: com.tencent.bugly.crashreport.biz.a$2, reason: invalid class name */
    /* loaded from: classes2.dex */
    public final class AnonymousClass2 implements Runnable {
        /* JADX INFO: Access modifiers changed from: package-private */
        public AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                a.this.c();
            } catch (Throwable th) {
                x.a(th);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BUGLY */
    /* renamed from: com.tencent.bugly.crashreport.biz.a$a, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public class RunnableC0072a implements Runnable {
        private boolean a;
        private UserInfoBean b;

        public RunnableC0072a(UserInfoBean userInfoBean, boolean z) {
            this.b = userInfoBean;
            this.a = z;
        }

        @Override // java.lang.Runnable
        public final void run() {
            com.tencent.bugly.crashreport.common.info.a b;
            try {
                if (this.b != null) {
                    UserInfoBean userInfoBean = this.b;
                    if (userInfoBean != null && (b = com.tencent.bugly.crashreport.common.info.a.b()) != null) {
                        userInfoBean.j = b.e();
                    }
                    x.c("[UserInfo] Record user info.", new Object[0]);
                    a.a(a.this, this.b, false);
                }
                if (this.a) {
                    a aVar = a.this;
                    w a = w.a();
                    if (a != null) {
                        a.a(new AnonymousClass2());
                    }
                }
            } catch (Throwable th) {
                if (x.a(th)) {
                    return;
                }
                th.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BUGLY */
    /* loaded from: classes2.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis < a.this.b) {
                w.a().a(new b(), (a.this.b - currentTimeMillis) + BootloaderScanner.TIMEOUT);
            } else {
                a.this.a(3, false, 0L);
                a.this.a();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BUGLY */
    /* loaded from: classes2.dex */
    public class c implements Runnable {
        private long a;

        public c(long j) {
            this.a = 21600000L;
            this.a = j;
        }

        @Override // java.lang.Runnable
        public final void run() {
            a aVar = a.this;
            w a = w.a();
            if (a != null) {
                a.a(new AnonymousClass2());
            }
            a aVar2 = a.this;
            long j = this.a;
            w.a().a(new c(j), j);
        }
    }

    public a(Context context, boolean z) {
        this.d = true;
        this.a = context;
        this.d = z;
    }

    private static ContentValues a(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (userInfoBean.a > 0) {
                contentValues.put(FileDownloadModel.ID, Long.valueOf(userInfoBean.a));
            }
            contentValues.put("_tm", Long.valueOf(userInfoBean.e));
            contentValues.put("_ut", Long.valueOf(userInfoBean.f));
            contentValues.put("_tp", Integer.valueOf(userInfoBean.b));
            contentValues.put("_pc", userInfoBean.c);
            contentValues.put("_dt", z.a(userInfoBean));
            return contentValues;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static UserInfoBean a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("_dt"));
            if (blob == null) {
                return null;
            }
            long j = cursor.getLong(cursor.getColumnIndex(FileDownloadModel.ID));
            UserInfoBean userInfoBean = (UserInfoBean) z.a(blob, UserInfoBean.CREATOR);
            if (userInfoBean != null) {
                userInfoBean.a = j;
            }
            return userInfoBean;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    static /* synthetic */ void a(a aVar, UserInfoBean userInfoBean, boolean z) {
        List<UserInfoBean> a;
        if (userInfoBean != null) {
            if (!z && userInfoBean.b != 1 && (a = aVar.a(com.tencent.bugly.crashreport.common.info.a.a(aVar.a).d)) != null && a.size() >= 20) {
                x.a("[UserInfo] There are too many user info in local: %d", Integer.valueOf(a.size()));
                return;
            }
            long a2 = p.a().a("t_ui", a(userInfoBean), (o) null, true);
            if (a2 >= 0) {
                x.c("[Database] insert %s success with ID: %d", "t_ui", Long.valueOf(a2));
                userInfoBean.a = a2;
            }
        }
    }

    private static void a(List<UserInfoBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size() && i < 50; i++) {
            UserInfoBean userInfoBean = list.get(i);
            sb.append(" or _id");
            sb.append(" = ");
            sb.append(userInfoBean.a);
        }
        String sb2 = sb.toString();
        if (sb2.length() > 0) {
            sb2 = sb2.substring(4);
        }
        String str = sb2;
        sb.setLength(0);
        try {
            x.c("[Database] deleted %s data %d", "t_ui", Integer.valueOf(p.a().a("t_ui", str, (String[]) null, (o) null, true)));
        } catch (Throwable th) {
            if (x.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0115  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x00f2 A[Catch: all -> 0x0173, TryCatch #0 {, blocks: (B:3:0x0001, B:8:0x0007, B:12:0x000f, B:16:0x0017, B:18:0x001d, B:22:0x0027, B:24:0x003c, B:27:0x0045, B:29:0x004c, B:30:0x004f, B:32:0x0055, B:34:0x0069, B:36:0x0079, B:43:0x0081, B:45:0x008b, B:46:0x0090, B:48:0x0096, B:50:0x00a4, B:52:0x00b1, B:53:0x00b4, B:56:0x00c2, B:58:0x00c6, B:60:0x00cb, B:63:0x00d0, B:73:0x00d7, B:74:0x00ec, B:76:0x00f2, B:78:0x00f7, B:81:0x00fe, B:84:0x0116, B:86:0x011c, B:89:0x0125, B:91:0x012b, B:94:0x0134, B:96:0x013e, B:99:0x0147, B:102:0x0165, B:107:0x016a, B:111:0x00e6), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x011c A[Catch: all -> 0x0173, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:8:0x0007, B:12:0x000f, B:16:0x0017, B:18:0x001d, B:22:0x0027, B:24:0x003c, B:27:0x0045, B:29:0x004c, B:30:0x004f, B:32:0x0055, B:34:0x0069, B:36:0x0079, B:43:0x0081, B:45:0x008b, B:46:0x0090, B:48:0x0096, B:50:0x00a4, B:52:0x00b1, B:53:0x00b4, B:56:0x00c2, B:58:0x00c6, B:60:0x00cb, B:63:0x00d0, B:73:0x00d7, B:74:0x00ec, B:76:0x00f2, B:78:0x00f7, B:81:0x00fe, B:84:0x0116, B:86:0x011c, B:89:0x0125, B:91:0x012b, B:94:0x0134, B:96:0x013e, B:99:0x0147, B:102:0x0165, B:107:0x016a, B:111:0x00e6), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0125 A[Catch: all -> 0x0173, TRY_ENTER, TryCatch #0 {, blocks: (B:3:0x0001, B:8:0x0007, B:12:0x000f, B:16:0x0017, B:18:0x001d, B:22:0x0027, B:24:0x003c, B:27:0x0045, B:29:0x004c, B:30:0x004f, B:32:0x0055, B:34:0x0069, B:36:0x0079, B:43:0x0081, B:45:0x008b, B:46:0x0090, B:48:0x0096, B:50:0x00a4, B:52:0x00b1, B:53:0x00b4, B:56:0x00c2, B:58:0x00c6, B:60:0x00cb, B:63:0x00d0, B:73:0x00d7, B:74:0x00ec, B:76:0x00f2, B:78:0x00f7, B:81:0x00fe, B:84:0x0116, B:86:0x011c, B:89:0x0125, B:91:0x012b, B:94:0x0134, B:96:0x013e, B:99:0x0147, B:102:0x0165, B:107:0x016a, B:111:0x00e6), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void c() {
        /*
            Method dump skipped, instructions count: 374
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.biz.a.c():void");
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x00be  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.tencent.bugly.crashreport.biz.UserInfoBean> a(java.lang.String r13) {
        /*
            r12 = this;
            r0 = 0
            boolean r1 = com.tencent.bugly.proguard.z.a(r13)     // Catch: java.lang.Throwable -> La5 java.lang.Throwable -> Laa
            if (r1 == 0) goto L9
            r4 = r0
            goto L1d
        L9:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La5 java.lang.Throwable -> Laa
            java.lang.String r2 = "_pc = '"
            r1.<init>(r2)     // Catch: java.lang.Throwable -> La5 java.lang.Throwable -> Laa
            r1.append(r13)     // Catch: java.lang.Throwable -> La5 java.lang.Throwable -> Laa
            java.lang.String r13 = "'"
            r1.append(r13)     // Catch: java.lang.Throwable -> La5 java.lang.Throwable -> Laa
            java.lang.String r13 = r1.toString()     // Catch: java.lang.Throwable -> La5 java.lang.Throwable -> Laa
            r4 = r13
        L1d:
            com.tencent.bugly.proguard.p r1 = com.tencent.bugly.proguard.p.a()     // Catch: java.lang.Throwable -> La5 java.lang.Throwable -> Laa
            java.lang.String r2 = "t_ui"
            r3 = 0
            r5 = 0
            r6 = 0
            r7 = 1
            android.database.Cursor r13 = r1.a(r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> La5 java.lang.Throwable -> Laa
            if (r13 != 0) goto L33
            if (r13 == 0) goto L32
            r13.close()
        L32:
            return r0
        L33:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            r1.<init>()     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            r2.<init>()     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
        L3d:
            boolean r3 = r13.moveToNext()     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            r4 = 0
            if (r3 == 0) goto L6e
            com.tencent.bugly.crashreport.biz.UserInfoBean r3 = a(r13)     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            if (r3 == 0) goto L4e
            r2.add(r3)     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            goto L3d
        L4e:
            java.lang.String r3 = "_id"
            int r3 = r13.getColumnIndex(r3)     // Catch: java.lang.Throwable -> L66 java.lang.Throwable -> Lbb
            long r5 = r13.getLong(r3)     // Catch: java.lang.Throwable -> L66 java.lang.Throwable -> Lbb
            java.lang.String r3 = " or _id"
            r1.append(r3)     // Catch: java.lang.Throwable -> L66 java.lang.Throwable -> Lbb
            java.lang.String r3 = " = "
            r1.append(r3)     // Catch: java.lang.Throwable -> L66 java.lang.Throwable -> Lbb
            r1.append(r5)     // Catch: java.lang.Throwable -> L66 java.lang.Throwable -> Lbb
            goto L3d
        L66:
            java.lang.String r3 = "[Database] unknown id."
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            com.tencent.bugly.proguard.x.d(r3, r4)     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            goto L3d
        L6e:
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            int r3 = r1.length()     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            if (r3 <= 0) goto L9d
            r3 = 4
            java.lang.String r7 = r1.substring(r3)     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            com.tencent.bugly.proguard.p r5 = com.tencent.bugly.proguard.p.a()     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            java.lang.String r6 = "t_ui"
            r8 = 0
            r9 = 0
            r10 = 1
            int r1 = r5.a(r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            java.lang.String r3 = "[Database] deleted %s error data %d"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            java.lang.String r6 = "t_ui"
            r5[r4] = r6     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            r4 = 1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            r5[r4] = r1     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
            com.tencent.bugly.proguard.x.d(r3, r5)     // Catch: java.lang.Throwable -> La3 java.lang.Throwable -> Lbb
        L9d:
            if (r13 == 0) goto La2
            r13.close()
        La2:
            return r2
        La3:
            r1 = move-exception
            goto Lac
        La5:
            r13 = move-exception
            r11 = r0
            r0 = r13
            r13 = r11
            goto Lbc
        Laa:
            r1 = move-exception
            r13 = r0
        Lac:
            boolean r2 = com.tencent.bugly.proguard.x.a(r1)     // Catch: java.lang.Throwable -> Lbb
            if (r2 != 0) goto Lb5
            r1.printStackTrace()     // Catch: java.lang.Throwable -> Lbb
        Lb5:
            if (r13 == 0) goto Lba
            r13.close()
        Lba:
            return r0
        Lbb:
            r0 = move-exception
        Lbc:
            if (r13 == 0) goto Lc1
            r13.close()
        Lc1:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.biz.a.a(java.lang.String):java.util.List");
    }

    public final void a() {
        this.b = z.b() + DateUtils.MILLIS_PER_DAY;
        w.a().a(new b(), (this.b - System.currentTimeMillis()) + BootloaderScanner.TIMEOUT);
    }

    public final void a(int i, boolean z, long j) {
        com.tencent.bugly.crashreport.common.strategy.a a = com.tencent.bugly.crashreport.common.strategy.a.a();
        if (a != null && !a.c().f && i != 1 && i != 3) {
            x.e("UserInfo is disable", new Object[0]);
            return;
        }
        if (i == 1 || i == 3) {
            this.c++;
        }
        com.tencent.bugly.crashreport.common.info.a a2 = com.tencent.bugly.crashreport.common.info.a.a(this.a);
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.b = i;
        userInfoBean.c = a2.d;
        userInfoBean.d = a2.g();
        userInfoBean.e = System.currentTimeMillis();
        userInfoBean.f = -1L;
        userInfoBean.n = a2.k;
        userInfoBean.o = i == 1 ? 1 : 0;
        userInfoBean.l = a2.a();
        userInfoBean.m = a2.q;
        userInfoBean.g = a2.r;
        userInfoBean.h = a2.s;
        userInfoBean.i = a2.t;
        userInfoBean.k = a2.u;
        userInfoBean.r = a2.t();
        userInfoBean.s = a2.y();
        userInfoBean.p = a2.z();
        userInfoBean.q = a2.A();
        w.a().a(new RunnableC0072a(userInfoBean, z), 0L);
    }

    public final void b() {
        w a = w.a();
        if (a != null) {
            a.a(new AnonymousClass2());
        }
    }
}
