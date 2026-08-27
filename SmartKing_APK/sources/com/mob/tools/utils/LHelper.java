package com.mob.tools.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.czw.smartkit.BuildConfig;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.MobLog;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;

@SuppressLint({"MissingPermission"})
/* loaded from: classes2.dex */
public class LHelper {
    private static final int CACHE_LIFE_CYCLE = 5000;
    private static final int CANCEL_GPS_LOCATING = 1;
    private static final int CANCEL_NETWORK_LOCATING = 2;
    private static final int START_LOCATING = 0;
    private static LHelper instance;
    private Location cache;
    private int gpsTimeoutSec;
    private Handler handler = MobHandlerThread.newHandler("T-lct", new Handler.Callback() { // from class: com.mob.tools.utils.LHelper.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            try {
                switch (message.what) {
                    case 0:
                        LHelper.this.onRequest();
                        break;
                    case 1:
                        LHelper.this.onGPSTimeout();
                        break;
                    case 2:
                        LHelper.this.onNetworkTimeout();
                        break;
                }
                return false;
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
                LHelper.this.finished();
                return false;
            }
        }
    });
    private LocationListener listener = new LocationListener() { // from class: com.mob.tools.utils.LHelper.2
        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            try {
                try {
                    LHelper.this.lm.removeUpdates(this);
                    LHelper.this.requestedLoc = new Location(location);
                    LHelper.this.cache = new Location(location);
                    LHelper.this.requestAt = System.currentTimeMillis();
                } catch (Throwable th) {
                    MobLog.getInstance().d(th);
                }
            } finally {
                LHelper.this.finished();
            }
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    };
    private LocationManager lm;
    private int networkTimeoutSec;
    private long requestAt;
    private Location requestedLoc;

    private LHelper() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finished() {
        try {
            synchronized (this) {
                notifyAll();
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    public static LHelper getInstance() {
        if (instance == null) {
            synchronized (LHelper.class) {
                if (instance == null) {
                    instance = new LHelper();
                }
            }
        }
        return instance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGPSTimeout() {
        if (this.lm != null) {
            this.lm.removeUpdates(this.listener);
            if ((this.networkTimeoutSec != 0) && this.lm.isProviderEnabled("network")) {
                onRequestNetwork();
            } else {
                finished();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNetworkTimeout() {
        if (this.lm != null) {
            this.lm.removeUpdates(this.listener);
        }
        finished();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRequest() {
        boolean z = this.gpsTimeoutSec != 0;
        boolean z2 = this.networkTimeoutSec != 0;
        if (this.lm == null) {
            finished();
            return;
        }
        if (z && this.lm.isProviderEnabled("gps")) {
            onRequestGps();
        } else if (z2 && this.lm.isProviderEnabled("network")) {
            onRequestNetwork();
        } else {
            finished();
        }
    }

    private void onRequestGps() {
        try {
            ReflectHelper.invokeInstanceMethod(this.lm, Strings.getString(124), new Object[]{Strings.getString(122), 1000, 0, this.listener}, new Class[]{String.class, Long.TYPE, Float.TYPE, LocationListener.class});
            if (this.gpsTimeoutSec > 0) {
                this.handler.sendEmptyMessageDelayed(1, this.gpsTimeoutSec * 1000);
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            this.handler.sendEmptyMessage(1);
        }
    }

    private Location onRequestLastKnown() {
        Location location;
        Throwable th;
        try {
            location = (Location) ReflectHelper.invokeInstanceMethod(this.lm, Strings.getString(BuildConfig.VERSION_CODE), Strings.getString(122));
            if (location == null) {
                try {
                    return (Location) ReflectHelper.invokeInstanceMethod(this.lm, Strings.getString(BuildConfig.VERSION_CODE), Strings.getString(123));
                } catch (Throwable th2) {
                    th = th2;
                    MobLog.getInstance().w(th);
                    return location;
                }
            }
        } catch (Throwable th3) {
            location = null;
            th = th3;
        }
        return location;
    }

    private void onRequestNetwork() {
        try {
            ReflectHelper.invokeInstanceMethod(this.lm, Strings.getString(124), new Object[]{Strings.getString(123), 1000, 0, this.listener}, new Class[]{String.class, Long.TYPE, Float.TYPE, LocationListener.class});
            if (this.networkTimeoutSec > 0) {
                this.handler.sendEmptyMessageDelayed(2, this.networkTimeoutSec * 1000);
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            this.handler.sendEmptyMessage(2);
        }
    }

    private Location quickResponse(boolean z) {
        if (z || this.cache == null || System.currentTimeMillis() - this.requestAt > BootloaderScanner.TIMEOUT) {
            return null;
        }
        return new Location(this.cache);
    }

    private Location requestLocation(Context context, int i, int i2, boolean z) {
        Location location = null;
        try {
            DeviceHelper deviceHelper = DeviceHelper.getInstance(context);
            if (deviceHelper.checkPermission("android.permission.ACCESS_FINE_LOCATION")) {
                this.gpsTimeoutSec = i;
                this.networkTimeoutSec = i2;
                if (this.lm == null) {
                    this.lm = (LocationManager) deviceHelper.getSystemServiceSafe("location");
                }
                if (this.lm == null) {
                    return null;
                }
                synchronized (this) {
                    this.handler.sendEmptyMessageDelayed(0, 50L);
                    wait();
                }
                if (this.requestedLoc == null && z) {
                    this.requestedLoc = onRequestLastKnown();
                }
                if (this.requestedLoc != null) {
                    this.cache = new Location(this.requestedLoc);
                    this.requestAt = System.currentTimeMillis();
                    Location location2 = new Location(this.requestedLoc);
                    try {
                        this.requestedLoc = null;
                        return location2;
                    } catch (Throwable th) {
                        location = location2;
                        th = th;
                        MobLog.getInstance().d(th);
                        return location;
                    }
                }
            }
        } catch (Throwable th2) {
            th = th2;
        }
        return location;
    }

    public Location getLocation(Context context) {
        return getLocation(context, 0);
    }

    public Location getLocation(Context context, int i) {
        return getLocation(context, i, 0);
    }

    public Location getLocation(Context context, int i, int i2) {
        return getLocation(context, i, i2, true);
    }

    public Location getLocation(Context context, int i, int i2, boolean z) {
        return getLocation(context, i, i2, z, false);
    }

    public Location getLocation(Context context, int i, int i2, boolean z, boolean z2) {
        Location quickResponse = quickResponse(z2);
        if (quickResponse == null) {
            synchronized (LHelper.class) {
                Location quickResponse2 = quickResponse(z2);
                quickResponse = quickResponse2 == null ? requestLocation(context, i, i2, z) : quickResponse2;
            }
        }
        return quickResponse;
    }
}
