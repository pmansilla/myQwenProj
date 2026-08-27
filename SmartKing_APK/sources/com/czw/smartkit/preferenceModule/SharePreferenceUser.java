package com.czw.smartkit.preferenceModule;

import com.czw.smartkit.net.domain.UserDTO;
import com.czw.smartkit.util.SaveObjectUtils;

/* loaded from: classes.dex */
public class SharePreferenceUser {
    public static void clearAll() {
        save(null);
    }

    public static UserDTO read() {
        return (UserDTO) SaveObjectUtils.getObject("cfg_user", UserDTO.class);
    }

    public static void save(UserDTO userDTO) {
        SaveObjectUtils.setObject("cfg_user", userDTO);
    }
}
