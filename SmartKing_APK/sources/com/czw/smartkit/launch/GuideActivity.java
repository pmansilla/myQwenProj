package com.czw.smartkit.launch;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.czw.smartkit.R;
import com.czw.smartkit.base.PageGuideActivity;
import com.czw.smartkit.homeModule.MainActivity;
import com.czw.smartkit.preferenceModule.SharePreferenceLogin;
import com.czw.smartkit.preferenceModule.SharePrefreshLaunch;
import com.czw.smartkit.sharedpreferences.domain.LoginInfo;
import com.czw.smartkit.user.LoginAcitivty;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class GuideActivity extends PageGuideActivity {
    ArrayList<View> pages = new ArrayList<>();

    @Override // com.czw.smartkit.base.PageGuideActivity
    protected int getPointCount() {
        return this.pages.size();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public boolean isFullScreen() {
        return true;
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public boolean isNoTitle() {
        return true;
    }

    @Override // com.czw.smartkit.base.PageGuideActivity
    protected ArrayList<View> loadPages() {
        this.pages.clear();
        this.pages.add(LayoutInflater.from(this).inflate(R.layout.activity_guide_1, (ViewGroup) null, false));
        this.pages.add(LayoutInflater.from(this).inflate(R.layout.activity_guide_2, (ViewGroup) null, false));
        this.pages.add(LayoutInflater.from(this).inflate(R.layout.activity_guide_3, (ViewGroup) null, false));
        return this.pages;
    }

    @Override // com.czw.smartkit.base.PageGuideActivity
    public void onLastSelect() {
        new Handler().postDelayed(new Runnable() { // from class: com.czw.smartkit.launch.GuideActivity.1
            @Override // java.lang.Runnable
            public void run() {
                SharePrefreshLaunch.save(false);
                LoginInfo read = SharePreferenceLogin.read();
                if (read == null || TextUtils.isEmpty(read.getValue())) {
                    GuideActivity.this.jumpAndFinsh(LoginAcitivty.class);
                } else {
                    GuideActivity.this.jumpAndFinsh(MainActivity.class);
                }
            }
        }, 500L);
    }
}
