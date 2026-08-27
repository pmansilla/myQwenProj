package no.nordicsemi.android.support.v18.scanner;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;

/* JADX INFO: Access modifiers changed from: package-private */
@TargetApi(21)
/* loaded from: classes2.dex */
public class BluetoothLeScannerImplLollipop extends BluetoothLeScannerCompat {

    @NonNull
    private final Map<ScanCallback, ScanCallbackWrapperLollipop> wrappers = new HashMap();

    /* loaded from: classes2.dex */
    static class ScanCallbackWrapperLollipop extends BluetoothLeScannerCompat.ScanCallbackWrapper {

        @NonNull
        private final android.bluetooth.le.ScanCallback nativeCallback;

        private ScanCallbackWrapperLollipop(boolean z, boolean z2, @NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull ScanCallback scanCallback, @NonNull Handler handler) {
            super(z, z2, list, scanSettings, scanCallback, handler);
            this.nativeCallback = new android.bluetooth.le.ScanCallback() { // from class: no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplLollipop.ScanCallbackWrapperLollipop.1
                private long lastBatchTimestamp;

                @Override // android.bluetooth.le.ScanCallback
                public void onBatchScanResults(final List<android.bluetooth.le.ScanResult> list2) {
                    ScanCallbackWrapperLollipop.this.handler.post(new Runnable() { // from class: no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplLollipop.ScanCallbackWrapperLollipop.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            long elapsedRealtime = SystemClock.elapsedRealtime();
                            if (AnonymousClass1.this.lastBatchTimestamp > (elapsedRealtime - ScanCallbackWrapperLollipop.this.scanSettings.getReportDelayMillis()) + 5) {
                                return;
                            }
                            AnonymousClass1.this.lastBatchTimestamp = elapsedRealtime;
                            ScanCallbackWrapperLollipop.this.handleScanResults(((BluetoothLeScannerImplLollipop) BluetoothLeScannerCompat.getScanner()).fromNativeScanResults(list2));
                        }
                    });
                }

                @Override // android.bluetooth.le.ScanCallback
                public void onScanFailed(final int i) {
                    ScanCallbackWrapperLollipop.this.handler.post(new Runnable() { // from class: no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplLollipop.ScanCallbackWrapperLollipop.1.3
                        @Override // java.lang.Runnable
                        @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
                        public void run() {
                            if (!ScanCallbackWrapperLollipop.this.scanSettings.getUseHardwareCallbackTypesIfSupported() || ScanCallbackWrapperLollipop.this.scanSettings.getCallbackType() == 1) {
                                ScanCallbackWrapperLollipop.this.handleScanError(i);
                                return;
                            }
                            ScanCallbackWrapperLollipop.this.scanSettings.disableUseHardwareCallbackTypes();
                            BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
                            try {
                                scanner.stopScan(ScanCallbackWrapperLollipop.this.scanCallback);
                            } catch (Exception unused) {
                            }
                            try {
                                scanner.startScanInternal(ScanCallbackWrapperLollipop.this.filters, ScanCallbackWrapperLollipop.this.scanSettings, ScanCallbackWrapperLollipop.this.scanCallback, ScanCallbackWrapperLollipop.this.handler);
                            } catch (Exception unused2) {
                            }
                        }
                    });
                }

                @Override // android.bluetooth.le.ScanCallback
                public void onScanResult(final int i, final android.bluetooth.le.ScanResult scanResult) {
                    ScanCallbackWrapperLollipop.this.handler.post(new Runnable() { // from class: no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplLollipop.ScanCallbackWrapperLollipop.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ScanCallbackWrapperLollipop.this.handleScanResult(i, ((BluetoothLeScannerImplLollipop) BluetoothLeScannerCompat.getScanner()).fromNativeScanResult(scanResult));
                        }
                    });
                }
            };
        }
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
    @RequiresPermission("android.permission.BLUETOOTH")
    public void flushPendingScanResults(@NonNull ScanCallback scanCallback) {
        ScanCallbackWrapperLollipop scanCallbackWrapperLollipop;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeUtils.checkAdapterStateOn(defaultAdapter);
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback cannot be null!");
        }
        synchronized (this.wrappers) {
            scanCallbackWrapperLollipop = this.wrappers.get(scanCallback);
        }
        if (scanCallbackWrapperLollipop == null) {
            throw new IllegalArgumentException("callback not registered!");
        }
        ScanSettings scanSettings = scanCallbackWrapperLollipop.scanSettings;
        if (!defaultAdapter.isOffloadedScanBatchingSupported() || !scanSettings.getUseHardwareBatchingIfSupported()) {
            scanCallbackWrapperLollipop.flushPendingScanResults();
            return;
        }
        BluetoothLeScanner bluetoothLeScanner = defaultAdapter.getBluetoothLeScanner();
        if (bluetoothLeScanner == null) {
            return;
        }
        bluetoothLeScanner.flushPendingScanResults(scanCallbackWrapperLollipop.nativeCallback);
    }

    @NonNull
    ScanResult fromNativeScanResult(@NonNull android.bluetooth.le.ScanResult scanResult) {
        return new ScanResult(scanResult.getDevice(), ScanRecord.parseFromBytes(scanResult.getScanRecord() != null ? scanResult.getScanRecord().getBytes() : null), scanResult.getRssi(), scanResult.getTimestampNanos());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NonNull
    public ArrayList<ScanResult> fromNativeScanResults(@NonNull List<android.bluetooth.le.ScanResult> list) {
        ArrayList<ScanResult> arrayList = new ArrayList<>();
        Iterator<android.bluetooth.le.ScanResult> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(fromNativeScanResult(it.next()));
        }
        return arrayList;
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    void startScanInternal(@NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull Context context, @NonNull PendingIntent pendingIntent) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeUtils.checkAdapterStateOn(defaultAdapter);
        if (defaultAdapter.getBluetoothLeScanner() == null) {
            throw new IllegalStateException("BT le scanner not available");
        }
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
        ScanCallbackWrapperLollipop scanCallbackWrapperLollipop;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeUtils.checkAdapterStateOn(defaultAdapter);
        BluetoothLeScanner bluetoothLeScanner = defaultAdapter.getBluetoothLeScanner();
        if (bluetoothLeScanner == null) {
            throw new IllegalStateException("BT le scanner not available");
        }
        boolean isOffloadedScanBatchingSupported = defaultAdapter.isOffloadedScanBatchingSupported();
        boolean isOffloadedFilteringSupported = defaultAdapter.isOffloadedFilteringSupported();
        synchronized (this.wrappers) {
            if (this.wrappers.containsKey(scanCallback)) {
                throw new IllegalArgumentException("scanner already started with given callback");
            }
            scanCallbackWrapperLollipop = new ScanCallbackWrapperLollipop(isOffloadedScanBatchingSupported, isOffloadedFilteringSupported, list, scanSettings, scanCallback, handler);
            this.wrappers.put(scanCallback, scanCallbackWrapperLollipop);
        }
        android.bluetooth.le.ScanSettings nativeScanSettings = toNativeScanSettings(defaultAdapter, scanSettings, false);
        ArrayList<android.bluetooth.le.ScanFilter> arrayList = null;
        if (!list.isEmpty() && isOffloadedFilteringSupported && scanSettings.getUseHardwareFilteringIfSupported()) {
            arrayList = toNativeScanFilters(list);
        }
        bluetoothLeScanner.startScan(arrayList, nativeScanSettings, scanCallbackWrapperLollipop.nativeCallback);
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    void stopScanInternal(@NonNull Context context, @NonNull PendingIntent pendingIntent) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeUtils.checkAdapterStateOn(defaultAdapter);
        if (defaultAdapter.getBluetoothLeScanner() == null) {
            throw new IllegalStateException("BT le scanner not available");
        }
        Intent intent = new Intent(context, (Class<?>) ScannerService.class);
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_PENDING_INTENT", pendingIntent);
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_START", false);
        context.startService(intent);
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    void stopScanInternal(@NonNull ScanCallback scanCallback) {
        ScanCallbackWrapperLollipop remove;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeUtils.checkAdapterStateOn(defaultAdapter);
        BluetoothLeScanner bluetoothLeScanner = defaultAdapter.getBluetoothLeScanner();
        if (bluetoothLeScanner == null) {
            throw new IllegalStateException("BT le scanner not available");
        }
        synchronized (this.wrappers) {
            remove = this.wrappers.remove(scanCallback);
        }
        if (remove == null) {
            return;
        }
        remove.close();
        bluetoothLeScanner.stopScan(remove.nativeCallback);
    }

    @NonNull
    android.bluetooth.le.ScanFilter toNativeScanFilter(@NonNull ScanFilter scanFilter) {
        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setDeviceAddress(scanFilter.getDeviceAddress()).setDeviceName(scanFilter.getDeviceName()).setServiceUuid(scanFilter.getServiceUuid(), scanFilter.getServiceUuidMask()).setManufacturerData(scanFilter.getManufacturerId(), scanFilter.getManufacturerData(), scanFilter.getManufacturerDataMask());
        if (scanFilter.getServiceDataUuid() != null) {
            builder.setServiceData(scanFilter.getServiceDataUuid(), scanFilter.getServiceData(), scanFilter.getServiceDataMask());
        }
        return builder.build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NonNull
    public ArrayList<android.bluetooth.le.ScanFilter> toNativeScanFilters(@NonNull List<ScanFilter> list) {
        ArrayList<android.bluetooth.le.ScanFilter> arrayList = new ArrayList<>();
        Iterator<ScanFilter> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(toNativeScanFilter(it.next()));
        }
        return arrayList;
    }

    @NonNull
    android.bluetooth.le.ScanSettings toNativeScanSettings(@NonNull BluetoothAdapter bluetoothAdapter, @NonNull ScanSettings scanSettings, boolean z) {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        if (z || (bluetoothAdapter.isOffloadedScanBatchingSupported() && scanSettings.getUseHardwareBatchingIfSupported())) {
            builder.setReportDelay(scanSettings.getReportDelayMillis());
        }
        if (scanSettings.getScanMode() != -1) {
            builder.setScanMode(scanSettings.getScanMode());
        } else {
            builder.setScanMode(0);
        }
        scanSettings.disableUseHardwareCallbackTypes();
        return builder.build();
    }
}
