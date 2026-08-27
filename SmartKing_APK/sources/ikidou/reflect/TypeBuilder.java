package ikidou.reflect;

import ikidou.reflect.exception.TypeException;
import ikidou.reflect.typeimpl.ParameterizedTypeImpl;
import ikidou.reflect.typeimpl.WildcardTypeImpl;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TypeBuilder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final List<Type> args = new ArrayList();
    private final TypeBuilder parent;
    private final Class raw;

    private TypeBuilder(Class cls, TypeBuilder typeBuilder) {
        this.raw = cls;
        this.parent = typeBuilder;
    }

    private Type getType() {
        return this.args.isEmpty() ? this.raw : new ParameterizedTypeImpl(this.raw, (Type[]) this.args.toArray(new Type[this.args.size()]), null);
    }

    public static TypeBuilder newInstance(Class cls) {
        return new TypeBuilder(cls, null);
    }

    private static TypeBuilder newInstance(Class cls, TypeBuilder typeBuilder) {
        return new TypeBuilder(cls, typeBuilder);
    }

    public TypeBuilder addTypeParam(Class cls) {
        return addTypeParam((Type) cls);
    }

    public TypeBuilder addTypeParam(Type type) {
        if (type == null) {
            throw new NullPointerException("addTypeParam expect not null Type");
        }
        this.args.add(type);
        return this;
    }

    public TypeBuilder addTypeParamExtends(Class... clsArr) {
        if (clsArr != null) {
            return addTypeParam(new WildcardTypeImpl(null, clsArr));
        }
        throw new NullPointerException("addTypeParamExtends() expect not null Class");
    }

    public TypeBuilder addTypeParamSuper(Class... clsArr) {
        if (clsArr != null) {
            return addTypeParam(new WildcardTypeImpl(clsArr, null));
        }
        throw new NullPointerException("addTypeParamSuper() expect not null Class");
    }

    public TypeBuilder beginSubType(Class cls) {
        return newInstance(cls, this);
    }

    public Type build() {
        if (this.parent == null) {
            return getType();
        }
        throw new TypeException("expect endSubType() before build()");
    }

    public TypeBuilder endSubType() {
        if (this.parent == null) {
            throw new TypeException("expect beginSubType() before endSubType()");
        }
        this.parent.addTypeParam(getType());
        return this.parent;
    }
}
