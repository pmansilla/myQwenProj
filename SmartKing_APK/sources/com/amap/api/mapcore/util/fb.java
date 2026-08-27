package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import com.amap.api.maps.model.LatLng;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Iterator;

/* compiled from: InfoCollectUtils.java */
/* loaded from: classes.dex */
public class fb {
    private static boolean a = false;
    private static volatile fb d;
    private Hashtable<String, String> b = new Hashtable<>();
    private WeakReference<Context> c = null;

    private fb() {
    }

    public static fb a() {
        if (d == null) {
            synchronized (fb.class) {
                if (d == null) {
                    d = new fb();
                }
            }
        }
        return d;
    }

    public static void a(int i) {
        if (a) {
            a(i < 1000);
        }
    }

    private void a(String str) {
        if (str == null || this.b == null) {
            return;
        }
        synchronized (this.b) {
            String b = hl.b(str);
            if (this.b != null && !this.b.contains(b)) {
                this.b.put(b, str);
            }
            if (d()) {
                c();
            }
        }
    }

    public static void a(boolean z) {
        a = z;
    }

    public static void b() {
        if (d != null) {
            if (d.b != null && d.b.size() > 0) {
                synchronized (d.b) {
                    d.c();
                    if (d.c != null) {
                        d.c.clear();
                    }
                }
            }
            d = null;
        }
        a(false);
    }

    private void c() {
        if (!a) {
            this.b.clear();
            return;
        }
        if (this.b != null) {
            StringBuffer stringBuffer = new StringBuffer();
            int i = 0;
            int size = this.b.size();
            if (size > 0) {
                stringBuffer.append("[");
                Iterator<String> it = this.b.values().iterator();
                while (it.hasNext()) {
                    i++;
                    stringBuffer.append(it.next());
                    if (i < size) {
                        stringBuffer.append(",");
                    }
                }
                stringBuffer.append("]");
                String stringBuffer2 = stringBuffer.toString();
                if (!TextUtils.isEmpty(stringBuffer2) && this.c != null && this.c.get() != null) {
                    je.a(stringBuffer2, this.c.get());
                }
            }
            this.b.clear();
        }
    }

    private boolean d() {
        return this.b != null && this.b.size() > 20;
    }

    public void a(Context context) {
        if (context != null) {
            this.c = new WeakReference<>(context);
        }
    }

    public void a(LatLng latLng, String str, String str2) {
        if (!a) {
            this.b.clear();
            return;
        }
        if (latLng == null || TextUtils.isEmpty(str)) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{");
        stringBuffer.append("\"lon\":");
        stringBuffer.append(latLng.longitude);
        stringBuffer.append(",");
        stringBuffer.append("\"lat\":");
        stringBuffer.append(latLng.latitude);
        stringBuffer.append(",");
        stringBuffer.append("\"title\":");
        stringBuffer.append("\"");
        stringBuffer.append(str);
        stringBuffer.append("\"");
        stringBuffer.append(",");
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        stringBuffer.append("\"snippet\":");
        stringBuffer.append("\"");
        stringBuffer.append(str2);
        stringBuffer.append("\"");
        stringBuffer.append("}");
        a(stringBuffer.toString());
    }
}
