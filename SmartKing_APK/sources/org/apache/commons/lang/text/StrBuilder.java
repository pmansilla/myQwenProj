package org.apache.commons.lang.text;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;

/* loaded from: classes2.dex */
public class StrBuilder implements Cloneable {
    static final int CAPACITY = 32;
    private static final long serialVersionUID = 7628716375283629643L;
    protected char[] buffer;
    private String newLine;
    private String nullText;
    protected int size;

    /* loaded from: classes2.dex */
    class StrBuilderReader extends Reader {
        private int mark;
        private int pos;

        StrBuilderReader() {
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // java.io.Reader
        public void mark(int i) {
            this.mark = this.pos;
        }

        @Override // java.io.Reader
        public boolean markSupported() {
            return true;
        }

        @Override // java.io.Reader
        public int read() {
            if (!ready()) {
                return -1;
            }
            StrBuilder strBuilder = StrBuilder.this;
            int i = this.pos;
            this.pos = i + 1;
            return strBuilder.charAt(i);
        }

        @Override // java.io.Reader
        public int read(char[] cArr, int i, int i2) {
            int i3;
            if (i < 0 || i2 < 0 || i > cArr.length || (i3 = i + i2) > cArr.length || i3 < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (i2 == 0) {
                return 0;
            }
            if (this.pos >= StrBuilder.this.size()) {
                return -1;
            }
            if (this.pos + i2 > StrBuilder.this.size()) {
                i2 = StrBuilder.this.size() - this.pos;
            }
            StrBuilder.this.getChars(this.pos, this.pos + i2, cArr, i);
            this.pos += i2;
            return i2;
        }

        @Override // java.io.Reader
        public boolean ready() {
            return this.pos < StrBuilder.this.size();
        }

        @Override // java.io.Reader
        public void reset() {
            this.pos = this.mark;
        }

        @Override // java.io.Reader
        public long skip(long j) {
            if (this.pos + j > StrBuilder.this.size()) {
                j = StrBuilder.this.size() - this.pos;
            }
            if (j < 0) {
                return 0L;
            }
            this.pos = (int) (this.pos + j);
            return j;
        }
    }

    /* loaded from: classes2.dex */
    class StrBuilderTokenizer extends StrTokenizer {
        StrBuilderTokenizer() {
        }

        @Override // org.apache.commons.lang.text.StrTokenizer
        public String getContent() {
            String content = super.getContent();
            return content == null ? StrBuilder.this.toString() : content;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.apache.commons.lang.text.StrTokenizer
        public List tokenize(char[] cArr, int i, int i2) {
            return cArr == null ? super.tokenize(StrBuilder.this.buffer, 0, StrBuilder.this.size()) : super.tokenize(cArr, i, i2);
        }
    }

    /* loaded from: classes2.dex */
    class StrBuilderWriter extends Writer {
        StrBuilderWriter() {
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
        }

        @Override // java.io.Writer
        public void write(int i) {
            StrBuilder.this.append((char) i);
        }

        @Override // java.io.Writer
        public void write(String str) {
            StrBuilder.this.append(str);
        }

        @Override // java.io.Writer
        public void write(String str, int i, int i2) {
            StrBuilder.this.append(str, i, i2);
        }

        @Override // java.io.Writer
        public void write(char[] cArr) {
            StrBuilder.this.append(cArr);
        }

        @Override // java.io.Writer
        public void write(char[] cArr, int i, int i2) {
            StrBuilder.this.append(cArr, i, i2);
        }
    }

    public StrBuilder() {
        this(32);
    }

    public StrBuilder(int i) {
        this.buffer = new char[i <= 0 ? 32 : i];
    }

    public StrBuilder(String str) {
        if (str == null) {
            this.buffer = new char[32];
        } else {
            this.buffer = new char[str.length() + 32];
            append(str);
        }
    }

    private void deleteImpl(int i, int i2, int i3) {
        System.arraycopy(this.buffer, i2, this.buffer, i, this.size - i2);
        this.size -= i3;
    }

    private StrBuilder replaceImpl(StrMatcher strMatcher, String str, int i, int i2, int i3) {
        if (strMatcher == null || this.size == 0) {
            return this;
        }
        int length = str == null ? 0 : str.length();
        char[] cArr = this.buffer;
        int i4 = i3;
        int i5 = i2;
        int i6 = i;
        while (i6 < i5 && i4 != 0) {
            int isMatch = strMatcher.isMatch(cArr, i6, i, i5);
            if (isMatch > 0) {
                replaceImpl(i6, i6 + isMatch, isMatch, str, length);
                i5 = (i5 - isMatch) + length;
                i6 = (i6 + length) - 1;
                if (i4 > 0) {
                    i4--;
                }
            }
            i6++;
        }
        return this;
    }

    private void replaceImpl(int i, int i2, int i3, String str, int i4) {
        int i5 = (this.size - i3) + i4;
        if (i4 != i3) {
            ensureCapacity(i5);
            System.arraycopy(this.buffer, i2, this.buffer, i + i4, this.size - i2);
            this.size = i5;
        }
        if (i4 > 0) {
            str.getChars(0, i4, this.buffer, i);
        }
    }

    public StrBuilder append(char c) {
        ensureCapacity(length() + 1);
        char[] cArr = this.buffer;
        int i = this.size;
        this.size = i + 1;
        cArr[i] = c;
        return this;
    }

    public StrBuilder append(double d) {
        return append(String.valueOf(d));
    }

    public StrBuilder append(float f) {
        return append(String.valueOf(f));
    }

    public StrBuilder append(int i) {
        return append(String.valueOf(i));
    }

    public StrBuilder append(long j) {
        return append(String.valueOf(j));
    }

    public StrBuilder append(Object obj) {
        return obj == null ? appendNull() : append(obj.toString());
    }

    public StrBuilder append(String str) {
        if (str == null) {
            return appendNull();
        }
        int length = str.length();
        if (length > 0) {
            int length2 = length();
            ensureCapacity(length2 + length);
            str.getChars(0, length, this.buffer, length2);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(String str, int i, int i2) {
        int i3;
        if (str == null) {
            return appendNull();
        }
        if (i < 0 || i > str.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (i2 < 0 || (i3 = i + i2) > str.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (i2 > 0) {
            int length = length();
            ensureCapacity(length + i2);
            str.getChars(i, i3, this.buffer, length);
            this.size += i2;
        }
        return this;
    }

    public StrBuilder append(StringBuffer stringBuffer) {
        if (stringBuffer == null) {
            return appendNull();
        }
        int length = stringBuffer.length();
        if (length > 0) {
            int length2 = length();
            ensureCapacity(length2 + length);
            stringBuffer.getChars(0, length, this.buffer, length2);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(StringBuffer stringBuffer, int i, int i2) {
        int i3;
        if (stringBuffer == null) {
            return appendNull();
        }
        if (i < 0 || i > stringBuffer.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (i2 < 0 || (i3 = i + i2) > stringBuffer.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (i2 > 0) {
            int length = length();
            ensureCapacity(length + i2);
            stringBuffer.getChars(i, i3, this.buffer, length);
            this.size += i2;
        }
        return this;
    }

    public StrBuilder append(StrBuilder strBuilder) {
        if (strBuilder == null) {
            return appendNull();
        }
        int length = strBuilder.length();
        if (length > 0) {
            int length2 = length();
            ensureCapacity(length2 + length);
            System.arraycopy(strBuilder.buffer, 0, this.buffer, length2, length);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(StrBuilder strBuilder, int i, int i2) {
        int i3;
        if (strBuilder == null) {
            return appendNull();
        }
        if (i < 0 || i > strBuilder.length()) {
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        if (i2 < 0 || (i3 = i + i2) > strBuilder.length()) {
            throw new StringIndexOutOfBoundsException("length must be valid");
        }
        if (i2 > 0) {
            int length = length();
            ensureCapacity(length + i2);
            strBuilder.getChars(i, i3, this.buffer, length);
            this.size += i2;
        }
        return this;
    }

    public StrBuilder append(boolean z) {
        if (z) {
            ensureCapacity(this.size + 4);
            char[] cArr = this.buffer;
            int i = this.size;
            this.size = i + 1;
            cArr[i] = 't';
            char[] cArr2 = this.buffer;
            int i2 = this.size;
            this.size = i2 + 1;
            cArr2[i2] = 'r';
            char[] cArr3 = this.buffer;
            int i3 = this.size;
            this.size = i3 + 1;
            cArr3[i3] = 'u';
            char[] cArr4 = this.buffer;
            int i4 = this.size;
            this.size = i4 + 1;
            cArr4[i4] = 'e';
        } else {
            ensureCapacity(this.size + 5);
            char[] cArr5 = this.buffer;
            int i5 = this.size;
            this.size = i5 + 1;
            cArr5[i5] = 'f';
            char[] cArr6 = this.buffer;
            int i6 = this.size;
            this.size = i6 + 1;
            cArr6[i6] = 'a';
            char[] cArr7 = this.buffer;
            int i7 = this.size;
            this.size = i7 + 1;
            cArr7[i7] = 'l';
            char[] cArr8 = this.buffer;
            int i8 = this.size;
            this.size = i8 + 1;
            cArr8[i8] = 's';
            char[] cArr9 = this.buffer;
            int i9 = this.size;
            this.size = i9 + 1;
            cArr9[i9] = 'e';
        }
        return this;
    }

    public StrBuilder append(char[] cArr) {
        if (cArr == null) {
            return appendNull();
        }
        int length = cArr.length;
        if (length > 0) {
            int length2 = length();
            ensureCapacity(length2 + length);
            System.arraycopy(cArr, 0, this.buffer, length2, length);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(char[] cArr, int i, int i2) {
        if (cArr == null) {
            return appendNull();
        }
        if (i < 0 || i > cArr.length) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Invalid startIndex: ");
            stringBuffer.append(i2);
            throw new StringIndexOutOfBoundsException(stringBuffer.toString());
        }
        if (i2 < 0 || i + i2 > cArr.length) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Invalid length: ");
            stringBuffer2.append(i2);
            throw new StringIndexOutOfBoundsException(stringBuffer2.toString());
        }
        if (i2 > 0) {
            int length = length();
            ensureCapacity(length + i2);
            System.arraycopy(cArr, i, this.buffer, length, i2);
            this.size += i2;
        }
        return this;
    }

    public StrBuilder appendAll(Collection collection) {
        if (collection != null && collection.size() > 0) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                append(it.next());
            }
        }
        return this;
    }

    public StrBuilder appendAll(Iterator it) {
        if (it != null) {
            while (it.hasNext()) {
                append(it.next());
            }
        }
        return this;
    }

    public StrBuilder appendAll(Object[] objArr) {
        if (objArr != null && objArr.length > 0) {
            for (Object obj : objArr) {
                append(obj);
            }
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(int i, int i2, char c) {
        return appendFixedWidthPadLeft(String.valueOf(i), i2, c);
    }

    public StrBuilder appendFixedWidthPadLeft(Object obj, int i, char c) {
        if (i > 0) {
            ensureCapacity(this.size + i);
            String nullText = obj == null ? getNullText() : obj.toString();
            if (nullText == null) {
                nullText = "";
            }
            int length = nullText.length();
            if (length >= i) {
                nullText.getChars(length - i, length, this.buffer, this.size);
            } else {
                int i2 = i - length;
                for (int i3 = 0; i3 < i2; i3++) {
                    this.buffer[this.size + i3] = c;
                }
                nullText.getChars(0, length, this.buffer, this.size + i2);
            }
            this.size += i;
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadRight(int i, int i2, char c) {
        return appendFixedWidthPadRight(String.valueOf(i), i2, c);
    }

    public StrBuilder appendFixedWidthPadRight(Object obj, int i, char c) {
        if (i > 0) {
            ensureCapacity(this.size + i);
            String nullText = obj == null ? getNullText() : obj.toString();
            if (nullText == null) {
                nullText = "";
            }
            int length = nullText.length();
            if (length >= i) {
                nullText.getChars(0, i, this.buffer, this.size);
            } else {
                int i2 = i - length;
                nullText.getChars(0, length, this.buffer, this.size);
                for (int i3 = 0; i3 < i2; i3++) {
                    this.buffer[this.size + length + i3] = c;
                }
            }
            this.size += i;
        }
        return this;
    }

    public StrBuilder appendNewLine() {
        if (this.newLine != null) {
            return append(this.newLine);
        }
        append(SystemUtils.LINE_SEPARATOR);
        return this;
    }

    public StrBuilder appendNull() {
        return this.nullText == null ? this : append(this.nullText);
    }

    public StrBuilder appendPadding(int i, char c) {
        if (i >= 0) {
            ensureCapacity(this.size + i);
            for (int i2 = 0; i2 < i; i2++) {
                char[] cArr = this.buffer;
                int i3 = this.size;
                this.size = i3 + 1;
                cArr[i3] = c;
            }
        }
        return this;
    }

    public StrBuilder appendSeparator(char c) {
        if (size() > 0) {
            append(c);
        }
        return this;
    }

    public StrBuilder appendSeparator(char c, int i) {
        if (i > 0) {
            append(c);
        }
        return this;
    }

    public StrBuilder appendSeparator(String str) {
        if (str != null && size() > 0) {
            append(str);
        }
        return this;
    }

    public StrBuilder appendSeparator(String str, int i) {
        if (str != null && i > 0) {
            append(str);
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Collection collection, String str) {
        if (collection != null && collection.size() > 0) {
            if (str == null) {
                str = "";
            }
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(str);
                }
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Iterator it, String str) {
        if (it != null) {
            if (str == null) {
                str = "";
            }
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(str);
                }
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Object[] objArr, String str) {
        if (objArr != null && objArr.length > 0) {
            if (str == null) {
                str = "";
            }
            append(objArr[0]);
            for (int i = 1; i < objArr.length; i++) {
                append(str);
                append(objArr[i]);
            }
        }
        return this;
    }

    public StrBuilder appendln(char c) {
        return append(c).appendNewLine();
    }

    public StrBuilder appendln(double d) {
        return append(d).appendNewLine();
    }

    public StrBuilder appendln(float f) {
        return append(f).appendNewLine();
    }

    public StrBuilder appendln(int i) {
        return append(i).appendNewLine();
    }

    public StrBuilder appendln(long j) {
        return append(j).appendNewLine();
    }

    public StrBuilder appendln(Object obj) {
        return append(obj).appendNewLine();
    }

    public StrBuilder appendln(String str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(String str, int i, int i2) {
        return append(str, i, i2).appendNewLine();
    }

    public StrBuilder appendln(StringBuffer stringBuffer) {
        return append(stringBuffer).appendNewLine();
    }

    public StrBuilder appendln(StringBuffer stringBuffer, int i, int i2) {
        return append(stringBuffer, i, i2).appendNewLine();
    }

    public StrBuilder appendln(StrBuilder strBuilder) {
        return append(strBuilder).appendNewLine();
    }

    public StrBuilder appendln(StrBuilder strBuilder, int i, int i2) {
        return append(strBuilder, i, i2).appendNewLine();
    }

    public StrBuilder appendln(boolean z) {
        return append(z).appendNewLine();
    }

    public StrBuilder appendln(char[] cArr) {
        return append(cArr).appendNewLine();
    }

    public StrBuilder appendln(char[] cArr, int i, int i2) {
        return append(cArr, i, i2).appendNewLine();
    }

    public Reader asReader() {
        return new StrBuilderReader();
    }

    public StrTokenizer asTokenizer() {
        return new StrBuilderTokenizer();
    }

    public Writer asWriter() {
        return new StrBuilderWriter();
    }

    public int capacity() {
        return this.buffer.length;
    }

    public char charAt(int i) {
        if (i < 0 || i >= length()) {
            throw new StringIndexOutOfBoundsException(i);
        }
        return this.buffer[i];
    }

    public StrBuilder clear() {
        this.size = 0;
        return this;
    }

    public boolean contains(char c) {
        char[] cArr = this.buffer;
        for (int i = 0; i < this.size; i++) {
            if (cArr[i] == c) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(String str) {
        return indexOf(str, 0) >= 0;
    }

    public boolean contains(StrMatcher strMatcher) {
        return indexOf(strMatcher, 0) >= 0;
    }

    public StrBuilder delete(int i, int i2) {
        int validateRange = validateRange(i, i2);
        int i3 = validateRange - i;
        if (i3 > 0) {
            deleteImpl(i, validateRange, i3);
        }
        return this;
    }

    public StrBuilder deleteAll(char c) {
        int i = 0;
        while (i < this.size) {
            if (this.buffer[i] == c) {
                int i2 = i;
                do {
                    i2++;
                    if (i2 >= this.size) {
                        break;
                    }
                } while (this.buffer[i2] == c);
                int i3 = i2 - i;
                deleteImpl(i, i2, i3);
                i = i2 - i3;
            }
            i++;
        }
        return this;
    }

    public StrBuilder deleteAll(String str) {
        int length = str == null ? 0 : str.length();
        if (length > 0) {
            int indexOf = indexOf(str, 0);
            while (indexOf >= 0) {
                deleteImpl(indexOf, indexOf + length, length);
                indexOf = indexOf(str, indexOf);
            }
        }
        return this;
    }

    public StrBuilder deleteAll(StrMatcher strMatcher) {
        return replace(strMatcher, null, 0, this.size, -1);
    }

    public StrBuilder deleteCharAt(int i) {
        if (i < 0 || i >= this.size) {
            throw new StringIndexOutOfBoundsException(i);
        }
        deleteImpl(i, i + 1, 1);
        return this;
    }

    public StrBuilder deleteFirst(char c) {
        int i = 0;
        while (true) {
            if (i >= this.size) {
                break;
            }
            if (this.buffer[i] == c) {
                deleteImpl(i, i + 1, 1);
                break;
            }
            i++;
        }
        return this;
    }

    public StrBuilder deleteFirst(String str) {
        int indexOf;
        int length = str == null ? 0 : str.length();
        if (length > 0 && (indexOf = indexOf(str, 0)) >= 0) {
            deleteImpl(indexOf, indexOf + length, length);
        }
        return this;
    }

    public StrBuilder deleteFirst(StrMatcher strMatcher) {
        return replace(strMatcher, null, 0, this.size, 1);
    }

    public boolean endsWith(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return true;
        }
        if (length > this.size) {
            return false;
        }
        int i = this.size - length;
        int i2 = 0;
        while (i2 < length) {
            if (this.buffer[i] != str.charAt(i2)) {
                return false;
            }
            i2++;
            i++;
        }
        return true;
    }

    public StrBuilder ensureCapacity(int i) {
        if (i > this.buffer.length) {
            char[] cArr = this.buffer;
            this.buffer = new char[i];
            System.arraycopy(cArr, 0, this.buffer, 0, this.size);
        }
        return this;
    }

    public boolean equals(Object obj) {
        if (obj instanceof StrBuilder) {
            return equals((StrBuilder) obj);
        }
        return false;
    }

    public boolean equals(StrBuilder strBuilder) {
        if (this == strBuilder) {
            return true;
        }
        if (this.size != strBuilder.size) {
            return false;
        }
        char[] cArr = this.buffer;
        char[] cArr2 = strBuilder.buffer;
        for (int i = this.size - 1; i >= 0; i--) {
            if (cArr[i] != cArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean equalsIgnoreCase(StrBuilder strBuilder) {
        if (this == strBuilder) {
            return true;
        }
        if (this.size != strBuilder.size) {
            return false;
        }
        char[] cArr = this.buffer;
        char[] cArr2 = strBuilder.buffer;
        for (int i = this.size - 1; i >= 0; i--) {
            char c = cArr[i];
            char c2 = cArr2[i];
            if (c != c2 && Character.toUpperCase(c) != Character.toUpperCase(c2)) {
                return false;
            }
        }
        return true;
    }

    public void getChars(int i, int i2, char[] cArr, int i3) {
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        }
        if (i2 < 0 || i2 > length()) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i > i2) {
            throw new StringIndexOutOfBoundsException("end < start");
        }
        System.arraycopy(this.buffer, i, cArr, i3, i2 - i);
    }

    public char[] getChars(char[] cArr) {
        int length = length();
        if (cArr == null || cArr.length < length) {
            cArr = new char[length];
        }
        System.arraycopy(this.buffer, 0, cArr, 0, length);
        return cArr;
    }

    public String getNewLineText() {
        return this.newLine;
    }

    public String getNullText() {
        return this.nullText;
    }

    public int hashCode() {
        char[] cArr = this.buffer;
        int i = 0;
        for (int i2 = this.size - 1; i2 >= 0; i2--) {
            i = (i * 31) + cArr[i2];
        }
        return i;
    }

    public int indexOf(char c) {
        return indexOf(c, 0);
    }

    public int indexOf(char c, int i) {
        if (i < 0) {
            i = 0;
        }
        if (i >= this.size) {
            return -1;
        }
        char[] cArr = this.buffer;
        while (i < this.size) {
            if (cArr[i] == c) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x0039, code lost:
    
        r10 = r10 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int indexOf(java.lang.String r9, int r10) {
        /*
            r8 = this;
            r0 = 0
            if (r10 >= 0) goto L4
            r10 = 0
        L4:
            r1 = -1
            if (r9 == 0) goto L41
            int r2 = r8.size
            if (r10 < r2) goto Lc
            goto L41
        Lc:
            int r2 = r9.length()
            r3 = 1
            if (r2 != r3) goto L1c
            char r9 = r9.charAt(r0)
            int r9 = r8.indexOf(r9, r10)
            return r9
        L1c:
            if (r2 != 0) goto L1f
            return r10
        L1f:
            int r4 = r8.size
            if (r2 <= r4) goto L24
            return r1
        L24:
            char[] r4 = r8.buffer
            int r5 = r8.size
            int r5 = r5 - r2
            int r5 = r5 + r3
        L2a:
            if (r10 >= r5) goto L40
            r3 = 0
        L2d:
            if (r3 >= r2) goto L3f
            char r6 = r9.charAt(r3)
            int r7 = r10 + r3
            char r7 = r4[r7]
            if (r6 == r7) goto L3c
            int r10 = r10 + 1
            goto L2a
        L3c:
            int r3 = r3 + 1
            goto L2d
        L3f:
            return r10
        L40:
            return r1
        L41:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.text.StrBuilder.indexOf(java.lang.String, int):int");
    }

    public int indexOf(StrMatcher strMatcher) {
        return indexOf(strMatcher, 0);
    }

    public int indexOf(StrMatcher strMatcher, int i) {
        if (i < 0) {
            i = 0;
        }
        if (strMatcher == null || i >= this.size) {
            return -1;
        }
        int i2 = this.size;
        char[] cArr = this.buffer;
        for (int i3 = i; i3 < i2; i3++) {
            if (strMatcher.isMatch(cArr, i3, i, i2) > 0) {
                return i3;
            }
        }
        return -1;
    }

    public StrBuilder insert(int i, char c) {
        validateIndex(i);
        ensureCapacity(this.size + 1);
        System.arraycopy(this.buffer, i, this.buffer, i + 1, this.size - i);
        this.buffer[i] = c;
        this.size++;
        return this;
    }

    public StrBuilder insert(int i, double d) {
        return insert(i, String.valueOf(d));
    }

    public StrBuilder insert(int i, float f) {
        return insert(i, String.valueOf(f));
    }

    public StrBuilder insert(int i, int i2) {
        return insert(i, String.valueOf(i2));
    }

    public StrBuilder insert(int i, long j) {
        return insert(i, String.valueOf(j));
    }

    public StrBuilder insert(int i, Object obj) {
        return obj == null ? insert(i, this.nullText) : insert(i, obj.toString());
    }

    public StrBuilder insert(int i, String str) {
        validateIndex(i);
        if (str == null) {
            str = this.nullText;
        }
        int length = str == null ? 0 : str.length();
        if (length > 0) {
            int i2 = this.size + length;
            ensureCapacity(i2);
            System.arraycopy(this.buffer, i, this.buffer, i + length, this.size - i);
            this.size = i2;
            str.getChars(0, length, this.buffer, i);
        }
        return this;
    }

    public StrBuilder insert(int i, boolean z) {
        validateIndex(i);
        if (z) {
            ensureCapacity(this.size + 4);
            System.arraycopy(this.buffer, i, this.buffer, i + 4, this.size - i);
            int i2 = i + 1;
            this.buffer[i] = 't';
            int i3 = i2 + 1;
            this.buffer[i2] = 'r';
            this.buffer[i3] = 'u';
            this.buffer[i3 + 1] = 'e';
            this.size += 4;
        } else {
            ensureCapacity(this.size + 5);
            System.arraycopy(this.buffer, i, this.buffer, i + 5, this.size - i);
            int i4 = i + 1;
            this.buffer[i] = 'f';
            int i5 = i4 + 1;
            this.buffer[i4] = 'a';
            int i6 = i5 + 1;
            this.buffer[i5] = 'l';
            this.buffer[i6] = 's';
            this.buffer[i6 + 1] = 'e';
            this.size += 5;
        }
        return this;
    }

    public StrBuilder insert(int i, char[] cArr) {
        validateIndex(i);
        if (cArr == null) {
            return insert(i, this.nullText);
        }
        int length = cArr.length;
        if (length > 0) {
            ensureCapacity(this.size + length);
            System.arraycopy(this.buffer, i, this.buffer, i + length, this.size - i);
            System.arraycopy(cArr, 0, this.buffer, i, length);
            this.size += length;
        }
        return this;
    }

    public StrBuilder insert(int i, char[] cArr, int i2, int i3) {
        validateIndex(i);
        if (cArr == null) {
            return insert(i, this.nullText);
        }
        if (i2 < 0 || i2 > cArr.length) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Invalid offset: ");
            stringBuffer.append(i2);
            throw new StringIndexOutOfBoundsException(stringBuffer.toString());
        }
        if (i3 < 0 || i2 + i3 > cArr.length) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Invalid length: ");
            stringBuffer2.append(i3);
            throw new StringIndexOutOfBoundsException(stringBuffer2.toString());
        }
        if (i3 > 0) {
            ensureCapacity(this.size + i3);
            System.arraycopy(this.buffer, i, this.buffer, i + i3, this.size - i);
            System.arraycopy(cArr, i2, this.buffer, i, i3);
            this.size += i3;
        }
        return this;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int lastIndexOf(char c) {
        return lastIndexOf(c, this.size - 1);
    }

    public int lastIndexOf(char c, int i) {
        if (i >= this.size) {
            i = this.size - 1;
        }
        if (i < 0) {
            return -1;
        }
        while (i >= 0) {
            if (this.buffer[i] == c) {
                return i;
            }
            i--;
        }
        return -1;
    }

    public int lastIndexOf(String str) {
        return lastIndexOf(str, this.size - 1);
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0037, code lost:
    
        r9 = r9 - 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int lastIndexOf(java.lang.String r8, int r9) {
        /*
            r7 = this;
            int r0 = r7.size
            r1 = 1
            if (r9 < r0) goto L8
            int r9 = r7.size
            int r9 = r9 - r1
        L8:
            r0 = -1
            if (r8 == 0) goto L42
            if (r9 >= 0) goto Le
            goto L42
        Le:
            int r2 = r8.length()
            if (r2 <= 0) goto L3e
            int r3 = r7.size
            if (r2 > r3) goto L3e
            r3 = 0
            if (r2 != r1) goto L24
            char r8 = r8.charAt(r3)
            int r8 = r7.lastIndexOf(r8, r9)
            return r8
        L24:
            int r9 = r9 - r2
            int r9 = r9 + r1
        L26:
            if (r9 < 0) goto L41
            r1 = 0
        L29:
            if (r1 >= r2) goto L3d
            char r4 = r8.charAt(r1)
            char[] r5 = r7.buffer
            int r6 = r9 + r1
            char r5 = r5[r6]
            if (r4 == r5) goto L3a
            int r9 = r9 + (-1)
            goto L26
        L3a:
            int r1 = r1 + 1
            goto L29
        L3d:
            return r9
        L3e:
            if (r2 != 0) goto L41
            return r9
        L41:
            return r0
        L42:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.text.StrBuilder.lastIndexOf(java.lang.String, int):int");
    }

    public int lastIndexOf(StrMatcher strMatcher) {
        return lastIndexOf(strMatcher, this.size);
    }

    public int lastIndexOf(StrMatcher strMatcher, int i) {
        if (i >= this.size) {
            i = this.size - 1;
        }
        if (strMatcher == null || i < 0) {
            return -1;
        }
        char[] cArr = this.buffer;
        int i2 = i + 1;
        while (i >= 0) {
            if (strMatcher.isMatch(cArr, i, 0, i2) > 0) {
                return i;
            }
            i--;
        }
        return -1;
    }

    public String leftString(int i) {
        return i <= 0 ? "" : i >= this.size ? new String(this.buffer, 0, this.size) : new String(this.buffer, 0, i);
    }

    public int length() {
        return this.size;
    }

    public String midString(int i, int i2) {
        if (i < 0) {
            i = 0;
        }
        return (i2 <= 0 || i >= this.size) ? "" : this.size <= i + i2 ? new String(this.buffer, i, this.size - i) : new String(this.buffer, i, i2);
    }

    public StrBuilder minimizeCapacity() {
        if (this.buffer.length > length()) {
            char[] cArr = this.buffer;
            this.buffer = new char[length()];
            System.arraycopy(cArr, 0, this.buffer, 0, this.size);
        }
        return this;
    }

    public StrBuilder replace(int i, int i2, String str) {
        int validateRange = validateRange(i, i2);
        replaceImpl(i, validateRange, validateRange - i, str, str == null ? 0 : str.length());
        return this;
    }

    public StrBuilder replace(StrMatcher strMatcher, String str, int i, int i2, int i3) {
        return replaceImpl(strMatcher, str, i, validateRange(i, i2), i3);
    }

    public StrBuilder replaceAll(char c, char c2) {
        if (c != c2) {
            for (int i = 0; i < this.size; i++) {
                if (this.buffer[i] == c) {
                    this.buffer[i] = c2;
                }
            }
        }
        return this;
    }

    public StrBuilder replaceAll(String str, String str2) {
        int length = str == null ? 0 : str.length();
        if (length > 0) {
            int length2 = str2 == null ? 0 : str2.length();
            int indexOf = indexOf(str, 0);
            while (indexOf >= 0) {
                replaceImpl(indexOf, indexOf + length, length, str2, length2);
                indexOf = indexOf(str, indexOf + length2);
            }
        }
        return this;
    }

    public StrBuilder replaceAll(StrMatcher strMatcher, String str) {
        return replace(strMatcher, str, 0, this.size, -1);
    }

    public StrBuilder replaceFirst(char c, char c2) {
        if (c != c2) {
            int i = 0;
            while (true) {
                if (i >= this.size) {
                    break;
                }
                if (this.buffer[i] == c) {
                    this.buffer[i] = c2;
                    break;
                }
                i++;
            }
        }
        return this;
    }

    public StrBuilder replaceFirst(String str, String str2) {
        int indexOf;
        int length = str == null ? 0 : str.length();
        if (length > 0 && (indexOf = indexOf(str, 0)) >= 0) {
            replaceImpl(indexOf, indexOf + length, length, str2, str2 == null ? 0 : str2.length());
        }
        return this;
    }

    public StrBuilder replaceFirst(StrMatcher strMatcher, String str) {
        return replace(strMatcher, str, 0, this.size, 1);
    }

    public StrBuilder reverse() {
        if (this.size == 0) {
            return this;
        }
        int i = this.size / 2;
        char[] cArr = this.buffer;
        int i2 = 0;
        int i3 = this.size - 1;
        while (i2 < i) {
            char c = cArr[i2];
            cArr[i2] = cArr[i3];
            cArr[i3] = c;
            i2++;
            i3--;
        }
        return this;
    }

    public String rightString(int i) {
        return i <= 0 ? "" : i >= this.size ? new String(this.buffer, 0, this.size) : new String(this.buffer, this.size - i, i);
    }

    public StrBuilder setCharAt(int i, char c) {
        if (i < 0 || i >= length()) {
            throw new StringIndexOutOfBoundsException(i);
        }
        this.buffer[i] = c;
        return this;
    }

    public StrBuilder setLength(int i) {
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        }
        if (i < this.size) {
            this.size = i;
        } else if (i > this.size) {
            ensureCapacity(i);
            this.size = i;
            for (int i2 = this.size; i2 < i; i2++) {
                this.buffer[i2] = 0;
            }
        }
        return this;
    }

    public StrBuilder setNewLineText(String str) {
        this.newLine = str;
        return this;
    }

    public StrBuilder setNullText(String str) {
        if (str != null && str.length() == 0) {
            str = null;
        }
        this.nullText = str;
        return this;
    }

    public int size() {
        return this.size;
    }

    public boolean startsWith(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return true;
        }
        if (length > this.size) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (this.buffer[i] != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public String substring(int i) {
        return substring(i, this.size);
    }

    public String substring(int i, int i2) {
        return new String(this.buffer, i, validateRange(i, i2) - i);
    }

    public char[] toCharArray() {
        if (this.size == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        char[] cArr = new char[this.size];
        System.arraycopy(this.buffer, 0, cArr, 0, this.size);
        return cArr;
    }

    public char[] toCharArray(int i, int i2) {
        int validateRange = validateRange(i, i2) - i;
        if (validateRange == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        char[] cArr = new char[validateRange];
        System.arraycopy(this.buffer, i, cArr, 0, validateRange);
        return cArr;
    }

    public String toString() {
        return new String(this.buffer, 0, this.size);
    }

    public StringBuffer toStringBuffer() {
        StringBuffer stringBuffer = new StringBuffer(this.size);
        stringBuffer.append(this.buffer, 0, this.size);
        return stringBuffer;
    }

    public StrBuilder trim() {
        if (this.size == 0) {
            return this;
        }
        int i = this.size;
        char[] cArr = this.buffer;
        int i2 = 0;
        while (i2 < i && cArr[i2] <= ' ') {
            i2++;
        }
        while (i2 < i && cArr[i - 1] <= ' ') {
            i--;
        }
        if (i < this.size) {
            delete(i, this.size);
        }
        if (i2 > 0) {
            delete(0, i2);
        }
        return this;
    }

    protected void validateIndex(int i) {
        if (i < 0 || i > this.size) {
            throw new StringIndexOutOfBoundsException(i);
        }
    }

    protected int validateRange(int i, int i2) {
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        }
        if (i2 > this.size) {
            i2 = this.size;
        }
        if (i <= i2) {
            return i2;
        }
        throw new StringIndexOutOfBoundsException("end < start");
    }
}
