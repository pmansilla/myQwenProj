package com.tencent.bugly.proguard;

import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class an extends k {
    private static byte[] i;
    private static Map<String, String> j = new HashMap();
    public byte a = 0;
    public int b = 0;
    public byte[] c = null;
    private String f = "";
    public long d = 0;
    private String g = "";
    public String e = "";
    private Map<String, String> h = null;

    static {
        i = r0;
        byte[] bArr = {0};
        j.put("", "");
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(i iVar) {
        this.a = iVar.a(this.a, 0, true);
        this.b = iVar.a(this.b, 1, true);
        byte[] bArr = i;
        this.c = iVar.c(2, false);
        this.f = iVar.b(3, false);
        this.d = iVar.a(this.d, 4, false);
        this.g = iVar.b(5, false);
        this.e = iVar.b(6, false);
        this.h = (Map) iVar.a((i) j, 7, false);
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(j jVar) {
        jVar.a(this.a, 0);
        jVar.a(this.b, 1);
        if (this.c != null) {
            jVar.a(this.c, 2);
        }
        if (this.f != null) {
            jVar.a(this.f, 3);
        }
        jVar.a(this.d, 4);
        if (this.g != null) {
            jVar.a(this.g, 5);
        }
        if (this.e != null) {
            jVar.a(this.e, 6);
        }
        if (this.h != null) {
            jVar.a((Map) this.h, 7);
        }
    }
}
