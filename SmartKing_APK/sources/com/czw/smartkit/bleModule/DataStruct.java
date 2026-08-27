package com.czw.smartkit.bleModule;

import android.text.TextUtils;
import android.text.format.DateFormat;
import com.czw.smartkit.MainApplication;
import com.czw.smartkit.R;
import com.czw.smartkit.bleModule.data.AlarmClock;
import com.czw.smartkit.bleModule.data.RemindSetting;
import com.czw.smartkit.util.YCAppUtils;
import com.czw.utils.DateUtil;
import com.czw.utils.LogUtil;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes.dex */
public class DataStruct {
    public static final int Measure_ALl = 3;
    public static final int Measure_Bd = 1;
    public static final int Measure_Hr = 0;
    public static final int Measure_Ox = 2;
    private static final SimpleDateFormat yyMMDDSmp = new SimpleDateFormat("yy-MM-dd");

    private static String cnToUnicode(String str) {
        String str2 = "";
        for (char c : str.toCharArray()) {
            str2 = str2 + reverse(Integer.toString(c, 16));
        }
        return str2;
    }

    public static byte[] createAutoReportStep(boolean z) {
        return new byte[]{49, z ? (byte) 1 : (byte) 0};
    }

    public static byte[] createClock(AlarmClock... alarmClockArr) {
        byte[] bArr = new byte[19];
        bArr[0] = 2;
        bArr[1] = 3;
        for (int i = 0; i < alarmClockArr.length; i++) {
            System.arraycopy(alarmClockArr[i].packData(), 0, bArr, (i * 4) + 2, 4);
        }
        return bArr;
    }

    public static ArrayList<byte[]> createClockName(AlarmClock... alarmClockArr) throws UnsupportedEncodingException {
        ArrayList<byte[]> arrayList = new ArrayList<>();
        for (int i = 0; i < alarmClockArr.length; i++) {
            byte[] bArr = new byte[20];
            bArr[0] = 116;
            bArr[1] = (byte) i;
            String str = alarmClockArr[i].name;
            if (TextUtils.isEmpty(str)) {
                str = MainApplication.getContext().getString(R.string.clock);
            }
            byte[] bytes = str.getBytes("utf-8");
            System.arraycopy(bytes, 0, bArr, 2, bytes.length);
            arrayList.add(bArr);
        }
        return arrayList;
    }

    public static byte[] createFindBrand(boolean z) {
        return new byte[]{81, z ? (byte) 1 : (byte) 0};
    }

    public static byte[] createFirmware() {
        return new byte[]{31};
    }

    public static byte[] createGetBattery() {
        return new byte[]{20};
    }

    public static byte[] createHistoryCheckData() {
        byte[] bArr = new byte[4];
        bArr[0] = 23;
        return bArr;
    }

    public static byte[] createHistoryHealthData(int i) {
        String format = yyMMDDSmp.format(DateUtil.getTheDayAfterDate(new Date(), i));
        byte[] bArr = new byte[4];
        bArr[0] = 22;
        String[] split = format.split("-");
        int i2 = 0;
        int i3 = 1;
        while (i2 < 3) {
            bArr[i3] = (byte) (Integer.valueOf(split[i2]).intValue() + 0);
            i2++;
            i3++;
        }
        return bArr;
    }

    public static byte[] createHistorySleepData(int i) {
        String format = yyMMDDSmp.format(DateUtil.getTheDayAfterDate(new Date(), i));
        byte[] bArr = new byte[4];
        bArr[0] = 21;
        String[] split = format.split("-");
        int i2 = 0;
        int i3 = 1;
        while (i2 < 3) {
            bArr[i3] = (byte) (Integer.valueOf(split[i2]).intValue() + 0);
            i2++;
            i3++;
        }
        return bArr;
    }

    public static byte[] createHistorySportData(int i) {
        String format = yyMMDDSmp.format(DateUtil.getTheDayAfterDate(new Date(), i));
        byte[] bArr = new byte[4];
        bArr[0] = 19;
        String[] split = format.split("-");
        int i2 = 0;
        int i3 = 1;
        while (i2 < 3) {
            bArr[i3] = (byte) (Integer.valueOf(split[i2]).intValue() + 0);
            i2++;
            i3++;
        }
        return bArr;
    }

