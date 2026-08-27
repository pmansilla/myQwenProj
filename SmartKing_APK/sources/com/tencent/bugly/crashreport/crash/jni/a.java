package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class a implements NativeExceptionHandler {
    private final Context a;
    private final com.tencent.bugly.crashreport.crash.b b;
    private final com.tencent.bugly.crashreport.common.info.a c;
    private final com.tencent.bugly.crashreport.common.strategy.a d;

    public a(Context context, com.tencent.bugly.crashreport.common.info.a aVar, com.tencent.bugly.crashreport.crash.b bVar, com.tencent.bugly.crashreport.common.strategy.a aVar2) {
        this.a = context;
        this.b = bVar;
        this.c = aVar;
        this.d = aVar2;
    }

    @Override // com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler
    public final void handleNativeException(int i, int i2, long j, long j2, String str, String str2, String str3, String str4, int i3, String str5, int i4, int i5, int i6, String str6, String str7) {
        x.a("Native Crash Happen v1", new Object[0]);
        handleNativeException2(i, i2, j, j2, str, str2, str3, str4, i3, str5, i4, i5, i6, str6, str7, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x011f A[Catch: Throwable -> 0x02a6, TryCatch #2 {Throwable -> 0x02a6, blocks: (B:3:0x0010, B:5:0x0018, B:6:0x006f, B:9:0x0078, B:11:0x007b, B:13:0x007f, B:15:0x0098, B:18:0x00a0, B:17:0x00a9, B:22:0x00b3, B:24:0x00bd, B:26:0x00c5, B:27:0x00d1, B:29:0x00db, B:32:0x00e2, B:33:0x00f0, B:35:0x00fc, B:38:0x0104, B:39:0x0119, B:41:0x011f, B:44:0x012f, B:46:0x0153, B:48:0x0199, B:50:0x01bd, B:51:0x01c4, B:53:0x01ce, B:55:0x01d6, B:89:0x0172, B:90:0x00ec, B:92:0x00ac, B:95:0x0041, B:96:0x0047, B:98:0x0051), top: B:2:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0153 A[Catch: Throwable -> 0x02a6, TryCatch #2 {Throwable -> 0x02a6, blocks: (B:3:0x0010, B:5:0x0018, B:6:0x006f, B:9:0x0078, B:11:0x007b, B:13:0x007f, B:15:0x0098, B:18:0x00a0, B:17:0x00a9, B:22:0x00b3, B:24:0x00bd, B:26:0x00c5, B:27:0x00d1, B:29:0x00db, B:32:0x00e2, B:33:0x00f0, B:35:0x00fc, B:38:0x0104, B:39:0x0119, B:41:0x011f, B:44:0x012f, B:46:0x0153, B:48:0x0199, B:50:0x01bd, B:51:0x01c4, B:53:0x01ce, B:55:0x01d6, B:89:0x0172, B:90:0x00ec, B:92:0x00ac, B:95:0x0041, B:96:0x0047, B:98:0x0051), top: B:2:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01bd A[Catch: Throwable -> 0x02a6, TryCatch #2 {Throwable -> 0x02a6, blocks: (B:3:0x0010, B:5:0x0018, B:6:0x006f, B:9:0x0078, B:11:0x007b, B:13:0x007f, B:15:0x0098, B:18:0x00a0, B:17:0x00a9, B:22:0x00b3, B:24:0x00bd, B:26:0x00c5, B:27:0x00d1, B:29:0x00db, B:32:0x00e2, B:33:0x00f0, B:35:0x00fc, B:38:0x0104, B:39:0x0119, B:41:0x011f, B:44:0x012f, B:46:0x0153, B:48:0x0199, B:50:0x01bd, B:51:0x01c4, B:53:0x01ce, B:55:0x01d6, B:89:0x0172, B:90:0x00ec, B:92:0x00ac, B:95:0x0041, B:96:0x0047, B:98:0x0051), top: B:2:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01ce A[Catch: Throwable -> 0x02a6, TryCatch #2 {Throwable -> 0x02a6, blocks: (B:3:0x0010, B:5:0x0018, B:6:0x006f, B:9:0x0078, B:11:0x007b, B:13:0x007f, B:15:0x0098, B:18:0x00a0, B:17:0x00a9, B:22:0x00b3, B:24:0x00bd, B:26:0x00c5, B:27:0x00d1, B:29:0x00db, B:32:0x00e2, B:33:0x00f0, B:35:0x00fc, B:38:0x0104, B:39:0x0119, B:41:0x011f, B:44:0x012f, B:46:0x0153, B:48:0x0199, B:50:0x01bd, B:51:0x01c4, B:53:0x01ce, B:55:0x01d6, B:89:0x0172, B:90:0x00ec, B:92:0x00ac, B:95:0x0041, B:96:0x0047, B:98:0x0051), top: B:2:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0234 A[Catch: Throwable -> 0x02a2, TryCatch #1 {Throwable -> 0x02a2, blocks: (B:61:0x022e, B:63:0x0234, B:65:0x023d), top: B:60:0x022e }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x023d A[Catch: Throwable -> 0x02a2, TRY_LEAVE, TryCatch #1 {Throwable -> 0x02a2, blocks: (B:61:0x022e, B:63:0x0234, B:65:0x023d), top: B:60:0x022e }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x014f A[SYNTHETIC] */
    @Override // com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void handleNativeException2(int r26, int r27, long r28, long r30, java.lang.String r32, java.lang.String r33, java.lang.String r34, java.lang.String r35, int r36, java.lang.String r37, int r38, int r39, int r40, java.lang.String r41, java.lang.String r42, java.lang.String[] r43) {
        /*
            Method dump skipped, instructions count: 690
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.a.handleNativeException2(int, int, long, long, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, int, int, int, java.lang.String, java.lang.String, java.lang.String[]):void");
    }

    @Override // com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler
    public final CrashDetailBean packageCrashDatas(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, byte[] bArr, Map<String, String> map, boolean z, boolean z2) {
        int length;
        String str12;
        int indexOf;
        boolean m = c.a().m();
        if (m) {
            x.e("This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful!", new Object[0]);
        }
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        crashDetailBean.b = 1;
        crashDetailBean.e = this.c.h();
        crashDetailBean.f = this.c.k;
        crashDetailBean.g = this.c.q();
        crashDetailBean.m = this.c.g();
        crashDetailBean.n = str3;
        crashDetailBean.o = m ? " This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful![Bugly]" : "";
        crashDetailBean.p = str4;
        crashDetailBean.q = str5 == null ? "" : str5;
        crashDetailBean.r = j;
        crashDetailBean.u = z.a(crashDetailBean.q.getBytes());
        crashDetailBean.A = str;
        crashDetailBean.B = str2;
        crashDetailBean.I = this.c.s();
        crashDetailBean.h = this.c.p();
        crashDetailBean.i = this.c.B();
        crashDetailBean.v = str8;
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
        String dumpFilePath = nativeCrashHandler != null ? nativeCrashHandler.getDumpFilePath() : null;
        String a = b.a(dumpFilePath, str8);
        if (!z.a(a)) {
            crashDetailBean.V = a;
        }
        crashDetailBean.W = b.b(dumpFilePath);
        crashDetailBean.w = b.a(str9, c.e, null, false);
        crashDetailBean.x = b.a(str10, c.e, null, true);
        crashDetailBean.J = str7;
        crashDetailBean.K = str6;
        crashDetailBean.L = str11;
        crashDetailBean.F = this.c.k();
        crashDetailBean.G = this.c.j();
        crashDetailBean.H = this.c.l();
        if (z) {
            crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.g();
            crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.e();
            crashDetailBean.E = com.tencent.bugly.crashreport.common.info.b.i();
            if (crashDetailBean.w == null) {
                crashDetailBean.w = z.a(this.a, c.e, (String) null);
            }
            crashDetailBean.y = y.a();
            crashDetailBean.M = this.c.a;
            crashDetailBean.N = this.c.a();
            crashDetailBean.z = z.a(c.f, false);
            int indexOf2 = crashDetailBean.q.indexOf("java:\n");
            if (indexOf2 > 0 && (length = indexOf2 + "java:\n".length()) < crashDetailBean.q.length()) {
                String substring = crashDetailBean.q.substring(length, crashDetailBean.q.length() - 1);
                if (substring.length() > 0 && crashDetailBean.z.containsKey(crashDetailBean.B) && (indexOf = (str12 = crashDetailBean.z.get(crashDetailBean.B)).indexOf(substring)) > 0) {
                    String substring2 = str12.substring(indexOf);
                    crashDetailBean.z.put(crashDetailBean.B, substring2);
                    crashDetailBean.q = crashDetailBean.q.substring(0, length);
                    crashDetailBean.q += substring2;
                }
            }
            if (str == null) {
                crashDetailBean.A = this.c.d;
            }
            this.b.d(crashDetailBean);
            crashDetailBean.Q = this.c.z();
            crashDetailBean.R = this.c.A();
            crashDetailBean.S = this.c.t();
            crashDetailBean.T = this.c.y();
        } else {
            crashDetailBean.C = -1L;
            crashDetailBean.D = -1L;
            crashDetailBean.E = -1L;
            if (crashDetailBean.w == null) {
                crashDetailBean.w = "this crash is occurred at last process! Log is miss, when get an terrible ABRT Native Exception etc.";
            }
            crashDetailBean.M = -1L;
            crashDetailBean.Q = -1;
            crashDetailBean.R = -1;
            crashDetailBean.S = map;
            crashDetailBean.T = this.c.y();
            crashDetailBean.z = null;
            if (str == null) {
                crashDetailBean.A = "unknown(record)";
            }
            if (bArr != null) {
                crashDetailBean.y = bArr;
            }
        }
        return crashDetailBean;
    }
}
