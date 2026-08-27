package no.nordicsemi.android.dfu.internal;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import no.nordicsemi.android.dfu.internal.exception.HexFileValidationException;

/* loaded from: classes2.dex */
public class HexInputStream extends FilterInputStream {
    private final int LINE_LENGTH;
    private final int MBRSize;
    private int available;
    private int bytesRead;
    private int lastAddress;
    private final byte[] localBuf;
    private int localPos;
    private int pos;
    private int size;

    public HexInputStream(InputStream inputStream, int i) throws HexFileValidationException, IOException {
        super(new BufferedInputStream(inputStream));
        this.LINE_LENGTH = 128;
        this.localBuf = new byte[128];
        this.localPos = 128;
        this.size = this.localBuf.length;
        this.lastAddress = 0;
        this.MBRSize = i;
        this.available = calculateBinSize(i);
    }

    public HexInputStream(byte[] bArr, int i) throws HexFileValidationException, IOException {
        super(new ByteArrayInputStream(bArr));
        this.LINE_LENGTH = 128;
        this.localBuf = new byte[128];
        this.localPos = 128;
        this.size = this.localBuf.length;
        this.lastAddress = 0;
        this.MBRSize = i;
        this.available = calculateBinSize(i);
    }

    private int asciiToInt(int i) {
        if (i >= 65) {
            return i - 55;
        }
        if (i >= 48) {
            return i - 48;
        }
        return -1;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0023. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:14:0x006e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0066 A[ADDED_TO_REGION, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int calculateBinSize(int r10) throws java.io.IOException {
        /*
            r9 = this;
            java.io.InputStream r0 = r9.in
            int r1 = r0.available()
            r0.mark(r1)
            int r1 = r0.read()     // Catch: java.lang.Throwable -> L73
            r2 = 0
            r3 = 0
        Lf:
            r9.checkComma(r1)     // Catch: java.lang.Throwable -> L73
            int r1 = r9.readByte(r0)     // Catch: java.lang.Throwable -> L73
            int r4 = r9.readAddress(r0)     // Catch: java.lang.Throwable -> L73
            int r5 = r9.readByte(r0)     // Catch: java.lang.Throwable -> L73
            r6 = 2
            r8 = 4
            if (r5 == r8) goto L50
            switch(r5) {
                case 0: goto L43;
                case 1: goto L3f;
                case 2: goto L27;
                default: goto L26;
            }     // Catch: java.lang.Throwable -> L73
        L26:
            goto L47
        L27:
            int r1 = r9.readAddress(r0)     // Catch: java.lang.Throwable -> L73
            int r1 = r1 << r8
            if (r3 <= 0) goto L3a
            int r4 = r1 >> 16
            int r2 = r2 >> 16
            int r2 = r2 + 1
            if (r4 == r2) goto L3a
            r0.reset()
            return r3
        L3a:
            r9.skip(r0, r6)     // Catch: java.lang.Throwable -> L73
        L3d:
            r2 = r1
            goto L66
        L3f:
            r0.reset()
            return r3
        L43:
            int r4 = r4 + r2
            if (r4 < r10) goto L47
            int r3 = r3 + r1
        L47:
            int r1 = r1 * 2
            int r1 = r1 + 2
            long r4 = (long) r1
            r9.skip(r0, r4)     // Catch: java.lang.Throwable -> L73
            goto L66
        L50:
            int r1 = r9.readAddress(r0)     // Catch: java.lang.Throwable -> L73
            if (r3 <= 0) goto L60
            int r2 = r2 >> 16
            int r2 = r2 + 1
            if (r1 == r2) goto L60
            r0.reset()
            return r3
        L60:
            int r1 = r1 << 16
            r9.skip(r0, r6)     // Catch: java.lang.Throwable -> L73
            goto L3d
        L66:
            int r1 = r0.read()     // Catch: java.lang.Throwable -> L73
            r4 = 10
            if (r1 == r4) goto L66
            r4 = 13
            if (r1 == r4) goto L66
            goto Lf
        L73:
            r10 = move-exception
            r0.reset()
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: no.nordicsemi.android.dfu.internal.HexInputStream.calculateBinSize(int):int");
    }

    private void checkComma(int i) throws HexFileValidationException {
        if (i != 58) {
            throw new HexFileValidationException("Not a HEX file");
        }
    }

    private int readAddress(InputStream inputStream) throws IOException {
        return readByte(inputStream) | (readByte(inputStream) << 8);
    }

    private int readByte(InputStream inputStream) throws IOException {
        return asciiToInt(inputStream.read()) | (asciiToInt(inputStream.read()) << 4);
    }

    private int readLine() throws IOException {
        if (this.pos == -1) {
            return 0;
        }
        InputStream inputStream = this.in;
        while (true) {
            int read = inputStream.read();
            this.pos++;
            if (read != 10 && read != 13) {
                checkComma(read);
                int readByte = readByte(inputStream);
                this.pos += 2;
                int readAddress = readAddress(inputStream);
                this.pos += 4;
                int readByte2 = readByte(inputStream);
                this.pos += 2;
                if (readByte2 != 4) {
                    switch (readByte2) {
                        case 0:
                            if (this.lastAddress + readAddress < this.MBRSize) {
                                this.pos = (int) (this.pos + skip(inputStream, (readByte * 2) + 2));
                                readByte2 = -1;
                                break;
                            }
                            break;
                        case 1:
                            this.pos = -1;
                            return 0;
                        case 2:
                            int readAddress2 = readAddress(inputStream) << 4;
                            this.pos += 4;
                            if (this.bytesRead > 0 && (readAddress2 >> 16) != (this.lastAddress >> 16) + 1) {
                                return 0;
                            }
                            this.lastAddress = readAddress2;
                            this.pos = (int) (this.pos + skip(inputStream, 2L));
                            break;
                            break;
                        default:
                            this.pos = (int) (this.pos + skip(inputStream, (readByte * 2) + 2));
                            break;
                    }
                } else {
                    int readAddress3 = readAddress(inputStream);
                    this.pos += 4;
                    if (this.bytesRead > 0 && readAddress3 != (this.lastAddress >> 16) + 1) {
                        return 0;
                    }
                    this.lastAddress = readAddress3 << 16;
                    this.pos = (int) (this.pos + skip(inputStream, 2L));
                }
                if (readByte2 == 0) {
                    for (int i = 0; i < this.localBuf.length && i < readByte; i++) {
                        int readByte3 = readByte(inputStream);
                        this.pos += 2;
                        this.localBuf[i] = (byte) readByte3;
                    }
                    this.pos = (int) (this.pos + skip(inputStream, 2L));
                    this.localPos = 0;
                    return readByte;
                }
            }
        }
    }

    private long skip(InputStream inputStream, long j) throws IOException {
        long skip = inputStream.skip(j);
        return skip < j ? skip + inputStream.skip(j - skip) : skip;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() {
        return this.available - this.bytesRead;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        throw new UnsupportedOperationException("Please, use readPacket() method instead");
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        return readPacket(bArr);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        throw new UnsupportedOperationException("Please, use readPacket() method instead");
    }

    public int readPacket(byte[] bArr) throws HexFileValidationException, IOException {
        int i = 0;
        while (i < bArr.length) {
            if (this.localPos < this.size) {
                byte[] bArr2 = this.localBuf;
                int i2 = this.localPos;
                this.localPos = i2 + 1;
                bArr[i] = bArr2[i2];
                i++;
            } else {
                int i3 = this.bytesRead;
                int readLine = readLine();
                this.size = readLine;
                this.bytesRead = i3 + readLine;
                if (this.size == 0) {
                    break;
                }
            }
        }
        return i;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        super.reset();
        this.pos = 0;
        this.bytesRead = 0;
        this.localPos = 0;
    }

    public int sizeInBytes() {
        return this.available;
    }

    public int sizeInPackets(int i) throws IOException {
        int sizeInBytes = sizeInBytes();
        return (sizeInBytes / i) + (sizeInBytes % i > 0 ? 1 : 0);
    }
}
