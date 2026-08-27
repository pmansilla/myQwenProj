package com.sun.mail.util;

import com.alibaba.fastjson.asm.Opcodes;
import com.autonavi.amap.mapcore.tools.GlMapUtil;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class UUDecoderStream extends FilterInputStream {
    private byte[] buffer;
    private int bufsize;
    private boolean gotEnd;
    private boolean gotPrefix;
    private boolean ignoreErrors;
    private boolean ignoreMissingBeginEnd;
    private int index;
    private LineInputStream lin;
    private int mode;
    private String name;
    private String readAhead;

    public UUDecoderStream(InputStream inputStream) {
        super(inputStream);
        this.buffer = new byte[45];
        this.bufsize = 0;
        this.index = 0;
        this.gotPrefix = false;
        this.gotEnd = false;
        this.lin = new LineInputStream(inputStream);
        this.ignoreErrors = PropUtil.getBooleanSystemProperty("mail.mime.uudecode.ignoreerrors", false);
        this.ignoreMissingBeginEnd = PropUtil.getBooleanSystemProperty("mail.mime.uudecode.ignoremissingbeginend", false);
    }

    public UUDecoderStream(InputStream inputStream, boolean z, boolean z2) {
        super(inputStream);
        this.buffer = new byte[45];
        this.bufsize = 0;
        this.index = 0;
        this.gotPrefix = false;
        this.gotEnd = false;
        this.lin = new LineInputStream(inputStream);
        this.ignoreErrors = z;
        this.ignoreMissingBeginEnd = z2;
    }

    private boolean decode() throws IOException {
        String readLine;
        if (this.gotEnd) {
            return false;
        }
        this.bufsize = 0;
        while (true) {
            if (this.readAhead != null) {
                readLine = this.readAhead;
                this.readAhead = null;
            } else {
                readLine = this.lin.readLine();
            }
            if (readLine == null) {
                if (!this.ignoreMissingBeginEnd) {
                    throw new DecodingException("UUDecoder: Missing end at EOF");
                }
                this.gotEnd = true;
                return false;
            }
            if (readLine.equals("end")) {
                this.gotEnd = true;
                return false;
            }
            if (readLine.length() != 0) {
                char charAt = readLine.charAt(0);
                if (charAt >= ' ') {
                    int i = (charAt - ' ') & 63;
                    if (i == 0) {
                        String readLine2 = this.lin.readLine();
                        if ((readLine2 == null || !readLine2.equals("end")) && !this.ignoreMissingBeginEnd) {
                            throw new DecodingException("UUDecoder: Missing End after count 0 line");
                        }
                        this.gotEnd = true;
                        return false;
                    }
                    if (readLine.length() >= (((i * 8) + 5) / 6) + 1) {
                        int i2 = 1;
                        while (this.bufsize < i) {
                            int i3 = i2 + 1;
                            byte charAt2 = (byte) ((readLine.charAt(i2) - ' ') & 63);
                            int i4 = i3 + 1;
                            byte charAt3 = (byte) ((readLine.charAt(i3) - ' ') & 63);
                            byte[] bArr = this.buffer;
                            int i5 = this.bufsize;
                            this.bufsize = i5 + 1;
                            bArr[i5] = (byte) (((charAt2 << 2) & 252) | ((charAt3 >>> 4) & 3));
                            if (this.bufsize < i) {
                                i2 = i4 + 1;
                                byte charAt4 = (byte) ((readLine.charAt(i4) - ' ') & 63);
                                byte[] bArr2 = this.buffer;
                                int i6 = this.bufsize;
                                this.bufsize = i6 + 1;
                                bArr2[i6] = (byte) (((charAt3 << 4) & GlMapUtil.DEVICE_DISPLAY_DPI_MEDIAN) | ((charAt4 >>> 2) & 15));
                                charAt3 = charAt4;
                            } else {
                                i2 = i4;
                            }
                            if (this.bufsize < i) {
                                int i7 = i2 + 1;
                                byte charAt5 = (byte) ((readLine.charAt(i2) - ' ') & 63);
                                byte[] bArr3 = this.buffer;
                                int i8 = this.bufsize;
                                this.bufsize = i8 + 1;
                                bArr3[i8] = (byte) ((charAt5 & 63) | ((charAt3 << 6) & Opcodes.CHECKCAST));
                                i2 = i7;
                            }
                        }
                        return true;
                    }
                    if (!this.ignoreErrors) {
                        throw new DecodingException("UUDecoder: Short buffer error");
                    }
                } else if (!this.ignoreErrors) {
                    throw new DecodingException("UUDecoder: Buffer format error");
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x00b2, code lost:
    
        r8.readAhead = r0;
        r8.gotPrefix = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00b6, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readPrefix() throws java.io.IOException {
        /*
            r8 = this;
            boolean r0 = r8.gotPrefix
            if (r0 == 0) goto L5
            return
        L5:
            r0 = 438(0x1b6, float:6.14E-43)
            r8.mode = r0
            java.lang.String r0 = "encoder.buf"
            r8.name = r0
        Ld:
            com.sun.mail.util.LineInputStream r0 = r8.lin
            java.lang.String r0 = r0.readLine()
            r7 = 1
            if (r0 != 0) goto L28
            boolean r0 = r8.ignoreMissingBeginEnd
            if (r0 == 0) goto L20
            r8.gotPrefix = r7
            r8.gotEnd = r7
            goto Lb6
        L20:
            com.sun.mail.util.DecodingException r0 = new com.sun.mail.util.DecodingException
            java.lang.String r1 = "UUDecoder: Missing begin"
            r0.<init>(r1)
            throw r0
        L28:
            r2 = 0
            r3 = 0
            java.lang.String r4 = "begin"
            r5 = 0
            r6 = 5
            r1 = r0
            boolean r1 = r1.regionMatches(r2, r3, r4, r5, r6)
            r2 = 6
            if (r1 == 0) goto L90
            r1 = 9
            java.lang.String r1 = r0.substring(r2, r1)     // Catch: java.lang.NumberFormatException -> L43
            int r1 = java.lang.Integer.parseInt(r1)     // Catch: java.lang.NumberFormatException -> L43
            r8.mode = r1     // Catch: java.lang.NumberFormatException -> L43
            goto L48
        L43:
            r1 = move-exception
            boolean r2 = r8.ignoreErrors
            if (r2 == 0) goto L75
        L48:
            int r1 = r0.length()
            r2 = 10
            if (r1 <= r2) goto L57
            java.lang.String r0 = r0.substring(r2)
            r8.name = r0
            goto L5b
        L57:
            boolean r1 = r8.ignoreErrors
            if (r1 == 0) goto L5e
        L5b:
            r8.gotPrefix = r7
            goto Lb6
        L5e:
            com.sun.mail.util.DecodingException r1 = new com.sun.mail.util.DecodingException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "UUDecoder: Missing name: "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            r1.<init>(r0)
            throw r1
        L75:
            com.sun.mail.util.DecodingException r0 = new com.sun.mail.util.DecodingException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "UUDecoder: Error in mode: "
            r2.append(r3)
            java.lang.String r1 = r1.toString()
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            r0.<init>(r1)
            throw r0
        L90:
            boolean r1 = r8.ignoreMissingBeginEnd
            if (r1 == 0) goto Ld
            int r1 = r0.length()
            if (r1 == 0) goto Ld
            r1 = 0
            char r1 = r0.charAt(r1)
            int r1 = r1 + (-32)
            r1 = r1 & 63
            int r1 = r1 * 8
            int r1 = r1 + 5
            int r1 = r1 / r2
            if (r1 == 0) goto Lb2
            int r2 = r0.length()
            int r1 = r1 + 1
            if (r2 < r1) goto Ld
        Lb2:
            r8.readAhead = r0
            r8.gotPrefix = r7
        Lb6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.util.UUDecoderStream.readPrefix():void");
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        return ((this.in.available() * 3) / 4) + (this.bufsize - this.index);
    }

    public int getMode() throws IOException {
        readPrefix();
        return this.mode;
    }

    public String getName() throws IOException {
        readPrefix();
        return this.name;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.index >= this.bufsize) {
            readPrefix();
            if (!decode()) {
                return -1;
            }
            this.index = 0;
        }
        byte[] bArr = this.buffer;
        int i = this.index;
        this.index = i + 1;
        return bArr[i] & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        int i3 = 0;
        while (i3 < i2) {
            int read = read();
            if (read == -1) {
                if (i3 == 0) {
                    return -1;
                }
                return i3;
            }
            bArr[i + i3] = (byte) read;
            i3++;
        }
        return i3;
    }
}
