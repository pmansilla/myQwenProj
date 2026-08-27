package com.mob.elp.a;

import android.os.Bundle;
import android.text.TextUtils;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.MobSDK;
import com.mob.elp.PushMessage;

/* compiled from: ELPImpl.java */
/* loaded from: classes.dex */
class c implements Runnable {
    final /* synthetic */ Bundle a;
    final /* synthetic */ String b;
    final /* synthetic */ a c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(a aVar, Bundle bundle, String str) {
        this.c = aVar;
        this.a = bundle;
        this.b = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        PushMessage createPushMessage;
        try {
            String string = this.a.getString(AeUtil.ROOT_DATA_PATH_OLD_NAME);
            if (TextUtils.isEmpty(string) || (createPushMessage = PushMessage.createPushMessage(string, this.b)) == null) {
                return;
            }
            boolean z = false;
            boolean z2 = createPushMessage.unfold.showType == 1 && createPushMessage.unfold.images != null && createPushMessage.unfold.images.size() >= 4;
            if (createPushMessage.unfold.showType > 1 && !TextUtils.isEmpty(createPushMessage.unfold.image)) {
                z = true;
            }
            if (z2 || z) {
                if (createPushMessage.unfold.location == 1) {
                    a.a(this.c, MobSDK.getContext(), createPushMessage, this.b);
                } else if (createPushMessage.unfold.location == 2) {
                    a.a(this.c, createPushMessage, this.b);
                }
            }
        } catch (Throwable th) {
            com.mob.elp.d.d.a().a(th);
        }
    }
}
