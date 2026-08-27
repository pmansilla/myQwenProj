package com.amap.api.mapcore.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.WindowManager;
import com.amap.api.maps.model.Marker;

/* compiled from: SensorEventHelper.java */
/* loaded from: classes.dex */
public class ap implements SensorEventListener {
    private SensorManager a;
    private Sensor b;
    private float d;
    private Context e;
    private ad f;
    private Marker g;
    private long c = 0;
    private boolean h = true;

    public ap(Context context, ad adVar) {
        this.e = context.getApplicationContext();
        this.f = adVar;
        try {
            this.a = (SensorManager) context.getSystemService("sensor");
            if (this.a != null) {
                this.b = this.a.getDefaultSensor(3);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static int a(Context context) {
        WindowManager windowManager;
        if (context != null && (windowManager = (WindowManager) context.getSystemService("window")) != null) {
            switch (windowManager.getDefaultDisplay().getRotation()) {
                case 0:
                    return 0;
                case 1:
                    return 90;
                case 2:
                    return 180;
                case 3:
                    return -90;
            }
        }
        return 0;
    }

    public void a() {
        if (this.a == null || this.b == null) {
            return;
        }
        this.a.registerListener(this, this.b, 3);
    }

    public void a(Marker marker) {
        this.g = marker;
    }

    public void a(boolean z) {
        this.h = z;
    }

    public void b() {
        if (this.a == null || this.b == null) {
            return;
        }
        this.a.unregisterListener(this, this.b);
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        try {
            if (System.currentTimeMillis() - this.c < 100) {
                return;
            }
            if ((this.f.a() == null || this.f.a().getAnimateionsCount() <= 0) && sensorEvent.sensor.getType() == 3) {
                float a = (sensorEvent.values[0] + a(this.e)) % 360.0f;
                if (a > 180.0f) {
                    a -= 360.0f;
                } else if (a < -180.0f) {
                    a += 360.0f;
                }
                if (Math.abs(this.d - a) < 3.0f) {
                    return;
                }
                if (Float.isNaN(a)) {
                    a = 0.0f;
                }
                this.d = a;
                if (this.g != null) {
                    try {
                        if (this.h) {
                            this.f.a(aw.d(this.d));
                            this.g.setRotateAngle(-this.d);
                        } else {
                            this.g.setRotateAngle(360.0f - this.d);
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                this.c = System.currentTimeMillis();
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }
}
