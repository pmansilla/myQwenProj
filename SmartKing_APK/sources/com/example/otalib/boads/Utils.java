package com.example.otalib.boads;

import android.content.Context;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Semaphore;

/* loaded from: classes.dex */
public class Utils {
    public static boolean UI_ONLY_SURPPORT_LED = false;
    private Semaphore write_log_lock = new Semaphore(1);

    public static int MSB_Bytearray_2_Int4(byte[] bArr) {
        if (bArr == null || bArr.length != 4) {
            return 0;
        }
        return ((bArr[0] & 255) << 24) | (bArr[3] & 255) | ((bArr[2] & 255) << 8) | ((bArr[1] & 255) << 16);
    }

    public static int bytearray_to_int4(byte[] bArr) {
        if (bArr == null || bArr.length != 4) {
            return 0;
        }
        return ((bArr[3] & 255) << 24) | (bArr[0] & 255) | ((bArr[1] & 255) << 8) | ((bArr[2] & 255) << 16);
    }

    public static boolean bytearraycmp(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null || bArr.length != bArr2.length) {
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString + SQLBuilder.BLANK);
        }
        return sb.toString();
    }

    public static float convertDpToPixel(Context context, float f) {
        return f * (context.getResources().getDisplayMetrics().densityDpi / 160.0f);
    }

    public static int convertDpToPixelInt(Context context, float f) {
        return (int) (f * (context.getResources().getDisplayMetrics().densityDpi / 160.0f));
    }

    public static String hexStr2Str(String str) {
        char[] charArray = str.toCharArray();
        byte[] bArr = new byte[str.length() / 2];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) ((("0123456789ABCDEF".indexOf(charArray[i2]) * 16) + "0123456789ABCDEF".indexOf(charArray[i2 + 1])) & 255);
        }
        return new String(bArr);
    }

    public static void int4_to_bytearray(int i, byte[] bArr) {
        if (bArr == null || bArr.length < 4) {
            return;
        }
        bArr[0] = (byte) (i & 255);
        bArr[1] = (byte) ((i >> 8) & 255);
        bArr[2] = (byte) ((i >> 16) & 255);
        bArr[3] = (byte) ((i >> 24) & 255);
    }

    public static void java_memset(byte[] bArr, int i, byte b, int i2) {
        if (bArr.length < i + i2) {
            return;
        }
        while (i < i2) {
            bArr[i] = 0;
            i++;
        }
    }

    public static String str2HexStr(String str) {
        char[] charArray = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bytes = str.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(charArray[(bytes[i] & 240) >> 4]);
            sb.append(charArray[bytes[i] & 15]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    public static void swap_byte_array(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return;
        }
        int length = bArr.length - 1;
        for (int i = 0; i < bArr.length / 2; i++) {
            byte b = bArr[i];
            bArr[i] = bArr[length];
            bArr[length] = b;
            length--;
        }
    }

    public static byte[] toByteArray(String str) {
        if (str.isEmpty()) {
            throw new IllegalArgumentException("this hexString must not be empty");
        }
        String lowerCase = str.toLowerCase();
        byte[] bArr = new byte[lowerCase.length() / 2];
        int i = 0;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) ((((byte) (Character.digit(lowerCase.charAt(i), 16) & 255)) << 4) | ((byte) (Character.digit(lowerCase.charAt(i + 1), 16) & 255)));
            i += 2;
        }
        return bArr;
    }

    public static void uint16_to_bytearray(long j, byte[] bArr) {
        if (bArr == null || bArr.length < 2) {
            return;
        }
        bArr[0] = (byte) (j & 255);
        bArr[1] = (byte) ((j >> 8) & 255);
    }

    public static void uint32_to_bytearray(long j, byte[] bArr) {
        if (bArr == null || bArr.length < 4) {
            return;
        }
        bArr[0] = (byte) (j & 255);
        bArr[1] = (byte) ((j >> 8) & 255);
        bArr[2] = (byte) ((j >> 16) & 255);
        bArr[3] = (byte) ((j >> 24) & 255);
    }

    public float convertPixelsToDp(Context context, float f) {
        return f / (context.getResources().getDisplayMetrics().densityDpi / 160.0f);
    }

    public Properties loadConfig(Context context, String str) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public byte[] readSDFile(String str) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(str));
        byte[] bArr = new byte[fileInputStream.available()];
        fileInputStream.read(bArr);
        fileInputStream.close();
        return bArr;
    }

    public void saveConfig(Context context, String str, Properties properties) {
        try {
            properties.store(new FileOutputStream(str, false), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeSDFile(String str, String str2) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
        fileOutputStream.write(str2.getBytes());
        fileOutputStream.close();
    }

    public void writeSDFile(String str, byte[] bArr) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
        fileOutputStream.write(bArr);
        fileOutputStream.close();
    }

    public void writeSDFileAppend(String str, String str2) {
        try {
            this.write_log_lock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            FileWriter fileWriter = new FileWriter(str, true);
            fileWriter.write(str2);
            fileWriter.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        this.write_log_lock.release();
    }
}
