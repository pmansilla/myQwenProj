package com.loc;

import android.content.Context;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: StatisticsManager.java */
/* loaded from: classes.dex */
public class br {
    private static WeakReference<bl> a;

    public static void a(final Context context) {
        aq.d().submit(new Runnable() { // from class: com.loc.br.2
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    bl a2 = bs.a(br.a);
                    bs.a(context, a2, ao.h, 1000, 307200, AmapLoc.RESULT_TYPE_FUSED);
                    if (a2.g == null) {
                        a2.g = new bt(new bx(context, new bu(new by(new ca()))));
                    }
                    a2.h = DateUtils.MILLIS_IN_HOUR;
                    if (TextUtils.isEmpty(a2.i)) {
                        a2.i = "cKey";
                    }
                    if (a2.f == null) {
                        a2.f = new ce(context, a2.h, a2.i, new cb(30, a2.a, new cg(context)));
                    }
                    bm.a(a2);
                } catch (Throwable th) {
                    aq.b(th, "stm", "usd");
                }
            }
        });
    }

    static /* synthetic */ void a(Context context, byte[] bArr) throws IOException {
        bl a2 = bs.a(a);
        bs.a(context, a2, ao.h, 1000, 307200, AmapLoc.RESULT_TYPE_FUSED);
        if (a2.e == null) {
            a2.e = new aj();
        }
        try {
            bm.a(Integer.toString(new Random().nextInt(100)) + Long.toString(System.nanoTime()), bArr, a2);
        } catch (Throwable th) {
            aq.b(th, "stm", "wts");
        }
    }

    public static synchronized void a(final List<bq> list, final Context context) {
        synchronized (br.class) {
            try {
                if (list.size() == 0) {
                    return;
                }
            } catch (Throwable unused) {
            }
            aq.d().submit(new Runnable() { // from class: com.loc.br.1
                @Override // java.lang.Runnable
                public final void run() {
                    byte[] bArr;
                    ByteArrayOutputStream byteArrayOutputStream;
                    Throwable th;
                    byte[] bArr2;
                    try {
                        synchronized (br.class) {
                            ByteArrayOutputStream byteArrayOutputStream2 = null;
                            try {
                                bArr = new byte[0];
                            } catch (Throwable th2) {
                                th = th2;
                            }
                            try {
                                byteArrayOutputStream = new ByteArrayOutputStream();
                                try {
                                    for (bq bqVar : list) {
                                        if (bqVar != null) {
                                            byteArrayOutputStream.write(bqVar.a());
                                        }
                                    }
                                    bArr2 = byteArrayOutputStream.toByteArray();
                                    try {
                                        byteArrayOutputStream.close();
                                    } catch (Throwable th3) {
                                        th3.printStackTrace();
                                    }
                                } catch (Throwable th4) {
                                    th = th4;
                                    aq.b(th, "stm", "aStB");
                                    if (byteArrayOutputStream != null) {
                                        try {
                                            byteArrayOutputStream.close();
                                        } catch (Throwable th5) {
                                            th5.printStackTrace();
                                        }
                                    }
                                    bArr2 = bArr;
                                    br.a(context, bArr2);
                                }
                            } catch (Throwable th6) {
                                th = th6;
                                if (0 != 0) {
                                    try {
                                        byteArrayOutputStream2.close();
                                    } catch (Throwable th7) {
                                        th7.printStackTrace();
                                    }
                                }
                                throw th;
                            }
                            br.a(context, bArr2);
                        }
                    } catch (Throwable th8) {
                        aq.b(th8, "stm", "apb");
                    }
                }
            });
        }
    }
}
