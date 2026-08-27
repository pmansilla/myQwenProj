package com.amap.api.mapcore.util;

/* compiled from: DownloadManager.java */
/* loaded from: classes.dex */
public class iu {
    private iv a;
    private ix b;

    /* compiled from: DownloadManager.java */
    /* loaded from: classes.dex */
    public interface a {
        void onDownload(byte[] bArr, long j);

        void onException(Throwable th);

        void onFinish();

        void onStop();
    }

    public iu(ix ixVar) {
        this(ixVar, 0L, -1L);
    }

    public iu(ix ixVar, long j, long j2) {
        this(ixVar, j, j2, false);
    }

    public iu(ix ixVar, long j, long j2, boolean z) {
        this.b = ixVar;
        this.a = new iv(this.b.a, this.b.b, ixVar.c == null ? null : ixVar.c, z);
        this.a.b(j2);
        this.a.a(j);
    }

    public void a() {
        this.a.a();
    }

    public void a(a aVar) {
        this.a.a(this.b.getURL(), this.b.isIPRequest(), this.b.getIPDNSName(), this.b.getRequestHead(), this.b.getParams(), this.b.getEntityBytes(), aVar);
    }
}
