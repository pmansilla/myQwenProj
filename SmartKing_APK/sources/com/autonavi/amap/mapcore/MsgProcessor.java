package com.autonavi.amap.mapcore;

import android.os.Build;

/* loaded from: classes.dex */
public class MsgProcessor {
    public native void nativeCall();

    public native void nativeInit(int i, String str);

    public void nativeInitMsg() {
        try {
            nativeInit(Build.VERSION.SDK_INT, Build.MODEL);
        } catch (Throwable unused) {
        }
    }

    public void nativeMsgProcessor(String str) {
    }
}
