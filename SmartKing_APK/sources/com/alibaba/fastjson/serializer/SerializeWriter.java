package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import com.tencent.bugly.Bugly;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import kotlin.text.Typography;

/* loaded from: classes.dex */
public final class SerializeWriter extends Writer {
    private static final ThreadLocal<char[]> bufLocal = new ThreadLocal<>();
    private static final ThreadLocal<byte[]> bytesBufLocal = new ThreadLocal<>();
    static final int nonDirectFeatures = ((((((((SerializerFeature.UseSingleQuotes.mask | 0) | SerializerFeature.BrowserCompatible.mask) | SerializerFeature.PrettyFormat.mask) | SerializerFeature.WriteEnumUsingToString.mask) | SerializerFeature.WriteNonStringValueAsString.mask) | SerializerFeature.WriteSlashAsSpecial.mask) | SerializerFeature.IgnoreErrorGetter.mask) | SerializerFeature.WriteClassName.mask) | SerializerFeature.NotWriteDefaultValue.mask;
    protected boolean beanToArray;
    protected boolean browserSecure;
    protected char[] buf;
    protected int count;
    protected boolean disableCircularReferenceDetect;
    protected int features;
    protected char keySeperator;
    protected int maxBufSize;
    protected boolean notWriteDefaultValue;
    protected boolean quoteFieldNames;
    protected long sepcialBits;
    protected boolean sortField;
    protected boolean useSingleQuotes;
    protected boolean writeDirect;
    protected boolean writeEnumUsingName;
    protected boolean writeEnumUsingToString;
    protected boolean writeNonStringValueAsString;
    private final Writer writer;

    public SerializeWriter() {
        this((Writer) null);
    }

    public SerializeWriter(int i) {
        this((Writer) null, i);
    }

    public SerializeWriter(Writer writer) {
        this(writer, JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.EMPTY);
    }

    public SerializeWriter(Writer writer, int i) {
        this.maxBufSize = -1;
        this.writer = writer;
        if (i > 0) {
            this.buf = new char[i];
            computeFeatures();
        } else {
            throw new IllegalArgumentException("Negative initial size: " + i);
        }
    }

    public SerializeWriter(Writer writer, int i, SerializerFeature... serializerFeatureArr) {
        this.maxBufSize = -1;
        this.writer = writer;
        this.buf = bufLocal.get();
        if (this.buf != null) {
            bufLocal.set(null);
        } else {
            this.buf = new char[2048];
        }
        for (SerializerFeature serializerFeature : serializerFeatureArr) {
            i |= serializerFeature.getMask();
        }
        this.features = i;
        computeFeatures();
    }

    public SerializeWriter(Writer writer, SerializerFeature... serializerFeatureArr) {
        this(writer, 0, serializerFeatureArr);
    }

    public SerializeWriter(SerializerFeature... serializerFeatureArr) {
        this((Writer) null, serializerFeatureArr);
    }

    private int encodeToUTF8(OutputStream outputStream) throws IOException {
        double d = this.count;
        Double.isNaN(d);
        int i = (int) (d * 3.0d);
        byte[] bArr = bytesBufLocal.get();
        if (bArr == null) {
            bArr = new byte[8192];
            bytesBufLocal.set(bArr);
        }
        if (bArr.length < i) {
            bArr = new byte[i];
        }
        int encodeUTF8 = IOUtils.encodeUTF8(this.buf, 0, this.count, bArr);
        outputStream.write(bArr, 0, encodeUTF8);
        return encodeUTF8;
    }

    private byte[] encodeToUTF8Bytes() {
        double d = this.count;
        Double.isNaN(d);
        int i = (int) (d * 3.0d);
        byte[] bArr = bytesBufLocal.get();
        if (bArr == null) {
            bArr = new byte[8192];
            bytesBufLocal.set(bArr);
        }
        if (bArr.length < i) {
            bArr = new byte[i];
        }
        int encodeUTF8 = IOUtils.encodeUTF8(this.buf, 0, this.count, bArr);
        byte[] bArr2 = new byte[encodeUTF8];
        System.arraycopy(bArr, 0, bArr2, 0, encodeUTF8);
        return bArr2;
    }

    private void writeEnumFieldValue(char c, String str, String str2) {
        if (this.useSingleQuotes) {
            writeFieldValue(c, str, str2);
        } else {
            writeFieldValueStringWithDoubleQuote(c, str, str2);
        }
    }

    private void writeKeyWithSingleQuoteIfHasSpecial(String str) {
        byte[] bArr = IOUtils.specicalFlags_singleQuotes;
        int length = str.length();
        boolean z = true;
        int i = this.count + length + 1;
        int i2 = 0;
        if (i > this.buf.length) {
            if (this.writer != null) {
                if (length == 0) {
                    write(39);
                    write(39);
                    write(58);
                    return;
                }
                int i3 = 0;
                while (true) {
                    if (i3 < length) {
                        char charAt = str.charAt(i3);
                        if (charAt < bArr.length && bArr[charAt] != 0) {
                            break;
                        } else {
                            i3++;
                        }
                    } else {
                        z = false;
                        break;
                    }
                }
                if (z) {
                    write(39);
                }
                while (i2 < length) {
                    char charAt2 = str.charAt(i2);
                    if (charAt2 >= bArr.length || bArr[charAt2] == 0) {
                        write(charAt2);
                    } else {
                        write(92);
                        write(IOUtils.replaceChars[charAt2]);
                    }
                    i2++;
                }
                if (z) {
                    write(39);
                }
                write(58);
                return;
            }
            expandCapacity(i);
        }
        if (length == 0) {
            if (this.count + 3 > this.buf.length) {
                expandCapacity(this.count + 3);
            }
            char[] cArr = this.buf;
            int i4 = this.count;
            this.count = i4 + 1;
            cArr[i4] = '\'';
            char[] cArr2 = this.buf;
            int i5 = this.count;
            this.count = i5 + 1;
            cArr2[i5] = '\'';
            char[] cArr3 = this.buf;
            int i6 = this.count;
            this.count = i6 + 1;
            cArr3[i6] = ':';
            return;
        }
        int i7 = this.count;
        int i8 = i7 + length;
        str.getChars(0, length, this.buf, i7);
        this.count = i;
        int i9 = i7;
        boolean z2 = false;
        while (i9 < i8) {
            char c = this.buf[i9];
            if (c < bArr.length && bArr[c] != 0) {
                if (z2) {
                    i++;
                    if (i > this.buf.length) {
                        expandCapacity(i);
                    }
                    this.count = i;
                    int i10 = i9 + 1;
                    System.arraycopy(this.buf, i10, this.buf, i9 + 2, i8 - i9);
                    this.buf[i9] = '\\';
                    this.buf[i10] = IOUtils.replaceChars[c];
                    i8++;
                    i9 = i10;
                } else {
                    i += 3;
                    if (i > this.buf.length) {
                        expandCapacity(i);
                    }
                    this.count = i;
                    int i11 = i9 + 1;
                    System.arraycopy(this.buf, i11, this.buf, i9 + 3, (i8 - i9) - 1);
                    System.arraycopy(this.buf, i2, this.buf, 1, i9);
                    this.buf[i7] = '\'';
                    this.buf[i11] = '\\';
                    int i12 = i11 + 1;
                    this.buf[i12] = IOUtils.replaceChars[c];
                    i8 += 2;
                    this.buf[this.count - 2] = '\'';
                    i9 = i12;
                    z2 = true;
                }
            }
            i9++;
            i2 = 0;
        }
        this.buf[i - 1] = ':';
    }

    @Override // java.io.Writer, java.lang.Appendable
    public SerializeWriter append(char c) {
        write(c);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public SerializeWriter append(CharSequence charSequence) {
        String charSequence2 = charSequence == null ? "null" : charSequence.toString();
        write(charSequence2, 0, charSequence2.length());
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public SerializeWriter append(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            charSequence = "null";
        }
        String charSequence2 = charSequence.subSequence(i, i2).toString();
        write(charSequence2, 0, charSequence2.length());
        return this;
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.writer != null && this.count > 0) {
            flush();
        }
        if (this.buf.length <= 131072) {
            bufLocal.set(this.buf);
        }
        this.buf = null;
    }

