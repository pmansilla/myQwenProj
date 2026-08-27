package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.amap.api.mapcore.util.ge;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ZoomControllerView.java */
/* loaded from: classes.dex */
public class gi extends LinearLayout {
    private Bitmap a;
    private Bitmap b;
    private Bitmap c;
    private Bitmap d;
    private Bitmap e;
    private Bitmap f;
    private Bitmap g;
    private Bitmap h;
    private Bitmap i;
    private Bitmap j;
    private Bitmap k;
    private Bitmap l;
    private ImageView m;
    private ImageView n;
    private ad o;

    public gi(Context context, ad adVar) {
        super(context);
        this.o = adVar;
        try {
            this.g = fr.a(context, "zoomin_selected.png");
            this.a = fr.a(this.g, w.a);
            this.h = fr.a(context, "zoomin_unselected.png");
            this.b = fr.a(this.h, w.a);
            this.i = fr.a(context, "zoomout_selected.png");
            this.c = fr.a(this.i, w.a);
            this.j = fr.a(context, "zoomout_unselected.png");
            this.d = fr.a(this.j, w.a);
            this.k = fr.a(context, "zoomin_pressed.png");
            this.e = fr.a(this.k, w.a);
            this.l = fr.a(context, "zoomout_pressed.png");
            this.f = fr.a(this.l, w.a);
            this.m = new ImageView(context);
            this.m.setImageBitmap(this.a);
            this.m.setClickable(true);
            this.n = new ImageView(context);
            this.n.setImageBitmap(this.c);
            this.n.setClickable(true);
            this.m.setOnTouchListener(new View.OnTouchListener() { // from class: com.amap.api.mapcore.util.gi.1
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    try {
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    if (gi.this.o.g() < gi.this.o.getMaxZoomLevel() && gi.this.o.isMaploaded()) {
                        if (motionEvent.getAction() == 0) {
                            gi.this.m.setImageBitmap(gi.this.e);
                        } else if (motionEvent.getAction() == 1) {
                            gi.this.m.setImageBitmap(gi.this.a);
                            try {
                                gi.this.o.b(aw.a());
                            } catch (RemoteException e) {
                                ic.c(e, "ZoomControllerView", "zoomin ontouch");
                                e.printStackTrace();
                            }
                        }
                        return false;
                    }
                    return false;
                }
            });
            this.n.setOnTouchListener(new View.OnTouchListener() { // from class: com.amap.api.mapcore.util.gi.2
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    try {
                    } catch (Throwable th) {
                        ic.c(th, "ZoomControllerView", "zoomout ontouch");
                        th.printStackTrace();
                    }
                    if (gi.this.o.g() > gi.this.o.getMinZoomLevel() && gi.this.o.isMaploaded()) {
                        if (motionEvent.getAction() == 0) {
                            gi.this.n.setImageBitmap(gi.this.f);
                        } else if (motionEvent.getAction() == 1) {
                            gi.this.n.setImageBitmap(gi.this.c);
                            gi.this.o.b(aw.b());
                        }
                        return false;
                    }
                    return false;
                }
            });
            this.m.setPadding(0, 0, 20, -2);
            this.n.setPadding(0, 0, 20, 20);
            setOrientation(1);
            addView(this.m);
            addView(this.n);
        } catch (Throwable th) {
            ic.c(th, "ZoomControllerView", "create");
            th.printStackTrace();
        }
    }

    public void a() {
        try {
            removeAllViews();
            this.a.recycle();
            this.b.recycle();
            this.c.recycle();
            this.d.recycle();
            this.e.recycle();
            this.f.recycle();
            this.a = null;
            this.b = null;
            this.c = null;
            this.d = null;
            this.e = null;
            this.f = null;
            if (this.g != null) {
                this.g.recycle();
                this.g = null;
            }
            if (this.h != null) {
                this.h.recycle();
                this.h = null;
            }
            if (this.i != null) {
                this.i.recycle();
                this.i = null;
            }
            if (this.j != null) {
                this.j.recycle();
                this.g = null;
            }
            if (this.k != null) {
                this.k.recycle();
                this.k = null;
            }
            if (this.l != null) {
                this.l.recycle();
                this.l = null;
            }
            this.m = null;
            this.n = null;
        } catch (Throwable th) {
            ic.c(th, "ZoomControllerView", "destory");
            th.printStackTrace();
        }
    }

    public void a(float f) {
        try {
            if (f < this.o.getMaxZoomLevel() && f > this.o.getMinZoomLevel()) {
                this.m.setImageBitmap(this.a);
                this.n.setImageBitmap(this.c);
            } else if (f == this.o.getMinZoomLevel()) {
                this.n.setImageBitmap(this.d);
                this.m.setImageBitmap(this.a);
            } else if (f == this.o.getMaxZoomLevel()) {
                this.m.setImageBitmap(this.b);
                this.n.setImageBitmap(this.c);
            }
        } catch (Throwable th) {
            ic.c(th, "ZoomControllerView", "setZoomBitmap");
            th.printStackTrace();
        }
    }

    public void a(int i) {
        try {
            ge.a aVar = (ge.a) getLayoutParams();
            if (i == 1) {
                aVar.d = 16;
            } else if (i == 2) {
                aVar.d = 80;
            }
            setLayoutParams(aVar);
        } catch (Throwable th) {
            ic.c(th, "ZoomControllerView", "setZoomPosition");
            th.printStackTrace();
        }
    }

    public void a(boolean z) {
        if (z) {
            setVisibility(0);
        } else {
            setVisibility(8);
        }
    }
}
