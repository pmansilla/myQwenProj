package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import com.amap.api.mapcore.util.ef;
import com.amap.api.mapcore.util.er;
import com.amap.api.mapcore.util.fu;
import com.amap.api.mapcore.util.fv;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.TileProvider;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.ITileOverlay;
import java.lang.ref.WeakReference;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: TileOverlayDelegateImp.java */
/* loaded from: classes.dex */
public class ed implements du {
    private static int h;
    ef.f a;
    private aq b;
    private TileProvider c;
    private Float d;
    private boolean e;
    private boolean f;
    private ad g;
    private int i;
    private int j;
    private int k;
    private fs l;
    private List<a> m = new ArrayList();
    private boolean n = false;
    private b o = null;
    private String p;
    private FloatBuffer q;

    /* compiled from: TileOverlayDelegateImp.java */
    /* loaded from: classes.dex */
    public static class a implements Cloneable {
        public int a;
        public int b;
        public int c;
        public int d;
        public IPoint e;
        public int f;
        public boolean g;
        public FloatBuffer h;
        public Bitmap i;
        public fu.a j;
        public int k;
        private ad l;
        private aq m;
        private fs n;

        public a(int i, int i2, int i3, int i4, ad adVar, aq aqVar, fs fsVar) {
            this.f = 0;
            this.g = false;
            this.h = null;
            this.i = null;
            this.j = null;
            this.k = 0;
            this.a = i;
            this.b = i2;
            this.c = i3;
            this.d = i4;
            this.l = adVar;
            this.m = aqVar;
            this.n = fsVar;
        }

