package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.entity.UserDateTotalStep;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.util.SaveObjectUtils;
import java.text.SimpleDateFormat;

/* loaded from: classes.dex */
public class SharePreferenceUserLastStep {
    public static void clearAll() {
        save(null);
    }

    public static UserDateTotalStep read() {
        return (UserDateTotalStep) SaveObjectUtils.getObject(new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(System.currentTimeMillis())) + "_" + UserUtil.getUid(), UserDateTotalStep.class);
    }

    public static void save(UserDateTotalStep userDateTotalStep) {
        String format = new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(System.currentTimeMillis()));
        SaveObjectUtils.setObject(format + "_" + UserUtil.getUid(), format);
    }
}
