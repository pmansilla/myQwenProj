package com.mob.commons;

/* loaded from: classes.dex */
public class SMSSDK implements MobProduct {
    @Override // com.mob.commons.MobProduct
    public String getProductTag() {
        return "SMSSDK";
    }

    @Override // com.mob.commons.MobProduct
    public int getSdkver() {
        return cn.smssdk.utils.a.e;
    }
}
