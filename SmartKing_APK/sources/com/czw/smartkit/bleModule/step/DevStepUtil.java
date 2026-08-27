package com.czw.smartkit.bleModule.step;

import android.text.TextUtils;
import com.czw.smartkit.databaseModule.step.StepDataTable;
import com.czw.smartkit.databaseModule.step.StepServiceImpl;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.views.StepTableView;
import com.czw.utils.DateUtil;
import com.czw.utils.LogUtil;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes.dex */
public class DevStepUtil {
    private static final int SmallPckDateLen = 11;
    private static final DevStepUtil instance = new DevStepUtil();
    private static final String yyyyMMddFormatString = "yyyy-MM-dd";
    private int totalGlobalStep = 0;
    private StepServiceImpl stepService = StepServiceImpl.getInstance();
    private HashMap<String, List<DevPartStepBean>> timeStepDataMap = new HashMap<>();
    private HashSet<DevStepCallback> callbackHashSet = new HashSet<>();
    private boolean idDebug = true;

    private DevStepUtil() {
    }

    private int getDataIndex(List<DevPartStepBean> list, DevPartStepBean devPartStepBean) {
        if (list == null || list.size() < 1) {
            return -1;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getDateTimeStr().equalsIgnoreCase(devPartStepBean.getDateTimeStr()) && list.get(i).getHour() == devPartStepBean.getHour()) {
                return i;
            }
        }
        return -1;
    }

    public static String getDate(byte[] bArr) {
        return (BleUtil.byte2IntLR(bArr[1]) + 2000) + "-" + String.format("%02d", Integer.valueOf(BleUtil.byte2IntLR(bArr[2]))) + "-" + String.format("%02d", Integer.valueOf(BleUtil.byte2IntLR(bArr[3])));
    }

    private DevPartStepBean getHourStepData(byte[] bArr) {
        DevPartStepBean devPartStepBean = new DevPartStepBean();
        devPartStepBean.setHour(BleUtil.byte2IntLR(bArr[4]));
        devPartStepBean.setStep(BleUtil.byte2IntLR(bArr[5], bArr[6], bArr[7], bArr[8]));
        devPartStepBean.setCalorie(BleUtil.byte2IntLR(bArr[9], bArr[10]));
        devPartStepBean.setDistanceM(BleUtil.byte2IntLR(bArr[11], bArr[12], bArr[13], bArr[14]));
        devPartStepBean.setDateTimeStr(devPartStepBean.getDateTimeStr());
        devPartStepBean.setBleHexStr(BleUtil.byte2HexStr(bArr));
        return devPartStepBean;
    }

    public static List<DevPartStepBean> getMinuteStepData(String str) {
        ArrayList arrayList = new ArrayList();
        LogUtil.e("debug==解析的ble数据是:::===>" + str);
        String replace = str.replace(",", "");
        if (TextUtils.isEmpty(replace) || (replace.length() - 8) % 22 != 0) {
            LogUtil.e("数据不符合格式！！！");
            return arrayList;
        }
        byte[] hexStr2Byte = BleUtil.hexStr2Byte(replace);
        int length = (hexStr2Byte.length - 4) / 11;
        byte[] bArr = new byte[4];
        System.arraycopy(hexStr2Byte, 0, bArr, 0, bArr.length);
        String date = getDate(bArr);
        for (int i = 0; i < length; i++) {
            byte[] bArr2 = new byte[11];
            System.arraycopy(hexStr2Byte, (i * 11) + 4, bArr2, 0, bArr2.length);
            DevPartStepBean devPartStepBean = new DevPartStepBean();
            devPartStepBean.setHour(BleUtil.byte2IntLR(bArr2[0]));
            devPartStepBean.setStep(BleUtil.byte2IntLR(bArr2[1], bArr2[2], bArr2[3], bArr2[4]));
            devPartStepBean.setCalorie(BleUtil.byte2IntLR(bArr2[5], bArr2[6]));
            devPartStepBean.setDistanceM(BleUtil.byte2IntLR(bArr2[7], bArr2[8], bArr2[9], bArr2[10]));
            devPartStepBean.setDateStr(date);
            devPartStepBean.setDateTimeStr(devPartStepBean.getDateTimeStr());
            if (devPartStepBean.getHour() >= 0 && devPartStepBean.getHour() < 48 && devPartStepBean.getStep() > 0) {
                arrayList.add(devPartStepBean);
            }
        }
        return arrayList;
    }

    public static DevStepUtil gteInstance() {
        return instance;
    }

    public static List<StepTableView.StepTableData> stepDTO2ViewData(List<DevPartStepBean> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null || list.size() < 1) {
            return arrayList;
        }
        for (DevPartStepBean devPartStepBean : list) {
            int hour = devPartStepBean.getHour();
            int step = devPartStepBean.getStep();
            if (hour >= 0 && hour <= 23) {
                arrayList.add(new StepTableView.StepTableData(hour, step));
            }
        }
        return arrayList;
    }

    void handThisHistoryDataInYourApp(DevTotalStepBean devTotalStepBean) {
        StepDataTable stepDataTable = new StepDataTable();
        stepDataTable.setCalorie(devTotalStepBean.getCalorie());
        stepDataTable.setDistance(devTotalStepBean.getDistance());
        if (devTotalStepBean.getStep() < this.totalGlobalStep) {
            devTotalStepBean.setStep(this.totalGlobalStep);
        }
        stepDataTable.setWalkCounts(devTotalStepBean.getStep());
        stepDataTable.setSync(false);
        stepDataTable.setUid(UserUtil.getUid());
        stepDataTable.setDate(devTotalStepBean.getDate());
        stepDataTable.setDetailJson(devTotalStepBean.getBleHexStr());
        stepDataTable.setGoalWalk(UserUtil.getTarget());
        stepDataTable.setStartTime(DateFormatUtils.format(devTotalStepBean.getDate() * 1000, yyyyMMddFormatString));
        LogUtil.e("ble step history >>> " + new Gson().toJson(stepDataTable));
        this.stepService.saveData(stepDataTable);
    }

    public int needQueryDayDataCount() {
        StepDataTable findLast = this.stepService.findLast(UserUtil.getUid());
        if (findLast == null) {
            return 3;
        }
        Date date = new Date(findLast.getDate() * 1000);
        LogUtil.e("debg===最后一次步数的记录日期是:" + DateFormatUtils.format(date, yyyyMMddFormatString));
        int daysBetween = DateUtil.daysBetween(new Date(), date);
        LogUtil.e("debug==两个日期相差多少天===>" + daysBetween);
        if (daysBetween > 3) {
            daysBetween = 3;
        }
        if (daysBetween == 0) {
            daysBetween = 1;
        }
        LogUtil.e("debug==实际需要请求天数===>" + daysBetween);
        return daysBetween;
    }

    void notifyTotalStep(DevTotalStepBean devTotalStepBean) {
        Iterator<DevStepCallback> it = this.callbackHashSet.iterator();
        while (it.hasNext()) {
            it.next().onStepTotal(devTotalStepBean);
        }
        StepDataTable stepDataByDate = StepServiceImpl.getInstance().getStepDataByDate(UserUtil.getUid(), new SimpleDateFormat(yyyyMMddFormatString).format(Long.valueOf(System.currentTimeMillis())));
        LogUtil.e("db-步数/今天的总步数:" + new Gson().toJson(stepDataByDate));
        if (stepDataByDate != null) {
            stepDataByDate.setWalkCounts(devTotalStepBean.getStep());
            StepServiceImpl.getInstance().saveData(stepDataByDate);
        }
    }

    public synchronized void receiveHistoryData(byte[] bArr) throws ParseException {
        int i;
        int i2;
        int i3;
        if (bArr == null) {
            return;
        }
        if (bArr.length != 15) {
            return;
        }
        int byte2IntLR = BleUtil.byte2IntLR(bArr[1]) + 2000;
        int byte2IntLR2 = BleUtil.byte2IntLR(bArr[2]);
        int byte2IntLR3 = BleUtil.byte2IntLR(bArr[3]);
        int byte2IntLR4 = BleUtil.byte2IntLR(bArr[4]);
        String format = String.format("%d-%02d-%02d", Integer.valueOf(byte2IntLR), Integer.valueOf(byte2IntLR2), Integer.valueOf(byte2IntLR3));
        List<DevPartStepBean> arrayList = this.timeStepDataMap.containsKey(format) ? this.timeStepDataMap.get(format) : new ArrayList<>();
        DevPartStepBean hourStepData = getHourStepData(bArr);
        hourStepData.setDateTimeStr(format);
        int dataIndex = getDataIndex(arrayList, hourStepData);
        if (dataIndex == -1) {
            arrayList.add(hourStepData);
        } else {
            arrayList.set(dataIndex, hourStepData);
        }
        this.timeStepDataMap.put(format, arrayList);
        if (byte2IntLR4 == 255) {
            LogUtil.e("结束数据，可以拼装数据了");
            for (Map.Entry<String, List<DevPartStepBean>> entry : this.timeStepDataMap.entrySet()) {
                String key = entry.getKey();
                List<DevPartStepBean> value = entry.getValue();
                Collections.sort(value, new Comparator<DevPartStepBean>() { // from class: com.czw.smartkit.bleModule.step.DevStepUtil.1
                    @Override // java.util.Comparator
                    public int compare(DevPartStepBean devPartStepBean, DevPartStepBean devPartStepBean2) {
                        return devPartStepBean.getHour() - devPartStepBean2.getHour();
                    }
                });
                LogUtil.e("debug===步数1小时的分段数据:" + new Gson().toJson(value));
                StringBuilder sb = new StringBuilder();
                if (value == null || value.size() <= 0) {
                    i = 0;
                    i2 = 0;
                    i3 = 0;
                } else {
                    sb.append(value.get(0).getBleHexStr().substring(0, 8));
                    i = 0;
                    i2 = 0;
                    i3 = 0;
                    for (DevPartStepBean devPartStepBean : value) {
                        if (devPartStepBean.getHour() >= 0 && devPartStepBean.getHour() < 48) {
                            i3 += devPartStepBean.getStep();
                            i2 += devPartStepBean.getDistanceM();
                            i += devPartStepBean.getCalorie();
                        }
                        sb.append(devPartStepBean.getBleHexStr().substring(8));
                    }
                }
                long time = DateUtils.parseDate(key, new String[]{yyyyMMddFormatString}).getTime();
                String format2 = DateFormatUtils.format(time, yyyyMMddFormatString);
                DevTotalStepBean devTotalStepBean = new DevTotalStepBean();
                devTotalStepBean.setCalorie(i);
                devTotalStepBean.setDate(time / 1000);
                devTotalStepBean.setDistance(i2);
                devTotalStepBean.setStep(i3);
                devTotalStepBean.setBleHexStr(BleUtil.byte2HexStr(BleUtil.hexStr2Byte(sb.toString()), ","));
                handThisHistoryDataInYourApp(devTotalStepBean);
                if (format2.equalsIgnoreCase(DateFormatUtils.format(System.currentTimeMillis(), yyyyMMddFormatString))) {
                    Iterator<DevStepCallback> it = this.callbackHashSet.iterator();
                    while (it.hasNext()) {
                        it.next().onStepHistoryDataList(value);
                    }
                }
            }
            this.timeStepDataMap.clear();
        }
        if (this.idDebug) {
        }
    }

    public void receiveTotalStep(byte[] bArr) {
        DevTotalStepBean devTotalStepBean = new DevTotalStepBean();
        if (BleUtil.byte2IntLR(bArr[1]) == 1) {
            devTotalStepBean.setDate(System.currentTimeMillis() / 1000);
            devTotalStepBean.setStep(BleUtil.byte2IntLR(bArr[2], bArr[3], bArr[4], bArr[5]));
            devTotalStepBean.setCalorie(BleUtil.byte2IntLR(bArr[6], bArr[7]));
            devTotalStepBean.setDistance(BleUtil.byte2IntLR(bArr[8], bArr[9], bArr[10], bArr[11]));
        }
        this.totalGlobalStep = devTotalStepBean.getStep();
        notifyTotalStep(devTotalStepBean);
    }

    public void registerCallback(DevStepCallback devStepCallback) {
        if (this.callbackHashSet.contains(devStepCallback)) {
            return;
        }
        this.callbackHashSet.add(devStepCallback);
    }

    public void unRegisterCallback(DevStepCallback devStepCallback) {
        if (this.callbackHashSet.contains(devStepCallback)) {
            this.callbackHashSet.remove(devStepCallback);
        }
    }
}
