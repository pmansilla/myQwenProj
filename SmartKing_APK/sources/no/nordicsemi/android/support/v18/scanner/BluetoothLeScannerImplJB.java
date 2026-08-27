package no.nordicsemi.android.support.v18.scanner;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.LongCompanionObject;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class BluetoothLeScannerImplJB extends BluetoothLeScannerCompat {

    @Nullable
    private HandlerThread handlerThread;

    @Nullable
    private Handler powerSaveHandler;
    private long powerSaveRestInterval;
    private long powerSaveScanInterval;

    @NonNull
    private final Map<ScanCallback, BluetoothLeScannerCompat.ScanCallbackWrapper> wrappers = new HashMap();
    private final Runnable powerSaveSleepTask = new Runnable() { // from class: no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplJB.1
        @Override // java.lang.Runnable
        @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
        public void run() {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter == null || BluetoothLeScannerImplJB.this.powerSaveRestInterval <= 0 || BluetoothLeScannerImplJB.this.powerSaveScanInterval <= 0) {
                return;
            }
            defaultAdapter.stopLeScan(BluetoothLeScannerImplJB.this.scanCallback);
            BluetoothLeScannerImplJB.this.powerSaveHandler.postDelayed(BluetoothLeScannerImplJB.this.powerSaveScanTask, BluetoothLeScannerImplJB.this.powerSaveRestInterval);
        }
    };
    private final Runnable powerSaveScanTask = new Runnable() { // from class: no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplJB.2
        @Override // java.lang.Runnable
        @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
        public void run() {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter == null || BluetoothLeScannerImplJB.this.powerSaveRestInterval <= 0 || BluetoothLeScannerImplJB.this.powerSaveScanInterval <= 0) {
                return;
            }
            defaultAdapter.startLeScan(BluetoothLeScannerImplJB.this.scanCallback);
            BluetoothLeScannerImplJB.this.powerSaveHandler.postDelayed(BluetoothLeScannerImplJB.this.powerSaveSleepTask, BluetoothLeScannerImplJB.this.powerSaveScanInterval);
        }
    };
    private final BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() { // from class: no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplJB.3
        @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
            final ScanResult scanResult = new ScanResult(bluetoothDevice, ScanRecord.parseFromBytes(bArr), i, SystemClock.elapsedRealtimeNanos());
            synchronized (BluetoothLeScannerImplJB.this.wrappers) {
                for (final BluetoothLeScannerCompat.ScanCallbackWrapper scanCallbackWrapper : BluetoothLeScannerImplJB.this.wrappers.values()) {
                    scanCallbackWrapper.handler.post(new Runnable() { // from class: no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplJB.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            scanCallbackWrapper.handleScanResult(1, scanResult);
                        }
                    });
                }
            }
        }
    };

    private void setPowerSaveSettings() {
        long j;
        long j2;
        synchronized (this.wrappers) {
            Iterator<BluetoothLeScannerCompat.ScanCallbackWrapper> it = this.wrappers.values().iterator();
            j = Long.MAX_VALUE;
            j2 = Long.MAX_VALUE;
            while (it.hasNext()) {
                ScanSettings scanSettings = it.next().scanSettings;
                if (scanSettings.hasPowerSaveMode()) {
                    if (j > scanSettings.getPowerSaveRest()) {
                        j = scanSettings.getPowerSaveRest();
                    }
                    if (j2 > scanSettings.getPowerSaveScan()) {
                        j2 = scanSettings.getPowerSaveScan();
                    }
                }
            }
        }
        if (j >= LongCompanionObject.MAX_VALUE || j2 >= LongCompanionObject.MAX_VALUE) {
            this.powerSaveScanInterval = 0L;
            this.powerSaveRestInterval = 0L;
            if (this.powerSaveHandler != null) {
                this.powerSaveHandler.removeCallbacks(this.powerSaveScanTask);
                this.powerSaveHandler.removeCallbacks(this.powerSaveSleepTask);
                return;
            }
            return;
        }
        this.powerSaveRestInterval = j;
        this.powerSaveScanInterval = j2;
        if (this.powerSaveHandler != null) {
            this.powerSaveHandler.removeCallbacks(this.powerSaveScanTask);
            this.powerSaveHandler.removeCallbacks(this.powerSaveSleepTask);
            this.powerSaveHandler.postDelayed(this.powerSaveSleepTask, this.powerSaveScanInterval);
        }
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
    @RequiresPermission("android.permission.BLUETOOTH")
    public void flushPendingScanResults(@NonNull ScanCallback scanCallback) {
        BluetoothLeScannerCompat.ScanCallbackWrapper scanCallbackWrapper;
        BluetoothLeUtils.checkAdapterStateOn(BluetoothAdapter.getDefaultAdapter());
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback cannot be null!");
        }
        synchronized (this.wrappers) {
            scanCallbackWrapper = this.wrappers.get(scanCallback);
        }
        if (scanCallbackWrapper == null) {
            throw new IllegalArgumentException("callback not registered!");
        }
        scanCallbackWrapper.flushPendingScanResults();
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    void startScanInternal(@NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull Context context, @NonNull PendingIntent pendingIntent) {
        BluetoothLeUtils.checkAdapterStateOn(BluetoothAdapter.getDefaultAdapter());
        Intent intent = new Intent(context, (Class<?>) ScannerService.class);
        intent.putParcelableArrayListExtra("no.nordicsemi.android.support.v18.EXTRA_FILTERS", new ArrayList<>(list));
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_SETTINGS", scanSettings);
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_PENDING_INTENT", pendingIntent);
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_START", true);
        context.startService(intent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public void startScanInternal(@NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull ScanCallback scanCallback, @NonNull Handler handler) {
        boolean isEmpty;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeUtils.checkAdapterStateOn(defaultAdapter);
        synchronized (this.wrappers) {
            if (this.wrappers.containsKey(scanCallback)) {
                throw new IllegalArgumentException("scanner already started with given scanCallback");
            }
            BluetoothLeScannerCompat.ScanCallbackWrapper scanCallbackWrapper = new BluetoothLeScannerCompat.ScanCallbackWrapper(false, false, list, scanSettings, scanCallback, handler);
            isEmpty = this.wrappers.isEmpty();
            this.wrappers.put(scanCallback, scanCallbackWrapper);
        }
        if (this.handlerThread == null) {
            this.handlerThread = new HandlerThread(BluetoothLeScannerImplJB.class.getName());
            this.handlerThread.start();
            this.powerSaveHandler = new Handler(this.handlerThread.getLooper());
        }
        setPowerSaveSettings();
        if (isEmpty) {
            defaultAdapter.startLeScan(this.scanCallback);
        }
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    void stopScanInternal(@NonNull Context context, @NonNull PendingIntent pendingIntent) {
        BluetoothLeUtils.checkAdapterStateOn(BluetoothAdapter.getDefaultAdapter());
        Intent intent = new Intent(context, (Class<?>) ScannerService.class);
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_PENDING_INTENT", pendingIntent);
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_START", false);
        context.startService(intent);
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    void stopScanInternal(@NonNull ScanCallback scanCallback) {
        BluetoothLeScannerCompat.ScanCallbackWrapper remove;
        boolean isEmpty;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeUtils.checkAdapterStateOn(defaultAdapter);
        synchronized (this.wrappers) {
            remove = this.wrappers.remove(scanCallback);
            isEmpty = this.wrappers.isEmpty();
        }
        if (remove == null) {
            return;
        }
        remove.close();
        setPowerSaveSettings();
        if (isEmpty) {
            defaultAdapter.stopLeScan(this.scanCallback);
            if (this.powerSaveHandler != null) {
                this.powerSaveHandler.removeCallbacksAndMessages(null);
            }
            if (this.handlerThread != null) {
                this.handlerThread.quitSafely();
                this.handlerThread = null;
            }
        }
    }
}
