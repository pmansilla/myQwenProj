package com.amap.api.mapcore.util;

import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.sun.mail.imap.IMAPStore;
import org.json.JSONObject;

/* compiled from: MapLocationModel.java */
/* loaded from: classes.dex */
public final class ki extends Inner_3dMap_location {
    boolean a;
    private String b;
    private String c;
    private int d;
    private String e;
    private String f;
    private JSONObject g;
    private String h;
    private String i;
    private long j;
    private String k;

    public ki(String str) {
        super(str);
        this.b = null;
        this.c = "";
        this.e = "";
        this.f = AmapLoc.TYPE_NEW;
        this.g = null;
        this.h = "";
        this.a = true;
        this.i = "";
        this.j = 0L;
        this.k = null;
    }

    public final String a() {
        return this.b;
    }

    public final void a(String str) {
        this.b = str;
    }

    public final String b() {
        return this.c;
    }

    public final void b(String str) {
        this.c = str;
    }

    public final int c() {
        return this.d;
    }

    public final void c(String str) {
        int i;
        if (!TextUtils.isEmpty(str)) {
            if (getProvider().equals("gps")) {
                this.d = 0;
                return;
            } else if (str.equals(AmapLoc.RESULT_TYPE_GPS)) {
                this.d = 0;
                return;
            } else if (str.equals(AmapLoc.RESULT_TYPE_WIFI_ONLY)) {
                i = 1;
                this.d = i;
            }
        }
        i = -1;
        this.d = i;
    }

    public final String d() {
        return this.e;
    }

    public final void d(String str) {
        this.e = str;
    }

    public final JSONObject e() {
        return this.g;
    }

    public final void e(String str) {
        this.desc = str;
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_location
    public final void setFloor(String str) {
        if (!TextUtils.isEmpty(str)) {
            str = str.replace("F", "");
            try {
                Integer.parseInt(str);
            } catch (Throwable th) {
                kw.a(th, "MapLocationModel", "setFloor");
                str = null;
            }
        }
        this.floor = str;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0004. Please report as an issue. */
    @Override // com.autonavi.amap.mapcore.Inner_3dMap_location
    public final JSONObject toJson(int i) {
        try {
            JSONObject json = super.toJson(i);
            switch (i) {
                case 1:
                    json.put("retype", this.e);
                    json.put("cens", this.i);
                    json.put("poiid", this.buildingId);
                    json.put("floor", this.floor);
                    json.put("coord", this.d);
                    json.put("mcell", this.h);
                    json.put("desc", this.desc);
                    json.put(IMAPStore.ID_ADDRESS, getAddress());
                    if (this.g != null && la.a(json, "offpct")) {
                        json.put("offpct", this.g.getString("offpct"));
                    }
                    break;
                case 2:
                case 3:
                    json.put("type", this.f);
                    json.put("isReversegeo", this.a);
                    return json;
                default:
                    return json;
            }
        } catch (Throwable th) {
            kw.a(th, "MapLocationModel", "toStr");
            return null;
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_location
    public final String toStr(int i) {
        JSONObject jSONObject;
        try {
            jSONObject = super.toJson(i);
            jSONObject.put("nb", this.k);
        } catch (Throwable th) {
            kw.a(th, "MapLocationModel", "toStr part2");
            jSONObject = null;
        }
        if (jSONObject == null) {
            return null;
        }
        return jSONObject.toString();
    }
}
