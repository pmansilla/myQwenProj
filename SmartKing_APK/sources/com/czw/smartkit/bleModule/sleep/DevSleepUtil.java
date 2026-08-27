package com.czw.smartkit.bleModule.sleep;

import android.text.TextUtils;
import com.czw.smartkit.databaseModule.sleep.SleepDataTable;
import com.czw.smartkit.databaseModule.sleep.SleepServiceImpl;
import com.czw.smartkit.user.UserUtil;
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
import java.util.List;
import java.util.Map;
import me.panpf.sketch.uri.FileUriModel;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes.dex */
public class DevSleepUtil {
    private static final int SmallPckDateLen = 6;
    private static final DevSleepUtil ourInstance = new DevSleepUtil();
    private SleepServiceImpl sleepService = SleepServiceImpl.getInstance();
    private HashMap<String, List<DevPartSleepBean>> listHashMap = new HashMap<>();
    private Gson gson = new Gson();
    private DevSleepCallback devSleepCallback = null;

    private DevSleepUtil() {
    }

    static byte[] date2ByteArrLR(long j) {
        return new byte[]{(byte) (((-16777216) & j) >> 24), (byte) ((16711680 & j) >> 16), (byte) ((65280 & j) >> 8), (byte) (j & 255)};
    }

    private static List<DevPartSleepBean> getDevMinuteData(byte[] bArr) {
        ArrayList arrayList = new ArrayList();
        long byte2IntLR = BleUtil.byte2IntLR(bArr[3], bArr[4], bArr[5], bArr[6]);
        int byte2IntLR2 = BleUtil.byte2IntLR(bArr[7]);
        int byte2IntLR3 = BleUtil.byte2IntLR(bArr[8]);
        if (byte2IntLR == 0 || byte2IntLR2 == 0 || byte2IntLR3 == 0) {
            return arrayList;
        }
        DevPartSleepBean devPartSleepBean = new DevPartSleepBean();
        devPartSleepBean.setDate(byte2IntLR);
        devPartSleepBean.setDuration(byte2IntLR2);
        devPartSleepBean.setSleepType(byte2IntLR3);
        byte[] bArr2 = new byte[6];
        System.arraycopy(bArr, 3, bArr2, 0, 6);
        devPartSleepBean.setBleHexString(BleUtil.byte2HexStr(bArr2));
        arrayList.add(devPartSleepBean);
        long byte2IntLR4 = BleUtil.byte2IntLR(bArr[9], bArr[10], bArr[11], bArr[12]);
        int byte2IntLR5 = BleUtil.byte2IntLR(bArr[13]);
        int byte2IntLR6 = BleUtil.byte2IntLR(bArr[14]);
        if (byte2IntLR4 != 0 && byte2IntLR5 != 0 && byte2IntLR6 != 0) {
            DevPartSleepBean devPartSleepBean2 = new DevPartSleepBean();
            devPartSleepBean2.setDate(byte2IntLR4);
            devPartSleepBean2.setDuration(byte2IntLR5);
            devPartSleepBean2.setSleepType(byte2IntLR6);
            byte[] bArr3 = new byte[6];
            System.arraycopy(bArr, 9, bArr3, 0, 6);
            devPartSleepBean2.setBleHexString(BleUtil.byte2HexStr(bArr3));
            arrayList.add(devPartSleepBean2);
        }
        return arrayList;
    }

    public static DevSleepUtil getInstance() {
        return ourInstance;
    }

