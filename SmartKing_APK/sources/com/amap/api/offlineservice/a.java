package com.amap.api.offlineservice;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import com.amap.api.maps.offlinemap.OfflineMapActivity;

/* compiled from: ServiceModule.java */
/* loaded from: classes.dex */
public abstract class a {
    protected OfflineMapActivity a = null;

    public int a(float f) {
        return this.a != null ? (int) ((f * (this.a.getResources().getDisplayMetrics().densityDpi / 160.0f)) + 0.5f) : (int) f;
    }

    public abstract void a();

    public void a(Bundle bundle) {
        this.a.showScr();
    }

    public abstract void a(View view);

    public void a(OfflineMapActivity offlineMapActivity) {
        this.a = offlineMapActivity;
    }

    public boolean b() {
        return true;
    }

    public abstract RelativeLayout c();

    public abstract void d();

    public void e() {
    }

    public void f() {
    }

    public void g() {
    }

    public void h() {
    }
}
