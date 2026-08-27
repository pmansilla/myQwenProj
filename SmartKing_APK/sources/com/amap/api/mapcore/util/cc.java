package com.amap.api.mapcore.util;

import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.sun.mail.imap.IMAPStore;

/* compiled from: DTInfo.java */
@ih(a = "update_item")
/* loaded from: classes.dex */
public class cc {

    @ii(a = "localPath", b = 6)
    protected String h;

    @ii(a = "mCompleteCode", b = 2)
    protected int j;

    @ii(a = "mState", b = 2)
    public int l;

    @ii(a = "title", b = 6)
    protected String a = null;

    @ii(a = FileDownloadModel.URL, b = 6)
    protected String b = null;

    @ii(a = "mAdcode", b = 6)
    protected String c = null;

    @ii(a = "fileName", b = 6)
    protected String d = null;

    @ii(a = IMAPStore.ID_VERSION, b = 6)
    protected String e = "";

    @ii(a = "lLocalLength", b = 5)
    protected long f = 0;

    @ii(a = "lRemoteLength", b = 5)
    protected long g = 0;

    @ii(a = "isProvince", b = 2)
    protected int i = 0;

    @ii(a = "mCityCode", b = 6)
    protected String k = "";

    @ii(a = "mPinyin", b = 6)
    public String m = "";

    public static String e(String str) {
        return "mAdcode='" + str + "'";
    }

    public static String f(String str) {
        return "mPinyin='" + str + "'";
    }

    public void c(String str) {
        this.c = str;
    }

    public String d() {
        return this.a;
    }

    public void d(String str) {
        this.k = str;
    }

    public String e() {
        return this.e;
    }

    public String f() {
        return this.c;
    }

    public String g() {
        return this.b;
    }

    public int h() {
        return this.j;
    }

    public String i() {
        return this.m;
    }
}
