package org.apache.commons.lang.math;

import com.amap.location.common.model.AmapLoc;
import java.math.BigInteger;

/* loaded from: classes2.dex */
public final class Fraction extends Number implements Comparable {
    private static final long serialVersionUID = 65382027393090L;
    private final int denominator;
    private final int numerator;
    public static final Fraction ZERO = new Fraction(0, 1);
    public static final Fraction ONE = new Fraction(1, 1);
    public static final Fraction ONE_HALF = new Fraction(1, 2);
    public static final Fraction ONE_THIRD = new Fraction(1, 3);
    public static final Fraction TWO_THIRDS = new Fraction(2, 3);
    public static final Fraction ONE_QUARTER = new Fraction(1, 4);
    public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
    public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
    public static final Fraction ONE_FIFTH = new Fraction(1, 5);
    public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
    public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
    public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
    private transient int hashCode = 0;
    private transient String toString = null;
    private transient String toProperString = null;

    private Fraction(int i, int i2) {
        this.numerator = i;
        this.denominator = i2;
    }

    private static int addAndCheck(int i, int i2) {
        long j = i + i2;
        if (j < -2147483648L || j > 2147483647L) {
            throw new ArithmeticException("overflow: add");
        }
        return (int) j;
    }

    private Fraction addSub(Fraction fraction, boolean z) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        }
        if (this.numerator == 0) {
            return z ? fraction : fraction.negate();
        }
        if (fraction.numerator == 0) {
            return this;
        }
        int greatestCommonDivisor = greatestCommonDivisor(this.denominator, fraction.denominator);
        if (greatestCommonDivisor == 1) {
            int mulAndCheck = mulAndCheck(this.numerator, fraction.denominator);
            int mulAndCheck2 = mulAndCheck(fraction.numerator, this.denominator);
            return new Fraction(z ? addAndCheck(mulAndCheck, mulAndCheck2) : subAndCheck(mulAndCheck, mulAndCheck2), mulPosAndCheck(this.denominator, fraction.denominator));
        }
        BigInteger multiply = BigInteger.valueOf(this.numerator).multiply(BigInteger.valueOf(fraction.denominator / greatestCommonDivisor));
        BigInteger multiply2 = BigInteger.valueOf(fraction.numerator).multiply(BigInteger.valueOf(this.denominator / greatestCommonDivisor));
        BigInteger add = z ? multiply.add(multiply2) : multiply.subtract(multiply2);
        int intValue = add.mod(BigInteger.valueOf(greatestCommonDivisor)).intValue();
        int greatestCommonDivisor2 = intValue == 0 ? greatestCommonDivisor : greatestCommonDivisor(intValue, greatestCommonDivisor);
        BigInteger divide = add.divide(BigInteger.valueOf(greatestCommonDivisor2));
        if (divide.bitLength() <= 31) {
            return new Fraction(divide.intValue(), mulPosAndCheck(this.denominator / greatestCommonDivisor, fraction.denominator / greatestCommonDivisor2));
        }
        throw new ArithmeticException("overflow: numerator too large after multiply");
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x008a, code lost:
    
        return getReducedFraction((r12 + (r4 * r14)) * r17, r14);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static org.apache.commons.lang.math.Fraction getFraction(double r24) {
        /*
            r1 = 0
            int r5 = (r24 > r1 ? 1 : (r24 == r1 ? 0 : -1))
            if (r5 >= 0) goto L8
            r1 = -1
            goto L9
        L8:
            r1 = 1
        L9:
            double r2 = java.lang.Math.abs(r24)
            r4 = 4746794007244308480(0x41dfffffffc00000, double:2.147483647E9)
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 > 0) goto L93
            boolean r4 = java.lang.Double.isNaN(r2)
            if (r4 != 0) goto L93
            int r4 = (int) r2
            double r5 = (double) r4
            java.lang.Double.isNaN(r5)
            double r2 = r2 - r5
            int r5 = (int) r2
            r6 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r8 = (double) r5
            java.lang.Double.isNaN(r8)
            double r8 = r2 - r8
            r10 = 9218868437227405311(0x7fefffffffffffff, double:1.7976931348623157E308)
            r12 = 0
            r17 = r1
            r12 = 1
            r13 = 0
            r14 = 0
            r15 = 1
            r16 = 1
        L39:
            double r0 = r6 / r8
            int r0 = (int) r0
            r18 = r10
            double r10 = (double) r0
            java.lang.Double.isNaN(r10)
            double r10 = r10 * r8
            double r6 = r6 - r10
            int r1 = r5 * r12
            int r1 = r1 + r13
            int r5 = r5 * r14
            int r5 = r5 + r15
            double r10 = (double) r1
            r20 = r0
            r21 = r1
            double r0 = (double) r5
            java.lang.Double.isNaN(r10)
            java.lang.Double.isNaN(r0)
            double r10 = r10 / r0
            double r0 = r2 - r10
            double r10 = java.lang.Math.abs(r0)
            r0 = 1
            int r1 = r16 + 1
            r13 = 25
            int r15 = (r18 > r10 ? 1 : (r18 == r10 ? 0 : -1))
            if (r15 <= 0) goto L7f
            r15 = 10000(0x2710, float:1.4013E-41)
            if (r5 > r15) goto L7f
            if (r5 <= 0) goto L7f
            if (r1 < r13) goto L70
            goto L7f
        L70:
            r16 = r1
            r13 = r12
            r15 = r14
            r12 = r21
            r14 = r5
            r5 = r20
            r22 = r6
            r6 = r8
            r8 = r22
            goto L39
        L7f:
            if (r1 == r13) goto L8b
            int r4 = r4 * r14
            int r12 = r12 + r4
            int r12 = r12 * r17
            org.apache.commons.lang.math.Fraction r0 = getReducedFraction(r12, r14)
            return r0
        L8b:
            java.lang.ArithmeticException r0 = new java.lang.ArithmeticException
            java.lang.String r1 = "Unable to convert double to fraction"
            r0.<init>(r1)
            throw r0
        L93:
            java.lang.ArithmeticException r0 = new java.lang.ArithmeticException
            java.lang.String r1 = "The value must not be greater than Integer.MAX_VALUE or NaN"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.math.Fraction.getFraction(double):org.apache.commons.lang.math.Fraction");
    }

    public static Fraction getFraction(int i, int i2) {
        if (i2 == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (i2 < 0) {
            if (i == Integer.MIN_VALUE || i2 == Integer.MIN_VALUE) {
                throw new ArithmeticException("overflow: can't negate");
            }
            i = -i;
            i2 = -i2;
        }
        return new Fraction(i, i2);
    }

    public static Fraction getFraction(int i, int i2, int i3) {
        if (i3 == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (i3 < 0) {
            throw new ArithmeticException("The denominator must not be negative");
        }
        if (i2 < 0) {
            throw new ArithmeticException("The numerator must not be negative");
        }
        long j = i < 0 ? (i * i3) - i2 : (i * i3) + i2;
        if (j < -2147483648L || j > 2147483647L) {
            throw new ArithmeticException("Numerator too large to represent as an Integer.");
        }
        return new Fraction((int) j, i3);
    }

    public static Fraction getFraction(String str) {
        if (str == null) {
            throw new IllegalArgumentException("The string must not be null");
        }
        if (str.indexOf(46) >= 0) {
            return getFraction(Double.parseDouble(str));
        }
        int indexOf = str.indexOf(32);
        if (indexOf <= 0) {
            int indexOf2 = str.indexOf(47);
            return indexOf2 < 0 ? getFraction(Integer.parseInt(str), 1) : getFraction(Integer.parseInt(str.substring(0, indexOf2)), Integer.parseInt(str.substring(indexOf2 + 1)));
        }
        int parseInt = Integer.parseInt(str.substring(0, indexOf));
        String substring = str.substring(indexOf + 1);
        int indexOf3 = substring.indexOf(47);
        if (indexOf3 >= 0) {
            return getFraction(parseInt, Integer.parseInt(substring.substring(0, indexOf3)), Integer.parseInt(substring.substring(indexOf3 + 1)));
        }
        throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
    }

    public static Fraction getReducedFraction(int i, int i2) {
        if (i2 == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (i == 0) {
            return ZERO;
        }
        if (i2 == Integer.MIN_VALUE && (i & 1) == 0) {
            i /= 2;
            i2 /= 2;
        }
        if (i2 < 0) {
            if (i == Integer.MIN_VALUE || i2 == Integer.MIN_VALUE) {
                throw new ArithmeticException("overflow: can't negate");
            }
            i = -i;
            i2 = -i2;
        }
        int greatestCommonDivisor = greatestCommonDivisor(i, i2);
        return new Fraction(i / greatestCommonDivisor, i2 / greatestCommonDivisor);
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x002a, code lost:
    
        if (r2 != 1) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x002c, code lost:
    
        r2 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0033, code lost:
    
        if ((r2 & 1) != 0) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0038, code lost:
    
        if (r2 <= 0) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x003a, code lost:
    
        r5 = -r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x003d, code lost:
    
        r2 = (r6 - r5) / 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0041, code lost:
    
        if (r2 != 0) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0048, code lost:
    
        return (-r5) * (1 << r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x003c, code lost:
    
        r6 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0035, code lost:
    
        r2 = r2 / 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x002e, code lost:
    
        r2 = -(r5 / 2);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int greatestCommonDivisor(int r5, int r6) {
        /*
            int r0 = java.lang.Math.abs(r5)
            r1 = 1
            if (r0 <= r1) goto L51
            int r0 = java.lang.Math.abs(r6)
            if (r0 > r1) goto Le
            goto L51
        Le:
            if (r5 <= 0) goto L11
            int r5 = -r5
        L11:
            if (r6 <= 0) goto L14
            int r6 = -r6
        L14:
            r0 = 0
        L15:
            r2 = r5 & 1
            r3 = 31
            if (r2 != 0) goto L28
            r4 = r6 & 1
            if (r4 != 0) goto L28
            if (r0 >= r3) goto L28
            int r5 = r5 / 2
            int r6 = r6 / 2
            int r0 = r0 + 1
            goto L15
        L28:
            if (r0 == r3) goto L49
            if (r2 != r1) goto L2e
            r2 = r6
            goto L31
        L2e:
            int r2 = r5 / 2
            int r2 = -r2
        L31:
            r3 = r2 & 1
            if (r3 != 0) goto L38
            int r2 = r2 / 2
            goto L31
        L38:
            if (r2 <= 0) goto L3c
            int r5 = -r2
            goto L3d
        L3c:
            r6 = r2
        L3d:
            int r2 = r6 - r5
            int r2 = r2 / 2
            if (r2 != 0) goto L31
            int r5 = -r5
            int r6 = r1 << r0
            int r5 = r5 * r6
            return r5
        L49:
            java.lang.ArithmeticException r5 = new java.lang.ArithmeticException
            java.lang.String r6 = "overflow: gcd is 2^31"
            r5.<init>(r6)
            throw r5
        L51:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang.math.Fraction.greatestCommonDivisor(int, int):int");
    }

    private static int mulAndCheck(int i, int i2) {
        long j = i * i2;
        if (j < -2147483648L || j > 2147483647L) {
            throw new ArithmeticException("overflow: mul");
        }
        return (int) j;
    }

    private static int mulPosAndCheck(int i, int i2) {
        long j = i * i2;
        if (j <= 2147483647L) {
            return (int) j;
        }
        throw new ArithmeticException("overflow: mulPos");
    }

    private static int subAndCheck(int i, int i2) {
        long j = i - i2;
        if (j < -2147483648L || j > 2147483647L) {
            throw new ArithmeticException("overflow: add");
        }
        return (int) j;
    }

    public Fraction abs() {
        return this.numerator >= 0 ? this : negate();
    }

    public Fraction add(Fraction fraction) {
        return addSub(fraction, true);
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        Fraction fraction = (Fraction) obj;
        if (this == fraction) {
            return 0;
        }
        if (this.numerator == fraction.numerator && this.denominator == fraction.denominator) {
            return 0;
        }
        long j = this.numerator * fraction.denominator;
        long j2 = fraction.numerator * this.denominator;
        if (j == j2) {
            return 0;
        }
        return j < j2 ? -1 : 1;
    }

    public Fraction divideBy(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        }
        if (fraction.numerator != 0) {
            return multiplyBy(fraction.invert());
        }
        throw new ArithmeticException("The fraction to divide by must not be zero");
    }

    @Override // java.lang.Number
    public double doubleValue() {
        double d = this.numerator;
        double d2 = this.denominator;
        Double.isNaN(d);
        Double.isNaN(d2);
        return d / d2;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Fraction)) {
            return false;
        }
        Fraction fraction = (Fraction) obj;
        return getNumerator() == fraction.getNumerator() && getDenominator() == fraction.getDenominator();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.numerator / this.denominator;
    }

    public int getDenominator() {
        return this.denominator;
    }

    public int getNumerator() {
        return this.numerator;
    }

    public int getProperNumerator() {
        return Math.abs(this.numerator % this.denominator);
    }

    public int getProperWhole() {
        return this.numerator / this.denominator;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = ((getNumerator() + 629) * 37) + getDenominator();
        }
        return this.hashCode;
    }

    @Override // java.lang.Number
    public int intValue() {
        return this.numerator / this.denominator;
    }

    public Fraction invert() {
        if (this.numerator == 0) {
            throw new ArithmeticException("Unable to invert zero.");
        }
        if (this.numerator != Integer.MIN_VALUE) {
            return this.numerator < 0 ? new Fraction(-this.denominator, -this.numerator) : new Fraction(this.denominator, this.numerator);
        }
        throw new ArithmeticException("overflow: can't negate numerator");
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.numerator / this.denominator;
    }

    public Fraction multiplyBy(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        }
        if (this.numerator == 0 || fraction.numerator == 0) {
            return ZERO;
        }
        int greatestCommonDivisor = greatestCommonDivisor(this.numerator, fraction.denominator);
        int greatestCommonDivisor2 = greatestCommonDivisor(fraction.numerator, this.denominator);
        return getReducedFraction(mulAndCheck(this.numerator / greatestCommonDivisor, fraction.numerator / greatestCommonDivisor2), mulPosAndCheck(this.denominator / greatestCommonDivisor2, fraction.denominator / greatestCommonDivisor));
    }

    public Fraction negate() {
        if (this.numerator != Integer.MIN_VALUE) {
            return new Fraction(-this.numerator, this.denominator);
        }
        throw new ArithmeticException("overflow: too large to negate");
    }

    public Fraction pow(int i) {
        if (i == 1) {
            return this;
        }
        if (i == 0) {
            return ONE;
        }
        if (i < 0) {
            return i == Integer.MIN_VALUE ? invert().pow(2).pow(-(i / 2)) : invert().pow(-i);
        }
        Fraction multiplyBy = multiplyBy(this);
        return i % 2 == 0 ? multiplyBy.pow(i / 2) : multiplyBy.pow(i / 2).multiplyBy(this);
    }

    public Fraction reduce() {
        if (this.numerator == 0) {
            return equals(ZERO) ? this : ZERO;
        }
        int greatestCommonDivisor = greatestCommonDivisor(Math.abs(this.numerator), this.denominator);
        return greatestCommonDivisor == 1 ? this : getFraction(this.numerator / greatestCommonDivisor, this.denominator / greatestCommonDivisor);
    }

    public Fraction subtract(Fraction fraction) {
        return addSub(fraction, false);
    }

    public String toProperString() {
        if (this.toProperString == null) {
            if (this.numerator == 0) {
                this.toProperString = AmapLoc.RESULT_TYPE_GPS;
            } else if (this.numerator == this.denominator) {
                this.toProperString = AmapLoc.RESULT_TYPE_WIFI_ONLY;
            } else if (this.numerator == this.denominator * (-1)) {
                this.toProperString = "-1";
            } else {
                if ((this.numerator > 0 ? -this.numerator : this.numerator) < (-this.denominator)) {
                    int properNumerator = getProperNumerator();
                    if (properNumerator == 0) {
                        this.toProperString = Integer.toString(getProperWhole());
                    } else {
                        StringBuffer stringBuffer = new StringBuffer(32);
                        stringBuffer.append(getProperWhole());
                        stringBuffer.append(' ');
                        stringBuffer.append(properNumerator);
                        stringBuffer.append('/');
                        stringBuffer.append(getDenominator());
                        this.toProperString = stringBuffer.toString();
                    }
                } else {
                    StringBuffer stringBuffer2 = new StringBuffer(32);
                    stringBuffer2.append(getNumerator());
                    stringBuffer2.append('/');
                    stringBuffer2.append(getDenominator());
                    this.toProperString = stringBuffer2.toString();
                }
            }
        }
        return this.toProperString;
    }

    public String toString() {
        if (this.toString == null) {
            StringBuffer stringBuffer = new StringBuffer(32);
            stringBuffer.append(getNumerator());
            stringBuffer.append('/');
            stringBuffer.append(getDenominator());
            this.toString = stringBuffer.toString();
        }
        return this.toString;
    }
}
