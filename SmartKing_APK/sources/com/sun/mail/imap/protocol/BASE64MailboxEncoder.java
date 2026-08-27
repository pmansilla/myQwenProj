package com.sun.mail.imap.protocol;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;

/* loaded from: classes2.dex */
public class BASE64MailboxEncoder {
    private static final char[] pem_array = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', ','};
    protected Writer out;
    protected byte[] buffer = new byte[4];
    protected int bufsize = 0;
    protected boolean started = false;

    public BASE64MailboxEncoder(Writer writer) {
        this.out = null;
        this.out = writer;
    }

    public static String encode(String str) {
        char[] charArray = str.toCharArray();
        CharArrayWriter charArrayWriter = new CharArrayWriter(charArray.length);
        BASE64MailboxEncoder bASE64MailboxEncoder = null;
        boolean z = false;
        for (char c : charArray) {
            if (c < ' ' || c > '~') {
                if (bASE64MailboxEncoder == null) {
                    bASE64MailboxEncoder = new BASE64MailboxEncoder(charArrayWriter);
                    z = true;
                }
                bASE64MailboxEncoder.write(c);
            } else {
                if (bASE64MailboxEncoder != null) {
                    bASE64MailboxEncoder.flush();
                }
                if (c == '&') {
                    charArrayWriter.write(38);
                    charArrayWriter.write(45);
                    z = true;
                } else {
                    charArrayWriter.write(c);
                }
            }
        }
        if (bASE64MailboxEncoder != null) {
            bASE64MailboxEncoder.flush();
        }
        return z ? charArrayWriter.toString() : str;
    }

    protected void encode() throws IOException {
        if (this.bufsize == 1) {
            byte b = this.buffer[0];
            this.out.write(pem_array[(b >>> 2) & 63]);
            this.out.write(pem_array[((b << 4) & 48) + 0]);
            return;
        }
        if (this.bufsize == 2) {
            byte b2 = this.buffer[0];
            byte b3 = this.buffer[1];
            this.out.write(pem_array[(b2 >>> 2) & 63]);
            this.out.write(pem_array[((b2 << 4) & 48) + ((b3 >>> 4) & 15)]);
            this.out.write(pem_array[((b3 << 2) & 60) + 0]);
            return;
        }
        byte b4 = this.buffer[0];
        byte b5 = this.buffer[1];
        byte b6 = this.buffer[2];
        this.out.write(pem_array[(b4 >>> 2) & 63]);
        this.out.write(pem_array[((b4 << 4) & 48) + ((b5 >>> 4) & 15)]);
        this.out.write(pem_array[((b5 << 2) & 60) + ((b6 >>> 6) & 3)]);
        this.out.write(pem_array[b6 & 63]);
        if (this.bufsize == 4) {
            this.buffer[0] = this.buffer[3];
        }
    }

    public void flush() {
        try {
            if (this.bufsize > 0) {
                encode();
                this.bufsize = 0;
            }
            if (this.started) {
                this.out.write(45);
                this.started = false;
            }
        } catch (IOException unused) {
        }
    }

    public void write(int i) {
        try {
            if (!this.started) {
                this.started = true;
                this.out.write(38);
            }
            byte[] bArr = this.buffer;
            int i2 = this.bufsize;
            this.bufsize = i2 + 1;
            bArr[i2] = (byte) (i >> 8);
            byte[] bArr2 = this.buffer;
            int i3 = this.bufsize;
            this.bufsize = i3 + 1;
            bArr2[i3] = (byte) (i & 255);
            if (this.bufsize >= 3) {
                encode();
                this.bufsize -= 3;
            }
        } catch (IOException unused) {
        }
    }
}
