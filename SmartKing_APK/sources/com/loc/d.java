package com.loc;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.List;

/* compiled from: AdiuStorageModel.java */
/* loaded from: classes.dex */
public final class d {
    private static d e;
    private List<String> a;
    private String b;
    private final Context c;
    private final Handler d;

    /* compiled from: AdiuStorageModel.java */
    /* loaded from: classes.dex */
    private static final class a extends Handler {
        private final WeakReference<d> a;

        a(Looper looper, d dVar) {
            super(looper);
            this.a = new WeakReference<>(dVar);
        }

        a(d dVar) {
            this.a = new WeakReference<>(dVar);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            d dVar = this.a.get();
            if (dVar == null || message == null || message.obj == null) {
                return;
            }
            dVar.a((String) message.obj, message.what);
        }
    }

    private d(Context context) {
        this.c = context.getApplicationContext();
        this.d = Looper.myLooper() == null ? new a(Looper.getMainLooper(), this) : new a(this);
    }

    public static d a(Context context) {
        if (e == null) {
            synchronized (d.class) {
                if (e == null) {
                    e = new d(context);
                }
            }
        }
        return e;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v14, types: [com.loc.d$1] */
    public synchronized void a(final String str, final int i) {
        ContentResolver contentResolver;
        String str2;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread() { // from class: com.loc.d.1
                @Override // java.lang.Thread, java.lang.Runnable
                public final void run() {
                    ContentResolver contentResolver2;
                    String str3;
                    String b = i.b(str);
                    if (TextUtils.isEmpty(b)) {
                        return;
                    }
                    if ((i & 1) > 0) {
                        try {
                            if (Build.VERSION.SDK_INT >= 23) {
                                contentResolver2 = d.this.c.getContentResolver();
                                str3 = d.this.b;
                            } else {
                                contentResolver2 = d.this.c.getContentResolver();
                                str3 = d.this.b;
                            }
                            Settings.System.putString(contentResolver2, str3, b);
                        } catch (Exception unused) {
                        }
                    }
                    if ((i & 16) > 0) {
                        e.a(d.this.c, d.this.b, b);
                    }
                    if ((i & 256) > 0) {
                        SharedPreferences.Editor edit = d.this.c.getSharedPreferences("SharedPreferenceAdiu", 0).edit();
                        edit.putString(d.this.b, b);
                        if (Build.VERSION.SDK_INT >= 9) {
                            edit.apply();
                        } else {
                            edit.commit();
                        }
                    }
                }
            }.start();
            return;
        }
        String b = i.b(str);
        if (!TextUtils.isEmpty(b)) {
            if ((i & 1) > 0) {
                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        contentResolver = this.c.getContentResolver();
                        str2 = this.b;
                    } else {
                        contentResolver = this.c.getContentResolver();
                        str2 = this.b;
                    }
                    Settings.System.putString(contentResolver, str2, b);
                } catch (Exception unused) {
                }
            }
            if ((i & 16) > 0) {
                e.a(this.c, this.b, b);
            }
            if ((i & 256) > 0) {
                SharedPreferences.Editor edit = this.c.getSharedPreferences("SharedPreferenceAdiu", 0).edit();
                edit.putString(this.b, b);
                if (Build.VERSION.SDK_INT >= 9) {
                    edit.apply();
                    return;
                }
                edit.commit();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0082  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.List<java.lang.String> b() {
        /*
            r7 = this;
            java.lang.String r0 = ""
            android.content.Context r1 = r7.c     // Catch: java.lang.Exception -> L19
            android.content.ContentResolver r1 = r1.getContentResolver()     // Catch: java.lang.Exception -> L19
            java.lang.String r2 = r7.b     // Catch: java.lang.Exception -> L19
            java.lang.String r1 = android.provider.Settings.System.getString(r1, r2)     // Catch: java.lang.Exception -> L19
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Exception -> L19
            if (r2 != 0) goto L19
            java.lang.String r1 = com.loc.i.c(r1)     // Catch: java.lang.Exception -> L19
            r0 = r1
        L19:
            java.lang.String r1 = ""
            android.content.Context r2 = r7.c
            java.lang.String r3 = r7.b
            java.lang.String r2 = com.loc.e.a(r2, r3)
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L2d
            java.lang.String r1 = com.loc.i.c(r2)
        L2d:
            java.lang.String r2 = ""
            android.content.Context r3 = r7.c
            java.lang.String r4 = "SharedPreferenceAdiu"
            r5 = 0
            android.content.SharedPreferences r3 = r3.getSharedPreferences(r4, r5)
            java.lang.String r4 = r7.b
            r6 = 0
            java.lang.String r3 = r3.getString(r4, r6)
            boolean r4 = android.text.TextUtils.isEmpty(r3)
            if (r4 != 0) goto L49
            java.lang.String r2 = com.loc.i.c(r3)
        L49:
            java.util.ArrayList r3 = new java.util.ArrayList
            r4 = 3
            r3.<init>(r4)
            boolean r4 = android.text.TextUtils.isEmpty(r0)
            if (r4 != 0) goto L8e
            r3.add(r0)
            boolean r4 = android.text.TextUtils.isEmpty(r1)
            if (r4 != 0) goto L67
            boolean r4 = android.text.TextUtils.equals(r1, r0)
            if (r4 != 0) goto L69
            r3.add(r1)
        L67:
            r5 = 16
        L69:
            boolean r4 = android.text.TextUtils.isEmpty(r2)
            if (r4 != 0) goto L7e
            boolean r4 = android.text.TextUtils.equals(r2, r0)
            if (r4 != 0) goto L80
            boolean r1 = android.text.TextUtils.equals(r2, r1)
            if (r1 != 0) goto L7e
            r3.add(r2)
        L7e:
            r5 = r5 | 256(0x100, float:3.59E-43)
        L80:
            if (r5 <= 0) goto L8d
            android.os.Handler r1 = r7.d
            android.os.Handler r2 = r7.d
            android.os.Message r0 = r2.obtainMessage(r5, r0)
            r1.sendMessage(r0)
        L8d:
            return r3
        L8e:
            boolean r0 = android.text.TextUtils.isEmpty(r1)
            if (r0 != 0) goto Lb6
            r3.add(r1)
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 != 0) goto La6
            boolean r0 = android.text.TextUtils.equals(r2, r1)
            if (r0 != 0) goto La8
            r3.add(r2)
        La6:
            r5 = 256(0x100, float:3.59E-43)
        La8:
            r0 = r5 | 1
            android.os.Handler r2 = r7.d
            android.os.Handler r4 = r7.d
            android.os.Message r0 = r4.obtainMessage(r0, r1)
            r2.sendMessage(r0)
            return r3
        Lb6:
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 != 0) goto Lcd
            r3.add(r2)
            android.os.Handler r0 = r7.d
            android.os.Handler r1 = r7.d
            r4 = 17
            android.os.Message r1 = r1.obtainMessage(r4, r2)
            r0.sendMessage(r1)
            return r3
        Lcd:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.d.b():java.util.List");
    }

    public final List<String> a() {
        if (this.a != null && this.a.size() > 0 && !TextUtils.isEmpty(this.a.get(0))) {
            return this.a;
        }
        this.a = b();
        return this.a;
    }

    public final void a(String str) {
        this.b = str;
    }

    public final void b(String str) {
        if (this.a != null) {
            this.a.clear();
            this.a.add(str);
        }
        a(str, 273);
    }
}
