package com.loc;

import android.content.Context;
import android.text.TextUtils;
import java.lang.ref.WeakReference;

/* compiled from: MarkInfoManager.java */
/* loaded from: classes.dex */
public class bn {
    static WeakReference<bl> a;

    public static void a(final Context context) {
        aq.d().submit(new Runnable() { // from class: com.loc.bn.1
            @Override // java.lang.Runnable
            public final void run() {
                synchronized (bn.class) {
                    bl a2 = bs.a(bn.a);
                    bs.a(context, a2, ao.j, 50, 102400, "10");
                    if (a2.g == null) {
                        a2.g = new bw(new bv(context, new ca(), new ag(new ak(new ai())), "WImFwcG5hbWUiOiIlcyIsInBrZyI6IiVzIiwiZGl1IjoiJXMi", u.b(context), u.c(context), bn.b(context)));
                    }
                    a2.h = 14400000;
                    if (TextUtils.isEmpty(a2.i)) {
                        a2.i = "eKey";
                    }
                    if (a2.f == null) {
                        a2.f = new ce(context, a2.h, a2.i, new cb(5, a2.a, new cg(context)));
                    }
                    bm.a(a2);
                }
            }
        });
    }

    static /* synthetic */ String b(Context context) {
        String u = x.u(context);
        if (!TextUtils.isEmpty(u)) {
            return u;
        }
        String g = x.g(context);
        if (!TextUtils.isEmpty(g)) {
            return g;
        }
        String l = x.l(context);
        return !TextUtils.isEmpty(l) ? l : x.a(context);
    }
}
