package com.mob.mobapm.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/* loaded from: classes.dex */
public class a extends b {
    private File c;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.mobapm.internal.b
    public InputStream a() throws Throwable {
        return new FileInputStream(this.c);
    }

    public void a(String str) {
        this.c = new File(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mob.mobapm.internal.b
    public long b() throws Throwable {
        return this.c.length();
    }

    public String toString() {
        return this.c.toString();
    }
}
