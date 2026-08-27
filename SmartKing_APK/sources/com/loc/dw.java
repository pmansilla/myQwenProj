package com.loc;

/* compiled from: StringUtils.java */
/* loaded from: classes.dex */
public final class dw {
    public static boolean a(String str) {
        return str == null || str.length() <= 0;
    }

    public static int b(String str) {
        if (str.length() <= 0) {
            return 0;
        }
        int i = 0;
        for (char c : str.toCharArray()) {
            i = (i * 31) + c;
        }
        return i;
    }
}
