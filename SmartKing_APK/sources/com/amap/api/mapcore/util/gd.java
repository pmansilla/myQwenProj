package com.amap.api.mapcore.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.amap.api.maps.model.LatLng;

/* compiled from: LocationView.java */
/* loaded from: classes.dex */
public class gd extends LinearLayout {
    Bitmap a;
    Bitmap b;
    Bitmap c;
    Bitmap d;
    Bitmap e;
    Bitmap f;
    ImageView g;
    ad h;
    boolean i;

    @SuppressLint({"ClickableViewAccessibility"})
    public gd(Context context, ad adVar) {
        super(context);
        this.i = false;
        this.h = adVar;
        try {
            this.d = fr.a(context, "location_selected.png");
            this.a = fr.a(this.d, w.a);
            this.e = fr.a(context, "location_pressed.png");
            this.b = fr.a(this.e, w.a);
            this.f = fr.a(context, "location_unselected.png");
            this.c = fr.a(this.f, w.a);
            this.g = new ImageView(context);
            this.g.setImageBitmap(this.a);
            this.g.setClickable(true);
            this.g.setPadding(0, 20, 20, 0);
            this.g.setOnTouchListener(new View.OnTouchListener() { // from class: com.amap.api.mapcore.util.gd.1
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (!gd.this.i) {
                        return false;
                    }
                    if (motionEvent.getAction() == 0) {
                        gd.this.g.setImageBitmap(gd.this.b);
                    } else if (motionEvent.getAction() == 1) {
                        try {
                            gd.this.g.setImageBitmap(gd.this.a);
                            gd.this.h.setMyLocationEnabled(true);
                            Location myLocation = gd.this.h.getMyLocation();
                            if (myLocation == null) {
                                return false;
                            }
                            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                            gd.this.h.a(myLocation);
                            gd.this.h.a(aw.a(latLng, gd.this.h.g()));
                        } catch (Throwable th) {
                            ic.c(th, "LocationView", "onTouch");
                            th.printStackTrace();
                        }
                    }
                    return false;
                }
            });
            addView(this.g);
        } catch (Throwable th) {
            ic.c(th, "LocationView", "create");
            th.printStackTrace();
        }
    }

    public void a() {
        try {
            removeAllViews();
            if (this.a != null) {
                this.a.recycle();
            }
            if (this.b != null) {
                this.b.recycle();
            }
            if (this.b != null) {
                this.c.recycle();
            }
            this.a = null;
            this.b = null;
            this.c = null;
            if (this.d != null) {
                this.d.recycle();
                this.d = null;
            }
            if (this.e != null) {
                this.e.recycle();
                this.e = null;
            }
            if (this.f != null) {
                this.f.recycle();
                this.f = null;
            }
        } catch (Throwable th) {
            ic.c(th, "LocationView", "destroy");
            th.printStackTrace();
        }
    }

    public void a(boolean z) {
        this.i = z;
        try {
            if (z) {
                this.g.setImageBitmap(this.a);
            } else {
                this.g.setImageBitmap(this.c);
            }
            this.g.invalidate();
        } catch (Throwable th) {
            ic.c(th, "LocationView", "showSelect");
            th.printStackTrace();
        }
    }
}
