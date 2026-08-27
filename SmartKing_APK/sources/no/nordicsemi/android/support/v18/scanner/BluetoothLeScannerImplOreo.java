package no.nordicsemi.android.support.v18.scanner;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;

/* JADX INFO: Access modifiers changed from: package-private */
@TargetApi(26)
/* loaded from: classes2.dex */
public class BluetoothLeScannerImplOreo extends BluetoothLeScannerImplMarshmallow {

    @NonNull
    private final HashMap<PendingIntent, PendingIntentExecutorWrapper> wrappers = new HashMap<>();

    /* loaded from: classes2.dex */
    static class PendingIntentExecutorWrapper extends BluetoothLeScannerCompat.ScanCallbackWrapper {

        @NonNull
        final PendingIntentExecutor executor;

        /* JADX INFO: Access modifiers changed from: package-private */
        public PendingIntentExecutorWrapper(boolean z, boolean z2, @NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull PendingIntent pendingIntent) {
            super(z, z2, list, scanSettings, new PendingIntentExecutor(pendingIntent, scanSettings), new Handler());
            this.executor = (PendingIntentExecutor) this.scanCallback;
        }
    }

    @NonNull
    private PendingIntent createStartingPendingIntent(@NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull Context context, @NonNull PendingIntent pendingIntent) {
        int hashCode = pendingIntent.hashCode();
        Intent intent = new Intent(context, (Class<?>) PendingIntentReceiver.class);
        intent.setAction("no.nordicsemi.android.support.v18.ACTION_FOUND");
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_PENDING_INTENT", pendingIntent);
        intent.putParcelableArrayListExtra("no.nordicsemi.android.support.v18.EXTRA_FILTERS", toNativeScanFilters(list));
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_SETTINGS", toNativeScanSettings(defaultAdapter, scanSettings, true));
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_USE_HARDWARE_BATCHING", scanSettings.getUseHardwareBatchingIfSupported());
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_USE_HARDWARE_FILTERING", scanSettings.getUseHardwareFilteringIfSupported());
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_USE_HARDWARE_CALLBACK_TYPES", scanSettings.getUseHardwareCallbackTypesIfSupported());
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_MATCH_MODE", scanSettings.getMatchMode());
        intent.putExtra("no.nordicsemi.android.support.v18.EXTRA_NUM_OF_MATCHES", scanSettings.getNumOfMatches());
        return PendingIntent.getBroadcast(context, hashCode, intent, AMapEngineUtils.HALF_MAX_P20_WIDTH);
    }

