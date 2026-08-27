package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.Iterator;
import java.util.List;

/* compiled from: SDKDBOperation.java */
/* loaded from: classes.dex */
public class il {
    private ig a;
    private Context b;

    public il(Context context, boolean z) {
        this.b = context;
        this.a = a(this.b, z);
    }

    private ig a(Context context, boolean z) {
        try {
            return new ig(context, ig.a((Class<? extends Cif>) ik.class));
        } catch (Throwable th) {
            if (!z) {
                ic.c(th, "sd", "gdb");
            }
            return null;
        }
    }

    private boolean a(List<ho> list, ho hoVar) {
        Iterator<ho> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().equals(hoVar)) {
                return false;
            }
        }
        return true;
    }

    public List<ho> a() {
        try {
            return this.a.a(ho.h(), ho.class, true);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public void a(ho hoVar) {
        if (hoVar == null) {
            return;
        }
        try {
            if (this.a == null) {
                this.a = a(this.b, false);
            }
            String a = ho.a(hoVar.a());
            List<ho> b = this.a.b(a, ho.class);
            if (b != null && b.size() != 0) {
                if (a(b, hoVar)) {
                    this.a.a(a, hoVar);
                    return;
                }
                return;
            }
            this.a.a((ig) hoVar);
        } catch (Throwable th) {
            ic.c(th, "sd", "it");
        }
    }
}
