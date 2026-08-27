package cn.sharesdk.tencent.qzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.amap.location.common.model.AmapLoc;
import org.json.JSONObject;

/* compiled from: QZoneSSOProcessor.java */
/* loaded from: classes.dex */
public class c extends cn.sharesdk.framework.authorize.d {
    private String d;
    private String e;

    public c(cn.sharesdk.framework.authorize.c cVar) {
        super(cVar);
    }

    @Override // cn.sharesdk.framework.authorize.d
    public void a() {
        try {
            if (this.a.getContext().getPackageManager().getPackageInfo("com.tencent.mobileqq", 0) == null) {
                this.a.finish();
                if (this.c != null) {
                    this.c.onFailed(new TencentSSOClientNotInstalledException());
                    return;
                }
                return;
            }
            Intent intent = new Intent();
            intent.setClassName("com.tencent.mobileqq", "com.tencent.open.agent.AgentActivity");
            if (this.a.getContext().getPackageManager().resolveActivity(intent, 0) == null) {
                this.a.finish();
                if (this.c != null) {
                    this.c.onFailed(new TencentSSOClientNotInstalledException());
                    return;
                }
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("scope", this.e);
            bundle.putString("client_id", this.d);
            bundle.putString("pf", "openmobile_android");
            bundle.putString("need_pay", AmapLoc.RESULT_TYPE_WIFI_ONLY);
            intent.putExtra("key_params", bundle);
            intent.putExtra("key_request_code", this.b);
            intent.putExtra("key_action", "action_login");
            try {
                this.a.startActivityForResult(intent, this.b);
            } catch (Throwable th) {
                this.a.finish();
                if (this.c != null) {
                    this.c.onFailed(th);
                }
            }
        } catch (Throwable unused) {
            this.a.finish();
            if (this.c != null) {
                this.c.onFailed(new TencentSSOClientNotInstalledException());
            }
        }
    }

    @Override // cn.sharesdk.framework.authorize.d
    public void a(int i, int i2, Intent intent) {
        this.a.finish();
        if (i2 == 0) {
            if (this.c != null) {
                this.c.onCancel();
                return;
            }
            return;
        }
        if (intent == null) {
            if (this.c != null) {
                this.c.onFailed(new Throwable("response is empty"));
                return;
            }
            return;
        }
        Bundle extras = intent.getExtras();
        if (extras == null) {
            if (this.c != null) {
                this.c.onFailed(new Throwable("response is empty"));
                return;
            }
            return;
        }
        if (!extras.containsKey("key_response")) {
            if (this.c != null) {
                this.c.onFailed(new Throwable("response is empty"));
                return;
            }
            return;
        }
        String string = extras.getString("key_response");
        if (string == null || string.length() <= 0) {
            if (this.c != null) {
                this.c.onFailed(new Throwable("response is empty"));
                return;
            }
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(string);
            Bundle bundle = new Bundle();
            bundle.putInt("ret", jSONObject.optInt("ret"));
            bundle.putString("pay_token", jSONObject.optString("pay_token"));
            bundle.putString("pf", jSONObject.optString("pf"));
            bundle.putString("open_id", jSONObject.optString("openid"));
            bundle.putString("expires_in", jSONObject.optString("expires_in"));
            bundle.putString("pfkey", jSONObject.optString("pfkey"));
            bundle.putString(NotificationCompat.CATEGORY_MESSAGE, jSONObject.optString(NotificationCompat.CATEGORY_MESSAGE));
            bundle.putString("access_token", jSONObject.optString("access_token"));
            if (this.c != null) {
                this.c.onComplete(bundle);
            }
        } catch (Throwable th) {
            if (this.c != null) {
                this.c.onFailed(th);
            }
        }
    }

    public void a(String str, String str2) {
        this.d = str;
        this.e = str2;
    }
}
