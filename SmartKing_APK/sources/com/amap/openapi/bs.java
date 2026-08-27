package com.amap.openapi;

import com.amap.location.common.model.AmapLoc;

/* compiled from: OffCellInfo.java */
/* loaded from: classes.dex */
public class bs {
    public boolean a;
    public int b;
    public int c;
    public int d;
    public int e;
    public String f;
    public long g;
    public boolean h = false;
    public AmapLoc i;

    public bs(boolean z, String str, long j, int i, int i2, int i3, int i4) {
        this.a = z;
        this.f = str;
        this.g = j;
        this.b = i;
        this.c = i2;
        this.d = i3;
        this.e = i4;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append(this.a);
        sb.append("@");
        sb.append(this.f);
        sb.append("@");
        sb.append(this.b);
        sb.append("@");
        sb.append(this.c);
        sb.append("@");
        sb.append(this.e);
        sb.append("@");
        sb.append(this.h);
        sb.append("@");
        sb.append(this.i != null ? this.i.getLat() : 0.0d);
        sb.append("@");
        sb.append(this.i != null ? this.i.getLon() : 0.0d);
        sb.append('}');
        return sb.toString();
    }
}
