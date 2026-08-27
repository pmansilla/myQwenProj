package com.amap.openapi;

import android.content.Context;
import android.os.Handler;
import com.amap.openapi.df;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: WifiScanListener.java */
/* loaded from: classes.dex */
public class dh implements df.a {
    private final List<a> a = new ArrayList();
    private di b;

    /* compiled from: WifiScanListener.java */
    /* loaded from: classes.dex */
    private static class a {
        private Handler a;

        public void a() {
            this.a.sendEmptyMessage(0);
        }
    }

    public dh(Context context, di diVar) {
        this.b = diVar;
        this.b.a(context, this);
    }

    @Override // com.amap.openapi.df.a
    public void a() {
        synchronized (this.a) {
            Iterator<a> it = this.a.iterator();
            while (it.hasNext()) {
                it.next().a();
            }
        }
    }
}
