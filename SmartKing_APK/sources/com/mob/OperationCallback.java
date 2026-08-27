package com.mob;

import com.mob.tools.proguard.PublicMemberKeeper;

/* loaded from: classes.dex */
public abstract class OperationCallback<T> implements PublicMemberKeeper {
    public abstract void onComplete(T t);

    public abstract void onFailure(Throwable th);
}
