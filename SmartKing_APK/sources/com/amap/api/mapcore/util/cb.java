package com.amap.api.mapcore.util;

import com.amap.location.common.model.AmapLoc;

/* compiled from: DTFileInfo.java */
@ih(a = "update_item_file")
/* loaded from: classes.dex */
class cb {

    @ii(a = "mAdcode", b = 6)
    private String a;

    @ii(a = AmapLoc.TYPE_OFFLINE_CELL, b = 6)
    private String b;

    public cb() {
        this.a = "";
        this.b = "";
    }

    public cb(String str, String str2) {
        this.a = "";
        this.b = "";
        this.a = str;
        this.b = str2;
    }

    public static String a(String str) {
        return "mAdcode='" + str + "'";
    }

    public String a() {
        return this.b;
    }
}
