package com.mob.elp.a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.mob.mcl.BusinessCallBack;
import com.mob.mcl.MCLSDK;
import com.mob.mcl.util.MCLog;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ELPImpl.java */
/* loaded from: classes.dex */
public class e extends BroadcastReceiver {
    final /* synthetic */ com.mob.elp.a.a a;

    /* compiled from: ELPImpl.java */
    /* loaded from: classes.dex */
    class a implements BusinessCallBack<Boolean> {
        a() {
        }

        @Override // com.mob.mcl.BusinessCallBack
        public void callback(Boolean bool) {
            Boolean bool2 = bool;
            try {
                com.mob.elp.d.d.a().a("tcp status = " + bool2);
                e.this.a.c();
            } catch (Throwable th) {
                com.mob.elp.d.d.a().a("check tcp status ex : " + th.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(com.mob.elp.a.a aVar) {
        this.a = aVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            if ("com.mob.elp.intent.CHECK_TCP_STATUS".equals(intent.getAction()) && context.getPackageName().equals(intent.getPackage())) {
                MCLSDK.getClientTcpStatus(new a());
            }
        } catch (Throwable th) {
            MCLog.getInstance().d(th);
        }
    }
}
