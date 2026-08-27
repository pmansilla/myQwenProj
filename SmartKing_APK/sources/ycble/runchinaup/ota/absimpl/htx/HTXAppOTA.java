package ycble.runchinaup.ota.absimpl.htx;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.example.otalib.boads.Constant;
import com.example.otalib.boads.Utils;
import com.example.otalib.boads.WorkOnBoads;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.panpf.sketch.uri.FileUriModel;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.ota.absimpl.htx.BluetoothLeService;
import ycble.runchinaup.ota.callback.OTACallback;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes2.dex */
class HTXAppOTA {
    public static final int MSG1_BLE_ERROR = 18;
    public static final int MSG1_NO_FILE = 17;
    public static final int MSG_BURN_APP_SUCCESS = 0;
    public static final int MSG_BURN_CFG_SUCCESS = 1;
    public static final int MSG_BURN_PATCH_SUCCESS = 2;
    public static final int MSG_DISCONNECT_BLE = 16;
    public static final int MSG_FLASH_EMPTY = 5;
    public static final int MSG_HANDS_UP_FAILED = 8;
    public static final int MSG_OTA_COMPLETE = 4;
    public static final int MSG_OTA_RESEPONSE = 9;
    public static boolean mConnected = false;
    private List<BluetoothGattService> BluetoothGattServices;
    private String appFileStringPath;
    private WorkOnBoads do_work_on_boads;
    public BluetoothLeService mBLE;
    private BluetoothAdapter mBluetoothAdapter;
    private String mDeviceAddress;
    private Thread mTransThread = null;
    private boolean mIsWorcking = false;
    private boolean isSuccess = false;
    public BluetoothGattCharacteristic ota_tx_dat_charac = null;
    public BluetoothGattCharacteristic ota_rx_dat_charac = null;
    public BluetoothGattCharacteristic ota_tx_cmd_charac = null;
    public BluetoothGattCharacteristic ota_rx_cmd_charac = null;
    private Context context = null;
    private OTACallback otaCallback = null;
    private int fileTotalSize = 0;
    public Handler handler = new Handler() { // from class: ycble.runchinaup.ota.absimpl.htx.HTXAppOTA.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message == null) {
                return;
            }
            ycBleLog.e("handleMessage:MsgWhat===========> " + message.what);
            if (message.what == 1005) {
                int i = message.arg1;
                int i2 = message.arg2;
                switch (((Integer) message.obj).intValue()) {
                }
            } else if (message.what == 10 && message.obj != null) {
            }
            ycBleLog.e("msg2.arg1========>" + message.arg1);
            switch (message.arg1) {
                case 0:
                case 1:
                case 2:
                case 5:
                case 16:
                case 17:
                default:
                    return;
                case 4:
                    HTXAppOTA.this.mIsWorcking = false;
                    HTXAppOTA.this.isSuccess = true;
                    ycBleLog.e("OTA has done and success!\"");
                    if (!HTXAppOTA.mConnected || HTXAppOTA.this.mBLE == null) {
                        return;
                    }
                    HTXAppOTA.this.mBLE.disconnect();
                    return;
                case 8:
                    ycBleLog.e("Hands up to the boads failed before OTA!");
                    return;
                case 9:
                    if (message.obj != null) {
                        HTXAppOTA.this.do_work_on_boads.resetTarget();
                        return;
                    }
                    return;
                case 18:
                    ycBleLog.e("Bluetooth connection failed,Please scan bluetooth agai ");
                    HTXAppOTA.this.mBLE.disconnect();
                    return;
                case 1000:
                    int i3 = message.arg2;
                    int i4 = i3 % 20;
                    byte[] bArr = (byte[]) message.obj;
                    if (HTXAppOTA.this.ota_tx_cmd_charac == null) {
                        ycBleLog.e("OTA has not discover the right character!");
                        return;
                    }
                    int i5 = 0;
                    for (int i6 = 0; i6 < i3 / 20; i6++) {
                        byte[] bArr2 = new byte[20];
                        System.arraycopy(bArr, i5, bArr2, 0, 20);
                        if (!HTXAppOTA.mConnected || HTXAppOTA.this.mBLE == null) {
                            return;
                        }
                        HTXAppOTA.this.ota_tx_cmd_charac.setValue(bArr2);
                        if (!HTXAppOTA.this.mBLE.writeCharacteristic(HTXAppOTA.this.ota_tx_cmd_charac)) {
                            ycBleLog.e("writeCharacteristic() failed!!!");
                            return;
                        } else {
                            i5 += 20;
                            ycBleLog.e(new Gson().toJson(bArr2));
                        }
                    }
                    if (i4 != 0) {
                        byte[] bArr3 = new byte[i4];
                        System.arraycopy(bArr, i5, bArr3, 0, i4);
                        if (!HTXAppOTA.mConnected || HTXAppOTA.this.mBLE == null) {
                            return;
                        }
                        ycBleLog.e("cmd data:" + Utils.bytesToHexString(bArr3));
                        if (!HTXAppOTA.this.ota_tx_cmd_charac.setValue(bArr3)) {
                            ycBleLog.e("setValue failed!");
                            return;
                        } else {
                            if (!HTXAppOTA.this.mBLE.writeCharacteristic(HTXAppOTA.this.ota_tx_cmd_charac)) {
                                ycBleLog.e("writeCharacteristic() failed!!!");
                                return;
                            }
                            if (BleUtil.byte2HexStr(bArr3).equals("00000000")) {
                                HTXAppOTA.this.isSuccess = true;
                            }
                            ycBleLog.e(bArr3.toString());
                            return;
                        }
                    }
                    return;
                case 1001:
                    ycBleLog.e("OTA exchange key please try again");
                    HTXAppOTA.this.mBLE.disconnect();
                    return;
                case 1002:
                    int i7 = message.arg2;
                    ycBleLog.e("len==>1 " + i7);
                    HTXAppOTA.this.fileTotalSize = i7;
                    return;
                case 1003:
                    int i8 = message.arg2;
                    float f = (i8 * 100) / HTXAppOTA.this.fileTotalSize;
                    ycBleLog.e("len==>2 " + i8 + FileUriModel.SCHEME + HTXAppOTA.this.fileTotalSize);
                    if (HTXAppOTA.this.otaCallback != null) {
                        HTXAppOTA.this.otaCallback.onProgress((int) f);
                        return;
                    }
                    return;
                case 1004:
                    int i9 = message.arg2;
                    int i10 = i9 % 20;
                    byte[] bArr4 = (byte[]) message.obj;
                    if (HTXAppOTA.this.ota_tx_dat_charac == null) {
                        ycBleLog.e(" 发数据 OTA has not discover the right character!");
                        return;
                    }
                    int i11 = 0;
                    for (int i12 = 0; i12 < i9 / 20; i12++) {
                        byte[] bArr5 = new byte[20];
                        System.arraycopy(bArr4, i11, bArr5, 0, 20);
                        if (!HTXAppOTA.mConnected || HTXAppOTA.this.mBLE == null) {
                            return;
                        }
                        ycBleLog.e("ota send lenth:" + bArr5.length);
                        HTXAppOTA.this.ota_tx_dat_charac.setValue(bArr5);
                        HTXAppOTA.this.ota_tx_dat_charac.setWriteType(1);
                        if (!HTXAppOTA.this.mBLE.writeCharacteristic(HTXAppOTA.this.ota_tx_dat_charac)) {
                            ycBleLog.e("writeCharacteristic() failed!!!");
                            return;
                        }
                        i11 += 20;
                        ycBleLog.e("ota pos:" + i11 + " / " + i9);
                    }
                    if (i10 != 0) {
                        byte[] bArr6 = new byte[i10];
                        System.arraycopy(bArr4, i11, bArr6, 0, i10);
                        if (!HTXAppOTA.mConnected || HTXAppOTA.this.mBLE == null) {
                            return;
                        }
                        ycBleLog.e("send data:" + Utils.bytesToHexString(bArr6));
                        if (!HTXAppOTA.this.ota_tx_dat_charac.setValue(bArr6)) {
                            ycBleLog.e("setValue() failed!!!");
                        }
                        HTXAppOTA.this.ota_tx_dat_charac.setWriteType(1);
                        if (HTXAppOTA.this.mBLE.writeCharacteristic(HTXAppOTA.this.ota_tx_dat_charac)) {
                            return;
                        }
                        ycBleLog.e("writeCharacteristic() failed!!!");
                        return;
                    }
                    return;
                case 1008:
                    ycBleLog.e(((Float) message.obj).floatValue() + "kB/s");
                    return;
            }
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: ycble.runchinaup.ota.absimpl.htx.HTXAppOTA.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                HTXAppOTA.mConnected = true;
                return;
            }
            if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                HTXAppOTA.mConnected = false;
                ycBleLog.e("disconnection/isSuccess:" + HTXAppOTA.this.isSuccess);
                if (HTXAppOTA.this.mIsWorcking) {
                    ycBleLog.e("The connection is lost while OTA is working!");
                    HTXAppOTA.this.mIsWorcking = false;
                }
                if (HTXAppOTA.this.otaCallback != null) {
                    if (HTXAppOTA.this.isSuccess) {
                        HTXAppOTA.this.otaCallback.onSuccess();
                        return;
                    } else {
                        HTXAppOTA.this.otaCallback.onFailure(1, "The connection is lost while OTA is working!");
                        return;
                    }
                }
                return;
            }
            if (BluetoothLeService.ACTION_GATT_STATUS_133.equals(action)) {
                if (HTXAppOTA.this.mBluetoothAdapter != null) {
                    HTXAppOTA.this.mBluetoothAdapter.disable();
                    ycBleLog.e("Bluetooth connection status is 133,reset the bluetooth now,please wait");
                    HTXAppOTA.this.mBluetoothAdapter.enable();
                }
                if (HTXAppOTA.this.otaCallback != null) {
                    if (HTXAppOTA.this.isSuccess) {
                        HTXAppOTA.this.otaCallback.onSuccess();
                        return;
                    } else {
                        HTXAppOTA.this.otaCallback.onFailure(1, "The connection is lost while OTA is working!");
                        return;
                    }
                }
                return;
            }
            if (!BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                    intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                    return;
                }
                if (BluetoothLeService.ACTION_GATT_CHARACTER_NOTIFY.equals(action)) {
                    intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                    return;
                }
                if (BluetoothLeService.OTA_RX_DAT_ACTION.equals(action)) {
                    byte[] byteArrayExtra = intent.getByteArrayExtra(BluetoothLeService.ARRAY_BYTE_DATA);
                    if (byteArrayExtra != null) {
                        HTXAppOTA.this.do_work_on_boads.setBluetoothNotifyData(byteArrayExtra, 2);
                        return;
                    }
                    return;
                }
                if (BluetoothLeService.OTA_RX_CMD_ACTION.equals(action)) {
                    byte[] byteArrayExtra2 = intent.getByteArrayExtra(BluetoothLeService.ARRAY_BYTE_DATA);
                    ycBleLog.e("CMD notify:" + Utils.bytesToHexString(byteArrayExtra2));
                    if (byteArrayExtra2 != null) {
                        HTXAppOTA.this.do_work_on_boads.setBluetoothNotifyData(byteArrayExtra2, 1);
                        return;
                    }
                    return;
                }
                if (BluetoothLeService.OTA_RX_ISP_CMD_ACTION.equals(action)) {
                    ycBleLog.e("ISP notify:" + Utils.bytesToHexString(intent.getByteArrayExtra(BluetoothLeService.ARRAY_BYTE_DATA)));
                    HTXAppOTA.this.do_work_on_boads.entryIspModel(1007);
                    HTXAppOTA.this.handler.postDelayed(new Runnable() { // from class: ycble.runchinaup.ota.absimpl.htx.HTXAppOTA.3.3
                        @Override // java.lang.Runnable
                        public void run() {
                            if (HTXAppOTA.this.mBLE != null) {
                                HTXAppOTA.this.mBLE.disconnect();
                            }
                        }
                    }, 500L);
                    return;
                }
                return;
            }
            HTXAppOTA.this.BluetoothGattServices = HTXAppOTA.this.mBLE.getSupportedGattServices();
            ycBleLog.e("已经连接上了 扫描服务: ");
            if (HTXAppOTA.this.BluetoothGattServices == null) {
                return;
            }
            final Message obtain = Message.obtain();
            Iterator it = HTXAppOTA.this.BluetoothGattServices.iterator();
            while (it.hasNext()) {
                List<BluetoothGattCharacteristic> characteristics = ((BluetoothGattService) it.next()).getCharacteristics();
                new ArrayList();
                for (final BluetoothGattCharacteristic bluetoothGattCharacteristic : characteristics) {
                    ycBleLog.e("gattCharacteristic: " + bluetoothGattCharacteristic.getUuid());
                    if (!bluetoothGattCharacteristic.getUuid().toString().contains("00002")) {
                        if (SampleGattAttributes.otas_tx_dat_uuid.equals(bluetoothGattCharacteristic.getUuid().toString())) {
                            HTXAppOTA.this.ota_tx_dat_charac = bluetoothGattCharacteristic;
                            ycBleLog.e("不为空吧？: " + HTXAppOTA.this.ota_tx_dat_charac);
                        } else if (SampleGattAttributes.otas_rx_dat_uuid.equals(bluetoothGattCharacteristic.getUuid().toString())) {
                            HTXAppOTA.this.ota_rx_dat_charac = bluetoothGattCharacteristic;
                            if ((HTXAppOTA.this.ota_rx_dat_charac.getProperties() & 16) > 0) {
                                HTXAppOTA.this.handler.postDelayed(new Runnable() { // from class: ycble.runchinaup.ota.absimpl.htx.HTXAppOTA.3.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (HTXAppOTA.this.mBLE.setCharacteristicNotification(bluetoothGattCharacteristic, true)) {
                                            ycBleLog.e("Set Notify Success");
                                            return;
                                        }
                                        obtain.arg1 = 18;
                                        HTXAppOTA.this.handler.sendMessage(obtain);
                                        ycBleLog.e("Notify failed!!");
                                    }
                                }, 500L);
                            }
                        } else if (SampleGattAttributes.otas_rx_cmd_uuid.equals(bluetoothGattCharacteristic.getUuid().toString())) {
                            HTXAppOTA.this.ota_rx_cmd_charac = bluetoothGattCharacteristic;
                            if ((HTXAppOTA.this.ota_rx_cmd_charac.getProperties() & 16) <= 0) {
                                continue;
                            } else {
                                if (!HTXAppOTA.this.mBLE.setCharacteristicNotification(bluetoothGattCharacteristic, true)) {
                                    obtain.arg1 = 18;
                                    HTXAppOTA.this.handler.sendMessage(obtain);
                                    ycBleLog.e("Notify failed!!");
                                    return;
                                }
                                ycBleLog.e("Set Notify Success");
                            }
                        } else if (SampleGattAttributes.otas_tx_cmd_uuid.equals(bluetoothGattCharacteristic.getUuid().toString())) {
                            HTXAppOTA.this.ota_tx_cmd_charac = bluetoothGattCharacteristic;
                        } else if (SampleGattAttributes.otas_tx_ips_cmd_uuid.equals(bluetoothGattCharacteristic.getUuid().toString())) {
                            HTXAppOTA.this.ota_tx_cmd_charac = bluetoothGattCharacteristic;
                        } else if (SampleGattAttributes.otas_rx_ips_cmd_uuid.equals(bluetoothGattCharacteristic.getUuid().toString())) {
                            HTXAppOTA.this.ota_rx_cmd_charac = bluetoothGattCharacteristic;
                            if (HTXAppOTA.this.ota_rx_cmd_charac.getProperties() != 16) {
                                continue;
                            } else {
                                if (!HTXAppOTA.this.mBLE.setCharacteristicNotification(bluetoothGattCharacteristic, true)) {
                                    obtain.arg1 = 18;
                                    HTXAppOTA.this.handler.sendMessage(obtain);
                                    ycBleLog.e("Notify failed!!");
                                    return;
                                }
                                ycBleLog.e("Set Notify Success");
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
            HTXAppOTA.this.handler.postDelayed(new Runnable() { // from class: ycble.runchinaup.ota.absimpl.htx.HTXAppOTA.3.2
                @Override // java.lang.Runnable
                public void run() {
                    HTXAppOTA.this.loadData();
                }
            }, 1000L);
        }
    };
    private final ServiceConnection mServiceConnection = new ServiceConnection() { // from class: ycble.runchinaup.ota.absimpl.htx.HTXAppOTA.4
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            HTXAppOTA.this.mBLE = ((BluetoothLeService.LocalBinder) iBinder).getService();
            ycBleLog.e("in onServiceConnected!!!");
            if (!HTXAppOTA.this.mBLE.initialize()) {
                ycBleLog.e("Unable to initialize Bluetooth");
            }
            HTXAppOTA.this.mBLE.connect(HTXAppOTA.this.mDeviceAddress);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            ycBleLog.e("in onServiceDisconnected!!!");
            HTXAppOTA.this.mBLE = null;
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void SendFileRseponse(int i) {
        String str;
        if (i != 0) {
            switch (i) {
                case -1404:
                    str = "User Addr error !";
                    break;
                case Constant.USERADDRERROR /* -1403 */:
                    str = "Error sending upgrade package format!";
                    break;
                case Constant.EXECFORMATERROR /* -1402 */:
                    str = "Send load binary file error!";
                    break;
                default:
                    switch (i) {
                        case Constant.FILETOBIGERROR /* -1203 */:
                            str = "Too large a file to send!";
                            break;
                        case Constant.INVALIDPARAMETERERROR /* -1202 */:
                            str = "Send parameter error!";
                            break;
                        case Constant.NORESPONSEERROR /* -1201 */:
                            str = "OTA is not response!";
                            break;
                        default:
                            str = "返回值是：" + i;
                            break;
                    }
            }
        } else {
            str = "OTA has been successful!";
        }
        Message obtainMessage = this.handler.obtainMessage();
        obtainMessage.arg1 = 9;
        obtainMessage.obj = str;
        this.handler.sendMessage(obtainMessage);
        this.mIsWorcking = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadData() {
        this.mIsWorcking = true;
        this.isSuccess = false;
        this.mTransThread = new Thread(new Runnable() { // from class: ycble.runchinaup.ota.absimpl.htx.HTXAppOTA.1
            @Override // java.lang.Runnable
            public void run() {
                HTXAppOTA.this.do_work_on_boads.setEncrypt(false);
                try {
                    HTXAppOTA.this.SendFileRseponse(HTXAppOTA.this.do_work_on_boads.loadBinary(new Utils().readSDFile(HTXAppOTA.this.appFileStringPath), 3));
                } catch (IOException e) {
                    e.printStackTrace();
                    HTXAppOTA.this.mIsWorcking = false;
                }
            }
        });
        this.mTransThread.start();
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CHARACTER_NOTIFY);
        intentFilter.addAction(BluetoothLeService.OTA_RX_CMD_ACTION);
        intentFilter.addAction(BluetoothLeService.OTA_RX_DAT_ACTION);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_STATUS_133);
        intentFilter.addAction(BluetoothLeService.OTA_RX_ISP_CMD_ACTION);
        return intentFilter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void free() {
        try {
            if (this.context != null) {
                this.context.unregisterReceiver(this.mGattUpdateReceiver);
            }
            if (this.mServiceConnection != null) {
                this.context.unbindService(this.mServiceConnection);
            }
            if (this.handler != null) {
                this.handler.removeCallbacksAndMessages(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAppFileStringPath(String str) {
        this.appFileStringPath = str;
    }

    public void setOtaCallback(OTACallback oTACallback) {
        this.otaCallback = oTACallback;
    }

    public void setmDeviceAddress(String str) {
        this.mDeviceAddress = str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startOTA(Context context) {
        this.context = context;
        this.do_work_on_boads = new WorkOnBoads(context, this.handler);
        Intent intent = new Intent(context, (Class<?>) BluetoothLeService.class);
        context.startService(intent);
        context.bindService(intent, this.mServiceConnection, 1);
        context.registerReceiver(this.mGattUpdateReceiver, makeGattUpdateIntentFilter());
        ycBleLog.e("startOTA======> startOTA");
    }
}
