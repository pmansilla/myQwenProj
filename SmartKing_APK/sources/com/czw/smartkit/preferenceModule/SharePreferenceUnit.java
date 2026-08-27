package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.bleModule.data.UnitCfg;
import com.czw.smartkit.util.SaveObjectUtils;

/* loaded from: classes.dex */
public class SharePreferenceUnit {
    public static void clearAll() {
        save(null);
    }

    public static UnitCfg read() {
        UnitCfg unitCfg = (UnitCfg) SaveObjectUtils.getObject("cfg_unit", UnitCfg.class);
        return unitCfg == null ? new UnitCfg() : unitCfg;
    }

    public static void save(UnitCfg unitCfg) {
        SaveObjectUtils.setObject("cfg_unit", unitCfg);
    }
}
