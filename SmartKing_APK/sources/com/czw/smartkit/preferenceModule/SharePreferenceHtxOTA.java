package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.databaseModule.htxOta.HtxOtaFailureEntity;
import com.czw.smartkit.databaseModule.htxOta.HtxOtaFailureEntityDatabaseUtil;
import com.czw.smartkit.user.UserUtil;

/* loaded from: classes.dex */
public class SharePreferenceHtxOTA {
    public static void clear() {
        HtxOtaFailureEntityDatabaseUtil.getInstance().deleteData(UserUtil.getUid());
    }

    public static HtxOtaFailureEntity read() {
        return HtxOtaFailureEntityDatabaseUtil.getInstance().getUserDevice(UserUtil.getUid());
    }

    public static void save(HtxOtaFailureEntity htxOtaFailureEntity) {
        HtxOtaFailureEntityDatabaseUtil.getInstance().saveData(htxOtaFailureEntity);
    }
}
