package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.amap.api.mapcore.util.cg;
import com.amap.api.mapcore.util.cp;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import java.io.File;
import me.panpf.sketch.uri.FileUriModel;

/* compiled from: CityObject.java */
/* loaded from: classes.dex */
public class bo extends OfflineMapCity implements bx, co {
    public static final Parcelable.Creator<bo> o = new Parcelable.Creator<bo>() { // from class: com.amap.api.mapcore.util.bo.2
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public bo createFromParcel(Parcel parcel) {
            return new bo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public bo[] newArray(int i) {
            return new bo[i];
        }
    };
    public final cs a;
    public final cs b;
    public final cs c;
    public final cs d;
    public final cs e;
    public final cs f;
    public final cs g;
    public final cs h;
    public final cs i;
    public final cs j;
    public final cs k;
    cs l;
    Context m;
    boolean n;
    private String p;
    private String q;
    private long r;

    public bo(Context context, int i) {
        this.a = new cu(6, this);
        this.b = new db(2, this);
        this.c = new cx(0, this);
        this.d = new cz(3, this);
        this.e = new da(1, this);
        this.f = new ct(4, this);
        this.g = new cy(7, this);
        this.h = new cv(-1, this);
        this.i = new cv(101, this);
        this.j = new cv(102, this);
        this.k = new cv(103, this);
        this.p = null;
        this.q = "";
        this.n = false;
        this.r = 0L;
        this.m = context;
        a(i);
    }

    public bo(Context context, OfflineMapCity offlineMapCity) {
        this(context, offlineMapCity.getState());
        setCity(offlineMapCity.getCity());
        setUrl(offlineMapCity.getUrl());
        setState(offlineMapCity.getState());
        setCompleteCode(offlineMapCity.getcompleteCode());
        setAdcode(offlineMapCity.getAdcode());
        setVersion(offlineMapCity.getVersion());
        setSize(offlineMapCity.getSize());
        setCode(offlineMapCity.getCode());
        setJianpin(offlineMapCity.getJianpin());
        setPinyin(offlineMapCity.getPinyin());
        t();
    }

    public bo(Parcel parcel) {
        super(parcel);
        this.a = new cu(6, this);
        this.b = new db(2, this);
        this.c = new cx(0, this);
        this.d = new cz(3, this);
        this.e = new da(1, this);
        this.f = new ct(4, this);
        this.g = new cy(7, this);
        this.h = new cv(-1, this);
        this.i = new cv(101, this);
        this.j = new cv(102, this);
        this.k = new cv(103, this);
        this.p = null;
        this.q = "";
        this.n = false;
        this.r = 0L;
        this.q = parcel.readString();
    }

    private void a(final File file, File file2, final String str) {
        new cg().a(file, file2, -1L, cm.a(file), new cg.a() { // from class: com.amap.api.mapcore.util.bo.1
            @Override // com.amap.api.mapcore.util.cg.a
            public void a(String str2, String str3) {
            }

            @Override // com.amap.api.mapcore.util.cg.a
            public void a(String str2, String str3, float f) {
                int i = bo.this.getcompleteCode();
                double d = f;
                Double.isNaN(d);
                int i2 = (int) ((d * 0.39d) + 60.0d);
                if (i2 - i <= 0 || System.currentTimeMillis() - bo.this.r <= 1000) {
                    return;
                }
                bo.this.setCompleteCode(i2);
                bo.this.r = System.currentTimeMillis();
            }

            @Override // com.amap.api.mapcore.util.cg.a
            public void a(String str2, String str3, int i) {
                bo.this.l.a(bo.this.k.b());
            }

            @Override // com.amap.api.mapcore.util.cg.a
            public void b(String str2, String str3) {
                try {
                    if (new File(str).delete()) {
                        cm.b(file);
                        bo.this.setCompleteCode(100);
                        bo.this.l.g();
                    }
                } catch (Exception unused) {
                    bo.this.l.a(bo.this.k.b());
                }
            }
        });
    }

    @Override // com.amap.api.mapcore.util.co
    public String A() {
        return getAdcode();
    }

    @Override // com.amap.api.mapcore.util.ci
    public String B() {
        return u();
    }

    @Override // com.amap.api.mapcore.util.ci
    public String C() {
        return v();
    }

    public String a() {
        return this.q;
    }

    public void a(int i) {
        switch (i) {
            case -1:
                this.l = this.h;
                break;
            case 0:
                this.l = this.c;
                break;
            case 1:
                this.l = this.e;
                break;
            case 2:
                this.l = this.b;
                break;
            case 3:
                this.l = this.d;
                break;
            case 4:
                this.l = this.f;
                break;
            default:
                switch (i) {
                    case 6:
                        this.l = this.a;
                        break;
                    case 7:
                        this.l = this.g;
                        break;
                    default:
                        switch (i) {
                            case 101:
                                this.l = this.i;
                                break;
                            case 102:
                                this.l = this.j;
                                break;
                            case 103:
                                this.l = this.k;
                                break;
                            default:
                                if (i < 0) {
                                    this.l = this.h;
                                    break;
                                }
                                break;
                        }
                }
        }
        setState(i);
    }

    @Override // com.amap.api.mapcore.util.ch
    public void a(long j) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.r > 500) {
            int i = (int) j;
            if (i > getcompleteCode()) {
                setCompleteCode(i);
                d();
            }
            this.r = currentTimeMillis;
        }
    }

    @Override // com.amap.api.mapcore.util.cp
    public void a(long j, long j2) {
        int i = (int) ((j2 * 100) / j);
        if (i != getcompleteCode()) {
            setCompleteCode(i);
            d();
        }
    }

    @Override // com.amap.api.mapcore.util.cp
    public void a(cp.a aVar) {
        int b;
        switch (aVar) {
            case amap_exception:
                b = this.j.b();
                break;
            case file_io_exception:
                b = this.k.b();
                break;
            case network_exception:
                b = this.i.b();
                break;
            default:
                b = 6;
                break;
        }
        if (this.l.equals(this.c) || this.l.equals(this.b)) {
            this.l.a(b);
        }
    }

    public void a(cs csVar) {
        this.l = csVar;
        setState(csVar.b());
    }

    public void a(String str) {
        this.q = str;
    }

    public cs b(int i) {
        switch (i) {
            case 101:
                return this.i;
            case 102:
                return this.j;
            case 103:
                return this.k;
            default:
                return this.h;
        }
    }

    @Override // com.amap.api.mapcore.util.bx
    public String b() {
        return getUrl();
    }

    @Override // com.amap.api.mapcore.util.ch
    public void b(String str) {
        this.l.equals(this.e);
        this.q = str;
        String u = u();
        String v = v();
        if (TextUtils.isEmpty(u) || TextUtils.isEmpty(v)) {
            r();
            return;
        }
        File file = new File(v + FileUriModel.SCHEME);
        File file2 = new File(fr.a(this.m) + File.separator + "map/");
        File file3 = new File(fr.a(this.m));
        if (file3.exists() || file3.mkdir()) {
            if (file2.exists() || file2.mkdir()) {
                a(file, file2, u);
            }
        }
    }

    public cs c() {
        return this.l;
    }

    public void d() {
        bp a = bp.a(this.m);
        if (a != null) {
            a.c(this);
        }
    }

    @Override // com.amap.api.maps.offlinemap.OfflineMapCity, com.amap.api.maps.offlinemap.City, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public void e() {
        bp a = bp.a(this.m);
        if (a != null) {
            a.e(this);
            d();
        }
    }

    public void f() {
        cm.a("CityOperation current State==>" + c().b());
        if (this.l.equals(this.d)) {
            this.l.d();
            return;
        }
        if (this.l.equals(this.c)) {
            this.l.e();
            return;
        }
        if (this.l.equals(this.g) || this.l.equals(this.h)) {
            k();
            this.n = true;
        } else if (this.l.equals(this.j) || this.l.equals(this.i) || this.l.a(this.k)) {
            this.l.c();
        } else {
            c().h();
        }
    }

    public void g() {
        this.l.e();
    }

    public void h() {
        this.l.a(this.k.b());
    }

    public void i() {
        this.l.a();
        if (this.n) {
            this.l.h();
        }
        this.n = false;
    }

    public void j() {
        this.l.equals(this.f);
        this.l.f();
    }

    public void k() {
        bp a = bp.a(this.m);
        if (a != null) {
            a.a(this);
        }
    }

    public void l() {
        bp a = bp.a(this.m);
        if (a != null) {
            a.b(this);
        }
    }

    public void m() {
        bp a = bp.a(this.m);
        if (a != null) {
            a.d(this);
        }
    }

    @Override // com.amap.api.mapcore.util.cp
    public void n() {
        this.r = 0L;
        if (!this.l.equals(this.b)) {
            cm.a("state must be waiting when download onStart");
        }
        this.l.c();
    }

    @Override // com.amap.api.mapcore.util.cp
    public void o() {
        if (!this.l.equals(this.c)) {
            cm.a("state must be Loading when download onFinish");
        }
        this.l.g();
    }

    @Override // com.amap.api.mapcore.util.cp
    public void p() {
        e();
    }

    @Override // com.amap.api.mapcore.util.ch
    public void q() {
        this.r = 0L;
        setCompleteCode(0);
        this.l.equals(this.e);
        this.l.c();
    }

    @Override // com.amap.api.mapcore.util.ch
    public void r() {
        this.l.equals(this.e);
        this.l.a(this.h.b());
    }

    @Override // com.amap.api.mapcore.util.ch
    public void s() {
        e();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void t() {
        String str = bp.a;
        String c = cm.c(getUrl());
        if (c != null) {
            this.p = str + c + ".zip.tmp";
            return;
        }
        this.p = str + getPinyin() + ".zip.tmp";
    }

    public String u() {
        if (TextUtils.isEmpty(this.p)) {
            return null;
        }
        return this.p.substring(0, this.p.lastIndexOf("."));
    }

    public String v() {
        if (TextUtils.isEmpty(this.p)) {
            return null;
        }
        String u = u();
        return u.substring(0, u.lastIndexOf(46));
    }

    public boolean w() {
        double a = cm.a();
        double size = getSize();
        Double.isNaN(size);
        double size2 = getcompleteCode() * getSize();
        Double.isNaN(size2);
        return a < (size * 2.5d) - size2 ? false : false;
    }

    @Override // com.amap.api.maps.offlinemap.OfflineMapCity, com.amap.api.maps.offlinemap.City, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.q);
    }

    public bz x() {
        setState(this.l.b());
        bz bzVar = new bz(this, this.m);
        bzVar.a(a());
        cm.a("vMapFileNames: " + a());
        return bzVar;
    }

    @Override // com.amap.api.mapcore.util.co
    public boolean y() {
        return w();
    }

    @Override // com.amap.api.mapcore.util.co
    public String z() {
        StringBuffer stringBuffer = new StringBuffer();
        String c = cm.c(getUrl());
        if (c != null) {
            stringBuffer.append(c);
        } else {
            stringBuffer.append(getPinyin());
        }
        stringBuffer.append(".zip");
        return stringBuffer.toString();
    }
}
