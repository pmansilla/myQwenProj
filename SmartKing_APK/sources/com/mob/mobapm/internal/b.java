package com.mob.mobapm.internal;

import com.mob.tools.network.ByteCounterInputStream;
import com.mob.tools.network.OnReadListener;
import java.io.InputStream;

/* loaded from: classes.dex */
public abstract class b {
    private long a;
    private OnReadListener b;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract InputStream a() throws Throwable;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract long b() throws Throwable;

    public InputStream c() throws Throwable {
        ByteCounterInputStream byteCounterInputStream = new ByteCounterInputStream(a());
        byteCounterInputStream.setOnInputStreamReadListener(this.b);
        long j = this.a;
        if (j > 0) {
            byteCounterInputStream.skip(j);
        }
        return byteCounterInputStream;
    }
}
