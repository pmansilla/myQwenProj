package com.amap.api.mapcore.util;

import com.amap.api.maps.model.particle.ColorGenerate;
import com.amap.api.maps.model.particle.ParticleEmissionModule;
import com.amap.api.maps.model.particle.ParticleOverLifeModule;
import com.amap.api.maps.model.particle.ParticleShapeModule;
import com.amap.api.maps.model.particle.VelocityGenerate;

/* compiled from: IParticleLatyer.java */
/* loaded from: classes.dex */
public interface dq extends Cdo {
    void a(int i);

    void a(int i, int i2);

    void a(long j);

    void a(ColorGenerate colorGenerate);

    void a(ParticleEmissionModule particleEmissionModule);

    void a(ParticleOverLifeModule particleOverLifeModule);

    void a(ParticleShapeModule particleShapeModule);

    void a(VelocityGenerate velocityGenerate);

    void a(boolean z);

    int b();

    void b(long j);
}
