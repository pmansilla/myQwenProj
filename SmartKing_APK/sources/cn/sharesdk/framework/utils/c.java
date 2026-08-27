package cn.sharesdk.framework.utils;

/* compiled from: Preconditions.java */
/* loaded from: classes.dex */
public final class c {
    public static <T> T a(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }
}
