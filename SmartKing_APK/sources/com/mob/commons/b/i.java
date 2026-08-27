package com.mob.commons.b;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.autonavi.amap.mapcore.tools.GlMapUtil;
import com.czw.smartkit.BuildConfig;
import com.mob.commons.b.f;

/* compiled from: Nubia.java */
/* loaded from: classes.dex */
public class i extends f {
    private Uri c;

    public i(Context context) {
        super(context);
        this.c = Uri.parse(com.mob.commons.k.a(118));
    }

    private String a(String str, String str2) {
        Bundle b = b(str, str2);
        if (a(b)) {
            return b.getString(com.mob.commons.k.a(122));
        }
        if (b != null) {
            return b.getString(com.mob.commons.k.a(123));
        }
        return null;
    }

    private boolean a(Bundle bundle) {
        if (bundle == null) {
            return false;
        }
        try {
            return bundle.getInt(com.mob.commons.k.a(119), -1) == 0;
        } catch (Throwable unused) {
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v3 */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARN: Type inference failed for: r0v8 */
    private Bundle b(String str, String str2) {
        Bundle bundle;
        ?? r0 = 0;
        r0 = 0;
        r0 = 0;
        r0 = 0;
        try {
            if (Build.VERSION.SDK_INT >= 17) {
                ContentProviderClient acquireUnstableContentProviderClient = this.a.getContentResolver().acquireUnstableContentProviderClient(this.c);
                bundle = acquireUnstableContentProviderClient.call(str, str2, null);
                if (acquireUnstableContentProviderClient != null) {
                    try {
                        r0 = 24;
                        r0 = 24;
                        if (Build.VERSION.SDK_INT >= 24) {
                            acquireUnstableContentProviderClient.close();
                        } else {
                            acquireUnstableContentProviderClient.release();
                        }
                    } catch (Throwable th) {
                        th = th;
                        c.a().a(th);
                        return bundle;
                    }
                }
            } else {
                bundle = Build.VERSION.SDK_INT >= 11 ? this.a.getContentResolver().call(this.c, str, str2, (Bundle) null) : null;
            }
        } catch (Throwable th2) {
            th = th2;
            bundle = r0;
        }
        return bundle;
    }

    private boolean j() {
        Bundle b = b(com.mob.commons.k.a(GlMapUtil.DEVICE_DISPLAY_DPI_LOW), null);
        if (a(b)) {
            return b.getBoolean(com.mob.commons.k.a(BuildConfig.VERSION_CODE), true);
        }
        return false;
    }

    @Override // com.mob.commons.b.f
    protected f.c c() {
        f.c cVar = new f.c();
        cVar.a = j();
        cVar.c = a(com.mob.commons.k.a(102), this.b);
        cVar.b = a(com.mob.commons.k.a(100), (String) null);
        cVar.e = a(com.mob.commons.k.a(101), this.b);
        return cVar;
    }
}
