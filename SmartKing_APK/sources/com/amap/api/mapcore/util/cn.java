package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.mapcore.util.he;
import com.amap.api.maps.AMapException;
import java.util.Map;
import org.json.JSONObject;

/* compiled from: AbstractProtocalHandler.java */
/* loaded from: classes.dex */
public abstract class cn<T, V> {
    protected T a;
    protected int b = 3;
    protected Context c;

    public cn(Context context, T t) {
        a(context, t);
    }

    private void a(Context context, T t) {
        this.c = context;
        this.a = t;
    }

    protected abstract String a();

    protected abstract JSONObject a(he.a aVar);

    protected abstract V b(JSONObject jSONObject) throws AMapException;

    protected abstract Map<String, String> b();

    public V c() throws AMapException {
        if (this.a != null) {
            return d();
        }
        return null;
    }

    protected V d() throws AMapException {
        he.a a;
        he.a aVar;
        V b;
        int i = 0;
        V v = null;
        he.a aVar2 = null;
        while (i < this.b) {
            try {
                a = he.a(this.c, fr.e(), a(), b());
                try {
                    b = b(a(a));
                } catch (Throwable th) {
                    aVar = a;
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                i = this.b;
                v = b;
                aVar2 = a;
            } catch (Throwable th3) {
                aVar = a;
                th = th3;
                v = b;
                aVar2 = aVar;
                ic.c(th, "AbstractProtocalHandler", "getDataMayThrow AMapException");
                th.printStackTrace();
                i++;
                if (i < this.b) {
                    continue;
                } else {
                    if (aVar2 != null && aVar2.a != null) {
                        throw new AMapException(aVar2.a);
                    }
                    v = null;
                }
            }
        }
        return v;
    }
}
