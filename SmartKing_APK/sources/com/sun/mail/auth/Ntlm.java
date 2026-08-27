package com.sun.mail.auth;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.MailLogger;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/* loaded from: classes2.dex */
public class Ntlm {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private Cipher cipher;
    private SecretKeyFactory fac;
    private String hostname;
    private MailLogger logger;
    private MD4 md4;
    private String ntdomain;
    private String password;
    private byte[] type1;
    private byte[] type3;
    private String username;

    public Ntlm(String str, String str2, String str3, String str4, MailLogger mailLogger) {
        int indexOf = str2.indexOf(46);
        str2 = indexOf != -1 ? str2.substring(0, indexOf) : str2;
        int indexOf2 = str3.indexOf(92);
        if (indexOf2 != -1) {
            str = str3.substring(0, indexOf2).toUpperCase(Locale.ENGLISH);
            str3 = str3.substring(indexOf2 + 1);
        } else if (str == null) {
            str = "";
        }
        this.ntdomain = str;
        this.hostname = str2;
        this.username = str3;
        this.password = str4;
        this.logger = mailLogger.getLogger(getClass(), "DEBUG NTLM");
        init0();
    }

    private byte[] calcLMHash() throws GeneralSecurityException {
        byte[] bArr;
        byte[] bArr2 = {75, 71, 83, 33, 64, 35, 36, 37};
        try {
            bArr = this.password.toUpperCase(Locale.ENGLISH).getBytes("iso-8859-1");
        } catch (UnsupportedEncodingException unused) {
            bArr = null;
        }
        byte[] bArr3 = new byte[14];
        int length = this.password.length();
        System.arraycopy(bArr, 0, bArr3, 0, length <= 14 ? length : 14);
        DESKeySpec dESKeySpec = new DESKeySpec(makeDesKey(bArr3, 0));
        DESKeySpec dESKeySpec2 = new DESKeySpec(makeDesKey(bArr3, 7));
        SecretKey generateSecret = this.fac.generateSecret(dESKeySpec);
        SecretKey generateSecret2 = this.fac.generateSecret(dESKeySpec2);
        this.cipher.init(1, generateSecret);
        byte[] doFinal = this.cipher.doFinal(bArr2, 0, 8);
        this.cipher.init(1, generateSecret2);
        byte[] doFinal2 = this.cipher.doFinal(bArr2, 0, 8);
        byte[] bArr4 = new byte[21];
        System.arraycopy(doFinal, 0, bArr4, 0, 8);
        System.arraycopy(doFinal2, 0, bArr4, 8, 8);
        return bArr4;
    }

    private byte[] calcNTHash() throws GeneralSecurityException {
        byte[] bArr;
        try {
            bArr = this.password.getBytes("UnicodeLittleUnmarked");
        } catch (UnsupportedEncodingException unused) {
            bArr = null;
        }
        byte[] bArr2 = new byte[21];
        System.arraycopy(this.md4.digest(bArr), 0, bArr2, 0, 16);
        return bArr2;
    }

    private byte[] calcResponse(byte[] bArr, byte[] bArr2) throws GeneralSecurityException {
        DESKeySpec dESKeySpec = new DESKeySpec(makeDesKey(bArr, 0));
        DESKeySpec dESKeySpec2 = new DESKeySpec(makeDesKey(bArr, 7));
        DESKeySpec dESKeySpec3 = new DESKeySpec(makeDesKey(bArr, 14));
        SecretKey generateSecret = this.fac.generateSecret(dESKeySpec);
        SecretKey generateSecret2 = this.fac.generateSecret(dESKeySpec2);
        SecretKey generateSecret3 = this.fac.generateSecret(dESKeySpec3);
        this.cipher.init(1, generateSecret);
        byte[] doFinal = this.cipher.doFinal(bArr2, 0, 8);
        this.cipher.init(1, generateSecret2);
        byte[] doFinal2 = this.cipher.doFinal(bArr2, 0, 8);
        this.cipher.init(1, generateSecret3);
        byte[] doFinal3 = this.cipher.doFinal(bArr2, 0, 8);
        byte[] bArr3 = new byte[24];
        System.arraycopy(doFinal, 0, bArr3, 0, 8);
        System.arraycopy(doFinal2, 0, bArr3, 8, 8);
        System.arraycopy(doFinal3, 0, bArr3, 16, 8);
        return bArr3;
    }

    private void copybytes(byte[] bArr, int i, String str, String str2) {
        try {
            byte[] bytes = str.getBytes(str2);
            System.arraycopy(bytes, 0, bArr, i, bytes.length);
        } catch (UnsupportedEncodingException unused) {
        }
    }

