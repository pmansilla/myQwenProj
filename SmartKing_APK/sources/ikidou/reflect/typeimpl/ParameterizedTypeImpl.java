package ikidou.reflect.typeimpl;

import ikidou.reflect.exception.TypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import kotlin.text.Typography;

/* loaded from: classes2.dex */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Type[] args;
    private final Type owner;
    private final Class raw;

    public ParameterizedTypeImpl(Class cls, Type[] typeArr, Type type) {
        this.raw = cls;
        this.args = typeArr == null ? new Type[0] : typeArr;
        this.owner = type;
        checkArgs();
    }

    private void checkArgs() {
        if (this.raw == null) {
            throw new TypeException("raw class can't be null");
        }
        TypeVariable[] typeParameters = this.raw.getTypeParameters();
        if (this.args.length == 0 || typeParameters.length == this.args.length) {
            return;
        }
        throw new TypeException(this.raw.getName() + " expect " + typeParameters.length + " arg(s), got " + this.args.length);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ParameterizedTypeImpl parameterizedTypeImpl = (ParameterizedTypeImpl) obj;
        if (this.raw.equals(parameterizedTypeImpl.raw) && Arrays.equals(this.args, parameterizedTypeImpl.args)) {
            return this.owner != null ? this.owner.equals(parameterizedTypeImpl.owner) : parameterizedTypeImpl.owner == null;
        }
        return false;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type[] getActualTypeArguments() {
        return this.args;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getOwnerType() {
        return this.owner;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getRawType() {
        return this.raw;
    }

    public int hashCode() {
        return (((this.raw.hashCode() * 31) + Arrays.hashCode(this.args)) * 31) + (this.owner != null ? this.owner.hashCode() : 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.raw.getName());
        if (this.args.length != 0) {
            sb.append(Typography.less);
            for (int i = 0; i < this.args.length; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                Type type = this.args[i];
                if (type instanceof Class) {
                    Class<?> cls = (Class) type;
                    if (cls.isArray()) {
                        Class<?> cls2 = cls;
                        int i2 = 0;
                        do {
                            i2++;
                            cls2 = cls2.getComponentType();
                        } while (cls2.isArray());
                        sb.append(cls2.getName());
                        while (i2 > 0) {
                            sb.append("[]");
                            i2--;
                        }
                    } else {
                        sb.append(cls.getName());
                    }
                } else {
                    sb.append(this.args[i].toString());
                }
            }
            sb.append(Typography.greater);
        }
        return sb.toString();
    }
}
