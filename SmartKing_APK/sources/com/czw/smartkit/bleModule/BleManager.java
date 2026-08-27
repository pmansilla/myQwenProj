package com.czw.smartkit.bleModule;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import basecamera.module.cfg.BaseCameraCfg;
import com.alibaba.fastjson.asm.Opcodes;
import com.czw.smartkit.MainApplication;
import com.czw.smartkit.bleModule.DevFunction.DevFunctionHelper;
import com.czw.smartkit.bleModule.battery.BatteryHelper;
import com.czw.smartkit.bleModule.data.DevFunctionEntity;
import com.czw.smartkit.bleModule.data.UnitCfg;
import com.czw.smartkit.bleModule.data.util.DevDataUtil;
import com.czw.smartkit.bleModule.extra.PhoneUtils;
import com.czw.smartkit.bleModule.gpsLocation.GpsLocationHelper;
import com.czw.smartkit.bleModule.measure.DevMeasureUtil;
import com.czw.smartkit.bleModule.sleep.DevSleepUtil;
import com.czw.smartkit.bleModule.step.DevStepUtil;
import com.czw.smartkit.homeModule.fragment.FragmentStep;
import com.czw.smartkit.homeModule.syncData.SyncDataUtil;
import com.czw.smartkit.preferenceModule.SharePreferenceDevice;
import com.czw.smartkit.preferenceModule.SharePreferenceRemind;
import com.czw.smartkit.preferenceModule.SharePreferenceUnit;
import com.czw.smartkit.preferenceModule.SharedPrefereceWeather;
import com.tencent.bugly.BuglyStrategy;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.panpf.sketch.uri.FileUriModel;
import ycble.runchinaup.core.AbsBleManager;
import ycble.runchinaup.core.BleUnitTask;
import ycble.runchinaup.device.BleDevice;
import ycble.runchinaup.exception.BleUUIDNullException;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes.dex */
public class BleManager extends AbsBleManager implements BleCfg {
    private static final int TYPE_MEASURE = 3;
    private static final int TYPE_SLEEP = 2;
    private static final int TYPE_STEP = 1;
    private static BleManager bleManager = new BleManager();
    private static final int intMaxLostCount = 1;
    private LostPlayUtil lostPlayUtil;
    private ExecutorService cachedThreadPool = Executors.newFixedThreadPool(20);
    private boolean isGetBattery = false;
    private boolean isAfterConnFirstTask = false;
    private boolean boolIsSyncData = false;
    private int intLostCount = 0;
    private Handler handler = new Handler();
    private boolean isSyncHistoryData = false;
    private boolean isMuliteDataWrite = false;
    private String versionStr = "";
    private int dataType = 1;
    private int syncIndex = -1;
    private int needQueryDayCount = 0;
    private List<byte[]> listMultiPckData = new ArrayList();
    private int intMultiPckDataIndex = -1;
    private boolean boolIsMultiWriteDataIng = false;
    private Handler handlerMulti = new Handler();
    private boolean isDebug = true;
    private Handler handlerPlayFindPhone = new Handler();
    private HashSet<SyncCallback> syncCallbackHashSet = new HashSet<>();

    private BleManager() {
        this.lostPlayUtil = null;
        init();
        ycBleLog.initLogDirName("smartking");
        this.cfgTimeOutSinglePkgMilli = 3200;
        this.cfgTimeOutMultiPkgMilli = BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH;
        this.cfgResendCount = 2;
        LostPlayUtil.init(MainApplication.getApp());
        this.lostPlayUtil = LostPlayUtil.getLostPlayUtil();
    }

