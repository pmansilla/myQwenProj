package com.amap.api.mapcore.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;

/* compiled from: PluginContext.java */
/* loaded from: classes.dex */
public class gs extends ContextThemeWrapper {
    private static final String[] d = {"android.widget", "android.webkit", "android.app"};
    private Resources a;
    private LayoutInflater b;
    private ClassLoader c;
    private a e;
    private LayoutInflater.Factory f;

    /* compiled from: PluginContext.java */
    /* loaded from: classes.dex */
    public class a {
        public HashSet<String> a = new HashSet<>();
        public HashMap<String, Constructor<?>> b = new HashMap<>();

        public a() {
        }
    }

    public gs(Context context, int i, ClassLoader classLoader) {
        super(context, i);
        this.e = new a();
        this.f = new LayoutInflater.Factory() { // from class: com.amap.api.mapcore.util.gs.1
            @Override // android.view.LayoutInflater.Factory
            public View onCreateView(String str, Context context2, AttributeSet attributeSet) {
                return gs.this.a(str, context2, attributeSet);
            }
        };
        this.a = gt.a();
        this.c = classLoader;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0073 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.view.View a(java.lang.String r12, android.content.Context r13, android.util.AttributeSet r14) {
        /*
            r11 = this;
            com.amap.api.mapcore.util.gs$a r0 = r11.e
            java.util.HashSet<java.lang.String> r0 = r0.a
            boolean r0 = r0.contains(r12)
            r1 = 0
            if (r0 == 0) goto Lc
            return r1
        Lc:
            com.amap.api.mapcore.util.gs$a r0 = r11.e
            java.util.HashMap<java.lang.String, java.lang.reflect.Constructor<?>> r0 = r0.b
            java.lang.Object r0 = r0.get(r12)
            java.lang.reflect.Constructor r0 = (java.lang.reflect.Constructor) r0
            r2 = 2
            r3 = 1
            r4 = 0
            if (r0 != 0) goto L8b
            java.lang.String r5 = "api.navi"
            boolean r5 = r12.contains(r5)     // Catch: java.lang.Throwable -> L66
            if (r5 == 0) goto L2a
            java.lang.ClassLoader r5 = r11.c     // Catch: java.lang.Throwable -> L66
            java.lang.Class r5 = r5.loadClass(r12)     // Catch: java.lang.Throwable -> L66
            goto L52
        L2a:
            java.lang.String[] r5 = com.amap.api.mapcore.util.gs.d     // Catch: java.lang.Throwable -> L66
            int r6 = r5.length     // Catch: java.lang.Throwable -> L66
            r7 = 0
        L2e:
            if (r7 >= r6) goto L51
            r8 = r5[r7]     // Catch: java.lang.Throwable -> L66
            java.lang.ClassLoader r9 = r11.c     // Catch: java.lang.Throwable -> L4e
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4e
            r10.<init>()     // Catch: java.lang.Throwable -> L4e
            r10.append(r8)     // Catch: java.lang.Throwable -> L4e
            java.lang.String r8 = "."
            r10.append(r8)     // Catch: java.lang.Throwable -> L4e
            r10.append(r12)     // Catch: java.lang.Throwable -> L4e
            java.lang.String r8 = r10.toString()     // Catch: java.lang.Throwable -> L4e
            java.lang.Class r8 = r9.loadClass(r8)     // Catch: java.lang.Throwable -> L4e
            r5 = r8
            goto L52
        L4e:
            int r7 = r7 + 1
            goto L2e
        L51:
            r5 = r1
        L52:
            if (r5 != 0) goto L55
            goto L62
        L55:
            java.lang.Class<android.view.ViewStub> r6 = android.view.ViewStub.class
            if (r5 != r6) goto L5a
            goto L62
        L5a:
            java.lang.ClassLoader r6 = r5.getClassLoader()     // Catch: java.lang.Throwable -> L67
            java.lang.ClassLoader r7 = r11.c     // Catch: java.lang.Throwable -> L67
            if (r6 == r7) goto L63
        L62:
            goto L67
        L63:
            r6 = r5
            r5 = 1
            goto L69
        L66:
            r5 = r1
        L67:
            r6 = r5
            r5 = 0
        L69:
            if (r5 != 0) goto L73
            com.amap.api.mapcore.util.gs$a r13 = r11.e
            java.util.HashSet<java.lang.String> r13 = r13.a
            r13.add(r12)
            return r1
        L73:
            java.lang.Class[] r5 = new java.lang.Class[r2]     // Catch: java.lang.Throwable -> L8a
            java.lang.Class<android.content.Context> r7 = android.content.Context.class
            r5[r4] = r7     // Catch: java.lang.Throwable -> L8a
            java.lang.Class<android.util.AttributeSet> r7 = android.util.AttributeSet.class
            r5[r3] = r7     // Catch: java.lang.Throwable -> L8a
            java.lang.reflect.Constructor r5 = r6.getConstructor(r5)     // Catch: java.lang.Throwable -> L8a
            com.amap.api.mapcore.util.gs$a r0 = r11.e     // Catch: java.lang.Throwable -> L88
            java.util.HashMap<java.lang.String, java.lang.reflect.Constructor<?>> r0 = r0.b     // Catch: java.lang.Throwable -> L88
            r0.put(r12, r5)     // Catch: java.lang.Throwable -> L88
        L88:
            r0 = r5
            goto L8b
        L8a:
        L8b:
            if (r0 == 0) goto L9b
            java.lang.Object[] r12 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L9a
            r12[r4] = r13     // Catch: java.lang.Throwable -> L9a
            r12[r3] = r14     // Catch: java.lang.Throwable -> L9a
            java.lang.Object r12 = r0.newInstance(r12)     // Catch: java.lang.Throwable -> L9a
            android.view.View r12 = (android.view.View) r12     // Catch: java.lang.Throwable -> L9a
            goto L9c
        L9a:
            return r1
        L9b:
            r12 = r1
        L9c:
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.gs.a(java.lang.String, android.content.Context, android.util.AttributeSet):android.view.View");
    }

    @Override // android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        return this.a != null ? this.a : super.getResources();
    }

    @Override // android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public Object getSystemService(String str) {
        if (!"layout_inflater".equals(str)) {
            return super.getSystemService(str);
        }
        if (this.b == null) {
            LayoutInflater layoutInflater = (LayoutInflater) super.getSystemService(str);
            if (layoutInflater != null) {
                this.b = layoutInflater.cloneInContext(this);
            }
            this.b.setFactory(this.f);
            this.b = this.b.cloneInContext(this);
        }
        return this.b;
    }
}
