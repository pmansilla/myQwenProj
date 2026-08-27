package com.sun.mail.iap;

import com.sun.mail.util.ASCIIUtility;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.apache.commons.lang.CharUtils;

/* loaded from: classes2.dex */
public class Response {
    private static String ASTRING_CHAR_DELIM = " (){%*\"\\";
    private static String ATOM_CHAR_DELIM = " (){%*\"\\]";
    public static final int BAD = 12;
    public static final int BYE = 16;
    public static final int CONTINUATION = 1;
    public static final int NO = 8;
    public static final int OK = 4;
    public static final int SYNTHETIC = 32;
    public static final int TAGGED = 2;
    public static final int TAG_MASK = 3;
    public static final int TYPE_MASK = 28;
    public static final int UNTAGGED = 3;
    private static final int increment = 100;
    protected byte[] buffer;
    protected Exception ex;
    protected int index;
    protected int pindex;
    protected int size;
    protected String tag;
    protected int type;
    protected boolean utf8;

    public Response(Protocol protocol) throws IOException, ProtocolException {
        this.buffer = null;
        this.type = 0;
        this.tag = null;
        this.buffer = protocol.getInputStream().readResponse(protocol.getResponseBuffer()).getBytes();
        this.size = r0.getCount() - 2;
        this.utf8 = protocol.supportsUtf8();
        parse();
    }

    public Response(Response response) {
        this.buffer = null;
        this.type = 0;
        this.tag = null;
        this.index = response.index;
        this.pindex = response.pindex;
        this.size = response.size;
        this.buffer = response.buffer;
        this.type = response.type;
        this.tag = response.tag;
        this.ex = response.ex;
        this.utf8 = response.utf8;
    }

    public Response(String str) {
        this(str, true);
    }

    public Response(String str, boolean z) {
        this.buffer = null;
        this.type = 0;
        this.tag = null;
        if (z) {
            this.buffer = str.getBytes(StandardCharsets.UTF_8);
        } else {
            this.buffer = str.getBytes(StandardCharsets.US_ASCII);
        }
        this.size = this.buffer.length;
        this.utf8 = z;
        parse();
    }

    public static Response byeResponse(Exception exc) {
        Response response = new Response(("* BYE JavaMail Exception: " + exc.toString()).replace(CharUtils.CR, ' ').replace('\n', ' '));
        response.type = response.type | 32;
        response.ex = exc;
        return response;
    }

    private void parse() {
        this.index = 0;
        if (this.size == 0) {
            return;
        }
        if (this.buffer[this.index] == 43) {
            this.type |= 1;
            this.index++;
            return;
        }
        if (this.buffer[this.index] == 42) {
            this.type |= 3;
            this.index++;
        } else {
            this.type |= 2;
            this.tag = readAtom();
            if (this.tag == null) {
                this.tag = "";
            }
        }
        int i = this.index;
        String readAtom = readAtom();
        if (readAtom == null) {
            readAtom = "";
        }
        if (readAtom.equalsIgnoreCase("OK")) {
            this.type |= 4;
        } else if (readAtom.equalsIgnoreCase("NO")) {
            this.type |= 8;
        } else if (readAtom.equalsIgnoreCase("BAD")) {
            this.type |= 12;
        } else if (readAtom.equalsIgnoreCase("BYE")) {
            this.type |= 16;
        } else {
            this.index = i;
        }
        this.pindex = this.index;
    }

