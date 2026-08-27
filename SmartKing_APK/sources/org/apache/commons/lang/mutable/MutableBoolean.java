package org.apache.commons.lang.mutable;

import java.io.Serializable;
import org.apache.commons.lang.BooleanUtils;

/* loaded from: classes2.dex */
public class MutableBoolean implements Mutable, Serializable, Comparable {
    private static final long serialVersionUID = -4830728138360036487L;
    private boolean value;

    public MutableBoolean() {
    }

    public MutableBoolean(Boolean bool) {
        this.value = bool.booleanValue();
    }

    public MutableBoolean(boolean z) {
        this.value = z;
    }

    public boolean booleanValue() {
        return this.value;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        if (this.value == ((MutableBoolean) obj).value) {
            return 0;
        }
        return this.value ? 1 : -1;
    }

    public boolean equals(Object obj) {
        return (obj instanceof MutableBoolean) && this.value == ((MutableBoolean) obj).booleanValue();
    }

    @Override // org.apache.commons.lang.mutable.Mutable
    public Object getValue() {
        return BooleanUtils.toBooleanObject(this.value);
    }

    public int hashCode() {
        return (this.value ? Boolean.TRUE : Boolean.FALSE).hashCode();
    }

    @Override // org.apache.commons.lang.mutable.Mutable
    public void setValue(Object obj) {
        setValue(((Boolean) obj).booleanValue());
    }

    public void setValue(boolean z) {
        this.value = z;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
