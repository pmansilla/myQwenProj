package com.tencent.bugly.proguard;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public final class ah extends k implements Cloneable {
    public String a = "";
    private String d = "";
    public String b = "";
    private String e = "";
    public String c = "";

    @Override // com.tencent.bugly.proguard.k
    public final void a(i iVar) {
        this.a = iVar.b(0, true);
        this.d = iVar.b(1, false);
        this.b = iVar.b(2, false);
        this.e = iVar.b(3, false);
        this.c = iVar.b(4, false);
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(j jVar) {
        jVar.a(this.a, 0);
        if (this.d != null) {
            jVar.a(this.d, 1);
        }
        if (this.b != null) {
            jVar.a(this.b, 2);
        }
        if (this.e != null) {
            jVar.a(this.e, 3);
        }
        if (this.c != null) {
            jVar.a(this.c, 4);
        }
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(StringBuilder sb, int i) {
    }
}
