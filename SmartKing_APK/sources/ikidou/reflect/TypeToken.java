package ikidou.reflect;

import ikidou.reflect.exception.TypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/* loaded from: classes2.dex */
public abstract class TypeToken<T> {
    private final Type type;

    public TypeToken() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new TypeException("No generics found!");
        }
        this.type = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return this.type;
    }
}
