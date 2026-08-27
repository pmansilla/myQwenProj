package com.loc;

import android.content.Context;
import java.util.Iterator;
import java.util.List;

/* compiled from: SDKDBOperation.java */
/* loaded from: classes.dex */
public final class ba {
    private av a;
    private Context b;

    public ba(Context context, boolean z) {
        this.b = context;
        this.a = a(this.b, z);
    }

    private static av a(Context context, boolean z) {
        try {
            return new av(context, av.a((Class<? extends au>) az.class));
        } catch (Throwable th) {
            if (!z) {
                aq.b(th, "sd", "gdb");
            }
            return null;
        }
    }

    public final List<ac> a() {
        try {
            return this.a.a(ac.g(), ac.class, true);
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public final void a(ac acVar) {
        if (acVar == null) {
            return;
        }
        try {
            boolean z = false;
            if (this.a == null) {
                this.a = a(this.b, false);
            }
            String a = ac.a(acVar.a());
            List a2 = this.a.a(a, ac.class, false);
            if (a2.size() == 0) {
                this.a.a((av) acVar);
                return;
            }
            Iterator it = a2.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = true;
                    break;
                } else if (((ac) it.next()).equals(acVar)) {
                    break;
                }
            }
            if (z) {
                this.a.a(a, acVar);
            }
        } catch (Throwable th) {
            aq.b(th, "sd", "it");
        }
    }
}
