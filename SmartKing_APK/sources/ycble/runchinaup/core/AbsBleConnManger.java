package ycble.runchinaup.core;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import com.google.gson.Gson;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import me.panpf.sketch.uri.FileUriModel;
import ycble.runchinaup.BleCfg;
import ycble.runchinaup.core.callback.ScanListener;
import ycble.runchinaup.device.BleDevice;
import ycble.runchinaup.exception.BleUUIDNullException;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes2.dex */
public final class AbsBleConnManger {
    private BluetoothGatt bluetoothGatt;
    private Context context;
    private BluetoothAdapter bluetoothAdapter = null;
    private long startConnTime = 0;
    private HashSet<UUID> mustUUIDList = new HashSet<>();
    private Handler handler = new Handler();
    private boolean boolIsInterceptConn = false;
    private boolean isConnected = false;
    private boolean hasServicesDiscovered = false;
    private boolean hadScanDeviceFlag = true;
    private boolean isHandDisConn = false;
    private boolean hasConn = false;
    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() { // from class: ycble.runchinaup.core.AbsBleConnManger.2
        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            super.onCharacteristicChanged(bluetoothGatt, bluetoothGattCharacteristic);
            if (AbsBleConnManger.this.absBleConnCallback != null) {
                AbsBleConnManger.this.absBleConnCallback.onDataChange(bluetoothGattCharacteristic.getUuid(), bluetoothGattCharacteristic.getValue());
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            super.onCharacteristicRead(bluetoothGatt, bluetoothGattCharacteristic, i);
            if (i == 0) {
                if (AbsBleConnManger.this.absBleConnCallback != null) {
                    AbsBleConnManger.this.absBleConnCallback.onDataRead(true, bluetoothGattCharacteristic.getValue(), bluetoothGattCharacteristic.getUuid());
                }
            } else if (AbsBleConnManger.this.absBleConnCallback != null) {
                AbsBleConnManger.this.absBleConnCallback.onException(ErrCode.ERR_READ_CHARA, "onCharacteristicRead", new UUID[0]);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            super.onCharacteristicWrite(bluetoothGatt, bluetoothGattCharacteristic, i);
            if (i == 0) {
                if (AbsBleConnManger.this.absBleConnCallback != null) {
                    AbsBleConnManger.this.absBleConnCallback.onDataWrite(true, bluetoothGattCharacteristic.getValue(), bluetoothGattCharacteristic.getUuid());
                }
            } else if (AbsBleConnManger.this.absBleConnCallback != null) {
                AbsBleConnManger.this.absBleConnCallback.onException(ErrCode.ERR_WRITE_CHARA, "onCharacteristicWrite", bluetoothGattCharacteristic.getUuid());
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(final BluetoothGatt bluetoothGatt, int i, int i2) {
            super.onConnectionStateChange(bluetoothGatt, i, i2);
            ycBleLog.e("===========================================" + this);
            ycBleLog.e(i + ":" + i2);
            ycBleLog.e(PhoneBleExceptionCode.getPhoneCode(i));
            ycBleLog.e("===========================================");
            if (PhoneBleExceptionCode.isPhoneBleExcepiton(i)) {
                ycBleLog.e("系统蓝牙挂壁了");
                if (AbsBleConnManger.this.absBleConnCallback != null) {
                    AbsBleConnManger.this.absBleConnCallback.connResult(BleConnState.PHONEBLEANR);
                    return;
                }
                return;
            }
            if (i != 0 || i2 != 2) {
                ycBleLog.e("没有连接的所有情况====>>>>>>>>");
                AbsBleConnManger.this.isConnected = false;
                if (!AbsBleConnManger.this.hasConn) {
                    ycBleLog.e("如果是断开之前没有连接，很明显，异常连接");
                    if (AbsBleConnManger.this.absBleConnCallback != null) {
                        AbsBleConnManger.this.absBleConnCallback.connResult(BleConnState.CONNEXCEPTION);
                    }
                } else if (AbsBleConnManger.this.boolIsInterceptConn) {
                    if (AbsBleConnManger.this.absBleConnCallback != null) {
                        AbsBleConnManger.this.absBleConnCallback.connResult(BleConnState.HANDDISCONN);
                    }
                } else if (AbsBleConnManger.this.absBleConnCallback != null) {
                    AbsBleConnManger.this.absBleConnCallback.connResult(AbsBleConnManger.this.isHandDisConn ? BleConnState.HANDDISCONN : BleConnState.CONNEXCEPTION);
                }
                AbsBleConnManger.this.boolIsInterceptConn = false;
                AbsBleConnManger.this.bluetoothGatt.disconnect();
                AbsBleConnManger.this.close(AbsBleConnManger.this.bluetoothGatt);
                if (AbsBleConnManger.this.hasConn) {
                    AbsBleConnManger.this.hasConn = false;
                }
                AbsBleConnManger.refreshCache(AbsBleConnManger.this.context, AbsBleConnManger.this.bluetoothGatt);
                return;
            }
            ycBleLog.e("================设备连接上了，耗时:" + ((System.currentTimeMillis() - AbsBleConnManger.this.startConnTime) / 1000) + "S");
            AbsBleConnManger.this.hasConn = true;
            if (AbsBleConnManger.this.boolIsInterceptConn) {
                ycBleLog.e("================有拦截请求，此处断开");
                AbsBleConnManger.this.handler.postDelayed(new Runnable() { // from class: ycble.runchinaup.core.AbsBleConnManger.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        bluetoothGatt.disconnect();
                    }
                }, 500L);
                return;
            }
            AbsBleConnManger.this.bluetoothGatt = bluetoothGatt;
            AbsBleConnManger.this.isConnected = true;
            if (AbsBleConnManger.this.absBleConnCallback != null) {
                AbsBleConnManger.this.absBleConnCallback.connResult(BleConnState.CONNECTED);
            }
            ycBleLog.e("先移除所有的关于一次连接的消息队列");
            AbsBleConnManger.this.hasServicesDiscovered = false;
            AbsBleConnManger.this.handler.removeCallbacksAndMessages(null);
            AbsBleConnManger.this.handler.postDelayed(new Runnable() { // from class: ycble.runchinaup.core.AbsBleConnManger.2.2
                @Override // java.lang.Runnable
                public void run() {
                    if (!AbsBleConnManger.this.isConnected || AbsBleConnManger.this.bluetoothGatt == null) {
                        ycBleLog.e("500毫秒后，不再连接是情况，移除discoverService");
                        AbsBleConnManger.this.handler.removeCallbacks(AbsBleConnManger.this.discoverServiceRunnable);
                        return;
                    }
                    ycBleLog.e("500毫秒后，如果还连接上的，则开始扫描服务");
                    boolean discoverServices = AbsBleConnManger.this.bluetoothGatt.discoverServices();
                    ycBleLog.e("discoverServices结果:" + discoverServices);
                    if (discoverServices) {
                        AbsBleConnManger.this.handler.postDelayed(AbsBleConnManger.this.discoverServiceRunnable, 1000L);
                    } else if (bluetoothGatt != null) {
                        bluetoothGatt.disconnect();
                    }
                }
            }, 500L);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            super.onDescriptorRead(bluetoothGatt, bluetoothGattDescriptor, i);
            if (i == 0) {
                if (AbsBleConnManger.this.absBleConnCallback != null) {
                    AbsBleConnManger.this.absBleConnCallback.onDataRead(false, bluetoothGattDescriptor.getValue(), bluetoothGattDescriptor.getCharacteristic().getUuid(), bluetoothGattDescriptor.getUuid());
                }
            } else if (AbsBleConnManger.this.absBleConnCallback != null) {
                AbsBleConnManger.this.absBleConnCallback.onException(ErrCode.ERR_READ_DESCR, "onDescriptorRead", new UUID[0]);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
            super.onDescriptorWrite(bluetoothGatt, bluetoothGattDescriptor, i);
            if (i == 0) {
                if (AbsBleConnManger.this.absBleConnCallback != null) {
                    AbsBleConnManger.this.absBleConnCallback.onDataWrite(false, bluetoothGattDescriptor.getValue(), bluetoothGattDescriptor.getCharacteristic().getUuid(), bluetoothGattDescriptor.getUuid());
                }
            } else if (AbsBleConnManger.this.absBleConnCallback != null) {
                AbsBleConnManger.this.absBleConnCallback.onException(ErrCode.ERR_WRITE_DESCR, "onDescriptorWrite", new UUID[0]);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onReadRemoteRssi(BluetoothGatt bluetoothGatt, int i, int i2) {
            super.onReadRemoteRssi(bluetoothGatt, i, i2);
            if (i2 == 0) {
                if (AbsBleConnManger.this.absBleConnCallback != null) {
                    AbsBleConnManger.this.absBleConnCallback.onRssi(i);
                }
            } else if (AbsBleConnManger.this.absBleConnCallback != null) {
                AbsBleConnManger.this.absBleConnCallback.onException(ErrCode.ERR_RSSI, "onReadRemoteRssi", new UUID[0]);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            super.onServicesDiscovered(bluetoothGatt, i);
            ycBleLog.e("onServicesDiscovered==status=>" + i + new Gson().toJson(AbsBleConnManger.this.mustUUIDList));
            int i2 = 0;
            if (i != 0) {
                if (AbsBleConnManger.this.absBleConnCallback != null) {
                    AbsBleConnManger.this.absBleConnCallback.onException(ErrCode.ERR_DISCOVER_SERVICE, "onServicesDiscovered", new UUID[0]);
                    return;
                }
                return;
            }
            Iterator<BluetoothGattService> it = bluetoothGatt.getServices().iterator();
            while (it.hasNext()) {
                for (BluetoothGattCharacteristic bluetoothGattCharacteristic : it.next().getCharacteristics()) {
                    Iterator it2 = AbsBleConnManger.this.mustUUIDList.iterator();
                    while (it2.hasNext()) {
                        if (((UUID) it2.next()).equals(bluetoothGattCharacteristic.getUuid())) {
                            i2++;
                        }
                    }
                }
            }
            AbsBleConnManger.this.hasServicesDiscovered = true;
            AbsBleConnManger.this.handler.removeCallbacks(AbsBleConnManger.this.discoverServiceRunnable);
            if (i2 != AbsBleConnManger.this.mustUUIDList.size()) {
                ycBleLog.e("uuid对不上，情况不对");
                bluetoothGatt.disconnect();
            } else if (AbsBleConnManger.this.absBleConnCallback != null) {
                AbsBleConnManger.this.absBleConnCallback.onLoadCharas(bluetoothGatt);
            }
        }
    };
    private AbsBleConnCallback absBleConnCallback = null;
    private Runnable discoverServiceRunnable = new Runnable() { // from class: ycble.runchinaup.core.AbsBleConnManger.3
        @Override // java.lang.Runnable
        public void run() {
            ycBleLog.e("1000毫秒后，如果还连接上的，检测服务有没有被扫描到");
            if (AbsBleConnManger.this.hasServicesDiscovered) {
                AbsBleConnManger.this.handler.removeCallbacks(AbsBleConnManger.this.discoverServiceRunnable);
                return;
            }
            ycBleLog.e("服务没有被检测到");
            if (AbsBleConnManger.this.bluetoothGatt != null) {
                AbsBleConnManger.this.bluetoothGatt.disconnect();
            }
        }
    };

    /* loaded from: classes2.dex */
    public static abstract class AbsBleConnCallback {
        protected abstract void connResult(BleConnState bleConnState);

        /* JADX INFO: Access modifiers changed from: protected */
        public void onDataChange(UUID uuid, byte[] bArr) {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onDataRead(boolean z, byte[] bArr, UUID... uuidArr) {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onDataWrite(boolean z, byte[] bArr, UUID... uuidArr) {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onException(ErrCode errCode, String str, UUID... uuidArr) {
            ycBleLog.e("nopointer/npBle->debug:on ERROR " + str);
        }

        protected abstract void onLoadCharas(BluetoothGatt bluetoothGatt);

        /* JADX INFO: Access modifiers changed from: protected */
        public void onRssi(int i) {
        }
    }

    public AbsBleConnManger(Context context) {
        this.context = context;
        initBleAdapter();
    }

    private void clearFlag() {
        this.hasServicesDiscovered = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void close(BluetoothGatt bluetoothGatt) {
        ycBleLog.e("close Gatt");
        if (bluetoothGatt != null) {
            bluetoothGatt.close();
        }
    }

    public static List<BluetoothDevice> connDeviceList(Context context) {
        BluetoothManager bluetoothManager;
        ycBleLog.d("nopointer/npBle->debug:读取连接的蓝牙设备");
        if (context == null || (bluetoothManager = (BluetoothManager) context.getSystemService("bluetooth")) == null || bluetoothManager.getAdapter() == null || !bluetoothManager.getAdapter().isEnabled()) {
            return null;
        }
        List<BluetoothDevice> connectedDevices = bluetoothManager.getConnectedDevices(7);
        for (BluetoothDevice bluetoothDevice : connectedDevices) {
            ycBleLog.d("debug==>device：===>" + bluetoothDevice.getAddress() + "==" + bluetoothDevice.getName());
        }
        return connectedDevices;
    }

    private boolean enableNotifyOrIndication(UUID uuid, UUID uuid2, boolean z, byte[] bArr) throws BleUUIDNullException {
        BluetoothGattCharacteristic chara = getChara(getService(uuid), uuid2);
        BluetoothGattDescriptor descriptor = getDescriptor(chara, UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        this.bluetoothGatt.setCharacteristicNotification(chara, z);
        descriptor.setValue(bArr);
        boolean writeDescriptor = this.bluetoothGatt.writeDescriptor(descriptor);
        ycBleLog.i("nopointer/npBle->debug:notify/indication:" + uuid.toString() + "-->" + uuid2.toString() + "-->" + descriptor + "-->" + writeDescriptor + "{ " + BleUtil.byte2HexStr(bArr) + " }");
        return writeDescriptor;
    }

    private BluetoothGattCharacteristic getChara(BluetoothGattService bluetoothGattService, UUID uuid) throws BleUUIDNullException {
        BluetoothGattCharacteristic characteristic = bluetoothGattService.getCharacteristic(uuid);
        if (characteristic != null) {
            return characteristic;
        }
        throw new BleUUIDNullException(String.format("not find this uuid %s for BluetoothGattCharacteristic please check service  or charateristic uuid", uuid.toString()));
    }

    private BluetoothGattDescriptor getDescriptor(BluetoothGattCharacteristic bluetoothGattCharacteristic, UUID uuid) throws BleUUIDNullException {
        BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(uuid);
        if (descriptor != null) {
            return descriptor;
        }
        throw new BleUUIDNullException(String.format("not find this uuid %s for BluetoothGattCharacteristic please check service  or charateristic or descriptor uuid", uuid.toString()));
    }

    private BluetoothGattService getService(UUID uuid) throws BleUUIDNullException {
        if (this.bluetoothGatt == null) {
            throw new BleUUIDNullException(String.format("not find this bluetoothGatt", uuid.toString()));
        }
        BluetoothGattService service = this.bluetoothGatt.getService(uuid);
        if (service != null) {
            return service;
        }
        throw new BleUUIDNullException(String.format("not find this uuid %s for BluetoothGattService", uuid.toString()));
    }

    private void initBleAdapter() {
        if (this.context == null) {
            this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            return;
        }
        BluetoothManager bluetoothManager = (BluetoothManager) this.context.getSystemService("bluetooth");
        if (bluetoothManager == null) {
            this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        } else {
            this.bluetoothAdapter = bluetoothManager.getAdapter();
        }
    }

    public static BluetoothDevice isInConnList(String str, Context context) {
        List<BluetoothDevice> connDeviceList = connDeviceList(context);
        if (connDeviceList == null || connDeviceList.size() < 1) {
            return null;
        }
        for (BluetoothDevice bluetoothDevice : connDeviceList) {
            if (bluetoothDevice.getAddress().equalsIgnoreCase(str)) {
                return bluetoothDevice;
            }
        }
        return null;
    }

    public static void refreshCache(Context context, BluetoothGatt bluetoothGatt) {
        List<BluetoothDevice> connectedDevices = ((BluetoothManager) context.getSystemService("bluetooth")).getConnectedDevices(7);
        ycBleLog.e(BleCfg.npBleTag + connectedDevices.size() + "");
        Iterator<BluetoothDevice> it = connectedDevices.iterator();
        while (it.hasNext()) {
            ycBleLog.d(BleCfg.npBleTag + it.next().getAddress());
        }
        try {
            Method method = bluetoothGatt.getClass().getMethod("refresh", new Class[0]);
            if (method != null) {
                method.invoke(bluetoothGatt, new Object[0]);
                ycBleLog.e("nopointer/npBle->debug:刷新BLE缓存");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ycBleLog.e("nopointer/npBle->debug:An exception occured while refreshing device");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addMustUUID(UUID uuid) {
        this.mustUUIDList.add(uuid);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void connect(final BluetoothDevice bluetoothDevice) {
        ycBleLog.e("当前实际连接设备是:" + new Gson().toJson(new String[]{bluetoothDevice.getAddress(), bluetoothDevice.getName()}));
        this.boolIsInterceptConn = false;
        this.isHandDisConn = false;
        this.startConnTime = System.currentTimeMillis();
        if (TextUtils.isEmpty(bluetoothDevice.getName())) {
            ycBleLog.e("名称为空，需要开启一下扫描来缓存一下设备名称");
            this.hadScanDeviceFlag = true;
            BleScanner.getInstance().registerScanListener(new ScanListener() { // from class: ycble.runchinaup.core.AbsBleConnManger.1
                @Override // ycble.runchinaup.core.callback.ScanListener
                public void onFailure(int i) {
                }

                @Override // ycble.runchinaup.core.callback.ScanListener
                public void onScan(BleDevice bleDevice) {
                    ycBleLog.i("hadScanDeviceFlag=====>" + AbsBleConnManger.this.hadScanDeviceFlag + "///扫描到的设备:" + new Gson().toJson(bleDevice));
                    if (AbsBleConnManger.this.hadScanDeviceFlag && bleDevice != null && bleDevice.getMac().equalsIgnoreCase(bluetoothDevice.getAddress())) {
                        BleScanner.getInstance().unRegisterScanListener(this);
                        AbsBleConnManger.this.hadScanDeviceFlag = false;
                        BleScanner.getInstance().stopScan();
                        ycBleLog.e("扫描到设备了，停止扫描，然后再连接");
                        AbsBleConnManger.this.handler.postDelayed(new Runnable() { // from class: ycble.runchinaup.core.AbsBleConnManger.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AbsBleConnManger.this.bluetoothGatt = bluetoothDevice.connectGatt(AbsBleConnManger.this.context, false, AbsBleConnManger.this.gattCallback);
                            }
                        }, 2000L);
                    }
                }
            });
            BleScanner.getInstance().startScan();
        } else {
            this.bluetoothGatt = bluetoothDevice.connectGatt(this.context, false, this.gattCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void connect(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ycBleLog.e("发起连接请求的mac:" + str);
        initBleAdapter();
        connect(this.bluetoothAdapter.getRemoteDevice(str));
    }

    public boolean disAbleNotityOrIndication(UUID uuid, UUID uuid2) throws BleUUIDNullException {
        return enableNotifyOrIndication(uuid, uuid2, false, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
    }

    public void disConnect() {
        ycBleLog.e("=====>手动断开指令");
        this.isHandDisConn = true;
        if (this.bluetoothGatt == null || !this.isConnected) {
            ycBleLog.e("没有在连接中，发出拦截请求即连接后立马断开）");
            this.boolIsInterceptConn = true;
        } else {
            ycBleLog.e("已经在连接中，就不发出拦截请求了，直接断开");
            this.bluetoothGatt.disconnect();
        }
    }

    public boolean enableIndication(UUID uuid, UUID uuid2) throws BleUUIDNullException {
        return enableNotifyOrIndication(uuid, uuid2, true, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
    }

    public boolean enableNotity(UUID uuid, UUID uuid2) throws BleUUIDNullException {
        return enableNotifyOrIndication(uuid, uuid2, true, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
    }

    protected BluetoothGatt getBluetoothGatt() {
        return this.bluetoothGatt;
    }

    public int getClientIf(BluetoothGatt bluetoothGatt) {
        try {
            try {
                Field declaredField = bluetoothGatt.getClass().getDeclaredField("mClientIf");
                declaredField.setAccessible(true);
                return ((Integer) declaredField.get(bluetoothGatt)).intValue();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } catch (Throwable unused) {
            return 0;
        }
    }

    public boolean isHandDisConn() {
        return this.isHandDisConn;
    }

    public boolean readData(UUID uuid, UUID uuid2) throws BleUUIDNullException {
        ycBleLog.i("nopointer/npBle->debug:->read:" + uuid.toString() + FileUriModel.SCHEME + uuid2.toString());
        return this.bluetoothGatt.readCharacteristic(getChara(getService(uuid), uuid2));
    }

    public boolean readData(UUID uuid, UUID uuid2, UUID uuid3) throws BleUUIDNullException {
        return this.bluetoothGatt.readDescriptor(getDescriptor(getChara(getService(uuid), uuid2), uuid3));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void requestConnectionPriority(int i) {
        if (this.bluetoothGatt == null || Build.VERSION.SDK_INT < 21) {
            return;
        }
        this.bluetoothGatt.requestConnectionPriority(1);
    }

    public void setAbsBleConnAndStateCallback(AbsBleConnCallback absBleConnCallback) {
        this.absBleConnCallback = absBleConnCallback;
    }

    public void setHadScanDeviceFlag(boolean z) {
        this.hadScanDeviceFlag = z;
    }

    public synchronized void setHandDisConn(boolean z) {
        ycBleLog.e("handDisConn===>" + z);
        this.isHandDisConn = z;
    }

    public boolean writeData(UUID uuid, UUID uuid2, UUID uuid3, byte[] bArr) throws BleUUIDNullException {
        BluetoothGattDescriptor descriptor = getDescriptor(getChara(getService(uuid), uuid2), uuid3);
        descriptor.setValue(bArr);
        boolean writeDescriptor = this.bluetoothGatt.writeDescriptor(descriptor);
        ycBleLog.e("nopointer/npBle->debug:->write:" + uuid.toString() + FileUriModel.SCHEME + uuid2.toString() + FileUriModel.SCHEME + writeDescriptor + "{ " + BleUtil.byte2HexStr(bArr) + " }");
        return writeDescriptor;
    }

    public boolean writeData(UUID uuid, UUID uuid2, byte[] bArr) throws BleUUIDNullException {
        BluetoothGattCharacteristic chara = getChara(getService(uuid), uuid2);
        chara.setWriteType(2);
        chara.setValue(bArr);
        boolean writeCharacteristic = this.bluetoothGatt.writeCharacteristic(chara);
        ycBleLog.e("默认写:" + uuid.toString() + FileUriModel.SCHEME + uuid2.toString() + ">" + String.format(" [%d] ", Integer.valueOf(bArr.length)) + writeCharacteristic + "< " + BleUtil.byte2HexStr(bArr) + " >");
        return writeCharacteristic;
    }

    public boolean writeDataWithOutResponse(UUID uuid, UUID uuid2, byte[] bArr) throws BleUUIDNullException {
        BluetoothGattCharacteristic chara = getChara(getService(uuid), uuid2);
        chara.setWriteType(1);
        chara.setValue(bArr);
        boolean writeCharacteristic = this.bluetoothGatt.writeCharacteristic(chara);
        ycBleLog.e("无响应写:" + uuid.toString() + FileUriModel.SCHEME + uuid2.toString() + ">" + String.format(" [%d] ", Integer.valueOf(bArr.length)) + writeCharacteristic + "< " + BleUtil.byte2HexStr(bArr) + " >");
        return writeCharacteristic;
    }
}