    private Object parseString(boolean z, boolean z2) {
        byte b;
        skipSpaces();
        byte b2 = this.buffer[this.index];
        if (b2 == 34) {
            this.index++;
            int i = this.index;
            int i2 = this.index;
            while (this.index < this.size && (b = this.buffer[this.index]) != 34) {
                if (b == 92) {
                    this.index++;
                }
                if (this.index != i2) {
                    this.buffer[i2] = this.buffer[this.index];
                }
                i2++;
                this.index++;
            }
            if (this.index >= this.size) {
                return null;
            }
            this.index++;
            return z2 ? toString(this.buffer, i, i2) : new ByteArray(this.buffer, i, i2 - i);
        }
        if (b2 != 123) {
            if (z) {
                return z2 ? readDelimString(ASTRING_CHAR_DELIM) : new ByteArray(this.buffer, this.index, this.index);
            }
            if (b2 != 78 && b2 != 110) {
                return null;
            }
            this.index += 3;
            return null;
        }
        int i3 = this.index + 1;
        this.index = i3;
        while (this.buffer[this.index] != 125) {
            this.index++;
        }
        try {
            int parseInt = ASCIIUtility.parseInt(this.buffer, i3, this.index);
            int i4 = this.index + 3;
            int i5 = i4 + parseInt;
            this.index = i5;
            return z2 ? toString(this.buffer, i4, i5) : new ByteArray(this.buffer, i4, parseInt);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private String readDelimString(String str) {
        int i;
        skipSpaces();
        if (this.index >= this.size) {
            return null;
        }
        int i2 = this.index;
        while (this.index < this.size && (i = this.buffer[this.index] & 255) >= 32 && str.indexOf((char) i) < 0 && i != 127) {
            this.index++;
        }
        return toString(this.buffer, i2, this.index);
    }

    private String[] readStringList(boolean z) {
        skipSpaces();
        if (this.buffer[this.index] != 40) {
            return null;
        }
        this.index++;
        ArrayList arrayList = new ArrayList();
        while (!isNextNonSpace(')')) {
            arrayList.add(z ? readAtomString() : readString());
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private String toString(byte[] bArr, int i, int i2) {
        return this.utf8 ? new String(bArr, i, i2 - i, StandardCharsets.UTF_8) : ASCIIUtility.toString(bArr, i, i2);
    }

    public Exception getException() {
        return this.ex;
    }

    public String getRest() {
        skipSpaces();
        return toString(this.buffer, this.index, this.size);
    }

    public String getTag() {
        return this.tag;
    }

    public int getType() {
        return this.type;
    }

    public boolean isBAD() {
        return (this.type & 28) == 12;
    }

    public boolean isBYE() {
        return (this.type & 28) == 16;
    }

    public boolean isContinuation() {
        return (this.type & 3) == 1;
    }

    public boolean isNO() {
        return (this.type & 28) == 8;
    }

    public boolean isNextNonSpace(char c) {
        skipSpaces();
        if (this.index >= this.size || this.buffer[this.index] != ((byte) c)) {
            return false;
        }
        this.index++;
        return true;
    }

    public boolean isOK() {
        return (this.type & 28) == 4;
    }

    public boolean isSynthetic() {
        return (this.type & 32) == 32;
    }

    public boolean isTagged() {
        return (this.type & 3) == 2;
    }

    public boolean isUnTagged() {
        return (this.type & 3) == 3;
    }

    public byte peekByte() {
        if (this.index < this.size) {
            return this.buffer[this.index];
        }
        return (byte) 0;
    }

    public String readAtom() {
        return readDelimString(ATOM_CHAR_DELIM);
    }

    public String readAtomString() {
        return (String) parseString(true, true);
    }

    public String[] readAtomStringList() {
        return readStringList(true);
    }

    public byte readByte() {
        if (this.index >= this.size) {
            return (byte) 0;
        }
        byte[] bArr = this.buffer;
        int i = this.index;
        this.index = i + 1;
        return bArr[i];
    }

    public ByteArray readByteArray() {
        if (!isContinuation()) {
            return (ByteArray) parseString(false, false);
        }
        skipSpaces();
        return new ByteArray(this.buffer, this.index, this.size - this.index);
    }

    public ByteArrayInputStream readBytes() {
        ByteArray readByteArray = readByteArray();
        if (readByteArray != null) {
            return readByteArray.toByteArrayInputStream();
        }
        return null;
    }

    public long readLong() {
        skipSpaces();
        int i = this.index;
        while (this.index < this.size && Character.isDigit((char) this.buffer[this.index])) {
            this.index++;
        }
        if (this.index <= i) {
            return -1L;
        }
        try {
            return ASCIIUtility.parseLong(this.buffer, i, this.index);
        } catch (NumberFormatException unused) {
            return -1L;
        }
    }

    public int readNumber() {
        skipSpaces();
        int i = this.index;
        while (this.index < this.size && Character.isDigit((char) this.buffer[this.index])) {
            this.index++;
        }
        if (this.index <= i) {
            return -1;
        }
        try {
            return ASCIIUtility.parseInt(this.buffer, i, this.index);
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    public String readString() {
        return (String) parseString(false, true);
    }

    public String readString(char c) {
        skipSpaces();
        if (this.index >= this.size) {
            return null;
        }
        int i = this.index;
        while (this.index < this.size && this.buffer[this.index] != c) {
            this.index++;
        }
        return toString(this.buffer, i, this.index);
    }

    public String[] readStringList() {
        return readStringList(false);
    }

    public void reset() {
        this.index = this.pindex;
    }

    public void skip(int i) {
        this.index += i;
    }

    public void skipSpaces() {
        while (this.index < this.size && this.buffer[this.index] == 32) {
            this.index++;
        }
    }

    public void skipToken() {
        while (this.index < this.size && this.buffer[this.index] != 32) {
            this.index++;
        }
    }

    public boolean supportsUtf8() {
        return this.utf8;
    }

    public String toString() {
        return toString(this.buffer, 0, this.size);
    }
}
