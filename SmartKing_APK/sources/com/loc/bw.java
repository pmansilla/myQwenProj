package com.loc;

/* compiled from: LogJsonDataStrategy.java */
/* loaded from: classes.dex */
public final class bw extends bz {
    private StringBuilder a;
    private boolean b;

    public bw() {
        this.a = new StringBuilder();
        this.b = true;
    }

    public bw(bz bzVar) {
        super(bzVar);
        this.a = new StringBuilder();
        this.b = true;
    }

    @Override // com.loc.bz
    protected final byte[] a(byte[] bArr) {
        byte[] a = ad.a(this.a.toString());
        this.d = a;
        this.b = true;
        this.a.delete(0, this.a.length());
        return a;
    }

    @Override // com.loc.bz
    public final void b(byte[] bArr) {
        String a = ad.a(bArr);
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
