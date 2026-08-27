package com.loc;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

/* compiled from: AmapSensorManager.java */
/* loaded from: classes.dex */
public final class eb implements SensorEventListener {
    SensorManager a;
    Sensor b;
    Sensor c;
    Sensor d;
    private Context s;
    public boolean e = false;
    public double f = 0.0d;
    public float g = 0.0f;
    private float t = 1013.25f;
    private float u = 0.0f;
    Handler h = new Handler() { // from class: com.loc.eb.1
    };
    double i = 0.0d;
    double j = 0.0d;
    double k = 0.0d;
    double l = 0.0d;
    double[] m = new double[3];
    volatile double n = 0.0d;
    long o = 0;
    long p = 0;
    final int q = 100;
    final int r = 30;

    public eb(Context context) {
        this.s = null;
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        try {
            this.s = context;
            if (this.a == null) {
                this.a = (SensorManager) this.s.getSystemService("sensor");
            }
            try {
                this.b = this.a.getDefaultSensor(6);
            } catch (Throwable unused) {
            }
            try {
                this.c = this.a.getDefaultSensor(11);
            } catch (Throwable unused2) {
            }
            try {
                this.d = this.a.getDefaultSensor(1);
            } catch (Throwable unused3) {
            }
        } catch (Throwable th) {
            es.a(th, "AMapSensorManager", "<init>");
        }
    }

    public final void a() {
        if (this.a == null || this.e) {
            return;
        }
        this.e = true;
        try {
            if (this.b != null) {
                this.a.registerListener(this, this.b, 3, this.h);
            }
        } catch (Throwable th) {
            es.a(th, "AMapSensorManager", "registerListener mPressure");
        }
        try {
            if (this.c != null) {
                this.a.registerListener(this, this.c, 3, this.h);
            }
        } catch (Throwable th2) {
            es.a(th2, "AMapSensorManager", "registerListener mRotationVector");
        }
        try {
            if (this.d != null) {
                this.a.registerListener(this, this.d, 3, this.h);
            }
        } catch (Throwable th3) {
            es.a(th3, "AMapSensorManager", "registerListener mAcceleroMeterVector");
        }
    }

    public final void b() {
        if (this.a == null || !this.e) {
            return;
        }
        this.e = false;
        try {
            if (this.b != null) {
                this.a.unregisterListener(this, this.b);
            }
        } catch (Throwable unused) {
        }
        try {
            if (this.c != null) {
                this.a.unregisterListener(this, this.c);
            }
        } catch (Throwable unused2) {
        }
        try {
            if (this.d != null) {
                this.a.unregisterListener(this, this.d);
            }
        } catch (Throwable unused3) {
        }
    }

    public final float c() {
        return this.u;
    }

    public final double d() {
        return this.l;
    }

    public final void e() {
        try {
            b();
            this.b = null;
            this.c = null;
            this.a = null;
            this.d = null;
            this.e = false;
        } catch (Throwable th) {
            es.a(th, "AMapSensorManager", "destroy");
        }
    }

    @Override // android.hardware.SensorEventListener
    public final void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // android.hardware.SensorEventListener
    public final void onSensorChanged(SensorEvent sensorEvent) {
        float[] fArr;
        if (sensorEvent == null) {
            return;
        }
        try {
            int type = sensorEvent.sensor.getType();
            if (type != 1) {
                if (type == 6) {
                    try {
                        if (this.b != null) {
                            float[] fArr2 = (float[]) sensorEvent.values.clone();
                            if (fArr2 != null) {
                                this.g = fArr2[0];
                            }
                            if (fArr2 != null) {
                                this.f = fa.a(SensorManager.getAltitude(this.t, fArr2[0]));
                                return;
                            }
                            return;
                        }
                        return;
                    } catch (Throwable unused) {
                        return;
                    }
                }
                if (type != 11) {
                    return;
                }
                try {
                    if (this.c == null || (fArr = (float[]) sensorEvent.values.clone()) == null) {
                        return;
                    }
                    float[] fArr3 = new float[9];
                    SensorManager.getRotationMatrixFromVector(fArr3, fArr);
                    SensorManager.getOrientation(fArr3, new float[3]);
                    this.u = (float) Math.toDegrees(r12[0]);
                    this.u = (float) Math.floor(this.u > 0.0f ? this.u : this.u + 360.0f);
                    return;
                } catch (Throwable unused2) {
                    return;
                }
            }
            if (this.d != null) {
                float[] fArr4 = (float[]) sensorEvent.values.clone();
                double[] dArr = this.m;
                double d = this.m[0] * 0.800000011920929d;
                double d2 = fArr4[0] * 0.19999999f;
                Double.isNaN(d2);
                dArr[0] = d + d2;
                double[] dArr2 = this.m;
                double d3 = this.m[1] * 0.800000011920929d;
                double d4 = fArr4[1] * 0.19999999f;
                Double.isNaN(d4);
                dArr2[1] = d3 + d4;
                double[] dArr3 = this.m;
                double d5 = this.m[2] * 0.800000011920929d;
                double d6 = fArr4[2] * 0.19999999f;
                Double.isNaN(d6);
                dArr3[2] = d5 + d6;
                double d7 = fArr4[0];
                double d8 = this.m[0];
                Double.isNaN(d7);
                this.i = d7 - d8;
                double d9 = fArr4[1];
                double d10 = this.m[1];
                Double.isNaN(d9);
                this.j = d9 - d10;
                double d11 = fArr4[2];
                double d12 = this.m[2];
                Double.isNaN(d11);
                this.k = d11 - d12;
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - this.o >= 100) {
                    double sqrt = Math.sqrt((this.i * this.i) + (this.j * this.j) + (this.k * this.k));
                    this.p++;
                    this.o = currentTimeMillis;
                    this.n += sqrt;
                    if (this.p >= 30) {
                        double d13 = this.n;
                        double d14 = this.p;
                        Double.isNaN(d14);
                        this.l = d13 / d14;
                        this.n = 0.0d;
                        this.p = 0L;
                    }
                }
            }
        } catch (Throwable unused3) {
        }
    }
}