    public static List<DevPartSleepBean> getMinuteSleepDataList(String str) {
        ArrayList arrayList = new ArrayList();
        String replace = str.replace(",", "");
        if (TextUtils.isEmpty(replace) || (replace.length() - 6) % 12 != 0) {
            LogUtil.e("数据不符合格式！！！");
            return arrayList;
        }
        byte[] hexStr2Byte = BleUtil.hexStr2Byte(replace);
        byte[] bArr = new byte[3];
        System.arraycopy(hexStr2Byte, 0, bArr, 0, bArr.length);
        if (BleUtil.byte2HexStr(bArr).startsWith("95FFFF")) {
            LogUtil.e("没有睡眠数据！！！");
            return arrayList;
        }
        int length = (hexStr2Byte.length - 3) / 6;
        for (int i = 0; i < length; i++) {
            byte[] bArr2 = new byte[6];
            System.arraycopy(hexStr2Byte, (i * 6) + 3, bArr2, 0, bArr2.length);
            DevPartSleepBean devPartSleepBean = new DevPartSleepBean();
            long byte2IntLR = BleUtil.byte2IntLR(bArr2[0], bArr2[1], bArr2[2], bArr2[3]);
            devPartSleepBean.setDate(byte2IntLR);
            devPartSleepBean.setDuration(BleUtil.byte2IntLR(bArr2[4]));
            devPartSleepBean.setSleepType(BleUtil.byte2IntLR(bArr2[5]));
            if (byte2IntLR != 0) {
                arrayList.add(devPartSleepBean);
            }
        }
        return arrayList;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:15:0x00cc. Please report as an issue. */
    public static DevTotalSleepBean getSleepBean(String str, long j) {
        LogUtil.e("debug==time==>" + j);
        String replace = str.replace(",", "");
        if (TextUtils.isEmpty(replace) || (replace.length() - 6) % 12 != 0) {
            LogUtil.e("数据不符合格式！！！");
            return null;
        }
        byte[] hexStr2Byte = BleUtil.hexStr2Byte(replace);
        byte[] bArr = new byte[3];
        System.arraycopy(hexStr2Byte, 0, bArr, 0, bArr.length);
        DevTotalSleepBean devTotalSleepBean = new DevTotalSleepBean();
        DevTotalSleepBean devTotalSleepBean2 = new DevTotalSleepBean();
        devTotalSleepBean2.setDate(j);
        devTotalSleepBean2.setBleHexStr(str);
        ArrayList arrayList = new ArrayList();
        LogUtil.e("debug==睡眠头数据==>" + BleUtil.byte2HexStr(bArr));
        if (replace.equalsIgnoreCase("95FFFF")) {
            LogUtil.e("debug==没有数据==>");
            return devTotalSleepBean;
        }
        int length = (hexStr2Byte.length - 3) / 6;
        for (int i = 0; i < length; i++) {
            byte[] bArr2 = new byte[6];
            System.arraycopy(hexStr2Byte, (i * 6) + 3, bArr2, 0, bArr2.length);
            DevPartSleepBean devPartSleepBean = new DevPartSleepBean();
            devPartSleepBean.setDate(BleUtil.byte2IntLR(bArr2[0], bArr2[1], bArr2[2], bArr2[3]));
            devPartSleepBean.setDuration(BleUtil.byte2IntLR(bArr2[4]));
            devPartSleepBean.setSleepType(BleUtil.byte2IntLR(bArr2[5]));
            if (devPartSleepBean.getSleepType() != 0) {
                switch (devPartSleepBean.getSleepType()) {
                    case 1:
                        devPartSleepBean.getDuration();
                        break;
                    case 2:
                        devPartSleepBean.getDuration();
                        break;
                    case 3:
                        devPartSleepBean.getDuration();
                        break;
                }
                arrayList.add(devPartSleepBean);
            }
        }
        return devTotalSleepBean;
    }

    private void handThisHistoryDataInYourApp(DevTotalSleepBean devTotalSleepBean) {
        SleepDataTable sleepDataTable = new SleepDataTable();
        sleepDataTable.setDate(devTotalSleepBean.getDate());
        sleepDataTable.setTotalTime(devTotalSleepBean.getTotalDeep() + devTotalSleepBean.getTotalLight());
        sleepDataTable.setDeepTime(devTotalSleepBean.getTotalDeep());
        sleepDataTable.setShallowTime(devTotalSleepBean.getTotalLight());
        sleepDataTable.setSoberTime(devTotalSleepBean.getTotalAwake());
        sleepDataTable.setRecord(devTotalSleepBean.getBleHexStr());
        sleepDataTable.setUid(UserUtil.getUid());
        sleepDataTable.setStartTime(DateFormatUtils.format(devTotalSleepBean.getDate() * 1000, "yyyy-MM-dd HH:mm:ss"));
        sleepDataTable.setEndTime(DateFormatUtils.format((devTotalSleepBean.getDate() * 1000) + DateUtils.MILLIS_PER_DAY, "yyyy-MM-dd HH:mm:ss"));
        LogUtil.e("睡眠统计数据:" + new Gson().toJson(devTotalSleepBean));
        this.sleepService.saveData(sleepDataTable);
    }

    public static DevTotalSleepBean totalSleepData(List<DevPartSleepBean> list, long j) {
        DevTotalSleepBean devTotalSleepBean = new DevTotalSleepBean();
        StringBuilder sb = new StringBuilder();
        if (list == null || list.size() < 1) {
            LogUtil.e("睡眠统计数据 " + j);
            sb.append("95FFFF");
            sb.append(BleUtil.byte2HexStr(date2ByteArrLR(j)));
            sb.append("0000000000000000");
        } else {
            sb.append("950000");
        }
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        for (DevPartSleepBean devPartSleepBean : list) {
            switch (devPartSleepBean.getSleepType()) {
                case 1:
                    i += devPartSleepBean.getDuration();
                    break;
                case 2:
                    i2 += devPartSleepBean.getDuration();
                    break;
                case 3:
                    i3 += devPartSleepBean.getDuration();
                    break;
            }
            sb.append(devPartSleepBean.getBleHexString());
        }
        devTotalSleepBean.setTotalDeep(i);
        devTotalSleepBean.setTotalLight(i2);
        devTotalSleepBean.setTotalAwake(i3);
        devTotalSleepBean.setBleHexStr(BleUtil.byte2HexStr(BleUtil.hexStr2Byte(sb.toString()), ","));
        return devTotalSleepBean;
    }

    public int needQueryDayDataCount() {
        SleepDataTable findLast = this.sleepService.findLast(UserUtil.getUid());
        if (findLast == null) {
            return 3;
        }
        Date date = new Date(findLast.getDate() * 1000);
        ycBleLog.e("debg===最后一次睡眠的记录日期是:" + new SimpleDateFormat("yyyy-MM-dd").format(date));
        int daysBetween = DateUtil.daysBetween(new Date(), date);
        if (daysBetween < 0) {
            daysBetween = 0;
        }
        ycBleLog.e("debug==两个日期相差多少天===>" + daysBetween);
        if (daysBetween > 3) {
            daysBetween = 3;
        }
        if (daysBetween == 0) {
            return 1;
        }
        return daysBetween;
    }

    public synchronized void receiveHistoryData(byte[] bArr, int i) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(DateUtil.getTheDayAfterDate(new Date(), i));
        LogUtil.e("dayIndex===>" + i + "///" + format);
        int byte2IntLR = BleUtil.byte2IntLR(bArr[1], bArr[2]);
        if (this.listHashMap.containsKey(format)) {
            List<DevPartSleepBean> list = this.listHashMap.get(format);
            list.addAll(getDevMinuteData(bArr));
            this.listHashMap.put(format, list);
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(getDevMinuteData(bArr));
            this.listHashMap.put(format, arrayList);
        }
        if (byte2IntLR == 65535) {
            LogUtil.e("睡眠数据 某一天的接收完成了,统计一下");
            for (Map.Entry<String, List<DevPartSleepBean>> entry : this.listHashMap.entrySet()) {
                String key = entry.getKey();
                List<DevPartSleepBean> value = entry.getValue();
                Collections.sort(value, new Comparator<DevPartSleepBean>() { // from class: com.czw.smartkit.bleModule.sleep.DevSleepUtil.1
                    @Override // java.util.Comparator
                    public int compare(DevPartSleepBean devPartSleepBean, DevPartSleepBean devPartSleepBean2) {
                        return (int) (devPartSleepBean.getDate() - devPartSleepBean2.getDate());
                    }
                });
                LogUtil.e("debug==睡眠数据:" + this.gson.toJson(value));
                long time = simpleDateFormat.parse(key).getTime() / 1000;
                DevTotalSleepBean devTotalSleepBean = totalSleepData(value, time);
                devTotalSleepBean.setDate(time);
                handThisHistoryDataInYourApp(devTotalSleepBean);
                if (key.equalsIgnoreCase(simpleDateFormat.format(Long.valueOf(System.currentTimeMillis()))) && this.devSleepCallback != null) {
                    this.devSleepCallback.onDevTodaySleepLoad();
                }
                LogUtil.e("睡眠统计数据:" + entry.getKey() + FileUriModel.SCHEME + this.gson.toJson(devTotalSleepBean));
            }
            this.listHashMap.clear();
        }
    }

    public void setDevSleepEntityCallback(DevSleepCallback devSleepCallback) {
        this.devSleepCallback = devSleepCallback;
    }
}
