package com.loc;

import android.content.Context;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class dc implements dd {
    private boolean b = false;
    private static dg c = dg.a();
    private static cv d = null;
    static dc a = null;

    private dc() {
    }

    public static dd a(Context context, String str) {
        if (a == null) {
            synchronized (dc.class) {
                if (a == null) {
                    if (!dl.a()) {
                        Thread thread = new Thread(new di(context));
                        thread.setUncaughtExceptionHandler(new dm());
                        thread.start();
                    }
                    Cdo.a(context);
                    dq.a(context);
                    dj.a(str);
                    a = new dc();
                }
            }
        }
        return a;
    }

    @Override // com.loc.dd
    public final String a(String str) {
        String[] b = b(str);
        if (b.length > 0) {
            return b[0];
        }
        return null;
    }

    @Override // com.loc.dd
    public final void a() {
        this.b = true;
    }

    @Override // com.loc.dd
    public final void a(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            String str = (String) arrayList.get(i);
            if (!dg.b(str)) {
                de.a().submit(new dq(str));
            }
        }
    }

    @Override // com.loc.dd
    public final String[] b(String str) {
        if (!dn.a(str)) {
            return dj.b;
        }
        if (dn.b(str)) {
            return new String[]{str};
        }
        if (d != null && d.a()) {
            return dj.b;
        }
        dh a2 = dg.a(str);
        if ((a2 == null || a2.b()) && !dg.b(str)) {
            dk.a("refresh host async: " + str);
            de.a().submit(new dq(str));
        }
        return (a2 == null || (a2.b() && !(a2.b() && this.b))) ? dj.b : a2.a();
    }
}
