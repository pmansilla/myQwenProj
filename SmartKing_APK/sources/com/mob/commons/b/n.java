package com.mob.commons.b;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import basecamera.module.lib.CameraInterface;
import com.mob.commons.b.f;

/* compiled from: Zte.java */
/* loaded from: classes.dex */
public class n extends f {
    public n(Context context) {
        super(context);
    }

    private void j() {
        try {
            Intent intent = new Intent();
            intent.setClassName(com.mob.commons.k.a(138), com.mob.commons.k.a(139));
            intent.setAction(com.mob.commons.k.a(141));
            intent.putExtra(com.mob.commons.k.a(142), this.b);
            intent.putExtra(com.mob.commons.k.a(143), true);
            if (this.a.startService(intent) != null) {
            }
        } catch (Throwable th) {
            c.a().a(th);
        }
    }

    private boolean k() {
        try {
            this.a.getPackageManager().getPackageInfo(com.mob.commons.k.a(138), 0);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    @Override // com.mob.commons.b.f
    protected Intent a() {
        j();
        Intent intent = new Intent();
        intent.setClassName(com.mob.commons.k.a(138), com.mob.commons.k.a(140));
        intent.setAction(com.mob.commons.k.a(CameraInterface.TYPE_RECORDER));
        intent.putExtra(com.mob.commons.k.a(142), this.b);
        return intent;
    }

    @Override // com.mob.commons.b.f
    protected f.c a(IBinder iBinder) {
        f.c cVar = new f.c();
        cVar.b = a(com.mob.commons.k.a(69), iBinder, com.mob.commons.k.a(CameraInterface.TYPE_CAPTURE), 3, new String[0]);
        cVar.a = k();
        return cVar;
    }
}
