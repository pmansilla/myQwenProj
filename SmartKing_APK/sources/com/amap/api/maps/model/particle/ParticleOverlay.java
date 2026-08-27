package com.amap.api.maps.model.particle;

import android.text.TextUtils;
import com.amap.api.mapcore.util.dq;
import com.amap.api.maps.interfaces.IGlOverlayLayer;
import com.amap.api.maps.model.BaseOverlay;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class ParticleOverlay extends BaseOverlay {
    private WeakReference<IGlOverlayLayer> glOverlayLayerRef;
    private ParticleOverlayOptions options;
    private dq overlayDelegate;
    private String overlayName;

    public ParticleOverlay(dq dqVar) {
        this.overlayDelegate = dqVar;
    }

    public ParticleOverlay(IGlOverlayLayer iGlOverlayLayer, ParticleOverlayOptions particleOverlayOptions) {
        this.glOverlayLayerRef = new WeakReference<>(iGlOverlayLayer);
        this.options = particleOverlayOptions;
        this.overlayName = "";
    }

    private void a() {
        IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
        if (TextUtils.isEmpty(this.overlayName) || iGlOverlayLayer == null) {
            return;
        }
        iGlOverlayLayer.updateOption(this.overlayName, this.options);
    }

    public void destroy() {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.destroy();
            } else {
                IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
                if (iGlOverlayLayer != null) {
                    iGlOverlayLayer.removeOverlay(this.overlayName);
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public int getCurrentParticleNum() {
        try {
            if (this.overlayDelegate != null) {
                return this.overlayDelegate.b();
            }
            IGlOverlayLayer iGlOverlayLayer = this.glOverlayLayerRef.get();
            if (iGlOverlayLayer != null) {
                return iGlOverlayLayer.getCurrentParticleNum(this.overlayName);
            }
            return 0;
        } catch (Throwable th) {
            th.printStackTrace();
            return 0;
        }
    }

    public void setDuration(long j) {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.a(j);
            } else if (this.options != null) {
                this.options.setDuration(j);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setLoop(boolean z) {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.a(z);
            } else if (this.options != null) {
                this.options.setLoop(z);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setMaxParticles(int i) {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.a(i);
            } else if (this.options != null) {
                this.options.setMaxParticles(i);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setParticleEmission(ParticleEmissionModule particleEmissionModule) {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.a(particleEmissionModule);
            } else if (this.options != null) {
                this.options.setParticleEmissionModule(particleEmissionModule);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setParticleLifeTime(long j) {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.b(j);
            } else if (this.options != null) {
                this.options.setParticleLifeTime(j);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setParticleOverLifeModule(ParticleOverLifeModule particleOverLifeModule) {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.a(particleOverLifeModule);
            } else if (this.options != null) {
                this.options.setParticleOverLifeModule(particleOverLifeModule);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setParticleShapeModule(ParticleShapeModule particleShapeModule) {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.a(particleShapeModule);
            } else if (this.options != null) {
                this.options.setParticleShapeModule(particleShapeModule);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setParticleStartSpeed(VelocityGenerate velocityGenerate) {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.a(velocityGenerate);
            } else if (this.options != null) {
                this.options.setParticleStartSpeed(velocityGenerate);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setStartColor(ColorGenerate colorGenerate) {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.a(colorGenerate);
            } else if (this.options != null) {
                this.options.setParticleStartColor(colorGenerate);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setStartParticleSize(int i, int i2) {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.a(i, i2);
            } else if (this.options != null) {
                this.options.setStartParticleSize(i, i2);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setVisible(boolean z) {
        try {
            if (this.overlayDelegate != null) {
                this.overlayDelegate.setVisible(z);
            } else if (this.options != null) {
                this.options.setVisible(z);
                a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
