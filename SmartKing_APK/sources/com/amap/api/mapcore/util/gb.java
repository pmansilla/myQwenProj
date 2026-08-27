package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.amap.api.maps.model.CameraPosition;

/* compiled from: CompassView.java */
/* loaded from: classes.dex */
public class gb extends LinearLayout {
    Bitmap a;
    Bitmap b;
    Bitmap c;
    ImageView d;
    ad e;
    Matrix f;

    public gb(Context context, ad adVar) {
        super(context);
        this.f = new Matrix();
        this.e = adVar;
        try {
            this.c = fr.a(context, "maps_dav_compass_needle_large.png");
            this.b = fr.a(this.c, w.a * 0.8f);
            this.c = fr.a(this.c, w.a * 0.7f);
            if (this.b != null && this.c != null) {
                this.a = Bitmap.createBitmap(this.b.getWidth(), this.b.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(this.a);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                canvas.drawBitmap(this.c, (this.b.getWidth() - this.c.getWidth()) / 2.0f, (this.b.getHeight() - this.c.getHeight()) / 2.0f, paint);
                this.d = new ImageView(context);
                this.d.setScaleType(ImageView.ScaleType.MATRIX);
                this.d.setImageBitmap(this.a);
                this.d.setClickable(true);
                b();
                this.d.setOnTouchListener(new View.OnTouchListener() { // from class: com.amap.api.mapcore.util.gb.1
                    @Override // android.view.View.OnTouchListener
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        try {
                        } catch (Throwable th) {
                            ic.c(th, "CompassView", "onTouch");
                            th.printStackTrace();
                        }
                        if (!gb.this.e.isMaploaded()) {
                            return false;
                        }
                        if (motionEvent.getAction() == 0) {
                            gb.this.d.setImageBitmap(gb.this.b);
                        } else if (motionEvent.getAction() == 1) {
                            gb.this.d.setImageBitmap(gb.this.a);
                            CameraPosition cameraPosition = gb.this.e.getCameraPosition();
                            gb.this.e.b(aw.a(new CameraPosition(cameraPosition.target, cameraPosition.zoom, 0.0f, 0.0f)));
                        }
                        return false;
                    }
                });
                addView(this.d);
            }
        } catch (Throwable th) {
            ic.c(th, "CompassView", "create");
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
            if (this.c != null) {
                this.c.recycle();
            }
            if (this.f != null) {
                this.f.reset();
                this.f = null;
            }
            this.c = null;
            this.a = null;
            this.b = null;
        } catch (Throwable th) {
            ic.c(th, "CompassView", "destroy");
            th.printStackTrace();
        }
    }

    public void a(boolean z) {
        if (!z) {
            setVisibility(8);
        } else {
            setVisibility(0);
            b();
        }
    }

    public void b() {
        try {
            if (this.e == null || this.d == null) {
                return;
            }
            float o = this.e.o(1);
            float n = this.e.n(1);
            if (this.f == null) {
                this.f = new Matrix();
            }
            this.f.reset();
            this.f.postRotate(-n, this.d.getDrawable().getBounds().width() / 2.0f, this.d.getDrawable().getBounds().height() / 2.0f);
            Matrix matrix = this.f;
            double d = o;
            Double.isNaN(d);
            matrix.postScale(1.0f, (float) Math.cos((d * 3.141592653589793d) / 180.0d), this.d.getDrawable().getBounds().width() / 2.0f, this.d.getDrawable().getBounds().height() / 2.0f);
            this.d.setImageMatrix(this.f);
        } catch (Throwable th) {
            ic.c(th, "CompassView", "invalidateAngle");
            th.printStackTrace();
        }
    }
}
