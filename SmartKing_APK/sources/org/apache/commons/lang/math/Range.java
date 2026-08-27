package org.apache.commons.lang.math;

/* loaded from: classes2.dex */
public abstract class Range {
    public boolean containsDouble(double d) {
        return NumberUtils.compare(getMinimumDouble(), d) <= 0 && NumberUtils.compare(getMaximumDouble(), d) >= 0;
    }

    public boolean containsDouble(Number number) {
        if (number == null) {
            return false;
        }
        return containsDouble(number.doubleValue());
    }

    public boolean containsFloat(float f) {
        return NumberUtils.compare(getMinimumFloat(), f) <= 0 && NumberUtils.compare(getMaximumFloat(), f) >= 0;
    }

    public boolean containsFloat(Number number) {
        if (number == null) {
            return false;
        }
        return containsFloat(number.floatValue());
    }

    public boolean containsInteger(int i) {
        return i >= getMinimumInteger() && i <= getMaximumInteger();
    }

    public boolean containsInteger(Number number) {
        if (number == null) {
            return false;
        }
        return containsInteger(number.intValue());
    }

    public boolean containsLong(long j) {
        return j >= getMinimumLong() && j <= getMaximumLong();
    }

    public boolean containsLong(Number number) {
        if (number == null) {
            return false;
        }
        return containsLong(number.longValue());
    }

    public abstract boolean containsNumber(Number number);

    public boolean containsRange(Range range) {
        return range != null && containsNumber(range.getMinimumNumber()) && containsNumber(range.getMaximumNumber());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Range range = (Range) obj;
        return getMinimumNumber().equals(range.getMinimumNumber()) && getMaximumNumber().equals(range.getMaximumNumber());
    }

    public double getMaximumDouble() {
        return getMaximumNumber().doubleValue();
    }

    public float getMaximumFloat() {
        return getMaximumNumber().floatValue();
    }

    public int getMaximumInteger() {
        return getMaximumNumber().intValue();
    }

    public long getMaximumLong() {
        return getMaximumNumber().longValue();
    }

    public abstract Number getMaximumNumber();

    public double getMinimumDouble() {
        return getMinimumNumber().doubleValue();
    }

    public float getMinimumFloat() {
        return getMinimumNumber().floatValue();
    }

    public int getMinimumInteger() {
        return getMinimumNumber().intValue();
    }

    public long getMinimumLong() {
        return getMinimumNumber().longValue();
    }

    public abstract Number getMinimumNumber();

    public int hashCode() {
        return ((((629 + getClass().hashCode()) * 37) + getMinimumNumber().hashCode()) * 37) + getMaximumNumber().hashCode();
    }

    public boolean overlapsRange(Range range) {
        if (range == null) {
            return false;
        }
        return range.containsNumber(getMinimumNumber()) || range.containsNumber(getMaximumNumber()) || containsNumber(range.getMinimumNumber());
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(32);
        stringBuffer.append("Range[");
        stringBuffer.append(getMinimumNumber());
        stringBuffer.append(',');
        stringBuffer.append(getMaximumNumber());
        stringBuffer.append(']');
        return stringBuffer.toString();
    }
}