        public a(a aVar) {
            this.f = 0;
            this.g = false;
            this.h = null;
            this.i = null;
            this.j = null;
            this.k = 0;
            this.a = aVar.a;
            this.b = aVar.b;
            this.c = aVar.c;
            this.d = aVar.d;
            this.e = aVar.e;
            this.h = aVar.h;
            this.k = 0;
            this.m = aVar.m;
            this.l = aVar.l;
            this.n = aVar.n;
        }

        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public a clone() {
            try {
                a aVar = (a) super.clone();
                aVar.a = this.a;
                aVar.b = this.b;
                aVar.c = this.c;
                aVar.d = this.d;
                aVar.e = (IPoint) this.e.clone();
                aVar.h = this.h.asReadOnlyBuffer();
                this.k = 0;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return new a(this);
        }

        public synchronized void a(Bitmap bitmap) {
            if (bitmap != null) {
                try {
                    if (!bitmap.isRecycled()) {
                        try {
                            this.j = null;
                            this.i = bitmap;
                            this.l.setRunLowFrame(false);
                        } catch (Throwable th) {
                            ic.c(th, "TileOverlayDelegateImp", "setBitmap");
                            th.printStackTrace();
                            if (this.k < 3) {
                                this.k++;
                                if (this.n != null) {
                                    this.n.a(true, this);
                                }
                            }
                        }
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            }
            if (this.k < 3) {
                this.k++;
                if (this.n != null) {
                    this.n.a(true, this);
                }
            }
        }

        public void b() {
            try {
                fu.a(this);
                if (this.g) {
                    this.m.a(this.f);
                }
                this.g = false;
                this.f = 0;
                if (this.i != null && !this.i.isRecycled()) {
                    this.i.recycle();
                }
                this.i = null;
                if (this.h != null) {
                    this.h.clear();
                }
                this.h = null;
                this.j = null;
                this.k = 0;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return this.a == aVar.a && this.b == aVar.b && this.c == aVar.c && this.d == aVar.d;
        }

        public int hashCode() {
            return (this.a * 7) + (this.b * 11) + (this.c * 13) + this.d;
        }

        public String toString() {
            return this.a + "-" + this.b + "-" + this.c + "-" + this.d;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: TileOverlayDelegateImp.java */
    /* loaded from: classes.dex */
    public static class b extends er<Void, Void, List<a>> {
        private int d;
        private boolean e;
        private int f;
        private int g;
        private int h;
        private WeakReference<ad> i;
        private List<a> j;
        private boolean k;
        private WeakReference<aq> l;
        private WeakReference<fs> m;

        public b(boolean z, ad adVar, int i, int i2, int i3, List<a> list, boolean z2, aq aqVar, fs fsVar) {
            this.f = 256;
            this.g = 256;
            this.h = 0;
            this.e = z;
            this.i = new WeakReference<>(adVar);
            this.f = i;
            this.g = i2;
            this.h = i3;
            this.j = list;
            this.k = z2;
            this.l = new WeakReference<>(aqVar);
            this.m = new WeakReference<>(fsVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.er
        public List<a> a(Void... voidArr) {
            try {
                ad adVar = this.i.get();
                if (adVar == null) {
                    return null;
                }
                int mapWidth = adVar.getMapWidth();
                int mapHeight = adVar.getMapHeight();
                this.d = (int) adVar.g();
                if (mapWidth > 0 && mapHeight > 0) {
                    return ed.b(adVar, this.d, this.f, this.g, this.h, this.l.get(), this.m.get());
                }
                return null;
            } catch (Throwable th) {
                th.printStackTrace();
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.amap.api.mapcore.util.er
        public void a(List<a> list) {
            if (list == null) {
                return;
            }
            try {
                if (list.size() <= 0) {
                    return;
                }
                ed.b(this.i.get(), list, this.d, this.e, this.j, this.k, this.l.get(), this.m.get());
                list.clear();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public ed(TileOverlayOptions tileOverlayOptions, aq aqVar, boolean z) {
        this.f = false;
        this.i = 256;
        this.j = 256;
        this.k = -1;
        this.p = null;
        this.q = null;
        this.b = aqVar;
        this.c = tileOverlayOptions.getTileProvider();
        this.i = this.c.getTileWidth();
        this.j = this.c.getTileHeight();
        this.q = fr.a(new float[]{0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f});
        this.d = Float.valueOf(tileOverlayOptions.getZIndex());
        this.e = tileOverlayOptions.isVisible();
        this.f = z;
        if (this.f) {
            this.p = "TileOverlay0";
        } else {
            this.p = getId();
        }
        this.g = this.b.a();
        this.k = Integer.parseInt(this.p.substring("TileOverlay".length()));
        try {
            fv.a aVar = z ? new fv.a(this.b.e(), this.p, aqVar.a().getMapConfig().getMapLanguage()) : new fv.a(this.b.e(), this.p);
            aVar.a(tileOverlayOptions.getMemoryCacheEnabled());
            if (this.f) {
                aVar.i = false;
            }
            aVar.b(tileOverlayOptions.getDiskCacheEnabled());
            aVar.a(tileOverlayOptions.getMemCacheSize());
            aVar.a(tileOverlayOptions.getDiskCacheSize());
            String diskCacheDir = tileOverlayOptions.getDiskCacheDir();
            if (diskCacheDir != null && !"".equals(diskCacheDir)) {
                aVar.a(diskCacheDir);
            }
            this.l = new fs(this.b.e(), this.i, this.j);
            this.l.a(this.c);
            this.l.a(aVar);
            this.l.a(new fu.c() { // from class: com.amap.api.mapcore.util.ed.1
                @Override // com.amap.api.mapcore.util.fu.c
                public void a() {
                    ed.this.g.q();
                }
            });
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void a(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        if (floatBuffer == null || floatBuffer2 == null || i == 0) {
            return;
        }
        if (this.a == null || this.a.c()) {
            e();
        }
        this.a.a();
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glBlendColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        GLES20.glEnableVertexAttribArray(this.a.b);
        GLES20.glVertexAttribPointer(this.a.b, 3, 5126, false, 12, (Buffer) floatBuffer);
        GLES20.glEnableVertexAttribArray(this.a.c);
        GLES20.glVertexAttribPointer(this.a.c, 2, 5126, false, 8, (Buffer) floatBuffer2);
        GLES20.glUniformMatrix4fv(this.a.a, 1, false, this.b.g(), 0);
        GLES20.glDrawArrays(6, 0, 4);
        GLES20.glDisableVertexAttribArray(this.a.b);
        GLES20.glDisableVertexAttribArray(this.a.c);
        GLES20.glBindTexture(3553, 0);
        GLES20.glUseProgram(0);
        GLES20.glDisable(3042);
    }

    private boolean a(a aVar) {
        float f = aVar.c;
        int i = this.i;
        int i2 = this.j;
        int i3 = aVar.e.x;
        int i4 = 1 << (20 - ((int) f));
        int i5 = i2 * i4;
        int i6 = aVar.e.y + i5;
        MapConfig mapConfig = this.g.getMapConfig();
        int i7 = (i4 * i) + i3;
        int i8 = i6 - i5;
        float[] fArr = {i3 - mapConfig.getSX(), i6 - mapConfig.getSY(), 0.0f, i7 - mapConfig.getSX(), i6 - mapConfig.getSY(), 0.0f, i7 - mapConfig.getSX(), i8 - mapConfig.getSY(), 0.0f, i3 - mapConfig.getSX(), i8 - mapConfig.getSY(), 0.0f};
        if (aVar.h == null) {
            aVar.h = fr.a(fArr);
        } else {
            aVar.h = fr.a(fArr, aVar.h);
        }
        return true;
    }

    private static String b(String str) {
        h++;
        return str + h;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x017a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.ArrayList<com.amap.api.mapcore.util.ed.a> b(com.amap.api.mapcore.util.ad r28, int r29, int r30, int r31, int r32, com.amap.api.mapcore.util.aq r33, com.amap.api.mapcore.util.fs r34) {
        /*
            Method dump skipped, instructions count: 574
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.ed.b(com.amap.api.mapcore.util.ad, int, int, int, int, com.amap.api.mapcore.util.aq, com.amap.api.mapcore.util.fs):java.util.ArrayList");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean b(ad adVar, List<a> list, int i, boolean z, List<a> list2, boolean z2, aq aqVar, fs fsVar) {
        int size;
        if (list == null || list2 == null) {
            return false;
        }
        synchronized (list2) {
            Iterator<a> it = list2.iterator();
            while (true) {
                boolean z3 = true;
                if (!it.hasNext()) {
                    break;
                }
                a next = it.next();
                Iterator<a> it2 = list.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        z3 = false;
                        break;
                    }
                    a next2 = it2.next();
                    if (next.equals(next2) && next.g) {
                        next2.g = next.g;
                        next2.f = next.f;
                        break;
                    }
                }
                if (!z3) {
                    next.b();
                }
            }
            list2.clear();
        }
        if (i > ((int) adVar.getMaxZoomLevel()) || i < ((int) adVar.getMinZoomLevel()) || (size = list.size()) <= 0) {
            return false;
        }
        for (int i2 = 0; i2 < size; i2++) {
            a aVar = list.get(i2);
            if (aVar != null) {
                if (z2) {
                    if (aqVar.a().getMapConfig().getMapLanguage().equals("zh_cn")) {
                        if (MapsInitializer.isLoadWorldGridMap()) {
                            if (aVar.c >= 7) {
                                if (fk.a(aVar.a, aVar.b, aVar.c)) {
                                }
                            }
                        }
                    } else if (!MapsInitializer.isLoadWorldGridMap() && aVar.c >= 7 && !fk.a(aVar.a, aVar.b, aVar.c)) {
                    }
                }
                list2.add(aVar);
                if (!aVar.g && fsVar != null) {
                    fsVar.a(z, aVar);
                }
            }
        }
        return true;
    }

    private void c(boolean z) {
        this.o = new b(z, this.g, this.i, this.j, this.k, this.m, this.f, this.b, this.l);
        this.o.c((Object[]) new Void[0]);
    }

    private void d() {
        if (this.o == null || this.o.a() != er.e.RUNNING) {
            return;
        }
        this.o.a(true);
    }

    private void e() {
        if (this.b == null || this.b.a() == null) {
            return;
        }
        this.a = (ef.f) this.b.a().u(0);
    }

    @Override // com.amap.api.mapcore.util.du
    public void a() {
        if (this.m != null) {
            synchronized (this.m) {
                if (this.m.size() == 0) {
                    return;
                }
                int size = this.m.size();
                for (int i = 0; i < size; i++) {
                    a aVar = this.m.get(i);
                    if (!aVar.g) {
                        try {
                            IPoint iPoint = aVar.e;
                            if (aVar.i != null && !aVar.i.isRecycled() && iPoint != null) {
                                aVar.f = fr.a(aVar.i);
                                if (aVar.f != 0) {
                                    aVar.g = true;
                                }
                                aVar.i = null;
                            }
                        } catch (Throwable th) {
                            ic.c(th, "TileOverlayDelegateImp", "drawTiles");
                        }
                    }
                    if (aVar.g) {
                        a(aVar);
                        a(aVar.f, aVar.h, this.q);
                    }
                }
            }
        }
    }

    public void a(String str) {
        d();
        b();
        if (this.l != null) {
            this.l.a(true);
            this.l.a(str);
            this.l.a(false);
        }
        c(true);
    }

    @Override // com.amap.api.mapcore.util.du
    public void a(boolean z) {
        if (this.n) {
            return;
        }
        d();
        c(z);
    }

    public void b() {
        if (this.m != null) {
            synchronized (this.m) {
                this.m.clear();
            }
        }
    }

    @Override // com.amap.api.mapcore.util.du
    public void b(boolean z) {
        if (this.n != z) {
            this.n = z;
            if (this.l != null) {
                this.l.a(z);
            }
        }
    }

    public void c() {
        d();
        synchronized (this.m) {
            int size = this.m.size();
            for (int i = 0; i < size; i++) {
                this.m.get(i).b();
            }
            this.m.clear();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public void clearTileCache() {
        if (this.l != null) {
            this.l.f();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public void destroy(boolean z) {
        d();
        synchronized (this.m) {
            int size = this.m.size();
            for (int i = 0; i < size; i++) {
                this.m.get(i).b();
            }
            this.m.clear();
        }
        if (this.l != null) {
            this.l.c(z);
            this.l.a(true);
            this.l.a((TileProvider) null);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public boolean equalsRemote(ITileOverlay iTileOverlay) {
        return equals(iTileOverlay) || iTileOverlay.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public String getId() {
        if (this.p == null) {
            this.p = b("TileOverlay");
        }
        return this.p;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public float getZIndex() {
        return this.d.floatValue();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public int hashCodeRemote() {
        return super.hashCode();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public boolean isVisible() {
        return this.e;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public void remove() {
        this.b.b(this);
        this.g.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public void setVisible(boolean z) {
        this.e = z;
        this.g.setRunLowFrame(false);
        if (z) {
            a(true);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ITileOverlay
    public void setZIndex(float f) {
        this.d = Float.valueOf(f);
        this.b.d();
    }
}
