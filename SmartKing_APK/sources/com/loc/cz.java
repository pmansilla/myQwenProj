package com.loc;

import android.content.Context;
import android.provider.Settings;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.zip.Adler32;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: UTUtdid.java */
/* loaded from: classes.dex */
public final class cz {
    private static final Object b = new Object();
    private static cz c = null;
    private static final String j = ".UTSystemConfig" + File.separator + "Global";
    private Context a;
    private da e;
    private String f;
    private String g;
    private dz h;
    private dz i;
    private String d = null;
    private Pattern k = Pattern.compile("[^0-9a-zA-Z=/+]+");

    private cz(Context context) {
        this.a = null;
        this.e = null;
        this.f = "xx_utdid_key";
        this.g = "xx_utdid_domain";
        this.h = null;
        this.i = null;
        this.a = context;
        this.i = new dz(context, j, "Alvin2");
        this.h = new dz(context, ".DataStorage", "ContextData");
        this.e = new da();
        this.f = String.format("K_%d", Integer.valueOf(dw.b(this.f)));
        this.g = String.format("D_%d", Integer.valueOf(dw.b(this.g)));
    }

    public static cz a(Context context) {
        if (context != null && c == null) {
            synchronized (b) {
                if (c == null) {
                    c = new cz(context);
                }
            }
        }
        return c;
    }

    private void a(String str) {
        long j2;
        if (e(str)) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (str.length() != 24 || this.i == null) {
                return;
            }
            String a = this.i.a("UTDID");
            String a2 = this.i.a("EI");
            if (dw.a(a2)) {
                a2 = dv.a(this.a);
            }
            String a3 = this.i.a("SI");
            if (dw.a(a3)) {
                a3 = dv.b(this.a);
            }
            String a4 = this.i.a("DID");
            if (dw.a(a4)) {
                a4 = a2;
            }
            if (a == null || !a.equals(str)) {
                cx cxVar = new cx();
                cxVar.a(a2);
                cxVar.b(a3);
                cxVar.d(str);
                cxVar.c(a4);
                cxVar.b(System.currentTimeMillis());
                this.i.a("UTDID", str);
                this.i.a("EI", cxVar.b());
                this.i.a("SI", cxVar.c());
                this.i.a("DID", cxVar.d());
                this.i.a("timestamp", cxVar.a());
                dz dzVar = this.i;
                String format = String.format("%s%s%s%s%s", cxVar.e(), cxVar.d(), Long.valueOf(cxVar.a()), cxVar.c(), cxVar.b());
                if (dw.a(format)) {
                    j2 = 0;
                } else {
                    Adler32 adler32 = new Adler32();
                    adler32.reset();
                    adler32.update(format.getBytes());
                    j2 = adler32.getValue();
                }
                dzVar.a("S", j2);
                this.i.a();
            }
        }
    }

    private void b(String str) {
        if (str == null || this.h == null || str.equals(this.h.a(this.f))) {
            return;
        }
        this.h.a(this.f, str);
        this.h.a();
    }

    private final byte[] b() throws Exception {
        String sb;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        int nextInt = new Random().nextInt();
        byte[] a = du.a(currentTimeMillis);
        byte[] a2 = du.a(nextInt);
        byteArrayOutputStream.write(a, 0, 4);
        byteArrayOutputStream.write(a2, 0, 4);
        byteArrayOutputStream.write(3);
        byteArrayOutputStream.write(0);
        try {
            sb = dv.a(this.a);
        } catch (Exception unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(new Random().nextInt());
            sb = sb2.toString();
        }
        byteArrayOutputStream.write(du.a(dw.b(sb)), 0, 4);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec("d6fc3a4a06adbde89223bvefedc24fecde188aaa9161".getBytes(), mac.getAlgorithm()));
        byteArrayOutputStream.write(du.a(dw.b(dt.a(mac.doFinal(byteArray), 2))));
        return byteArrayOutputStream.toByteArray();
    }

    private void c(String str) {
        if (this.a.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") == 0 && e(str)) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (24 != str.length() || e(Settings.System.getString(this.a.getContentResolver(), "mqBRboGZkQPcAkyk"))) {
                return;
            }
            Settings.System.putString(this.a.getContentResolver(), "mqBRboGZkQPcAkyk", str);
        }
    }

    private void d(String str) {
        if (this.a.checkCallingOrSelfPermission("android.permission.WRITE_SETTINGS") != 0 || str == null || str.equals(Settings.System.getString(this.a.getContentResolver(), "dxCRMxhQkdGePGnp"))) {
            return;
        }
        Settings.System.putString(this.a.getContentResolver(), "dxCRMxhQkdGePGnp", str);
    }

    private boolean e(String str) {
        if (str != null) {
            if (str.endsWith("\n")) {
                str = str.substring(0, str.length() - 1);
            }
            if (24 == str.length() && !this.k.matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x00aa A[Catch: all -> 0x012b, TryCatch #1 {, blocks: (B:3:0x0001, B:5:0x0005, B:9:0x0009, B:13:0x001d, B:15:0x0035, B:17:0x003f, B:20:0x0044, B:22:0x004e, B:24:0x005a, B:25:0x0069, B:27:0x0075, B:30:0x0087, B:32:0x008c, B:34:0x009a, B:37:0x00a4, B:39:0x00aa, B:41:0x00b2, B:42:0x00b5, B:45:0x00bf, B:47:0x00cd, B:49:0x00d7, B:50:0x00dd, B:52:0x00e3, B:54:0x00ef, B:56:0x00f3, B:57:0x00f6, B:61:0x00ff, B:63:0x0105, B:66:0x011b, B:67:0x011e, B:68:0x0121, B:75:0x0126), top: B:2:0x0001, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00bf A[Catch: all -> 0x012b, TRY_ENTER, TryCatch #1 {, blocks: (B:3:0x0001, B:5:0x0005, B:9:0x0009, B:13:0x001d, B:15:0x0035, B:17:0x003f, B:20:0x0044, B:22:0x004e, B:24:0x005a, B:25:0x0069, B:27:0x0075, B:30:0x0087, B:32:0x008c, B:34:0x009a, B:37:0x00a4, B:39:0x00aa, B:41:0x00b2, B:42:0x00b5, B:45:0x00bf, B:47:0x00cd, B:49:0x00d7, B:50:0x00dd, B:52:0x00e3, B:54:0x00ef, B:56:0x00f3, B:57:0x00f6, B:61:0x00ff, B:63:0x0105, B:66:0x011b, B:67:0x011e, B:68:0x0121, B:75:0x0126), top: B:2:0x0001, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final synchronized java.lang.String a() {
        /*
            Method dump skipped, instructions count: 302
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.cz.a():java.lang.String");
    }
}
