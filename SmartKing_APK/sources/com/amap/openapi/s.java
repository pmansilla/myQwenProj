package com.amap.openapi;

import android.support.annotation.NonNull;
import com.amap.openapi.bi;

/* compiled from: CollectDataItem.java */
/* loaded from: classes.dex */
public class s implements bi.a {
    private int a;
    private byte[] b;

    public s(int i, @NonNull byte[] bArr) {
        this.a = i;
        this.b = bArr;
    }

    @Override // com.amap.openapi.bi.a
    public long a() {
        return this.b.length + 17;
    }

    public int b() {
        return this.a;
    }

    public byte[] c() {
        return this.b;
    }
}
