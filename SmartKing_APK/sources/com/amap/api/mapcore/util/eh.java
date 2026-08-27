package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.mapcore.util.eg;
import com.amap.api.maps.MapsInitializer;

/* compiled from: CustomStyleTask.java */
/* loaded from: classes.dex */
public class eh implements Runnable {
    private Context a;
    private ad b;
    private eg c;
    private a d;
    private int e;

    /* compiled from: CustomStyleTask.java */
    /* loaded from: classes.dex */
    public interface a {
        void a(byte[] bArr, int i);
    }

    public eh(Context context, ad adVar) {
        this.e = 0;
        this.a = context;
        this.b = adVar;
        if (this.c == null) {
            this.c = new eg(this.a, "");
        }
    }

    public eh(Context context, a aVar, int i) {
        this.e = 0;
        this.a = context;
        this.d = aVar;
        this.e = i;
        if (this.c == null) {
            this.c = new eg(this.a, "", i == 1);
        }
    }

    public void a() {
        this.a = null;
        if (this.c != null) {
            this.c = null;
        }
    }

    public void a(String str) {
        if (this.c != null) {
            this.c.c(str);
        }
    }

    public void b() {
        fq.a().a(this);
    }

    @Override // java.lang.Runnable
    public void run() {
        eg.a a2;
        try {
            if (MapsInitializer.getNetWorkEnable()) {
                if (this.c != null && (a2 = this.c.a()) != null && a2.a != null) {
                    if (this.d != null) {
                        this.d.a(a2.a, this.e);
                    } else if (this.b != null) {
                        this.b.a(this.b.getMapConfig().isCustomStyleEnable(), a2.a);
                    }
                }
                ic.a(this.a, fr.e());
                if (this.b != null) {
                    this.b.setRunLowFrame(false);
                }
            }
        } catch (Throwable th) {
            ic.c(th, "CustomStyleTask", "download customStyle");
            th.printStackTrace();
        }
    }
}
