package com.amap.api.mapcore.util;

import com.amap.api.maps.model.MultiPointItem;
import com.autonavi.amap.mapcore.IPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* compiled from: QuadTree2.java */
/* loaded from: classes.dex */
public class bn {
    private final bk a;
    private final int b;
    private int c;
    private List<MultiPointItem> d;
    private List<bn> e;

    private bn(int i, int i2, int i3, int i4, int i5) {
        this(new bk(i, i2, i3, i4), i5);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public bn(bk bkVar) {
        this(bkVar, 0);
    }

    private bn(bk bkVar, int i) {
        this.c = 30;
        this.e = null;
        this.a = bkVar;
        this.b = i;
        this.c = a(this.b);
    }

    private int a(int i) {
        switch (i) {
            case 0:
                return 50;
            case 1:
                return 30;
            case 2:
            case 3:
                return 20;
            case 4:
            case 5:
                return 10;
            case 6:
            default:
                return 5;
        }
    }

    private void a(int i, int i2, MultiPointItem multiPointItem) {
        if (this.d == null) {
            this.d = new ArrayList();
        }
        if (this.d.size() <= this.c || this.b >= 40) {
            this.d.add(multiPointItem);
            return;
        }
        if (this.e == null) {
            b();
        }
        if (this.e != null) {
            if (i2 < this.a.f) {
                if (i < this.a.e) {
                    this.e.get(0).a(i, i2, multiPointItem);
                    return;
                } else {
                    this.e.get(1).a(i, i2, multiPointItem);
                    return;
                }
            }
            if (i < this.a.e) {
                this.e.get(2).a(i, i2, multiPointItem);
            } else {
                this.e.get(3).a(i, i2, multiPointItem);
            }
        }
    }

    private void a(bk bkVar, Collection<MultiPointItem> collection, float f, double d) {
        if (this.a.a(bkVar)) {
            if (this.d != null) {
                int size = (int) (this.d.size() * f);
                for (int i = 0; i < size; i++) {
                    MultiPointItem multiPointItem = this.d.get(i);
                    if (bkVar.a(multiPointItem.getIPoint())) {
                        collection.add(multiPointItem);
                    }
                }
            }
            if (d > 0.0d) {
                double d2 = this.a.d;
                double d3 = this.a.b;
                Double.isNaN(d2);
                Double.isNaN(d3);
                double d4 = d2 - d3;
                double d5 = this.a.c;
                double d6 = this.a.a;
                Double.isNaN(d5);
                Double.isNaN(d6);
                double d7 = (d4 * (d5 - d6)) / d;
                if (d7 < 0.7f) {
                    return;
                } else {
                    f = d7 > 1.0d ? 1.0f : (float) ((((4.8188d * d7) * d7) - (d7 * 4.9339d)) + 1.1093d);
                }
            }
            if (this.e != null) {
                Iterator<bn> it = this.e.iterator();
                while (it.hasNext()) {
                    it.next().a(bkVar, collection, f, d);
                }
            }
        }
    }

    private void b() {
        this.e = new ArrayList(4);
        this.e.add(new bn(this.a.a, this.a.e, this.a.b, this.a.f, this.b + 1));
        this.e.add(new bn(this.a.e, this.a.c, this.a.b, this.a.f, this.b + 1));
        this.e.add(new bn(this.a.a, this.a.e, this.a.f, this.a.d, this.b + 1));
        this.e.add(new bn(this.a.e, this.a.c, this.a.f, this.a.d, this.b + 1));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a() {
        this.e = null;
        if (this.d != null) {
            this.d.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(bk bkVar, Collection<MultiPointItem> collection, double d) {
        a(bkVar, collection, 1.0f, d);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(MultiPointItem multiPointItem) {
        IPoint iPoint = multiPointItem.getIPoint();
        if (this.a.a(iPoint.x, iPoint.y)) {
            a(iPoint.x, iPoint.y, multiPointItem);
        }
    }
}
