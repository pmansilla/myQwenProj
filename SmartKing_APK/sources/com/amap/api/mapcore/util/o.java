package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.amap.api.mapcore.util.x;
import com.autonavi.ae.gmap.GLMapRender;

/* compiled from: AMapGLTextureView.java */
/* loaded from: classes.dex */
public class o extends x implements ae {
    protected boolean a;
    private ad b;
    private GLMapRender c;

    public o(Context context) {
        this(context, null);
    }

    public o(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.b = null;
        this.c = null;
        this.a = false;
        ez.a(this, 5, 6, 5, 0, 16, 8);
        this.b = new l(this, context, attributeSet);
    }

    public ad a() {
        return this.b;
    }

    @Override // com.amap.api.mapcore.util.ae
    public void a(ex exVar) {
        super.a((x.e) exVar);
    }

    @Override // com.amap.api.mapcore.util.ae
    public void a(ey eyVar) {
        super.a((x.f) eyVar);
    }

    @Override // com.amap.api.mapcore.util.ae
    public void b() {
        c();
        try {
            if (this.c != null) {
                this.c.onDetachedFromWindow();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        super.onDetachedFromWindow();
    }

    @Override // com.amap.api.mapcore.util.x
    public void c() {
        if (!this.c.mSurfacedestoryed) {
            queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.o.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (o.this.c != null) {
                            o.this.c.onSurfaceDestory();
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            });
            int i = 0;
            while (!this.c.mSurfacedestoryed) {
                int i2 = i + 1;
                if (i >= 20) {
                    break;
                }
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException unused) {
                }
                i = i2;
            }
        }
        super.c();
    }

    @Override // com.amap.api.mapcore.util.x
    public void d() {
        super.d();
    }

    @Override // com.amap.api.mapcore.util.ae
    public SurfaceHolder getHolder() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.x, android.view.TextureView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            if (this.c != null) {
                this.c.onAttachedToWindow();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        d();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.x, android.view.View
    public void onDetachedFromWindow() {
    }

    @Override // com.amap.api.mapcore.util.x, android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        requestRender();
        try {
            Thread.sleep(100L);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return super.onSurfaceTextureDestroyed(surfaceTexture);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        try {
            return this.b.onTouchEvent(motionEvent);
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        try {
            if (i == 8 || i == 4) {
                if (this.c != null) {
                    this.c.renderPause();
                    this.a = false;
                }
                requestRender();
                return;
            }
            if (i != 0 || this.c == null) {
                return;
            }
            this.c.renderResume();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.x, com.amap.api.mapcore.util.ae
    public void setRenderer(GLSurfaceView.Renderer renderer) {
        this.c = (GLMapRender) renderer;
        super.setRenderer(renderer);
    }
}
