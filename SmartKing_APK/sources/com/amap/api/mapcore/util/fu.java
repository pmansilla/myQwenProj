package com.amap.api.mapcore.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import com.amap.api.mapcore.util.ed;
import com.amap.api.mapcore.util.fv;
import java.lang.ref.WeakReference;

/* compiled from: AbstractImageWorker.java */
/* loaded from: classes.dex */
public abstract class fu {
    private fv a;
    private fv.a b;
    protected Resources d;
    private boolean e = false;
    protected boolean c = false;
    private final Object f = new Object();
    private c g = null;

    /* compiled from: AbstractImageWorker.java */
    /* loaded from: classes.dex */
    public class a extends er<Boolean, Void, Bitmap> {
        private final WeakReference<ed.a> e;

        public a(ed.a aVar) {
            this.e = new WeakReference<>(aVar);
        }

        private ed.a e() {
            ed.a aVar = this.e.get();
            if (this == fu.c(aVar)) {
                return aVar;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.er
        public Bitmap a(Boolean... boolArr) {
            try {
                boolean booleanValue = boolArr[0].booleanValue();
                ed.a aVar = this.e.get();
                if (aVar == null) {
                    return null;
                }
                String str = aVar.a + "-" + aVar.b + "-" + aVar.c;
                synchronized (fu.this.f) {
                    while (fu.this.c && !d()) {
                        fu.this.f.wait();
                    }
                }
                Bitmap b = (fu.this.a == null || d() || e() == null || fu.this.e) ? null : fu.this.a.b(str);
                if (booleanValue && b == null && !d() && e() != null && !fu.this.e) {
                    synchronized (fu.class) {
                        b = fu.this.a((Object) aVar);
                    }
                }
                if (b != null && fu.this.a != null) {
                    fu.this.a.a(str, b);
                }
                return b;
            } catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.er
        public void a(Bitmap bitmap) {
            try {
                if (d() || fu.this.e) {
                    bitmap = null;
                }
                ed.a e = e();
                if (bitmap == null || bitmap.isRecycled() || e == null) {
                    return;
                }
                e.a(bitmap);
                if (fu.this.g != null) {
                    fu.this.g.a();
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.er
        public void b(Bitmap bitmap) {
            super.b((a) bitmap);
            synchronized (fu.this.f) {
                try {
                    fu.this.f.notifyAll();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* compiled from: AbstractImageWorker.java */
    /* loaded from: classes.dex */
    public class b extends er<Object, Void, Void> {
        protected b() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.er
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public Void a(Object... objArr) {
            try {
                switch (((Integer) objArr[0]).intValue()) {
                    case 0:
                        fu.this.c();
                        break;
                    case 1:
                        fu.this.b();
                        break;
                    case 2:
                        fu.this.d();
                        break;
                    case 3:
                        fu.this.b(((Boolean) objArr[1]).booleanValue());
                        break;
                    case 4:
                        fu.this.e();
                        break;
                }
                return null;
            } catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        }
    }

    /* compiled from: AbstractImageWorker.java */
    /* loaded from: classes.dex */
    public interface c {
        void a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public fu(Context context) {
        this.d = context.getResources();
    }

    public static void a(ed.a aVar) {
        a c2 = c(aVar);
        if (c2 != null) {
            c2.a(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static a c(ed.a aVar) {
        if (aVar != null) {
            return aVar.j;
        }
        return null;
    }

    protected abstract Bitmap a(Object obj);

    /* JADX INFO: Access modifiers changed from: protected */
    public fv a() {
        return this.a;
    }

    public void a(c cVar) {
        this.g = cVar;
    }

    public void a(fv.a aVar) {
        this.b = aVar;
        this.a = fv.a(this.b);
        new b().c(1);
    }

    public void a(String str) {
        this.b.b(str);
        new b().c(4);
    }

    public void a(boolean z) {
        synchronized (this.f) {
            this.c = z;
            if (!this.c) {
                try {
                    this.f.notifyAll();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }

    public void a(boolean z, ed.a aVar) {
        if (aVar == null) {
            return;
        }
        Bitmap bitmap = null;
        try {
            if (this.a != null) {
                bitmap = this.a.a(aVar.a + "-" + aVar.b + "-" + aVar.c);
            }
            if (bitmap != null) {
                aVar.a(bitmap);
                return;
            }
            a aVar2 = new a(aVar);
            aVar.j = aVar2;
            aVar2.a(er.c, Boolean.valueOf(z));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    protected void b() {
        if (this.a != null) {
            this.a.a();
        }
    }

    protected void b(boolean z) {
        if (this.a != null) {
            this.a.a(z);
            this.a = null;
        }
    }

    protected void c() {
        if (this.a != null) {
            this.a.b();
        }
    }

    public void c(boolean z) {
        new b().c(3, Boolean.valueOf(z));
    }

    protected void d() {
        if (this.a != null) {
            this.a.c();
        }
    }

    protected void e() {
        if (this.a != null) {
            this.a.a(false);
            this.a.a();
        }
    }

    public void f() {
        new b().c(0);
    }
}
