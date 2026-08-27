package com.amap.api.mapcore.util;

import com.amap.api.maps.MapsInitializer;
import java.util.Map;

/* compiled from: AbstractAMapRequest.java */
/* loaded from: classes.dex */
public abstract class eq extends ix {
    protected boolean isPostFlag = true;

    @Override // com.amap.api.mapcore.util.ix
    public Map<String, String> getParams() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.ix
    public Map<String, String> getRequestHead() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] makeHttpRequest() throws hc {
        int protocol = MapsInitializer.getProtocol();
        iw a = iw.a(false);
        if (protocol == 1) {
            return this.isPostFlag ? a.b(this) : a.d(this);
        }
        if (protocol == 2) {
            return this.isPostFlag ? a.a(this) : a.e(this);
        }
        return null;
    }
}
