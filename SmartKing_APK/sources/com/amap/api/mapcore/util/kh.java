package com.amap.api.mapcore.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.autonavi.amap.mapcore.Inner_3dMap_location;

/* compiled from: MapLocationManagerResultHandler.java */
/* loaded from: classes.dex */
public final class kh extends Handler {
    kf a;

    public kh(Looper looper, kf kfVar) {
        super(looper);
        this.a = null;
        this.a = kfVar;
    }

    public kh(kf kfVar) {
        this.a = null;
        this.a = kfVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        if (message.what != 1) {
            return;
        }
        try {
            if (this.a != null) {
                this.a.a((Inner_3dMap_location) message.obj);
            }
        } catch (Throwable th) {
            kw.a(th, "ClientResultHandler", "RESULT_LOCATION_FINISH");
        }
    }
}
