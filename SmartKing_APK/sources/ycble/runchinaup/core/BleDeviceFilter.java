package ycble.runchinaup.core;

import android.bluetooth.BluetoothDevice;
import ycble.runchinaup.device.BleDevice;

/* loaded from: classes2.dex */
public abstract class BleDeviceFilter<T extends BleDevice> {
    public abstract boolean filter(T t);

    public T parserDevice(BluetoothDevice bluetoothDevice, byte[] bArr, int i) {
        return (T) BleDevice.parserFromScanData(bluetoothDevice, bArr, i);
    }
}
