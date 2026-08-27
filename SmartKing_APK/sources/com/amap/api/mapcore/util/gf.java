package com.amap.api.mapcore.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/* compiled from: MethodCallHelper.java */
/* loaded from: classes.dex */
public class gf {
    private boolean b = false;
    ArrayList<a> a = new ArrayList<>();

    /* compiled from: MethodCallHelper.java */
    /* loaded from: classes.dex */
    public static class a {
        private String a;
        private Object b;
        private Class<?>[] c;
        private Object[] d;

        public a(Object obj, String str, Object... objArr) {
            this.b = obj;
            this.a = str;
            if (objArr == null || objArr.length <= 0) {
                return;
            }
            this.c = new Class[objArr.length];
            for (int i = 0; i < objArr.length; i++) {
                this.c[i] = objArr[i].getClass();
            }
            this.d = new Object[objArr.length];
            for (int i2 = 0; i2 < objArr.length; i2++) {
                this.d[i2] = objArr[i2];
            }
        }
    }

    public synchronized void a() {
        Class<?> cls;
        if (this.b) {
            return;
        }
        this.b = true;
        for (int i = 0; i < this.a.size(); i++) {
            a aVar = this.a.get(i);
            try {
                try {
                    try {
                        if (aVar.b != null && (cls = aVar.b.getClass()) != null) {
                            Method method = null;
                            try {
                                method = cls.getDeclaredMethod(aVar.a, aVar.c);
                            } catch (NoSuchMethodException unused) {
                                if (aVar.c.length > 0) {
                                    Class<?>[] clsArr = new Class[aVar.c.length];
                                    for (int i2 = 0; i2 < aVar.c.length; i2++) {
                                        if (aVar.c[i2].getInterfaces().length > 0) {
                                            clsArr[i2] = aVar.c[i2].getInterfaces()[0];
                                        }
                                    }
                                    method = cls.getDeclaredMethod(aVar.a, clsArr);
                                }
                            }
                            if (method != null) {
                                method.setAccessible(true);
                                method.invoke(aVar.b, aVar.d);
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                } catch (IllegalArgumentException e3) {
                    e3.printStackTrace();
                }
            } catch (SecurityException e4) {
                e4.printStackTrace();
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
            }
        }
        this.a.clear();
    }

    public synchronized void a(Object obj, Object... objArr) {
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace != null && stackTrace.length >= 3) {
                this.a.add(new a(obj, stackTrace[3].getMethodName(), objArr));
            }
        } catch (Throwable unused) {
        }
        this.b = false;
    }
}
