package com.mob.commons;

import com.mob.guard.MobGuard;
import com.mob.tools.proguard.ClassKeeper;

/* loaded from: classes.dex */
public class MOBGUARD implements MobProduct, ClassKeeper {
    @Override // com.mob.commons.MobProduct
    public String getProductTag() {
        return MobGuard.getSdkTag();
    }

    @Override // com.mob.commons.MobProduct
    public int getSdkver() {
        return MobGuard.SDK_VERSION_CODE;
    }
}
