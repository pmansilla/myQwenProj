package ycble.runchinaup.ota.absimpl.htx;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes2.dex */
public class BluetoothLeService extends Service {
    public static final String ACTION_BLE_RECV_DATA = "com.example.bluetooth.le.ACTION_BLE_RECV_DATA";
    public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_GATT_CHARACTER_NOTIFY = "com.example.bluetooth.le.ACTION_GATT_CHARACTER_NOTIFY";
    public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String ACTION_GATT_STATUS_133 = "com.example.bluetooth.le.ACTION_GATT_STATUS_133";
    public static final String ACTION_GATT_WRITE_RESULT = "com.example.bluetooth.le.ACTION_GATT_WRITE_RESULT";
    public static final String ACTION_THEMOMETER_RECV_VALUE = "com.example.bluetooth.le.ACTION_THEMOMETER_RECV_VALUE";
    public static final String ARRAY_BYTE_DATA = "com.example.bluetooth.le.ARRAY_BYTE_DATA";
    public static final String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    public static final String OTA_RX_CMD_ACTION = "com.hs.bluetooth.le.OTA_RX_CMD_ACTION";
    public static final String OTA_RX_DAT_ACTION = "com.hs.bluetooth.le.OTA_RX_DAT_ACTION";
    public static final String OTA_RX_ISP_CMD_ACTION = "com.hs.bluetooth.le.OTA_RX_ISP_CMD_ACTION";
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 0;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    private Context mContext;
    public static Semaphore write_characer_lock = new Semaphore(1);
    private static int mPlayCount = 0;
    private static final String TAG = BluetoothLeService.class.getSimpleName();
    public static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
    public static final UUID UUID_RSSI_VALUE = UUID.fromString(SampleGattAttributes.RSSI_VALUE);
    public static final UUID UUID_RSSI_CONFIGARATION = UUID.fromString(SampleGattAttributes.RSSI_CONFIGARATION);
    public static final UUID UUID_BLUE_RECV_VALUE = UUID.fromString(SampleGattAttributes.BLUE_RECV_VALUE);
    public static final UUID UUID_TEMPERATURE_MEASUREMENT = UUID.fromString(SampleGattAttributes.TEMP_MEASUREMENT);
    public static final UUID UUID_OTA_TX_CMD = UUID.fromString(SampleGattAttributes.otas_tx_cmd_uuid);
    public static final UUID UUID_ISP_TX_CMD = UUID.fromString(SampleGattAttributes.otas_tx_ips_cmd_uuid);
    public static final UUID UUID_OTA_TX_DAT = UUID.fromString(SampleGattAttributes.otas_tx_dat_uuid);
    public static final UUID UUID_OTA_RX_CMD = UUID.fromString(SampleGattAttributes.otas_rx_cmd_uuid);
    public static final UUID UUID_ISP_RX_CMD = UUID.fromString(SampleGattAttributes.otas_rx_ips_cmd_uuid);
    public static final UUID UUID_OTA_RX_DAT = UUID.fromString(SampleGattAttributes.otas_rx_dat_uuid);
    private int mRes = 0;
    private final IBinder mBinder = new LocalBinder();
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() { // from class: ycble.runchinaup.ota.absimpl.htx.BluetoothLeService.1
        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            if (SampleGattAttributes.otas_rx_dat_uuid.equals(bluetoothGattCharacteristic.getUuid().toString())) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.OTA_RX_DAT_ACTION, bluetoothGattCharacteristic);
            }
            if (SampleGattAttributes.otas_rx_ips_cmd_uuid.equals(bluetoothGattCharacteristic.getUuid().toString())) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.OTA_RX_ISP_CMD_ACTION, bluetoothGattCharacteristic);
            }
            if (SampleGattAttributes.otas_rx_cmd_uuid.equals(bluetoothGattCharacteristic.getUuid().toString())) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.OTA_RX_CMD_ACTION, bluetoothGattCharacteristic);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, bluetoothGattCharacteristic);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            Log.i("DEBUG_OTA", "write status: " + i);
            BluetoothLeService.write_characer_lock.release(1);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            if (i == 133) {
                BluetoothLeService.this.refreshDeviceCache();
                BluetoothLeService.this.close();
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_STATUS_133);
                return;
            }
            if (i2 != 2) {
                if (i2 == 0) {
                    BluetoothLeService.this.refreshDeviceCache();
                    BluetoothLeService.this.close();
                    BluetoothLeService.write_characer_lock.release();
                    Log.i(BluetoothLeService.TAG, "Disconnected from GATT server.");
                    BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_DISCONNECTED);
                    return;
                }
                return;
            }
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_CONNECTED);
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(BluetoothLeService.TAG, "Connected to GATT server.");
            Log.i(BluetoothLeService.TAG, "Attempting to start service discovery:" + BluetoothLeService.this.mBluetoothGatt.discoverServices());
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            if (i == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
                Log.i(BluetoothLeService.TAG, "onServicesDiscovered");
                return;
            }
            Log.w(BluetoothLeService.TAG, "onServicesDiscovered received: " + i);
        }
    };

    /* loaded from: classes2.dex */
    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastUpdate(String str) {
        Intent intent = new Intent(str);
        Log.i(TAG, "in " + str);
        sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastUpdate(String str, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        int i;
        Intent intent = new Intent(str);
        if (UUID_OTA_RX_DAT.equals(bluetoothGattCharacteristic.getUuid())) {
            byte[] value = bluetoothGattCharacteristic.getValue();
            if (value != null && value.length > 0) {
                intent.putExtra(ARRAY_BYTE_DATA, value);
            }
        } else if (UUID_OTA_RX_CMD.equals(bluetoothGattCharacteristic.getUuid())) {
            byte[] value2 = bluetoothGattCharacteristic.getValue();
            if (value2 != null && value2.length > 0) {
                intent.putExtra(ARRAY_BYTE_DATA, value2);
            }
        } else if (UUID_ISP_RX_CMD.equals(bluetoothGattCharacteristic.getUuid())) {
            byte[] value3 = bluetoothGattCharacteristic.getValue();
            if (value3 != null && value3.length > 0) {
                intent.putExtra(ARRAY_BYTE_DATA, value3);
            }
        } else if (UUID_HEART_RATE_MEASUREMENT.equals(bluetoothGattCharacteristic.getUuid())) {
            if ((bluetoothGattCharacteristic.getProperties() & 1) != 0) {
                i = 18;
                ycBleLog.e("Heart rate format UINT16.");
            } else {
                i = 17;
                ycBleLog.e("Heart rate format UINT8.");
            }
            int intValue = bluetoothGattCharacteristic.getIntValue(i, 1).intValue();
            ycBleLog.e(String.format("Received heart rate: %d", Integer.valueOf(intValue)));
            intent.putExtra(EXTRA_DATA, String.valueOf(intValue));
        } else if (UUID_RSSI_VALUE.equals(bluetoothGattCharacteristic.getUuid())) {
            byte[] value4 = bluetoothGattCharacteristic.getValue();
            if (value4 != null && value4.length > 0) {
                StringBuilder sb = new StringBuilder(value4.length);
                for (byte b : value4) {
                    sb.append(String.format("%d ", Byte.valueOf(b)));
                }
                intent.putExtra(EXTRA_DATA, new String("RSSI = ") + sb.toString());
            }
        } else if (UUID_TEMPERATURE_MEASUREMENT.equals(bluetoothGattCharacteristic.getUuid())) {
            byte[] value5 = bluetoothGattCharacteristic.getValue();
            if (value5 != null) {
                intent.putExtra(EXTRA_DATA, value5);
            }
        } else if (UUID_RSSI_CONFIGARATION.equals(bluetoothGattCharacteristic.getUuid())) {
            byte[] value6 = bluetoothGattCharacteristic.getValue();
            if (value6 != null && value6.length > 0) {
                StringBuilder sb2 = new StringBuilder(value6.length);
                for (byte b2 : value6) {
                    sb2.append(String.format("%02x", Byte.valueOf(b2)));
                }
                intent.putExtra(EXTRA_DATA, new String("RSSI_CONFIGARATION = 0x") + sb2.toString());
            }
        } else if (UUID_BLUE_RECV_VALUE.equals(bluetoothGattCharacteristic.getUuid())) {
            byte[] value7 = bluetoothGattCharacteristic.getValue();
            if (value7 != null && value7.length > 0) {
                intent.putExtra(EXTRA_DATA, new String(value7));
            }
        } else {
            byte[] value8 = bluetoothGattCharacteristic.getValue();
            if (value8 != null && value8.length > 0) {
                StringBuilder sb3 = new StringBuilder(value8.length);
                for (byte b3 : value8) {
                    sb3.append(String.format("%02X ", Byte.valueOf(b3)));
                }
                intent.putExtra(EXTRA_DATA, new String(value8) + "\n" + sb3.toString());
            }
        }
        sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean refreshDeviceCache() {
        if (this.mBluetoothGatt != null) {
            try {
                BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
                Method method = bluetoothGatt.getClass().getMethod("refresh", new Class[0]);
                if (method != null) {
                    return ((Boolean) method.invoke(bluetoothGatt, new Object[0])).booleanValue();
                }
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
        return false;
    }

    public void close() {
        if (this.mBluetoothGatt == null) {
            return;
        }
        this.mBluetoothGatt.close();
        this.mBluetoothGatt = null;
    }

    public boolean connect(String str) {
        if (this.mBluetoothAdapter == null || str == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        if (this.mBluetoothDeviceAddress != null && str.equals(this.mBluetoothDeviceAddress) && this.mBluetoothGatt != null) {
            ycBleLog.e("Trying to use an existing mBluetoothGatt for connection.");
            return this.mBluetoothGatt.connect();
        }
        BluetoothDevice remoteDevice = this.mBluetoothAdapter.getRemoteDevice(str);
        if (remoteDevice == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        this.mBluetoothGatt = remoteDevice.connectGatt(this.mContext, false, this.mGattCallback);
        ycBleLog.e("Trying to create a new connection.");
        this.mBluetoothDeviceAddress = str;
        return true;
    }

    public void disconnect() {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.disconnect();
        }
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (this.mBluetoothGatt == null) {
            return null;
        }
        return this.mBluetoothGatt.getServices();
    }

    public boolean initialize() {
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
            if (this.mBluetoothManager == null) {
                ycBleLog.e("Unable to initialize BluetoothManager.");
                return false;
            }
        }
        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        if (this.mBluetoothAdapter != null) {
            return true;
        }
        ycBleLog.e("Unable to obtain a BluetoothAdapter.");
        return false;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void readCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
        }
    }

    public boolean setCharacteristicIndication(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
        byte[] bArr = new byte[6];
        bArr[0] = 2;
        descriptor.setValue(bArr);
        this.mBluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, z);
        return this.mBluetoothGatt.writeDescriptor(descriptor);
    }

    public boolean setCharacteristicNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        this.mBluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, z);
        BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
        if (descriptor == null) {
            return false;
        }
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        return this.mBluetoothGatt.writeDescriptor(descriptor);
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        try {
            write_characer_lock.acquire(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.mBluetoothGatt == null) {
            return false;
        }
        boolean writeCharacteristic = this.mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
        if (!writeCharacteristic) {
            write_characer_lock.release();
            disconnect();
        }
        return writeCharacteristic;
    }
}
