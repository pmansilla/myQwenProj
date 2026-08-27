package cn.smssdk.gui.util;

import cn.smssdk.gui.entity.Profile;
import com.mob.MobSDK;
import com.mob.tools.utils.SharePrefrenceHelper;

/* loaded from: classes.dex */
public class GUISPDB {
    private static final String DB_NAME = "SMSSDKGUI_SPDB";
    private static final int DB_VERSION = 1;
    private static final String KEY_PROFILE = "key_profile";
    private static final String KEY_TEMP_CODE = "key_tempCode";
    private static SharePrefrenceHelper sp = new SharePrefrenceHelper(MobSDK.getContext());

    static {
        sp.open(DB_NAME, 1);
    }

    public static Profile getProfile() {
        return (Profile) sp.get(KEY_PROFILE);
    }

    public static String getTempCode() {
        return sp.getString(KEY_TEMP_CODE);
    }

    public static void setProfile(Profile profile) {
        sp.put(KEY_PROFILE, profile);
    }

    public static void setTempCode(String str) {
        sp.putString(KEY_TEMP_CODE, str);
    }
}
