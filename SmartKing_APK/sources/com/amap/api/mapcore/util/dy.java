package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.os.RemoteException;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.particle.ColorGenerate;
import com.amap.api.maps.model.particle.ParticleEmissionModule;
import com.amap.api.maps.model.particle.ParticleOverLifeModule;
import com.amap.api.maps.model.particle.ParticleOverlayOptions;
import com.amap.api.maps.model.particle.ParticleShapeModule;
import com.amap.api.maps.model.particle.VelocityGenerate;
import com.autonavi.amap.mapcore.AMapNativeParticleSystem;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.util.ArrayList;
import java.util.List;

/* compiled from: ParticleLayerDelegateImp.java */
/* loaded from: classes.dex */
public class dy implements dq {
    private ef e;
    private ab f;
    private String i;
    private BitmapDescriptor j;
    private long d = 0;
    private boolean g = true;
    private float h = 1.0f;
    private boolean k = false;
    private List<am> l = new ArrayList();
    private int m = 0;
    private ParticleOverlayOptions n = new ParticleOverlayOptions();
    private boolean o = false;
    float a = 1.0f;
    int b = 0;
    int c = 0;
    private float p = -1.0f;
    private float q = -1.0f;
    private float[] r = new float[16];
    private float[] s = new float[16];
    private float[] t = new float[16];

    public dy(ab abVar) {
        this.f = abVar;
    }

