package com.amap.api.mapcore.util;

/* compiled from: LogJsonDataStrategy.java */
/* loaded from: classes.dex */
public class jn extends jq {
    private StringBuilder a;
    private boolean b;

    public jn() {
        this.a = new StringBuilder();
        this.b = true;
    }

    public jn(jq jqVar) {
        super(jqVar);
        this.a = new StringBuilder();
        this.b = true;
    }

    @Override // com.amap.api.mapcore.util.jq
    protected byte[] a(byte[] bArr) {
        byte[] a = hp.a(this.a.toString());
        c(a);
        this.b = true;
        this.a.delete(0, this.a.length());
        return a;
    }

    @Override // com.amap.api.mapcore.util.jq
    public void b(byte[] bArr) {
        String a = hp.a(bArr);
        if (this.b) {
            this.b = false;
        } else {
            this.a.append(",");
        }
        StringBuilder sb = this.a;
        sb.append("{\"log\":\"");
        sb.append(a);
        sb.append("\"}");
    }
}
