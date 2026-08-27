package com.czw.serializer;

/* compiled from: PHPSerializer.java */
/* loaded from: classes.dex */
class UnSerializeResult {
    public int hv;
    public Object value;

    public UnSerializeResult() {
    }

    public UnSerializeResult(Object obj, int i) {
        this.value = obj;
        this.hv = i;
    }
}
