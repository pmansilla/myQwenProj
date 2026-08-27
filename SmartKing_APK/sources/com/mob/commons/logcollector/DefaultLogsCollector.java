package com.mob.commons.logcollector;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import cn.sharesdk.framework.ShareSDK;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.log.LogCollector;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.Data;
import java.util.HashMap;

/* loaded from: classes.dex */
public class DefaultLogsCollector implements LogCollector, PublicMemberKeeper {
    private static DefaultLogsCollector a;
    private final HashMap<String, Integer> b = new HashMap<>();
    private c c = c.a();
    private boolean d;

    private DefaultLogsCollector() {
        try {
            if (MobSDK.getContext().getPackageManager().getPackageInfo("cn.sharesdk.log", 64) != null) {
                this.d = true;
            }
        } catch (Throwable unused) {
            this.d = false;
        }
    }

    public static synchronized DefaultLogsCollector get() {
        DefaultLogsCollector defaultLogsCollector;
        synchronized (DefaultLogsCollector.class) {
            if (a == null) {
                a = new DefaultLogsCollector();
            }
            defaultLogsCollector = a;
        }
        return defaultLogsCollector;
    }

    final int a(int i, String str) {
        if (MobSDK.getContext() == null || !this.d) {
            return 0;
        }
        try {
            Intent intent = new Intent();
            intent.setPackage("cn.sharesdk.log");
            String packageName = MobSDK.getContext().getPackageName();
            intent.putExtra("package", packageName);
            intent.putExtra("priority", i);
            intent.putExtra(NotificationCompat.CATEGORY_MESSAGE, Data.AES128Encode(packageName, str));
            MobSDK.getContext().sendBroadcast(intent);
            return 0;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return 0;
        }
    }

    public void addSDK(String str, int i) {
        synchronized (this.b) {
            Integer num = this.b.get(str);
            this.b.put(str, Integer.valueOf(i));
            if (num == null && this.c != null) {
                this.c.a(i, str);
            }
        }
    }

    @Override // com.mob.tools.log.LogCollector
    public final void log(String str, int i, int i2, String str2, String str3) {
        Integer num;
        a(i, str3);
        if ("MOBTOOLS".equalsIgnoreCase(str)) {
            num = this.b.get("MOBSDK");
            if (num == null) {
                num = -999;
            }
        } else {
            num = this.b.get(str);
            if (num == null) {
                return;
            }
        }
        if (!ShareSDK.SDK_TAG.equals(str) || (str3.contains("com.mob.") && str3.contains("cn.sharesdk."))) {
            if (i2 == 1) {
                this.c.b(num.intValue(), i2, str, str3);
            } else if (i2 == 2) {
                this.c.a(num.intValue(), i2, str, str3);
            } else if (i2 == 3) {
                this.c.a(num.intValue(), i2, str, str3);
            }
        }
    }
}
