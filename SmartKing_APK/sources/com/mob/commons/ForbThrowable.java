package com.mob.commons;

/* loaded from: classes.dex */
public class ForbThrowable extends Throwable {
    public ForbThrowable() {
        super("Service is forbidden currently");
    }

    public ForbThrowable(String str) {
        super(str);
    }
}
