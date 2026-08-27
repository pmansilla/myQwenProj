package no.nordicsemi.android.support.v18.scanner;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

/* loaded from: classes2.dex */
public abstract class BluetoothLeScannerCompat {
    public static final String EXTRA_CALLBACK_TYPE = "android.bluetooth.le.extra.CALLBACK_TYPE";
    public static final String EXTRA_ERROR_CODE = "android.bluetooth.le.extra.ERROR_CODE";
    public static final String EXTRA_LIST_SCAN_RESULT = "android.bluetooth.le.extra.LIST_SCAN_RESULT";
    private static BluetoothLeScannerCompat instance;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class ScanCallbackWrapper {
        private final boolean emulateBatching;
        private final boolean emulateFiltering;
        private final boolean emulateFoundOrLostCallbackType;

        @NonNull
        final List<ScanFilter> filters;

        @NonNull
        final Handler handler;

        @NonNull
        final ScanCallback scanCallback;

        @NonNull
        final ScanSettings scanSettings;

        @NonNull
        private final Object LOCK = new Object();

        @NonNull
        private final List<ScanResult> scanResults = new ArrayList();

        @NonNull
        private final Set<String> devicesInBatch = new HashSet();

        @NonNull
        private final Map<String, ScanResult> devicesInRange = new HashMap();

        @NonNull
        private final Runnable flushPendingScanResultsTask = new Runnable() { // from class: no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat.ScanCallbackWrapper.1
            @Override // java.lang.Runnable
            public void run() {
                if (ScanCallbackWrapper.this.scanningStopped) {
                    return;
                }
                ScanCallbackWrapper.this.flushPendingScanResults();
                ScanCallbackWrapper.this.handler.postDelayed(this, ScanCallbackWrapper.this.scanSettings.getReportDelayMillis());
            }
        };

        @NonNull
        private final Runnable matchLostNotifierTask = new Runnable() { // from class: no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat.ScanCallbackWrapper.2
            @Override // java.lang.Runnable
            public void run() {
                long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
                synchronized (ScanCallbackWrapper.this.LOCK) {
                    Iterator it = ScanCallbackWrapper.this.devicesInRange.values().iterator();
                    while (it.hasNext()) {
                        final ScanResult scanResult = (ScanResult) it.next();
                        if (scanResult.getTimestampNanos() < elapsedRealtimeNanos - ScanCallbackWrapper.this.scanSettings.getMatchLostDeviceTimeout()) {
                            it.remove();
                            ScanCallbackWrapper.this.handler.post(new Runnable() { // from class: no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat.ScanCallbackWrapper.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    ScanCallbackWrapper.this.scanCallback.onScanResult(4, scanResult);
                                }
                            });
                        }
                    }
                    if (!ScanCallbackWrapper.this.devicesInRange.isEmpty()) {
                        ScanCallbackWrapper.this.handler.postDelayed(this, ScanCallbackWrapper.this.scanSettings.getMatchLostTaskInterval());
                    }
                }
            }
        };
        private boolean scanningStopped = false;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ScanCallbackWrapper(boolean z, boolean z2, @NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull ScanCallback scanCallback, @NonNull Handler handler) {
            this.filters = Collections.unmodifiableList(list);
            this.scanSettings = scanSettings;
            this.scanCallback = scanCallback;
            this.handler = handler;
            boolean z3 = false;
            this.emulateFoundOrLostCallbackType = (scanSettings.getCallbackType() == 1 || ((Build.VERSION.SDK_INT >= 23) && scanSettings.getUseHardwareCallbackTypesIfSupported())) ? false : true;
            this.emulateFiltering = (list.isEmpty() || (z2 && scanSettings.getUseHardwareFilteringIfSupported())) ? false : true;
            long reportDelayMillis = scanSettings.getReportDelayMillis();
            if (reportDelayMillis > 0 && (!z || !scanSettings.getUseHardwareBatchingIfSupported())) {
                z3 = true;
            }
            this.emulateBatching = z3;
            if (this.emulateBatching) {
                handler.postDelayed(this.flushPendingScanResultsTask, reportDelayMillis);
            }
        }