    private boolean boolCanRWN() {
        if (this.isConn && !this.isAfterConnFirstTask && !this.boolIsSyncData) {
            return true;
        }
        if (!this.isConn) {
            ycBleLog.e("没有连接");
            return false;
        }
        if (!this.isAfterConnFirstTask) {
            return false;
        }
        ycBleLog.e("在同步任务时序");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void clearMultiFlag() {
        this.intMultiPckDataIndex = -1;
        this.listMultiPckData.clear();
        this.boolIsMultiWriteDataIng = false;
    }

    public static BleManager getBleManager() {
        return bleManager;
    }

    private void handFindMusic(boolean z) {
        if (!z) {
            this.lostPlayUtil.stop();
        } else {
            this.lostPlayUtil.play();
            this.handlerPlayFindPhone.postDelayed(new Runnable() { // from class: com.czw.smartkit.bleModule.BleManager.6
                @Override // java.lang.Runnable
                public void run() {
                    BleManager.this.lostPlayUtil.stop();
                }
            }, 15000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handWithTaskSuccess() {
        taskSuccess(50);
        this.isMuliteDataWrite = false;
    }

    private final void handWithWriteDataFlag(byte[] bArr) {
        int i = bArr[0] & 255;
        if (i == 115) {
            handWithTaskSuccess();
            nextPckDataForMultiData();
        } else {
            if (i == 17) {
                handWithTaskSuccess();
                return;
            }
            if (i == 29) {
                handWithTaskSuccess();
            } else if (i == 7) {
                handWithTaskSuccess();
                nextPckDataForMultiData();
            }
        }
    }

    private synchronized void nextPckDataForMultiData() {
        this.intMultiPckDataIndex++;
        if (this.intMultiPckDataIndex < this.listMultiPckData.size()) {
            writeData(this.listMultiPckData.get(this.intMultiPckDataIndex));
        } else {
            this.handlerMulti.removeCallbacksAndMessages(null);
            clearMultiFlag();
        }
    }

    private void onHandData(final byte[] bArr) {
        ycBleLog.e("===>>>" + BleUtil.byte2HexStr(bArr));
        switch (bArr[0] & 255) {
            case 6:
            case 7:
                ycBleLog.e("请求gps地址");
                GpsLocationHelper.getInstance().startLocation(true);
                break;
            case 83:
                handFindMusic(BleUtil.byte2IntLR(bArr[1]) == 1);
                handWithTaskSuccess();
                break;
            case 129:
            case 130:
            case Opcodes.RETURN /* 177 */:
            case 209:
            case 210:
                handWithTaskSuccess();
                break;
            case 131:
                DevFunctionHelper.getInstance().notifyDeviceFunction(DevDataUtil.getDevFunctionList(bArr));
                handWithTaskSuccess();
                break;
            case 133:
                ycBleLog.e("天气同步成功，保存本地一次");
                if (FragmentStep.currentWeather != null) {
                    SharedPrefereceWeather.save(FragmentStep.currentWeather);
                }
                handWithTaskSuccess();
                break;
            case 147:
                this.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.bleModule.BleManager.3
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            try {
                                DevStepUtil.gteInstance().receiveHistoryData(bArr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                if ((bArr[4] & 255) != 255) {
                                    return;
                                }
                                BleManager.this.handWithTaskSuccess();
                                if (!BleManager.this.isSyncHistoryData) {
                                    return;
                                }
                            }
                            if ((bArr[4] & 255) == 255) {
                                BleManager.this.handWithTaskSuccess();
                                if (!BleManager.this.isSyncHistoryData) {
                                    return;
                                }
                                BleManager.this.syncDataWithFunction();
                            }
                        } catch (Throwable th) {
                            if ((bArr[4] & 255) == 255) {
                                BleManager.this.handWithTaskSuccess();
                                if (BleManager.this.isSyncHistoryData) {
                                    BleManager.this.syncDataWithFunction();
                                }
                            }
                            throw th;
                        }
                    }
                });
                break;
            case Opcodes.LCMP /* 148 */:
                BatteryHelper.getInstance().notifyBattery(BleUtil.byte2IntLR(bArr[1]));
                if (this.isGetBattery) {
                    handWithTaskSuccess();
                    break;
                }
                break;
            case Opcodes.FCMPL /* 149 */:
                this.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.bleModule.BleManager.4
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            try {
                                DevSleepUtil.getInstance().receiveHistoryData(bArr, BleManager.this.syncIndex - BleManager.this.needQueryDayCount);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                if (BleUtil.byte2IntLR(bArr[1], bArr[2]) != 65535) {
                                    return;
                                }
                                BleManager.this.handWithTaskSuccess();
                                if (!BleManager.this.isSyncHistoryData) {
                                    return;
                                }
                            }
                            if (BleUtil.byte2IntLR(bArr[1], bArr[2]) == 65535) {
                                BleManager.this.handWithTaskSuccess();
                                if (!BleManager.this.isSyncHistoryData) {
                                    return;
                                }
                                BleManager.this.syncDataWithFunction();
                            }
                        } catch (Throwable th) {
                            if (BleUtil.byte2IntLR(bArr[1], bArr[2]) == 65535) {
                                BleManager.this.handWithTaskSuccess();
                                if (BleManager.this.isSyncHistoryData) {
                                    BleManager.this.syncDataWithFunction();
                                }
                            }
                            throw th;
                        }
                    }
                });
                break;
            case 150:
                this.cachedThreadPool.execute(new Runnable() { // from class: com.czw.smartkit.bleModule.BleManager.5
                    @Override // java.lang.Runnable
                    public void run() {
                        DevMeasureUtil.getInstance().receiveHistoryData(bArr);
                        if (BleUtil.byte2IntLR(bArr[1], bArr[2]) == 65535) {
                            BleManager.this.handWithTaskSuccess();
                            if (BleManager.this.isSyncHistoryData) {
                                BleManager.this.syncDataWithFunction();
                            }
                        }
                    }
                });
                break;
            case Opcodes.DCMPL /* 151 */:
                DevMeasureUtil.getInstance().getDeviceUiMeasureValue(bArr);
                handWithTaskSuccess();
                break;
            case Opcodes.IF_ICMPEQ /* 159 */:
                this.versionStr = String.format("%d.%d", Integer.valueOf(bArr[12] & 255), Integer.valueOf(bArr[13] & 255));
                ycBleLog.e("固件版本:" + this.versionStr);
                handWithTaskSuccess();
                break;
            case Opcodes.IF_ICMPGE /* 162 */:
                MainApplication.getApp().sendBroadcast(new Intent(BaseCameraCfg.takePhotoAction));
                handWithTaskSuccess();
                break;
            case Opcodes.GETSTATIC /* 178 */:
            case 179:
                DevStepUtil.gteInstance().receiveTotalStep(bArr);
                handWithTaskSuccess();
                break;
            case 224:
                DevMeasureUtil.getInstance().getMeasureState(bArr);
                handWithTaskSuccess();
                break;
            case 225:
                DevMeasureUtil.getInstance().getRealMeasureEntity(bArr);
                handWithTaskSuccess();
                break;
            case 243:
                PhoneUtils.endCall(MainApplication.getContext());
                handWithTaskSuccess();
                break;
        }
        if (BleUtil.byte2HexStr(bArr).equalsIgnoreCase("EEEEEEEE") && this.isSyncHistoryData) {
            handWithTaskSuccess();
            syncDataWithFunction();
        }
    }

    private void privateWriteData(byte[] bArr) {
        if (this.isConn) {
            reUpdateSomeStataAndFlag(bArr);
            try {
                writeDataWithoutResp(U_SER, U_write, bArr);
            } catch (BleUUIDNullException e) {
                e.printStackTrace();
            }
        }
    }

    private void reConn() {
        this.handler.postDelayed(new Runnable() { // from class: com.czw.smartkit.bleModule.BleManager.1
            @Override // java.lang.Runnable
            public void run() {
                BleDevice read = SharePreferenceDevice.read();
                if (read == null || TextUtils.isEmpty(read.getMac())) {
                    return;
                }
                BleManager.this.connDevice(read.getMac());
            }
        }, 1200L);
    }

    private void reUpdateSomeStataAndFlag(byte[] bArr) {
        int i = bArr[0] & 255;
        if (i == 19 || i == 21 || i == 49 || !BleUtil.byte2HexStr(bArr).equalsIgnoreCase("1d0155aa")) {
            return;
        }
        ycBleLog.e("===>>>" + BleUtil.byte2HexStr(bArr));
    }

    private final void setSyncFinish(boolean z) {
        this.boolIsSyncData = !z;
        Iterator<SyncCallback> it = this.syncCallbackHashSet.iterator();
        while (it.hasNext()) {
            SyncCallback next = it.next();
            if (z) {
                next.onSyncFinish();
            } else {
                next.onSyncDataing();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncDataWithFunction() {
        DevFunctionEntity devFunctionEntity = DevFunctionHelper.getInstance().getDevFunctionEntity();
        this.syncIndex++;
        ycBleLog.e("debug===请求历史数据");
        if (devFunctionEntity.isSupportStep() && this.dataType == 1) {
            ycBleLog.e("步数" + this.syncIndex + FileUriModel.SCHEME + this.needQueryDayCount);
            if (this.syncIndex <= this.needQueryDayCount) {
                ycBleLog.e("debug===继续请求步数数据===>" + this.syncIndex);
                privateWriteData(DataStruct.createHistorySportData(this.syncIndex - this.needQueryDayCount));
                return;
            }
            ycBleLog.e("debug===步数请求完成了，开始计算睡眠的数据===>" + this.syncIndex);
            this.dataType = 2;
            this.syncIndex = -1;
            this.needQueryDayCount = DevSleepUtil.getInstance().needQueryDayDataCount();
            if (this.isSyncHistoryData) {
                syncDataWithFunction();
                return;
            }
            return;
        }
        SyncDataUtil.getInstance().syncStepData();
        if (devFunctionEntity.isSupportSleep() && this.dataType == 2) {
            ycBleLog.e("睡眠" + this.syncIndex + FileUriModel.SCHEME + this.needQueryDayCount);
            if (this.syncIndex <= this.needQueryDayCount) {
                ycBleLog.e("debug===继续请求睡眠数据===>" + this.syncIndex);
                privateWriteData(DataStruct.createHistorySleepData(this.syncIndex - this.needQueryDayCount));
                return;
            }
            ycBleLog.e("debug===睡眠请求完成了，开始计算测量的数据" + this.syncIndex);
            this.dataType = 3;
            this.syncIndex = -1;
            this.needQueryDayCount = DevMeasureUtil.getInstance().needQueryDayDataCount(devFunctionEntity);
            if (this.isSyncHistoryData) {
                syncDataWithFunction();
                return;
            }
            return;
        }
        SyncDataUtil.getInstance().syncSleepData();
        if ((!devFunctionEntity.isSupportHr() && !devFunctionEntity.isSupportOx() && !devFunctionEntity.isSupportBlood()) || this.dataType != 3) {
            this.isSyncHistoryData = false;
            ycBleLog.e("=================================");
            ycBleLog.e("|=======数据同步完成了============|");
            ycBleLog.e("=================================");
            setSyncFinish(true);
            SyncDataUtil.getInstance().syncHrData();
            SyncDataUtil.getInstance().syncOxData();
            SyncDataUtil.getInstance().syncBloodData();
            bleManager.writeData(DataStruct.createAutoReportStep(true));
            return;
        }
        ycBleLog.e("测量" + this.syncIndex + FileUriModel.SCHEME + this.needQueryDayCount);
        if (this.syncIndex < this.needQueryDayCount) {
            ycBleLog.e("debug===继续请求测量数据===>");
            privateWriteData(DataStruct.createHistoryHealthData(this.syncIndex - this.needQueryDayCount));
            return;
        }
        this.dataType = -1;
        this.syncIndex = -1;
        if (this.isSyncHistoryData) {
            syncDataWithFunction();
        }
    }

    public String getVersionStr() {
        return this.versionStr;
    }

    public boolean isSyncHistoryData() {
        return this.isSyncHistoryData || this.isAfterConnFirstTask;
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void loadCfg() {
        addBleUnitTask(BleUnitTask.createEnableNotify(U_SER, U_notify, "打开通知"));
        addBleUnitTask(BleUnitTask.createWriteWithOutResp(U_SER, U_write, DataStruct.createGetBattery(), "获取电量"));
        addBleUnitTask(BleUnitTask.createWriteWithOutResp(U_SER, U_write, DataStruct.currentTime(), "同步时间"));
        addBleUnitTask(BleUnitTask.createWriteWithOutResp(U_SER, U_write, DataStruct.createFirmware(), "获取固件版本"));
        addBleUnitTask(BleUnitTask.createWriteWithOutResp(U_SER, U_write, DataStruct.createRemindEnable(SharePreferenceRemind.read()), new String[0]));
        addBleUnitTask(BleUnitTask.createWriteWithOutResp(U_SER, U_write, DataStruct.createHistoryCheckData(), "获取手环界面上的数据"));
        addBleUnitTask(BleUnitTask.createWriteWithOutResp(U_SER, U_write, DataStruct.devFunction(), "获取手环功能"));
        addBleUnitTask(BleUnitTask.createWriteWithOutResp(U_SER, U_write, new byte[]{112, 0}, "关闭断开提醒"));
        UnitCfg read = SharePreferenceUnit.read();
        if (read == null) {
            read = new UnitCfg();
        }
        addBleUnitTask(BleUnitTask.createWriteWithOutResp(U_SER, U_write, DataStruct.unitSet(read.getIndex()), "同步一次公英制"));
        addBleUnitTask(BleUnitTask.createWriteWithOutResp(U_SER, U_write, DataStruct.createAutoReportStep(true), "主动上报一次数据"));
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public synchronized void onBleOpen() {
        this.intLostCount = 0;
        this.isSyncHistoryData = false;
        reConn();
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onConnException() {
        MainApplication.getApp().sendBroadcast(new Intent(BaseCameraCfg.exitTakePhotoForAppWithDisconnected));
        BatteryHelper.getInstance().notifyBattery(-1);
        setSyncFinish(true);
        this.isAfterConnFirstTask = false;
        if (isOTAMode()) {
            ycBleLog.e(":OTA 模式 不需要扫描设备");
        } else {
            ycBleLog.e(" 蓝牙此时还打开着，判定为异常断开");
            reConn();
        }
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onConnectSuccess() {
        this.lostPlayUtil.stop();
        this.intLostCount = 0;
        this.isAfterConnFirstTask = true;
        this.isGetBattery = false;
        setSyncFinish(false);
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onDataReceive(byte[] bArr, UUID uuid) {
        onHandData(bArr);
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onDataWrite(byte[] bArr, boolean z, UUID... uuidArr) {
        super.onDataWrite(bArr, z, uuidArr);
        if (z) {
            handWithWriteDataFlag(bArr);
        } else if (BleUtil.byte2HexStr(bArr).equalsIgnoreCase("0100")) {
            handWithTaskSuccess();
        }
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onFinishTaskAfterConn() {
        this.isAfterConnFirstTask = false;
        this.isSyncHistoryData = true;
        DevFunctionEntity devFunctionEntity = DevFunctionHelper.getInstance().getDevFunctionEntity();
        if (devFunctionEntity == null) {
            disConn();
            return;
        }
        ycBleLog.e("同步时序任务结束");
        ycBleLog.e("基本指令同步完成....");
        ycBleLog.e("先拿到今天的步数....");
        ycBleLog.e("先罗列一下设备功能列表:" + devFunctionEntity.toString());
        if (devFunctionEntity.isSupportStep()) {
            this.needQueryDayCount = DevStepUtil.gteInstance().needQueryDayDataCount();
            this.dataType = 1;
        } else if (devFunctionEntity.isSupportSleep()) {
            this.needQueryDayCount = DevSleepUtil.getInstance().needQueryDayDataCount();
            this.dataType = 2;
        } else if (devFunctionEntity.isSupportHr() || devFunctionEntity.isSupportOx() || devFunctionEntity.isSupportBlood()) {
            this.needQueryDayCount = DevMeasureUtil.getInstance().needQueryDayDataCount(devFunctionEntity);
            this.dataType = 3;
        }
        this.syncIndex = -1;
        syncDataWithFunction();
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onHandDisConn() {
        this.isAfterConnFirstTask = false;
        BatteryHelper.getInstance().notifyBattery(-1);
        setSyncFinish(true);
        this.isSyncHistoryData = false;
        ycBleLog.e("手动断开的设备");
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    public void onResponseTimeOut(byte[] bArr) {
        super.onResponseTimeOut(bArr);
        if (BleUtil.byte2HexStr(bArr).equalsIgnoreCase("0300")) {
            DevFunctionHelper.getInstance().notifyDeviceFunction(new DevFunctionEntity());
        } else if (bArr[0] == 19 || bArr[0] == 21 || bArr[0] == 22) {
            this.isMuliteDataWrite = false;
            if (this.isSyncHistoryData) {
                syncDataWithFunction();
            }
        }
        ycBleLog.e("--超时的指令是:" + BleUtil.byte2HexStr(bArr));
    }

    public void registerSyncCallback(SyncCallback syncCallback) {
        if (this.syncCallbackHashSet.contains(syncCallback)) {
            return;
        }
        this.syncCallbackHashSet.add(syncCallback);
    }

    @Override // ycble.runchinaup.core.AbsBleManager
    protected boolean specialCommand(BleUnitTask bleUnitTask) {
        byte[] data = bleUnitTask.getData();
        if (bleUnitTask.getOptionType() != 1) {
            int byte2IntLR = BleUtil.byte2IntLR(data[0]);
            if (byte2IntLR == 19 || byte2IntLR == 21 || byte2IntLR == 22) {
                this.isMuliteDataWrite = true;
                return true;
            }
            if (byte2IntLR == 20) {
                this.isGetBattery = true;
            }
        }
        return false;
    }

    public void unRegisterSyncCallback(SyncCallback syncCallback) {
        if (this.syncCallbackHashSet.contains(syncCallback)) {
            this.syncCallbackHashSet.remove(syncCallback);
        }
    }

    public void writeData(byte[] bArr) {
        ycBleLog.e("准备写指令<<<" + BleUtil.byte2HexStr(bArr));
        if (!isConn()) {
            ycBleLog.e(">不在连接状态，不响应");
            return;
        }
        if (this.isAfterConnFirstTask) {
            ycBleLog.e(">正在进行连接后的时序,不响应");
            return;
        }
        if (this.isSyncHistoryData) {
            ycBleLog.e(">正在同步历史数据,不响应");
            return;
        }
        if (this.isMuliteDataWrite) {
            ycBleLog.e(">当前多包的数据 还没有完全响应，不处理指令");
            return;
        }
        ycBleLog.e(">准备写指令==>" + BleUtil.byte2HexStr(bArr));
        if (!isConn() || this.isAfterConnFirstTask) {
            ycBleLog.e(">没有连接,或者正在数据同步历史");
            return;
        }
        if (isConn() && !this.isAfterConnFirstTask) {
            privateWriteData(bArr);
        } else if (this.isAfterConnFirstTask) {
            ycBleLog.e(">数据正在同步,不能交互");
        }
    }

    public synchronized void writeMuliteData(ArrayList<byte[]> arrayList) {
        if (boolCanRWN()) {
            if (this.boolIsMultiWriteDataIng) {
                ycBleLog.e("消息队列里面还有消息没有推送完成");
            } else {
                clearMultiFlag();
                this.listMultiPckData.addAll(arrayList);
                nextPckDataForMultiData();
                this.handlerMulti.postDelayed(new Runnable() { // from class: com.czw.smartkit.bleModule.BleManager.2
                    @Override // java.lang.Runnable
                    public void run() {
                        BleManager.this.clearMultiFlag();
                    }
                }, 6000L);
            }
        }
    }
}