    protected void computeFeatures() {
        this.quoteFieldNames = (this.features & SerializerFeature.QuoteFieldNames.mask) != 0;
        this.useSingleQuotes = (this.features & SerializerFeature.UseSingleQuotes.mask) != 0;
        this.sortField = (this.features & SerializerFeature.SortField.mask) != 0;
        this.disableCircularReferenceDetect = (this.features & SerializerFeature.DisableCircularReferenceDetect.mask) != 0;
        this.beanToArray = (this.features & SerializerFeature.BeanToArray.mask) != 0;
        this.writeNonStringValueAsString = (this.features & SerializerFeature.WriteNonStringValueAsString.mask) != 0;
        this.notWriteDefaultValue = (this.features & SerializerFeature.NotWriteDefaultValue.mask) != 0;
        this.writeEnumUsingName = (this.features & SerializerFeature.WriteEnumUsingName.mask) != 0;
        this.writeEnumUsingToString = (this.features & SerializerFeature.WriteEnumUsingToString.mask) != 0;
        this.writeDirect = this.quoteFieldNames && (this.features & nonDirectFeatures) == 0 && (this.beanToArray || this.writeEnumUsingName);
        this.keySeperator = this.useSingleQuotes ? '\'' : Typography.quote;
        this.browserSecure = (this.features & SerializerFeature.BrowserSecure.mask) != 0;
        this.sepcialBits = this.browserSecure ? 5764610843043954687L : (this.features & SerializerFeature.WriteSlashAsSpecial.mask) != 0 ? 140758963191807L : 21474836479L;
    }

    public void config(SerializerFeature serializerFeature, boolean z) {
        if (z) {
            this.features |= serializerFeature.getMask();
            if (serializerFeature == SerializerFeature.WriteEnumUsingToString) {
                this.features &= SerializerFeature.WriteEnumUsingName.getMask() ^ (-1);
            } else if (serializerFeature == SerializerFeature.WriteEnumUsingName) {
                this.features &= SerializerFeature.WriteEnumUsingToString.getMask() ^ (-1);
            }
        } else {
            this.features = (serializerFeature.getMask() ^ (-1)) & this.features;
        }
        computeFeatures();
    }

