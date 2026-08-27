package com.czw.utils;

import android.support.v4.view.MotionEventCompat;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
public class ByteUtil {
    public static byte[] getBytes(char c) {
        return new byte[]{(byte) c, (byte) (c >> '\b')};
    }

    public static byte[] getBytes(double d) {
        return getBytes(Double.doubleToLongBits(d));
    }

    public static byte[] getBytes(float f) {
        return getBytes(Float.floatToIntBits(f));
    }

    public static byte[] getBytes(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((65280 & i) >> 8), (byte) ((16711680 & i) >> 16), (byte) ((i & (-16777216)) >> 24)};
    }

    public static byte[] getBytes(long j) {
        return new byte[]{(byte) (j & 255), (byte) ((j >> 8) & 255), (byte) ((j >> 16) & 255), (byte) ((j >> 24) & 255), (byte) ((j >> 32) & 255), (byte) ((j >> 40) & 255), (byte) ((j >> 48) & 255), (byte) ((j >> 56) & 255)};
    }

    public static byte[] getBytes(String str) {
        return getBytes(str, "GBK");
    }

    public static byte[] getBytes(String str, String str2) {
        return str.getBytes(Charset.forName(str2));
    }

    public static byte[] getBytes(short s) {
        return new byte[]{(byte) (s & 255), (byte) ((s & 65280) >> 8)};
    }

    public static char getChar(byte[] bArr) {
        return (char) (((bArr[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (bArr[0] & 255));
    }

    public static double getDouble(byte[] bArr) {
        long j = getLong(bArr);
        System.out.println(j);
        return Double.longBitsToDouble(j);
    }

    public static float getFloat(byte[] bArr) {
        return Float.intBitsToFloat(getInt(bArr));
    }

    public static int getInt(byte[] bArr) {
        return ((bArr[3] << 24) & (-16777216)) | (bArr[0] & 255) | ((bArr[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | ((bArr[2] << 16) & 16711680);
    }

    public static long getLong(byte[] bArr) {
        return (bArr[0] & 255) | ((bArr[1] << 8) & 65280) | ((bArr[2] << 16) & 16711680) | ((bArr[3] << 24) & 4278190080L) | ((bArr[4] << 32) & 1095216660480L) | ((bArr[5] << 40) & 280375465082880L) | ((bArr[6] << 48) & 71776119061217280L) | ((bArr[7] << 56) & (-72057594037927936L));
    }

    public static short getShort(byte[] bArr) {
        return (short) (((bArr[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (bArr[0] & 255));
    }

    public static String getString(byte[] bArr) {
        return getString(bArr, "GBK");
    }

    public static String getString(byte[] bArr, String str) {
        return new String(bArr, Charset.forName(str));
    }

    public static void main(String[] strArr) {
        System.out.println(122);
        System.out.println(122);
        System.out.println(1222222L);
        System.out.println('a');
        System.out.println(122.22f);
        System.out.println(122.22d);
        System.out.println("我是好孩子");
        System.out.println("**************");
        System.out.println(getFloat(getBytes(1.23f)));
    }
}
