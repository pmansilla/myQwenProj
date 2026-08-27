package com.mob.commons.b;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.mob.commons.b.f;

/* compiled from: ASUS.java */
/* loaded from: classes.dex */
public class a extends f {
    public a(Context context) {
        super(context);
    }

    @Override // com.mob.commons.b.f
    protected Intent a() {
        Intent intent = new Intent(com.mob.commons.k.a(76));
        intent.setComponent(new ComponentName(com.mob.commons.k.a(77), com.mob.commons.k.a(78)));
        return intent;
    }

    @Override // com.mob.commons.b.f
    public f.c a(IBinder iBinder) {
        f.c cVar = new f.c();
        cVar.d = a(com.mob.commons.k.a(71), iBinder, com.mob.commons.k.a(79), 2, new String[0]);
        cVar.b = a(com.mob.commons.k.a(69), iBinder, com.mob.commons.k.a(79), 3, new String[0]);
        cVar.e = a(com.mob.commons.k.a(70), iBinder, com.mob.commons.k.a(79), 4, new String[0]);
        cVar.c = a(com.mob.commons.k.a(75), iBinder, com.mob.commons.k.a(79), 5, new String[0]);
        cVar.a = a("isSupported", iBinder, com.mob.commons.k.a(79), 1) != 0;
        return cVar;
    }
}
