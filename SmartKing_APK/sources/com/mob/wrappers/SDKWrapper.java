package com.mob.wrappers;

import com.mob.commons.MobProduct;
import com.mob.commons.MobProductCollector;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class SDKWrapper {
    /* JADX INFO: Access modifiers changed from: protected */
    public static int isAvailable(String str) {
        try {
            Iterator<MobProduct> it = MobProductCollector.getProducts().iterator();
            while (it.hasNext()) {
                if (str.equals(it.next().getProductTag())) {
                    return 1;
                }
            }
            return -1;
        } catch (Throwable unused) {
            return -1;
        }
    }
}
