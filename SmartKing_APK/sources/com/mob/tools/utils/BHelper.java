package com.mob.tools.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.MobLog;
import com.sun.mail.imap.IMAPStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes2.dex */
public class BHelper {
    private static final String TAG = "BHelper";
    private static BHelper instance;
    private Map<String, BOperationCallback> bOperationCallbackMap;
    private BroadcastReceiver bOperationReceiver;
    private BroadcastReceiver bScanReceiver;
    private Context context;
    private boolean mScanning = false;
    private boolean bOperationRegistered = false;
    private boolean bScanRegistered = false;

    /* loaded from: classes2.dex */
    public static class BOperationCallback {
        protected void onConnectionChanged(boolean z, HashMap<String, Object> hashMap) {
        }

        protected void onDeviceConnected(HashMap<String, Object> hashMap) {
        }

        protected void onDeviceDisconnected(HashMap<String, Object> hashMap) {
        }

        protected void onDisabled() {
        }

        protected void onEnabled() {
        }
    }

    /* loaded from: classes2.dex */
    public interface BScanCallback {
        void onScan(ArrayList<HashMap<String, Object>> arrayList);
    }

    private BHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    private BluetoothAdapter getBAdapter() {
        try {
            return Build.VERSION.SDK_INT >= 18 ? ((BluetoothManager) DeviceHelper.getInstance(this.context).getSystemServiceSafe("bluetooth")).getAdapter() : BluetoothAdapter.getDefaultAdapter();
        } catch (Throwable th) {
            MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
            return null;
        }
    }

