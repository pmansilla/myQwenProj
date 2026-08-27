package com.litesuits.orm.db.model;

import com.litesuits.orm.db.utils.DataUtil;
import java.io.Serializable;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class Property implements Serializable {
    private static final long serialVersionUID = 1542861322620643038L;
    public int classType;
    public String column;
    public Field field;

    public Property(String str, Field field) {
        this.classType = 0;
        this.column = str;
        this.field = field;
        if (this.classType <= 0) {
            this.classType = DataUtil.getFieldClassType(field);
        }
    }

    public Property(String str, Field field, int i) {
        this.classType = 0;
        this.column = str;
        this.field = field;
        if (i <= 0) {
            this.classType = DataUtil.getFieldClassType(field);
        }
        this.classType = i;
    }

    public String toString() {
        return "Property{column='" + this.column + "', field=" + this.field + ", classType=" + this.classType + '}';
    }
}
