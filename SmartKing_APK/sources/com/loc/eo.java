package com.loc;

import android.content.Context;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/* compiled from: LocationRequest.java */
/* loaded from: classes.dex */
public final class eo extends bh {
    Map<String, String> f;
    String g;
    byte[] h;
    byte[] i;
    boolean j;
    String k;
    Map<String, String> l;
    boolean m;
    private String n;

    public eo(Context context, ac acVar) {
        super(context, acVar);
        this.f = null;
        this.n = "";
        this.g = "";
        this.h = null;
        this.i = null;
        this.j = false;
        this.k = null;
        this.l = null;
        this.m = false;
    }

    public final void a(String str) {
        if (TextUtils.isEmpty(str)) {
            this.n = "";
        } else {
            this.n = str;
        }
    }

    @Override // com.loc.bh
    public final byte[] a_() {
        return this.h;
    }

    @Override // com.loc.bj
    public final Map<String, String> b() {
        return this.f;
    }

    public final void b(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                if (bArr != null) {
                    try {
                        byteArrayOutputStream.write(a(bArr));
                        byteArrayOutputStream.write(bArr);
                    } catch (Throwable th) {
                        th = th;
                        byteArrayOutputStream2 = byteArrayOutputStream;
                        th.printStackTrace();
                        if (byteArrayOutputStream2 != null) {
                            try {
                                byteArrayOutputStream2.close();
                                return;
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        return;
                    }
                }
                this.i = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
            byteArrayOutputStream = byteArrayOutputStream2;
        }
    }

    @Override // com.loc.bh, com.loc.bj
    public final Map<String, String> b_() {
        return this.l;
    }

    @Override // com.loc.bj
    public final String c() {
        return this.g;
    }

    @Override // com.loc.bh
    public final byte[] e() {
        return this.i;
    }

    @Override // com.loc.bh
    public final boolean g() {
        return this.j;
    }

    @Override // com.loc.bh
    public final String h() {
        return this.k;
    }

    @Override // com.loc.bh
    protected final boolean i() {
        return this.m;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.loc.bj
    public final String j() {
        return this.n;
    }
}
