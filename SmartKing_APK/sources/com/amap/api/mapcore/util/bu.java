package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Bundle;
import com.amap.api.mapcore.util.cj;
import com.amap.api.mapcore.util.cp;
import com.amap.api.maps.AMap;
import java.io.IOException;

/* compiled from: OfflineMapDownloadTask.java */
/* loaded from: classes.dex */
public class bu extends jz implements cj.a {
    private cj a;
    private cl b;
    private co c;
    private Context e;
    private Bundle f;
    private boolean g;

    public bu(co coVar, Context context) {
        this.f = new Bundle();
        this.g = false;
        this.c = coVar;
        this.e = context;
    }

    public bu(co coVar, Context context, AMap aMap) {
        this(coVar, context);
    }

    private String d() {
        return fr.c(this.e);
    }

    private void e() throws IOException {
        this.a = new cj(new ck(this.c.getUrl(), d(), this.c.z(), 1, this.c.A()), this.c.getUrl(), this.e, this.c);
        this.a.a(this);
        this.b = new cl(this.c, this.c);
        if (this.g) {
            return;
        }
        this.a.a();
    }

    public void a() {
        this.g = true;
        if (this.a != null) {
            this.a.b();
        } else {
            cancelTask();
        }
        if (this.b != null) {
            this.b.a();
        }
    }

    public void b() {
        if (this.f != null) {
            this.f.clear();
            this.f = null;
        }
    }

    @Override // com.amap.api.mapcore.util.cj.a
    public void c() {
        if (this.b != null) {
            this.b.b();
        }
    }

    @Override // com.amap.api.mapcore.util.jz
    public void runTask() {
        if (this.c.y()) {
            this.c.a(cp.a.file_io_exception);
            return;
        }
        try {
            e();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