    private void init0() {
        this.type1 = new byte[256];
        this.type3 = new byte[256];
        System.arraycopy(new byte[]{78, 84, 76, 77, 83, 83, 80, 0, 1}, 0, this.type1, 0, 9);
        this.type1[12] = 3;
        this.type1[13] = -78;
        this.type1[28] = 32;
        System.arraycopy(new byte[]{78, 84, 76, 77, 83, 83, 80, 0, 3}, 0, this.type3, 0, 9);
        this.type3[12] = 24;
        this.type3[14] = 24;
        this.type3[20] = 24;
        this.type3[22] = 24;
        this.type3[32] = 64;
        this.type3[60] = 1;
        this.type3[61] = -126;
        try {
            this.fac = SecretKeyFactory.getInstance("DES");
            this.cipher = Cipher.getInstance("DES/ECB/NoPadding");
            this.md4 = new MD4();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException unused) {
        }
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:77)
        */
    private byte[] makeDesKey(byte[] r12, int r13) {
        /*
            r11 = this;
            int r0 = r12.length
            int[] r0 = new int[r0]
            r1 = 0
            r2 = 0
        L5:
            int r3 = r0.length
            if (r2 >= r3) goto L18
            r3 = r12[r2]
            if (r3 >= 0) goto L11
            r3 = r12[r2]
            int r3 = r3 + 256
            goto L13
        L11:
            r3 = r12[r2]
        L13:
            r0[r2] = r3
            int r2 = r2 + 1
            goto L5
        L18:
            r12 = 8
            byte[] r12 = new byte[r12]
            int r2 = r13 + 0
            r3 = r0[r2]
            byte r3 = (byte) r3
            r12[r1] = r3
            r1 = r0[r2]
            r2 = 7
            int r1 = r1 << r2
            r1 = r1 & 255(0xff, float:3.57E-43)
            int r3 = r13 + 1
            r4 = r0[r3]
            r5 = 1
            int r4 = r4 >> r5
            r1 = r1 | r4
            byte r1 = (byte) r1
            r12[r5] = r1
            r1 = r0[r3]
            r3 = 6
            int r1 = r1 << r3
            r1 = r1 & 255(0xff, float:3.57E-43)
            int r4 = r13 + 2
            r6 = r0[r4]
            r7 = 2
            int r6 = r6 >> r7
            r1 = r1 | r6
            byte r1 = (byte) r1
            r12[r7] = r1
            r1 = r0[r4]
            r4 = 5
            int r1 = r1 << r4
            r1 = r1 & 255(0xff, float:3.57E-43)
            int r6 = r13 + 3
            r8 = r0[r6]
            r9 = 3
            int r8 = r8 >> r9
            r1 = r1 | r8
            byte r1 = (byte) r1
            r12[r9] = r1
            r1 = r0[r6]
            r6 = 4
            int r1 = r1 << r6
            r1 = r1 & 255(0xff, float:3.57E-43)
            int r8 = r13 + 4
            r10 = r0[r8]
            int r10 = r10 >> r6
            r1 = r1 | r10
            byte r1 = (byte) r1
            r12[r6] = r1
            r1 = r0[r8]
            int r1 = r1 << r9
            r1 = r1 & 255(0xff, float:3.57E-43)
            int r6 = r13 + 5
            r8 = r0[r6]
            int r8 = r8 >> r4
            r1 = r1 | r8
            byte r1 = (byte) r1
            r12[r4] = r1
            r1 = r0[r6]
            int r1 = r1 << r7
            r1 = r1 & 255(0xff, float:3.57E-43)
            int r13 = r13 + r3
            r4 = r0[r13]
            int r4 = r4 >> r3
            r1 = r1 | r4
            byte r1 = (byte) r1
            r12[r3] = r1
            r13 = r0[r13]
            int r13 = r13 << r5
            r13 = r13 & 255(0xff, float:3.57E-43)
            byte r13 = (byte) r13
            r12[r2] = r13
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.auth.Ntlm.makeDesKey(byte[], int):byte[]");
    }

    private static String toHex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 3);
        for (int i = 0; i < bArr.length; i++) {
            stringBuffer.append(hex[(bArr[i] >> 4) & 15]);
            stringBuffer.append(hex[bArr[i] & 15]);
            stringBuffer.append(' ');
        }
        return stringBuffer.toString();
    }

    public String generateType1Msg(int i) {
        int length = this.ntdomain.length();
        this.type1[16] = (byte) (length % 256);
        this.type1[17] = (byte) (length / 256);
        this.type1[18] = this.type1[16];
        this.type1[19] = this.type1[17];
        if (length == 0) {
            byte[] bArr = this.type1;
            bArr[13] = (byte) (bArr[13] & (-17));
        }
        int length2 = this.hostname.length();
        this.type1[24] = (byte) (length2 % 256);
        this.type1[25] = (byte) (length2 / 256);
        this.type1[26] = this.type1[24];
        this.type1[27] = this.type1[25];
        copybytes(this.type1, 32, this.hostname, "iso-8859-1");
        int i2 = length2 + 32;
        copybytes(this.type1, i2, this.ntdomain, "iso-8859-1");
        this.type1[20] = (byte) (i2 % 256);
        this.type1[21] = (byte) (i2 / 256);
        int i3 = i2 + length;
        byte[] bArr2 = new byte[i3];
        System.arraycopy(this.type1, 0, bArr2, 0, i3);
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("type 1 message: " + toHex(bArr2));
        }
        try {
            return new String(BASE64EncoderStream.encode(bArr2), "iso-8859-1");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public String generateType3Msg(String str) {
        byte[] bArr;
        try {
            try {
                bArr = BASE64DecoderStream.decode(str.getBytes("us-ascii"));
            } catch (GeneralSecurityException e) {
                this.logger.log(Level.FINE, "GeneralSecurityException", (Throwable) e);
                return "";
            }
        } catch (UnsupportedEncodingException unused) {
            bArr = null;
        }
        byte[] bArr2 = new byte[8];
        System.arraycopy(bArr, 24, bArr2, 0, 8);
        int length = this.username.length() * 2;
        byte[] bArr3 = this.type3;
        byte b = (byte) (length % 256);
        this.type3[38] = b;
        bArr3[36] = b;
        byte[] bArr4 = this.type3;
        byte b2 = (byte) (length / 256);
        this.type3[39] = b2;
        bArr4[37] = b2;
        int length2 = this.ntdomain.length() * 2;
        byte[] bArr5 = this.type3;
        byte b3 = (byte) (length2 % 256);
        this.type3[30] = b3;
        bArr5[28] = b3;
        byte[] bArr6 = this.type3;
        byte b4 = (byte) (length2 / 256);
        this.type3[31] = b4;
        bArr6[29] = b4;
        int length3 = this.hostname.length() * 2;
        byte[] bArr7 = this.type3;
        byte b5 = (byte) (length3 % 256);
        this.type3[46] = b5;
        bArr7[44] = b5;
        byte[] bArr8 = this.type3;
        byte b6 = (byte) (length3 / 256);
        this.type3[47] = b6;
        bArr8[45] = b6;
        copybytes(this.type3, 64, this.ntdomain, "UnicodeLittleUnmarked");
        this.type3[32] = (byte) 64;
        this.type3[33] = (byte) 0;
        int i = length2 + 64;
        copybytes(this.type3, i, this.username, "UnicodeLittleUnmarked");
        this.type3[40] = (byte) (i % 256);
        this.type3[41] = (byte) (i / 256);
        int i2 = i + length;
        copybytes(this.type3, i2, this.hostname, "UnicodeLittleUnmarked");
        this.type3[48] = (byte) (i2 % 256);
        this.type3[49] = (byte) (i2 / 256);
        int i3 = i2 + length3;
        byte[] calcResponse = calcResponse(calcLMHash(), bArr2);
        byte[] calcResponse2 = calcResponse(calcNTHash(), bArr2);
        System.arraycopy(calcResponse, 0, this.type3, i3, 24);
        this.type3[16] = (byte) (i3 % 256);
        this.type3[17] = (byte) (i3 / 256);
        int i4 = i3 + 24;
        System.arraycopy(calcResponse2, 0, this.type3, i4, 24);
        this.type3[24] = (byte) (i4 % 256);
        this.type3[25] = (byte) (i4 / 256);
        int i5 = i4 + 24;
        this.type3[56] = (byte) (i5 % 256);
        this.type3[57] = (byte) (i5 / 256);
        byte[] bArr9 = new byte[i5];
        System.arraycopy(this.type3, 0, bArr9, 0, i5);
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine("type 3 message: " + toHex(bArr9));
        }
        try {
            return new String(BASE64EncoderStream.encode(bArr9), "iso-8859-1");
        } catch (UnsupportedEncodingException unused2) {
            return null;
        }
    }
}
