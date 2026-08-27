package com.mob.mcl.a;

import android.os.Bundle;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.apc.APCMessage;
import com.mob.tools.network.HttpConnection;
import com.mob.tools.network.HttpResponseCallback;
import com.mob.tools.utils.Hashon;

/* compiled from: ApcHelper.java */
/* loaded from: classes.dex */
class a implements HttpResponseCallback {
    final /* synthetic */ APCMessage a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(b bVar, APCMessage aPCMessage) {
        this.a = aPCMessage;
    }

    @Override // com.mob.tools.network.HttpResponseCallback
    public void onResponse(HttpConnection httpConnection) throws Throwable {
        if (httpConnection instanceof com.mob.mcl.b.c) {
            Bundle bundle = new Bundle();
            bundle.putString(AeUtil.ROOT_DATA_PATH_OLD_NAME, new Hashon().fromHashMap(((com.mob.mcl.b.c) httpConnection).a()));
            this.a.data = bundle;
        }
    }
}
