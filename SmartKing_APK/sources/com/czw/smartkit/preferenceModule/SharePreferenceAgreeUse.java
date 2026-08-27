package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.entity.AgreeEntity;
import com.czw.smartkit.util.SaveObjectUtils;

/* loaded from: classes.dex */
public class SharePreferenceAgreeUse {
    public static void clearAll() {
        save(null);
    }

    public static AgreeEntity read() {
        return (AgreeEntity) SaveObjectUtils.getObject("cfg_agree_use", AgreeEntity.class);
    }

    public static void save(AgreeEntity agreeEntity) {
        SaveObjectUtils.setObject("cfg_agree_use", agreeEntity);
    }
}
