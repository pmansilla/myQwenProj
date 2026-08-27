package me.panpf.sketch.request;

import android.support.annotation.NonNull;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import me.panpf.sketch.SLog;

/* loaded from: classes2.dex */
public class FreeRideManager {
    private static final String NAME = "FreeRideManager";
    private Map<String, DisplayFreeRide> displayFreeRideProviderMap;
    private Map<String, DownloadFreeRide> downloadFreeRideProviderMap;
    private final Object displayFreeRideProviderMapLock = new Object();
    private final Object downloadFreeRideProviderMapLock = new Object();

    /* loaded from: classes2.dex */
    public interface DisplayFreeRide {
        void byDisplayFreeRide(DisplayFreeRide displayFreeRide);

        boolean canByDisplayFreeRide();

        String getDisplayFreeRideKey();

        String getDisplayFreeRideLog();

        Set<DisplayFreeRide> getDisplayFreeRideSet();

        boolean processDisplayFreeRide();
    }

    /* loaded from: classes2.dex */
    public interface DownloadFreeRide {
        void byDownloadFreeRide(DownloadFreeRide downloadFreeRide);

        boolean canByDownloadFreeRide();

        String getDownloadFreeRideKey();

        String getDownloadFreeRideLog();

        Set<DownloadFreeRide> getDownloadFreeRideSet();

        boolean processDownloadFreeRide();
    }

    public boolean byDisplayFreeRide(DisplayFreeRide displayFreeRide) {
        if (!displayFreeRide.canByDisplayFreeRide()) {
            return false;
        }
        synchronized (this.displayFreeRideProviderMapLock) {
            DisplayFreeRide displayFreeRide2 = this.displayFreeRideProviderMap != null ? this.displayFreeRideProviderMap.get(displayFreeRide.getDisplayFreeRideKey()) : null;
            if (displayFreeRide2 == null) {
                return false;
            }
            displayFreeRide2.byDisplayFreeRide(displayFreeRide);
            if (SLog.isLoggable(65538)) {
                SLog.d(NAME, "display. by free ride. %s -> %s", displayFreeRide.getDisplayFreeRideLog(), displayFreeRide2.getDisplayFreeRideLog());
            }
            return true;
        }
    }

    public boolean byDownloadFreeRide(DownloadFreeRide downloadFreeRide) {
        if (!downloadFreeRide.canByDownloadFreeRide()) {
            return false;
        }
        synchronized (this.downloadFreeRideProviderMapLock) {
            DownloadFreeRide downloadFreeRide2 = this.downloadFreeRideProviderMap != null ? this.downloadFreeRideProviderMap.get(downloadFreeRide.getDownloadFreeRideKey()) : null;
            if (downloadFreeRide2 == null) {
                return false;
            }
            downloadFreeRide2.byDownloadFreeRide(downloadFreeRide);
            if (SLog.isLoggable(65538)) {
                SLog.d(NAME, "download. by free ride. %s -> %s", downloadFreeRide.getDownloadFreeRideLog(), downloadFreeRide2.getDownloadFreeRideLog());
            }
            return true;
        }
    }

    public void registerDisplayFreeRideProvider(DisplayFreeRide displayFreeRide) {
        if (displayFreeRide.canByDisplayFreeRide()) {
            synchronized (this.displayFreeRideProviderMapLock) {
                if (this.displayFreeRideProviderMap == null) {
                    synchronized (this) {
                        if (this.displayFreeRideProviderMap == null) {
                            this.displayFreeRideProviderMap = new WeakHashMap();
                        }
                    }
                }
                this.displayFreeRideProviderMap.put(displayFreeRide.getDisplayFreeRideKey(), displayFreeRide);
                if (SLog.isLoggable(65538)) {
                    SLog.d(NAME, "display. register free ride provider. %s", displayFreeRide.getDisplayFreeRideLog());
                }
            }
        }
    }

