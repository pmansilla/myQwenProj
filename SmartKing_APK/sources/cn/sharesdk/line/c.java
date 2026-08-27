package cn.sharesdk.line;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.authorize.d;
import cn.sharesdk.framework.utils.e;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import java.util.HashMap;

/* compiled from: LineSSOProcessor.java */
/* loaded from: classes.dex */
public class c extends d {
    private String d;

    public c(cn.sharesdk.framework.authorize.c cVar) {
        super(cVar);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [cn.sharesdk.line.c$1] */
    private void a(final Platform platform, final String str) {
        new Thread() { // from class: cn.sharesdk.line.c.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                String str2;
                try {
                    try {
                        str2 = b.a(platform).b(str);
                    } catch (Throwable th) {
                        c.this.c.onFailed(th);
                        str2 = null;
                    }
                    if (str2 == null) {
                        c.this.c.onFailed(new Throwable("Authorize token is empty"));
                        return;
                    }
                    HashMap fromJson = new Hashon().fromJson(str2);
                    Bundle bundle = new Bundle();
                    bundle.putString("mid", String.valueOf(fromJson.get("mid")));
                    bundle.putString("access_token", String.valueOf(fromJson.get("access_token")));
                    bundle.putString("refresh_token", String.valueOf(fromJson.get("refresh_token")));
                    bundle.putString("expires_in", String.valueOf(fromJson.get("expires_in")));
                    bundle.putString("token_type", String.valueOf(fromJson.get("token_type")));
                    c.this.c.onComplete(bundle);
                } catch (Throwable th2) {
                    e.b().d(th2);
                    c.this.c.onFailed(th2);
                }
            }
        }.start();
    }

    private void b() {
        Intent intent = new Intent();
        intent.setData(Uri.parse("line." + this.d + ":"));
        if (this.a.getContext().getPackageManager().resolveActivity(intent, 0) != null) {
            cn.sharesdk.framework.authorize.c.registerExecutor("line." + this.d, this.a);
        }
    }

    private boolean b(Intent intent) {
        if (intent == null || intent.getData() == null) {
            return false;
        }
        Uri data = intent.getData();
        if (this.c != null && data != null) {
            Bundle urlToBundle = ResHelper.urlToBundle(data.toString());
            try {
                int parseInt = ResHelper.parseInt(urlToBundle.getString("status"));
                String string = urlToBundle.getString("requestToken");
                switch (parseInt) {
                    case 0:
                        a(this.a.a().getPlatform(), string);
                        break;
                    case 1:
                        this.c.onCancel();
                        break;
                    default:
                        this.c.onFailed(new Throwable(data.toString()));
                        break;
                }
            } catch (Throwable th) {
                this.c.onFailed(th);
                e.b().w(th);
                return true;
            }
        }
        return true;
    }

    @Override // cn.sharesdk.framework.authorize.d
    public void a() {
        if (b(((Activity) this.a.getContext()).getIntent())) {
            return;
        }
        try {
            if (TextUtils.isEmpty(this.d)) {
                return;
            }
            try {
                Intent intent = new Intent("jp.naver.line.android.intent.action.APPAUTH");
                intent.putExtra("channelId", this.d);
                intent.putExtra("otpId", this.d);
                intent.putExtra("appPackage", this.a.getContext().getPackageName());
                intent.putExtra("authScheme", "line." + this.d);
                intent.addFlags(65536);
                this.a.startActivity(intent);
                b();
            } catch (Throwable th) {
                if (this.c != null) {
                    this.c.onFailed(th);
                }
                e.b().w(th);
            }
        } finally {
            this.a.finish();
        }
    }

    public void a(String str) {
        this.d = str;
    }
}
