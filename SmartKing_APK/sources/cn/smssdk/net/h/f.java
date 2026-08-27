package cn.smssdk.net.h;

import com.amap.location.common.model.AmapLoc;
import java.security.MessageDigest;

/* compiled from: Md5Util.java */
/* loaded from: classes.dex */
public class f {
    private static final String[] a = {AmapLoc.RESULT_TYPE_GPS, AmapLoc.RESULT_TYPE_WIFI_ONLY, AmapLoc.RESULT_TYPE_FUSED, AmapLoc.RESULT_TYPE_CELL_ONLY, AmapLoc.RESULT_TYPE_CELL_WITH_NEIGHBORS, AmapLoc.RESULT_TYPE_SELF_LAT_LON, AmapLoc.RESULT_TYPE_NO_LONGER_USED, "7", AmapLoc.RESULT_TYPE_FAIL, AmapLoc.RESULT_TYPE_CELL_WITHIN_SAME_ADDRESS, "a", "b", "c", "d", "e", "f"};

    /* JADX WARN: Code restructure failed: missing block: B:0:?, code lost:
    
        r3 = r3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String a(byte r3) {
        /*
            if (r3 >= 0) goto L4
            int r3 = r3 + 256
        L4:
            int r0 = r3 / 16
            int r3 = r3 % 16
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String[] r2 = cn.smssdk.net.h.f.a
            r0 = r2[r0]
            r1.append(r0)
            java.lang.String[] r0 = cn.smssdk.net.h.f.a
            r3 = r0[r3]
            r1.append(r3)
            java.lang.String r3 = r1.toString()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.smssdk.net.h.f.a(byte):java.lang.String");
    }

    public static String a(String str, String str2) {
        String a2;
        try {
            String str3 = new String(str);
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                if (str2 != null && !"".equals(str2)) {
                    a2 = a(messageDigest.digest(str3.getBytes(str2)));
                    return a2;
                }
                a2 = a(messageDigest.digest(str3.getBytes()));
                return a2;
            } catch (Exception unused) {
                return str3;
            }
        } catch (Exception unused2) {
            return null;
        }
    }

    public static String a(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            stringBuffer.append(a(b));
        }
        return stringBuffer.toString();
    }
}
