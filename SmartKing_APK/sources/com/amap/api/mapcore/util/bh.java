package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.InfoWindowParams;
import com.amap.api.maps.model.BasePointOverlay;
import com.amap.api.maps.model.Marker;

/* compiled from: InfoWindowDelegate.java */
/* loaded from: classes.dex */
public class bh {
    Context c;
    private View e;
    private TextView f;
    private TextView g;
    private bg i;
    private bg j;
    AMap.InfoWindowAdapter a = null;
    AMap.CommonInfoWindowAdapter b = null;
    private boolean d = true;
    private Drawable h = null;
    private AMap.InfoWindowAdapter k = new AMap.InfoWindowAdapter() { // from class: com.amap.api.mapcore.util.bh.1
        @Override // com.amap.api.maps.AMap.InfoWindowAdapter
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override // com.amap.api.maps.AMap.InfoWindowAdapter
        public View getInfoWindow(Marker marker) {
            try {
                if (bh.this.h == null) {
                    bh.this.h = fg.a(bh.this.c, "infowindow_bg.9.png");
                }
                if (bh.this.e == null) {
                    bh.this.e = new LinearLayout(bh.this.c);
                    bh.this.e.setBackground(bh.this.h);
                    bh.this.f = new TextView(bh.this.c);
                    bh.this.f.setText(marker.getTitle());
                    bh.this.f.setTextColor(-16777216);
                    bh.this.g = new TextView(bh.this.c);
                    bh.this.g.setTextColor(-16777216);
                    bh.this.g.setText(marker.getSnippet());
                    ((LinearLayout) bh.this.e).setOrientation(1);
                    ((LinearLayout) bh.this.e).addView(bh.this.f);
                    ((LinearLayout) bh.this.e).addView(bh.this.g);
                }
            } catch (Throwable th) {
                ic.c(th, "InfoWindowDelegate", "showInfoWindow decodeDrawableFromAsset");
                th.printStackTrace();
            }
            return bh.this.e;
        }
    };
    private AMap.CommonInfoWindowAdapter l = new AMap.CommonInfoWindowAdapter() { // from class: com.amap.api.mapcore.util.bh.2
        @Override // com.amap.api.maps.AMap.CommonInfoWindowAdapter
        public InfoWindowParams getInfoWindowParams(BasePointOverlay basePointOverlay) {
            try {
                InfoWindowParams infoWindowParams = new InfoWindowParams();
                if (bh.this.h == null) {
                    bh.this.h = fg.a(bh.this.c, "infowindow_bg.9.png");
                }
                bh.this.e = new LinearLayout(bh.this.c);
                bh.this.e.setBackground(bh.this.h);
                bh.this.f = new TextView(bh.this.c);
                bh.this.f.setText("标题");
                bh.this.f.setTextColor(-16777216);
                bh.this.g = new TextView(bh.this.c);
                bh.this.g.setTextColor(-16777216);
                bh.this.g.setText("内容");
                ((LinearLayout) bh.this.e).setOrientation(1);
                ((LinearLayout) bh.this.e).addView(bh.this.f);
                ((LinearLayout) bh.this.e).addView(bh.this.g);
                infoWindowParams.setInfoWindowType(2);
                infoWindowParams.setInfoWindow(bh.this.e);
                return infoWindowParams;
            } catch (Throwable th) {
                ic.c(th, "InfoWindowDelegate", "showInfoWindow decodeDrawableFromAsset");
                th.printStackTrace();
                return null;
            }
        }
    };

    public bh(Context context) {
        this.c = context;
    }

    public View a(BasePointOverlay basePointOverlay) {
        InfoWindowParams infoWindowParams;
        if (this.a != null) {
            return this.a.getInfoWindow((Marker) basePointOverlay);
        }
        if (this.b != null && (infoWindowParams = this.b.getInfoWindowParams(basePointOverlay)) != null) {
            return infoWindowParams.getInfoWindow();
        }
        InfoWindowParams infoWindowParams2 = this.l.getInfoWindowParams(basePointOverlay);
        if (infoWindowParams2 != null) {
            return infoWindowParams2.getInfoWindow();
        }
        return null;
    }

    public View a(Marker marker) {
        if (this.a == null || !(this.a instanceof AMap.MultiPositionInfoWindowAdapter)) {
            return null;
        }
        return ((AMap.MultiPositionInfoWindowAdapter) this.a).getInfoWindowClick(marker);
    }

