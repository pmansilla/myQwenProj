package com.sun.mail.iap;

import com.sun.mail.util.ASCIIUtility;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class ResponseInputStream {
    private static final int incrementSlop = 16;
    private static final int maxIncrement = 262144;
    private static final int minIncrement = 256;
    private BufferedInputStream bin;

    public ResponseInputStream(InputStream inputStream) {
        this.bin = new BufferedInputStream(inputStream, 2048);
    }

    public int available() throws IOException {
        return this.bin.available();
    }

    public ByteArray readResponse() throws IOException {
        return readResponse(null);
    }

    public ByteArray readResponse(ByteArray byteArray) throws IOException {
        int i;
        if (byteArray == null) {
            byteArray = new ByteArray(new byte[128], 0, 128);
        }
        byte[] bytes = byteArray.getBytes();
        int i2 = 0;
        while (true) {
            byte[] bArr = bytes;
            i = i2;
            boolean z = false;
            int i3 = 0;
            while (!z && (i3 = this.bin.read()) != -1) {
                if (i3 == 10 && i > 0 && bArr[i - 1] == 13) {
                    z = true;
                }
                if (i >= bArr.length) {
                    int length = bArr.length;
                    if (length > 262144) {
                        length = 262144;
                    }
                    byteArray.grow(length);
                    bArr = byteArray.getBytes();
                }
                bArr[i] = (byte) i3;
                i++;
            }
            if (i3 == -1) {
                throw new IOException("Connection dropped by server?");
            }
            if (i < 5) {
                break;
            }
            int i4 = i - 3;
            if (bArr[i4] != 125) {
                break;
            }
            int i5 = i - 4;
            while (i5 >= 0 && bArr[i5] != 123) {
                i5--;
            }
            if (i5 < 0) {
                break;
            }
            try {
                int parseInt = ASCIIUtility.parseInt(bArr, i5 + 1, i4);
                if (parseInt > 0) {
                    int length2 = bArr.length - i;
                    int i6 = parseInt + 16;
                    if (i6 > length2) {
                        int i7 = i6 - length2;
                        byteArray.grow(256 <= i7 ? i7 : 256);
                        bArr = byteArray.getBytes();
                    }
                    while (parseInt > 0) {
                        int read = this.bin.read(bArr, i, parseInt);
                        if (read == -1) {
                            throw new IOException("Connection dropped by server?");
                        }
                        parseInt -= read;
                        i += read;
                    }
                }
                i2 = i;
                bytes = bArr;
            } catch (NumberFormatException unused) {
            }
        }
        byteArray.setCount(i);
        return byteArray;
    }
}