    public void expandCapacity(int i) {
        if (this.maxBufSize != -1 && i >= this.maxBufSize) {
            throw new JSONException("serialize exceeded MAX_OUTPUT_LENGTH=" + this.maxBufSize + ", minimumCapacity=" + i);
        }
        int length = this.buf.length + (this.buf.length >> 1) + 1;
        if (length >= i) {
            i = length;
        }
        char[] cArr = new char[i];
        System.arraycopy(this.buf, 0, cArr, 0, this.count);
        this.buf = cArr;
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
        if (this.writer == null) {
            return;
        }
        try {
            this.writer.write(this.buf, 0, this.count);
            this.writer.flush();
            this.count = 0;
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public int getBufferLength() {
        return this.buf.length;
    }

    public int getMaxBufSize() {
        return this.maxBufSize;
    }

    public boolean isEnabled(int i) {
        return (i & this.features) != 0;
    }

    public boolean isEnabled(SerializerFeature serializerFeature) {
        return (serializerFeature.mask & this.features) != 0;
    }

    public boolean isNotWriteDefaultValue() {
        return this.notWriteDefaultValue;
    }

    public boolean isSortField() {
        return this.sortField;
    }

    public void setMaxBufSize(int i) {
        if (i >= this.buf.length) {
            this.maxBufSize = i;
            return;
        }
        throw new JSONException("must > " + this.buf.length);
    }

    public int size() {
        return this.count;
    }

    public byte[] toBytes(String str) {
        return toBytes((str == null || "UTF-8".equals(str)) ? IOUtils.UTF8 : Charset.forName(str));
    }

    public byte[] toBytes(Charset charset) {
        if (this.writer == null) {
            return charset == IOUtils.UTF8 ? encodeToUTF8Bytes() : new String(this.buf, 0, this.count).getBytes(charset);
        }
        throw new UnsupportedOperationException("writer not null");
    }

    public char[] toCharArray() {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        char[] cArr = new char[this.count];
        System.arraycopy(this.buf, 0, cArr, 0, this.count);
        return cArr;
    }

    public char[] toCharArrayForSpringWebSocket() {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        char[] cArr = new char[this.count - 2];
        System.arraycopy(this.buf, 1, cArr, 0, this.count - 2);
        return cArr;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    @Override // java.io.Writer
    public void write(int i) {
        int i2 = this.count + 1;
        if (i2 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i2);
            } else {
                flush();
                i2 = 1;
            }
        }
        this.buf[this.count] = (char) i;
        this.count = i2;
    }

    @Override // java.io.Writer
    public void write(String str) {
        if (str == null) {
            writeNull();
        } else {
            write(str, 0, str.length());
        }
    }

    @Override // java.io.Writer
    public void write(String str, int i, int i2) {
        int i3;
        int i4 = this.count + i2;
        if (i4 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i4);
            } else {
                while (true) {
                    int length = this.buf.length - this.count;
                    i3 = i + length;
                    str.getChars(i, i3, this.buf, this.count);
                    this.count = this.buf.length;
                    flush();
                    i2 -= length;
                    if (i2 <= this.buf.length) {
                        break;
                    } else {
                        i = i3;
                    }
                }
                i4 = i2;
                i = i3;
            }
        }
        str.getChars(i, i2 + i, this.buf, this.count);
        this.count = i4;
    }

    public void write(List<String> list) {
        boolean z;
        int i;
        if (list.isEmpty()) {
            write("[]");
            return;
        }
        int i2 = this.count;
        int size = list.size();
        int i3 = i2;
        int i4 = 0;
        while (i4 < size) {
            String str = list.get(i4);
            if (str == null) {
                z = true;
            } else {
                int length = str.length();
                z = false;
                for (int i5 = 0; i5 < length; i5++) {
                    char charAt = str.charAt(i5);
                    z = charAt < ' ' || charAt > '~' || charAt == '\"' || charAt == '\\';
                    if (z) {
                        break;
                    }
                }
            }
            if (z) {
                this.count = i2;
                write(91);
                for (int i6 = 0; i6 < list.size(); i6++) {
                    String str2 = list.get(i6);
                    if (i6 != 0) {
                        write(44);
                    }
                    if (str2 == null) {
                        write("null");
                    } else {
                        writeStringWithDoubleQuote(str2, (char) 0);
                    }
                }
                write(93);
                return;
            }
            int length2 = str.length() + i3 + 3;
            if (i4 == list.size() - 1) {
                length2++;
            }
            if (length2 > this.buf.length) {
                this.count = i3;
                expandCapacity(length2);
            }
            if (i4 == 0) {
                i = i3 + 1;
                this.buf[i3] = '[';
            } else {
                i = i3 + 1;
                this.buf[i3] = ',';
            }
            int i7 = i + 1;
            this.buf[i] = Typography.quote;
            str.getChars(0, str.length(), this.buf, i7);
            int length3 = i7 + str.length();
            this.buf[length3] = Typography.quote;
            i4++;
            i3 = length3 + 1;
        }
        this.buf[i3] = ']';
        this.count = i3 + 1;
    }

    public void write(boolean z) {
        if (z) {
            write("true");
        } else {
            write(Bugly.SDK_IS_DEV);
        }
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i, int i2) {
        int i3;
        if (i < 0 || i > cArr.length || i2 < 0 || (i3 = i + i2) > cArr.length || i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i2 == 0) {
            return;
        }
        int i4 = this.count + i2;
        if (i4 > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(i4);
            }
            do {
                int length = this.buf.length - this.count;
                System.arraycopy(cArr, i, this.buf, this.count, length);
                this.count = this.buf.length;
                flush();
                i2 -= length;
                i += length;
            } while (i2 > this.buf.length);
            i4 = i2;
        }
        System.arraycopy(cArr, i, this.buf, this.count, i2);
        this.count = i4;
    }

    public void writeByteArray(byte[] bArr) {
        if (isEnabled(SerializerFeature.WriteClassName.mask)) {
            writeHex(bArr);
            return;
        }
        int length = bArr.length;
        char c = this.useSingleQuotes ? '\'' : Typography.quote;
        if (length == 0) {
            write(this.useSingleQuotes ? "''" : "\"\"");
            return;
        }
        char[] cArr = IOUtils.CA;
        int i = (length / 3) * 3;
        int i2 = length - 1;
        int i3 = this.count;
        int i4 = this.count + (((i2 / 3) + 1) << 2) + 2;
        if (i4 > this.buf.length) {
            if (this.writer != null) {
                write(c);
                int i5 = 0;
                while (i5 < i) {
                    int i6 = i5 + 1;
                    int i7 = i6 + 1;
                    int i8 = ((bArr[i5] & 255) << 16) | ((bArr[i6] & 255) << 8) | (bArr[i7] & 255);
                    write(cArr[(i8 >>> 18) & 63]);
                    write(cArr[(i8 >>> 12) & 63]);
                    write(cArr[(i8 >>> 6) & 63]);
                    write(cArr[i8 & 63]);
                    i5 = i7 + 1;
                }
                int i9 = length - i;
                if (i9 > 0) {
                    int i10 = ((bArr[i] & 255) << 10) | (i9 == 2 ? (bArr[i2] & 255) << 2 : 0);
                    write(cArr[i10 >> 12]);
                    write(cArr[(i10 >>> 6) & 63]);
                    write(i9 == 2 ? cArr[i10 & 63] : '=');
                    write(61);
                }
                write(c);
                return;
            }
            expandCapacity(i4);
        }
        this.count = i4;
        int i11 = i3 + 1;
        this.buf[i3] = c;
        int i12 = 0;
        while (i12 < i) {
            int i13 = i12 + 1;
            int i14 = i13 + 1;
            int i15 = ((bArr[i12] & 255) << 16) | ((bArr[i13] & 255) << 8);
            int i16 = i14 + 1;
            int i17 = i15 | (bArr[i14] & 255);
            int i18 = i11 + 1;
            this.buf[i11] = cArr[(i17 >>> 18) & 63];
            int i19 = i18 + 1;
            this.buf[i18] = cArr[(i17 >>> 12) & 63];
            int i20 = i19 + 1;
            this.buf[i19] = cArr[(i17 >>> 6) & 63];
            this.buf[i20] = cArr[i17 & 63];
            i12 = i16;
            i11 = i20 + 1;
        }
        int i21 = length - i;
        if (i21 > 0) {
            int i22 = ((bArr[i] & 255) << 10) | (i21 == 2 ? (bArr[i2] & 255) << 2 : 0);
            this.buf[i4 - 5] = cArr[i22 >> 12];
            this.buf[i4 - 4] = cArr[(i22 >>> 6) & 63];
            this.buf[i4 - 3] = i21 == 2 ? cArr[i22 & 63] : '=';
            this.buf[i4 - 2] = '=';
        }
        this.buf[i4 - 1] = c;
    }

    public void writeDouble(double d, boolean z) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            writeNull();
            return;
        }
        String d2 = Double.toString(d);
        if (isEnabled(SerializerFeature.WriteNullNumberAsZero) && d2.endsWith(".0")) {
            d2 = d2.substring(0, d2.length() - 2);
        }
        write(d2);
        if (z && isEnabled(SerializerFeature.WriteClassName)) {
            write(68);
        }
    }

    public void writeEnum(Enum<?> r3) {
        if (r3 == null) {
            writeNull();
            return;
        }
        String str = null;
        if (this.writeEnumUsingName && !this.writeEnumUsingToString) {
            str = r3.name();
        } else if (this.writeEnumUsingToString) {
            str = r3.toString();
        }
        if (str == null) {
            writeInt(r3.ordinal());
            return;
        }
        int i = isEnabled(SerializerFeature.UseSingleQuotes) ? 39 : 34;
        write(i);
        write(str);
        write(i);
    }

    public void writeFieldName(String str) {
        writeFieldName(str, false);
    }

    public void writeFieldName(String str, boolean z) {
        if (str == null) {
            write("null:");
            return;
        }
        if (this.useSingleQuotes) {
            if (!this.quoteFieldNames) {
                writeKeyWithSingleQuoteIfHasSpecial(str);
                return;
            } else {
                writeStringWithSingleQuote(str);
                write(58);
                return;
            }
        }
        if (this.quoteFieldNames) {
            writeStringWithDoubleQuote(str, ':');
            return;
        }
        boolean z2 = str.length() == 0;
        int i = 0;
        while (true) {
            if (i >= str.length()) {
                break;
            }
            char charAt = str.charAt(i);
            if ((charAt < '@' && (this.sepcialBits & (1 << charAt)) != 0) || charAt == '\\') {
                z2 = true;
                break;
            }
            i++;
        }
        if (z2) {
            writeStringWithDoubleQuote(str, ':');
        } else {
            write(str);
            write(58);
        }
    }

    public void writeFieldNameDirect(String str) {
        int length = str.length();
        int i = this.count + length + 3;
        if (i > this.buf.length) {
            expandCapacity(i);
        }
        int i2 = this.count + 1;
        this.buf[this.count] = Typography.quote;
        str.getChars(0, length, this.buf, i2);
        this.count = i;
        this.buf[this.count - 2] = Typography.quote;
        this.buf[this.count - 1] = ':';
    }

    public void writeFieldValue(char c, String str, char c2) {
        write(c);
        writeFieldName(str);
        if (c2 == 0) {
            writeString("\u0000");
        } else {
            writeString(Character.toString(c2));
        }
    }

    public void writeFieldValue(char c, String str, double d) {
        write(c);
        writeFieldName(str);
        writeDouble(d, false);
    }

    public void writeFieldValue(char c, String str, float f) {
        write(c);
        writeFieldName(str);
        writeFloat(f, false);
    }

    public void writeFieldValue(char c, String str, int i) {
        if (i == Integer.MIN_VALUE || !this.quoteFieldNames) {
            write(c);
            writeFieldName(str);
            writeInt(i);
            return;
        }
        int stringSize = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        int length = str.length();
        int i2 = this.count + length + 4 + stringSize;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                write(c);
                writeFieldName(str);
                writeInt(i);
                return;
            }
            expandCapacity(i2);
        }
        int i3 = this.count;
        this.count = i2;
        this.buf[i3] = c;
        int i4 = i3 + length + 1;
        this.buf[i3 + 1] = this.keySeperator;
        str.getChars(0, length, this.buf, i3 + 2);
        this.buf[i4 + 1] = this.keySeperator;
        this.buf[i4 + 2] = ':';
        IOUtils.getChars(i, this.count, this.buf);
    }

    public void writeFieldValue(char c, String str, long j) {
        if (j == Long.MIN_VALUE || !this.quoteFieldNames) {
            write(c);
            writeFieldName(str);
            writeLong(j);
            return;
        }
        int stringSize = j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j);
        int length = str.length();
        int i = this.count + length + 4 + stringSize;
        if (i > this.buf.length) {
            if (this.writer != null) {
                write(c);
                writeFieldName(str);
                writeLong(j);
                return;
            }
            expandCapacity(i);
        }
        int i2 = this.count;
        this.count = i;
        this.buf[i2] = c;
        int i3 = i2 + length + 1;
        this.buf[i2 + 1] = this.keySeperator;
        str.getChars(0, length, this.buf, i2 + 2);
        this.buf[i3 + 1] = this.keySeperator;
        this.buf[i3 + 2] = ':';
        IOUtils.getChars(j, this.count, this.buf);
    }

    public void writeFieldValue(char c, String str, Enum<?> r4) {
        if (r4 == null) {
            write(c);
            writeFieldName(str);
            writeNull();
        } else if (this.writeEnumUsingName && !this.writeEnumUsingToString) {
            writeEnumFieldValue(c, str, r4.name());
        } else if (this.writeEnumUsingToString) {
            writeEnumFieldValue(c, str, r4.toString());
        } else {
            writeFieldValue(c, str, r4.ordinal());
        }
    }

    public void writeFieldValue(char c, String str, String str2) {
        if (!this.quoteFieldNames) {
            write(c);
            writeFieldName(str);
            if (str2 == null) {
                writeNull();
                return;
            } else {
                writeString(str2);
                return;
            }
        }
        if (this.useSingleQuotes) {
            write(c);
            writeFieldName(str);
            if (str2 == null) {
                writeNull();
                return;
            } else {
                writeString(str2);
                return;
            }
        }
        if (!isEnabled(SerializerFeature.BrowserCompatible)) {
            writeFieldValueStringWithDoubleQuoteCheck(c, str, str2);
            return;
        }
        write(c);
        writeStringWithDoubleQuote(str, ':');
        writeStringWithDoubleQuote(str2, (char) 0);
    }

    public void writeFieldValue(char c, String str, BigDecimal bigDecimal) {
        write(c);
        writeFieldName(str);
        if (bigDecimal == null) {
            writeNull();
        } else {
            write(bigDecimal.toString());
        }
    }

    public void writeFieldValue(char c, String str, boolean z) {
        if (!this.quoteFieldNames) {
            write(c);
            writeFieldName(str);
            write(z);
            return;
        }
        int i = z ? 4 : 5;
        int length = str.length();
        int i2 = this.count + length + 4 + i;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                write(c);
                writeString(str);
                write(58);
                write(z);
                return;
            }
            expandCapacity(i2);
        }
        int i3 = this.count;
        this.count = i2;
        this.buf[i3] = c;
        int i4 = i3 + length + 1;
        this.buf[i3 + 1] = this.keySeperator;
        str.getChars(0, length, this.buf, i3 + 2);
        this.buf[i4 + 1] = this.keySeperator;
        if (z) {
            System.arraycopy(":true".toCharArray(), 0, this.buf, i4 + 2, 5);
        } else {
            System.arraycopy(":false".toCharArray(), 0, this.buf, i4 + 2, 6);
        }
    }

    public void writeFieldValueStringWithDoubleQuote(char c, String str, String str2) {
        int length = str.length();
        int i = this.count;
        int length2 = str2.length();
        int i2 = i + length + length2 + 6;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                write(c);
                writeStringWithDoubleQuote(str, ':');
                writeStringWithDoubleQuote(str2, (char) 0);
                return;
            }
            expandCapacity(i2);
        }
        this.buf[this.count] = c;
        int i3 = this.count + 2;
        int i4 = i3 + length;
        this.buf[this.count + 1] = Typography.quote;
        str.getChars(0, length, this.buf, i3);
        this.count = i2;
        this.buf[i4] = Typography.quote;
        int i5 = i4 + 1;
        int i6 = i5 + 1;
        this.buf[i5] = ':';
        this.buf[i6] = Typography.quote;
        str2.getChars(0, length2, this.buf, i6 + 1);
        this.buf[this.count - 1] = Typography.quote;
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x00cf, code lost:
    
        if ((r19.sepcialBits & (1 << r5)) == 0) goto L40;
     */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0101  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void writeFieldValueStringWithDoubleQuoteCheck(char r20, java.lang.String r21, java.lang.String r22) {
        /*
            Method dump skipped, instructions count: 931
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.writeFieldValueStringWithDoubleQuoteCheck(char, java.lang.String, java.lang.String):void");
    }

    public void writeFloat(float f, boolean z) {
        if (Float.isNaN(f) || Float.isInfinite(f)) {
            writeNull();
            return;
        }
        String f2 = Float.toString(f);
        if (isEnabled(SerializerFeature.WriteNullNumberAsZero) && f2.endsWith(".0")) {
            f2 = f2.substring(0, f2.length() - 2);
        }
        write(f2);
        if (z && isEnabled(SerializerFeature.WriteClassName)) {
            write(70);
        }
    }

    public void writeHex(byte[] bArr) {
        int i = 2;
        int length = this.count + (bArr.length * 2) + 3;
        int i2 = 0;
        if (length > this.buf.length) {
            if (this.writer != null) {
                char[] cArr = new char[bArr.length + 3];
                cArr[0] = 'x';
                cArr[1] = '\'';
                while (i2 < bArr.length) {
                    int i3 = bArr[i2] & 255;
                    int i4 = i3 >> 4;
                    int i5 = i3 & 15;
                    int i6 = i + 1;
                    cArr[i] = (char) (i4 + (i4 < 10 ? 48 : 55));
                    i = i6 + 1;
                    cArr[i6] = (char) (i5 + (i5 < 10 ? 48 : 55));
                    i2++;
                }
                cArr[i] = '\'';
                try {
                    this.writer.write(cArr);
                    return;
                } catch (IOException e) {
                    throw new JSONException("writeBytes error.", e);
                }
            }
            expandCapacity(length);
        }
        char[] cArr2 = this.buf;
        int i7 = this.count;
        this.count = i7 + 1;
        cArr2[i7] = 'x';
        char[] cArr3 = this.buf;
        int i8 = this.count;
        this.count = i8 + 1;
        cArr3[i8] = '\'';
        while (i2 < bArr.length) {
            int i9 = bArr[i2] & 255;
            int i10 = i9 >> 4;
            int i11 = i9 & 15;
            char[] cArr4 = this.buf;
            int i12 = this.count;
            this.count = i12 + 1;
            cArr4[i12] = (char) (i10 + (i10 < 10 ? 48 : 55));
            char[] cArr5 = this.buf;
            int i13 = this.count;
            this.count = i13 + 1;
            cArr5[i13] = (char) (i11 + (i11 < 10 ? 48 : 55));
            i2++;
        }
        char[] cArr6 = this.buf;
        int i14 = this.count;
        this.count = i14 + 1;
        cArr6[i14] = '\'';
    }

    public void writeInt(int i) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return;
        }
        int stringSize = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        int i2 = this.count + stringSize;
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                char[] cArr = new char[stringSize];
                IOUtils.getChars(i, stringSize, cArr);
                write(cArr, 0, cArr.length);
                return;
            }
            expandCapacity(i2);
        }
        IOUtils.getChars(i, i2, this.buf);
        this.count = i2;
    }

    public void writeLong(long j) {
        boolean z = isEnabled(SerializerFeature.BrowserCompatible) && !isEnabled(SerializerFeature.WriteClassName) && (j > 9007199254740991L || j < -9007199254740991L);
        if (j == Long.MIN_VALUE) {
            if (z) {
                write("\"-9223372036854775808\"");
                return;
            } else {
                write("-9223372036854775808");
                return;
            }
        }
        int stringSize = j < 0 ? IOUtils.stringSize(-j) + 1 : IOUtils.stringSize(j);
        int i = this.count + stringSize;
        if (z) {
            i += 2;
        }
        if (i > this.buf.length) {
            if (this.writer != null) {
                char[] cArr = new char[stringSize];
                IOUtils.getChars(j, stringSize, cArr);
                if (!z) {
                    write(cArr, 0, cArr.length);
                    return;
                }
                write(34);
                write(cArr, 0, cArr.length);
                write(34);
                return;
            }
            expandCapacity(i);
        }
        if (z) {
            this.buf[this.count] = Typography.quote;
            int i2 = i - 1;
            IOUtils.getChars(j, i2, this.buf);
            this.buf[i2] = Typography.quote;
        } else {
            IOUtils.getChars(j, i, this.buf);
        }
        this.count = i;
    }

    public void writeNull() {
        write("null");
    }

    public void writeNull(int i, int i2) {
        if ((i & i2) == 0 && (this.features & i2) == 0) {
            writeNull();
            return;
        }
        if (i2 == SerializerFeature.WriteNullListAsEmpty.mask) {
            write("[]");
            return;
        }
        if (i2 == SerializerFeature.WriteNullStringAsEmpty.mask) {
            writeString("");
            return;
        }
        if (i2 == SerializerFeature.WriteNullBooleanAsFalse.mask) {
            write(Bugly.SDK_IS_DEV);
        } else if (i2 == SerializerFeature.WriteNullNumberAsZero.mask) {
            write(48);
        } else {
            writeNull();
        }
    }

    public void writeNull(SerializerFeature serializerFeature) {
        writeNull(0, serializerFeature.mask);
    }

    public void writeString(String str) {
        if (this.useSingleQuotes) {
            writeStringWithSingleQuote(str);
        } else {
            writeStringWithDoubleQuote(str, (char) 0);
        }
    }

    public void writeString(String str, char c) {
        if (!this.useSingleQuotes) {
            writeStringWithDoubleQuote(str, c);
        } else {
            writeStringWithSingleQuote(str);
            write(c);
        }
    }

    public void writeString(char[] cArr) {
        if (this.useSingleQuotes) {
            writeStringWithSingleQuote(cArr);
        } else {
            writeStringWithDoubleQuote(new String(cArr), (char) 0);
        }
    }

    public void writeStringWithDoubleQuote(String str, char c) {
        int i;
        if (str == null) {
            writeNull();
            if (c != 0) {
                write(c);
                return;
            }
            return;
        }
        int length = str.length();
        int i2 = this.count + length + 2;
        if (c != 0) {
            i2++;
        }
        char c2 = '\b';
        if (i2 > this.buf.length) {
            if (this.writer != null) {
                write(34);
                for (int i3 = 0; i3 < str.length(); i3++) {
                    char charAt = str.charAt(i3);
                    if (isEnabled(SerializerFeature.BrowserSecure) && (charAt == '(' || charAt == ')' || charAt == '<' || charAt == '>')) {
                        write(92);
                        write(117);
                        write(IOUtils.DIGITS[(charAt >>> '\f') & 15]);
                        write(IOUtils.DIGITS[(charAt >>> '\b') & 15]);
                        write(IOUtils.DIGITS[(charAt >>> 4) & 15]);
                        write(IOUtils.DIGITS[charAt & 15]);
                    } else if (!isEnabled(SerializerFeature.BrowserCompatible)) {
                        if ((charAt < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[charAt] != 0) || (charAt == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            write(92);
                            if (IOUtils.specicalFlags_doubleQuotes[charAt] == 4) {
                                write(117);
                                write(IOUtils.DIGITS[(charAt >>> '\f') & 15]);
                                write(IOUtils.DIGITS[(charAt >>> '\b') & 15]);
                                write(IOUtils.DIGITS[(charAt >>> 4) & 15]);
                                write(IOUtils.DIGITS[charAt & 15]);
                            } else {
                                write(IOUtils.replaceChars[charAt]);
                            }
                        }
                        write(charAt);
                    } else if (charAt == '\b' || charAt == '\f' || charAt == '\n' || charAt == '\r' || charAt == '\t' || charAt == '\"' || charAt == '/' || charAt == '\\') {
                        write(92);
                        write(IOUtils.replaceChars[charAt]);
                    } else if (charAt < ' ') {
                        write(92);
                        write(117);
                        write(48);
                        write(48);
                        int i4 = charAt * 2;
                        write(IOUtils.ASCII_CHARS[i4]);
                        write(IOUtils.ASCII_CHARS[i4 + 1]);
                    } else {
                        if (charAt >= 127) {
                            write(92);
                            write(117);
                            write(IOUtils.DIGITS[(charAt >>> '\f') & 15]);
                            write(IOUtils.DIGITS[(charAt >>> '\b') & 15]);
                            write(IOUtils.DIGITS[(charAt >>> 4) & 15]);
                            write(IOUtils.DIGITS[charAt & 15]);
                        }
                        write(charAt);
                    }
                }
                write(34);
                if (c != 0) {
                    write(c);
                    return;
                }
                return;
            }
            expandCapacity(i2);
        }
        int i5 = this.count + 1;
        int i6 = i5 + length;
        this.buf[this.count] = Typography.quote;
        str.getChars(0, length, this.buf, i5);
        this.count = i2;
        int i7 = -1;
        if (isEnabled(SerializerFeature.BrowserCompatible)) {
            for (int i8 = i5; i8 < i6; i8++) {
                char c3 = this.buf[i8];
                if (c3 == '\"' || c3 == '/' || c3 == '\\') {
                    i2++;
                } else if (c3 == '\b' || c3 == '\f' || c3 == '\n' || c3 == '\r' || c3 == '\t') {
                    i2++;
                } else if (c3 < ' ') {
                    i2 += 5;
                } else if (c3 >= 127) {
                    i2 += 5;
                }
                i7 = i8;
            }
            if (i2 > this.buf.length) {
                expandCapacity(i2);
            }
            this.count = i2;
            while (i7 >= i5) {
                char c4 = this.buf[i7];
                if (c4 == c2 || c4 == '\f' || c4 == '\n' || c4 == '\r' || c4 == '\t') {
                    int i9 = i7 + 1;
                    System.arraycopy(this.buf, i9, this.buf, i7 + 2, (i6 - i7) - 1);
                    this.buf[i7] = '\\';
                    this.buf[i9] = IOUtils.replaceChars[c4];
                    i6++;
                } else if (c4 == '\"' || c4 == '/' || c4 == '\\') {
                    int i10 = i7 + 1;
                    System.arraycopy(this.buf, i10, this.buf, i7 + 2, (i6 - i7) - 1);
                    this.buf[i7] = '\\';
                    this.buf[i10] = c4;
                    i6++;
                } else if (c4 < ' ') {
                    int i11 = i7 + 1;
                    System.arraycopy(this.buf, i11, this.buf, i7 + 6, (i6 - i7) - 1);
                    this.buf[i7] = '\\';
                    this.buf[i11] = 'u';
                    this.buf[i7 + 2] = '0';
                    this.buf[i7 + 3] = '0';
                    int i12 = c4 * 2;
                    this.buf[i7 + 4] = IOUtils.ASCII_CHARS[i12];
                    this.buf[i7 + 5] = IOUtils.ASCII_CHARS[i12 + 1];
                    i6 += 5;
                } else if (c4 >= 127) {
                    int i13 = i7 + 1;
                    System.arraycopy(this.buf, i13, this.buf, i7 + 6, (i6 - i7) - 1);
                    this.buf[i7] = '\\';
                    this.buf[i13] = 'u';
                    this.buf[i7 + 2] = IOUtils.DIGITS[(c4 >>> '\f') & 15];
                    this.buf[i7 + 3] = IOUtils.DIGITS[(c4 >>> '\b') & 15];
                    this.buf[i7 + 4] = IOUtils.DIGITS[(c4 >>> 4) & 15];
                    this.buf[i7 + 5] = IOUtils.DIGITS[c4 & 15];
                    i6 += 5;
                }
                i7--;
                c2 = '\b';
            }
            if (c == 0) {
                this.buf[this.count - 1] = Typography.quote;
                return;
            } else {
                this.buf[this.count - 2] = Typography.quote;
                this.buf[this.count - 1] = c;
                return;
            }
        }
        int i14 = i2;
        int i15 = 0;
        char c5 = 0;
        int i16 = -1;
        int i17 = -1;
        for (int i18 = i5; i18 < i6; i18++) {
            char c6 = this.buf[i18];
            if (c6 >= ']') {
                if (c6 >= 127 && (c6 == 8232 || c6 == 8233 || c6 < 160)) {
                    if (i16 == i7) {
                        i16 = i18;
                    }
                    i15++;
                    i14 += 4;
                    i17 = i18;
                }
            } else if ((c6 < '@' && (this.sepcialBits & (1 << c6)) != 0) || c6 == '\\') {
                i15++;
                if (c6 == '(' || c6 == ')' || c6 == '<' || c6 == '>' || (c6 < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[c6] == 4)) {
                    i14 += 4;
                }
                i7 = -1;
                if (i16 == -1) {
                    i16 = i18;
                    i17 = i16;
                }
                i17 = i18;
            } else {
                i7 = -1;
            }
            c5 = c6;
        }
        if (i15 > 0) {
            int i19 = i14 + i15;
            if (i19 > this.buf.length) {
                expandCapacity(i19);
            }
            this.count = i19;
            if (i15 == 1) {
                if (c5 == 8232) {
                    int i20 = i17 + 1;
                    System.arraycopy(this.buf, i20, this.buf, i17 + 6, (i6 - i17) - 1);
                    this.buf[i17] = '\\';
                    this.buf[i20] = 'u';
                    int i21 = i20 + 1;
                    this.buf[i21] = '2';
                    int i22 = i21 + 1;
                    this.buf[i22] = '0';
                    int i23 = i22 + 1;
                    this.buf[i23] = '2';
                    this.buf[i23 + 1] = '8';
                } else if (c5 == 8233) {
                    int i24 = i17 + 1;
                    System.arraycopy(this.buf, i24, this.buf, i17 + 6, (i6 - i17) - 1);
                    this.buf[i17] = '\\';
                    this.buf[i24] = 'u';
                    int i25 = i24 + 1;
                    this.buf[i25] = '2';
                    int i26 = i25 + 1;
                    this.buf[i26] = '0';
                    int i27 = i26 + 1;
                    this.buf[i27] = '2';
                    this.buf[i27 + 1] = '9';
                } else if (c5 == '(' || c5 == ')' || c5 == '<' || c5 == '>') {
                    int i28 = i17 + 1;
                    System.arraycopy(this.buf, i28, this.buf, i17 + 6, (i6 - i17) - 1);
                    this.buf[i17] = '\\';
                    this.buf[i28] = 'u';
                    int i29 = i28 + 1;
                    this.buf[i29] = IOUtils.DIGITS[(c5 >>> '\f') & 15];
                    int i30 = i29 + 1;
                    this.buf[i30] = IOUtils.DIGITS[(c5 >>> '\b') & 15];
                    int i31 = i30 + 1;
                    this.buf[i31] = IOUtils.DIGITS[(c5 >>> 4) & 15];
                    this.buf[i31 + 1] = IOUtils.DIGITS[c5 & 15];
                } else if (c5 >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[c5] != 4) {
                    int i32 = i17 + 1;
                    System.arraycopy(this.buf, i32, this.buf, i17 + 2, (i6 - i17) - 1);
                    this.buf[i17] = '\\';
                    this.buf[i32] = IOUtils.replaceChars[c5];
                } else {
                    int i33 = i17 + 1;
                    System.arraycopy(this.buf, i33, this.buf, i17 + 6, (i6 - i17) - 1);
                    this.buf[i17] = '\\';
                    int i34 = i33 + 1;
                    this.buf[i33] = 'u';
                    int i35 = i34 + 1;
                    this.buf[i34] = IOUtils.DIGITS[(c5 >>> '\f') & 15];
                    int i36 = i35 + 1;
                    this.buf[i35] = IOUtils.DIGITS[(c5 >>> '\b') & 15];
                    this.buf[i36] = IOUtils.DIGITS[(c5 >>> 4) & 15];
                    this.buf[i36 + 1] = IOUtils.DIGITS[c5 & 15];
                }
            } else if (i15 > 1) {
                for (int i37 = i16 - i5; i37 < str.length(); i37++) {
                    char charAt2 = str.charAt(i37);
                    if (this.browserSecure && (charAt2 == '(' || charAt2 == ')' || charAt2 == '<' || charAt2 == '>')) {
                        int i38 = i16 + 1;
                        this.buf[i16] = '\\';
                        int i39 = i38 + 1;
                        this.buf[i38] = 'u';
                        int i40 = i39 + 1;
                        this.buf[i39] = IOUtils.DIGITS[(charAt2 >>> '\f') & 15];
                        int i41 = i40 + 1;
                        this.buf[i40] = IOUtils.DIGITS[(charAt2 >>> '\b') & 15];
                        int i42 = i41 + 1;
                        this.buf[i41] = IOUtils.DIGITS[(charAt2 >>> 4) & 15];
                        this.buf[i42] = IOUtils.DIGITS[charAt2 & 15];
                        i16 = i42 + 1;
                    } else if ((charAt2 < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[charAt2] != 0) || (charAt2 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                        int i43 = i16 + 1;
                        this.buf[i16] = '\\';
                        if (IOUtils.specicalFlags_doubleQuotes[charAt2] == 4) {
                            int i44 = i43 + 1;
                            this.buf[i43] = 'u';
                            int i45 = i44 + 1;
                            this.buf[i44] = IOUtils.DIGITS[(charAt2 >>> '\f') & 15];
                            int i46 = i45 + 1;
                            this.buf[i45] = IOUtils.DIGITS[(charAt2 >>> '\b') & 15];
                            int i47 = i46 + 1;
                            this.buf[i46] = IOUtils.DIGITS[(charAt2 >>> 4) & 15];
                            i = i47 + 1;
                            this.buf[i47] = IOUtils.DIGITS[charAt2 & 15];
                        } else {
                            i = i43 + 1;
                            this.buf[i43] = IOUtils.replaceChars[charAt2];
                        }
                        i16 = i;
                    } else if (charAt2 == 8232 || charAt2 == 8233) {
                        int i48 = i16 + 1;
                        this.buf[i16] = '\\';
                        int i49 = i48 + 1;
                        this.buf[i48] = 'u';
                        int i50 = i49 + 1;
                        this.buf[i49] = IOUtils.DIGITS[(charAt2 >>> '\f') & 15];
                        int i51 = i50 + 1;
                        this.buf[i50] = IOUtils.DIGITS[(charAt2 >>> '\b') & 15];
                        int i52 = i51 + 1;
                        this.buf[i51] = IOUtils.DIGITS[(charAt2 >>> 4) & 15];
                        this.buf[i52] = IOUtils.DIGITS[charAt2 & 15];
                        i16 = i52 + 1;
                    } else {
                        this.buf[i16] = charAt2;
                        i16++;
                    }
                }
            }
        }
        if (c == 0) {
            this.buf[this.count - 1] = Typography.quote;
        } else {
            this.buf[this.count - 2] = Typography.quote;
            this.buf[this.count - 1] = c;
        }
    }

    public void writeStringWithDoubleQuote(char[] cArr, char c) {
        if (cArr == null) {
            writeNull();
            if (c != 0) {
                write(c);
                return;
            }
            return;
        }
        int length = cArr.length;
        int i = this.count + length + 2;
        if (c != 0) {
            i++;
        }
        char c2 = '\b';
        char c3 = '\f';
        if (i > this.buf.length) {
            if (this.writer != null) {
                write(34);
                for (char c4 : cArr) {
                    if (isEnabled(SerializerFeature.BrowserSecure) && (c4 == '(' || c4 == ')' || c4 == '<' || c4 == '>')) {
                        write(92);
                        write(117);
                        write(IOUtils.DIGITS[(c4 >>> '\f') & 15]);
                        write(IOUtils.DIGITS[(c4 >>> '\b') & 15]);
                        write(IOUtils.DIGITS[(c4 >>> 4) & 15]);
                        write(IOUtils.DIGITS[c4 & 15]);
                    } else if (!isEnabled(SerializerFeature.BrowserCompatible)) {
                        if ((c4 < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[c4] != 0) || (c4 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            write(92);
                            if (IOUtils.specicalFlags_doubleQuotes[c4] == 4) {
                                write(117);
                                write(IOUtils.DIGITS[(c4 >>> '\f') & 15]);
                                write(IOUtils.DIGITS[(c4 >>> '\b') & 15]);
                                write(IOUtils.DIGITS[(c4 >>> 4) & 15]);
                                write(IOUtils.DIGITS[c4 & 15]);
                            } else {
                                write(IOUtils.replaceChars[c4]);
                            }
                        }
                        write(c4);
                    } else if (c4 == '\b' || c4 == '\f' || c4 == '\n' || c4 == '\r' || c4 == '\t' || c4 == '\"' || c4 == '/' || c4 == '\\') {
                        write(92);
                        write(IOUtils.replaceChars[c4]);
                    } else if (c4 < ' ') {
                        write(92);
                        write(117);
                        write(48);
                        write(48);
                        int i2 = c4 * 2;
                        write(IOUtils.ASCII_CHARS[i2]);
                        write(IOUtils.ASCII_CHARS[i2 + 1]);
                    } else {
                        if (c4 >= 127) {
                            write(92);
                            write(117);
                            write(IOUtils.DIGITS[(c4 >>> '\f') & 15]);
                            write(IOUtils.DIGITS[(c4 >>> '\b') & 15]);
                            write(IOUtils.DIGITS[(c4 >>> 4) & 15]);
                            write(IOUtils.DIGITS[c4 & 15]);
                        }
                        write(c4);
                    }
                }
                write(34);
                if (c != 0) {
                    write(c);
                    return;
                }
                return;
            }
            expandCapacity(i);
        }
        int i3 = this.count + 1;
        int i4 = length + i3;
        this.buf[this.count] = Typography.quote;
        System.arraycopy(cArr, 0, this.buf, i3, cArr.length);
        this.count = i;
        int i5 = -1;
        if (isEnabled(SerializerFeature.BrowserCompatible)) {
            for (int i6 = i3; i6 < i4; i6++) {
                char c5 = this.buf[i6];
                if (c5 == '\"' || c5 == '/' || c5 == '\\') {
                    i++;
                } else if (c5 == '\b' || c5 == '\f' || c5 == '\n' || c5 == '\r' || c5 == '\t') {
                    i++;
                } else if (c5 < ' ') {
                    i += 5;
                } else if (c5 >= 127) {
                    i += 5;
                }
                i5 = i6;
            }
            if (i > this.buf.length) {
                expandCapacity(i);
            }
            this.count = i;
            while (i5 >= i3) {
                char c6 = this.buf[i5];
                if (c6 == c2 || c6 == c3 || c6 == '\n' || c6 == '\r' || c6 == '\t') {
                    int i7 = i5 + 1;
                    System.arraycopy(this.buf, i7, this.buf, i5 + 2, (i4 - i5) - 1);
                    this.buf[i5] = '\\';
                    this.buf[i7] = IOUtils.replaceChars[c6];
                    i4++;
                } else if (c6 == '\"' || c6 == '/' || c6 == '\\') {
                    int i8 = i5 + 1;
                    System.arraycopy(this.buf, i8, this.buf, i5 + 2, (i4 - i5) - 1);
                    this.buf[i5] = '\\';
                    this.buf[i8] = c6;
                    i4++;
                } else if (c6 < ' ') {
                    int i9 = i5 + 1;
                    System.arraycopy(this.buf, i9, this.buf, i5 + 6, (i4 - i5) - 1);
                    this.buf[i5] = '\\';
                    this.buf[i9] = 'u';
                    this.buf[i5 + 2] = '0';
                    this.buf[i5 + 3] = '0';
                    int i10 = c6 * 2;
                    this.buf[i5 + 4] = IOUtils.ASCII_CHARS[i10];
                    this.buf[i5 + 5] = IOUtils.ASCII_CHARS[i10 + 1];
                    i4 += 5;
                } else if (c6 >= 127) {
                    int i11 = i5 + 1;
                    System.arraycopy(this.buf, i11, this.buf, i5 + 6, (i4 - i5) - 1);
                    this.buf[i5] = '\\';
                    this.buf[i11] = 'u';
                    this.buf[i5 + 2] = IOUtils.DIGITS[(c6 >>> '\f') & 15];
                    this.buf[i5 + 3] = IOUtils.DIGITS[(c6 >>> '\b') & 15];
                    this.buf[i5 + 4] = IOUtils.DIGITS[(c6 >>> 4) & 15];
                    this.buf[i5 + 5] = IOUtils.DIGITS[c6 & 15];
                    i4 += 5;
                }
                i5--;
                c2 = '\b';
                c3 = '\f';
            }
            if (c == 0) {
                this.buf[this.count - 1] = Typography.quote;
                return;
            } else {
                this.buf[this.count - 2] = Typography.quote;
                this.buf[this.count - 1] = c;
                return;
            }
        }
        int i12 = i;
        int i13 = i3;
        int i14 = -1;
        int i15 = 0;
        char c7 = 0;
        int i16 = -1;
        while (i13 < i4) {
            char c8 = this.buf[i13];
            if (c8 < ']') {
                if ((c8 < '@' && (this.sepcialBits & (1 << c8)) != 0) || c8 == '\\') {
                    i15++;
                    if (c8 == '(' || c8 == ')' || c8 == '<' || c8 == '>' || (c8 < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[c8] == 4)) {
                        i12 += 4;
                    }
                    if (i14 == -1) {
                        i14 = i13;
                        i16 = i14;
                    } else {
                        i16 = i13;
                    }
                    c7 = c8;
                    i13++;
                    i5 = -1;
                }
            } else if (c8 >= 127 && (c8 == 8232 || c8 == 8233 || c8 < 160)) {
                if (i14 == i5) {
                    i14 = i13;
                }
                i15++;
                i12 += 4;
                i16 = i13;
                c7 = c8;
            }
            i13++;
            i5 = -1;
        }
        if (i15 > 0) {
            int i17 = i12 + i15;
            if (i17 > this.buf.length) {
                expandCapacity(i17);
            }
            this.count = i17;
            if (i15 == 1) {
                if (c7 == 8232) {
                    int i18 = i16 + 1;
                    System.arraycopy(this.buf, i18, this.buf, i16 + 6, (i4 - i16) - 1);
                    this.buf[i16] = '\\';
                    this.buf[i18] = 'u';
                    int i19 = i18 + 1;
                    this.buf[i19] = '2';
                    int i20 = i19 + 1;
                    this.buf[i20] = '0';
                    int i21 = i20 + 1;
                    this.buf[i21] = '2';
                    this.buf[i21 + 1] = '8';
                } else if (c7 == 8233) {
                    int i22 = i16 + 1;
                    System.arraycopy(this.buf, i22, this.buf, i16 + 6, (i4 - i16) - 1);
                    this.buf[i16] = '\\';
                    this.buf[i22] = 'u';
                    int i23 = i22 + 1;
                    this.buf[i23] = '2';
                    int i24 = i23 + 1;
                    this.buf[i24] = '0';
                    int i25 = i24 + 1;
                    this.buf[i25] = '2';
                    this.buf[i25 + 1] = '9';
                } else if (c7 == '(' || c7 == ')' || c7 == '<' || c7 == '>') {
                    int i26 = i16 + 1;
                    System.arraycopy(this.buf, i26, this.buf, i16 + 6, (i4 - i16) - 1);
                    this.buf[i16] = '\\';
                    this.buf[i26] = 'u';
                    int i27 = i26 + 1;
                    this.buf[i27] = IOUtils.DIGITS[(c7 >>> '\f') & 15];
                    int i28 = i27 + 1;
                    this.buf[i28] = IOUtils.DIGITS[(c7 >>> '\b') & 15];
                    int i29 = i28 + 1;
                    this.buf[i29] = IOUtils.DIGITS[(c7 >>> 4) & 15];
                    this.buf[i29 + 1] = IOUtils.DIGITS[c7 & 15];
                } else if (c7 >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[c7] != 4) {
                    int i30 = i16 + 1;
                    System.arraycopy(this.buf, i30, this.buf, i16 + 2, (i4 - i16) - 1);
                    this.buf[i16] = '\\';
                    this.buf[i30] = IOUtils.replaceChars[c7];
                } else {
                    int i31 = i16 + 1;
                    System.arraycopy(this.buf, i31, this.buf, i16 + 6, (i4 - i16) - 1);
                    this.buf[i16] = '\\';
                    int i32 = i31 + 1;
                    this.buf[i31] = 'u';
                    int i33 = i32 + 1;
                    this.buf[i32] = IOUtils.DIGITS[(c7 >>> '\f') & 15];
                    int i34 = i33 + 1;
                    this.buf[i33] = IOUtils.DIGITS[(c7 >>> '\b') & 15];
                    this.buf[i34] = IOUtils.DIGITS[(c7 >>> 4) & 15];
                    this.buf[i34 + 1] = IOUtils.DIGITS[c7 & 15];
                }
            } else if (i15 > 1) {
                for (int i35 = i14 - i3; i35 < cArr.length; i35++) {
                    char c9 = cArr[i35];
                    if (this.browserSecure && (c9 == '(' || c9 == ')' || c9 == '<' || c9 == '>')) {
                        int i36 = i14 + 1;
                        this.buf[i14] = '\\';
                        int i37 = i36 + 1;
                        this.buf[i36] = 'u';
                        int i38 = i37 + 1;
                        this.buf[i37] = IOUtils.DIGITS[(c9 >>> '\f') & 15];
                        int i39 = i38 + 1;
                        this.buf[i38] = IOUtils.DIGITS[(c9 >>> '\b') & 15];
                        int i40 = i39 + 1;
                        this.buf[i39] = IOUtils.DIGITS[(c9 >>> 4) & 15];
                        i14 = i40 + 1;
                        this.buf[i40] = IOUtils.DIGITS[c9 & 15];
                    } else {
                        if ((c9 >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[c9] == 0) && (c9 != '/' || !isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            if (c9 == 8232 || c9 == 8233) {
                                int i41 = i14 + 1;
                                this.buf[i14] = '\\';
                                int i42 = i41 + 1;
                                this.buf[i41] = 'u';
                                int i43 = i42 + 1;
                                this.buf[i42] = IOUtils.DIGITS[(c9 >>> '\f') & 15];
                                int i44 = i43 + 1;
                                this.buf[i43] = IOUtils.DIGITS[(c9 >>> '\b') & 15];
                                int i45 = i44 + 1;
                                this.buf[i44] = IOUtils.DIGITS[(c9 >>> 4) & 15];
                                i14 = i45 + 1;
                                this.buf[i45] = IOUtils.DIGITS[c9 & 15];
                            } else {
                                this.buf[i14] = c9;
                                i14++;
                            }
                        }
                        int i46 = i14 + 1;
                        this.buf[i14] = '\\';
                        if (IOUtils.specicalFlags_doubleQuotes[c9] == 4) {
                            int i47 = i46 + 1;
                            this.buf[i46] = 'u';
                            int i48 = i47 + 1;
                            this.buf[i47] = IOUtils.DIGITS[(c9 >>> '\f') & 15];
                            int i49 = i48 + 1;
                            this.buf[i48] = IOUtils.DIGITS[(c9 >>> '\b') & 15];
                            int i50 = i49 + 1;
                            this.buf[i49] = IOUtils.DIGITS[(c9 >>> 4) & 15];
                            i14 = i50 + 1;
                            this.buf[i50] = IOUtils.DIGITS[c9 & 15];
                        } else {
                            i14 = i46 + 1;
                            this.buf[i46] = IOUtils.replaceChars[c9];
                        }
                    }
                }
            }
        }
        if (c == 0) {
            this.buf[this.count - 1] = Typography.quote;
        } else {
            this.buf[this.count - 2] = Typography.quote;
            this.buf[this.count - 1] = c;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeStringWithSingleQuote(String str) {
        int i = 0;
        if (str == null) {
            int i2 = this.count + 4;
            if (i2 > this.buf.length) {
                expandCapacity(i2);
            }
            "null".getChars(0, 4, this.buf, this.count);
            this.count = i2;
            return;
        }
        int length = str.length();
        int i3 = this.count + length + 2;
        if (i3 > this.buf.length) {
            if (this.writer != null) {
                write(39);
                while (i < str.length()) {
                    char charAt = str.charAt(i);
                    if (charAt <= '\r' || charAt == '\\' || charAt == '\'' || (charAt == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                        write(92);
                        write(IOUtils.replaceChars[charAt]);
                    } else {
                        write(charAt);
                    }
                    i++;
                }
                write(39);
                return;
            }
            expandCapacity(i3);
        }
        int i4 = this.count + 1;
        int i5 = i4 + length;
        this.buf[this.count] = '\'';
        str.getChars(0, length, this.buf, i4);
        this.count = i3;
        int i6 = -1;
        char c = 0;
        for (int i7 = i4; i7 < i5; i7++) {
            char c2 = this.buf[i7];
            if (c2 <= '\r' || c2 == '\\' || c2 == '\'' || (c2 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                i++;
                i6 = i7;
                c = c2;
            }
        }
        int i8 = i3 + i;
        if (i8 > this.buf.length) {
            expandCapacity(i8);
        }
        this.count = i8;
        if (i == 1) {
            int i9 = i6 + 1;
            System.arraycopy(this.buf, i9, this.buf, i6 + 2, (i5 - i6) - 1);
            this.buf[i6] = '\\';
            this.buf[i9] = IOUtils.replaceChars[c];
        } else if (i > 1) {
            int i10 = i6 + 1;
            System.arraycopy(this.buf, i10, this.buf, i6 + 2, (i5 - i6) - 1);
            this.buf[i6] = '\\';
            this.buf[i10] = IOUtils.replaceChars[c];
            int i11 = i5 + 1;
            for (int i12 = i10 - 2; i12 >= i4; i12--) {
                char c3 = this.buf[i12];
                if (c3 <= '\r' || c3 == '\\' || c3 == '\'' || (c3 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                    int i13 = i12 + 1;
                    System.arraycopy(this.buf, i13, this.buf, i12 + 2, (i11 - i12) - 1);
                    this.buf[i12] = '\\';
                    this.buf[i13] = IOUtils.replaceChars[c3];
                    i11++;
                }
            }
        }
        this.buf[this.count - 1] = '\'';
    }

    protected void writeStringWithSingleQuote(char[] cArr) {
        int i = 0;
        if (cArr == null) {
            int i2 = this.count + 4;
            if (i2 > this.buf.length) {
                expandCapacity(i2);
            }
            "null".getChars(0, 4, this.buf, this.count);
            this.count = i2;
            return;
        }
        int length = cArr.length;
        int i3 = this.count + length + 2;
        if (i3 > this.buf.length) {
            if (this.writer != null) {
                write(39);
                while (i < cArr.length) {
                    char c = cArr[i];
                    if (c <= '\r' || c == '\\' || c == '\'' || (c == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                        write(92);
                        write(IOUtils.replaceChars[c]);
                    } else {
                        write(c);
                    }
                    i++;
                }
                write(39);
                return;
            }
            expandCapacity(i3);
        }
        int i4 = this.count + 1;
        int i5 = length + i4;
        this.buf[this.count] = '\'';
        System.arraycopy(cArr, 0, this.buf, i4, cArr.length);
        this.count = i3;
        int i6 = -1;
        char c2 = 0;
        for (int i7 = i4; i7 < i5; i7++) {
            char c3 = this.buf[i7];
            if (c3 <= '\r' || c3 == '\\' || c3 == '\'' || (c3 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                i++;
                i6 = i7;
                c2 = c3;
            }
        }
        int i8 = i3 + i;
        if (i8 > this.buf.length) {
            expandCapacity(i8);
        }
        this.count = i8;
        if (i == 1) {
            int i9 = i6 + 1;
            System.arraycopy(this.buf, i9, this.buf, i6 + 2, (i5 - i6) - 1);
            this.buf[i6] = '\\';
            this.buf[i9] = IOUtils.replaceChars[c2];
        } else if (i > 1) {
            int i10 = i6 + 1;
            System.arraycopy(this.buf, i10, this.buf, i6 + 2, (i5 - i6) - 1);
            this.buf[i6] = '\\';
            this.buf[i10] = IOUtils.replaceChars[c2];
            int i11 = i5 + 1;
            for (int i12 = i10 - 2; i12 >= i4; i12--) {
                char c4 = this.buf[i12];
                if (c4 <= '\r' || c4 == '\\' || c4 == '\'' || (c4 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                    int i13 = i12 + 1;
                    System.arraycopy(this.buf, i13, this.buf, i12 + 2, (i11 - i12) - 1);
                    this.buf[i12] = '\\';
                    this.buf[i13] = IOUtils.replaceChars[c4];
                    i11++;
                }
            }
        }
        this.buf[this.count - 1] = '\'';
    }

    public void writeTo(OutputStream outputStream, String str) throws IOException {
        writeTo(outputStream, Charset.forName(str));
    }

    public void writeTo(OutputStream outputStream, Charset charset) throws IOException {
        writeToEx(outputStream, charset);
    }

    public void writeTo(Writer writer) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        writer.write(this.buf, 0, this.count);
    }

    public int writeToEx(OutputStream outputStream, Charset charset) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        if (charset == IOUtils.UTF8) {
            return encodeToUTF8(outputStream);
        }
        byte[] bytes = new String(this.buf, 0, this.count).getBytes(charset);
        outputStream.write(bytes);
        return bytes.length;
    }
}