        private boolean matches(@NonNull ScanResult scanResult) {
            Iterator<ScanFilter> it = this.filters.iterator();
            while (it.hasNext()) {
                if (it.next().matches(scanResult)) {
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void close() {
            this.scanningStopped = true;
            this.handler.removeCallbacksAndMessages(null);
            synchronized (this.LOCK) {
                this.devicesInRange.clear();
                this.devicesInBatch.clear();
                this.scanResults.clear();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void flushPendingScanResults() {
            if (!this.emulateBatching || this.scanningStopped) {
                return;
            }
            synchronized (this.LOCK) {
                this.scanCallback.onBatchScanResults(new ArrayList(this.scanResults));
                this.scanResults.clear();
                this.devicesInBatch.clear();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void handleScanError(int i) {
            this.scanCallback.onScanFailed(i);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void handleScanResult(int i, @NonNull ScanResult scanResult) {
            boolean isEmpty;
            ScanResult put;
            if (this.scanningStopped) {
                return;
            }
            if (this.filters.isEmpty() || matches(scanResult)) {
                String address = scanResult.getDevice().getAddress();
                if (!this.emulateFoundOrLostCallbackType) {
                    if (!this.emulateBatching) {
                        this.scanCallback.onScanResult(i, scanResult);
                        return;
                    }
                    synchronized (this.LOCK) {
                        if (!this.devicesInBatch.contains(address)) {
                            this.scanResults.add(scanResult);
                            this.devicesInBatch.add(address);
                        }
                    }
                    return;
                }
                synchronized (this.devicesInRange) {
                    isEmpty = this.devicesInRange.isEmpty();
                    put = this.devicesInRange.put(address, scanResult);
                }
                if (put == null && (this.scanSettings.getCallbackType() & 2) > 0) {
                    this.scanCallback.onScanResult(2, scanResult);
                }
                if (!isEmpty || (this.scanSettings.getCallbackType() & 4) <= 0) {
                    return;
                }
                this.handler.removeCallbacks(this.matchLostNotifierTask);
                this.handler.postDelayed(this.matchLostNotifierTask, this.scanSettings.getMatchLostTaskInterval());
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void handleScanResults(@NonNull List<ScanResult> list) {
            if (this.scanningStopped) {
                return;
            }
            if (this.emulateFiltering) {
                ArrayList arrayList = new ArrayList();
                for (ScanResult scanResult : list) {
                    if (matches(scanResult)) {
                        arrayList.add(scanResult);
                    }
                }
                list = arrayList;
            }
            this.scanCallback.onBatchScanResults(list);
        }
    }

    @NonNull
    public static synchronized BluetoothLeScannerCompat getScanner() {
        synchronized (BluetoothLeScannerCompat.class) {
            if (instance != null) {
                return instance;
            }
            if (Build.VERSION.SDK_INT >= 26) {
                BluetoothLeScannerImplOreo bluetoothLeScannerImplOreo = new BluetoothLeScannerImplOreo();
                instance = bluetoothLeScannerImplOreo;
                return bluetoothLeScannerImplOreo;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                BluetoothLeScannerImplMarshmallow bluetoothLeScannerImplMarshmallow = new BluetoothLeScannerImplMarshmallow();
                instance = bluetoothLeScannerImplMarshmallow;
                return bluetoothLeScannerImplMarshmallow;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                BluetoothLeScannerImplLollipop bluetoothLeScannerImplLollipop = new BluetoothLeScannerImplLollipop();
                instance = bluetoothLeScannerImplLollipop;
                return bluetoothLeScannerImplLollipop;
            }
            BluetoothLeScannerImplJB bluetoothLeScannerImplJB = new BluetoothLeScannerImplJB();
            instance = bluetoothLeScannerImplJB;
            return bluetoothLeScannerImplJB;
        }
    }

    public abstract void flushPendingScanResults(@NonNull ScanCallback scanCallback);

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public final void startScan(@Nullable List<ScanFilter> list, @Nullable ScanSettings scanSettings, @NonNull Context context, @NonNull PendingIntent pendingIntent) {
        if (pendingIntent == null) {
            throw new IllegalArgumentException("callbackIntent is null");
        }
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        if (list == null) {
            list = Collections.emptyList();
        }
        if (scanSettings == null) {
            scanSettings = new ScanSettings.Builder().build();
        }
        startScanInternal(list, scanSettings, context, pendingIntent);
    }

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public final void startScan(@Nullable List<ScanFilter> list, @Nullable ScanSettings scanSettings, @NonNull ScanCallback scanCallback) {
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        Handler handler = new Handler(Looper.getMainLooper());
        if (list == null) {
            list = Collections.emptyList();
        }
        if (scanSettings == null) {
            scanSettings = new ScanSettings.Builder().build();
        }
        startScanInternal(list, scanSettings, scanCallback, handler);
    }

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public final void startScan(@Nullable List<ScanFilter> list, @Nullable ScanSettings scanSettings, @NonNull ScanCallback scanCallback, @Nullable Handler handler) {
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (list == null) {
            list = Collections.emptyList();
        }
        if (scanSettings == null) {
            scanSettings = new ScanSettings.Builder().build();
        }
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        startScanInternal(list, scanSettings, scanCallback, handler);
    }

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public final void startScan(@NonNull ScanCallback scanCallback) {
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        startScanInternal(Collections.emptyList(), new ScanSettings.Builder().build(), scanCallback, new Handler(Looper.getMainLooper()));
    }

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    abstract void startScanInternal(@NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull Context context, @NonNull PendingIntent pendingIntent);

    /* JADX INFO: Access modifiers changed from: package-private */
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public abstract void startScanInternal(@NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull ScanCallback scanCallback, @NonNull Handler handler);

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public final void stopScan(@NonNull Context context, @NonNull PendingIntent pendingIntent) {
        if (pendingIntent == null) {
            throw new IllegalArgumentException("callbackIntent is null");
        }
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        stopScanInternal(context, pendingIntent);
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    public final void stopScan(@NonNull ScanCallback scanCallback) {
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        stopScanInternal(scanCallback);
    }

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    abstract void stopScanInternal(@NonNull Context context, @NonNull PendingIntent pendingIntent);

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    abstract void stopScanInternal(@NonNull ScanCallback scanCallback);
}
