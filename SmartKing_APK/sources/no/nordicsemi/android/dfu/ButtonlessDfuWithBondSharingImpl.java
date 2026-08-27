package no.nordicsemi.android.dfu;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import java.util.UUID;
import no.nordicsemi.android.dfu.internal.exception.DeviceDisconnectedException;
import no.nordicsemi.android.dfu.internal.exception.DfuException;
import no.nordicsemi.android.dfu.internal.exception.UploadAbortedException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ButtonlessDfuWithBondSharingImpl extends ButtonlessDfuImpl {
    private BluetoothGattCharacteristic mButtonlessDfuCharacteristic;
    protected static final UUID DEFAULT_BUTTONLESS_DFU_SERVICE_UUID = SecureDfuImpl.DEFAULT_DFU_SERVICE_UUID;
    protected static final UUID DEFAULT_BUTTONLESS_DFU_UUID = new UUID(-8157989228746813600L, -6937650605005804976L);
    protected static UUID BUTTONLESS_DFU_SERVICE_UUID = DEFAULT_BUTTONLESS_DFU_SERVICE_UUID;
    protected static UUID BUTTONLESS_DFU_UUID = DEFAULT_BUTTONLESS_DFU_UUID;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ButtonlessDfuWithBondSharingImpl(Intent intent, DfuBaseService dfuBaseService) {
        super(intent, dfuBaseService);
    }

    @Override // no.nordicsemi.android.dfu.ButtonlessDfuImpl
    protected BluetoothGattCharacteristic getButtonlessDfuCharacteristic() {
        return this.mButtonlessDfuCharacteristic;
    }

    @Override // no.nordicsemi.android.dfu.ButtonlessDfuImpl
    protected int getResponseType() {
        return 2;
    }

    @Override // no.nordicsemi.android.dfu.DfuService
    public boolean isClientCompatible(Intent intent, BluetoothGatt bluetoothGatt) {
        BluetoothGattService service = bluetoothGatt.getService(BUTTONLESS_DFU_SERVICE_UUID);
        if (service == null) {
            return false;
        }
        this.mButtonlessDfuCharacteristic = service.getCharacteristic(BUTTONLESS_DFU_UUID);
        return this.mButtonlessDfuCharacteristic != null;
    }

    @Override // no.nordicsemi.android.dfu.ButtonlessDfuImpl, no.nordicsemi.android.dfu.DfuService
    public void performDfu(Intent intent) throws DfuException, DeviceDisconnectedException, UploadAbortedException {
        logi("Buttonless service with bond sharing found -> SDK 14 or newer");
        if (isBonded()) {
            intent.putExtra(DfuBaseService.EXTRA_KEEP_BOND, true);
            intent.putExtra(DfuBaseService.EXTRA_RESTORE_BOND, false);
            super.performDfu(intent);
        } else {
            logw("Device is not paired, cancelling DFU");
            this.mService.sendLogBroadcast(15, "Device is not bonded");
            this.mService.terminateConnection(this.mGatt, DfuBaseService.ERROR_DEVICE_NOT_BONDED);
        }
    }

    @Override // no.nordicsemi.android.dfu.ButtonlessDfuImpl
    protected boolean shouldScanForBootloader() {
        return false;
    }
}