    public void registerDownloadFreeRideProvider(DownloadFreeRide downloadFreeRide) {
        if (downloadFreeRide.canByDownloadFreeRide()) {
            synchronized (this.downloadFreeRideProviderMapLock) {
                if (this.downloadFreeRideProviderMap == null) {
                    synchronized (this) {
                        if (this.downloadFreeRideProviderMap == null) {
                            this.downloadFreeRideProviderMap = new WeakHashMap();
                        }
                    }
                }
                this.downloadFreeRideProviderMap.put(downloadFreeRide.getDownloadFreeRideKey(), downloadFreeRide);
                if (SLog.isLoggable(65538)) {
                    SLog.d(NAME, "download. register free ride provider. %s", downloadFreeRide.getDownloadFreeRideLog());
                }
            }
        }
    }

    @NonNull
    public String toString() {
        return NAME;
    }

    public void unregisterDisplayFreeRideProvider(DisplayFreeRide displayFreeRide) {
        Set<DisplayFreeRide> displayFreeRideSet;
        if (displayFreeRide.canByDisplayFreeRide()) {
            DisplayFreeRide displayFreeRide2 = null;
            synchronized (this.displayFreeRideProviderMapLock) {
                if (this.displayFreeRideProviderMap != null && (displayFreeRide2 = this.displayFreeRideProviderMap.remove(displayFreeRide.getDisplayFreeRideKey())) != null && SLog.isLoggable(65538)) {
                    SLog.d(NAME, "display. unregister free ride provider. %s", displayFreeRide2.getDisplayFreeRideLog());
                }
            }
            if (displayFreeRide2 == null || (displayFreeRideSet = displayFreeRide2.getDisplayFreeRideSet()) == null || displayFreeRideSet.size() == 0) {
                return;
            }
            String displayFreeRideLog = displayFreeRide2.getDisplayFreeRideLog();
            for (DisplayFreeRide displayFreeRide3 : displayFreeRideSet) {
                boolean processDisplayFreeRide = displayFreeRide3.processDisplayFreeRide();
                if (SLog.isLoggable(65538)) {
                    Object[] objArr = new Object[3];
                    objArr[0] = processDisplayFreeRide ? "success" : "failed";
                    objArr[1] = displayFreeRide3.getDisplayFreeRideLog();
                    objArr[2] = displayFreeRideLog;
                    SLog.d(NAME, "display. callback free ride. %s. %s  <-  %s", objArr);
                }
            }
            displayFreeRideSet.clear();
        }
    }

    public void unregisterDownloadFreeRideProvider(DownloadFreeRide downloadFreeRide) {
        Set<DownloadFreeRide> downloadFreeRideSet;
        if (downloadFreeRide.canByDownloadFreeRide()) {
            DownloadFreeRide downloadFreeRide2 = null;
            synchronized (this.downloadFreeRideProviderMapLock) {
                if (this.downloadFreeRideProviderMap != null && (downloadFreeRide2 = this.downloadFreeRideProviderMap.remove(downloadFreeRide.getDownloadFreeRideKey())) != null && SLog.isLoggable(65538)) {
                    SLog.d(NAME, "download. unregister free ride provider. %s", downloadFreeRide2.getDownloadFreeRideLog());
                }
            }
            if (downloadFreeRide2 == null || (downloadFreeRideSet = downloadFreeRide2.getDownloadFreeRideSet()) == null || downloadFreeRideSet.size() == 0) {
                return;
            }
            String downloadFreeRideLog = downloadFreeRide2.getDownloadFreeRideLog();
            for (DownloadFreeRide downloadFreeRide3 : downloadFreeRideSet) {
                boolean processDownloadFreeRide = downloadFreeRide3.processDownloadFreeRide();
                if (SLog.isLoggable(65538)) {
                    Object[] objArr = new Object[3];
                    objArr[0] = processDownloadFreeRide ? "success" : "failed";
                    objArr[1] = downloadFreeRide3.getDownloadFreeRideLog();
                    objArr[2] = downloadFreeRideLog;
                    SLog.d(NAME, "download. callback free ride. %s. %s  <-  %s", objArr);
                }
            }
            downloadFreeRideSet.clear();
        }
    }
}
