package com.amap.api.mapcore.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
        public void handleMessage(Message message) {
            d dVar = this.a.get();
            if (dVar == null || message == null || message.obj == null) {
                return;
            }
            dVar.a((String) message.obj, message.what);
        }
    }

    private d(Context context) {
        this.c = context.getApplicationContext();
        if (Looper.myLooper() == null) {
            this.d = new a(Looper.getMainLooper(), this);
        } else {
            this.d = new a(this);
        }
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
    /* JADX WARN: Type inference failed for: r0v13, types: [com.amap.api.mapcore.util.d$1] */
    public synchronized void a(final String str, final int i) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread() { // from class: com.amap.api.mapcore.util.d.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    String b = i.b(str);
                    if (TextUtils.isEmpty(b)) {
                        return;
                    }
                    if ((i & 1) > 0) {
                        try {
                            if (Build.VERSION.SDK_INT >= 23) {
                                Settings.System.putString(d.this.c.getContentResolver(), d.this.b, b);
                            } else {
                                Settings.System.putString(d.this.c.getContentResolver(), d.this.b, b);
                            }
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
        } else {
            String b = i.b(str);
            if (!TextUtils.isEmpty(b)) {
                if ((i & 1) > 0) {
                    try {
                        if (Build.VERSION.SDK_INT >= 23) {
                            Settings.System.putString(this.c.getContentResolver(), this.b, b);
                        } else {
                            Settings.System.putString(this.c.getContentResolver(), this.b, b);
                        }
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
                    } else {
                        edit.commit();
                    }
                }
            }
        }
    }

    private List<String> b() {
        String str = "";
        try {
            String string = Settings.System.getString(this.c.getContentResolver(), this.b);
            if (!TextUtils.isEmpty(string)) {
                str = i.c(string);
            }
        } catch (Exception unused) {
        }
        String a2 = e.a(this.c, this.b);
        String c = TextUtils.isEmpty(a2) ? "" : i.c(a2);
        String string2 = this.c.getSharedPreferences("SharedPreferenceAdiu", 0).getString(this.b, null);
        String c2 = TextUtils.isEmpty(string2) ? "" : i.c(string2);
        ArrayList arrayList = new ArrayList(3);
        if (TextUtils.isEmpty(str)) {
            if (TextUtils.isEmpty(c)) {
                if (TextUtils.isEmpty(c2)) {
                    return null;
                }
                arrayList.add(c2);
                this.d.sendMessage(this.d.obtainMessage(17, c2));
                return arrayList;
            }
            arrayList.add(c);
            int i = 256;
            if (!TextUtils.isEmpty(c2)) {
                if (TextUtils.equals(c2, c)) {
                    i = 0;
                } else {
                    arrayList.add(c2);
                }
            }
            this.d.sendMessage(this.d.obtainMessage(i | 1, c));
            return arrayList;
        }
        arrayList.add(str);
        int i2 = 16;
        if (!TextUtils.isEmpty(c)) {
            if (TextUtils.equals(c, str)) {
                i2 = 0;
            } else {
                arrayList.add(c);
            }
        }
        if (TextUtils.isEmpty(c2)) {
            i2 |= 256;
        } else if (!TextUtils.equals(c2, str)) {
            if (!TextUtils.equals(c2, c)) {
                arrayList.add(c2);
            }
            i2 |= 256;
        }
        if (i2 > 0) {
            this.d.sendMessage(this.d.obtainMessage(i2, str));
        }
        return arrayList;
    }

    public List<String> a() {
        if (this.a != null && this.a.size() > 0 && !TextUtils.isEmpty(this.a.get(0))) {
            return this.a;
        }
        this.a = b();
        return this.a;
    }

    public void a(String str) {
        this.b = str;
    }

    public void b(String str) {
        if (this.a != null) {
            this.a.clear();
            this.a.add(str);
        }
        a(str, 273);
    }
}