    public static BHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (BHelper.class) {
                if (instance == null) {
                    instance = new BHelper(context);
                }
            }
        }
        return instance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"MissingPermission"})
    public HashMap<String, Object> parseDevice2Map(BluetoothDevice bluetoothDevice) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (bluetoothDevice != null) {
            try {
                hashMap.put(IMAPStore.ID_NAME, bluetoothDevice.getName());
                hashMap.put(IMAPStore.ID_ADDRESS, bluetoothDevice.getAddress());
                hashMap.put("bondState", Integer.valueOf(bluetoothDevice.getBondState()));
                BluetoothClass bluetoothClass = bluetoothDevice.getBluetoothClass();
                int majorDeviceClass = bluetoothClass.getMajorDeviceClass();
                int deviceClass = bluetoothClass.getDeviceClass();
                hashMap.put("majorClass", Integer.valueOf(majorDeviceClass));
                hashMap.put("deviceClass", Integer.valueOf(deviceClass));
                if (Build.VERSION.SDK_INT >= 18) {
                    hashMap.put("type", Integer.valueOf(bluetoothDevice.getType()));
                }
                if (Build.VERSION.SDK_INT >= 15 && bluetoothDevice.getBondState() == 12) {
                    ArrayList arrayList = new ArrayList();
                    ParcelUuid[] uuids = bluetoothDevice.getUuids();
                    if (uuids != null && uuids.length > 0) {
                        for (ParcelUuid parcelUuid : uuids) {
                            if (parcelUuid != null && parcelUuid.getUuid() != null) {
                                arrayList.add(parcelUuid.getUuid().toString());
                            }
                        }
                    }
                    hashMap.put("uuids", arrayList);
                }
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
        }
        return hashMap;
    }

    @SuppressLint({"MissingPermission"})
    public void findLE(int i, final BluetoothAdapter bluetoothAdapter, final BScanCallback bScanCallback) {
        try {
            if (!DeviceHelper.getInstance(this.context).checkPermission("android.permission.BLUETOOTH") || !DeviceHelper.getInstance(this.context).checkPermission("android.permission.BLUETOOTH_ADMIN")) {
                bScanCallback.onScan(new ArrayList<>());
                return;
            }
            if (!bluetoothAdapter.isEnabled()) {
                bScanCallback.onScan(new ArrayList<>());
                return;
            }
            if (this.mScanning) {
                bScanCallback.onScan(new ArrayList<>());
                return;
            }
            if (i <= 0) {
                i = 6;
            }
            if (Build.VERSION.SDK_INT < 18) {
                findLEAndClassic(i, bScanCallback);
                return;
            }
            final ArrayList arrayList = new ArrayList();
            if (Build.VERSION.SDK_INT < 21) {
                final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() { // from class: com.mob.tools.utils.BHelper.4
                    @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
                    public void onLeScan(BluetoothDevice bluetoothDevice, int i2, byte[] bArr) {
                        try {
                            HashMap parseDevice2Map = BHelper.this.parseDevice2Map(bluetoothDevice);
                            parseDevice2Map.put("rssi", Integer.valueOf(i2));
                            arrayList.add(parseDevice2Map);
                        } catch (Throwable th) {
                            MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
                        }
                    }
                };
                this.mScanning = true;
                bluetoothAdapter.startLeScan(leScanCallback);
                MobHandlerThread.newHandler(new Handler.Callback() { // from class: com.mob.tools.utils.BHelper.5
                    @Override // android.os.Handler.Callback
                    public boolean handleMessage(Message message) {
                        BHelper.this.mScanning = false;
                        bluetoothAdapter.stopLeScan(leScanCallback);
                        bScanCallback.onScan(arrayList);
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, i * 1000);
                return;
            }
            final ScanCallback scanCallback = new ScanCallback() { // from class: com.mob.tools.utils.BHelper.6
                @Override // android.bluetooth.le.ScanCallback
                public void onBatchScanResults(List<ScanResult> list) {
                    super.onBatchScanResults(list);
                }

                @Override // android.bluetooth.le.ScanCallback
                public void onScanFailed(int i2) {
                    super.onScanFailed(i2);
                }

                @Override // android.bluetooth.le.ScanCallback
                public void onScanResult(int i2, ScanResult scanResult) {
                    super.onScanResult(i2, scanResult);
                    if (scanResult != null) {
                        HashMap hashMap = new HashMap();
                        BluetoothDevice device = scanResult.getDevice();
                        if (device != null) {
                            hashMap = BHelper.this.parseDevice2Map(device);
                        }
                        hashMap.put("rssi", Integer.valueOf(scanResult.getRssi()));
                        scanResult.getScanRecord();
                        arrayList.add(hashMap);
                    }
                }
            };
            final BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            this.mScanning = true;
            bluetoothLeScanner.startScan(scanCallback);
            MobHandlerThread.newHandler(new Handler.Callback() { // from class: com.mob.tools.utils.BHelper.7
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message message) {
                    BHelper.this.mScanning = false;
                    bluetoothLeScanner.stopScan(scanCallback);
                    bScanCallback.onScan(arrayList);
                    return false;
                }
            }).sendEmptyMessageDelayed(0, i * 1000);
        } catch (Throwable th) {
            MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
            bScanCallback.onScan(new ArrayList<>());
        }
    }

    @SuppressLint({"MissingPermission"})
    public void findLEAndClassic(int i, final BScanCallback bScanCallback) {
        try {
            if (!DeviceHelper.getInstance(this.context).checkPermission("android.permission.BLUETOOTH") || !DeviceHelper.getInstance(this.context).checkPermission("android.permission.BLUETOOTH_ADMIN")) {
                bScanCallback.onScan(new ArrayList<>());
                return;
            }
            final BluetoothAdapter bAdapter = getBAdapter();
            if (!bAdapter.isEnabled()) {
                bScanCallback.onScan(new ArrayList<>());
                return;
            }
            if (this.mScanning) {
                bScanCallback.onScan(new ArrayList<>());
                return;
            }
            if (i <= 0) {
                i = 6;
            }
            final Handler newHandler = MobHandlerThread.newHandler(new Handler.Callback() { // from class: com.mob.tools.utils.BHelper.2
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message message) {
                    bAdapter.cancelDiscovery();
                    return false;
                }
            });
            newHandler.sendEmptyMessageDelayed(0, i * 1000);
            final ArrayList arrayList = new ArrayList();
            this.bScanReceiver = new BroadcastReceiver() { // from class: com.mob.tools.utils.BHelper.3
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    try {
                        String action = intent.getAction();
                        if (action.equals("android.bluetooth.device.action.FOUND")) {
                            HashMap parseDevice2Map = BHelper.this.parseDevice2Map((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE"));
                            parseDevice2Map.put("rssi", Short.valueOf(intent.getShortExtra("android.bluetooth.device.extra.RSSI", (short) 0)));
                            arrayList.add(parseDevice2Map);
                        } else if (action.equals("android.bluetooth.adapter.action.DISCOVERY_STARTED")) {
                            MobLog.getInstance().d("started", new Object[0]);
                        } else if (action.equals("android.bluetooth.adapter.action.DISCOVERY_FINISHED")) {
                            MobLog.getInstance().d("done", new Object[0]);
                            BHelper.this.mScanning = false;
                            bScanCallback.onScan(arrayList);
                            newHandler.removeMessages(0);
                            BHelper.this.unRegisterBScanReceiver();
                        }
                    } catch (Throwable th) {
                        MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.bluetooth.device.action.FOUND");
            intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");
            intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
            ReflectHelper.invokeInstanceMethod(this.context, "registerReceiver", new Object[]{this.bScanReceiver, intentFilter}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
            this.bScanRegistered = true;
            this.mScanning = true;
            bAdapter.startDiscovery();
        } catch (Throwable th) {
            MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
            bScanCallback.onScan(new ArrayList<>());
        }
    }

    @SuppressLint({"MissingPermission"})
    public ArrayList<HashMap<String, Object>> getBondedDevice() {
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        try {
            if (DeviceHelper.getInstance(this.context).checkPermission("android.permission.BLUETOOTH")) {
                Set<BluetoothDevice> bondedDevices = getBAdapter().getBondedDevices();
                if (bondedDevices.size() > 0) {
                    for (BluetoothDevice bluetoothDevice : bondedDevices) {
                        HashMap<String, Object> parseDevice2Map = parseDevice2Map(bluetoothDevice);
                        parseDevice2Map.put("__currConnected", Integer.valueOf(isConnectedDevice(bluetoothDevice) ? 1 : 0));
                        arrayList.add(parseDevice2Map);
                    }
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
        }
        return arrayList;
    }

    public boolean isConnectedDevice(BluetoothDevice bluetoothDevice) {
        Boolean bool;
        if (bluetoothDevice == null) {
            return false;
        }
        try {
            if (!DeviceHelper.getInstance(this.context).checkPermission("android.permission.BLUETOOTH") || (bool = (Boolean) ReflectHelper.invokeInstanceMethod(bluetoothDevice, Strings.getString(115), new Object[0])) == null) {
                return false;
            }
            return bool.booleanValue();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return false;
        }
    }

    @SuppressLint({"MissingPermission"})
    public boolean isEnabled() {
        BluetoothAdapter bAdapter;
        try {
            if (!DeviceHelper.getInstance(this.context).checkPermission("android.permission.BLUETOOTH") || (bAdapter = getBAdapter()) == null) {
                return false;
            }
            return bAdapter.isEnabled();
        } catch (Throwable th) {
            MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
            return false;
        }
    }

    @SuppressLint({"MissingPermission"})
    public void open() {
        try {
            if (DeviceHelper.getInstance(this.context).checkPermission("android.permission.BLUETOOTH") && DeviceHelper.getInstance(this.context).checkPermission("android.permission.BLUETOOTH_ADMIN")) {
                getBAdapter().enable();
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
        }
    }

    public void registerBOperationReceiver(String str, BOperationCallback bOperationCallback) {
        if (bOperationCallback != null) {
            if (this.bOperationCallbackMap == null) {
                this.bOperationCallbackMap = new HashMap();
            }
            this.bOperationCallbackMap.put(str, bOperationCallback);
            if (this.bOperationReceiver == null) {
                try {
                    this.bOperationReceiver = new BroadcastReceiver() { // from class: com.mob.tools.utils.BHelper.1
                        @Override // android.content.BroadcastReceiver
                        public void onReceive(Context context, Intent intent) {
                            try {
                                String action = intent.getAction();
                                if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                                    int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 0);
                                    intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", 0);
                                    switch (intExtra) {
                                        case 10:
                                            if (BHelper.this.bOperationCallbackMap == null || BHelper.this.bOperationCallbackMap.isEmpty()) {
                                                return;
                                            }
                                            Iterator it = BHelper.this.bOperationCallbackMap.entrySet().iterator();
                                            while (it.hasNext()) {
                                                BOperationCallback bOperationCallback2 = (BOperationCallback) ((Map.Entry) it.next()).getValue();
                                                if (bOperationCallback2 != null) {
                                                    bOperationCallback2.onDisabled();
                                                }
                                            }
                                            return;
                                        case 11:
                                        case 13:
                                            return;
                                        case 12:
                                            if (BHelper.this.bOperationCallbackMap == null || BHelper.this.bOperationCallbackMap.isEmpty()) {
                                                return;
                                            }
                                            Iterator it2 = BHelper.this.bOperationCallbackMap.entrySet().iterator();
                                            while (it2.hasNext()) {
                                                BOperationCallback bOperationCallback3 = (BOperationCallback) ((Map.Entry) it2.next()).getValue();
                                                if (bOperationCallback3 != null) {
                                                    bOperationCallback3.onEnabled();
                                                }
                                            }
                                            return;
                                        default:
                                            return;
                                    }
                                }
                                if (action.equals("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED")) {
                                    int intExtra2 = intent.getIntExtra("android.bluetooth.adapter.extra.CONNECTION_STATE", 0);
                                    intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_CONNECTION_STATE", 0);
                                    HashMap<String, Object> parseDevice2Map = BHelper.this.parseDevice2Map((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE"));
                                    switch (intExtra2) {
                                        case 0:
                                            if (BHelper.this.bOperationCallbackMap == null || BHelper.this.bOperationCallbackMap.isEmpty()) {
                                                return;
                                            }
                                            Iterator it3 = BHelper.this.bOperationCallbackMap.entrySet().iterator();
                                            while (it3.hasNext()) {
                                                BOperationCallback bOperationCallback4 = (BOperationCallback) ((Map.Entry) it3.next()).getValue();
                                                if (bOperationCallback4 != null) {
                                                    bOperationCallback4.onConnectionChanged(false, parseDevice2Map);
                                                }
                                            }
                                            return;
                                        case 1:
                                        case 3:
                                            return;
                                        case 2:
                                            if (BHelper.this.bOperationCallbackMap == null || BHelper.this.bOperationCallbackMap.isEmpty()) {
                                                return;
                                            }
                                            Iterator it4 = BHelper.this.bOperationCallbackMap.entrySet().iterator();
                                            while (it4.hasNext()) {
                                                BOperationCallback bOperationCallback5 = (BOperationCallback) ((Map.Entry) it4.next()).getValue();
                                                if (bOperationCallback5 != null) {
                                                    bOperationCallback5.onConnectionChanged(true, parseDevice2Map);
                                                }
                                            }
                                            return;
                                        default:
                                            return;
                                    }
                                }
                                if (action.equals("android.bluetooth.device.action.ACL_CONNECTED")) {
                                    HashMap<String, Object> parseDevice2Map2 = BHelper.this.parseDevice2Map((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE"));
                                    if (BHelper.this.bOperationCallbackMap == null || BHelper.this.bOperationCallbackMap.isEmpty()) {
                                        return;
                                    }
                                    Iterator it5 = BHelper.this.bOperationCallbackMap.entrySet().iterator();
                                    while (it5.hasNext()) {
                                        BOperationCallback bOperationCallback6 = (BOperationCallback) ((Map.Entry) it5.next()).getValue();
                                        if (bOperationCallback6 != null) {
                                            bOperationCallback6.onDeviceConnected(parseDevice2Map2);
                                        }
                                    }
                                    return;
                                }
                                if (action.equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                                    HashMap<String, Object> parseDevice2Map3 = BHelper.this.parseDevice2Map((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE"));
                                    if (BHelper.this.bOperationCallbackMap == null || BHelper.this.bOperationCallbackMap.isEmpty()) {
                                        return;
                                    }
                                    Iterator it6 = BHelper.this.bOperationCallbackMap.entrySet().iterator();
                                    while (it6.hasNext()) {
                                        BOperationCallback bOperationCallback7 = (BOperationCallback) ((Map.Entry) it6.next()).getValue();
                                        if (bOperationCallback7 != null) {
                                            bOperationCallback7.onDeviceDisconnected(parseDevice2Map3);
                                        }
                                    }
                                }
                            } catch (Throwable th) {
                                MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
                            }
                        }
                    };
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
                    intentFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
                    intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
                    intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
                    ReflectHelper.invokeInstanceMethod(this.context, "registerReceiver", new Object[]{this.bOperationReceiver, intentFilter}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
                    this.bOperationRegistered = true;
                } catch (Throwable th) {
                    MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
                }
            }
        }
    }

    public void unRegisterBOperationReceiver(String str) {
        try {
            if (this.bOperationCallbackMap != null && !this.bOperationCallbackMap.containsKey(str)) {
                this.bOperationCallbackMap.remove(str);
            }
            if (this.bOperationCallbackMap.isEmpty() && this.bOperationReceiver != null && this.bOperationRegistered) {
                ReflectHelper.invokeInstanceMethod(this.context, "unregisterReceiver", new Object[]{this.bOperationReceiver}, new Class[]{BroadcastReceiver.class});
                this.bOperationRegistered = false;
                this.bOperationReceiver = null;
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
        }
    }

    public void unRegisterBScanReceiver() {
        try {
            if (this.bScanReceiver == null || !this.bScanRegistered) {
                return;
            }
            ReflectHelper.invokeInstanceMethod(this.context, "unregisterReceiver", new Object[]{this.bScanReceiver}, new Class[]{BroadcastReceiver.class});
            this.bScanRegistered = false;
            this.bScanReceiver = null;
        } catch (Throwable th) {
            MobLog.getInstance().d(th, th.getMessage() + "", new Object[0]);
        }
    }
}
