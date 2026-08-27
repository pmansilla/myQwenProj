package ycble.runchinaup.core;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.graphics.drawable.PathInterpolatorCompat;
import android.text.TextUtils;
import com.autonavi.amap.mapcore.tools.GLMapStaticValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import me.panpf.sketch.uri.FileUriModel;
import ycble.runchinaup.core.AbsBleConnManger;
import ycble.runchinaup.core.callback.BleConnCallback;
import ycble.runchinaup.exception.BleUUIDNullException;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes2.dex */
public abstract class AbsBleManager {
    static final int MSG_AFTER_SCAN_TIMEOUT = 3;
    static final int MSG_TASK_SUCCESS = 1;
    private static int bleTaskSize = 0;
    private static int bleUnitTaskIndex = -1;
    private static boolean isTaskFinish = false;
    private static Context mContext = null;
    private static final String strMacRule = "^[A-F0-9]{2}(:[A-F0-9]{2}){5}$";
    public BleScanner myBleScaner = BleScanner.getInstance();
    private AbsBleConnManger absBleConnManger = null;
    protected BleStateReceiver bleStateReceiver = new BleStateReceiver();
    protected int cfgResendCount = 3;
    protected int cfgTimeOutSinglePkgMilli = GLMapStaticValue.ANIMATION_MOVE_TIME;
    protected int cfgTimeOutMultiPkgMilli = PathInterpolatorCompat.MAX_NUM_POINTS;
    private List<BleUnitTask> bleUnitTaskList = new ArrayList();
    private HashSet<BleConnCallback> bleBleConnCallbackHashSet = new HashSet<>();
    private boolean isConnectIng = false;
    private boolean isOTAMode = false;
    protected BleConnState bleConnState = null;
    protected boolean isConn = false;
    private String connMac = null;
    private AbsBleConnManger.AbsBleConnCallback absBleConnCallback = new AbsBleConnManger.AbsBleConnCallback() { // from class: ycble.runchinaup.core.AbsBleManager.2
        @Override // ycble.runchinaup.core.AbsBleConnManger.AbsBleConnCallback
        protected void connResult(BleConnState bleConnState) {
            AbsBleManager.this.isConnectIng = false;
            AbsBleManager.this.isConn = bleConnState == BleConnState.CONNECTED;
            ycBleLog.e("连接结果==>connResult==>" + bleConnState + "=isConn=>" + AbsBleManager.this.isConn);
            if (bleConnState == BleConnState.CONNECTED) {
                AbsBleManager.this.withOnConnectSuccess();
                AbsBleManager.this.onConnectSuccess();
                return;
            }
            if (bleConnState == BleConnState.PHONEBLEANR) {
                AbsBleManager.this.onPhoneBleStateException();
                return;
            }
            if (bleConnState == BleConnState.CONNEXCEPTION) {
                AbsBleManager.this.withOnConnException();
                AbsBleManager.this.onConnException();
            } else if (bleConnState == BleConnState.HANDDISCONN) {
                AbsBleManager.this.withOnHandDisConn();
                AbsBleManager.this.onHandDisConn();
            }
        }

        @Override // ycble.runchinaup.core.AbsBleConnManger.AbsBleConnCallback
        protected void onDataChange(UUID uuid, byte[] bArr) {
            super.onDataChange(uuid, bArr);
            AbsBleManager.this.onDataReceive(bArr, uuid);
        }

        @Override // ycble.runchinaup.core.AbsBleConnManger.AbsBleConnCallback
        protected void onDataRead(boolean z, byte[] bArr, UUID... uuidArr) {
            super.onDataRead(z, bArr, uuidArr);
            AbsBleManager.this.onDataRead(bArr, z, uuidArr);
        }

        @Override // ycble.runchinaup.core.AbsBleConnManger.AbsBleConnCallback
        protected void onDataWrite(boolean z, byte[] bArr, UUID... uuidArr) {
            super.onDataWrite(z, bArr, uuidArr);
            AbsBleManager.this.onDataWrite(bArr, z, uuidArr);
        }

        @Override // ycble.runchinaup.core.AbsBleConnManger.AbsBleConnCallback
        protected void onException(ErrCode errCode, String str, UUID... uuidArr) {
            super.onException(errCode, str, uuidArr);
            AbsBleManager.this.onException(errCode, str, uuidArr);
        }

        @Override // ycble.runchinaup.core.AbsBleConnManger.AbsBleConnCallback
        protected void onLoadCharas(BluetoothGatt bluetoothGatt) {
            AbsBleManager.this.clearSomeFlag();
            AbsBleManager.this.loadCfg();
            AbsBleManager.this.handWithCfg();
        }

        @Override // ycble.runchinaup.core.AbsBleConnManger.AbsBleConnCallback
        protected void onRssi(int i) {
            super.onRssi(i);
            AbsBleManager.this.onRssi(i);
        }
    };
    private Handler timeOutHandler = new Handler();
    private HashMap<String, TimeOutHelper> timeOutHelperHashMap = new HashMap<>();
    private final Handler handler = new Handler();

