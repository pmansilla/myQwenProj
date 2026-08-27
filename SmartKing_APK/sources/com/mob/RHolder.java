package com.mob;

import com.mob.tools.proguard.PublicMemberKeeper;

@Deprecated
/* loaded from: classes.dex */
public class RHolder implements PublicMemberKeeper {
    private static RHolder a;
    private int b;
    private int c;
    private int d;

    private RHolder() {
    }

    public static RHolder getInstance() {
        if (a == null) {
            synchronized (RHolder.class) {
                if (a == null) {
                    a = new RHolder();
                }
            }
        }
        return a;
    }

    public int getActivityThemeId() {
        return this.b;
    }

    public int getDialogLayoutId() {
        return this.c;
    }

    public int getDialogThemeId() {
        return this.d;
    }

    public RHolder setActivityThemeId(int i) {
        this.b = i;
        return a;
    }

    public RHolder setDialogLayoutId(int i) {
        this.c = i;
        return a;
    }

    public RHolder setDialogThemeId(int i) {
        this.d = i;
        return a;
    }
}
