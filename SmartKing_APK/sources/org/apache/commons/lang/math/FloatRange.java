package org.apache.commons.lang.math;

import java.io.Serializable;

/* loaded from: classes2.dex */
public final class FloatRange extends Range implements Serializable {
    private static final long serialVersionUID = 71849363892750L;
    private transient int hashCode;
    private final float max;
    private transient Float maxObject;
    private final float min;
    private transient Float minObject;
    private transient String toString;

    public FloatRange(float f) {
        this.minObject = null;
        this.maxObject = null;
        this.hashCode = 0;
        this.toString = null;
        if (Float.isNaN(f)) {
            throw new IllegalArgumentException("The number must not be NaN");
        }
        this.min = f;
        this.max = f;
    }

    public FloatRange(float f, float f2) {
        this.minObject = null;
        this.maxObject = null;
        this.hashCode = 0;
        this.toString = null;
        if (Float.isNaN(f) || Float.isNaN(f2)) {
            throw new IllegalArgumentException("The numbers must not be NaN");
        }
        if (f2 < f) {
            this.min = f2;
            this.max = f;
        } else {
            this.min = f;
            this.max = f2;
        }
    }

    public FloatRange(Number number) {
        this.minObject = null;
        this.maxObject = null;
        this.hashCode = 0;
        this.toString = null;
        if (number == null) {
            throw new IllegalArgumentException("The number must not be null");
        }
        this.min = number.floatValue();
        this.max = number.floatValue();
        if (Float.isNaN(this.min) || Float.isNaN(this.max)) {
            throw new IllegalArgumentException("The number must not be NaN");
        }
        if (number instanceof Float) {
            Float f = (Float) number;
            this.minObject = f;
            this.maxObject = f;
        }
    }

    public FloatRange(Number number, Number number2) {
        this.minObject = null;
        this.maxObject = null;
        this.hashCode = 0;
        this.toString = null;
        if (number == null || number2 == null) {
            throw new IllegalArgumentException("The numbers must not be null");
        }
        float floatValue = number.floatValue();
        float floatValue2 = number2.floatValue();
        if (Float.isNaN(floatValue) || Float.isNaN(floatValue2)) {
            throw new IllegalArgumentException("The numbers must not be NaN");
        }
        if (floatValue2 < floatValue) {
            this.min = floatValue2;
            this.max = floatValue;
            if (number2 instanceof Float) {
                this.minObject = (Float) number2;
            }
            if (number instanceof Float) {
                this.maxObject = (Float) number;
                return;
            }
            return;
        }
        this.min = floatValue;
        this.max = floatValue2;
        if (number instanceof Float) {
            this.minObject = (Float) number;
        }
        if (number2 instanceof Float) {
            this.maxObject = (Float) number2;
        }
    }

    @Override // org.apache.commons.lang.math.Range
    public boolean containsFloat(float f) {
        return f >= this.min && f <= this.max;
    }

    @Override // org.apache.commons.lang.math.Range
    public boolean containsNumber(Number number) {
        if (number == null) {
            return false;
        }
        return containsFloat(number.floatValue());
    }

    @Override // org.apache.commons.lang.math.Range
    public boolean containsRange(Range range) {
        return range != null && containsFloat(range.getMinimumFloat()) && containsFloat(range.getMaximumFloat());
    }

    @Override // org.apache.commons.lang.math.Range
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FloatRange)) {
            return false;
        }
        FloatRange floatRange = (FloatRange) obj;
        return Float.floatToIntBits(this.min) == Float.floatToIntBits(floatRange.min) && Float.floatToIntBits(this.max) == Float.floatToIntBits(floatRange.max);
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
        return (int) this.max;
    }

    @Override // org.apache.commons.lang.math.Range
    public long getMaximumLong() {
        return this.max;
    }

    @Override // org.apache.commons.lang.math.Range
    public Number getMaximumNumber() {
        if (this.maxObject == null) {
            this.maxObject = new Float(this.max);
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
        return (int) this.min;
    }

    @Override // org.apache.commons.lang.math.Range
    public long getMinimumLong() {
        return this.min;
    }

    @Override // org.apache.commons.lang.math.Range
    public Number getMinimumNumber() {
        if (this.minObject == null) {
            this.minObject = new Float(this.min);
        }
        return this.minObject;
    }

    @Override // org.apache.commons.lang.math.Range
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = 17;
            this.hashCode = (this.hashCode * 37) + getClass().hashCode();
            this.hashCode = (this.hashCode * 37) + Float.floatToIntBits(this.min);
            this.hashCode = (this.hashCode * 37) + Float.floatToIntBits(this.max);
        }
        return this.hashCode;
    }

    @Override // org.apache.commons.lang.math.Range
    public boolean overlapsRange(Range range) {
        if (range == null) {
            return false;
        }
        return range.containsFloat(this.min) || range.containsFloat(this.max) || containsFloat(range.getMinimumFloat());
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
