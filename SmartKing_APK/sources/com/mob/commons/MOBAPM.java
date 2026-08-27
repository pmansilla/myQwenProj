package com.mob.commons;

import com.mob.tools.proguard.ClassKeeper;

/* loaded from: classes.dex */
public class MOBAPM implements MobProduct, ClassKeeper {
    public static final int SDK_VERSION_CODE;
    public static final String SDK_VERSION_NAME = "1.3.2";
    public static String sdkTag = "MOBAPM";

    static {
        String[] split = SDK_VERSION_NAME.split("\\.");
        int length = split.length;
        if (length > 3) {
            length = 3;
        }
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            i = (i * 100) + Integer.parseInt(split[i2]);
        }
        SDK_VERSION_CODE = i;
    }

    @Override // com.mob.commons.MobProduct
    public String getProductTag() {
        return sdkTag;
    }

    @Override // com.mob.commons.MobProduct
    public int getSdkver() {
        return SDK_VERSION_CODE;
    }
}
