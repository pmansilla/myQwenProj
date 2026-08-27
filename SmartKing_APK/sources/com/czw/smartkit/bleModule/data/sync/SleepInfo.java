package com.czw.smartkit.bleModule.data.sync;

import android.text.TextUtils;
import com.czw.smartkit.util.SkUtils;
import com.czw.utils.DateUtil;
import java.util.ArrayList;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes.dex */
public class SleepInfo {
    private String date;
    private long dateTime;
    private int timeLength = 0;
    private int sleepLevel = 1;
    private String hexData = null;

    public static int[] calculSleepTime(String str) {
        int[] iArr = new int[4];
        if (TextUtils.isEmpty(str) || str.length() < 30) {
            return iArr;
        }
        ArrayList<SleepInfo> parserFromData = parserFromData(BleUtil.hexStr2Byte(handMuliteDataFormat(str)));
        byte[] hexStr2Byte = BleUtil.hexStr2Byte(handMuliteDataFormat(str));
        parserFromData.addAll(parserFromData(hexStr2Byte));
        int length = hexStr2Byte.length / 15;
        for (int i = 0; i < length; i++) {
            byte[] bArr = new byte[15];
            System.arraycopy(hexStr2Byte, i * 15, bArr, 0, 15);
            parserFromData.addAll(parserFromData(bArr));
        }
        for (SleepInfo sleepInfo : parserFromData) {
            switch (sleepInfo.getSleepLevel()) {
                case 1:
                    iArr[0] = iArr[0] + sleepInfo.timeLength;
                    break;
                case 2:
                    iArr[1] = iArr[1] + sleepInfo.timeLength;
                    break;
                case 3:
                    iArr[2] = iArr[2] + sleepInfo.timeLength;
                    break;
            }
        }
        iArr[3] = iArr[0] + iArr[1];
        return iArr;
    }

    private static String getFormatData(String str) {
        if (str.length() == 30) {
            return str.toString();
        }
        if (str.length() <= 30) {
            return "";
        }
        int i = 0;
        StringBuilder sb = new StringBuilder(str.substring(0, 30));
        String substring = str.substring(30);
        int byte2IntRL = BleUtil.byte2IntRL(BleUtil.hexStr2Byte(substring.substring(2, 6)));
        int length = substring.length() / 24;
        System.out.println(length + "--->");
        while (i < length) {
            byte2IntRL++;
            int i2 = i * 24;
            i++;
            String substring2 = substring.substring(i2, i * 24);
            if (substring2.equals("000000000000000000000000")) {
                sb.append("95FFFF");
                sb.append(substring2);
            } else {
                sb.append("95");
                sb.append(BleUtil.byte2HexStr(BleUtil.int2ByteArrLR(byte2IntRL, 2)));
                sb.append(substring2);
            }
        }
        return sb.toString();
    }

    public static String handMuliteDataFormat(String str) {
        StringBuilder sb = new StringBuilder();
        String froamtStrFroSleepData = SkUtils.froamtStrFroSleepData(str);
        if (froamtStrFroSleepData.startsWith("[") && froamtStrFroSleepData.endsWith("]")) {
            for (String str2 : froamtStrFroSleepData.replace("[", "").replace("]", "").split(",")) {
                sb.append(String.format("%02X", Integer.valueOf(str2)));
            }
        } else {
            for (String str3 : froamtStrFroSleepData.split(",")) {
                sb.append(str3);
            }
        }
        String upperCase = getFormatData(sb.toString()).toUpperCase();
        System.out.println(upperCase);
        return upperCase.toString();
    }

    public static ArrayList<SleepInfo> parserFromData(byte[] bArr) {
        ArrayList<SleepInfo> arrayList = new ArrayList<>();
        String substring = BleUtil.byte2HexStr(bArr).substring(0, 6);
        SleepInfo sleepInfo = new SleepInfo();
        sleepInfo.setHexData(substring + BleUtil.byte2HexStr(bArr).substring(6, 18));
        sleepInfo.setDateTime((long) BleUtil.byte2IntLR(bArr[3], bArr[4], bArr[5], bArr[6]));
        sleepInfo.setDate(DateUtil.yyyyMMdd.format(Long.valueOf(sleepInfo.getDateTime() * 1000)) + " 00:00:00");
        sleepInfo.setTimeLength(BleUtil.byte2IntLR(bArr[7]));
        sleepInfo.setSleepLevel(BleUtil.byte2IntLR(bArr[8]));
        arrayList.add(sleepInfo);
        SleepInfo sleepInfo2 = new SleepInfo();
        sleepInfo2.setHexData(substring + BleUtil.byte2HexStr(bArr).substring(18, 30));
        sleepInfo2.setDateTime((long) BleUtil.byte2IntLR(bArr[9], bArr[10], bArr[11], bArr[12]));
        sleepInfo2.setTimeLength(BleUtil.byte2IntLR(bArr[13]));
        sleepInfo2.setSleepLevel(BleUtil.byte2IntLR(bArr[14]));
        sleepInfo2.setDate(DateUtil.yyyyMMdd.format(Long.valueOf(sleepInfo2.getDateTime() * 1000)) + " 00:00:00");
        arrayList.add(sleepInfo2);
        return arrayList;
    }

    public String getDate() {
        return this.date;
    }

    public long getDateTime() {
        return this.dateTime;
    }

    public String getHexData() {
        return this.hexData;
    }

    public int getSleepLevel() {
        return this.sleepLevel;
    }

    public int getTimeLength() {
        return this.timeLength;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public void setDateTime(long j) {
        this.dateTime = j;
    }

    public void setHexData(String str) {
        this.hexData = str;
    }

    public void setSleepLevel(int i) {
        this.sleepLevel = i;
    }

    public void setTimeLength(int i) {
        this.timeLength = i;
    }

    public String toString() {
        return "SleepInfo{dateTime=" + this.dateTime + ", timeLength=" + this.timeLength + ", sleepLevel=" + this.sleepLevel + ", hexData='" + this.hexData + "', date='" + this.date + "'}";
    }
}
