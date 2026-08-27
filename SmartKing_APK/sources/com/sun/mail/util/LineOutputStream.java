package com.sun.mail.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/* loaded from: classes2.dex */
public class LineOutputStream extends FilterOutputStream {
    private static byte[] newline = new byte[2];
    private boolean allowutf8;

    static {
        newline[0] = 13;
        newline[1] = 10;
    }

    public LineOutputStream(OutputStream outputStream) {
        this(outputStream, false);
    }

    public LineOutputStream(OutputStream outputStream, boolean z) {
        super(outputStream);
        this.allowutf8 = z;
    }

    public void writeln() throws IOException {
        this.out.write(newline);
    }

    public void writeln(String str) throws IOException {
        this.out.write(this.allowutf8 ? str.getBytes(StandardCharsets.UTF_8) : ASCIIUtility.getBytes(str));
        this.out.write(newline);
    }
}
