package com.sun.mail.iap;

import com.sun.mail.util.ASCIIUtility;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class Argument {
    protected List<Object> items = new ArrayList(1);

    private void astring(byte[] bArr, Protocol protocol) throws IOException, ProtocolException {
        nastring(bArr, protocol, false);
    }

    private void literal(Literal literal, Protocol protocol) throws IOException, ProtocolException {
        literal.writeTo(startLiteral(protocol, literal.size()));
    }

    private void literal(ByteArrayOutputStream byteArrayOutputStream, Protocol protocol) throws IOException, ProtocolException {
        byteArrayOutputStream.writeTo(startLiteral(protocol, byteArrayOutputStream.size()));
    }

    private void literal(byte[] bArr, Protocol protocol) throws IOException, ProtocolException {
        startLiteral(protocol, bArr.length).write(bArr);
    }

    private void nastring(byte[] bArr, Protocol protocol, boolean z) throws IOException, ProtocolException {
        int i;
        DataOutputStream dataOutputStream = (DataOutputStream) protocol.getOutputStream();
        int length = bArr.length;
        if (length > 1024) {
            literal(bArr, protocol);
            return;
        }
        boolean z2 = true;
        boolean z3 = length == 0 ? true : z;
        boolean supportsUtf8 = protocol.supportsUtf8();
        boolean z4 = z3;
        boolean z5 = false;
        for (byte b : bArr) {
            if (b == 0 || b == 13 || b == 10 || (!supportsUtf8 && (b & 255) > 127)) {
                literal(bArr, protocol);
                return;
            }
            if (b == 42 || b == 37 || b == 40 || b == 41 || b == 123 || b == 34 || b == 92 || (i = b & 255) <= 32 || i > 127) {
                if (b == 34 || b == 92) {
                    z4 = true;
                    z5 = true;
                } else {
                    z4 = true;
                }
            }
        }
        if (z4 || bArr.length != 3 || ((bArr[0] != 78 && bArr[0] != 110) || ((bArr[1] != 73 && bArr[1] != 105) || (bArr[2] != 76 && bArr[2] != 108)))) {
            z2 = z4;
        }
        if (z2) {
            dataOutputStream.write(34);
        }
        if (z5) {
            for (byte b2 : bArr) {
                if (b2 == 34 || b2 == 92) {
                    dataOutputStream.write(92);
                }
                dataOutputStream.write(b2);
            }
        } else {
            dataOutputStream.write(bArr);
        }
        if (z2) {
            dataOutputStream.write(34);
        }
    }

    private void nstring(byte[] bArr, Protocol protocol) throws IOException, ProtocolException {
        if (bArr == null) {
            ((DataOutputStream) protocol.getOutputStream()).writeBytes("NIL");
        } else {
            nastring(bArr, protocol, true);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x003f, code lost:
    
        throw new com.sun.mail.iap.LiteralException(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0040, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0026, code lost:
    
        if (r1 == false) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0028, code lost:
    
        r5 = r4.readResponse();
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0030, code lost:
    
        if (r5.isContinuation() == false) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0037, code lost:
    
        if (r5.isTagged() != false) goto L18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.io.OutputStream startLiteral(com.sun.mail.iap.Protocol r4, int r5) throws java.io.IOException, com.sun.mail.iap.ProtocolException {
        /*
            r3 = this;
            java.io.OutputStream r0 = r4.getOutputStream()
            java.io.DataOutputStream r0 = (java.io.DataOutputStream) r0
            boolean r1 = r4.supportsNonSyncLiterals()
            r2 = 123(0x7b, float:1.72E-43)
            r0.write(r2)
            java.lang.String r5 = java.lang.Integer.toString(r5)
            r0.writeBytes(r5)
            if (r1 == 0) goto L1e
            java.lang.String r5 = "+}\r\n"
            r0.writeBytes(r5)
            goto L23
        L1e:
            java.lang.String r5 = "}\r\n"
            r0.writeBytes(r5)
        L23:
            r0.flush()
            if (r1 != 0) goto L40
        L28:
            com.sun.mail.iap.Response r5 = r4.readResponse()
            boolean r1 = r5.isContinuation()
            if (r1 == 0) goto L33
            goto L40
        L33:
            boolean r1 = r5.isTagged()
            if (r1 != 0) goto L3a
            goto L28
        L3a:
            com.sun.mail.iap.LiteralException r4 = new com.sun.mail.iap.LiteralException
            r4.<init>(r5)
            throw r4
        L40:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.iap.Argument.startLiteral(com.sun.mail.iap.Protocol, int):java.io.OutputStream");
    }

    public Argument append(Argument argument) {
        this.items.addAll(argument.items);
        return this;
    }

    public void write(Protocol protocol) throws IOException, ProtocolException {
        int size = this.items != null ? this.items.size() : 0;
        DataOutputStream dataOutputStream = (DataOutputStream) protocol.getOutputStream();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                dataOutputStream.write(32);
            }
            Object obj = this.items.get(i);
            if (obj instanceof Atom) {
                dataOutputStream.writeBytes(((Atom) obj).string);
            } else if (obj instanceof Number) {
                dataOutputStream.writeBytes(((Number) obj).toString());
            } else if (obj instanceof AString) {
                astring(((AString) obj).bytes, protocol);
            } else if (obj instanceof NString) {
                nstring(((NString) obj).bytes, protocol);
            } else if (obj instanceof byte[]) {
                literal((byte[]) obj, protocol);
            } else if (obj instanceof ByteArrayOutputStream) {
                literal((ByteArrayOutputStream) obj, protocol);
            } else if (obj instanceof Literal) {
                literal((Literal) obj, protocol);
            } else if (obj instanceof Argument) {
                dataOutputStream.write(40);
                ((Argument) obj).write(protocol);
                dataOutputStream.write(41);
            }
        }
    }

    public Argument writeArgument(Argument argument) {
        this.items.add(argument);
        return this;
    }

    public Argument writeAtom(String str) {
        this.items.add(new Atom(str));
        return this;
    }

    public Argument writeBytes(Literal literal) {
        this.items.add(literal);
        return this;
    }

    public Argument writeBytes(ByteArrayOutputStream byteArrayOutputStream) {
        this.items.add(byteArrayOutputStream);
        return this;
    }

    public Argument writeBytes(byte[] bArr) {
        this.items.add(bArr);
        return this;
    }

    public Argument writeNString(String str) {
        if (str == null) {
            this.items.add(new NString(null));
        } else {
            this.items.add(new NString(ASCIIUtility.getBytes(str)));
        }
        return this;
    }

    public Argument writeNString(String str, String str2) throws UnsupportedEncodingException {
        if (str == null) {
            this.items.add(new NString(null));
        } else if (str2 == null) {
            writeString(str);
        } else {
            this.items.add(new NString(str.getBytes(str2)));
        }
        return this;
    }

    public Argument writeNString(String str, Charset charset) {
        if (str == null) {
            this.items.add(new NString(null));
        } else if (charset == null) {
            writeString(str);
        } else {
            this.items.add(new NString(str.getBytes(charset)));
        }
        return this;
    }

    public Argument writeNumber(int i) {
        this.items.add(Integer.valueOf(i));
        return this;
    }

    public Argument writeNumber(long j) {
        this.items.add(Long.valueOf(j));
        return this;
    }

    public Argument writeString(String str) {
        this.items.add(new AString(ASCIIUtility.getBytes(str)));
        return this;
    }

    public Argument writeString(String str, String str2) throws UnsupportedEncodingException {
        if (str2 == null) {
            writeString(str);
        } else {
            this.items.add(new AString(str.getBytes(str2)));
        }
        return this;
    }

    public Argument writeString(String str, Charset charset) {
        if (charset == null) {
            writeString(str);
        } else {
            this.items.add(new AString(str.getBytes(charset)));
        }
        return this;
    }
}
