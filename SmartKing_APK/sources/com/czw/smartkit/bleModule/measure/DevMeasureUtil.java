package com.czw.smartkit.bleModule.measure;

import com.czw.smartkit.bleModule.callback.DeviceUIValueCallback;
import com.czw.smartkit.bleModule.data.DevFunctionEntity;
import com.czw.smartkit.databaseModule.blood.BloodDataTable;
import com.czw.smartkit.databaseModule.blood.BloodServiceImpl;
import com.czw.smartkit.databaseModule.hr.HrDataTable;
import com.czw.smartkit.databaseModule.hr.HrServiceImpl;
import com.czw.smartkit.databaseModule.ox.OxDataTable;
import com.czw.smartkit.databaseModule.ox.OxServiceImpl;
import com.czw.smartkit.user.UserUtil;
import com.czw.utils.DateUtil;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.lang.time.DateFormatUtils;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes.dex */
public class DevMeasureUtil {
    private static final DevMeasureUtil instance = new DevMeasureUtil();
    private static final String yyyyMMddHHmmssFormatString = "yyyy-MM-dd HH:mm:ss";
    private DevMeasureBean devMeasureBean;
    private DeviceUIValueCallback deviceUIValueCallback;
    private MeasureType measureType;
    private HashSet<DevMeasureCallback> measureEntityCallbackHashSet = new HashSet<>();
    private HrServiceImpl hrService = HrServiceImpl.getInstance();
    private BloodServiceImpl bloodService = BloodServiceImpl.getInstance();
    private Gson gson = new Gson();

    private DevMeasureUtil() {
    }

    public static String getDate(byte[] bArr) {
        return (BleUtil.byte2IntLR(bArr[1]) + 2000) + "-" + String.format("%02d", Integer.valueOf(BleUtil.byte2IntLR(bArr[2]))) + "-" + String.format("%02d", Integer.valueOf(BleUtil.byte2IntLR(bArr[3])));
    }

    public static DevMeasureUtil getInstance() {
        return instance;
    }

    private void handThisHistoryDataInYourApp(DevMeasureBean devMeasureBean) {
        if (devMeasureBean == null || devMeasureBean.getDateTime() == 0) {
            LogUtil.e("时间为0,不处理这个数据");
            return;
        }
        if (devMeasureBean.getIntHr() != 0) {
            HrDataTable hrDataTable = new HrDataTable();
            hrDataTable.setUid(UserUtil.getUid());
            hrDataTable.setNumber(devMeasureBean.getIntHr() + "");
            hrDataTable.setDate(devMeasureBean.getDateTime());
            hrDataTable.setSync(false);
            hrDataTable.setDateTimeStr(DateFormatUtils.format(devMeasureBean.getDateTime() * 1000, yyyyMMddHHmmssFormatString));
            HrServiceImpl.getInstance().save(hrDataTable);
        }
        if (devMeasureBean.getIntOx() != 0) {
            OxDataTable oxDataTable = new OxDataTable();
            oxDataTable.setUid(UserUtil.getUid());
            oxDataTable.setNumber(devMeasureBean.getIntOx() + "");
            oxDataTable.setDate(devMeasureBean.getDateTime());
            oxDataTable.setSync(false);
            oxDataTable.setDateTimeStr(DateFormatUtils.format(devMeasureBean.getDateTime() * 1000, yyyyMMddHHmmssFormatString));
            OxServiceImpl.getInstance().save(oxDataTable);
        }
        if (devMeasureBean.getIntBpH() <= 60 || devMeasureBean.getIntBpH() >= 240 || devMeasureBean.getIntBpL() <= 30 || devMeasureBean.getIntBpL() >= 200) {
            return;
        }
        BloodDataTable bloodDataTable = new BloodDataTable();
        bloodDataTable.setUid(UserUtil.getUid());
        bloodDataTable.setBpH(devMeasureBean.getIntBpH() + "");
        bloodDataTable.setBpL(devMeasureBean.getIntBpL() + "");
        bloodDataTable.setDate(devMeasureBean.getDateTime());
        bloodDataTable.setSync(false);
        bloodDataTable.setDateTimeStr(DateFormatUtils.format(devMeasureBean.getDateTime() * 1000, yyyyMMddHHmmssFormatString));
        BloodServiceImpl.getInstance().save(bloodDataTable);
    }

    private void notifyMeasure(DevMeasureBean devMeasureBean) {
        Iterator<DevMeasureCallback> it = this.measureEntityCallbackHashSet.iterator();
        while (it.hasNext()) {
            it.next().onMeasure(devMeasureBean);
        }
    }

    private void notifyStart(MeasureType measureType) {
        this.measureType = measureType;
        Iterator<DevMeasureCallback> it = this.measureEntityCallbackHashSet.iterator();
        while (it.hasNext()) {
            it.next().onStartMeasure(measureType);
        }
    }

    private void notifyStop(MeasureType measureType) {
        Iterator<DevMeasureCallback> it = this.measureEntityCallbackHashSet.iterator();
        while (it.hasNext()) {
            it.next().onStopMeasure(measureType);
        }
        LogUtil.e("保存的测量数据是:" + this.gson.toJson(this.devMeasureBean));
        handThisHistoryDataInYourApp(this.devMeasureBean);
        this.devMeasureBean = null;
        this.measureType = null;
    }

