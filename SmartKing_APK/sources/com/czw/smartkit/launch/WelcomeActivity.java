package com.czw.smartkit.launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import com.czw.smartkit.R;
import com.czw.smartkit.homeModule.MainActivity;
import com.czw.smartkit.net.domain.UserDTO;
import com.czw.smartkit.preferenceModule.SharePreferenceUser;
import com.czw.smartkit.preferenceModule.SharePrefreshLaunch;
import com.czw.smartkit.user.LoginAcitivty;
import com.czw.smartkit.util.ActivityManager;

/* loaded from: classes.dex */
public class WelcomeActivity extends Activity {
    private Handler handler = new Handler();

    private void doStep(int i) {
        this.handler.postDelayed(new Runnable() { // from class: com.czw.smartkit.launch.WelcomeActivity.1
            @Override // java.lang.Runnable
            public void run() {
                if (SharePrefreshLaunch.read()) {
                    WelcomeActivity.this.startActivity(new Intent(WelcomeActivity.this, (Class<?>) GuideActivity.class));
                    WelcomeActivity.this.finish();
                    return;
                }
                UserDTO read = SharePreferenceUser.read();
                if (read == null || TextUtils.isEmpty(read.getUserId())) {
                    WelcomeActivity.this.startActivity(new Intent(WelcomeActivity.this, (Class<?>) LoginAcitivty.class));
                    WelcomeActivity.this.finish();
                } else {
                    WelcomeActivity.this.startActivity(new Intent(WelcomeActivity.this, (Class<?>) MainActivity.class));
                    WelcomeActivity.this.finish();
                }
            }
        }, i);
    }

    public void initView() {
        doStep(1500);
    }

    public int loadLayout() {
        return R.layout.ui_launch;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if ((getIntent().getFlags() & 4194304) != 0) {
            finish();
            return;
        }
        ActivityManager.getInstance().putActivity(this);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(loadLayout());
        initView();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
            this.handler = null;
        }
        ActivityManager.getInstance().removeActivity(this);
    }
}
