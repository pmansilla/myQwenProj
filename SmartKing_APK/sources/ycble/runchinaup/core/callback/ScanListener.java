package ycble.runchinaup.core.callback;

import ycble.runchinaup.device.BleDevice;

/* loaded from: classes2.dex */
public interface ScanListener<T extends BleDevice> {
    void onFailure(int i);

    void onScan(T t);
}
