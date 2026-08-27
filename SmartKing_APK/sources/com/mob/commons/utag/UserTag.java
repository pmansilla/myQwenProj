package com.mob.commons.utag;

import com.mob.tools.proguard.PublicMemberKeeper;

/* loaded from: classes.dex */
public class UserTag implements PublicMemberKeeper {
    private UserTag() {
    }

    public static TagRequester getUserTags() {
        return new TagRequester();
    }

    public static UserTager tagUser() {
        return new UserTager();
    }
}
