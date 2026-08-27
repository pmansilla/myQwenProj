package com.amap.api.mapcore.util;

import java.io.File;

/* compiled from: FileNumUpdateStrategy.java */
/* loaded from: classes.dex */
public class js extends jw {
    private int b;
    private String c;

    public js(int i, String str, jw jwVar) {
        super(jwVar);
        this.b = i;
        this.c = str;
    }

    public int a(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                return file.list().length;
            }
            return 0;
        } catch (Throwable th) {
            ic.c(th, "fus", "gfn");
            return 0;
        }
    }

    @Override // com.amap.api.mapcore.util.jw
    protected boolean a() {
        return a(this.c) >= this.b;
    }
}