    /* loaded from: classes2.dex */
    public class BleStateReceiver extends BroadcastReceiver {
        private boolean hasCover;
        private String listenerMac;

        private BleStateReceiver() {
            this.listenerMac = null;
            this.hasCover = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IntentFilter createSateFilter() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
            intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
            return intentFilter;
        }

        public boolean isHasCover() {
            return this.hasCover;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ycBleLog.e("BleStateReceiver 广播的action:===>" + action);
            if (!action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (bluetoothDevice == null) {
                    ycBleLog.e("设备为空，不需要往后执行......");
                    return;
                }
                ycBleLog.e("相关的设备===>" + bluetoothDevice.getName() + FileUriModel.SCHEME + bluetoothDevice.getAddress());
                return;
            }
            switch (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE)) {
                case 10:
                    ycBleLog.e("手机蓝牙关闭状态");
                    AbsBleManager.this.onBleClose();
                    return;
                case 11:
                    ycBleLog.e("蓝牙正在打开......");
                    return;
                case 12:
                    ycBleLog.e("手机蓝牙开启状态");
                    AbsBleManager.this.onBleOpen();
                    return;
                case 13:
                    ycBleLog.e("蓝牙正在关闭......");
                    return;
                default:
                    return;
            }
        }

        public void setHasCover(boolean z) {
            this.hasCover = z;
        }

