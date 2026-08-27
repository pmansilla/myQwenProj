package no.nordicsemi.android.dfu;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.UUID;
import no.nordicsemi.android.dfu.BaseCustomDfuImpl;
import no.nordicsemi.android.dfu.internal.exception.DeviceDisconnectedException;
import no.nordicsemi.android.dfu.internal.exception.DfuException;
import no.nordicsemi.android.dfu.internal.exception.UnknownResponseException;
import no.nordicsemi.android.dfu.internal.exception.UploadAbortedException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class LegacyDfuImpl extends BaseCustomDfuImpl {
    private static final int DFU_STATUS_SUCCESS = 1;
    private static final int OP_CODE_ACTIVATE_AND_RESET_KEY = 5;
    private static final int OP_CODE_INIT_DFU_PARAMS_KEY = 2;
    private static final int OP_CODE_PACKET_RECEIPT_NOTIF_KEY = 17;
    private static final int OP_CODE_PACKET_RECEIPT_NOTIF_REQ_KEY = 8;
    private static final int OP_CODE_RECEIVE_FIRMWARE_IMAGE_KEY = 3;
    private static final int OP_CODE_RESET_KEY = 6;
    private static final int OP_CODE_RESPONSE_CODE_KEY = 16;
    private static final int OP_CODE_START_DFU_KEY = 1;
    private static final int OP_CODE_VALIDATE_KEY = 4;
    private final LegacyBluetoothCallback mBluetoothCallback;
    private BluetoothGattCharacteristic mControlPointCharacteristic;
    private boolean mImageSizeInProgress;
    private BluetoothGattCharacteristic mPacketCharacteristic;
    protected static final UUID DEFAULT_DFU_SERVICE_UUID = new UUID(23296205844446L, 1523193452336828707L);
    protected static final UUID DEFAULT_DFU_CONTROL_POINT_UUID = new UUID(23300500811742L, 1523193452336828707L);
    protected static final UUID DEFAULT_DFU_PACKET_UUID = new UUID(23304795779038L, 1523193452336828707L);
    protected static final UUID DEFAULT_DFU_VERSION_UUID = new UUID(23313385713630L, 1523193452336828707L);
    protected static UUID DFU_SERVICE_UUID = DEFAULT_DFU_SERVICE_UUID;
    protected static UUID DFU_CONTROL_POINT_UUID = DEFAULT_DFU_CONTROL_POINT_UUID;
    protected static UUID DFU_PACKET_UUID = DEFAULT_DFU_PACKET_UUID;
    protected static UUID DFU_VERSION_UUID = DEFAULT_DFU_VERSION_UUID;
    private static final byte[] OP_CODE_START_DFU = {1, 0};
    private static final byte[] OP_CODE_INIT_DFU_PARAMS = {2};
    private static final byte[] OP_CODE_INIT_DFU_PARAMS_START = {2, 0};
    private static final byte[] OP_CODE_INIT_DFU_PARAMS_COMPLETE = {2, 1};
    private static final byte[] OP_CODE_RECEIVE_FIRMWARE_IMAGE = {3};
    private static final byte[] OP_CODE_VALIDATE = {4};
    private static final byte[] OP_CODE_ACTIVATE_AND_RESET = {5};
    private static final byte[] OP_CODE_RESET = {6};
    private static final byte[] OP_CODE_PACKET_RECEIPT_NOTIF_REQ = {8, 0, 0};

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public class LegacyBluetoothCallback extends BaseCustomDfuImpl.BaseCustomBluetoothCallback {
        protected LegacyBluetoothCallback() {
            super();
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            if (bluetoothGattCharacteristic.getIntValue(17, 0).intValue() == 17) {
                LegacyDfuImpl.this.mProgressInfo.setBytesReceived(bluetoothGattCharacteristic.getIntValue(20, 1).intValue());
                handlePacketReceiptNotification(bluetoothGatt, bluetoothGattCharacteristic);
            } else if (!LegacyDfuImpl.this.mRemoteErrorOccurred) {
                if (bluetoothGattCharacteristic.getIntValue(17, 2).intValue() != 1) {
                    LegacyDfuImpl.this.mRemoteErrorOccurred = true;
                }
                handleNotification(bluetoothGatt, bluetoothGattCharacteristic);
            }
            LegacyDfuImpl.this.notifyLock();
        }

        @Override // no.nordicsemi.android.dfu.BaseCustomDfuImpl.BaseCustomBluetoothCallback
        protected void onPacketCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            if (LegacyDfuImpl.this.mImageSizeInProgress) {
                LegacyDfuImpl.this.mService.sendLogBroadcast(5, "Data written to " + bluetoothGattCharacteristic.getUuid() + ", value (0x): " + parse(bluetoothGattCharacteristic));
                LegacyDfuImpl.this.mImageSizeInProgress = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LegacyDfuImpl(Intent intent, DfuBaseService dfuBaseService) {
        super(intent, dfuBaseService);
        this.mBluetoothCallback = new LegacyBluetoothCallback();
    }

    private int getStatusCode(byte[] bArr, int i) throws UnknownResponseException {
        if (bArr == null || bArr.length != 3 || bArr[0] != 16 || bArr[1] != i || bArr[2] < 1 || bArr[2] > 6) {
            throw new UnknownResponseException("Invalid response received", bArr, 16, i);
        }
        return bArr[2];
    }

    private int readVersion(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (bluetoothGattCharacteristic != null) {
            return bluetoothGattCharacteristic.getIntValue(18, 0).intValue();
        }
        return 0;
    }

    private void resetAndRestart(BluetoothGatt bluetoothGatt, Intent intent) throws DfuException, DeviceDisconnectedException, UploadAbortedException {
        this.mService.sendLogBroadcast(15, "Last upload interrupted. Restarting device...");
        this.mProgressInfo.setProgress(-5);
        logi("Sending Reset command (Op Code = 6)");
        writeOpCode(this.mControlPointCharacteristic, OP_CODE_RESET);
        this.mService.sendLogBroadcast(10, "Reset request sent");
        this.mService.waitUntilDisconnected();
        this.mService.sendLogBroadcast(5, "Disconnected by the remote device");
        BluetoothGattService service = bluetoothGatt.getService(GENERIC_ATTRIBUTE_SERVICE_UUID);
        this.mService.refreshDeviceCache(bluetoothGatt, !((service == null || service.getCharacteristic(SERVICE_CHANGED_UUID) == null) ? false : true));
        this.mService.close(bluetoothGatt);
        logi("Restarting the service");
        Intent intent2 = new Intent();
        intent2.fillIn(intent, 24);
        restartService(intent2, false);
    }

    private void setNumberOfPackets(byte[] bArr, int i) {
        bArr[1] = (byte) (i & 255);
        bArr[2] = (byte) ((i >> 8) & 255);
    }

    private void writeImageSize(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) throws DeviceDisconnectedException, DfuException, UploadAbortedException {
        this.mReceivedData = null;
        this.mError = 0;
        this.mImageSizeInProgress = true;
        bluetoothGattCharacteristic.setWriteType(1);
        bluetoothGattCharacteristic.setValue(new byte[4]);
        bluetoothGattCharacteristic.setValue(i, 20, 0);
        this.mService.sendLogBroadcast(1, "Writing to characteristic " + bluetoothGattCharacteristic.getUuid());
        this.mService.sendLogBroadcast(0, "gatt.writeCharacteristic(" + bluetoothGattCharacteristic.getUuid() + SQLBuilder.PARENTHESES_RIGHT);
        this.mGatt.writeCharacteristic(bluetoothGattCharacteristic);
        try {
            synchronized (this.mLock) {
                while (true) {
                    if ((!this.mImageSizeInProgress || !this.mConnected || this.mError != 0 || this.mAborted) && !this.mPaused) {
                        break;
                    } else {
                        this.mLock.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            loge("Sleeping interrupted", e);
        }
        if (this.mAborted) {
            throw new UploadAbortedException();
        }
        if (this.mError != 0) {
            throw new DfuException("Unable to write Image Size", this.mError);
        }
        if (!this.mConnected) {
            throw new DeviceDisconnectedException("Unable to write Image Size: device disconnected");
        }
    }

    private void writeImageSize(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i, int i2, int i3) throws DeviceDisconnectedException, DfuException, UploadAbortedException {
        this.mReceivedData = null;
        this.mError = 0;
        this.mImageSizeInProgress = true;
        bluetoothGattCharacteristic.setWriteType(1);
        bluetoothGattCharacteristic.setValue(new byte[12]);
        bluetoothGattCharacteristic.setValue(i, 20, 0);
        bluetoothGattCharacteristic.setValue(i2, 20, 4);
        bluetoothGattCharacteristic.setValue(i3, 20, 8);
        this.mService.sendLogBroadcast(1, "Writing to characteristic " + bluetoothGattCharacteristic.getUuid());
        this.mService.sendLogBroadcast(0, "gatt.writeCharacteristic(" + bluetoothGattCharacteristic.getUuid() + SQLBuilder.PARENTHESES_RIGHT);
        this.mGatt.writeCharacteristic(bluetoothGattCharacteristic);
        try {
            synchronized (this.mLock) {
                while (true) {
                    if ((!this.mImageSizeInProgress || !this.mConnected || this.mError != 0 || this.mAborted) && !this.mPaused) {
                        break;
                    } else {
                        this.mLock.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            loge("Sleeping interrupted", e);
        }
        if (this.mAborted) {
            throw new UploadAbortedException();
        }
        if (this.mError != 0) {
            throw new DfuException("Unable to write Image Sizes", this.mError);
        }
        if (!this.mConnected) {
            throw new DeviceDisconnectedException("Unable to write Image Sizes: device disconnected");
        }
    }

    private void writeOpCode(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) throws DeviceDisconnectedException, DfuException, UploadAbortedException {
        writeOpCode(bluetoothGattCharacteristic, bArr, bArr[0] == 6 || bArr[0] == 5);
    }

    @Override // no.nordicsemi.android.dfu.BaseCustomDfuImpl
    protected UUID getControlPointCharacteristicUUID() {
        return DFU_CONTROL_POINT_UUID;
    }

    @Override // no.nordicsemi.android.dfu.BaseCustomDfuImpl
    protected UUID getDfuServiceUUID() {
        return DFU_SERVICE_UUID;
    }

    @Override // no.nordicsemi.android.dfu.DfuCallback
    public BaseCustomDfuImpl.BaseCustomBluetoothCallback getGattCallback() {
        return this.mBluetoothCallback;
    }

    @Override // no.nordicsemi.android.dfu.BaseCustomDfuImpl
    protected UUID getPacketCharacteristicUUID() {
        return DFU_PACKET_UUID;
    }

    @Override // no.nordicsemi.android.dfu.DfuService
    public boolean isClientCompatible(Intent intent, BluetoothGatt bluetoothGatt) {
        BluetoothGattService service = bluetoothGatt.getService(DFU_SERVICE_UUID);
        if (service == null) {
            return false;
        }
        this.mControlPointCharacteristic = service.getCharacteristic(DFU_CONTROL_POINT_UUID);
        this.mPacketCharacteristic = service.getCharacteristic(DFU_PACKET_UUID);
        return (this.mControlPointCharacteristic == null || this.mPacketCharacteristic == null) ? false : true;
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0431 A[Catch: RemoteDfuException -> 0x060e, UnknownResponseException -> 0x0668, UploadAbortedException -> 0x0697, TryCatch #3 {RemoteDfuException -> 0x060e, blocks: (B:10:0x0057, B:12:0x006e, B:13:0x0072, B:15:0x0076, B:16:0x007a, B:18:0x007e, B:19:0x0082, B:21:0x0088, B:23:0x0092, B:25:0x00b9, B:35:0x036c, B:37:0x0370, B:39:0x037a, B:40:0x03e6, B:43:0x0417, B:44:0x041e, B:45:0x03ba, B:47:0x0421, B:49:0x0425, B:54:0x0431, B:55:0x0471, B:57:0x048e, B:58:0x049f, B:60:0x0504, B:62:0x05bc, B:65:0x05e7, B:68:0x05ec, B:69:0x05f3, B:70:0x05f4, B:71:0x05fb, B:74:0x05fd, B:75:0x0603, B:76:0x042d, B:99:0x02c3, B:103:0x02cd, B:105:0x0365, B:109:0x0604, B:110:0x060b, B:111:0x060c, B:112:0x060d), top: B:9:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0504 A[Catch: RemoteDfuException -> 0x060e, UnknownResponseException -> 0x0668, UploadAbortedException -> 0x0697, TryCatch #3 {RemoteDfuException -> 0x060e, blocks: (B:10:0x0057, B:12:0x006e, B:13:0x0072, B:15:0x0076, B:16:0x007a, B:18:0x007e, B:19:0x0082, B:21:0x0088, B:23:0x0092, B:25:0x00b9, B:35:0x036c, B:37:0x0370, B:39:0x037a, B:40:0x03e6, B:43:0x0417, B:44:0x041e, B:45:0x03ba, B:47:0x0421, B:49:0x0425, B:54:0x0431, B:55:0x0471, B:57:0x048e, B:58:0x049f, B:60:0x0504, B:62:0x05bc, B:65:0x05e7, B:68:0x05ec, B:69:0x05f3, B:70:0x05f4, B:71:0x05fb, B:74:0x05fd, B:75:0x0603, B:76:0x042d, B:99:0x02c3, B:103:0x02cd, B:105:0x0365, B:109:0x0604, B:110:0x060b, B:111:0x060c, B:112:0x060d), top: B:9:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x05f4 A[Catch: RemoteDfuException -> 0x060e, UnknownResponseException -> 0x0668, UploadAbortedException -> 0x0697, TryCatch #3 {RemoteDfuException -> 0x060e, blocks: (B:10:0x0057, B:12:0x006e, B:13:0x0072, B:15:0x0076, B:16:0x007a, B:18:0x007e, B:19:0x0082, B:21:0x0088, B:23:0x0092, B:25:0x00b9, B:35:0x036c, B:37:0x0370, B:39:0x037a, B:40:0x03e6, B:43:0x0417, B:44:0x041e, B:45:0x03ba, B:47:0x0421, B:49:0x0425, B:54:0x0431, B:55:0x0471, B:57:0x048e, B:58:0x049f, B:60:0x0504, B:62:0x05bc, B:65:0x05e7, B:68:0x05ec, B:69:0x05f3, B:70:0x05f4, B:71:0x05fb, B:74:0x05fd, B:75:0x0603, B:76:0x042d, B:99:0x02c3, B:103:0x02cd, B:105:0x0365, B:109:0x0604, B:110:0x060b, B:111:0x060c, B:112:0x060d), top: B:9:0x0057 }] */
    @Override // no.nordicsemi.android.dfu.DfuService
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void performDfu(android.content.Intent r19) throws no.nordicsemi.android.dfu.internal.exception.DfuException, no.nordicsemi.android.dfu.internal.exception.DeviceDisconnectedException, no.nordicsemi.android.dfu.internal.exception.UploadAbortedException {
        /*
            Method dump skipped, instructions count: 1712
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: no.nordicsemi.android.dfu.LegacyDfuImpl.performDfu(android.content.Intent):void");
    }
}
