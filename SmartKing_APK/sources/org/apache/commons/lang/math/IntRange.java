package org.apache.commons.lang.math;

import java.io.Serializable;

/* loaded from: classes2.dex */
public final class IntRange extends Range implements Serializable {
    private static final long serialVersionUID = 71849363892730L;
    private transient int hashCode;
    private final int max;
    private transient Integer maxObject;
    private final int min;
    private transient Integer minObject;
    private transient String toString;

    public IntRange(int i) {
        this.minObject = null;
        this.maxObject = null;
        this.hashCode = 0;
        this.toString = null;
        this.min = i;
        this.max = i;
    }

    public IntRange(int i, int i2) {
        this.minObject = null;
        this.maxObject = null;
        this.hashCode = 0;
        this.toString = null;
        if (i2 < i) {
            this.min = i2;
            this.max = i;
        } else {
            this.min = i;
            this.max = i2;
        }
    }

    public IntRange(Number number) {
        this.minObject = null;
        this.maxObject = null;
        this.hashCode = 0;
        this.toString = null;
        if (number == null) {
            throw new IllegalArgumentException("The number must not be null");
        }
        this.min = number.intValue();
        this.max = number.intValue();
        if (number instanceof Integer) {
            Integer num = (Integer) number;
            this.minObject = num;
            this.maxObject = num;
        }
    }

    public IntRange(Number number, Number number2) {
        this.minObject = null;
        this.maxObject = null;
        this.hashCode = 0;
        this.toString = null;
        if (number == null || number2 == null) {
            throw new IllegalArgumentException("The numbers must not be null");
        }
        int intValue = number.intValue();
        int intValue2 = number2.intValue();
        if (intValue2 < intValue) {
            this.min = intValue2;
            this.max = intValue;
            if (number2 instanceof Integer) {
                this.minObject = (Integer) number2;
            }
            if (number instanceof Integer) {
                this.maxObject = (Integer) number;
                return;
            }
            return;
        }
        this.min = intValue;
        this.max = intValue2;
        if (number instanceof Integer) {
            this.minObject = (Integer) number;
        }
        if (number2 instanceof Integer) {
            this.maxObject = (Integer) number2;
        }
    }

    @Override // org.apache.commons.lang.math.Range
    public boolean containsInteger(int i) {
        return i >= this.min && i <= this.max;
    }

    @Override // org.apache.commons.lang.math.Range
    public boolean containsNumber(Number number) {
        if (number == null) {
            return false;
        }
        return containsInteger(number.intValue());
    }

    @Override // org.apache.commons.lang.math.Range
    public boolean containsRange(Range range) {
        return range != null && containsInteger(range.getMinimumInteger()) && containsInteger(range.getMaximumInteger());
    }

    @Override // org.apache.commons.lang.math.Range
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IntRange)) {
            return false;
        }
        IntRange intRange = (IntRange) obj;
        return this.min == intRange.min && this.max == intRange.max;
    }

    @Override // org.apache.commons.lang.math.Range
    public double getMaximumDouble() {
        return this.max;
    }

    @Override // org.apache.commons.lang.math.Range
    public float getMaximumFloat() {
        return this.max;
    }

    @Override // org.apache.commons.lang.math.Range
    public int getMaximumInteger() {
        return this.max;
    }

    @Override // org.apache.commons.lang.math.Range
    public long getMaximumLong() {
        return this.max;
    }

    @Override // org.apache.commons.lang.math.Range
    public Number getMaximumNumber() {
        if (this.maxObject == null) {
            this.maxObject = new Integer(this.max);
        }
        return this.maxObject;
    }

    @Override // org.apache.commons.lang.math.Range
    public double getMinimumDouble() {
        return this.min;
    }

    @Override // org.apache.commons.lang.math.Range
    public float getMinimumFloat() {
        return this.min;
    }

    @Override // org.apache.commons.lang.math.Range
    public int getMinimumInteger() {
        return this.min;
    }

    @Override // org.apache.commons.lang.math.Range
    public long getMinimumLong() {
        return this.min;
    }

    @Override // org.apache.commons.lang.math.Range
    public Number getMinimumNumber() {
        if (this.minObject == null) {
            this.minObject = new Integer(this.min);
        }
        return this.minObject;
    }

    @Override // org.apache.commons.lang.math.Range
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = 17;
            this.hashCode = (this.hashCode * 37) + getClass().hashCode();
            this.hashCode = (this.hashCode * 37) + this.min;
            this.hashCode = (this.hashCode * 37) + this.max;
        }
        return this.hashCode;
    }

    @Override // org.apache.commons.lang.math.Range
    public boolean overlapsRange(Range range) {
        if (range == null) {
            return false;
        }
        return range.containsInteger(this.min) || range.containsInteger(this.max) || containsInteger(range.getMinimumInteger());
    }

    public int[] toArray() {
        int[] iArr = new int[(this.max - this.min) + 1];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = this.min + i;
        }
        return iArr;
    }

    @Override // org.apache.commons.lang.math.Range
    public String toString() {
        if (this.toString == null) {
            StringBuffer stringBuffer = new StringBuffer(32);
            stringBuffer.append("Range[");
            stringBuffer.append(this.min);
            stringBuffer.append(',');
            stringBuffer.append(this.max);
            stringBuffer.append(']');
            this.toString = stringBuffer.toString();
        }
        return this.toString;
    }
}
