package com.amap.api.mapcore.util;

/* compiled from: DTDownloadInfo.java */
@ih(a = "update_item_download_info")
/* loaded from: classes.dex */
class ca {

    @ii(a = "mAdcode", b = 6)
    private String a;

    @ii(a = "fileLength", b = 5)
    private long b;

    @ii(a = "splitter", b = 2)
    private int c;

    @ii(a = "startPos", b = 5)
    private long d;

    @ii(a = "endPos", b = 5)
    private long e;

    public ca() {
        this.a = "";
        this.b = 0L;
        this.c = 0;
        this.d = 0L;
        this.e = 0L;
    }

    public ca(String str, long j, int i, long j2, long j3) {
        this.a = "";
        this.b = 0L;
        this.c = 0;
        this.d = 0L;
        this.e = 0L;
        this.a = str;
        this.b = j;
        this.c = i;
        this.d = j2;
        this.e = j3;
    }

    public static String a(String str) {
        return "mAdcode='" + str + "'";
    }
}
