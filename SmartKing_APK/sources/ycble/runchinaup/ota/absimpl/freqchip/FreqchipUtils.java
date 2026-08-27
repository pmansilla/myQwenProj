package ycble.runchinaup.ota.absimpl.freqchip;

import android.support.v4.view.MotionEventCompat;

/* loaded from: classes2.dex */
class FreqchipUtils {
    public static final int OTA_CMD_CHIP_ERASE = 4;
    public static final int OTA_CMD_GET_STR_BASE = 1;
    public static final int OTA_CMD_NULL = 10;
    public static final int OTA_CMD_PAGE_ERASE = 3;
    public static final int OTA_CMD_READ_DATA = 6;
    public static final int OTA_CMD_READ_MEM = 8;
    public static final int OTA_CMD_REBOOT = 9;
    public static final int OTA_CMD_WRITE_DATA = 5;
    public static final int OTA_CMD_WRITE_MEM = 7;

    FreqchipUtils() {
    }

    public static byte[] byteMerger(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public static int bytetoint(byte[] bArr) {
        return ((bArr[7] & 255) << 24) | (bArr[4] & 255) | ((bArr[5] & 255) << 8) | ((bArr[6] & 255) << 16);
    }

    public static byte[] cmd_operation(int i, int i2, int i3) {
        if (i == 7 || i == 5) {
            return cmd_write_op(i, 9, i3, i2);
        }
        if (i == 1) {
            return cmd_write_op(i, 3, 0, 0);
        }
        if (i == 3) {
            return cmd_write_op(i, 7, i3, 0);
        }
        return null;
    }

    private static byte[] cmd_write_op(int i, int i2, int i3, int i4) {
        byte[] bArr = i == 3 ? new byte[7] : new byte[9];
        bArr[0] = (byte) (i & 255);
        int i5 = i2 & 255;
        bArr[1] = (byte) i5;
        bArr[2] = (byte) (i5 >> 8);
        bArr[3] = (byte) (i3 & 255);
        bArr[4] = (byte) ((i3 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
        bArr[5] = (byte) ((16711680 & i3) >> 16);
        bArr[6] = (byte) ((i3 & (-16777216)) >> 24);
        if (i != 3) {
            bArr[7] = (byte) (i4 & 255);
            bArr[8] = (byte) ((i4 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
        }
        return bArr;
    }
}
