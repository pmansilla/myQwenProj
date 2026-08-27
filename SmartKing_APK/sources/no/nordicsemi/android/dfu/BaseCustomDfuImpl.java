package no.nordicsemi.android.dfu;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.CRC32;
import no.nordicsemi.android.dfu.BaseDfuImpl;
import no.nordicsemi.android.dfu.internal.exception.DeviceDisconnectedException;
import no.nordicsemi.android.dfu.internal.exception.DfuException;
import no.nordicsemi.android.dfu.internal.exception.HexFileValidationException;
import no.nordicsemi.android.dfu.internal.exception.UploadAbortedException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class BaseCustomDfuImpl extends BaseDfuImpl {
    protected boolean mFirmwareUploadInProgress;
    private boolean mInitPacketInProgress;
    protected int mPacketsBeforeNotification;
    protected int mPacketsSentSinceNotification;
    protected boolean mRemoteErrorOccurred;

    /* loaded from: classes2.dex */
    protected class BaseCustomBluetoothCallback extends BaseDfuImpl.BaseBluetoothGattCallback {
        /* JADX INFO: Access modifiers changed from: protected */
        public BaseCustomBluetoothCallback() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void handleNotification(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            BaseCustomDfuImpl.this.mService.sendLogBroadcast(5, "Notification received from " + bluetoothGattCharacteristic.getUuid() + ", value (0x): " + parse(bluetoothGattCharacteristic));
            BaseCustomDfuImpl.this.mReceivedData = bluetoothGattCharacteristic.getValue();
            BaseCustomDfuImpl.this.mFirmwareUploadInProgress = false;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void handlePacketReceiptNotification(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            if (!BaseCustomDfuImpl.this.mFirmwareUploadInProgress) {
                handleNotification(bluetoothGatt, bluetoothGattCharacteristic);
                return;
            }
            BluetoothGattCharacteristic characteristic = bluetoothGatt.getService(BaseCustomDfuImpl.this.getDfuServiceUUID()).getCharacteristic(BaseCustomDfuImpl.this.getPacketCharacteristicUUID());
            try {
                BaseCustomDfuImpl.this.mPacketsSentSinceNotification = 0;
                BaseCustomDfuImpl.this.waitIfPaused();
                if (!BaseCustomDfuImpl.this.mAborted && BaseCustomDfuImpl.this.mError == 0 && !BaseCustomDfuImpl.this.mRemoteErrorOccurred && !BaseCustomDfuImpl.this.mResetRequestSent) {
                    boolean isComplete = BaseCustomDfuImpl.this.mProgressInfo.isComplete();
                    boolean isObjectComplete = BaseCustomDfuImpl.this.mProgressInfo.isObjectComplete();
                    if (!isComplete && !isObjectComplete) {
                        int availableObjectSizeIsBytes = BaseCustomDfuImpl.this.mProgressInfo.getAvailableObjectSizeIsBytes();
                        byte[] bArr = BaseCustomDfuImpl.this.mBuffer;
                        if (availableObjectSizeIsBytes < bArr.length) {
                            bArr = new byte[availableObjectSizeIsBytes];
                        }
                        BaseCustomDfuImpl.this.writePacket(bluetoothGatt, characteristic, bArr, BaseCustomDfuImpl.this.mFirmwareStream.read(bArr));
                        return;
                    }
                    BaseCustomDfuImpl.this.mFirmwareUploadInProgress = false;
                    BaseCustomDfuImpl.this.notifyLock();
                    return;
                }
                BaseCustomDfuImpl.this.mFirmwareUploadInProgress = false;
                BaseCustomDfuImpl.this.mService.sendLogBroadcast(15, "Upload terminated");
            } catch (HexFileValidationException unused) {
                BaseCustomDfuImpl.this.loge("Invalid HEX file");
                BaseCustomDfuImpl.this.mError = 4099;
            } catch (IOException e) {
                BaseCustomDfuImpl.this.loge("Error while reading the input stream", e);
                BaseCustomDfuImpl.this.mError = DfuBaseService.ERROR_FILE_IO_EXCEPTION;
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            if (i == 0) {
                if (!bluetoothGattCharacteristic.getUuid().equals(BaseCustomDfuImpl.this.getPacketCharacteristicUUID())) {
                    BaseCustomDfuImpl.this.mService.sendLogBroadcast(5, "Data written to " + bluetoothGattCharacteristic.getUuid() + ", value (0x): " + parse(bluetoothGattCharacteristic));
                    BaseCustomDfuImpl.this.mRequestCompleted = true;
                } else if (BaseCustomDfuImpl.this.mInitPacketInProgress) {
                    BaseCustomDfuImpl.this.mService.sendLogBroadcast(5, "Data written to " + bluetoothGattCharacteristic.getUuid() + ", value (0x): " + parse(bluetoothGattCharacteristic));
                    BaseCustomDfuImpl.this.mInitPacketInProgress = false;
                } else if (BaseCustomDfuImpl.this.mFirmwareUploadInProgress) {
                    BaseCustomDfuImpl.this.mProgressInfo.addBytesSent(bluetoothGattCharacteristic.getValue().length);
                    BaseCustomDfuImpl.this.mPacketsSentSinceNotification++;
                    boolean z = BaseCustomDfuImpl.this.mPacketsBeforeNotification > 0 && BaseCustomDfuImpl.this.mPacketsSentSinceNotification >= BaseCustomDfuImpl.this.mPacketsBeforeNotification;
                    boolean isComplete = BaseCustomDfuImpl.this.mProgressInfo.isComplete();
                    boolean isObjectComplete = BaseCustomDfuImpl.this.mProgressInfo.isObjectComplete();
                    if (z) {
                        return;
                    }
                    if (isComplete || isObjectComplete) {
                        BaseCustomDfuImpl.this.mFirmwareUploadInProgress = false;
                        BaseCustomDfuImpl.this.notifyLock();
                        return;
                    }
                    try {
                        BaseCustomDfuImpl.this.waitIfPaused();
                        if (!BaseCustomDfuImpl.this.mAborted && BaseCustomDfuImpl.this.mError == 0 && !BaseCustomDfuImpl.this.mRemoteErrorOccurred && !BaseCustomDfuImpl.this.mResetRequestSent) {
                            int availableObjectSizeIsBytes = BaseCustomDfuImpl.this.mProgressInfo.getAvailableObjectSizeIsBytes();
                            byte[] bArr = BaseCustomDfuImpl.this.mBuffer;
                            if (availableObjectSizeIsBytes < bArr.length) {
                                bArr = new byte[availableObjectSizeIsBytes];
                            }
                            BaseCustomDfuImpl.this.writePacket(bluetoothGatt, bluetoothGattCharacteristic, bArr, BaseCustomDfuImpl.this.mFirmwareStream.read(bArr));
                            return;
                        }
                        BaseCustomDfuImpl.this.mFirmwareUploadInProgress = false;
                        BaseCustomDfuImpl.this.mService.sendLogBroadcast(15, "Upload terminated");
                        BaseCustomDfuImpl.this.notifyLock();
                        return;
                    } catch (HexFileValidationException unused) {
                        BaseCustomDfuImpl.this.loge("Invalid HEX file");
                        BaseCustomDfuImpl.this.mError = 4099;
                    } catch (IOException e) {
                        BaseCustomDfuImpl.this.loge("Error while reading the input stream", e);
                        BaseCustomDfuImpl.this.mError = DfuBaseService.ERROR_FILE_IO_EXCEPTION;
                    }
                } else {
                    onPacketCharacteristicWrite(bluetoothGatt, bluetoothGattCharacteristic, i);
                }
            } else if (BaseCustomDfuImpl.this.mResetRequestSent) {
                BaseCustomDfuImpl.this.mRequestCompleted = true;
            } else {
                BaseCustomDfuImpl.this.loge("Characteristic write error: " + i);
                BaseCustomDfuImpl.this.mError = i | 16384;
            }
            BaseCustomDfuImpl.this.notifyLock();
        }

        protected void onPacketCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0052, code lost:
    
        if (r7 <= 65535) goto L24;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public BaseCustomDfuImpl(android.content.Intent r7, no.nordicsemi.android.dfu.DfuBaseService r8) {
        /*
            r6 = this;
            r6.<init>(r7, r8)
            java.lang.String r0 = "no.nordicsemi.android.dfu.extra.EXTRA_PRN_ENABLED"
            boolean r0 = r7.hasExtra(r0)
            r1 = 65535(0xffff, float:9.1834E-41)
            r2 = 1
            r3 = 23
            r4 = 0
            r5 = 12
            if (r0 == 0) goto L32
            java.lang.String r8 = "no.nordicsemi.android.dfu.extra.EXTRA_PRN_ENABLED"
            int r0 = android.os.Build.VERSION.SDK_INT
            if (r0 >= r3) goto L1b
            goto L1c
        L1b:
            r2 = 0
        L1c:
            boolean r8 = r7.getBooleanExtra(r8, r2)
            java.lang.String r0 = "no.nordicsemi.android.dfu.extra.EXTRA_PRN_VALUE"
            int r7 = r7.getIntExtra(r0, r5)
            if (r7 < 0) goto L2a
            if (r7 <= r1) goto L2c
        L2a:
            r7 = 12
        L2c:
            if (r8 != 0) goto L2f
            r7 = 0
        L2f:
            r6.mPacketsBeforeNotification = r7
            goto L5b
        L32:
            android.content.SharedPreferences r7 = android.preference.PreferenceManager.getDefaultSharedPreferences(r8)
            java.lang.String r8 = "settings_packet_receipt_notification_enabled"
            int r0 = android.os.Build.VERSION.SDK_INT
            if (r0 >= r3) goto L3d
            goto L3e
        L3d:
            r2 = 0
        L3e:
            boolean r8 = r7.getBoolean(r8, r2)
            java.lang.String r0 = "settings_number_of_packets"
            java.lang.String r2 = java.lang.String.valueOf(r5)
            java.lang.String r7 = r7.getString(r0, r2)
            int r7 = java.lang.Integer.parseInt(r7)     // Catch: java.lang.NumberFormatException -> L54
            if (r7 < 0) goto L54
            if (r7 <= r1) goto L56
        L54:
            r7 = 12
        L56:
            if (r8 != 0) goto L59
            r7 = 0
        L59:
            r6.mPacketsBeforeNotification = r7
        L5b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: no.nordicsemi.android.dfu.BaseCustomDfuImpl.<init>(android.content.Intent, no.nordicsemi.android.dfu.DfuBaseService):void");
    }

    private void writeInitPacket(BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i) throws DeviceDisconnectedException, DfuException, UploadAbortedException {
        if (this.mAborted) {
            throw new UploadAbortedException();
        }
        if (bArr.length != i) {
            byte[] bArr2 = new byte[i];
            System.arraycopy(bArr, 0, bArr2, 0, i);
            bArr = bArr2;
        }
        this.mReceivedData = null;
        this.mError = 0;
        this.mInitPacketInProgress = true;
        bluetoothGattCharacteristic.setWriteType(1);
        bluetoothGattCharacteristic.setValue(bArr);
        logi("Sending init packet (Value = " + parse(bArr) + SQLBuilder.PARENTHESES_RIGHT);
        this.mService.sendLogBroadcast(1, "Writing to characteristic " + bluetoothGattCharacteristic.getUuid());
        this.mService.sendLogBroadcast(0, "gatt.writeCharacteristic(" + bluetoothGattCharacteristic.getUuid() + SQLBuilder.PARENTHESES_RIGHT);
        this.mGatt.writeCharacteristic(bluetoothGattCharacteristic);
        try {
            synchronized (this.mLock) {
                while (true) {
                    if ((!this.mInitPacketInProgress || !this.mConnected || this.mError != 0) && !this.mPaused) {
                        break;
                    } else {
                        this.mLock.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            loge("Sleeping interrupted", e);
        }
        if (this.mError != 0) {
            throw new DfuException("Unable to write Init DFU Parameters", this.mError);
        }
        if (!this.mConnected) {
            throw new DeviceDisconnectedException("Unable to write Init DFU Parameters: device disconnected");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writePacket(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, byte[] bArr, int i) {
        if (i <= 0) {
            return;
        }
        if (bArr.length != i) {
            byte[] bArr2 = new byte[i];
            System.arraycopy(bArr, 0, bArr2, 0, i);
            bArr = bArr2;
        }
        bluetoothGattCharacteristic.setWriteType(1);
        bluetoothGattCharacteristic.setValue(bArr);
        bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void finalize(Intent intent, boolean z) {
        boolean z2;
        boolean z3 = false;
        boolean booleanExtra = intent.getBooleanExtra(DfuBaseService.EXTRA_KEEP_BOND, false);
        this.mService.refreshDeviceCache(this.mGatt, z || !booleanExtra);
        this.mService.close(this.mGatt);
        if (this.mGatt.getDevice().getBondState() == 12) {
            boolean booleanExtra2 = intent.getBooleanExtra(DfuBaseService.EXTRA_RESTORE_BOND, false);
            if (booleanExtra2 || !booleanExtra) {
                removeBond();
                this.mService.waitFor(2000);
                z2 = true;
            } else {
                z2 = false;
            }
            if (!booleanExtra2 || (this.mFileType & 4) <= 0) {
                z3 = z2;
            } else {
                createBond();
            }
        }
        if (this.mProgressInfo.isLastPart()) {
            if (!z3) {
                this.mService.waitFor(1400);
            }
            this.mProgressInfo.setProgress(-6);
            return;
        }
        logi("Starting service that will upload application");
        Intent intent2 = new Intent();
        intent2.fillIn(intent, 24);
        intent2.putExtra(DfuBaseService.EXTRA_FILE_MIME_TYPE, DfuBaseService.MIME_TYPE_ZIP);
        intent2.putExtra(DfuBaseService.EXTRA_FILE_TYPE, 4);
        intent2.putExtra(DfuBaseService.EXTRA_PART_CURRENT, this.mProgressInfo.getCurrentPart() + 1);
        intent2.putExtra(DfuBaseService.EXTRA_PARTS_TOTAL, this.mProgressInfo.getTotalParts());
        restartService(intent2, true);
    }

    protected abstract UUID getControlPointCharacteristicUUID();

    protected abstract UUID getDfuServiceUUID();

    protected abstract UUID getPacketCharacteristicUUID();

    /* JADX INFO: Access modifiers changed from: protected */
    public void uploadFirmwareImage(BluetoothGattCharacteristic bluetoothGattCharacteristic) throws DeviceDisconnectedException, DfuException, UploadAbortedException {
        if (this.mAborted) {
            throw new UploadAbortedException();
        }
        this.mReceivedData = null;
        this.mError = 0;
        this.mFirmwareUploadInProgress = true;
        this.mPacketsSentSinceNotification = 0;
        byte[] bArr = this.mBuffer;
        try {
            int read = this.mFirmwareStream.read(bArr);
            this.mService.sendLogBroadcast(1, "Sending firmware to characteristic " + bluetoothGattCharacteristic.getUuid() + "...");
            writePacket(this.mGatt, bluetoothGattCharacteristic, bArr, read);
            try {
                synchronized (this.mLock) {
                    while (true) {
                        if ((!this.mFirmwareUploadInProgress || this.mReceivedData != null || !this.mConnected || this.mError != 0) && !this.mPaused) {
                            break;
                        } else {
                            this.mLock.wait();
                        }
                    }
                }
            } catch (InterruptedException e) {
                loge("Sleeping interrupted", e);
            }
            if (this.mError != 0) {
                throw new DfuException("Uploading Firmware Image failed", this.mError);
            }
            if (!this.mConnected) {
                throw new DeviceDisconnectedException("Uploading Firmware Image failed: device disconnected");
            }
        } catch (HexFileValidationException unused) {
            throw new DfuException("HEX file not valid", 4099);
        } catch (IOException unused2) {
            throw new DfuException("Error while reading file", DfuBaseService.ERROR_FILE_IO_EXCEPTION);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeInitData(BluetoothGattCharacteristic bluetoothGattCharacteristic, CRC32 crc32) throws DfuException, DeviceDisconnectedException, UploadAbortedException {
        try {
            byte[] bArr = this.mBuffer;
            while (true) {
                int read = this.mInitPacketStream.read(bArr, 0, bArr.length);
                if (read == -1) {
                    return;
                }
                writeInitPacket(bluetoothGattCharacteristic, bArr, read);
                if (crc32 != null) {
                    crc32.update(bArr, 0, read);
                }
            }
        } catch (IOException e) {
            loge("Error while reading Init packet file", e);
            throw new DfuException("Error while reading Init packet file", 4098);
        }
    }
}
