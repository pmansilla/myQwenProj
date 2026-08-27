package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.bleModule.data.AlarmClock;
import com.czw.smartkit.util.SaveObjectUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class SharePreferenceClock {
    public static void clearAll() {
        save(null);
    }

    public static List<AlarmClock> read() {
        return (List) SaveObjectUtils.getObject("cfg_clock", AlarmClock.class);
    }

    public static void save(ArrayList<AlarmClock> arrayList) {
        SaveObjectUtils.setObject("cfg_clock", arrayList);
    }
}
