package ycble.runchinaup.util;

import com.amap.location.common.model.AmapLoc;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import ycble.runchinaup.BleCfg;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes2.dex */
public class BleUtil {
    private static String Decrypt(Object obj) {
        byte[] hexStr2Byte = hexStr2Byte("ACED0005740000");
        new ByteArrayInputStream(hexStr2Byte);
        System.out.println(new String(hexStr2Byte));
        return "";
    }

    public static int bin2Int(String str) {
        int i = 0;
        for (char c : str.replace(SQLBuilder.BLANK, "").toCharArray()) {
            i = (i * 2) + (c == '1' ? 1 : 0);
        }
        return i;
    }

    public static boolean[] bit2Week(String str) {
        boolean[] zArr = new boolean[7];
        int i = 0;
        while (i < 7) {
            int i2 = i + 1;
            zArr[6 - i] = str.substring(i, i2).equals(AmapLoc.RESULT_TYPE_WIFI_ONLY);
            i = i2;
        }
        return zArr;
    }

    public static String byte2HexStr(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            sb.append(String.format("%02X", Integer.valueOf(b & 255)));
        }
        return sb.toString();
    }

    public static String byte2HexStr(byte[] bArr, String str) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            sb.append(String.format("%02X", Integer.valueOf(b & 255)));
            sb.append(str);
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static int byte2IntLR(byte... bArr) {
        int length = bArr.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            i |= (bArr[i2] & 255) << (((length - i2) - 1) * 8);
        }
        return i;
    }

    public static int byte2IntRL(byte... bArr) {
        int length = bArr.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            i |= (bArr[i2] & 255) << (i2 * 8);
        }
        return i;
    }

    public static int byteStr2IntLR(String str) {
        return byte2IntLR(hexStr2Byte(str));
    }

    public static int byteStr2IntRL(String str) {
        return byte2IntRL(hexStr2Byte(str));
    }

    public static String debug(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (byte b : bArr) {
            sb.append(String.format("%02X,", Integer.valueOf(b & 255)));
        }
        StringBuilder deleteCharAt = sb.deleteCharAt(sb.length() - 1);
        deleteCharAt.append("]");
        return deleteCharAt.toString();
    }

    public static void debug(String str, byte[] bArr) {
        ycBleLog.e(BleCfg.npBleTag + str + debug(bArr));
    }

    private static String encryption(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(byteArrayOutputStream).writeObject(obj);
            System.out.println(byte2HexStr(byteArrayOutputStream.toByteArray()));
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIncrementedAddress(String str) {
        return str.substring(0, 15) + String.format("%02X", Integer.valueOf((Integer.valueOf(str.substring(15), 16).intValue() + 1) & 255));
    }

    public static byte[] hexStr2Byte(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        int i = 0;
        while (i < length) {
            int i2 = i + 2;
            bArr[i / 2] = (byte) (Integer.parseInt(str.substring(i, i2), 16) + 0);
            i = i2;
        }
        return bArr;
    }

    public static String int2Bit(int i) {
        return String.format("%08d", Integer.valueOf(Integer.toBinaryString(i)));
    }

    public static byte[] int2ByteArrLR(int i, int i2) {
        byte[] bArr = new byte[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = (i2 - 1) - i3;
            bArr[i3] = (byte) ((((255 << i4) & i) >> (i4 * 8)) & 255);
        }
        return bArr;
    }

    public static byte[] int2ByteArrRL(int i, int i2) {
        byte[] int2ByteArrLR = int2ByteArrLR(i, i2);
        byte[] bArr = new byte[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            bArr[(i2 - 1) - i3] = int2ByteArrLR[i3];
        }
        return bArr;
    }

    public static void main(String[] strArr) {
        encryption("");
        Decrypt(null);
    }

    public static byte[] reverse(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        int i = 0;
        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        while (true) {
            length--;
            if (length < 0) {
                return bArr2;
            }
            bArr2[length] = bArr[i];
            i++;
        }
    }

    public static byte[] reverse(byte[] bArr, int i, int i2) {
        while (i < i2) {
            byte b = bArr[i2];
            bArr[i2] = bArr[i];
            bArr[i] = b;
            i++;
            i2--;
        }
        return bArr;
    }

    public static String week2Bit(boolean[] zArr) {
        String str = "";
        for (int i = 6; i >= 0; i--) {
            str = zArr[i] ? str + AmapLoc.RESULT_TYPE_WIFI_ONLY : str + AmapLoc.RESULT_TYPE_GPS;
        }
        return str;
    }
}