    private int a(boolean z, BitmapDescriptor bitmapDescriptor) {
        am amVar;
        e();
        if (z) {
            amVar = this.f.a(bitmapDescriptor);
            if (amVar != null) {
                int k = amVar.k();
                a(amVar);
                return k;
            }
        } else {
            amVar = null;
        }
        int i = 0;
        if (amVar == null) {
            amVar = new am(bitmapDescriptor, 0);
        }
        Bitmap bitmap = bitmapDescriptor.getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            i = f();
            amVar.a(i);
            if (z) {
                this.f.g().a(amVar);
            }
            a(amVar);
            fr.b(i, bitmap, true);
        }
        return i;
    }

    private void a(am amVar) {
        if (amVar != null) {
            this.l.add(amVar);
            amVar.l();
        }
    }

    private int d() {
        if (this.k) {
            return this.m;
        }
        int a = a(Build.VERSION.SDK_INT >= 12, this.j);
        this.k = true;
        return a;
    }

    private void e() {
        if (this.l != null) {
            for (am amVar : this.l) {
                if (amVar != null && this.f != null) {
                    this.f.a(amVar);
                }
            }
            this.l.clear();
        }
    }

    private int f() {
        int[] iArr = {0};
        GLES20.glGenTextures(1, iArr, 0);
        return iArr[0];
    }

    private void g() {
        if (this.d != 0) {
            a(this.n.getMaxParticles());
            a(this.n.getDuration());
            a(this.n.isLoop());
            b(true);
            b(this.n.getParticleLifeTime());
            a(this.n.getParticleStartSpeed());
            if (this.n.getParticleEmissionModule() != null) {
                a(this.n.getParticleEmissionModule());
            }
            if (this.n.getParticleShapeModule() != null) {
                a(this.n.getParticleShapeModule());
            }
            if (this.n.getParticleStartColor() != null) {
                a(this.n.getParticleStartColor());
            }
            if (this.n.getParticleOverLifeModule() != null) {
                a(this.n.getParticleOverLifeModule());
            }
            a(this.n.getStartParticleW(), this.n.getstartParticleH());
        }
    }

    @Override // com.amap.api.mapcore.util.dq
    public void a(int i) {
        if (this.n != null) {
            this.n.setMaxParticles(i);
        }
        if (this.d != 0) {
            AMapNativeParticleSystem.setMaxParticles(this.d, i);
        } else if (this.n != null) {
            synchronized (this) {
                this.o = true;
            }
        }
    }

    @Override // com.amap.api.mapcore.util.dq
    public void a(int i, int i2) {
        if (this.n != null) {
            this.n.setStartParticleSize(i, i2);
        }
        if (this.d != 0) {
            AMapNativeParticleSystem.setStartParticleSize(this.d, i, i2);
        }
    }

    @Override // com.amap.api.mapcore.util.dq
    public void a(long j) {
        if (this.n != null) {
            this.n.setDuration(j);
        }
        if (this.d != 0) {
            AMapNativeParticleSystem.setDuration(this.d, j);
        } else if (this.n != null) {
            synchronized (this) {
                this.o = true;
            }
        }
    }

    public void a(BitmapDescriptor bitmapDescriptor) {
        if (bitmapDescriptor == null) {
            return;
        }
        synchronized (this) {
            if (bitmapDescriptor.equals(this.j)) {
                return;
            }
            this.k = false;
            this.j = bitmapDescriptor;
        }
    }

    @Override // com.amap.api.mapcore.util.dq
    public void a(ColorGenerate colorGenerate) {
        if (this.n != null) {
            this.n.setParticleStartColor(colorGenerate);
        }
        if (this.d != 0 && colorGenerate != null) {
            if (colorGenerate.getNativeInstance() == 0) {
                colorGenerate.createNativeInstace();
            }
            AMapNativeParticleSystem.setStartColor(this.d, colorGenerate.getNativeInstance());
        } else if (this.n != null) {
            synchronized (this) {
                this.o = true;
            }
        }
    }

    @Override // com.amap.api.mapcore.util.dq
    public void a(ParticleEmissionModule particleEmissionModule) {
        if (this.n != null) {
            this.n.setParticleEmissionModule(particleEmissionModule);
        }
        if (this.d != 0 && particleEmissionModule != null) {
            if (particleEmissionModule.getNativeInstance() == 0) {
                particleEmissionModule.createNativeInstace();
            }
            AMapNativeParticleSystem.setParticleEmission(this.d, particleEmissionModule.getNativeInstance());
        } else if (this.n != null) {
            synchronized (this) {
                this.o = true;
            }
        }
    }

    @Override // com.amap.api.mapcore.util.dq
    public void a(ParticleOverLifeModule particleOverLifeModule) {
        if (this.n != null) {
            this.n.setParticleOverLifeModule(particleOverLifeModule);
        }
        if (this.d != 0 && particleOverLifeModule != null) {
            if (particleOverLifeModule.getNativeInstance() == 0) {
                particleOverLifeModule.createNativeInstace();
            }
            AMapNativeParticleSystem.setParticleOverLifeModule(this.d, particleOverLifeModule.getNativeInstance());
        } else if (this.n != null) {
            synchronized (this) {
                this.o = true;
            }
        }
    }

    public void a(ParticleOverlayOptions particleOverlayOptions) {
        synchronized (this) {
            if (particleOverlayOptions != null) {
                try {
                    a(particleOverlayOptions.getIcon());
                    this.n.setMaxParticles(particleOverlayOptions.getMaxParticles());
                    this.n.setLoop(particleOverlayOptions.isLoop());
                    this.n.setDuration(particleOverlayOptions.getDuration());
                    this.n.setParticleLifeTime(particleOverlayOptions.getParticleLifeTime());
                    this.n.setParticleEmissionModule(particleOverlayOptions.getParticleEmissionModule());
                    this.n.setParticleShapeModule(particleOverlayOptions.getParticleShapeModule());
                    this.n.setParticleStartSpeed(particleOverlayOptions.getParticleStartSpeed());
                    this.n.setParticleStartColor(particleOverlayOptions.getParticleStartColor());
                    this.n.setParticleOverLifeModule(particleOverlayOptions.getParticleOverLifeModule());
                    this.n.setStartParticleSize(particleOverlayOptions.getStartParticleW(), particleOverlayOptions.getstartParticleH());
                    this.n.zIndex(particleOverlayOptions.getZIndex());
                    this.h = this.n.getZIndex();
                    this.n.setVisible(particleOverlayOptions.isVisibile());
                    this.g = this.n.isVisibile();
                    this.o = true;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    @Override // com.amap.api.mapcore.util.dq
    public void a(ParticleShapeModule particleShapeModule) {
        if (this.n != null) {
            this.n.setParticleShapeModule(particleShapeModule);
        }
        if (this.d != 0 && particleShapeModule != null) {
            if (particleShapeModule.getNativeInstance() == 0) {
                particleShapeModule.createNativeInstace();
            }
            AMapNativeParticleSystem.setParticleShapeModule(this.d, particleShapeModule.getNativeInstance());
        } else if (this.n != null) {
            synchronized (this) {
                this.o = true;
            }
        }
    }

    @Override // com.amap.api.mapcore.util.dq
    public void a(VelocityGenerate velocityGenerate) {
        if (this.n != null) {
            this.n.setParticleStartSpeed(velocityGenerate);
        }
        if (this.d != 0 && velocityGenerate != null) {
            if (velocityGenerate.getNativeInstance() == 0) {
                velocityGenerate.createNativeInstace();
            }
            AMapNativeParticleSystem.setParticleStartSpeed(this.d, velocityGenerate.getNativeInstance());
        } else if (this.n != null) {
            synchronized (this) {
                this.o = true;
            }
        }
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public void a(MapConfig mapConfig) throws RemoteException {
        float f;
        int i;
        if (this.e == null) {
            this.e = this.f.b();
        }
        if (this.e == null) {
            return;
        }
        if (this.d == 0) {
            this.d = AMapNativeParticleSystem.nativeCreate();
            if (this.d != 0 && this.e != null) {
                AMapNativeParticleSystem.nativeSetGLShaderManager(this.d, this.e.a());
            }
        }
        if (this.d != 0) {
            synchronized (this) {
                if (this.o) {
                    g();
                    this.o = false;
                }
            }
            this.m = d();
            if (this.m == 0) {
                return;
            }
            AMapNativeParticleSystem.nativeSetTextureId(this.d, this.m);
            if (this.f != null) {
                this.f.a(false);
            }
            if (this.b != mapConfig.getMapWidth() || this.c != mapConfig.getMapHeight()) {
                this.b = mapConfig.getMapWidth();
                this.c = mapConfig.getMapHeight();
                if (this.b > this.c) {
                    f = this.b;
                    i = this.c;
                } else {
                    f = this.c;
                    i = this.b;
                }
                this.a = f / i;
                if (this.b > this.c) {
                    this.p = -this.a;
                    this.q = 1.0f;
                } else {
                    this.p = -1.0f;
                    this.q = this.a;
                }
                Matrix.orthoM(this.r, 0, this.p, -this.p, -this.q, this.q, 3.0f, 7.0f);
                Matrix.setLookAtM(this.s, 0, 0.0f, 0.0f, 3.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
            }
            Matrix.multiplyMM(this.t, 0, this.r, 0, this.s, 0);
            Matrix.translateM(this.t, 0, this.p, this.q, 0.0f);
            Matrix.scaleM(this.t, 0, Math.abs(this.p * 2.0f) / this.b, Math.abs(this.q * 2.0f) / this.c, 0.0f);
            AMapNativeParticleSystem.nativeRender(this.d, (float[]) this.t.clone(), mapConfig.getProjectionMatrix(), mapConfig.getSX(), mapConfig.getSY(), mapConfig.getSZ(), this.b, this.c);
        }
    }

    @Override // com.amap.api.mapcore.util.dq
    public void a(boolean z) {
        if (this.n != null) {
            this.n.setLoop(z);
        }
        if (this.d != 0) {
            AMapNativeParticleSystem.setLoop(this.d, z);
        } else if (this.n != null) {
            synchronized (this) {
                this.o = true;
            }
        }
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean a() {
        return true;
    }

    @Override // com.amap.api.mapcore.util.dq
    public int b() {
        if (this.d != 0) {
            return AMapNativeParticleSystem.getCurrentParticleNum(this.d);
        }
        return 0;
    }

    @Override // com.amap.api.mapcore.util.dq
    public void b(long j) {
        if (this.n != null) {
            this.n.setParticleLifeTime(j);
        }
        if (this.d != 0) {
            AMapNativeParticleSystem.setParticleLifeTime(this.d, j);
        } else if (this.n != null) {
            synchronized (this) {
                this.o = true;
            }
        }
    }

    public void b(boolean z) {
        if (this.d != 0) {
            AMapNativeParticleSystem.setPreWram(this.d, z);
        }
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean c() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        Bitmap bitmap;
        if (this.l != null && this.l.size() > 0) {
            for (int i = 0; i < this.l.size(); i++) {
                am amVar = this.l.get(i);
                if (amVar != null) {
                    if (this.f != null) {
                        this.f.a(amVar);
                    }
                    if (this.f.g() != null) {
                        this.f.g().c(amVar.o());
                    }
                }
            }
            this.l.clear();
        }
        if (this.j != null && (bitmap = this.j.getBitmap()) != null) {
            bitmap.recycle();
            this.j = null;
        }
        if (this.d != 0) {
            AMapNativeParticleSystem.nativeDestroy(this.d);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.i == null) {
            this.i = this.f.a("Particle");
        }
        return this.i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public int hashCodeRemote() throws RemoteException {
        return 0;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() throws RemoteException {
        return this.g;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) throws RemoteException {
        this.g = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.h = f;
    }
}
