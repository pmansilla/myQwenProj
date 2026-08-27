package com.tencent.bugly.proguard;

import android.content.Context;
import com.tencent.bugly.BuglyStrategy;
import java.util.Map;
import java.util.UUID;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class v implements Runnable {
    private int a;
    private int b;
    private final Context c;
    private final int d;
    private final byte[] e;
    private final com.tencent.bugly.crashreport.common.info.a f;
    private final com.tencent.bugly.crashreport.common.strategy.a g;
    private final s h;
    private final u i;
    private final int j;
    private final t k;
    private final t l;
    private String m;
    private final String n;
    private final Map<String, String> o;
    private int p;
    private long q;
    private long r;
    private boolean s;

    public v(Context context, int i, int i2, byte[] bArr, String str, String str2, t tVar, int i3, int i4, boolean z, Map<String, String> map) {
        this.a = 2;
        this.b = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        this.m = null;
        this.p = 0;
        this.q = 0L;
        this.r = 0L;
        this.s = false;
        this.c = context;
        this.f = com.tencent.bugly.crashreport.common.info.a.a(context);
        this.e = bArr;
        this.g = com.tencent.bugly.crashreport.common.strategy.a.a();
        this.h = s.a(context);
        this.i = u.a();
        this.j = i;
        this.m = str;
        this.n = str2;
        this.k = tVar;
        u uVar = this.i;
        this.l = null;
        this.d = i2;
        if (i3 > 0) {
            this.a = i3;
        }
        if (i4 > 0) {
            this.b = i4;
        }
        this.s = z;
        this.o = map;
    }

    public v(Context context, int i, int i2, byte[] bArr, String str, String str2, t tVar, boolean z, boolean z2) {
        this(context, i, i2, bArr, str, str2, tVar, 2, BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH, z2, null);
    }

    private static String a(String str) {
        if (z.a(str)) {
            return str;
        }
        try {
            return String.format("%s?aid=%s", str, UUID.randomUUID().toString());
        } catch (Throwable th) {
            x.a(th);
            return str;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x002c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void a(com.tencent.bugly.proguard.an r5, boolean r6, int r7, java.lang.String r8) {
        /*
            r4 = this;
            int r5 = r4.d
            r0 = 630(0x276, float:8.83E-43)
            if (r5 == r0) goto L1c
            r0 = 640(0x280, float:8.97E-43)
            if (r5 == r0) goto L19
            r0 = 830(0x33e, float:1.163E-42)
            if (r5 == r0) goto L1c
            r0 = 840(0x348, float:1.177E-42)
            if (r5 == r0) goto L19
            int r5 = r4.d
            java.lang.String r5 = java.lang.String.valueOf(r5)
            goto L1e
        L19:
            java.lang.String r5 = "userinfo"
            goto L1e
        L1c:
            java.lang.String r5 = "crash"
        L1e:
            r0 = 0
            r1 = 1
            if (r6 == 0) goto L2c
            java.lang.String r7 = "[Upload] Success: %s"
            java.lang.Object[] r8 = new java.lang.Object[r1]
            r8[r0] = r5
            com.tencent.bugly.proguard.x.a(r7, r8)
            goto L3f
        L2c:
            java.lang.String r2 = "[Upload] Failed to upload(%d) %s: %s"
            r3 = 3
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)
            r3[r0] = r7
            r3[r1] = r5
            r5 = 2
            r3[r5] = r8
            com.tencent.bugly.proguard.x.e(r2, r3)
        L3f:
            long r7 = r4.q
            long r0 = r4.r
            long r7 = r7 + r0
            r0 = 0
            int r5 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
            if (r5 <= 0) goto L5f
            com.tencent.bugly.proguard.u r5 = r4.i
            boolean r7 = r4.s
            long r7 = r5.a(r7)
            long r0 = r4.q
            long r7 = r7 + r0
            long r0 = r4.r
            long r7 = r7 + r0
            com.tencent.bugly.proguard.u r5 = r4.i
            boolean r0 = r4.s
            r5.a(r7, r0)
        L5f:
            com.tencent.bugly.proguard.t r5 = r4.k
            if (r5 == 0) goto L6e
            com.tencent.bugly.proguard.t r5 = r4.k
            int r7 = r4.d
            long r7 = r4.q
            long r7 = r4.r
            r5.a(r6)
        L6e:
            com.tencent.bugly.proguard.t r5 = r4.l
            if (r5 == 0) goto L7d
            com.tencent.bugly.proguard.t r5 = r4.l
            int r7 = r4.d
            long r7 = r4.q
            long r7 = r4.r
            r5.a(r6)
        L7d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.v.a(com.tencent.bugly.proguard.an, boolean, int, java.lang.String):void");
    }

    private static boolean a(an anVar, com.tencent.bugly.crashreport.common.info.a aVar, com.tencent.bugly.crashreport.common.strategy.a aVar2) {
        if (anVar == null) {
            x.d("resp == null!", new Object[0]);
            return false;
        }
        if (anVar.a != 0) {
            x.e("resp result error %d", Byte.valueOf(anVar.a));
            return false;
        }
        try {
            if (!z.a(anVar.e) && !com.tencent.bugly.crashreport.common.info.a.b().i().equals(anVar.e)) {
                p.a().a(com.tencent.bugly.crashreport.common.strategy.a.a, "device", anVar.e.getBytes("UTF-8"), (o) null, true);
                aVar.e(anVar.e);
            }
        } catch (Throwable th) {
            x.a(th);
        }
        aVar.j = anVar.d;
        if (anVar.b == 510) {
            if (anVar.c == null) {
                x.e("[Upload] Strategy data is null. Response cmd: %d", Integer.valueOf(anVar.b));
                return false;
            }
            ap apVar = (ap) a.a(anVar.c, ap.class);
            if (apVar == null) {
                x.e("[Upload] Failed to decode strategy from server. Response cmd: %d", Integer.valueOf(anVar.b));
                return false;
            }
            aVar2.a(apVar);
        }
        return true;
    }

    public final void a(long j) {
        this.p++;
        this.q += j;
    }

    public final void b(long j) {
        this.r += j;
    }

    /* JADX WARN: Removed duplicated region for block: B:64:0x01c3 A[Catch: Throwable -> 0x032e, TryCatch #0 {Throwable -> 0x032e, blocks: (B:3:0x0001, B:5:0x0014, B:9:0x001c, B:12:0x0021, B:14:0x0035, B:16:0x0039, B:18:0x003d, B:21:0x0043, B:23:0x004b, B:25:0x0051, B:27:0x007e, B:28:0x0083, B:30:0x00b2, B:33:0x00ba, B:35:0x00c0, B:37:0x00cf, B:38:0x00d3, B:40:0x00d7, B:41:0x00db, B:42:0x00e2, B:45:0x00ea, B:47:0x0101, B:48:0x010e, B:50:0x014f, B:53:0x0164, B:55:0x016a, B:58:0x0171, B:60:0x0179, B:61:0x0192, B:64:0x01c3, B:66:0x01f1, B:67:0x01f9, B:69:0x01ff, B:71:0x021f, B:81:0x0257, B:83:0x0269, B:85:0x027a, B:86:0x0282, B:88:0x0288, B:90:0x02a2, B:93:0x02aa, B:95:0x02b0, B:98:0x02b8, B:100:0x02be, B:102:0x02c4, B:105:0x02d9, B:107:0x02ec, B:109:0x02f2, B:111:0x02d6, B:114:0x02f9, B:117:0x0182, B:119:0x018a, B:120:0x0196, B:122:0x01a6, B:123:0x01b0, B:124:0x01bb, B:126:0x031c, B:128:0x0322, B:130:0x0328), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0228 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void run() {
        /*
            Method dump skipped, instructions count: 825
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.v.run():void");
    }
}