    public static ArrayList<byte[]> createLocation(String str) {
        LogUtil.e("location:" + str);
        ArrayList<byte[]> arrayList = new ArrayList<>();
        try {
            byte[] hexStr2Byte = BleUtil.hexStr2Byte("FFFE" + cnToUnicode(str));
            LogUtil.e("编码后的数据:" + BleUtil.byte2HexStr(hexStr2Byte));
            int length = hexStr2Byte.length % 18 == 0 ? hexStr2Byte.length / 18 : (hexStr2Byte.length / 18) + 1;
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                int i3 = i2 * 18;
                int length2 = hexStr2Byte.length - i3;
                int i4 = 20;
                if (length2 < 20) {
                    i4 = length2 + 2;
                }
                byte[] bArr = new byte[i4];
                bArr[0] = 7;
                bArr[1] = (byte) i;
                if (i == length - 1) {
                    bArr[1] = -1;
                }
                System.arraycopy(hexStr2Byte, i3, bArr, 2, i4 - 2);
                arrayList.add(bArr);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static ArrayList<byte[]> createPushMsg(String str, int i) {
        ArrayList<byte[]> arrayList = new ArrayList<>();
        try {
            byte[] bytes = str.getBytes("utf-8");
            int length = bytes.length % 17 == 0 ? bytes.length / 17 : (bytes.length / 17) + 1;
            if (length > 10) {
                length = 10;
            }
            int i2 = 0;
            int i3 = 0;
            while (i2 < length) {
                byte[] bArr = new byte[20];
                bArr[0] = 115;
                int i4 = i3 + 1;
                bArr[1] = (byte) i3;
                bArr[2] = (byte) i;
                int i5 = i2 * 17;
                int length2 = bytes.length - i5;
                if (length2 >= 17) {
                    length2 = 17;
                }
                System.arraycopy(bytes, i5, bArr, 3, length2);
                arrayList.add(bArr);
                i2++;
                i3 = i4;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static byte[] createRemindEnable(RemindSetting remindSetting) {
        return new byte[]{2, 2, remindSetting.skypeEnable ? (byte) 1 : (byte) 0, remindSetting.lineEnable ? (byte) 1 : (byte) 0, (byte) remindSetting.longSitTime, remindSetting.longSitEnable ? (byte) 1 : (byte) 0, remindSetting.callEnable ? (byte) 1 : (byte) 0, remindSetting.messageEnable ? (byte) 1 : (byte) 0, remindSetting.wechatEnable ? (byte) 1 : (byte) 0, remindSetting.qqEnable ? (byte) 1 : (byte) 0, (byte) (((remindSetting.viberEnable ? 1 : 0) << 1) | (remindSetting.kakaoEnable ? 1 : 0)), remindSetting.facebookEnable ? (byte) 1 : (byte) 0, remindSetting.twitterEnable ? (byte) 1 : (byte) 0, remindSetting.whatappEnable ? (byte) 1 : (byte) 0, remindSetting.linkedEnable ? (byte) 1 : (byte) 0, remindSetting.dynamicHr ? (byte) 1 : (byte) 0, remindSetting.lightScreen ? (byte) 1 : (byte) 0, remindSetting.hrCycle ? (byte) 1 : (byte) 0, (byte) remindSetting.hrCheckDuration};
    }

    public static byte[] createTakePhoto(boolean z) {
        return new byte[]{82, z ? (byte) 1 : (byte) 0};
    }

    public static byte[] createUser() {
        return new byte[]{2, 1, 1, -12, 20, -86, 70, 1, 0, 1, 0};
    }

    public static byte[] currentTime() {
        byte[] bArr = new byte[12];
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        bArr[0] = 1;
        bArr[1] = (byte) ((currentTimeMillis >> 24) & 255);
        bArr[2] = (byte) ((currentTimeMillis >> 16) & 255);
        bArr[3] = (byte) ((currentTimeMillis >> 8) & 255);
        bArr[4] = (byte) (currentTimeMillis & 255);
        long offset = Calendar.getInstance().getTimeZone().getOffset(new Date().getTime()) / 1000;
        bArr[5] = (byte) ((offset >> 24) & 255);
        bArr[6] = (byte) ((offset >> 16) & 255);
        bArr[7] = (byte) ((offset >> 8) & 255);
        bArr[8] = (byte) (offset & 255);
        bArr[9] = (byte) (DateFormat.is24HourFormat(MainApplication.getContext()) ? 1 : 2);
        bArr[10] = LanguageUtil.getLanguageCode();
        bArr[11] = YCAppUtils.isChainess() ? (byte) 1 : (byte) 0;
        return bArr;
    }

    public static byte[] devFunction() {
        return new byte[]{3, 0};
    }

    public static byte[] measure(boolean z, int i) {
        return new byte[]{96, (byte) i, z ? (byte) 1 : (byte) 0};
    }

    public static byte[] resetSys() {
        return new byte[]{113, 1, 2, 3};
    }

    private static String reverse(String str) {
        byte[] hexStr2Byte = BleUtil.hexStr2Byte(str);
        byte[] bArr = new byte[2];
        if (hexStr2Byte.length == 1) {
            bArr[1] = hexStr2Byte[0];
            hexStr2Byte = bArr;
        }
        return BleUtil.byte2HexStr(BleUtil.reverse(hexStr2Byte));
    }

    public static byte[] unitSet(int i) {
        return new byte[]{17, (byte) i};
    }
}
