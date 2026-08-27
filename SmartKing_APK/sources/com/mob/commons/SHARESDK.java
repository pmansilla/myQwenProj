package com.mob.commons;

import cn.sharesdk.framework.ShareSDK;

/* loaded from: classes.dex */
public class SHARESDK implements MobProduct {
    @Override // com.mob.commons.MobProduct
    public String getProductTag() {
        return ShareSDK.SDK_TAG;
    }

    @Override // com.mob.commons.MobProduct
    public int getSdkver() {
        return ShareSDK.SDK_VERSION_CODE;
    }
}
