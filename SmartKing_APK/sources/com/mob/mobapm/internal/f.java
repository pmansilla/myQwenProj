package com.mob.mobapm.internal;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/* loaded from: classes.dex */
public class f extends b {
    private StringBuilder c = new StringBuilder();

    public f a(String str) {
        this.c.append(str);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.mobapm.internal.b
    public InputStream a() throws Throwable {
        return new ByteArrayInputStream(this.c.toString().getBytes("utf-8"));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.mobapm.internal.b
    public long b() throws Throwable {
        return this.c.toString().getBytes("utf-8").length;
    }

    public String toString() {
        return this.c.toString();
    }
}
