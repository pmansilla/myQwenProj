package com.amap.openapi;

import com.amap.location.common.model.AmapLoc;
import java.util.HashMap;

/* compiled from: OffWifiInfo.java */
/* loaded from: classes.dex */
public class bu {
    public int a = 0;
    public HashMap<Long, bt> b = new HashMap<>();
    public int c = 0;
    public StringBuilder d = new StringBuilder();
    public StringBuilder e = new StringBuilder();
    public AmapLoc f;

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append(this.a);
        sb.append("@");
        sb.append(this.c);
        sb.append("@");
        sb.append(this.f != null ? this.f.getLat() : 0.0d);
        sb.append("@");
        sb.append(this.f != null ? this.f.getLon() : 0.0d);
        sb.append('}');
        return sb.toString();
    }
}
