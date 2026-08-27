package com.mob.wrappers;

import com.mob.moblink.ActionListener;
import com.mob.moblink.MobLink;
import com.mob.moblink.Scene;
import com.mob.tools.proguard.PublicMemberKeeper;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class MobLinkWrapper extends SDKWrapper implements PublicMemberKeeper {
    private static int state;

    /* loaded from: classes2.dex */
    public interface MobIdCallback {
        void onError(Throwable th);

        void onResult(String str);
    }

    public static boolean getMobId(String str, String str2, Map<String, Object> map, final MobIdCallback mobIdCallback) {
        if (!isAvailable()) {
            return false;
        }
        Scene scene = new Scene();
        scene.path = str;
        scene.source = str2;
        scene.params = new HashMap(map);
        MobLink.getMobID(scene, new ActionListener<String>() { // from class: com.mob.wrappers.MobLinkWrapper.1
            public void onError(Throwable th) {
                MobIdCallback.this.onError(th);
            }

            public void onResult(String str3) {
                MobIdCallback.this.onResult(str3);
            }
        });
        return true;
    }

    private static synchronized boolean isAvailable() {
        boolean z;
        synchronized (MobLinkWrapper.class) {
            if (state == 0) {
                state = isAvailable("MOBLINK");
            }
            z = state == 1;
        }
        return z;
    }
}
