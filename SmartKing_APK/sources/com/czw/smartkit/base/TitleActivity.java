package com.czw.smartkit.base;

import android.view.View;
import com.czw.modes.widget.TitleBar;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
public abstract class TitleActivity extends BasePermissionActivity {
    protected TitleBar titleBar;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity
    public void insertInit() {
        super.insertInit();
        this.titleBar = (TitleBar) $View(R.id.titleBar);
        this.titleBar.setTitleColor(getResources().getColor(R.color.white));
        this.titleBar.setParentBg(R.color.root_bg_color);
        this.titleBar.setLeftImage(R.mipmap.ic_back);
        this.titleBar.setClick(new TitleBar.TitleClick() { // from class: com.czw.smartkit.base.TitleActivity.1
            @Override // com.czw.modes.widget.TitleBar.LeftClick
            public void onLeftClick(View view) {
                TitleActivity.this.finish();
            }

            @Override // com.czw.modes.widget.TitleBar.TitleClick
            public void onRightClick(View view) {
                TitleActivity.this.onTitleRightClick(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    public void onTitleRightClick(View view) {
    }
}