        public void startListen(Context context, String str) {
            ycBleLog.e("监听此设备相关的蓝牙广播==>" + str);
            this.listenerMac = str;
            if (context != null) {
                try {
                    context.registerReceiver(this, createSateFilter());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void stopListen(Context context) {
            if (context != null) {
                try {
                    context.unregisterReceiver(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void clearSomeFlag() {
        this.bleUnitTaskList.clear();
        bleUnitTaskIndex = -1;
        bleTaskSize = 0;
        this.handler.removeCallbacksAndMessages(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearTimeOutHandler(boolean z) {
        if (z) {
            ycBleLog.e("没有收到响应指令,说明已经超时了==>");
        } else {
            ycBleLog.e("收到了数据，移除超时延时处理==>");
        }
        this.timeOutHandler.removeCallbacksAndMessages(null);
    }

    private synchronized void collectData(boolean z, final BleUnitTask bleUnitTask) {
        final byte[] data = bleUnitTask.getData();
        ycBleLog.e("debug===准备超时的数据>" + BleUtil.byte2HexStr(data) + "===>isMultiPgResponse:" + z);
        String byte2HexStr = BleUtil.byte2HexStr(data);
        if (!this.timeOutHelperHashMap.containsKey(byte2HexStr)) {
            this.timeOutHelperHashMap.put(byte2HexStr, new TimeOutHelper(byte2HexStr, z ? this.cfgTimeOutMultiPkgMilli : this.cfgTimeOutSinglePkgMilli));
        }
        TimeOutHelper timeOutHelper = this.timeOutHelperHashMap.get(byte2HexStr);
        timeOutHelper.reSendCount++;
        if (timeOutHelper.reSendCount < this.cfgResendCount) {
            this.timeOutHandler.postDelayed(new Runnable() { // from class: ycble.runchinaup.core.AbsBleManager.3
                @Override // java.lang.Runnable
                public void run() {
                    ycBleLog.e("debug====" + BleUtil.byte2HexStr(data) + "指令失败，重新下发");
                    try {
                        if (bleUnitTask.getOptionType() == 4 ? AbsBleManager.this.enableNotity(bleUnitTask.getU_service(), bleUnitTask.getU_chara()) : bleUnitTask.getOptionType() == 2 ? AbsBleManager.this.writeData(bleUnitTask.getU_service(), bleUnitTask.getU_chara(), data) : bleUnitTask.getOptionType() == 3 ? AbsBleManager.this.writeDataWithoutResp(bleUnitTask.getU_service(), bleUnitTask.getU_chara(), data) : true) {
                            return;
                        }
                        AbsBleManager.this.clearTimeOutHandler(true);
                        AbsBleManager.this.onResponseTimeOut(data);
                        if (AbsBleManager.isTaskFinish) {
                            return;
                        }
                        int unused = AbsBleManager.bleTaskSize = AbsBleManager.this.bleUnitTaskList.size();
                        if (AbsBleManager.bleUnitTaskIndex < AbsBleManager.bleTaskSize) {
                            AbsBleManager.this.toNextTask();
                        }
                    } catch (BleUUIDNullException e) {
                        e.printStackTrace();
                    }
                }
            }, timeOutHelper.getMilliSecond());
        } else {
            this.timeOutHandler.postDelayed(new Runnable() { // from class: ycble.runchinaup.core.AbsBleManager.4
                @Override // java.lang.Runnable
                public void run() {
                    AbsBleManager.this.timeOutHelperHashMap.clear();
                    AbsBleManager.this.clearTimeOutHandler(true);
                    AbsBleManager.this.onResponseTimeOut(data);
                    if (AbsBleManager.isTaskFinish) {
                        return;
                    }
                    int unused = AbsBleManager.bleTaskSize = AbsBleManager.this.bleUnitTaskList.size();
                    if (AbsBleManager.bleUnitTaskIndex < AbsBleManager.bleTaskSize) {
                        AbsBleManager.this.toNextTask();
                    }
                }
            }, timeOutHelper.getMilliSecond());
        }
    }

    private void connWithSysConn(BluetoothDevice bluetoothDevice) {
        if (this.absBleConnManger != null) {
            this.absBleConnManger.connect(bluetoothDevice);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final synchronized void handWithCfg() {
        bleUnitTaskIndex = -1;
        isTaskFinish = false;
        bleTaskSize = this.bleUnitTaskList.size();
        if (bleUnitTaskIndex < bleTaskSize) {
            toNextTask();
        } else {
            setTaskFinish();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void initSDK(Context context) {
        mContext = context;
    }

    private final void insertBeforeWrite(BleUnitTask bleUnitTask) {
        collectData(specialCommand(bleUnitTask), bleUnitTask);
    }

    private void privateConnnect(String str) {
        if (this.isConnectIng) {
            ycBleLog.e("ble-当前已经发出了连接请求，还没响应，不需要再发送这次请求");
            return;
        }
        withBleConnState(BleConnState.CONNECTING);
        this.absBleConnManger.connect(str);
        this.isConnectIng = true;
    }

    private final void setTaskFinish() {
        if (isTaskFinish) {
            return;
        }
        clearSomeFlag();
        ycBleLog.e("连接后的时序指令下发完成，可以自由交互数据了");
        onFinishTaskAfterConn();
        isTaskFinish = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void toNextTask() {
        if (this.absBleConnManger == null) {
            return;
        }
        bleTaskSize = this.bleUnitTaskList.size();
        bleUnitTaskIndex++;
        if (bleUnitTaskIndex < bleTaskSize) {
            ycBleLog.e("ble===task====>" + (bleUnitTaskIndex + 1) + FileUriModel.SCHEME + bleTaskSize);
            if (bleUnitTaskIndex < 0) {
                ycBleLog.e("bleUnitTaskIndex 异常====>" + bleUnitTaskIndex);
                return;
            }
            BleUnitTask bleUnitTask = this.bleUnitTaskList.get(bleUnitTaskIndex);
            if (bleUnitTask == null) {
                ycBleLog.e("unitTask 异常====>" + bleUnitTaskIndex);
                return;
            }
            try {
                switch (bleUnitTask.getOptionType()) {
                    case 1:
                        ycBleLog.d("nopointer/npBle->debug:读数据<<<< " + bleUnitTask.msg);
                        readData(bleUnitTask.getU_service(), bleUnitTask.getU_chara());
                        break;
                    case 2:
                        ycBleLog.d("nopointer/npBle->debug:写数据<<<< " + bleUnitTask.msg);
                        writeData(bleUnitTask.getU_service(), bleUnitTask.getU_chara(), bleUnitTask.getData());
                        break;
                    case 3:
                        ycBleLog.d("nopointer/npBle->debug:无响应写数据<<<< " + bleUnitTask.msg);
                        writeDataWithoutResp(bleUnitTask.getU_service(), bleUnitTask.getU_chara(), bleUnitTask.getData());
                        break;
                    case 4:
                        ycBleLog.d("nopointer/npBle->debug:打开通知<<<< " + bleUnitTask.msg);
                        enableNotity(bleUnitTask.getU_service(), bleUnitTask.getU_chara());
                        break;
                    case 5:
                        ycBleLog.d("nopointer/npBle->debug:打开指示<<<< " + bleUnitTask.msg);
                        bleUnitTask.setData(new byte[]{2, 0});
                        enableIndication(bleUnitTask.getU_service(), bleUnitTask.getU_chara());
                        break;
                    case 6:
                        ycBleLog.d("nopointer/npBle->debug: 关闭通知或者指示<<<< " + bleUnitTask.msg);
                        disAbleNotityOrIndication(bleUnitTask.getU_service(), bleUnitTask.getU_chara());
                        break;
                }
            } catch (BleUUIDNullException e) {
                e.printStackTrace();
                ycBleLog.e("debug===如果没有找到设备的相关通道,继续执行，不能中断在这里");
                toNextTask();
            }
        } else {
            setTaskFinish();
        }
    }

    private boolean verifyConnBefore(String str) {
        if (!isBLeEnabled()) {
            ycBleLog.e("蓝牙没有打开呢！");
            return false;
        }
        if (this.isConn) {
            ycBleLog.e("已经是连接的，，不需要花里胡哨的了");
            return false;
        }
        if (TextUtils.isEmpty(str) || !str.matches(strMacRule)) {
            ycBleLog.e("mac地址都不对,地址要注意大写,且不能为空！！！！！");
            return false;
        }
        if (this.isConnectIng) {
            ycBleLog.e("ble-当前已经发出了连接请求，还没响应，不需要再发送这次请求");
            withBleConnState(BleConnState.CONNECTING);
            return false;
        }
        if (!TextUtils.isEmpty(this.connMac) && !str.equals(this.connMac)) {
            ycBleLog.e("连接新的设备之前，需先调用断开指令");
            return false;
        }
        if (!this.isOTAMode) {
            return true;
        }
        ycBleLog.e("醒醒吧 现在是在OTA模式下");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addBleUnitTask(BleUnitTask bleUnitTask) {
        if (this.bleUnitTaskList == null) {
            this.bleUnitTaskList = new ArrayList();
        }
        this.bleUnitTaskList.add(bleUnitTask);
    }

    public void connDevice(String str) {
        if (verifyConnBefore(str)) {
            this.connMac = str;
            ycBleLog.reCreateLogFile(str);
            BluetoothDevice isInConnList = AbsBleConnManger.isInConnList(str, mContext);
            ycBleLog.e("debug===先判断 当前蓝牙设备是不是在其他的app中连接了");
            if (isInConnList == null) {
                privateConnnect(str);
            } else {
                ycBleLog.e("debug===还真被其他应用连接了,那就简单了,直接去拿连接过来就是了");
                connWithSysConn(isInConnList);
            }
        }
    }

    public final boolean disAbleNotityOrIndication(UUID uuid, UUID uuid2) throws BleUUIDNullException {
        if (this.absBleConnManger == null) {
            return false;
        }
        insertBeforeWrite(BleUnitTask.createDisEnableNotifyOrIndicate(uuid, uuid2, "使不能通知/指示"));
        return this.absBleConnManger.disAbleNotityOrIndication(uuid, uuid2);
    }

    public void disConn() {
        clearSomeFlag();
        this.connMac = null;
        this.isConnectIng = false;
        if (this.absBleConnManger != null) {
            this.absBleConnManger.disConnect();
        } else {
            ycBleLog.e("==absBleConnManger 为 null");
        }
    }

    protected final boolean enableIndication(UUID uuid, UUID uuid2) throws BleUUIDNullException {
        if (this.absBleConnManger == null) {
            return false;
        }
        insertBeforeWrite(BleUnitTask.createEnableIndicate(uuid, uuid2, "使能指示"));
        return this.absBleConnManger.enableIndication(uuid, uuid2);
    }

    protected final boolean enableNotity(UUID uuid, UUID uuid2) throws BleUUIDNullException {
        if (this.absBleConnManger == null) {
            return false;
        }
        insertBeforeWrite(BleUnitTask.createEnableNotify(uuid, uuid2, "使能通知"));
        return this.absBleConnManger.enableNotity(uuid, uuid2);
    }

    public BleConnState getBleConnState() {
        return this.bleConnState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void init() {
        init(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void init(UUID... uuidArr) {
        this.absBleConnManger = new AbsBleConnManger(mContext);
        if (uuidArr != null) {
            for (UUID uuid : uuidArr) {
                this.absBleConnManger.addMustUUID(uuid);
            }
        }
        this.absBleConnManger.setAbsBleConnAndStateCallback(this.absBleConnCallback);
        mContext.registerReceiver(this.bleStateReceiver, this.bleStateReceiver.createSateFilter());
    }

    public boolean isBLeEnabled() {
        return this.myBleScaner.isEnabled();
    }

    public final boolean isConn() {
        return this.isConn;
    }

    public boolean isOTAMode() {
        return this.isOTAMode;
    }

    public abstract void loadCfg();

    public void onBleClose() {
        this.isConnectIng = false;
        BleScanner.getInstance().stopScan();
        if (this.absBleConnManger != null) {
            this.absBleConnManger.setHadScanDeviceFlag(false);
        }
    }

    public void onBleOpen() {
        if (this.absBleConnManger != null) {
            this.absBleConnManger.setHadScanDeviceFlag(true);
        }
    }

    public abstract void onConnException();

    public abstract void onConnectSuccess();

    public void onDataRead(byte[] bArr, boolean z, UUID... uuidArr) {
    }

    public abstract void onDataReceive(byte[] bArr, UUID uuid);

    public void onDataWrite(byte[] bArr, boolean z, UUID... uuidArr) {
    }

    protected void onException(ErrCode errCode, String str, UUID... uuidArr) {
    }

    public abstract void onFinishTaskAfterConn();

    public abstract void onHandDisConn();

    public void onPhoneBleStateException() {
        clearSomeFlag();
    }

    public void onResponseTimeOut(byte[] bArr) {
    }

    public void onRssi(int i) {
    }

    public final boolean readData(UUID uuid, UUID uuid2) throws BleUUIDNullException {
        if (this.absBleConnManger != null) {
            return this.absBleConnManger.readData(uuid, uuid2);
        }
        return false;
    }

    public final boolean readData(UUID uuid, UUID uuid2, UUID uuid3) throws BleUUIDNullException {
        if (this.absBleConnManger != null) {
            return this.absBleConnManger.readData(uuid, uuid2, uuid3);
        }
        return false;
    }

    public void registerConnCallback(BleConnCallback bleConnCallback) {
        if (this.bleBleConnCallbackHashSet.contains(bleConnCallback)) {
            return;
        }
        this.bleBleConnCallbackHashSet.add(bleConnCallback);
    }

    protected void requestConnectionPriority(int i) {
        if (this.absBleConnManger != null) {
            this.absBleConnManger.requestConnectionPriority(i);
        }
    }

    public void setBleConnState(BleConnState bleConnState) {
        this.bleConnState = bleConnState;
    }

    public void setOTAMode(boolean z) {
        this.isOTAMode = z;
    }

    protected boolean specialCommand(BleUnitTask bleUnitTask) {
        return false;
    }

    protected synchronized void taskSuccess() {
        taskSuccess(0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void taskSuccess(int i) {
        clearTimeOutHandler(false);
        if (isConn()) {
            this.handler.postDelayed(new Runnable() { // from class: ycble.runchinaup.core.AbsBleManager.1
                @Override // java.lang.Runnable
                public void run() {
                    AbsBleManager.this.toNextTask();
                }
            }, i);
        }
    }

    public void unRegisterConnCallback(BleConnCallback bleConnCallback) {
        if (this.bleBleConnCallbackHashSet.contains(bleConnCallback)) {
            this.bleBleConnCallbackHashSet.remove(bleConnCallback);
        }
    }

    public final void withBleConnState(BleConnState bleConnState) {
        this.bleConnState = bleConnState;
        Iterator<BleConnCallback> it = this.bleBleConnCallbackHashSet.iterator();
        while (it.hasNext()) {
            it.next().onConnState(bleConnState);
        }
    }

    protected void withOnConnException() {
        clearSomeFlag();
        withBleConnState(BleConnState.CONNEXCEPTION);
    }

    protected final void withOnConnectSuccess() {
        clearSomeFlag();
        withBleConnState(BleConnState.CONNECTED);
    }

    public void withOnHandDisConn() {
        clearSomeFlag();
        withBleConnState(BleConnState.HANDDISCONN);
    }

    public final boolean writeData(UUID uuid, UUID uuid2, UUID uuid3, byte[] bArr) throws BleUUIDNullException {
        if (this.absBleConnManger == null) {
            return false;
        }
        insertBeforeWrite(BleUnitTask.createWrite(uuid, uuid2, bArr, "写数据"));
        return this.absBleConnManger.writeData(uuid, uuid2, uuid3, bArr);
    }

    public final boolean writeData(UUID uuid, UUID uuid2, byte[] bArr) throws BleUUIDNullException {
        if (this.absBleConnManger == null) {
            return false;
        }
        insertBeforeWrite(BleUnitTask.createWrite(uuid, uuid2, bArr, "写数据"));
        return this.absBleConnManger.writeData(uuid, uuid2, bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean writeDataWithoutResp(UUID uuid, UUID uuid2, byte[] bArr) throws BleUUIDNullException {
        if (this.absBleConnManger == null) {
            return false;
        }
        insertBeforeWrite(BleUnitTask.createWriteWithOutResp(uuid, uuid2, bArr, "无响应写数据"));
        return this.absBleConnManger.writeDataWithOutResponse(uuid, uuid2, bArr);
    }
}
