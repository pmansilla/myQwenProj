package com.mob.commons.logcollector;

import com.mob.tools.log.LogCollector;
import com.mob.tools.proguard.ProtectedMemberKeeper;
import com.mob.tools.proguard.PublicMemberKeeper;

/* loaded from: classes.dex */
public abstract class LogsCollector implements LogCollector, ProtectedMemberKeeper, PublicMemberKeeper {
    public LogsCollector() {
        DefaultLogsCollector.get().addSDK(getSDKTag(), getSDKVersion());
    }

    protected abstract String getSDKTag();

    protected abstract int getSDKVersion();

    @Override // com.mob.tools.log.LogCollector
    public final void log(String str, int i, int i2, String str2, String str3) {
        DefaultLogsCollector.get().log(str, i, i2, str2, str3);
    }
}
