package com.squareup.okhttp;

import java.io.UnsupportedEncodingException;
import okio.ByteString;

/* loaded from: classes2.dex */
public final class Credentials {
    private Credentials() {
    }

    public static String basic(String str, String str2) {
        try {
            return "Basic " + ByteString.of((str + ":" + str2).getBytes("ISO-8859-1")).base64();
        } catch (UnsupportedEncodingException unused) {
            throw new AssertionError();
        }
    }
}
