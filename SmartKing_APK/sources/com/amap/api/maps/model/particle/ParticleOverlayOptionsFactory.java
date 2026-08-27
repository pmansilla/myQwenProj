package com.amap.api.maps.model.particle;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import java.util.ArrayList;
import java.util.List;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;

/* loaded from: classes.dex */
public class ParticleOverlayOptionsFactory {
    public static final int PARTICLE_TYPE_HAZE = 3;
    public static final int PARTICLE_TYPE_RAIN = 1;
    public static final int PARTICLE_TYPE_SNOWY = 2;
    public static final int PARTICLE_TYPE_SUNNY = 0;

    private static List<ParticleOverlayOptions> a() {
        ArrayList arrayList = new ArrayList();
        ParticleOverlayOptions particleOverlayOptions = new ParticleOverlayOptions();
        particleOverlayOptions.setLoop(false);
        particleOverlayOptions.setMaxParticles(1);
        particleOverlayOptions.setDuration(10000L);
        particleOverlayOptions.setParticleEmissionModule(new ParticleEmissionModule(1, (int) 10000));
        particleOverlayOptions.setParticleShapeModule(new SinglePointParticleShape(0.5f, 0.5f, 0.0f, true));
        particleOverlayOptions.setParticleLifeTime(10000L);
        particleOverlayOptions.setParticleStartSpeed(new RandomVelocityBetweenTwoConstants(-10.0f, -0.0f, -0.0f, -20.0f, 0.0f, 0.0f));
        BitmapDescriptor fromAsset = BitmapDescriptorFactory.fromAsset("map_custom/particle/fog.png");
        particleOverlayOptions.icon(fromAsset);
        particleOverlayOptions.setStartParticleSize(fromAsset.getWidth() * 5, fromAsset.getWidth() * 5);
        arrayList.add(particleOverlayOptions);
        ParticleOverlayOptions particleOverlayOptions2 = new ParticleOverlayOptions();
        particleOverlayOptions2.setMaxParticles(1000);
        particleOverlayOptions2.setDuration(10000L);
        particleOverlayOptions2.setParticleEmissionModule(new ParticleEmissionModule(30, (int) 2500));
        particleOverlayOptions2.setLoop(true);
        particleOverlayOptions2.setParticleShapeModule(new RectParticleShape(0.5f, 0.5f, 1.0f, 1.0f, true));
        particleOverlayOptions2.setParticleLifeTime(10000L);
        particleOverlayOptions2.setParticleStartSpeed(new RandomVelocityBetweenTwoConstants(-100.0f, -100.0f, -1.0f, -200.0f, 100.0f, 1.0f));
        BitmapDescriptor fromAsset2 = BitmapDescriptorFactory.fromAsset("map_custom/particle/haze.png");
        particleOverlayOptions2.icon(fromAsset2);
        particleOverlayOptions2.setStartParticleSize(fromAsset2.getWidth(), fromAsset2.getHeight());
        particleOverlayOptions2.setParticleStartColor(new RandomColorBetWeenTwoConstants(255.0f, 255.0f, 255.0f, 102.0f, 255.0f, 255.0f, 255.0f, 255.0f));
        arrayList.add(particleOverlayOptions2);
        return arrayList;
    }

    private static ParticleOverlayOptions b() {
        ParticleOverlayOptions particleOverlayOptions = new ParticleOverlayOptions();
        particleOverlayOptions.setMaxParticles(1000);
        particleOverlayOptions.setDuration(BootloaderScanner.TIMEOUT);
        particleOverlayOptions.setParticleEmissionModule(new ParticleEmissionModule(20, 1000));
        particleOverlayOptions.setLoop(true);
        particleOverlayOptions.setParticleShapeModule(new RectParticleShape(0.0f, 0.0f, 1.0f, 0.1f, true));
        particleOverlayOptions.setParticleLifeTime(10000L);
        particleOverlayOptions.setParticleStartSpeed(new RandomVelocityBetweenTwoConstants(-50.0f, 100.0f, 0.0f, 50.0f, 200.0f, 0.0f));
        BitmapDescriptor fromAsset = BitmapDescriptorFactory.fromAsset("map_custom/particle/snow.png");
        particleOverlayOptions.icon(fromAsset);
        particleOverlayOptions.setStartParticleSize(fromAsset.getWidth(), fromAsset.getHeight());
        return particleOverlayOptions;
    }

