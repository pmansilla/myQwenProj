package com.loc;

import android.content.Context;
import android.text.TextUtils;
import com.amap.api.maps.AMapException;

/* compiled from: StatisticsEntity.java */
/* loaded from: classes.dex */
public final class bq {
    private Context a;
    private String b;
    private String c;
    private String d;
    private String e;

    public bq(Context context, String str, String str2, String str3) throws t {
        if (TextUtils.isEmpty(str3) || str3.length() > 256) {
            throw new t(AMapException.ERROR_INVALID_PARAMETER);
        }
        this.a = context.getApplicationContext();
        this.c = str;
        this.d = str2;
        this.b = str3;
    }

    public final void a(String str) throws t {
        if (TextUtils.isEmpty(str) || str.length() > 65536) {
            throw new t(AMapException.ERROR_INVALID_PARAMETER);
        }
        this.e = str;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(10:1|(9:2|3|4|5|6|7|8|9|10)|(2:24|(1:26)(6:27|13|14|15|17|18))|12|13|14|15|17|18|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x008b, code lost:
    
        r1 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x008c, code lost:
    
        r1.printStackTrace();
        r2 = r2;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v16, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v18, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v30 */
    /* JADX WARN: Type inference failed for: r2v31 */
    /* JADX WARN: Type inference failed for: r2v32 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final byte[] a() {
        /*
            r8 = this;
            r0 = 0
            byte[] r1 = new byte[r0]
            r2 = 0
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
            r3.<init>()     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
            java.lang.String r2 = r8.c     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            com.loc.ad.a(r3, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            java.lang.String r2 = r8.d     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            com.loc.ad.a(r3, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            java.lang.String r2 = r8.b     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            com.loc.ad.a(r3, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            android.content.Context r2 = r8.a     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            int r2 = com.loc.x.q(r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            com.loc.ad.a(r3, r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            long r4 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L2e java.lang.Throwable -> L90
            r6 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r6
            int r2 = (int) r4
            goto L2f
        L2e:
            r2 = 0
        L2f:
            r4 = 4
            byte[] r4 = new byte[r4]     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            int r5 = r2 >> 24
            r5 = r5 & 255(0xff, float:3.57E-43)
            byte r5 = (byte) r5     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r4[r0] = r5     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            int r5 = r2 >> 16
            r5 = r5 & 255(0xff, float:3.57E-43)
            byte r5 = (byte) r5     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r6 = 1
            r4[r6] = r5     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            int r5 = r2 >> 8
            r5 = r5 & 255(0xff, float:3.57E-43)
            byte r5 = (byte) r5     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r7 = 2
            r4[r7] = r5     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r5 = 3
            r2 = r2 & 255(0xff, float:3.57E-43)
            byte r2 = (byte) r2     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r4[r5] = r2     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r3.write(r4)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            java.lang.String r2 = r8.e     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            if (r2 == 0) goto L60
        L5a:
            byte[] r0 = new byte[r7]     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r0 = {x00b8: FILL_ARRAY_DATA , data: [0, 0} // fill-array     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            goto L77
        L60:
            java.lang.String r2 = r8.e     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            byte[] r2 = com.loc.ad.a(r2)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            if (r2 != 0) goto L69
            goto L5a
        L69:
            int r2 = r2.length     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            int r4 = r2 / 256
            byte r4 = (byte) r4     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            int r2 = r2 % 256
            byte r2 = (byte) r2     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            byte[] r5 = new byte[r7]     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r5[r0] = r4     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r5[r6] = r2     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r0 = r5
        L77:
            r3.write(r0)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            java.lang.String r0 = r8.e     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            byte[] r0 = com.loc.ad.a(r0)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r3.write(r0)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            byte[] r0 = r3.toByteArray()     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> L92
            r3.close()     // Catch: java.lang.Throwable -> L8b
            goto Lab
        L8b:
            r1 = move-exception
            r1.printStackTrace()
            goto Lab
        L90:
            r0 = move-exception
            goto Lac
        L92:
            r0 = move-exception
            r2 = r3
            goto L99
        L95:
            r0 = move-exception
            r3 = r2
            goto Lac
        L98:
            r0 = move-exception
        L99:
            java.lang.String r3 = "se"
            java.lang.String r4 = "tds"
            com.loc.aq.b(r0, r3, r4)     // Catch: java.lang.Throwable -> L95
            if (r2 == 0) goto Laa
            r2.close()     // Catch: java.lang.Throwable -> La6
            goto Laa
        La6:
            r0 = move-exception
            r0.printStackTrace()
        Laa:
            r0 = r1
        Lab:
            return r0
        Lac:
            if (r3 == 0) goto Lb6
            r3.close()     // Catch: java.lang.Throwable -> Lb2
            goto Lb6
        Lb2:
            r1 = move-exception
            r1.printStackTrace()
        Lb6:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.bq.a():byte[]");
    }
}
