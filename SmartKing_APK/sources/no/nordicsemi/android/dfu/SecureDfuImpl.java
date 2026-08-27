package no.nordicsemi.android.dfu;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Build;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.InputStream;
import java.util.Locale;
import java.util.UUID;
import javax.mail.UIDFolder;
import no.nordicsemi.android.dfu.BaseCustomDfuImpl;
import no.nordicsemi.android.dfu.BaseDfuImpl;
import no.nordicsemi.android.dfu.internal.ArchiveInputStream;
import no.nordicsemi.android.dfu.internal.exception.DeviceDisconnectedException;
import no.nordicsemi.android.dfu.internal.exception.DfuException;
import no.nordicsemi.android.dfu.internal.exception.RemoteDfuException;
import no.nordicsemi.android.dfu.internal.exception.RemoteDfuExtendedErrorException;
import no.nordicsemi.android.dfu.internal.exception.UnknownResponseException;
import no.nordicsemi.android.dfu.internal.exception.UploadAbortedException;
import no.nordicsemi.android.error.SecureDfuError;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class SecureDfuImpl extends BaseCustomDfuImpl {
    private static final int DFU_STATUS_SUCCESS = 1;
    private static final int MAX_ATTEMPTS = 3;
    private static final int OBJECT_COMMAND = 1;
    private static final int OBJECT_DATA = 2;
    private static final int OP_CODE_CALCULATE_CHECKSUM_KEY = 3;
    private static final int OP_CODE_CREATE_KEY = 1;
    private static final int OP_CODE_EXECUTE_KEY = 4;
    private static final int OP_CODE_PACKET_RECEIPT_NOTIF_REQ_KEY = 2;
    private static final int OP_CODE_RESPONSE_CODE_KEY = 96;
    private static final int OP_CODE_SELECT_OBJECT_KEY = 6;
    private final SecureBluetoothCallback mBluetoothCallback;
    private BluetoothGattCharacteristic mControlPointCharacteristic;
    private BluetoothGattCharacteristic mPacketCharacteristic;
    protected static final UUID DEFAULT_DFU_SERVICE_UUID = new UUID(279658205548544L, -9223371485494954757L);
    protected static final UUID DEFAULT_DFU_CONTROL_POINT_UUID = new UUID(-8157989241631715488L, -6937650605005804976L);
    protected static final UUID DEFAULT_DFU_PACKET_UUID = new UUID(-8157989237336748192L, -6937650605005804976L);
    protected static UUID DFU_SERVICE_UUID = DEFAULT_DFU_SERVICE_UUID;
    protected static UUID DFU_CONTROL_POINT_UUID = DEFAULT_DFU_CONTROL_POINT_UUID;
    protected static UUID DFU_PACKET_UUID = DEFAULT_DFU_PACKET_UUID;
    private static final byte[] OP_CODE_CREATE_COMMAND = {1, 1, 0, 0, 0, 0};
    private static final byte[] OP_CODE_CREATE_DATA = {1, 2, 0, 0, 0, 0};
    private static final byte[] OP_CODE_PACKET_RECEIPT_NOTIF_REQ = {2, 0, 0};
    private static final byte[] OP_CODE_CALCULATE_CHECKSUM = {3};
    private static final byte[] OP_CODE_EXECUTE = {4};
    private static final byte[] OP_CODE_SELECT_OBJECT = {6, 0};

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ObjectChecksum {
        protected int CRC32;
        protected int offset;

        private ObjectChecksum() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ObjectInfo extends ObjectChecksum {
        protected int maxSize;

        private ObjectInfo() {
            super();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public class SecureBluetoothCallback extends BaseCustomDfuImpl.BaseCustomBluetoothCallback {
        protected SecureBluetoothCallback() {
            super();
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            if (bluetoothGattCharacteristic.getValue() == null || bluetoothGattCharacteristic.getValue().length < 3) {
                SecureDfuImpl.this.loge("Empty response: " + parse(bluetoothGattCharacteristic));
                SecureDfuImpl.this.mError = DfuBaseService.ERROR_INVALID_RESPONSE;
                SecureDfuImpl.this.notifyLock();
                return;
            }
            if (bluetoothGattCharacteristic.getIntValue(17, 0).intValue() != 96) {
                SecureDfuImpl.this.loge("Invalid response: " + parse(bluetoothGattCharacteristic));
                SecureDfuImpl.this.mError = DfuBaseService.ERROR_INVALID_RESPONSE;
            } else if (bluetoothGattCharacteristic.getIntValue(17, 1).intValue() == 3) {
                int intValue = bluetoothGattCharacteristic.getIntValue(20, 3).intValue();
                if (((int) (((ArchiveInputStream) SecureDfuImpl.this.mFirmwareStream).getCrc32() & UIDFolder.MAXUID)) == bluetoothGattCharacteristic.getIntValue(20, 7).intValue()) {
                    SecureDfuImpl.this.mProgressInfo.setBytesReceived(intValue);
                } else if (SecureDfuImpl.this.mFirmwareUploadInProgress) {
                    SecureDfuImpl.this.mFirmwareUploadInProgress = false;
                    SecureDfuImpl.this.notifyLock();
                    return;
                }
                handlePacketReceiptNotification(bluetoothGatt, bluetoothGattCharacteristic);
            } else if (!SecureDfuImpl.this.mRemoteErrorOccurred) {
                if (bluetoothGattCharacteristic.getIntValue(17, 2).intValue() != 1) {
                    SecureDfuImpl.this.mRemoteErrorOccurred = true;
                }
                handleNotification(bluetoothGatt, bluetoothGattCharacteristic);
            }
            SecureDfuImpl.this.notifyLock();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SecureDfuImpl(Intent intent, DfuBaseService dfuBaseService) {
        super(intent, dfuBaseService);
        this.mBluetoothCallback = new SecureBluetoothCallback();
    }

    private int getStatusCode(byte[] bArr, int i) throws UnknownResponseException {
        if (bArr != null && bArr.length >= 3 && bArr[0] == 96 && bArr[1] == i && (bArr[2] == 1 || bArr[2] == 2 || bArr[2] == 3 || bArr[2] == 4 || bArr[2] == 5 || bArr[2] == 7 || bArr[2] == 8 || bArr[2] == 10 || bArr[2] == 11)) {
            return bArr[2];
        }
        throw new UnknownResponseException("Invalid response received", bArr, 96, i);
    }

    private ObjectChecksum readChecksum() throws DeviceDisconnectedException, DfuException, UploadAbortedException, RemoteDfuException, UnknownResponseException {
        if (!this.mConnected) {
            throw new DeviceDisconnectedException("Unable to read Checksum: device disconnected");
        }
        writeOpCode(this.mControlPointCharacteristic, OP_CODE_CALCULATE_CHECKSUM);
        byte[] readNotificationResponse = readNotificationResponse();
        int statusCode = getStatusCode(readNotificationResponse, 3);
        if (statusCode == 11) {
            throw new RemoteDfuExtendedErrorException("Receiving Checksum failed", readNotificationResponse[3]);
        }
        if (statusCode != 1) {
            throw new RemoteDfuException("Receiving Checksum failed", statusCode);
        }
        ObjectChecksum objectChecksum = new ObjectChecksum();
        objectChecksum.offset = this.mControlPointCharacteristic.getIntValue(20, 3).intValue();
        objectChecksum.CRC32 = this.mControlPointCharacteristic.getIntValue(20, 7).intValue();
        return objectChecksum;
    }

    private ObjectInfo selectObject(int i) throws DeviceDisconnectedException, DfuException, UploadAbortedException, RemoteDfuException, UnknownResponseException {
        if (!this.mConnected) {
            throw new DeviceDisconnectedException("Unable to read object info: device disconnected");
        }
        OP_CODE_SELECT_OBJECT[1] = (byte) i;
        writeOpCode(this.mControlPointCharacteristic, OP_CODE_SELECT_OBJECT);
        byte[] readNotificationResponse = readNotificationResponse();
        int statusCode = getStatusCode(readNotificationResponse, 6);
        if (statusCode == 11) {
            throw new RemoteDfuExtendedErrorException("Selecting object failed", readNotificationResponse[3]);
        }
        if (statusCode != 1) {
            throw new RemoteDfuException("Selecting object failed", statusCode);
        }
        ObjectInfo objectInfo = new ObjectInfo();
        objectInfo.maxSize = this.mControlPointCharacteristic.getIntValue(20, 3).intValue();
        objectInfo.offset = this.mControlPointCharacteristic.getIntValue(20, 7).intValue();
        objectInfo.CRC32 = this.mControlPointCharacteristic.getIntValue(20, 11).intValue();
        return objectInfo;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x01cd  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x03ed  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void sendFirmware(android.bluetooth.BluetoothGatt r21) throws no.nordicsemi.android.dfu.internal.exception.RemoteDfuException, no.nordicsemi.android.dfu.internal.exception.DeviceDisconnectedException, no.nordicsemi.android.dfu.internal.exception.DfuException, no.nordicsemi.android.dfu.internal.exception.UploadAbortedException, no.nordicsemi.android.dfu.internal.exception.UnknownResponseException {
        /*
            Method dump skipped, instructions count: 1100
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: no.nordicsemi.android.dfu.SecureDfuImpl.sendFirmware(android.bluetooth.BluetoothGatt):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x011c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void sendInitPacket(android.bluetooth.BluetoothGatt r17) throws no.nordicsemi.android.dfu.internal.exception.RemoteDfuException, no.nordicsemi.android.dfu.internal.exception.DeviceDisconnectedException, no.nordicsemi.android.dfu.internal.exception.DfuException, no.nordicsemi.android.dfu.internal.exception.UploadAbortedException, no.nordicsemi.android.dfu.internal.exception.UnknownResponseException {
        /*
            Method dump skipped, instructions count: 627
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: no.nordicsemi.android.dfu.SecureDfuImpl.sendInitPacket(android.bluetooth.BluetoothGatt):void");
    }

    private void setNumberOfPackets(byte[] bArr, int i) {
        bArr[1] = (byte) (i & 255);
        bArr[2] = (byte) ((i >> 8) & 255);
    }

    private void setObjectSize(byte[] bArr, int i) {
        bArr[2] = (byte) (i & 255);
        bArr[3] = (byte) ((i >> 8) & 255);
        bArr[4] = (byte) ((i >> 16) & 255);
        bArr[5] = (byte) ((i >> 24) & 255);
    }

    private void setPacketReceiptNotifications(int i) throws DfuException, DeviceDisconnectedException, UploadAbortedException, UnknownResponseException, RemoteDfuException {
        if (!this.mConnected) {
            throw new DeviceDisconnectedException("Unable to read Checksum: device disconnected");
        }
        logi("Sending the number of packets before notifications (Op Code = 2, Value = " + i + SQLBuilder.PARENTHESES_RIGHT);
        setNumberOfPackets(OP_CODE_PACKET_RECEIPT_NOTIF_REQ, i);
        writeOpCode(this.mControlPointCharacteristic, OP_CODE_PACKET_RECEIPT_NOTIF_REQ);
        byte[] readNotificationResponse = readNotificationResponse();
        int statusCode = getStatusCode(readNotificationResponse, 2);
        if (statusCode == 11) {
            throw new RemoteDfuExtendedErrorException("Sending the number of packets failed", readNotificationResponse[3]);
        }
        if (statusCode != 1) {
            throw new RemoteDfuException("Sending the number of packets failed", statusCode);
        }
    }

    private void writeCreateRequest(int i, int i2) throws DeviceDisconnectedException, DfuException, UploadAbortedException, RemoteDfuException, UnknownResponseException {
        if (!this.mConnected) {
            throw new DeviceDisconnectedException("Unable to create object: device disconnected");
        }
        byte[] bArr = i == 1 ? OP_CODE_CREATE_COMMAND : OP_CODE_CREATE_DATA;
        setObjectSize(bArr, i2);
        writeOpCode(this.mControlPointCharacteristic, bArr);
        byte[] readNotificationResponse = readNotificationResponse();
        int statusCode = getStatusCode(readNotificationResponse, 1);
        if (statusCode == 11) {
            throw new RemoteDfuExtendedErrorException("Creating Command object failed", readNotificationResponse[3]);
        }
        if (statusCode != 1) {
            throw new RemoteDfuException("Creating Command object failed", statusCode);
        }
    }

    private void writeExecute() throws DfuException, DeviceDisconnectedException, UploadAbortedException, UnknownResponseException, RemoteDfuException {
        if (!this.mConnected) {
            throw new DeviceDisconnectedException("Unable to read Checksum: device disconnected");
        }
        writeOpCode(this.mControlPointCharacteristic, OP_CODE_EXECUTE);
        byte[] readNotificationResponse = readNotificationResponse();
        int statusCode = getStatusCode(readNotificationResponse, 4);
        if (statusCode == 11) {
            throw new RemoteDfuExtendedErrorException("Executing object failed", readNotificationResponse[3]);
        }
        if (statusCode != 1) {
            throw new RemoteDfuException("Executing object failed", statusCode);
        }
    }

    private void writeExecute(boolean z) throws DfuException, DeviceDisconnectedException, UploadAbortedException, UnknownResponseException, RemoteDfuException {
        try {
            writeExecute();
        } catch (RemoteDfuException e) {
            if (!z || e.getErrorNumber() != 5) {
                throw e;
            }
            logw(e.getMessage() + ": " + SecureDfuError.parse(517));
            if (this.mFileType == 1) {
                logw("Are you sure your new SoftDevice is API compatible with the updated one? If not, update the bootloader as well");
            }
            this.mService.sendLogBroadcast(15, String.format(Locale.US, "Remote DFU error: %s. SD busy? Retrying...", SecureDfuError.parse(517)));
            logi("SD busy? Retrying...");
            logi("Executing data object (Op Code = 4)");
            writeExecute();
        }
    }

    private void writeOpCode(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr) throws DeviceDisconnectedException, DfuException, UploadAbortedException {
        writeOpCode(bluetoothGattCharacteristic, bArr, false);
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
    public BaseDfuImpl.BaseBluetoothGattCallback getGattCallback() {
        return this.mBluetoothCallback;
    }

    @Override // no.nordicsemi.android.dfu.BaseCustomDfuImpl
    protected UUID getPacketCharacteristicUUID() {
        return DFU_PACKET_UUID;
    }

    @Override // no.nordicsemi.android.dfu.BaseDfuImpl, no.nordicsemi.android.dfu.DfuService
    public boolean initialize(Intent intent, BluetoothGatt bluetoothGatt, int i, InputStream inputStream, InputStream inputStream2) throws DfuException, DeviceDisconnectedException, UploadAbortedException {
        if (inputStream2 != null) {
            return super.initialize(intent, bluetoothGatt, i, inputStream, inputStream2);
        }
        this.mService.sendLogBroadcast(20, "The Init packet is required by this version DFU Bootloader");
        this.mService.terminateConnection(bluetoothGatt, DfuBaseService.ERROR_INIT_PACKET_REQUIRED);
        return false;
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

    @Override // no.nordicsemi.android.dfu.DfuService
    public void performDfu(Intent intent) throws DfuException, DeviceDisconnectedException, UploadAbortedException {
        logw("Secure DFU bootloader found");
        this.mProgressInfo.setProgress(-2);
        this.mService.waitFor(1000);
        BluetoothGatt bluetoothGatt = this.mGatt;
        if (intent.hasExtra(DfuBaseService.EXTRA_MTU) && Build.VERSION.SDK_INT >= 21) {
            int intExtra = intent.getIntExtra(DfuBaseService.EXTRA_MTU, 517);
            logi("Requesting MTU = " + intExtra);
            requestMtu(intExtra);
        }
        try {
            enableCCCD(this.mControlPointCharacteristic, 1);
            this.mService.sendLogBroadcast(10, "Notifications enabled");
            this.mService.waitFor(1000);
            sendInitPacket(bluetoothGatt);
            sendFirmware(bluetoothGatt);
            this.mProgressInfo.setProgress(-5);
            this.mService.waitUntilDisconnected();
            this.mService.sendLogBroadcast(5, "Disconnected by the remote device");
            finalize(intent, false);
        } catch (RemoteDfuException e) {
            int errorNumber = e.getErrorNumber() | 512;
            loge(e.getMessage() + ": " + SecureDfuError.parse(errorNumber));
            this.mService.sendLogBroadcast(20, String.format(Locale.US, "Remote DFU error: %s", SecureDfuError.parse(errorNumber)));
            if (!(e instanceof RemoteDfuExtendedErrorException)) {
                this.mService.terminateConnection(bluetoothGatt, errorNumber | 8192);
                return;
            }
            RemoteDfuExtendedErrorException remoteDfuExtendedErrorException = (RemoteDfuExtendedErrorException) e;
            int extendedErrorNumber = remoteDfuExtendedErrorException.getExtendedErrorNumber() | 1024;
            loge("Extended Error details: " + SecureDfuError.parseExtendedError(extendedErrorNumber));
            this.mService.sendLogBroadcast(20, "Details: " + SecureDfuError.parseExtendedError(extendedErrorNumber) + " (Code = " + remoteDfuExtendedErrorException.getExtendedErrorNumber() + SQLBuilder.PARENTHESES_RIGHT);
            this.mService.terminateConnection(bluetoothGatt, extendedErrorNumber | 8192);
        } catch (UnknownResponseException e2) {
            loge(e2.getMessage());
            this.mService.sendLogBroadcast(20, e2.getMessage());
            this.mService.terminateConnection(bluetoothGatt, DfuBaseService.ERROR_INVALID_RESPONSE);
        } catch (UploadAbortedException e3) {
            throw e3;
        }
    }
}
