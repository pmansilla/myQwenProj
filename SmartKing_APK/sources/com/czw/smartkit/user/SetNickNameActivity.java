package com.czw.smartkit.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.czw.modes.widget.TitleBar;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;

/* loaded from: classes.dex */
public class SetNickNameActivity extends TitleActivity {
    private EditText user_name;

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.user_nickname);
        String stringExtra = getIntent().getStringExtra("userName");
        this.user_name = (EditText) $View(R.id.user_name);
        EditText editText = this.user_name;
        if (TextUtils.isEmpty(stringExtra)) {
            stringExtra = "";
        }
        editText.setText(stringExtra);
        this.user_name.setSelection(this.user_name.length());
        this.titleBar.setClick(new TitleBar.TitleClick() { // from class: com.czw.smartkit.user.SetNickNameActivity.1
            @Override // com.czw.modes.widget.TitleBar.LeftClick
            public void onLeftClick(View view) {
                SetNickNameActivity.this.setResult(200, new Intent().putExtra("userName", SetNickNameActivity.this.user_name.getText().toString()));
                SetNickNameActivity.this.finish();
            }

            @Override // com.czw.modes.widget.TitleBar.TitleClick
            public void onRightClick(View view) {
            }
        });
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_nickname;
    }
}
