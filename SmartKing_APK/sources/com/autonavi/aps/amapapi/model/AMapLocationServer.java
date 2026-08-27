package com.autonavi.aps.amapapi.model;

import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.location.common.model.AmapLoc;
import com.loc.es;
import com.loc.fa;
import com.sun.mail.imap.IMAPStore;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AMapLocationServer extends AMapLocation {
    protected String d;
    boolean e;
    String f;
    private String g;
    private String h;
    private int i;
    private String j;
    private String k;
    private JSONObject l;
    private String m;
    private String n;
    private long o;
    private String p;

    public AMapLocationServer(String str) {
        super(str);
        this.d = "";
        this.g = null;
        this.h = "";
        this.j = "";
        this.k = AmapLoc.TYPE_NEW;
        this.l = null;
        this.m = "";
        this.e = true;
        this.f = String.valueOf(AMapLocationClientOption.GeoLanguage.DEFAULT);
        this.n = "";
        this.o = 0L;
        this.p = null;
    }

    public final String a() {
        return this.g;
    }

    public final void a(long j) {
        this.o = j;
    }

    public final void a(String str) {
        this.g = str;
    }

    public final void a(JSONObject jSONObject) {
        this.l = jSONObject;
    }

    public final void a(boolean z) {
        this.e = z;
    }

    public final String b() {
        return this.h;
    }

    public final void b(String str) {
        this.h = str;
    }

    public final void b(JSONObject jSONObject) {
        try {
            es.a(this, jSONObject);
            this.k = jSONObject.optString("type", this.k);
            this.j = jSONObject.optString("retype", this.j);
            String optString = jSONObject.optString("cens", this.n);
            if (!TextUtils.isEmpty(optString)) {
                String[] split = optString.split("\\*");
                int length = split.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    String str = split[i];
                    if (!TextUtils.isEmpty(str)) {
                        String[] split2 = str.split(",");
                        setLongitude(fa.e(split2[0]));
                        setLatitude(fa.e(split2[1]));
                        setAccuracy(fa.g(split2[2]));
                        break;
                    }
                    i++;
                }
                this.n = optString;
            }
            this.d = jSONObject.optString("desc", this.d);
            c(jSONObject.optString("coord", String.valueOf(this.i)));
            this.m = jSONObject.optString("mcell", this.m);
            this.e = jSONObject.optBoolean("isReversegeo", this.e);
            this.f = jSONObject.optString("geoLanguage", this.f);
            if (fa.a(jSONObject, "poiid")) {
                setBuildingId(jSONObject.optString("poiid"));
            }
            if (fa.a(jSONObject, "pid")) {
                setBuildingId(jSONObject.optString("pid"));
            }
            if (fa.a(jSONObject, "floor")) {
                setFloor(jSONObject.optString("floor"));
            }
            if (fa.a(jSONObject, "flr")) {
                setFloor(jSONObject.optString("flr"));
            }
        } catch (Throwable th) {
            es.a(th, "AmapLoc", "AmapLoc");
        }
    }

    public final int c() {
        return this.i;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0021  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void c(java.lang.String r2) {
        /*
            r1 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 != 0) goto L1a
            java.lang.String r0 = "0"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L10
            r2 = 0
            goto L1b
        L10:
            java.lang.String r0 = "1"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L1a
            r2 = 1
            goto L1b
        L1a:
            r2 = -1
        L1b:
            r1.i = r2
            int r2 = r1.i
            if (r2 != 0) goto L27
            java.lang.String r2 = "WGS84"
        L23:
            super.setCoordType(r2)
            return
        L27:
            java.lang.String r2 = "GCJ02"
            goto L23
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autonavi.aps.amapapi.model.AMapLocationServer.c(java.lang.String):void");
    }

    public final String d() {
        return this.j;
    }

    public final void d(String str) {
        this.j = str;
    }

    public final String e() {
        return this.k;
    }

    public final void e(String str) {
        this.k = str;
    }

    public final JSONObject f() {
        return this.l;
    }

    public final void f(String str) {
        this.f = str;
    }

    public final String g() {
        return this.m;
    }

    public final void g(String str) {
        this.d = str;
    }

    public final AMapLocationServer h() {
        String str = this.m;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.split(",");
        if (split.length != 3) {
            return null;
        }
        AMapLocationServer aMapLocationServer = new AMapLocationServer("");
        aMapLocationServer.setProvider(getProvider());
        aMapLocationServer.setLongitude(fa.e(split[0]));
        aMapLocationServer.setLatitude(fa.e(split[1]));
        aMapLocationServer.setAccuracy(fa.f(split[2]));
        aMapLocationServer.setCityCode(getCityCode());
        aMapLocationServer.setAdCode(getAdCode());
        aMapLocationServer.setCountry(getCountry());
        aMapLocationServer.setProvince(getProvince());
        aMapLocationServer.setCity(getCity());
        aMapLocationServer.setTime(getTime());
        aMapLocationServer.k = this.k;
        aMapLocationServer.c(String.valueOf(this.i));
        if (fa.a(aMapLocationServer)) {
            return aMapLocationServer;
        }
        return null;
    }

    public final void h(String str) {
        this.p = str;
    }

    public final boolean i() {
        return this.e;
    }

    public final String j() {
        return this.f;
    }

    public final long k() {
        return this.o;
    }

    public final String l() {
        return this.p;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x0004. Please report as an issue. */
    @Override // com.amap.api.location.AMapLocation
    public JSONObject toJson(int i) {
        try {
            JSONObject json = super.toJson(i);
            switch (i) {
                case 1:
                    json.put("retype", this.j);
                    json.put("cens", this.n);
                    json.put("coord", this.i);
                    json.put("mcell", this.m);
                    json.put("desc", this.d);
                    json.put(IMAPStore.ID_ADDRESS, getAddress());
                    if (this.l != null && fa.a(json, "offpct")) {
                        json.put("offpct", this.l.getString("offpct"));
                    }
                    break;
                case 2:
                case 3:
                    json.put("type", this.k);
                    json.put("isReversegeo", this.e);
                    json.put("geoLanguage", this.f);
                    return json;
                default:
                    return json;
            }
        } catch (Throwable th) {
            es.a(th, "AmapLoc", "toStr");
            return null;
        }
    }

    @Override // com.amap.api.location.AMapLocation
    public String toStr() {
        return toStr(1);
    }

    @Override // com.amap.api.location.AMapLocation
    public String toStr(int i) {
        JSONObject jSONObject;
        try {
            jSONObject = toJson(i);
            jSONObject.put("nb", this.p);
        } catch (Throwable th) {
            es.a(th, "AMapLocation", "toStr part2");
            jSONObject = null;
        }
        if (jSONObject == null) {
            return null;
        }
        return jSONObject.toString();
    }
}
