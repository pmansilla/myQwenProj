package com.mob.commons.b;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.mob.commons.b.f;

/* compiled from: MotoLennovo.java */
/* loaded from: classes.dex */
public class h extends f {
    public h(Context context) {
        super(context);
    }

    @Override // com.mob.commons.b.f
    protected Intent a() {
        Intent intent = new Intent();
        intent.setClassName(com.mob.commons.k.a(84), com.mob.commons.k.a(85));
        return intent;
    }

    @Override // com.mob.commons.b.f
    public f.c a(IBinder iBinder) {
        String a = com.mob.commons.k.a(86);
        f.c cVar = new f.c();
        cVar.b = a(com.mob.commons.k.a(69), iBinder, a, 1, new String[0]);
        cVar.e = a(com.mob.commons.k.a(70), iBinder, a, 4, this.b);
        cVar.d = a(com.mob.commons.k.a(71), iBinder, a, 2, new String[0]);
        cVar.c = a(com.mob.commons.k.a(75), iBinder, a, 5, this.b);
        cVar.a = a(com.mob.commons.k.a(74), iBinder, a, 3) != 0;
        return cVar;
    }

    @Override // com.mob.commons.b.f
    protected long d() {
        return 3000L;
    }
}
