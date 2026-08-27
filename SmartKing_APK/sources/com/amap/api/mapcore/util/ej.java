package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.mapcore.util.ei;
import com.amap.api.maps.MapsInitializer;
import com.autonavi.amap.mapcore.FileUtil;
import me.panpf.sketch.uri.FileUriModel;

/* compiled from: CustomStyleTextureTask.java */
/* loaded from: classes.dex */
public class ej implements Runnable {
    private Context a;
    private ei b;
    private ep c;
    private a d;

    /* compiled from: CustomStyleTextureTask.java */
    /* loaded from: classes.dex */
    public interface a {
        void a(String str, ep epVar);
    }

    public ej(Context context) {
        this.a = context;
        if (this.b == null) {
            this.b = new ei(this.a, "");
        }
    }

    private String a(Context context) {
        return FileUtil.getMapBaseStorage(context);
    }

    private void a(String str, byte[] bArr) {
        FileUtil.writeDatasToFile(str, bArr);
    }

    public void a() {
        this.a = null;
        if (this.b != null) {
            this.b = null;
        }
    }

    public void a(a aVar) {
        this.d = aVar;
    }

    public void a(ep epVar) {
        this.c = epVar;
    }

    public void a(String str) {
        if (this.b != null) {
            this.b.a(str);
        }
    }

    public void b() {
        fq.a().a(this);
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (MapsInitializer.getNetWorkEnable()) {
                if (this.b != null) {
                    ei.a a2 = this.b.a();
                    String str = null;
                    if (a2 != null && a2.a != null) {
                        str = a(this.a) + FileUriModel.SCHEME + "custom_texture_data";
                        a(str, a2.a);
                    }
                    if (this.d != null) {
                        this.d.a(str, this.c);
                    }
                }
                ic.a(this.a, fr.e());
            }
        } catch (Throwable th) {
            ic.c(th, "CustomStyleTask", "download customStyle");
            th.printStackTrace();
        }
    }
}
