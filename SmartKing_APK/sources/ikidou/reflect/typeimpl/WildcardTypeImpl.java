package ikidou.reflect.typeimpl;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class WildcardTypeImpl implements WildcardType {
    private final Class[] lower;
    private final Class[] upper;

    public WildcardTypeImpl(Class[] clsArr, Class[] clsArr2) {
        this.lower = clsArr == null ? new Class[0] : clsArr;
        this.upper = clsArr2 == null ? new Class[0] : clsArr2;
        checkArgs();
    }

    private void checkArgs() {
        if (this.lower.length == 0 && this.upper.length == 0) {
            throw new IllegalArgumentException("lower or upper can't be null");
        }
        checkArgs(this.lower);
        checkArgs(this.upper);
    }

    private void checkArgs(Class[] clsArr) {
        for (int i = 1; i < clsArr.length; i++) {
            Class cls = clsArr[i];
            if (!cls.isInterface()) {
                throw new IllegalArgumentException(cls.getName() + " not a interface!");
            }
        }
    }

    private String getTypeString(String str, Class[] clsArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = 0; i < clsArr.length; i++) {
            if (i != 0) {
                sb.append(" & ");
            }
            sb.append(clsArr[i].getName());
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        WildcardTypeImpl wildcardTypeImpl = (WildcardTypeImpl) obj;
        return Arrays.equals(this.upper, wildcardTypeImpl.upper) && Arrays.equals(this.lower, wildcardTypeImpl.lower);
    }

    @Override // java.lang.reflect.WildcardType
    public Type[] getLowerBounds() {
        return this.lower;
    }

    @Override // java.lang.reflect.WildcardType
    public Type[] getUpperBounds() {
        return this.upper;
    }

    public int hashCode() {
        return (Arrays.hashCode(this.upper) * 31) + Arrays.hashCode(this.lower);
    }

    public String toString() {
        return this.upper.length > 0 ? this.upper[0] == Object.class ? "?" : getTypeString("? extends ", this.upper) : getTypeString("? super ", this.lower);
    }
}