    private static ParticleOverlayOptions c() {
        ParticleOverlayOptions particleOverlayOptions = new ParticleOverlayOptions();
        particleOverlayOptions.setMaxParticles(1000);
        particleOverlayOptions.setDuration(BootloaderScanner.TIMEOUT);
        particleOverlayOptions.setParticleEmissionModule(new ParticleEmissionModule(100, 1000));
        particleOverlayOptions.setLoop(true);
        particleOverlayOptions.setParticleLifeTime(BootloaderScanner.TIMEOUT);
        particleOverlayOptions.setParticleStartSpeed(new RandomVelocityBetweenTwoConstants(10.0f, 1000.0f, 0.0f, 100.0f, 1000.0f, 0.0f));
        particleOverlayOptions.setParticleShapeModule(new RectParticleShape(0.0f, 0.0f, 1.0f, 0.1f, true));
        BitmapDescriptor fromAsset = BitmapDescriptorFactory.fromAsset("map_custom/particle/rain.png");
        particleOverlayOptions.icon(fromAsset);
        particleOverlayOptions.setStartParticleSize(fromAsset.getWidth() * 2, fromAsset.getHeight() * 2);
        return particleOverlayOptions;
    }

    private static List<ParticleOverlayOptions> d() {
        ArrayList arrayList = new ArrayList();
        ParticleOverlayOptions particleOverlayOptions = new ParticleOverlayOptions();
        particleOverlayOptions.setMaxParticles(1);
        particleOverlayOptions.setDuration(10000L);
        int i = (int) 2500;
        particleOverlayOptions.setParticleEmissionModule(new ParticleEmissionModule(1, i));
        particleOverlayOptions.setLoop(true);
        particleOverlayOptions.setParticleShapeModule(new SinglePointParticleShape(0.0f, 0.0f, 0.0f));
        particleOverlayOptions.setParticleLifeTime(10000L);
        particleOverlayOptions.setParticleStartSpeed(new RandomVelocityBetweenTwoConstants(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        particleOverlayOptions.setParticleOverLifeModule(new ParticleOverLifeModule());
        particleOverlayOptions.icon(BitmapDescriptorFactory.fromAsset("map_custom/particle/sun_0.png"));
        particleOverlayOptions.setStartParticleSize(1000, 1000);
        arrayList.add(particleOverlayOptions);
        ParticleOverlayOptions particleOverlayOptions2 = new ParticleOverlayOptions();
        particleOverlayOptions2.setMaxParticles(1);
        particleOverlayOptions2.setDuration(10000L);
        particleOverlayOptions2.setParticleEmissionModule(new ParticleEmissionModule(1, i));
        particleOverlayOptions2.setLoop(true);
        particleOverlayOptions2.setParticleShapeModule(new SinglePointParticleShape(0.0f, 0.0f, 0.0f));
        particleOverlayOptions2.setParticleLifeTime(10000L);
        particleOverlayOptions2.setParticleStartSpeed(new RandomVelocityBetweenTwoConstants(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        ParticleOverLifeModule particleOverLifeModule = new ParticleOverLifeModule();
        particleOverLifeModule.setRotateOverLife(new ConstantRotationOverLife(45.0f));
        particleOverlayOptions2.setParticleOverLifeModule(particleOverLifeModule);
        particleOverlayOptions2.icon(BitmapDescriptorFactory.fromAsset("map_custom/particle/sun_1.png"));
        particleOverlayOptions2.setStartParticleSize(1000, 1000);
        arrayList.add(particleOverlayOptions2);
        return arrayList;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0028, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<com.amap.api.maps.model.particle.ParticleOverlayOptions> defaultOptions(int r1) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            switch(r1) {
                case 0: goto L21;
                case 1: goto L19;
                case 2: goto L11;
                case 3: goto L9;
                default: goto L8;
            }
        L8:
            goto L28
        L9:
            java.util.List r1 = a()
            r0.addAll(r1)
            goto L28
        L11:
            com.amap.api.maps.model.particle.ParticleOverlayOptions r1 = b()
            r0.add(r1)
            goto L28
        L19:
            com.amap.api.maps.model.particle.ParticleOverlayOptions r1 = c()
            r0.add(r1)
            goto L28
        L21:
            java.util.List r1 = d()
            r0.addAll(r1)
        L28:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.maps.model.particle.ParticleOverlayOptionsFactory.defaultOptions(int):java.util.List");
    }
}
