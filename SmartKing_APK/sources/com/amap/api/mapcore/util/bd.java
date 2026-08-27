package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.mapcore.util.bc;

/* compiled from: ScaleRotateGestureDetector.java */
/* loaded from: classes.dex */
public class bd extends bc {

    /* compiled from: ScaleRotateGestureDetector.java */
    /* loaded from: classes.dex */
    public static abstract class a implements bc.a {
        @Override // com.amap.api.mapcore.util.bc.a
        public boolean a(bc bcVar) {
            return a((bd) bcVar);
        }

        public abstract boolean a(bd bdVar);

        @Override // com.amap.api.mapcore.util.bc.a
        public boolean b(bc bcVar) {
            return b((bd) bcVar);
        }

        public abstract boolean b(bd bdVar);

        @Override // com.amap.api.mapcore.util.bc.a
        public void c(bc bcVar) {
            c((bd) bcVar);
        }

        public abstract void c(bd bdVar);
    }

    public bd(Context context, a aVar) {
        super(context, aVar);
    }

    public float l() {
        return (float) (((Math.atan2(i(), h()) - Math.atan2(f(), e())) * 180.0d) / 3.141592653589793d);
    }
}
