package cn.sharesdk.framework.utils;

import java.io.IOException;

/* compiled from: UnicodeEscaper.java */
/* loaded from: classes.dex */
public abstract class i implements Escaper {

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: UnicodeEscaper.java */
    /* loaded from: classes.dex */
    public static final class a extends ThreadLocal<char[]> {
        private a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public char[] initialValue() {
            return new char[1024];
        }
    }

    private static final char[] a(char[] cArr, int i, int i2) {
        char[] cArr2 = new char[i2];
        if (i > 0) {
            System.arraycopy(cArr, 0, cArr2, 0, i);
        }
        return cArr2;
    }

    protected static final int b(CharSequence charSequence, int i, int i2) {
        if (i >= i2) {
            throw new IndexOutOfBoundsException("Index exceeds specified range");
        }
        char charAt = charSequence.charAt(i);
        int i3 = i + 1;
        if (charAt < 55296 || charAt > 57343) {
            return charAt;
        }
        if (charAt > 56319) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected low surrogate character '");
            sb.append(charAt);
            sb.append("' with value ");
            sb.append((int) charAt);
            sb.append(" at index ");
            sb.append(i3 - 1);
            throw new IllegalArgumentException(sb.toString());
        }
        if (i3 == i2) {
            return -charAt;
        }
        char charAt2 = charSequence.charAt(i3);
        if (Character.isLowSurrogate(charAt2)) {
            return Character.toCodePoint(charAt, charAt2);
        }
        throw new IllegalArgumentException("Expected low surrogate but got char '" + charAt2 + "' with value " + ((int) charAt2) + " at index " + i3);
    }

    protected int a(CharSequence charSequence, int i, int i2) {
        while (i < i2) {
            int b = b(charSequence, i, i2);
            if (b < 0 || a(b) != null) {
                break;
            }
            i += Character.isSupplementaryCodePoint(b) ? 2 : 1;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String a(String str, int i) {
        int i2;
        int length = str.length();
        char[] cArr = new a().get();
        int i3 = 0;
        int i4 = 0;
        while (i < length) {
            int b = b(str, i, length);
            if (b < 0) {
                throw new IllegalArgumentException("Trailing high surrogate at end of input");
            }
            char[] a2 = a(b);
            if (a2 != null) {
                int i5 = i - i3;
                int i6 = i4 + i5;
                int length2 = a2.length + i6;
                if (cArr.length < length2) {
                    cArr = a(cArr, i4, length2 + (length - i) + 32);
                }
                if (i5 > 0) {
                    str.getChars(i3, i, cArr, i4);
                    i4 = i6;
                }
                if (a2.length > 0) {
                    System.arraycopy(a2, 0, cArr, i4, a2.length);
                    i4 += a2.length;
                }
            }
            i3 = (Character.isSupplementaryCodePoint(b) ? 2 : 1) + i;
            i = a(str, i3, length);
        }
        int i7 = length - i3;
        if (i7 > 0) {
            i2 = i7 + i4;
            if (cArr.length < i2) {
                cArr = a(cArr, i4, i2);
            }
            str.getChars(i3, length, cArr, i4);
        } else {
            i2 = i4;
        }
        return new String(cArr, 0, i2);
    }

    protected abstract char[] a(int i);

    @Override // cn.sharesdk.framework.utils.Escaper
    public Appendable escape(final Appendable appendable) {
        c.a(appendable);
        return new Appendable() { // from class: cn.sharesdk.framework.utils.i.1
            int a = -1;
            char[] b = new char[2];

            private void a(char[] cArr, int i) throws IOException {
                for (int i2 = 0; i2 < i; i2++) {
                    appendable.append(cArr[i2]);
                }
            }

            @Override // java.lang.Appendable
            public Appendable append(char c) throws IOException {
                if (this.a != -1) {
                    if (!Character.isLowSurrogate(c)) {
                        throw new IllegalArgumentException("Expected low surrogate character but got '" + c + "' with value " + ((int) c));
                    }
                    char[] a2 = i.this.a(Character.toCodePoint((char) this.a, c));
                    if (a2 != null) {
                        a(a2, a2.length);
                    } else {
                        appendable.append((char) this.a);
                        appendable.append(c);
                    }
                    this.a = -1;
                } else if (Character.isHighSurrogate(c)) {
                    this.a = c;
                } else {
                    if (Character.isLowSurrogate(c)) {
                        throw new IllegalArgumentException("Unexpected low surrogate character '" + c + "' with value " + ((int) c));
                    }
                    char[] a3 = i.this.a(c);
                    if (a3 != null) {
                        a(a3, a3.length);
                    } else {
                        appendable.append(c);
                    }
                }
                return this;
            }

            @Override // java.lang.Appendable
            public Appendable append(CharSequence charSequence) throws IOException {
                return append(charSequence, 0, charSequence.length());
            }

            @Override // java.lang.Appendable
            public Appendable append(CharSequence charSequence, int i, int i2) throws IOException {
                int i3;
                if (i < i2) {
                    if (this.a != -1) {
                        char charAt = charSequence.charAt(i);
                        int i4 = i + 1;
                        if (!Character.isLowSurrogate(charAt)) {
                            throw new IllegalArgumentException("Expected low surrogate character but got " + charAt);
                        }
                        char[] a2 = i.this.a(Character.toCodePoint((char) this.a, charAt));
                        if (a2 != null) {
                            a(a2, a2.length);
                            i = i4;
                        } else {
                            appendable.append((char) this.a);
                        }
                        this.a = -1;
                        i3 = i;
                        i = i4;
                    } else {
                        i3 = i;
                    }
                    while (true) {
                        int a3 = i.this.a(charSequence, i, i2);
                        if (a3 > i3) {
                            appendable.append(charSequence, i3, a3);
                        }
                        if (a3 == i2) {
                            break;
                        }
                        int b = i.b(charSequence, a3, i2);
                        if (b < 0) {
                            this.a = -b;
                            break;
                        }
                        char[] a4 = i.this.a(b);
                        if (a4 != null) {
                            a(a4, a4.length);
                        } else {
                            a(this.b, Character.toChars(b, this.b, 0));
                        }
                        i3 = (Character.isSupplementaryCodePoint(b) ? 2 : 1) + a3;
                        i = i3;
                    }
                }
                return this;
            }
        };
    }

    @Override // cn.sharesdk.framework.utils.Escaper
    public String escape(String str) {
        int length = str.length();
        int a2 = a(str, 0, length);
        return a2 == length ? str : a(str, a2);
    }
}
