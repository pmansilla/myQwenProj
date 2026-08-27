package com.litesuits.orm.db.model;

import com.litesuits.orm.db.assit.Checker;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class ColumnsValue {
    public String[] columns;
    private Map<String, Object> map;

    public ColumnsValue(Map<String, Object> map) {
        this.map = new HashMap();
        if (Checker.isEmpty(map)) {
            return;
        }
        this.columns = new String[map.size()];
        int i = 0;
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            this.columns[i] = it.next().getKey();
            i++;
        }
        this.map = map;
    }

    public ColumnsValue(String[] strArr) {
        this.map = new HashMap();
        this.columns = strArr;
        for (String str : strArr) {
            this.map.put(str, null);
        }
    }

    public ColumnsValue(String[] strArr, Object[] objArr) {
        this.map = new HashMap();
        this.columns = strArr;
        int i = 0;
        if (objArr == null) {
            int length = strArr.length;
            while (i < length) {
                this.map.put(strArr[i], null);
                i++;
            }
            return;
        }
        if (strArr.length != objArr.length) {
            throw new IllegalArgumentException("length of columns and values must be the same");
        }
        int length2 = strArr.length;
        int i2 = 0;
        while (i < length2) {
            this.map.put(strArr[i], objArr[i2]);
            i++;
            i2++;
        }
    }

    public boolean checkColumns() {
        if (this.columns != null) {
            return true;
        }
        throw new IllegalArgumentException("columns must not be null");
    }

    public Object getValue(String str) {
        return this.map.get(str);
    }
}
