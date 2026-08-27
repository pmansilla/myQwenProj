package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.bleModule.data.AlostLTO;
import com.czw.smartkit.util.SaveObjectUtils;

/* loaded from: classes.dex */
public class SharePreferenceAlost {
    public static void clearAll() {
        save(null);
    }

    public static AlostLTO read() {
        AlostLTO alostLTO = (AlostLTO) SaveObjectUtils.getObject("cfg_alost", AlostLTO.class);
        return alostLTO == null ? new AlostLTO() : alostLTO;
    }

    public static void save(AlostLTO alostLTO) {
        SaveObjectUtils.setObject("cfg_alost", alostLTO);
    }
}
