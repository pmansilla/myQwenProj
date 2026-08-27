package org.apache.commons.lang.mutable;

/* loaded from: classes2.dex */
public class MutableShort extends Number implements Comparable, Mutable {
    private static final long serialVersionUID = -2135791679;
    private short value;

    public MutableShort() {
    }

    public MutableShort(Number number) {
        this.value = number.shortValue();
    }

    public MutableShort(short s) {
        this.value = s;
    }

    public void add(Number number) {
        this.value = (short) (this.value + number.shortValue());
    }

    public void add(short s) {
        this.value = (short) (this.value + s);
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        short s = ((MutableShort) obj).value;
        if (this.value < s) {
            return -1;
        }
        return this.value == s ? 0 : 1;
    }

    public void decrement() {
        this.value = (short) (this.value - 1);
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        return (obj instanceof MutableShort) && this.value == ((MutableShort) obj).shortValue();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.value;
    }

    @Override // org.apache.commons.lang.mutable.Mutable
    public Object getValue() {
        return new Short(this.value);
    }

    public int hashCode() {
        return this.value;
    }

    public void increment() {
        this.value = (short) (this.value + 1);
    }

    @Override // java.lang.Number
    public int intValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.value;
    }

    @Override // org.apache.commons.lang.mutable.Mutable
    public void setValue(Object obj) {
        setValue(((Number) obj).shortValue());
    }

    public void setValue(short s) {
        this.value = s;
    }

    @Override // java.lang.Number
    public short shortValue() {
        return this.value;
    }

    public void subtract(Number number) {
        this.value = (short) (this.value - number.shortValue());
    }

    public void subtract(short s) {
        this.value = (short) (this.value - s);
    }

    public Short toShort() {
        return new Short(shortValue());
    }

    public String toString() {
        return String.valueOf((int) this.value);
    }
}
