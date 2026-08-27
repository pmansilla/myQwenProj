package com.amap.openapi;

import android.content.Context;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.RequiresPermission;
import com.amap.location.common.log.ALLog;
import java.util.List;

/* compiled from: SystemGpsProvider.java */
/* loaded from: classes.dex */
public class da implements cz {
    private LocationManager a;

    public da(Context context) {
        this.a = (LocationManager) context.getSystemService("location");
    }

    @Override // com.amap.openapi.cz
    public GpsStatus a(GpsStatus gpsStatus) {
        if (this.a == null) {
            return null;
        }
        try {
            return this.a.getGpsStatus(gpsStatus);
        } catch (SecurityException unused) {
            ALLog.trace("@_24_1_@", "@_24_1_5_@");
            return null;
        }
    }

    @Override // com.amap.openapi.cz
    public List<String> a() {
        if (this.a == null) {
            return null;
        }
        return this.a.getAllProviders();
    }

    @Override // com.amap.openapi.cz
    public void a(GpsStatus.NmeaListener nmeaListener) {
        if (this.a != null) {
            this.a.removeNmeaListener(nmeaListener);
        }
    }

    @Override // com.amap.openapi.cz
    public void a(LocationListener locationListener) {
        if (this.a != null) {
            try {
                this.a.removeUpdates(locationListener);
            } catch (Exception unused) {
                ALLog.trace("@_24_1_@", "@_24_1_6_@");
            }
        }
    }

    @Override // com.amap.openapi.cz
    public void a(OnNmeaMessageListener onNmeaMessageListener) {
        if (Build.VERSION.SDK_INT < 24 || this.a == null) {
            return;
        }
        this.a.removeNmeaListener(onNmeaMessageListener);
    }

    @Override // com.amap.openapi.cz
    public void a(String str, long j, float f, LocationListener locationListener, Looper looper) {
        try {
            if (this.a != null) {
                this.a.requestLocationUpdates(str, j, f, locationListener, looper);
            }
        } catch (SecurityException unused) {
            ALLog.trace("@_24_1_@", "@_24_2_1_@");
        }
    }

    @Override // com.amap.openapi.cz
    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    public boolean a(GnssStatus.Callback callback) {
        if (this.a != null && Build.VERSION.SDK_INT >= 24) {
            try {
                return this.a.registerGnssStatusCallback(callback);
            } catch (SecurityException e) {
                ALLog.trace("@_24_1_@", "@_24_1_7_@", e);
            }
        }
        return false;
    }

    @Override // com.amap.openapi.cz
    public boolean a(GpsStatus.Listener listener) {
        if (this.a == null) {
            return false;
        }
        try {
            return this.a.addGpsStatusListener(listener);
        } catch (SecurityException unused) {
            ALLog.trace("@_24_1_@", "@_24_1_3_@");
            return false;
        }
    }

    @Override // com.amap.openapi.cz
    public boolean a(GpsStatus.NmeaListener nmeaListener, Looper looper) {
        if (this.a == null) {
            return false;
        }
        try {
            return this.a.addNmeaListener(nmeaListener);
        } catch (SecurityException unused) {
            ALLog.trace("@_24_1_@", "@_24_1_2_@");
            return false;
        }
    }

    @Override // com.amap.openapi.cz
    public boolean a(OnNmeaMessageListener onNmeaMessageListener, Looper looper) {
        if (this.a == null) {
            return false;
        }
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                return this.a.addNmeaListener(onNmeaMessageListener);
            }
            return false;
        } catch (SecurityException unused) {
            ALLog.trace("@_24_1_@", "@_24_1_2_@");
            return false;
        }
    }

    @Override // com.amap.openapi.cz
    public boolean a(String str) {
        if (this.a == null) {
            return false;
        }
        try {
            return this.a.isProviderEnabled(str);
        } catch (Exception e) {
            ALLog.trace("@_24_1_@", "@_24_1_4_@", e);
            return false;
        }
    }

    @Override // com.amap.openapi.cz
    public void b(GnssStatus.Callback callback) {
        if (this.a == null || Build.VERSION.SDK_INT < 24) {
            return;
        }
        this.a.unregisterGnssStatusCallback(callback);
    }

    @Override // com.amap.openapi.cz
    public void b(GpsStatus.Listener listener) {
        if (this.a != null) {
            this.a.removeGpsStatusListener(listener);
        }
    }
}
