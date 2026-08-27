package no.nordicsemi.android.dfu;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import no.nordicsemi.android.dfu.DfuCallback;
import no.nordicsemi.android.dfu.internal.ArchiveInputStream;
import no.nordicsemi.android.dfu.internal.exception.DeviceDisconnectedException;
import no.nordicsemi.android.dfu.internal.exception.DfuException;
import no.nordicsemi.android.dfu.internal.exception.UploadAbortedException;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScannerFactory;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class BaseDfuImpl implements DfuService {
    protected static final int INDICATIONS = 2;
    protected static final int MAX_PACKET_SIZE_DEFAULT = 20;
    protected static final int NOTIFICATIONS = 1;
    private static final String TAG = "DfuImpl";
    protected boolean mAborted;
    protected int mError;
    protected int mFileType;
    protected InputStream mFirmwareStream;
    protected BluetoothGatt mGatt;
    protected int mImageSizeInBytes;
    protected int mInitPacketSizeInBytes;
    protected InputStream mInitPacketStream;
    protected boolean mPaused;
    protected DfuProgressInfo mProgressInfo;
    protected boolean mRequestCompleted;
    protected boolean mResetRequestSent;
    protected DfuBaseService mService;
    protected static final UUID GENERIC_ATTRIBUTE_SERVICE_UUID = new UUID(26392574038016L, -9223371485494954757L);
    protected static final UUID SERVICE_CHANGED_UUID = new UUID(46200963207168L, -9223371485494954757L);
    protected static final UUID CLIENT_CHARACTERISTIC_CONFIG = new UUID(45088566677504L, -9223371485494954757L);
    protected static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    protected final Object mLock = new Object();
    protected byte[] mReceivedData = null;
    protected byte[] mBuffer = new byte[20];
    protected boolean mConnected = true;

    /* loaded from: classes2.dex */
    protected class BaseBluetoothGattCallback extends DfuCallback.DfuGattCallback {
        /* JADX INFO: Access modifiers changed from: protected */
        public BaseBluetoothGattCallback() {
        }

        private String parse(byte[] bArr) {
            int length;
            if (bArr == null || (length = bArr.length) == 0) {
                return "";
            }
            char[] cArr = new char[(length * 3) - 1];
            for (int i = 0; i < length; i++) {
                int i2 = bArr[i] & 255;
                int i3 = i * 3;
                cArr[i3] = BaseDfuImpl.HEX_ARRAY[i2 >>> 4];
                cArr[i3 + 1] = BaseDfuImpl.HEX_ARRAY[i2 & 15];
                if (i != length - 1) {
                    cArr[i3 + 2] = '-';
                }
            }
            return new String(cArr);
        }

        private String phyToString(int i) {
            switch (i) {
                case 1:
                    return "LE 1M";
                case 2:
                    return "LE 2M";
                case 3:
                    return "LE Coded";
                default:
                    return "UNKNOWN (" + i + SQLBuilder.PARENTHESES_RIGHT;
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            if (i == 0) {
                BaseDfuImpl.this.mService.sendLogBroadcast(5, "Read Response received from " + bluetoothGattCharacteristic.getUuid() + ", value (0x): " + parse(bluetoothGattCharacteristic));
                BaseDfuImpl.this.mReceivedData = bluetoothGattCharacteristic.getValue();
                BaseDfuImpl.this.mRequestCompleted = true;
            } else {
                BaseDfuImpl.this.loge("Characteristic read error: " + i);
                BaseDfuImpl.this.mError = i | 16384;
            }
            BaseDfuImpl.this.notifyLock();
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            if (i != 0) {
                BaseDfuImpl.this.loge("Descriptor read error: " + i);
                BaseDfuImpl.this.mError = i | 16384;
            } else if (BaseDfuImpl.CLIENT_CHARACTERISTIC_CONFIG.equals(bluetoothGattDescriptor.getUuid())) {
                BaseDfuImpl.this.mService.sendLogBroadcast(5, "Read Response received from descr." + bluetoothGattDescriptor.getCharacteristic().getUuid() + ", value (0x): " + parse(bluetoothGattDescriptor));
                if (BaseDfuImpl.SERVICE_CHANGED_UUID.equals(bluetoothGattDescriptor.getCharacteristic().getUuid())) {
                    BaseDfuImpl.this.mRequestCompleted = true;
                } else {
                    BaseDfuImpl.this.loge("Unknown descriptor read");
                }
            }
            BaseDfuImpl.this.notifyLock();
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            if (i != 0) {
                BaseDfuImpl.this.loge("Descriptor write error: " + i);
                BaseDfuImpl.this.mError = i | 16384;
            } else if (BaseDfuImpl.CLIENT_CHARACTERISTIC_CONFIG.equals(bluetoothGattDescriptor.getUuid())) {
                BaseDfuImpl.this.mService.sendLogBroadcast(5, "Data written to descr." + bluetoothGattDescriptor.getCharacteristic().getUuid() + ", value (0x): " + parse(bluetoothGattDescriptor));
                if (BaseDfuImpl.SERVICE_CHANGED_UUID.equals(bluetoothGattDescriptor.getCharacteristic().getUuid())) {
                    BaseDfuImpl.this.mService.sendLogBroadcast(1, "Indications enabled for " + bluetoothGattDescriptor.getCharacteristic().getUuid());
                } else {
                    BaseDfuImpl.this.mService.sendLogBroadcast(1, "Notifications enabled for " + bluetoothGattDescriptor.getCharacteristic().getUuid());
                }
            }
            BaseDfuImpl.this.notifyLock();
        }

        @Override // no.nordicsemi.android.dfu.DfuCallback.DfuGattCallback
        public void onDisconnected() {
            BaseDfuImpl.this.mConnected = false;
            BaseDfuImpl.this.notifyLock();
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onMtuChanged(BluetoothGatt bluetoothGatt, int i, int i2) {
            if (i2 == 0) {
                BaseDfuImpl.this.mService.sendLogBroadcast(5, "MTU changed to: " + i);
                int i3 = i + (-3);
                if (i3 > BaseDfuImpl.this.mBuffer.length) {
                    BaseDfuImpl.this.mBuffer = new byte[i3];
                }
                BaseDfuImpl.this.logi("MTU changed to: " + i);
            } else {
                BaseDfuImpl.this.logw("Changing MTU failed: " + i2 + " (mtu: " + i + SQLBuilder.PARENTHESES_RIGHT);
            }
            BaseDfuImpl.this.mRequestCompleted = true;
            BaseDfuImpl.this.notifyLock();
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onPhyUpdate(BluetoothGatt bluetoothGatt, int i, int i2, int i3) {
            if (i3 != 0) {
                BaseDfuImpl.this.logw("Updating PHY failed: " + i3 + " (txPhy: " + i + ", rxPhy: " + i2 + SQLBuilder.PARENTHESES_RIGHT);
                return;
            }
            BaseDfuImpl.this.mService.sendLogBroadcast(5, "PHY updated (TX: " + phyToString(i) + ", RX: " + phyToString(i2) + SQLBuilder.PARENTHESES_RIGHT);
            BaseDfuImpl.this.logi("PHY updated (TX: " + phyToString(i) + ", RX: " + phyToString(i2) + SQLBuilder.PARENTHESES_RIGHT);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public String parse(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            return parse(bluetoothGattCharacteristic.getValue());
        }

        protected String parse(BluetoothGattDescriptor bluetoothGattDescriptor) {
            return parse(bluetoothGattDescriptor.getValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseDfuImpl(Intent intent, DfuBaseService dfuBaseService) {
        this.mService = dfuBaseService;
        this.mProgressInfo = dfuBaseService.mProgressInfo;
    }

    private boolean createBondApi18(BluetoothDevice bluetoothDevice) {
        try {
            Method method = bluetoothDevice.getClass().getMethod("createBond", new Class[0]);
            if (method != null) {
                this.mService.sendLogBroadcast(0, "gatt.getDevice().createBond() (hidden)");
                return ((Boolean) method.invoke(bluetoothDevice, new Object[0])).booleanValue();
            }
        } catch (Exception e) {
            Log.w(TAG, "An exception occurred while creating bond", e);
        }
        return false;
    }

    private boolean isServiceChangedCCCDEnabled() throws DeviceDisconnectedException, DfuException, UploadAbortedException {
        BluetoothGattCharacteristic characteristic;
        BluetoothGattDescriptor descriptor;
        if (!this.mConnected) {
            throw new DeviceDisconnectedException("Unable to read Service Changed CCCD: device disconnected");
        }
        if (this.mAborted) {
            throw new UploadAbortedException();
        }
        BluetoothGatt bluetoothGatt = this.mGatt;
        BluetoothGattService service = bluetoothGatt.getService(GENERIC_ATTRIBUTE_SERVICE_UUID);
        if (service == null || (characteristic = service.getCharacteristic(SERVICE_CHANGED_UUID)) == null || (descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG)) == null) {
            return false;
        }
        this.mRequestCompleted = false;
        this.mError = 0;
        logi("Reading Service Changed CCCD value...");
        this.mService.sendLogBroadcast(1, "Reading Service Changed CCCD value...");
        this.mService.sendLogBroadcast(0, "gatt.readDescriptor(" + descriptor.getUuid() + SQLBuilder.PARENTHESES_RIGHT);
        bluetoothGatt.readDescriptor(descriptor);
        try {
            synchronized (this.mLock) {
                while (true) {
                    if ((this.mRequestCompleted || !this.mConnected || this.mError != 0) && !this.mPaused) {
                        break;
                    }
                    this.mLock.wait();
                }
            }
        } catch (InterruptedException e) {
            loge("Sleeping interrupted", e);
        }
        if (this.mError != 0) {
            throw new DfuException("Unable to read Service Changed CCCD", this.mError);
        }
        if (this.mConnected) {
            return descriptor.getValue() != null && descriptor.getValue().length == 2 && descriptor.getValue()[0] == BluetoothGattDescriptor.ENABLE_INDICATION_VALUE[0] && descriptor.getValue()[1] == BluetoothGattDescriptor.ENABLE_INDICATION_VALUE[1];
        }
        throw new DeviceDisconnectedException("Unable to read Service Changed CCCD: device disconnected");
    }

    @Override // no.nordicsemi.android.dfu.DfuController
    public void abort() {
        this.mPaused = false;
        this.mAborted = true;
        notifyLock();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public boolean createBond() {
        boolean createBondApi18;
        BluetoothDevice device = this.mGatt.getDevice();
        if (device.getBondState() == 12) {
            return true;
        }
        this.mRequestCompleted = false;
        this.mService.sendLogBroadcast(1, "Starting pairing...");
        if (Build.VERSION.SDK_INT >= 19) {
            this.mService.sendLogBroadcast(0, "gatt.getDevice().createBond()");
            createBondApi18 = device.createBond();
        } else {
            createBondApi18 = createBondApi18(device);
        }
        try {
            synchronized (this.mLock) {
                while (!this.mRequestCompleted && !this.mAborted) {
                    this.mLock.wait();
                }
            }
        } catch (InterruptedException e) {
            loge("Sleeping interrupted", e);
        }
        return createBondApi18;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void enableCCCD(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) throws DeviceDisconnectedException, DfuException, UploadAbortedException {
        BluetoothGatt bluetoothGatt = this.mGatt;
        String str = i == 1 ? "notifications" : "indications";
        if (!this.mConnected) {
            throw new DeviceDisconnectedException("Unable to set " + str + " state: device disconnected");
        }
        if (this.mAborted) {
            throw new UploadAbortedException();
        }
        this.mReceivedData = null;
        this.mError = 0;
        BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
        boolean z = descriptor.getValue() != null && descriptor.getValue().length == 2 && descriptor.getValue()[0] > 0 && descriptor.getValue()[1] == 0;
        if (z) {
            return;
        }
        logi("Enabling " + str + "...");
        this.mService.sendLogBroadcast(1, "Enabling " + str + " for " + bluetoothGattCharacteristic.getUuid());
        this.mService.sendLogBroadcast(0, "gatt.setCharacteristicNotification(" + bluetoothGattCharacteristic.getUuid() + ", true)");
        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
        descriptor.setValue(i == 1 ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        DfuBaseService dfuBaseService = this.mService;
        StringBuilder sb = new StringBuilder();
        sb.append("gatt.writeDescriptor(");
        sb.append(descriptor.getUuid());
        sb.append(i == 1 ? ", value=0x01-00)" : ", value=0x02-00)");
        dfuBaseService.sendLogBroadcast(0, sb.toString());
        bluetoothGatt.writeDescriptor(descriptor);
        try {
            synchronized (this.mLock) {
                while (true) {
                    if (!z) {
                        try {
                            if (this.mConnected) {
                                if (this.mError != 0) {
                                }
                                this.mLock.wait();
                                z = descriptor.getValue() == null && descriptor.getValue().length == 2 && descriptor.getValue()[0] > 0 && descriptor.getValue()[1] == 0;
                            }
                        } finally {
                        }
                    }
                    if (!this.mPaused) {
                        break;
                    }
                    this.mLock.wait();
                    if (descriptor.getValue() == null) {
                    }
                }
            }
        } catch (InterruptedException e) {
            loge("Sleeping interrupted", e);
        }
        if (this.mError != 0) {
            throw new DfuException("Unable to set " + str + " state", this.mError);
        }
        if (this.mConnected) {
            return;
        }
        throw new DeviceDisconnectedException("Unable to set " + str + " state: device disconnected");
    }

    @Override // no.nordicsemi.android.dfu.DfuService
    public boolean initialize(Intent intent, BluetoothGatt bluetoothGatt, int i, InputStream inputStream, InputStream inputStream2) throws DfuException, DeviceDisconnectedException, UploadAbortedException {
        int i2;
        BluetoothGattService service;
        BluetoothGattCharacteristic characteristic;
        this.mGatt = bluetoothGatt;
        this.mFileType = i;
        this.mFirmwareStream = inputStream;
        this.mInitPacketStream = inputStream2;
        int intExtra = intent.getIntExtra(DfuBaseService.EXTRA_PART_CURRENT, 1);
        int intExtra2 = intent.getIntExtra(DfuBaseService.EXTRA_PARTS_TOTAL, 1);
        if (i > 4) {
            logw("DFU target does not support (SD/BL)+App update, splitting into 2 parts");
            this.mService.sendLogBroadcast(15, "Sending system components");
            this.mFileType &= -5;
            ((ArchiveInputStream) this.mFirmwareStream).setContentType(this.mFileType);
            intExtra2 = 2;
        }
        if (intExtra == 2) {
            this.mService.sendLogBroadcast(15, "Sending application");
        }
        int i3 = 0;
        try {
            i2 = inputStream2.available();
        } catch (Exception unused) {
            i2 = 0;
        }
        this.mInitPacketSizeInBytes = i2;
        try {
            i3 = inputStream.available();
        } catch (Exception unused2) {
        }
        this.mImageSizeInBytes = i3;
        this.mProgressInfo.init(i3, intExtra, intExtra2);
        if (bluetoothGatt.getDevice().getBondState() == 12 && (service = bluetoothGatt.getService(GENERIC_ATTRIBUTE_SERVICE_UUID)) != null && (characteristic = service.getCharacteristic(SERVICE_CHANGED_UUID)) != null) {
            if (!isServiceChangedCCCDEnabled()) {
                enableCCCD(characteristic, 2);
            }
            this.mService.sendLogBroadcast(10, "Service Changed indications enabled");
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isBonded() {
        return this.mGatt.getDevice().getBondState() == 12;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loge(String str) {
        Log.e(TAG, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loge(String str, Throwable th) {
        Log.e(TAG, str, th);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logi(String str) {
        if (DfuBaseService.DEBUG) {
            Log.i(TAG, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void logw(String str) {
        if (DfuBaseService.DEBUG) {
            Log.w(TAG, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void notifyLock() {
        synchronized (this.mLock) {
            this.mLock.notifyAll();
        }
    }

    @Override // no.nordicsemi.android.dfu.DfuCallback
    public void onBondStateChanged(int i) {
        this.mRequestCompleted = true;
        notifyLock();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String parse(byte[] bArr) {
        int length;
        if (bArr == null || (length = bArr.length) == 0) {
            return "";
        }
        char[] cArr = new char[(length * 3) - 1];
        for (int i = 0; i < length; i++) {
            int i2 = bArr[i] & 255;
            int i3 = i * 3;
            cArr[i3] = HEX_ARRAY[i2 >>> 4];
            cArr[i3 + 1] = HEX_ARRAY[i2 & 15];
            if (i != length - 1) {
                cArr[i3 + 2] = '-';
            }
        }
        return new String(cArr);
    }

    @Override // no.nordicsemi.android.dfu.DfuController
    public void pause() {
        this.mPaused = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] readNotificationResponse() throws DeviceDisconnectedException, DfuException, UploadAbortedException {
        try {
            synchronized (this.mLock) {
                while (true) {
                    if ((this.mReceivedData != null || !this.mConnected || this.mError != 0 || this.mAborted) && !this.mPaused) {
                        break;
                    }
                    this.mLock.wait();
                }
            }
        } catch (InterruptedException e) {
            loge("Sleeping interrupted", e);
        }
        if (this.mAborted) {
            throw new UploadAbortedException();
        }
        if (this.mError != 0) {
            throw new DfuException("Unable to write Op Code", this.mError);
        }
        if (this.mConnected) {
            return this.mReceivedData;
        }
        throw new DeviceDisconnectedException("Unable to write Op Code: device disconnected");
    }

    @Override // no.nordicsemi.android.dfu.DfuService
    public void release() {
        this.mService = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean removeBond() {
        BluetoothDevice device = this.mGatt.getDevice();
        boolean z = true;
        if (device.getBondState() == 10) {
            return true;
        }
        this.mService.sendLogBroadcast(1, "Removing bond information...");
        try {
            Method method = device.getClass().getMethod("removeBond", new Class[0]);
            if (method != null) {
                this.mRequestCompleted = false;
                this.mService.sendLogBroadcast(0, "gatt.getDevice().removeBond() (hidden)");
                boolean booleanValue = ((Boolean) method.invoke(device, new Object[0])).booleanValue();
                try {
                    try {
                        synchronized (this.mLock) {
                            while (!this.mRequestCompleted && !this.mAborted) {
                                this.mLock.wait();
                            }
                        }
                    } catch (InterruptedException e) {
                        loge("Sleeping interrupted", e);
                    }
                } catch (Exception e2) {
                    z = booleanValue;
                    e = e2;
                    Log.w(TAG, "An exception occurred while removing bond information", e);
                    return z;
                }
            }
        } catch (Exception e3) {
            e = e3;
            z = false;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @RequiresApi(api = 21)
    public void requestMtu(int i) throws DeviceDisconnectedException, UploadAbortedException {
        if (this.mAborted) {
            throw new UploadAbortedException();
        }
        this.mRequestCompleted = false;
        this.mService.sendLogBroadcast(1, "Requesting new MTU...");
        this.mService.sendLogBroadcast(0, "gatt.requestMtu(" + i + SQLBuilder.PARENTHESES_RIGHT);
        if (this.mGatt.requestMtu(i)) {
            try {
                synchronized (this.mLock) {
                    while (true) {
                        if ((this.mRequestCompleted || !this.mConnected || this.mError != 0) && !this.mPaused) {
                            break;
                        } else {
                            this.mLock.wait();
                        }
                    }
                }
            } catch (InterruptedException e) {
                loge("Sleeping interrupted", e);
            }
            if (!this.mConnected) {
                throw new DeviceDisconnectedException("Unable to read Service Changed CCCD: device disconnected");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void restartService(Intent intent, boolean z) {
        String str;
        if (z) {
            this.mService.sendLogBroadcast(1, "Scanning for the DFU Bootloader...");
            str = BootloaderScannerFactory.getScanner().searchFor(this.mGatt.getDevice().getAddress());
            logi("Scanning for new address finished with: " + str);
            if (str != null) {
                this.mService.sendLogBroadcast(5, "DFU Bootloader found with address " + str);
            } else {
                this.mService.sendLogBroadcast(5, "DFU Bootloader not found. Trying the same address...");
            }
        } else {
            str = null;
        }
        if (str != null) {
            intent.putExtra(DfuBaseService.EXTRA_DEVICE_ADDRESS, str);
        }
        this.mService.startService(intent);
    }

    @Override // no.nordicsemi.android.dfu.DfuController
    public void resume() {
        this.mPaused = false;
        notifyLock();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void waitIfPaused() {
        try {
            synchronized (this.mLock) {
                while (this.mPaused) {
                    this.mLock.wait();
                }
            }
        } catch (InterruptedException e) {
            loge("Sleeping interrupted", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeOpCode(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, boolean z) throws DeviceDisconnectedException, DfuException, UploadAbortedException {
        if (this.mAborted) {
            throw new UploadAbortedException();
        }
        this.mReceivedData = null;
        this.mError = 0;
        this.mRequestCompleted = false;
        this.mResetRequestSent = z;
        bluetoothGattCharacteristic.setWriteType(2);
        bluetoothGattCharacteristic.setValue(bArr);
        this.mService.sendLogBroadcast(1, "Writing to characteristic " + bluetoothGattCharacteristic.getUuid());
        this.mService.sendLogBroadcast(0, "gatt.writeCharacteristic(" + bluetoothGattCharacteristic.getUuid() + SQLBuilder.PARENTHESES_RIGHT);
        this.mGatt.writeCharacteristic(bluetoothGattCharacteristic);
        try {
            synchronized (this.mLock) {
                while (true) {
                    if ((this.mRequestCompleted || !this.mConnected || this.mError != 0) && !this.mPaused) {
                        break;
                    } else {
                        this.mLock.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            loge("Sleeping interrupted", e);
        }
        if (!this.mResetRequestSent && this.mError != 0) {
            throw new DfuException("Unable to write Op Code " + ((int) bArr[0]), this.mError);
        }
        if (this.mResetRequestSent || this.mConnected) {
            return;
        }
        throw new DeviceDisconnectedException("Unable to write Op Code " + ((int) bArr[0]) + ": device disconnected");
    }
}
