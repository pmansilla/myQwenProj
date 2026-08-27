package com.mob.commons.b;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import com.mob.commons.b.f;

/* compiled from: Huawei.java */
/* loaded from: classes.dex */
public class e extends f {
    public e(Context context) {
        super(context);
    }

    @Override // com.mob.commons.b.f
    protected Intent a() {
        Intent intent = new Intent(com.mob.commons.k.a(80));
        intent.setPackage(com.mob.commons.k.a(81));
        return intent;
    }

    @Override // com.mob.commons.b.f
    public f.c a(IBinder iBinder) {
        String a = com.mob.commons.k.a(82);
        f.c cVar = new f.c();
        cVar.b = a(com.mob.commons.k.a(69), iBinder, a, 1, new String[0]);
        a(com.mob.commons.k.a(83), iBinder, a, 2);
        cVar.a = !TextUtils.isEmpty(cVar.b);
        return cVar;
    }

    @Override // com.mob.commons.b.f
    public synchronized String b() {
        return i();
    }
}
