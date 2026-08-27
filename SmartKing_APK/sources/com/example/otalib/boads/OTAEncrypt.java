package com.example.otalib.boads;

/* loaded from: classes.dex */
public class OTAEncrypt {
    private static int rx_dat_count;
    private static int tx_dat_count;

    public static void otas_decrypt(byte[] bArr, byte[] bArr2, int i, byte[] bArr3) {
        for (int i2 = 0; i2 < i; i2++) {
            byte b = bArr[i2];
            int i3 = rx_dat_count;
            rx_dat_count = i3 + 1;
            bArr2[i2] = (byte) (b ^ bArr3[i3 % 32]);
        }
    }

    public static void otas_encrypt(byte[] bArr, byte[] bArr2, int i, byte[] bArr3) {
        for (int i2 = 0; i2 < i; i2++) {
            byte b = bArr[i2];
            int i3 = tx_dat_count;
            tx_dat_count = i3 + 1;
            bArr2[i2] = (byte) (b ^ bArr3[i3 % 32]);
        }
    }

    public static void setRx_dat_count(int i) {
        rx_dat_count = i;
    }

    public static void setTx_dat_count(int i) {
        tx_dat_count = i;
    }
}
