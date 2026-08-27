package com.mob.guard;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.mob.guard.impl.e;
import com.mob.guard.impl.f;
import com.mob.tools.proguard.ClassKeeper;

/* loaded from: classes.dex */
public class MobTranPullUpActivity extends Activity implements ClassKeeper {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            MobGuard.getSdkTag();
        } catch (Throwable unused) {
        }
        try {
            e.a().d("[MobGuard] MobTranPullUpActivity onCreate", new Object[0]);
            Intent intent = getIntent();
            if (intent == null) {
                return;
            }
            f.a(getApplicationContext(), intent, true);
            finish();
        } catch (Throwable th) {
            e.a().d(th);
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                finish();
            } catch (Throwable th) {
                e.a().d(th);
            }
        }
        super.onResume();
    }
}