    @NonNull
    private PendingIntent createStoppingPendingIntent(@NonNull Context context, @NonNull PendingIntent pendingIntent) {
        int hashCode = pendingIntent.hashCode();
        Intent intent = new Intent(context, (Class<?>) PendingIntentReceiver.class);
        intent.setAction("no.nordicsemi.android.support.v18.ACTION_FOUND");
        return PendingIntent.getBroadcast(context, hashCode, intent, AMapEngineUtils.MAX_P20_WIDTH);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addWrapper(@NonNull PendingIntent pendingIntent, @NonNull PendingIntentExecutorWrapper pendingIntentExecutorWrapper) {
        synchronized (this.wrappers) {
            this.wrappers.put(pendingIntent, pendingIntentExecutorWrapper);
        }
    }

    @NonNull
    ScanFilter fromNativeScanFilter(@NonNull android.bluetooth.le.ScanFilter scanFilter) {
        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setDeviceAddress(scanFilter.getDeviceAddress()).setDeviceName(scanFilter.getDeviceName()).setServiceUuid(scanFilter.getServiceUuid(), scanFilter.getServiceUuidMask()).setManufacturerData(scanFilter.getManufacturerId(), scanFilter.getManufacturerData(), scanFilter.getManufacturerDataMask());
        if (scanFilter.getServiceDataUuid() != null) {
            builder.setServiceData(scanFilter.getServiceDataUuid(), scanFilter.getServiceData(), scanFilter.getServiceDataMask());
        }
        return builder.build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NonNull
    public ArrayList<ScanFilter> fromNativeScanFilters(@NonNull List<android.bluetooth.le.ScanFilter> list) {
        ArrayList<ScanFilter> arrayList = new ArrayList<>();
        Iterator<android.bluetooth.le.ScanFilter> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(fromNativeScanFilter(it.next()));
        }
        return arrayList;
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplLollipop
    @NonNull
    ScanResult fromNativeScanResult(@NonNull android.bluetooth.le.ScanResult scanResult) {
        return new ScanResult(scanResult.getDevice(), (scanResult.getDataStatus() << 5) | (scanResult.isLegacy() ? 16 : 0) | scanResult.isConnectable(), scanResult.getPrimaryPhy(), scanResult.getSecondaryPhy(), scanResult.getAdvertisingSid(), scanResult.getTxPower(), scanResult.getRssi(), scanResult.getPeriodicAdvertisingInterval(), ScanRecord.parseFromBytes(scanResult.getScanRecord() != null ? scanResult.getScanRecord().getBytes() : null), scanResult.getTimestampNanos());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NonNull
    public ScanSettings fromNativeScanSettings(@NonNull android.bluetooth.le.ScanSettings scanSettings, boolean z, boolean z2, boolean z3, long j, long j2, int i, int i2) {
        return new ScanSettings.Builder().setLegacy(scanSettings.getLegacy()).setPhy(scanSettings.getPhy()).setCallbackType(scanSettings.getCallbackType()).setScanMode(scanSettings.getScanMode()).setReportDelay(scanSettings.getReportDelayMillis()).setUseHardwareBatchingIfSupported(z).setUseHardwareFilteringIfSupported(z2).setUseHardwareCallbackTypesIfSupported(z3).setMatchOptions(j, j2).setMatchMode(i).setNumOfMatches(i2).build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public PendingIntentExecutorWrapper getWrapper(@NonNull PendingIntent pendingIntent) {
        synchronized (this.wrappers) {
            if (!this.wrappers.containsKey(pendingIntent)) {
                return null;
            }
            PendingIntentExecutorWrapper pendingIntentExecutorWrapper = this.wrappers.get(pendingIntent);
            if (pendingIntentExecutorWrapper != null) {
                return pendingIntentExecutorWrapper;
            }
            throw new IllegalStateException("Scanning has been stopped");
        }
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplLollipop, no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    void startScanInternal(@Nullable List<ScanFilter> list, @Nullable ScanSettings scanSettings, @NonNull Context context, @NonNull PendingIntent pendingIntent) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeUtils.checkAdapterStateOn(defaultAdapter);
        BluetoothLeScanner bluetoothLeScanner = defaultAdapter.getBluetoothLeScanner();
        if (bluetoothLeScanner == null) {
            throw new IllegalStateException("BT le scanner not available");
        }
        if (scanSettings == null) {
            scanSettings = new ScanSettings.Builder().build();
        }
        List<ScanFilter> emptyList = list != null ? list : Collections.emptyList();
        android.bluetooth.le.ScanSettings nativeScanSettings = toNativeScanSettings(defaultAdapter, scanSettings, false);
        ArrayList<android.bluetooth.le.ScanFilter> arrayList = null;
        if (list != null && defaultAdapter.isOffloadedFilteringSupported() && scanSettings.getUseHardwareFilteringIfSupported()) {
            arrayList = toNativeScanFilters(list);
        }
        synchronized (this.wrappers) {
            this.wrappers.remove(pendingIntent);
        }
        bluetoothLeScanner.startScan(arrayList, nativeScanSettings, createStartingPendingIntent(emptyList, scanSettings, context, pendingIntent));
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplLollipop, no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    void stopScanInternal(@NonNull Context context, @NonNull PendingIntent pendingIntent) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeUtils.checkAdapterStateOn(defaultAdapter);
        BluetoothLeScanner bluetoothLeScanner = defaultAdapter.getBluetoothLeScanner();
        if (bluetoothLeScanner == null) {
            throw new IllegalStateException("BT le scanner not available");
        }
        bluetoothLeScanner.stopScan(createStoppingPendingIntent(context, pendingIntent));
        synchronized (this.wrappers) {
            this.wrappers.put(pendingIntent, null);
        }
    }

    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplMarshmallow, no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplLollipop
    @NonNull
    android.bluetooth.le.ScanSettings toNativeScanSettings(@NonNull BluetoothAdapter bluetoothAdapter, @NonNull ScanSettings scanSettings, boolean z) {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        if (z || (bluetoothAdapter.isOffloadedScanBatchingSupported() && scanSettings.getUseHardwareBatchingIfSupported())) {
            builder.setReportDelay(scanSettings.getReportDelayMillis());
        }
        if (z || scanSettings.getUseHardwareCallbackTypesIfSupported()) {
            builder.setCallbackType(scanSettings.getCallbackType()).setMatchMode(scanSettings.getMatchMode()).setNumOfMatches(scanSettings.getNumOfMatches());
        }
        builder.setScanMode(scanSettings.getScanMode()).setLegacy(scanSettings.getLegacy()).setPhy(scanSettings.getPhy());
        return builder.build();
    }
}
