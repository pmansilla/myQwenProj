package com.amap.api.mapcore.util;

import android.content.Context;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: OfflineDBOperation.java */
/* loaded from: classes.dex */
public class ce {
    private static volatile ce a;
    private static ig b;
    private Context c;

    private ce(Context context) {
        this.c = context;
        b = b(this.c);
    }

    public static ce a(Context context) {
        if (a == null) {
            synchronized (ce.class) {
                if (a == null) {
                    a = new ce(context);
                }
            }
        }
        return a;
    }

    private List<String> a(List<cb> list) {
        ArrayList arrayList = new ArrayList();
        if (list.size() > 0) {
            Iterator<cb> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().a());
            }
        }
        return arrayList;
    }

    private void a(String str, String str2) {
        if (str2 == null || str2.length() <= 0) {
            return;
        }
        String a2 = cb.a(str);
        if (b.b(a2, cb.class).size() > 0) {
            b.a(a2, cb.class);
        }
        String[] split = str2.split(";");
        ArrayList arrayList = new ArrayList();
        for (String str3 : split) {
            arrayList.add(new cb(str, str3));
        }
        b.a((List) arrayList);
    }

    private ig b(Context context) {
        try {
            return new ig(context, cd.a());
        } catch (Throwable th) {
            ic.c(th, "OfflineDB", "getDB");
            th.printStackTrace();
            return null;
        }
    }

    private boolean b() {
        if (b == null) {
            b = b(this.c);
        }
        return b != null;
    }

    public synchronized bz a(String str) {
        if (!b()) {
            return null;
        }
        List b2 = b.b(bz.e(str), bz.class);
        if (b2.size() <= 0) {
            return null;
        }
        return (bz) b2.get(0);
    }

    public ArrayList<bz> a() {
        ArrayList<bz> arrayList = new ArrayList<>();
        if (!b()) {
            return arrayList;
        }
        Iterator it = b.b("", bz.class).iterator();
        while (it.hasNext()) {
            arrayList.add((bz) it.next());
        }
        return arrayList;
    }

    public synchronized void a(bz bzVar) {
        if (b()) {
            b.a(bzVar, bz.f(bzVar.i()));
            a(bzVar.f(), bzVar.b());
        }
    }

    public void a(String str, int i, long j, long j2, long j3) {
        if (b()) {
            a(str, i, j, new long[]{j2, 0, 0, 0, 0}, new long[]{j3, 0, 0, 0, 0});
        }
    }

    public synchronized void a(String str, int i, long j, long[] jArr, long[] jArr2) {
        if (b()) {
            b.a(new ca(str, j, i, jArr[0], jArr2[0]), ca.a(str));
        }
    }

    public synchronized List<String> b(String str) {
        ArrayList arrayList = new ArrayList();
        if (!b()) {
            return arrayList;
        }
        arrayList.addAll(a(b.b(cb.a(str), cb.class)));
        return arrayList;
    }

    public synchronized void b(bz bzVar) {
        if (b()) {
            b.a(cc.f(bzVar.i()), cc.class);
            b.a(cb.a(bzVar.f()), cb.class);
            b.a(ca.a(bzVar.f()), ca.class);
        }
    }

    public synchronized void c(String str) {
        if (b()) {
            b.a(cc.e(str), cc.class);
            b.a(cb.a(str), cb.class);
            b.a(ca.a(str), ca.class);
        }
    }

    public synchronized String d(String str) {
        if (!b()) {
            return null;
        }
        List b2 = b.b(cc.f(str), cc.class);
        return b2.size() > 0 ? ((cc) b2.get(0)).e() : null;
    }
}
