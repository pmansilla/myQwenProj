package com.mob.elp.a;

import android.os.Bundle;
import android.text.TextUtils;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.elp.PushMessage;
import java.util.HashMap;

/* compiled from: ELPImpl.java */
/* loaded from: classes.dex */
class b implements Runnable {
    final /* synthetic */ Bundle a;
    final /* synthetic */ String b;
    final /* synthetic */ a c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(a aVar, Bundle bundle, String str) {
        this.c = aVar;
        this.a = bundle;
        this.b = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        PushMessage createPushMessage;
        HashMap hashMap;
        HashMap hashMap2;
        try {
            String string = this.a.getString(AeUtil.ROOT_DATA_PATH_OLD_NAME);
            if (TextUtils.isEmpty(string) || (createPushMessage = PushMessage.createPushMessage(string, this.b)) == null) {
                return;
            }
            hashMap = this.c.d;
            if (hashMap.size() > 0) {
                this.c.a(createPushMessage);
            } else {
                hashMap2 = this.c.e;
                hashMap2.put(this.b, createPushMessage);
            }
        } catch (Throwable th) {
            com.mob.elp.d.d.a().a(th);
        }
    }
}
