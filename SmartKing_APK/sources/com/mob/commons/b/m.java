package com.mob.commons.b;

import android.content.Context;
import com.mob.commons.b.f;
import java.lang.reflect.Method;

/* compiled from: Xiaomi.java */
/* loaded from: classes.dex */
public class m extends f {
    public m(Context context) {
        super(context);
    }

    private String a(Context context, Object obj, Method method) {
        if (obj == null || method == null) {
            return null;
        }
        try {
            Object invoke = method.invoke(obj, context);
            if (invoke != null) {
                return (String) invoke;
            }
            return null;
        } catch (Throwable th) {
            c.a().a(th);
            return null;
        }
    }

    @Override // com.mob.commons.b.f
    protected f.c c() {
        Class<?> cls;
        Object obj;
        Method method;
        Method method2;
        Method method3;
        Method method4 = null;
        try {
            cls = Class.forName(com.mob.commons.k.a(98));
            try {
                obj = cls.newInstance();
            } catch (Throwable th) {
                th = th;
                c.a().a(th);
                obj = null;
                if (cls != null) {
                }
                method = null;
                method2 = null;
                method3 = null;
                f.c cVar = new f.c();
                cVar.b = a(this.a, obj, method2);
                cVar.e = a(this.a, obj, method3);
                cVar.c = a(this.a, obj, method4);
                cVar.d = a(this.a, obj, method);
                cVar.a = cls == null && obj != null;
                return cVar;
            }
        } catch (Throwable th2) {
            th = th2;
            cls = null;
        }
        if (cls != null || obj == null) {
            method = null;
            method2 = null;
            method3 = null;
        } else {
            try {
                method = cls.getMethod(com.mob.commons.k.a(99), Context.class);
            } catch (Throwable th3) {
                c.a().a(th3);
                method = null;
            }
            try {
                method2 = cls.getMethod(com.mob.commons.k.a(100), Context.class);
            } catch (Throwable th4) {
                c.a().a(th4);
                method2 = null;
            }
            try {
                method3 = cls.getMethod(com.mob.commons.k.a(101), Context.class);
            } catch (Throwable th5) {
                c.a().a(th5);
                method3 = null;
            }
            try {
                method4 = cls.getMethod(com.mob.commons.k.a(102), Context.class);
            } catch (Throwable th6) {
                c.a().a(th6);
            }
        }
        f.c cVar2 = new f.c();
        cVar2.b = a(this.a, obj, method2);
        cVar2.e = a(this.a, obj, method3);
        cVar2.c = a(this.a, obj, method4);
        cVar2.d = a(this.a, obj, method);
        cVar2.a = cls == null && obj != null;
        return cVar2;
    }
}
