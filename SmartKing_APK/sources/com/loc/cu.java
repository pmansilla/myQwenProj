package com.loc;

import com.amap.api.location.AMapLocation;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;

/* compiled from: LocFilter.java */
/* loaded from: classes.dex */
public final class cu {
    AMapLocationServer a = null;
    long b = 0;
    long c = 0;
    private boolean h = true;
    int d = 0;
    long e = 0;
    AMapLocation f = null;
    long g = 0;

    private AMapLocationServer b(AMapLocationServer aMapLocationServer) {
        int i;
        if (fa.a(aMapLocationServer)) {
            if (!this.h || !er.b(aMapLocationServer.getTime())) {
                i = this.d;
            } else if (aMapLocationServer.getLocationType() == 5 || aMapLocationServer.getLocationType() == 6) {
                i = 4;
            }
            aMapLocationServer.setLocationType(i);
        }
        return aMapLocationServer;
    }

    public final AMapLocation a(AMapLocation aMapLocation) {
        if (!fa.a(aMapLocation)) {
            return aMapLocation;
        }
        long c = fa.c() - this.g;
        this.g = fa.c();
        if (c > BootloaderScanner.TIMEOUT) {
            return aMapLocation;
        }
        if (this.f == null) {
            this.f = aMapLocation;
            return aMapLocation;
        }
        if (1 != this.f.getLocationType() && !"gps".equalsIgnoreCase(this.f.getProvider())) {
            this.f = aMapLocation;
            return aMapLocation;
        }
        if (this.f.getAltitude() == aMapLocation.getAltitude() && this.f.getLongitude() == aMapLocation.getLongitude()) {
            this.f = aMapLocation;
            return aMapLocation;
        }
        long abs = Math.abs(aMapLocation.getTime() - this.f.getTime());
        if (30000 < abs) {
            this.f = aMapLocation;
            return aMapLocation;
        }
        if (fa.a(aMapLocation, this.f) > (((this.f.getSpeed() + aMapLocation.getSpeed()) * ((float) abs)) / 2000.0f) + ((this.f.getAccuracy() + aMapLocation.getAccuracy()) * 2.0f) + 3000.0f) {
            return this.f;
        }
        this.f = aMapLocation;
        return aMapLocation;
    }

    /* JADX WARN: Code restructure failed: missing block: B:61:0x0118, code lost:
    
        if (r11 < 30000) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x012d, code lost:
    
        if ((r9 - r19.c) > 30000) goto L48;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.autonavi.aps.amapapi.model.AMapLocationServer a(com.autonavi.aps.amapapi.model.AMapLocationServer r20) {
        /*
            Method dump skipped, instructions count: 304
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.cu.a(com.autonavi.aps.amapapi.model.AMapLocationServer):com.autonavi.aps.amapapi.model.AMapLocationServer");
    }

    public final void a() {
        this.a = null;
        this.b = 0L;
        this.c = 0L;
        this.f = null;
        this.g = 0L;
    }

    public final void a(boolean z) {
        this.h = z;
    }
}
