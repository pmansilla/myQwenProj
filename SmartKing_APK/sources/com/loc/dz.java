package com.loc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import com.loc.dy;
import java.io.File;
import java.util.Map;

/* compiled from: PersistentConfiguration.java */
/* loaded from: classes.dex */
public final class dz {
    private String a;
    private String b;
    private boolean c;
    private boolean d;
    private boolean e;
    private SharedPreferences f;
    private dy g;
    private SharedPreferences.Editor h = null;
    private dy.a i = null;
    private Context j;
    private ea k;
    private boolean l;

    /* JADX WARN: Removed duplicated region for block: B:16:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x013c A[Catch: Exception -> 0x014a, TRY_LEAVE, TryCatch #1 {Exception -> 0x014a, blocks: (B:38:0x0138, B:40:0x013c), top: B:37:0x0138 }] */
    /* JADX WARN: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0082 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public dz(android.content.Context r11, java.lang.String r12, java.lang.String r13) {
        /*
            Method dump skipped, instructions count: 331
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.dz.<init>(android.content.Context, java.lang.String, java.lang.String):void");
    }

    private static void a(SharedPreferences sharedPreferences, dy dyVar) {
        if (sharedPreferences == null || dyVar == null) {
            return;
        }
        dy.a c = dyVar.c();
        c.a();
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                c.a(key, (String) value);
            } else if (value instanceof Integer) {
                c.a(key, ((Integer) value).intValue());
            } else if (value instanceof Long) {
                c.a(key, ((Long) value).longValue());
            } else if (value instanceof Float) {
                c.a(key, ((Float) value).floatValue());
            } else if (value instanceof Boolean) {
                c.a(key, ((Boolean) value).booleanValue());
            }
        }
        c.b();
    }

    private static void a(dy dyVar, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit;
        if (dyVar == null || sharedPreferences == null || (edit = sharedPreferences.edit()) == null) {
            return;
        }
        edit.clear();
        for (Map.Entry<String, ?> entry : dyVar.b().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                edit.putString(key, (String) value);
            } else if (value instanceof Integer) {
                edit.putInt(key, ((Integer) value).intValue());
            } else if (value instanceof Long) {
                edit.putLong(key, ((Long) value).longValue());
            } else if (value instanceof Float) {
                edit.putFloat(key, ((Float) value).floatValue());
            } else if (value instanceof Boolean) {
                edit.putBoolean(key, ((Boolean) value).booleanValue());
            }
        }
        edit.commit();
    }

    private ea b(String str) {
        File file;
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory != null) {
            file = new File(String.format("%s%s%s", externalStorageDirectory.getAbsolutePath(), File.separator, str));
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            file = null;
        }
        if (file == null) {
            return null;
        }
        this.k = new ea(file.getAbsolutePath());
        return this.k;
    }

    private boolean b() {
        if (this.g == null) {
            return false;
        }
        boolean a = this.g.a();
        if (!a) {
            a();
        }
        return a;
    }

    private void c() {
        if (this.h == null && this.f != null) {
            this.h = this.f.edit();
        }
        if (this.e && this.i == null && this.g != null) {
            this.i = this.g.c();
        }
        b();
    }

    public final String a(String str) {
        b();
        if (this.f != null) {
            String string = this.f.getString(str, "");
            if (!dw.a(string)) {
                return string;
            }
        }
        return this.g != null ? this.g.a(str, "") : "";
    }

    public final void a(String str, long j) {
        if (dw.a(str) || str.equals("t")) {
            return;
        }
        c();
        if (this.h != null) {
            this.h.putLong(str, j);
        }
        if (this.i != null) {
            this.i.a(str, j);
        }
    }

    public final void a(String str, String str2) {
        if (dw.a(str) || str.equals("t")) {
            return;
        }
        c();
        if (this.h != null) {
            this.h.putString(str, str2);
        }
        if (this.i != null) {
            this.i.a(str, str2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x003f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean a() {
        /*
            r5 = this;
            long r0 = java.lang.System.currentTimeMillis()
            android.content.SharedPreferences$Editor r2 = r5.h
            r3 = 0
            if (r2 == 0) goto L22
            boolean r2 = r5.l
            if (r2 != 0) goto L18
            android.content.SharedPreferences r2 = r5.f
            if (r2 == 0) goto L18
            android.content.SharedPreferences$Editor r2 = r5.h
            java.lang.String r4 = "t"
            r2.putLong(r4, r0)
        L18:
            android.content.SharedPreferences$Editor r0 = r5.h
            boolean r0 = r0.commit()
            if (r0 != 0) goto L22
            r0 = 0
            goto L23
        L22:
            r0 = 1
        L23:
            android.content.SharedPreferences r1 = r5.f
            if (r1 == 0) goto L35
            android.content.Context r1 = r5.j
            if (r1 == 0) goto L35
            android.content.Context r1 = r5.j
            java.lang.String r2 = r5.a
            android.content.SharedPreferences r1 = r1.getSharedPreferences(r2, r3)
            r5.f = r1
        L35:
            java.lang.String r1 = android.os.Environment.getExternalStorageState()
            boolean r2 = com.loc.dw.a(r1)
            if (r2 != 0) goto La6
            java.lang.String r2 = "mounted"
            boolean r2 = r1.equals(r2)
            if (r2 == 0) goto L84
            com.loc.dy r2 = r5.g
            if (r2 != 0) goto L77
            java.lang.String r2 = r5.b
            com.loc.ea r2 = r5.b(r2)
            if (r2 == 0) goto L84
            java.lang.String r3 = r5.a
            com.loc.dy r2 = r2.a(r3)
            r5.g = r2
            boolean r2 = r5.l
            if (r2 != 0) goto L67
            android.content.SharedPreferences r2 = r5.f
            com.loc.dy r3 = r5.g
            a(r2, r3)
            goto L6e
        L67:
            com.loc.dy r2 = r5.g
            android.content.SharedPreferences r3 = r5.f
            a(r2, r3)
        L6e:
            com.loc.dy r2 = r5.g
            com.loc.dy$a r2 = r2.c()
            r5.i = r2
            goto L84
        L77:
            com.loc.dy$a r2 = r5.i
            if (r2 == 0) goto L84
            com.loc.dy$a r2 = r5.i
            boolean r2 = r2.b()
            if (r2 != 0) goto L84
            r0 = 0
        L84:
            java.lang.String r2 = "mounted"
            boolean r2 = r1.equals(r2)
            if (r2 != 0) goto L98
            java.lang.String r2 = "mounted_ro"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto La6
            com.loc.dy r1 = r5.g
            if (r1 == 0) goto La6
        L98:
            com.loc.ea r1 = r5.k     // Catch: java.lang.Exception -> La6
            if (r1 == 0) goto La6
            com.loc.ea r1 = r5.k     // Catch: java.lang.Exception -> La6
            java.lang.String r2 = r5.a     // Catch: java.lang.Exception -> La6
            com.loc.dy r1 = r1.a(r2)     // Catch: java.lang.Exception -> La6
            r5.g = r1     // Catch: java.lang.Exception -> La6
        La6:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.dz.a():boolean");
    }
}