    public void getDeviceUiMeasureValue(byte[] bArr) {
        int i = bArr[1] & 255;
        int i2 = bArr[2] & 255;
        int i3 = bArr[3] & 255;
        int i4 = bArr[4] & 255;
        if (this.deviceUIValueCallback != null) {
            this.deviceUIValueCallback.onDeviceUIvalue(i, i2, i3, i4);
        }
    }

    public void getMeasureState(byte[] bArr) {
        int byte2IntLR = BleUtil.byte2IntLR(bArr[2]);
        if (byte2IntLR == 0) {
            notifyStop(MeasureType.getMeasureType(BleUtil.byte2IntLR(bArr[1])));
        } else if (byte2IntLR == 1) {
            notifyStart(MeasureType.getMeasureType(BleUtil.byte2IntLR(bArr[1])));
        } else if (byte2IntLR == 238) {
            notifyStop(MeasureType.getMeasureType(BleUtil.byte2IntLR(bArr[1])));
        }
    }

    public void getRealMeasureEntity(byte[] bArr) {
        if (this.devMeasureBean == null) {
            this.devMeasureBean = new DevMeasureBean();
        }
        this.devMeasureBean.setDateTime(System.currentTimeMillis() / 1000);
        if (BleUtil.byte2IntLR(bArr[0]) == 225) {
            LogUtil.e("解析实时测量数据");
            this.devMeasureBean.setIntHr(BleUtil.byte2IntLR(bArr[1]));
            this.devMeasureBean.setIntBpH(BleUtil.byte2IntLR(bArr[2]));
            this.devMeasureBean.setIntBpL(BleUtil.byte2IntLR(bArr[3]));
            this.devMeasureBean.setIntOx(BleUtil.byte2IntLR(bArr[4]));
            this.devMeasureBean.setAvgHr(BleUtil.byte2IntLR(bArr[5]));
            this.devMeasureBean.setMaxHr(BleUtil.byte2IntLR(bArr[6]));
            this.devMeasureBean.setMinHr(BleUtil.byte2IntLR(bArr[7]));
        }
        LogUtil.e("实时检测数据:" + this.devMeasureBean.toString());
        notifyMeasure(this.devMeasureBean);
    }

    public int needQueryDayDataCount(DevFunctionEntity devFunctionEntity) {
        if (devFunctionEntity.isSupportHr()) {
            HrDataTable findLast = this.hrService.findLast(UserUtil.getUid());
            if (findLast == null) {
                return 3;
            }
            int daysBetween = DateUtil.daysBetween(new Date(), new Date(findLast.getDate() * 1000));
            LogUtil.e("debug==两个心率日期相差多少天===>" + daysBetween);
            if (daysBetween > 3) {
                daysBetween = 3;
            }
            LogUtil.e("debug==实际需要请求天数===>" + daysBetween);
            return daysBetween;
        }
        if (!devFunctionEntity.isSupportBlood()) {
            return 0;
        }
        BloodDataTable findLast2 = this.bloodService.findLast(UserUtil.getUid());
        if (findLast2 == null) {
            return 3;
        }
        int daysBetween2 = DateUtil.daysBetween(new Date(), new Date(findLast2.getDate() * 1000));
        LogUtil.e("debug==两个血压日期相差多少天===>" + daysBetween2);
        if (daysBetween2 > 3) {
            daysBetween2 = 3;
        }
        LogUtil.e("debug==实际需要请求天数===>" + daysBetween2);
        return daysBetween2;
    }

    public final void receiveHistoryData(byte[] bArr) {
        DevMeasureBean devMeasureBean = new DevMeasureBean();
        devMeasureBean.setDateTime(BleUtil.byte2IntLR(bArr[3], bArr[4], bArr[5], bArr[6]));
        devMeasureBean.setIntHr(BleUtil.byte2IntLR(bArr[7]));
        devMeasureBean.setIntBpH(BleUtil.byte2IntLR(bArr[8]));
        devMeasureBean.setIntBpL(BleUtil.byte2IntLR(bArr[9]));
        devMeasureBean.setIntOx(BleUtil.byte2IntLR(bArr[10]));
        LogUtil.e("第一个历史检测数据:" + devMeasureBean.toString());
        handThisHistoryDataInYourApp(devMeasureBean);
        DevMeasureBean devMeasureBean2 = new DevMeasureBean();
        devMeasureBean2.setDateTime((long) BleUtil.byte2IntLR(bArr[11], bArr[12], bArr[13], bArr[14]));
        devMeasureBean2.setIntHr(BleUtil.byte2IntLR(bArr[15]));
        devMeasureBean2.setIntBpH(BleUtil.byte2IntLR(bArr[16]));
        devMeasureBean2.setIntBpL(BleUtil.byte2IntLR(bArr[17]));
        devMeasureBean2.setIntOx(BleUtil.byte2IntLR(bArr[18]));
        LogUtil.e("第二个历史检测数据:" + devMeasureBean2.toString());
        handThisHistoryDataInYourApp(devMeasureBean2);
    }

    public void registerCallback(DevMeasureCallback devMeasureCallback) {
        if (this.measureEntityCallbackHashSet.contains(devMeasureCallback)) {
            return;
        }
        this.measureEntityCallbackHashSet.add(devMeasureCallback);
    }

    public void setDeviceUIValueCallback(DeviceUIValueCallback deviceUIValueCallback) {
        this.deviceUIValueCallback = deviceUIValueCallback;
    }

    public void unRegisterCallback(DevMeasureCallback devMeasureCallback) {
        if (this.measureEntityCallbackHashSet.contains(devMeasureCallback)) {
            this.measureEntityCallbackHashSet.remove(devMeasureCallback);
        }
    }
}
