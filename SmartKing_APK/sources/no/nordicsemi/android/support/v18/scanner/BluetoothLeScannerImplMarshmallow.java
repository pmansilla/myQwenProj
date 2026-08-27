package no.nordicsemi.android.support.v18.scanner;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanSettings;
import androidx.annotation.NonNull;

/* JADX INFO: Access modifiers changed from: package-private */
@TargetApi(23)
/* loaded from: classes2.dex */
public class BluetoothLeScannerImplMarshmallow extends BluetoothLeScannerImplLollipop {
    @Override // no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerImplLollipop
    @NonNull
    android.bluetooth.le.ScanSettings toNativeScanSettings(@NonNull BluetoothAdapter bluetoothAdapter, @NonNull ScanSettings scanSettings, boolean z) {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        if (z || (bluetoothAdapter.isOffloadedScanBatchingSupported() && scanSettings.getUseHardwareBatchingIfSupported())) {
            builder.setReportDelay(scanSettings.getReportDelayMillis());
        }
        if (z || scanSettings.getUseHardwareCallbackTypesIfSupported()) {
            builder.setCallbackType(scanSettings.getCallbackType()).setMatchMode(scanSettings.getMatchMode()).setNumOfMatches(scanSettings.getNumOfMatches());
        }
        builder.setScanMode(scanSettings.getScanMode());
        return builder.build();
    }
}
