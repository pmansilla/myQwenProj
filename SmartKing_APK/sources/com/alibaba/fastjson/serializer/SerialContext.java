package com.alibaba.fastjson.serializer;

import org.apache.commons.lang.ClassUtils;

/* loaded from: classes.dex */
public class SerialContext {
    public final int features;
    public final Object fieldName;
    public final Object object;
    public final SerialContext parent;

    public SerialContext(SerialContext serialContext, Object obj, Object obj2, int i, int i2) {
        this.parent = serialContext;
        this.object = obj;
        this.fieldName = obj2;
        this.features = i;
    }

    public Object getFieldName() {
        return this.fieldName;
    }

    public Object getObject() {
        return this.object;
    }

    public SerialContext getParent() {
        return this.parent;
    }

    public String getPath() {
        return toString();
    }

    public String toString() {
        if (this.parent == null) {
            return "$";
        }
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }

    protected void toString(StringBuilder sb) {
        if (this.parent == null) {
            sb.append('$');
            return;
        }
        this.parent.toString(sb);
        if (this.fieldName == null) {
            sb.append(".null");
            return;
        }
        if (this.fieldName instanceof Integer) {
            sb.append('[');
            sb.append(((Integer) this.fieldName).intValue());
            sb.append(']');
            return;
        }
        sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        String obj = this.fieldName.toString();
        boolean z = false;
        for (int i = 0; i < obj.length(); i++) {
            char charAt = obj.charAt(i);
            if (charAt == '.' || charAt == '@' || charAt == '(' || charAt == '\\') {
                z = true;
            }
        }
        if (!z) {
            sb.append(obj);
            return;
        }
        for (int i2 = 0; i2 < obj.length(); i2++) {
            char charAt2 = obj.charAt(i2);
            if (charAt2 == '.' || charAt2 == '@' || charAt2 == '(') {
                sb.append('\\');
                sb.append('\\');
            } else if (charAt2 == '\\') {
                sb.append('\\');
                sb.append('\\');
                sb.append('\\');
            }
            sb.append(charAt2);
        }
    }
}