    public void a(bg bgVar) {
        synchronized (this) {
            this.i = bgVar;
            if (this.i != null) {
                this.i.a(this);
            }
        }
    }

    public void a(v vVar) throws RemoteException {
        bg d = d();
        if (d != null) {
            d.a(vVar);
        }
    }

    public synchronized void a(AMap.CommonInfoWindowAdapter commonInfoWindowAdapter) {
        this.b = commonInfoWindowAdapter;
        this.a = null;
        if (this.b == null) {
            this.b = this.l;
            this.d = true;
        } else {
            this.d = false;
        }
        if (this.j != null) {
            this.j.a_();
        }
        if (this.i != null) {
            this.i.a_();
        }
    }

    public synchronized void a(AMap.InfoWindowAdapter infoWindowAdapter) {
        this.a = infoWindowAdapter;
        this.b = null;
        if (this.a == null) {
            this.a = this.k;
            this.d = true;
        } else {
            this.d = false;
        }
        if (this.j != null) {
            this.j.a_();
        }
        if (this.i != null) {
            this.i.a_();
        }
    }

    public void a(String str, String str2) {
        if (this.f != null) {
            this.f.requestLayout();
            this.f.setText(str);
        }
        if (this.g != null) {
            this.g.requestLayout();
            this.g.setText(str2);
        }
        if (this.e != null) {
            this.e.requestLayout();
        }
    }

    public synchronized boolean a() {
        return this.d;
    }

    public boolean a(MotionEvent motionEvent) {
        bg d = d();
        if (d != null) {
            return d.a(motionEvent);
        }
        return false;
    }

    public View b(BasePointOverlay basePointOverlay) {
        InfoWindowParams infoWindowParams;
        if (this.a != null) {
            return this.a.getInfoContents((Marker) basePointOverlay);
        }
        if (this.b != null && (infoWindowParams = this.b.getInfoWindowParams(basePointOverlay)) != null) {
            return infoWindowParams.getInfoContents();
        }
        InfoWindowParams infoWindowParams2 = this.l.getInfoWindowParams(basePointOverlay);
        if (infoWindowParams2 != null) {
            return infoWindowParams2.getInfoContents();
        }
        return null;
    }

    public View b(Marker marker) {
        if (this.a == null || !(this.a instanceof AMap.MultiPositionInfoWindowAdapter)) {
            return null;
        }
        return ((AMap.MultiPositionInfoWindowAdapter) this.a).getOverturnInfoWindow(marker);
    }

    public void b() {
        this.c = null;
        this.e = null;
        this.f = null;
        this.g = null;
        synchronized (this) {
            fr.a(this.h);
            this.h = null;
            this.k = null;
            this.a = null;
        }
        this.b = null;
        this.i = null;
        this.j = null;
    }

    public void b(bg bgVar) {
        synchronized (this) {
            this.j = bgVar;
            if (this.j != null) {
                this.j.a(this);
            }
        }
    }

    public long c(BasePointOverlay basePointOverlay) {
        InfoWindowParams infoWindowParams;
        if (this.a != null && (this.a instanceof AMap.ImageInfoWindowAdapter)) {
            return ((AMap.ImageInfoWindowAdapter) this.a).getInfoWindowUpdateTime();
        }
        if (this.b == null || (infoWindowParams = this.b.getInfoWindowParams(basePointOverlay)) == null) {
            return 0L;
        }
        return infoWindowParams.getInfoWindowUpdateTime();
    }

    public View c(Marker marker) {
        if (this.a == null || !(this.a instanceof AMap.MultiPositionInfoWindowAdapter)) {
            return null;
        }
        return ((AMap.MultiPositionInfoWindowAdapter) this.a).getOverturnInfoWindowClick(marker);
    }

    public void c() {
        bg d = d();
        if (d != null) {
            d.b();
        }
    }

    public synchronized bg d() {
        if (this.a != null) {
            if (this.a instanceof AMap.ImageInfoWindowAdapter) {
                return this.j;
            }
            if (this.a instanceof AMap.MultiPositionInfoWindowAdapter) {
                return this.j;
            }
        }
        if (this.b == null || this.b.getInfoWindowParams(null).getInfoWindowType() != 1) {
            return this.i;
        }
        return this.j;
    }

    public void e() {
        bg d = d();
        if (d != null) {
            d.a_();
        }
    }

    public Drawable f() {
        if (this.h == null) {
            try {
                this.h = fg.a(this.c, "infowindow_bg.9.png");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.h;
    }
}
