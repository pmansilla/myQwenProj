package cn.sharesdk.wechat.utils;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/* loaded from: classes.dex */
public class WechatHandlerActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        setTheme(R.style.Theme.Translucent);
        super.onCreate(bundle);
        try {
            k.a().a(this);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
        finish();
    }

    public void onGetMessageFromWXReq(WXMediaMessage wXMediaMessage) {
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        try {
            k.a().a(this);
        } catch (Throwable th) {
            cn.sharesdk.framework.utils.e.b().d(th);
        }
        finish();
    }

    public void onShowMessageFromWXReq(WXMediaMessage wXMediaMessage) {
    }
}
