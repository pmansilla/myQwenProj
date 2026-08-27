package com.amap.api.maps.model.particle;

import android.os.Parcel;
import android.os.Parcelable;
import com.autonavi.amap.mapcore.AMapNativeParticleSystem;
import com.autonavi.amap.mapcore.AbstractNativeInstance;

/* loaded from: classes.dex */
public class ParticleOverLifeModule extends AbstractNativeInstance implements Parcelable {
    public static final Parcelable.Creator<ParticleOverLifeModule> CREATOR = new Parcelable.Creator<ParticleOverLifeModule>() { // from class: com.amap.api.maps.model.particle.ParticleOverLifeModule.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public ParticleOverLifeModule createFromParcel(Parcel parcel) {
            return new ParticleOverLifeModule(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public ParticleOverLifeModule[] newArray(int i) {
            return new ParticleOverLifeModule[i];
        }
    };
    private static final int TYPE_COLOR = 3;
    private static final int TYPE_ROTATE = 1;
    private static final int TYPE_SCALE = 2;
    private static final int TYPE_VEL = 0;
    private ColorGenerate colorGenerate;
    private RotationOverLife rotateOverLife;
    private SizeOverLife sizeOverLife;
    private VelocityGenerate overLife = null;
    private VelocityGenerate velocityOverLife = null;
    private boolean isNeedReloadVelocityGenerate = false;
    private boolean isNeedReloadRotationOverLife = false;
    private boolean isNeedReloadSizeOverLife = false;
    private boolean isNeedReloadColorGenerate = false;

    public ParticleOverLifeModule() {
        createNativeInstace();
    }

    protected ParticleOverLifeModule(Parcel parcel) {
        this.nativeInstance = parcel.readLong();
    }

    @Override // com.autonavi.amap.mapcore.AbstractNativeInstance
    public void createNativeInstace() {
        try {
            this.nativeInstance = AMapNativeParticleSystem.nativeCreateParticleOverLifeModule();
            if (this.isNeedReloadVelocityGenerate) {
                setVelocityOverLife(this.overLife);
                this.isNeedReloadVelocityGenerate = false;
            }
            if (this.isNeedReloadRotationOverLife) {
                setRotateOverLife(this.rotateOverLife);
                this.isNeedReloadRotationOverLife = false;
            }
            if (this.isNeedReloadSizeOverLife) {
                setSizeOverLife(this.sizeOverLife);
                this.isNeedReloadSizeOverLife = false;
            }
            if (this.isNeedReloadColorGenerate) {
                setColorGenerate(this.colorGenerate);
                this.isNeedReloadColorGenerate = false;
            }
        } catch (Throwable unused) {
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.autonavi.amap.mapcore.AbstractNativeInstance
    public void finalize() throws Throwable {
        super.finalize();
        if (this.nativeInstance != 0) {
            AMapNativeParticleSystem.nativeReleaseParticleOverLifeModule(this.nativeInstance);
            this.nativeInstance = 0L;
        }
    }

    public void setColorGenerate(ColorGenerate colorGenerate) {
        this.colorGenerate = colorGenerate;
        if (this.nativeInstance == 0) {
            this.isNeedReloadColorGenerate = true;
        } else {
            if (this.colorGenerate == null) {
                AMapNativeParticleSystem.nativeSetOverLifeItem(this.nativeInstance, 0L, 3);
                return;
            }
            if (this.colorGenerate.getNativeInstance() == 0) {
                this.colorGenerate.createNativeInstace();
            }
            AMapNativeParticleSystem.nativeSetOverLifeItem(this.nativeInstance, this.colorGenerate.getNativeInstance(), 3);
        }
    }

    public void setRotateOverLife(RotationOverLife rotationOverLife) {
        this.rotateOverLife = rotationOverLife;
        if (this.nativeInstance == 0) {
            this.isNeedReloadRotationOverLife = true;
        } else {
            if (this.rotateOverLife == null) {
                AMapNativeParticleSystem.nativeSetOverLifeItem(this.nativeInstance, 0L, 1);
                return;
            }
            if (this.rotateOverLife.getNativeInstance() == 0) {
                this.rotateOverLife.createNativeInstace();
            }
            AMapNativeParticleSystem.nativeSetOverLifeItem(this.nativeInstance, this.rotateOverLife.getNativeInstance(), 1);
        }
    }

    public void setSizeOverLife(SizeOverLife sizeOverLife) {
        this.sizeOverLife = sizeOverLife;
        if (this.nativeInstance == 0) {
            this.isNeedReloadSizeOverLife = true;
        } else {
            if (this.sizeOverLife == null) {
                AMapNativeParticleSystem.nativeSetOverLifeItem(this.nativeInstance, 0L, 2);
                return;
            }
            if (this.sizeOverLife.getNativeInstance() == 0) {
                this.sizeOverLife.createNativeInstace();
            }
            AMapNativeParticleSystem.nativeSetOverLifeItem(this.nativeInstance, this.sizeOverLife.getNativeInstance(), 2);
        }
    }

    public void setVelocityOverLife(VelocityGenerate velocityGenerate) {
        this.overLife = velocityGenerate;
        this.velocityOverLife = velocityGenerate;
        if (this.nativeInstance == 0) {
            this.isNeedReloadVelocityGenerate = true;
        } else {
            if (this.overLife == null) {
                AMapNativeParticleSystem.nativeSetOverLifeItem(this.nativeInstance, 0L, 0);
                return;
            }
            if (this.overLife.getNativeInstance() == 0) {
                this.overLife.createNativeInstace();
            }
            AMapNativeParticleSystem.nativeSetOverLifeItem(this.nativeInstance, this.overLife.getNativeInstance(), 0);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.nativeInstance);
    }
}
