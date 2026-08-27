package com.mob.commons.b;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import com.mob.commons.b.f;
import java.security.MessageDigest;

/* compiled from: Oppo.java */
/* loaded from: classes.dex */
public class j extends f {
    private String c;

    public j(Context context) {
        super(context);
    }

    private final String a(IBinder iBinder, String str) {
        if (TextUtils.isEmpty(this.b)) {
            this.b = this.a.getPackageName();
        }
        if (TextUtils.isEmpty(this.c)) {
            try {
                Signature[] signatureArr = this.a.getPackageManager().getPackageInfo(this.b, 64).signatures;
                if (signatureArr != null && signatureArr.length > 0) {
                    byte[] byteArray = signatureArr[0].toByteArray();
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                    if (messageDigest != null) {
                        byte[] digest = messageDigest.digest(byteArray);
                        StringBuilder sb = new StringBuilder();
                        for (byte b : digest) {
                            sb.append(Integer.toHexString((b & 255) | 256).substring(1, 3));
                        }
                        this.c = sb.toString();
                    }
                }
            } catch (Throwable unused) {
            }
        }
        return a(str, iBinder, com.mob.commons.k.a(93), 1, this.b, this.c, str);
    }

    @Override // com.mob.commons.b.f
    protected Intent a() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(com.mob.commons.k.a(87), com.mob.commons.k.a(88)));
        intent.setAction(com.mob.commons.k.a(89));
        return intent;
    }

    @Override // com.mob.commons.b.f
    public f.c a(IBinder iBinder) {
        f.c cVar = new f.c();
        cVar.c = a(iBinder, com.mob.commons.k.a(90));
        cVar.b = a(iBinder, com.mob.commons.k.a(91));
        cVar.e = a(iBinder, com.mob.commons.k.a(92));
        return cVar;
    }

    @Override // com.mob.commons.b.f
    public synchronized boolean h() {
        boolean z = false;
        try {
            PackageInfo packageInfo = this.a.getPackageManager().getPackageInfo(com.mob.commons.k.a(87), 0);
            if (Build.VERSION.SDK_INT >= 28) {
                if (packageInfo != null && packageInfo.getLongVersionCode() >= 1) {
                    z = true;
                }
                return z;
            }
            if (packageInfo != null && packageInfo.versionCode >= 1) {
                z = true;
            }
            return z;
        } catch (Throwable unused) {
            return false;
        }
    }
}
