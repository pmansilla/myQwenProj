package com.czw.smartkit.setting;

import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;

/* loaded from: classes.dex */
public class HelpInfoActivity extends TitleActivity {
    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.help_info);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_help_info;
    }
}
