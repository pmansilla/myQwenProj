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
public class ExperimentalButtonlessDfuImpl extends ButtonlessDfuImpl {
    protected static final UUID DEFAULT_EXPERIMENTAL_BUTTONLESS_DFU_SERVICE_UUID = new UUID(-8196551313441075360L, -6937650605005804976L);
    protected static final UUID DEFAULT_EXPERIMENTAL_BUTTONLESS_DFU_UUID = new UUID(-8196551313441075360L, -6937650605005804976L);
    protected static UUID EXPERIMENTAL_BUTTONLESS_DFU_SERVICE_UUID = DEFAULT_EXPERIMENTAL_BUTTONLESS_DFU_SERVICE_UUID;
    protected static UUID EXPERIMENTAL_BUTTONLESS_DFU_UUID = DEFAULT_EXPERIMENTAL_BUTTONLESS_DFU_UUID;
    private BluetoothGattCharacteristic mButtonlessDfuCharacteristic;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ExperimentalButtonlessDfuImpl(Intent intent, DfuBaseService dfuBaseService) {
        super(intent, dfuBaseService);
    }

    @Override // no.nordicsemi.android.dfu.ButtonlessDfuImpl
    protected BluetoothGattCharacteristic getButtonlessDfuCharacteristic() {
        return this.mButtonlessDfuCharacteristic;
    }

    @Override // no.nordicsemi.android.dfu.ButtonlessDfuImpl
    protected int getResponseType() {
        return 1;
    }

    @Override // no.nordicsemi.android.dfu.DfuService
    public boolean isClientCompatible(Intent intent, BluetoothGatt bluetoothGatt) {
        BluetoothGattService service = bluetoothGatt.getService(EXPERIMENTAL_BUTTONLESS_DFU_SERVICE_UUID);
        if (service == null) {
            return false;
        }
        this.mButtonlessDfuCharacteristic = service.getCharacteristic(EXPERIMENTAL_BUTTONLESS_DFU_UUID);
        return this.mButtonlessDfuCharacteristic != null;
    }

    @Override // no.nordicsemi.android.dfu.ButtonlessDfuImpl, no.nordicsemi.android.dfu.DfuService
    public void performDfu(Intent intent) throws DfuException, DeviceDisconnectedException, UploadAbortedException {
        logi("Experimental buttonless service found -> SDK 12.x");
        super.performDfu(intent);
    }

    @Override // no.nordicsemi.android.dfu.ButtonlessDfuImpl
    protected boolean shouldScanForBootloader() {
        return true;
    }
}
