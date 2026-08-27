package ycble.runchinaup.core;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;
import ycble.runchinaup.core.callback.ScanListener;
import ycble.runchinaup.device.BleDevice;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes2.dex */
public class BleScanner {
    private static BleScanner bleScanner = new BleScanner();
    protected static boolean isShowScanLog = true;
    protected static Context mContext;
    private int scanRefreshTime = 1500;
    private ExecutorService cachedThreadPool = Executors.newScheduledThreadPool(10);
    private BluetoothAdapter adapter = null;
    private ScanCallback scanCallback = null;
    private boolean isScan = false;
    private HashSet<ScanListener> scanListenerHashSet = new HashSet<>();
    private BleDeviceFilter bleDeviceFilter = null;

    private BleScanner() {
        init();
    }

    public static Field getDeclaredField(Class<?> cls, String str) throws NoSuchFieldException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField;
    }

    public static Field getDeclaredField(Object obj, String str) throws NoSuchFieldException {
        Field declaredField = obj.getClass().getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField;
    }

    public static Method getDeclaredMethod(Class<?> cls, String str, Class<?>... clsArr) throws NoSuchMethodException {
        Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }

    public static Method getDeclaredMethod(Object obj, String str, Class<?>... clsArr) throws NoSuchMethodException {
        Method declaredMethod = obj.getClass().getDeclaredMethod(str, clsArr);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }

    @SuppressLint({"PrivateApi"})
    public static Object getIBluetoothGatt(Object obj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getDeclaredMethod(obj, "getBluetoothGatt", (Class<?>[]) new Class[0]).invoke(obj, new Object[0]);
    }

    @SuppressLint({"PrivateApi"})
    public static Object getIBluetoothManager(BluetoothAdapter bluetoothAdapter) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getDeclaredMethod((Class<?>) BluetoothAdapter.class, "getBluetoothManager", (Class<?>[]) new Class[0]).invoke(bluetoothAdapter, new Object[0]);
    }

    public static BleScanner getInstance() {
        return bleScanner;
    }

    private void init() {
        if (this.adapter == null) {
            this.adapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (this.scanCallback == null) {
            this.scanCallback = new ScanCallback() { // from class: ycble.runchinaup.core.BleScanner.1
                @Override // no.nordicsemi.android.support.v18.scanner.ScanCallback
                public void onBatchScanResults(@NonNull final List<ScanResult> list) {
                    super.onBatchScanResults(list);
                    ycBleLog.i("====onScanResult====>批量==>" + list.size());
                    BleScanner.this.cachedThreadPool.execute(new Runnable() { // from class: ycble.runchinaup.core.BleScanner.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            for (ScanResult scanResult : list) {
                                BleDevice parserFromScanData = BleDevice.parserFromScanData(scanResult.getDevice(), scanResult.getScanRecord().getBytes(), scanResult.getRssi());
                                if (BleScanner.isShowScanLog) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("====onScanResult====>");
                                    sb.append(parserFromScanData.toString());
                                    sb.append(BleScanner.this.bleDeviceFilter == null);
                                    ycBleLog.i(sb.toString());
                                }
                                if (BleScanner.this.bleDeviceFilter == null) {
                                    BleScanner.this.onScan(parserFromScanData);
                                } else if (BleScanner.this.bleDeviceFilter.filter(parserFromScanData)) {
                                    BleScanner.this.onScan(parserFromScanData);
                                }
                            }
                        }
                    });
                }

                @Override // no.nordicsemi.android.support.v18.scanner.ScanCallback
                public void onScanFailed(int i) {
                    super.onScanFailed(i);
                    ycBleLog.e("onScanFailed====>" + i);
                    BleScanner.this.onFailure(i);
                }

                @Override // no.nordicsemi.android.support.v18.scanner.ScanCallback
                public void onScanResult(int i, ScanResult scanResult) {
                    super.onScanResult(i, scanResult);
                    ycBleLog.e("====onScanResult====>单个==>" + scanResult.toString());
                }
            };
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void init(Context context) {
        mContext = context;
    }

    private void judgeScanOrStop() {
        try {
            if (!this.isScan) {
                BluetoothLeScannerCompat.getScanner().stopScan(this.scanCallback);
                return;
            }
            if (this.scanRefreshTime < 0) {
                this.scanRefreshTime = 1500;
            }
            BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            ScanSettings build = new ScanSettings.Builder().setLegacy(false).setScanMode(2).setReportDelay(this.scanRefreshTime).setUseHardwareBatchingIfSupported(false).build();
            ArrayList arrayList = new ArrayList();
            arrayList.add(new ScanFilter.Builder().build());
            scanner.startScan(arrayList, build, this.scanCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFailure(int i) {
        Iterator<ScanListener> it = this.scanListenerHashSet.iterator();
        while (it.hasNext()) {
            it.next().onFailure(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onScan(BleDevice bleDevice) {
        Iterator<ScanListener> it = this.scanListenerHashSet.iterator();
        while (it.hasNext()) {
            it.next().onScan(bleDevice);
        }
    }

    public static boolean releaseAllScanClient() {
        Object iBluetoothGatt;
        Method declaredMethod;
        boolean z;
        try {
            Object iBluetoothManager = getIBluetoothManager(BluetoothAdapter.getDefaultAdapter());
            if (iBluetoothManager == null || (iBluetoothGatt = getIBluetoothGatt(iBluetoothManager)) == null) {
                return false;
            }
            Method declaredMethod2 = getDeclaredMethod(iBluetoothGatt, "unregisterClient", (Class<?>[]) new Class[]{Integer.TYPE});
            try {
                declaredMethod = getDeclaredMethod(iBluetoothGatt, "stopScan", (Class<?>[]) new Class[]{Integer.TYPE, Boolean.TYPE});
                z = false;
            } catch (Exception unused) {
                declaredMethod = getDeclaredMethod(iBluetoothGatt, "stopScan", (Class<?>[]) new Class[]{Integer.TYPE});
                z = true;
            }
            for (int i = 0; i <= 40; i++) {
                if (!z) {
                    try {
                        declaredMethod.invoke(iBluetoothGatt, Integer.valueOf(i), false);
                    } catch (Exception unused2) {
                    }
                }
                if (z) {
                    try {
                        declaredMethod.invoke(iBluetoothGatt, Integer.valueOf(i));
                    } catch (Exception unused3) {
                    }
                }
                try {
                    declaredMethod2.invoke(iBluetoothGatt, Integer.valueOf(i));
                } catch (Exception unused4) {
                }
            }
            declaredMethod.setAccessible(false);
            declaredMethod2.setAccessible(false);
            getDeclaredMethod(iBluetoothGatt, "unregAll", (Class<?>[]) new Class[0]).invoke(iBluetoothGatt, new Object[0]);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeSysBLE() {
        if (this.adapter == null) {
            this.adapter = BluetoothAdapter.getDefaultAdapter();
        }
        this.adapter.disable();
    }

    public int getScanRefreshTime() {
        return this.scanRefreshTime;
    }

    public boolean isEnabled() {
        return this.adapter.isEnabled();
    }

    public boolean isScan() {
        return this.isScan;
    }

    public void openSysBLE() {
        if (this.adapter == null) {
            this.adapter = BluetoothAdapter.getDefaultAdapter();
        }
        this.adapter.enable();
    }

    public void registerScanListener(ScanListener scanListener) {
        if (this.scanListenerHashSet.contains(scanListener)) {
            return;
        }
        this.scanListenerHashSet.add(scanListener);
    }

    public void setBleDeviceFilter(BleDeviceFilter bleDeviceFilter) {
        this.bleDeviceFilter = bleDeviceFilter;
    }

    public void setScanRefreshTime(int i) {
        this.scanRefreshTime = i;
    }

    public void startScan() {
        init();
        if (!isEnabled()) {
            ycBleLog.e("nopointer/npBle->debug:蓝牙没有打开，请先打开手机蓝牙，再进行扫描");
            return;
        }
        ycBleLog.e("要求开始扫描设备,当前扫描状态:" + this.isScan);
        if (this.isScan) {
            return;
        }
        this.isScan = true;
        judgeScanOrStop();
    }

    public void stopScan() {
        init();
        if (!isEnabled()) {
            ycBleLog.w("nopointer/npBle->debug: 蓝牙没有打开--");
        } else if (this.isScan) {
            this.isScan = false;
            judgeScanOrStop();
        }
    }

    public void unRegisterScanListener(ScanListener scanListener) {
        if (this.scanListenerHashSet.contains(scanListener)) {
            this.scanListenerHashSet.remove(scanListener);
        }
    }
}
