package com.amap.api.maps.offlinemap;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.amap.api.mapcore.util.gq;
import com.amap.api.mapcore.util.gr;
import com.amap.api.mapcore.util.gt;
import com.amap.api.offlineservice.AMapPermissionActivity;
import com.amap.api.offlineservice.a;

/* loaded from: classes.dex */
public class OfflineMapActivity extends AMapPermissionActivity implements View.OnClickListener {
    private static int a;
    private a b;
    private gq c;
    private gq[] d = new gq[32];
    private int e = -1;
    private gr f;

    private void a(gq gqVar) {
        try {
            if (this.b != null) {
                this.b.d();
                this.b = null;
            }
            this.b = c(gqVar);
            if (this.b != null) {
                this.c = gqVar;
                this.b.a(this);
                this.b.a(this.c.b);
                this.b.a();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private boolean a(Bundle bundle) {
        try {
            if ((a != 1 || this.b == null) && a > 1) {
                a--;
                this.e = ((this.e - 1) + 32) % 32;
                gq gqVar = this.d[this.e];
                gqVar.b = bundle;
                a(gqVar);
                return true;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return false;
    }

    private void b(gq gqVar) {
        try {
            a++;
            a(gqVar);
            this.e = (this.e + 1) % 32;
            this.d[this.e] = gqVar;
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private a c(gq gqVar) {
        try {
            if (gqVar.a != 1) {
                return null;
            }
            if (this.f == null) {
                this.f = new gr();
            }
            return this.f;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public void closeScr() {
        try {
            if (a((Bundle) null)) {
                return;
            }
            if (this.b != null) {
                this.b.d();
            }
            finish();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void closeScr(Bundle bundle) {
        try {
            if (a(bundle)) {
                return;
            }
            if (this.b != null) {
                this.b.d();
            }
            finish();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        try {
            if (this.b != null) {
                this.b.a(view);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        try {
            super.onConfigurationChanged(configuration);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        try {
            super.onCreate(bundle);
            getWindow().setSoftInputMode(32);
            getWindow().setFormat(-3);
            requestWindowFeature(1);
            gt.a(getApplicationContext());
            this.e = -1;
            a = 0;
            b(new gq(1, null));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        try {
            super.onDestroy();
            if (this.b != null) {
                this.b.d();
                this.b = null;
            }
            this.c = null;
            this.d = null;
            if (this.f != null) {
                this.f.d();
                this.f = null;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            try {
                if (this.b != null && !this.b.b()) {
                    return true;
                }
                if (a((Bundle) null)) {
                    return false;
                }
                if (keyEvent == null) {
                    if (a == 1) {
                        finish();
                    }
                    return false;
                }
                this.e = -1;
                a = 0;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity
    protected void onPause() {
        try {
            super.onPause();
            if (this.b != null) {
                this.b.h();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.offlineservice.AMapPermissionActivity, android.app.Activity
    public void onResume() {
        try {
            super.onResume();
            if (this.b != null) {
                this.b.f();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // android.app.Activity
    protected void onStart() {
        try {
            super.onStart();
            if (this.b != null) {
                this.b.e();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        try {
            super.onStop();
            if (this.b != null) {
                this.b.g();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void showScr() {
        try {
            setContentView(this.b.c());
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
