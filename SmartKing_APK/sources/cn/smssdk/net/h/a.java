package cn.smssdk.net.h;

import android.util.Base64;
import java.nio.charset.Charset;

/* compiled from: Base64Utils.java */
/* loaded from: classes.dex */
public abstract class a {
    static {
        Charset.defaultCharset();
    }

    public static byte[] a(byte[] bArr) {
        return bArr.length == 0 ? bArr : Base64.decode(bArr, 0);
    }
}
