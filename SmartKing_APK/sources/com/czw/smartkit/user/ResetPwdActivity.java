package com.czw.smartkit.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import com.amap.location.common.model.AmapLoc;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.entity.CountryEntity;
import com.czw.smartkit.netModule.NetManager;
import com.czw.smartkit.preferenceModule.SharePreferenceLogin;
import com.czw.smartkit.preferenceModule.SharePreferenceLoginType;
import com.czw.smartkit.preferenceModule.SharePreferenceUser;
import com.czw.smartkit.util.ActivityManager;
import com.czw.smartkit.util.CounterUtils;
import com.czw.smartkit.util.EditTextUtil;
import ycnet.runchinaup.core.ycimpl.data.YCRespData;
import ycnet.runchinaup.core.ycimpl.response.YCResponseListener;

/* loaded from: classes.dex */
public class ResetPwdActivity extends TitleActivity implements View.OnClickListener {
    private TextView auth_code;
    private String code;
    private TextView codeTv;
    private EditText et_item_1;
    private EditText et_item_2;
    private EditText et_item_3;
    private View phoneLayout;
    private String phoneOrEmail;
    private String pwd;
    private String areaCode = "86";
    int type = 0;
    YCResponseListener<YCRespData> listener = new YCResponseListener<YCRespData>() { // from class: com.czw.smartkit.user.ResetPwdActivity.2
        @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
        public void onError(final int i, final String str) {
            ResetPwdActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.ResetPwdActivity.2.2
                @Override // java.lang.Runnable
                public void run() {
                    if (i == 2) {
                        ResetPwdActivity.this.toast(R.string.had_phone_rgister);
                    } else {
                        ResetPwdActivity.this.toast(str);
                    }
                }
            });
        }

        @Override // ycnet.runchinaup.core.abs.IResponseListener
        public void onSuccess(YCRespData yCRespData) {
            ResetPwdActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.ResetPwdActivity.2.1
                @Override // java.lang.Runnable
                public void run() {
                    ResetPwdActivity.this.toast(R.string.relogin);
                    ActivityManager.getInstance().closeAll();
                    SharePreferenceUser.clearAll();
                    SharePreferenceLoginType.clearAll();
                    SharePreferenceLogin.clearAll();
                    ResetPwdActivity.this.jumpAndFinsh(LoginAcitivty.class);
                }
            });
        }
    };

    private void authPhoneCode() {
        if (vertyRegister()) {
            SMSSDK.registerEventHandler(new EventHandler() { // from class: com.czw.smartkit.user.ResetPwdActivity.3
                @Override // cn.smssdk.EventHandler
                public void afterEvent(int i, final int i2, Object obj) {
                    ResetPwdActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.ResetPwdActivity.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (i2 == -1) {
                                NetManager.getNetManager().resetPwdByPhone(ResetPwdActivity.this.phoneOrEmail, ResetPwdActivity.this.pwd, ResetPwdActivity.this.listener);
                            } else {
                                ResetPwdActivity.this.toast(R.string.sms_code_eror);
                                ResetPwdActivity.this.finish();
                            }
                        }
                    });
                }
            });
            SMSSDK.submitVerificationCode(this.areaCode, this.phoneOrEmail, this.code);
        }
    }

    private void getCode(int i) {
        if (i == 0) {
            getPhoneCode();
        } else {
            getEmailCode();
        }
    }

    private void getEmailCode() {
        if (vertyPhone()) {
            showLoadingDialog("");
            NetManager.getNetManager().getEmailCode(this.phoneOrEmail, AmapLoc.RESULT_TYPE_FUSED, new YCResponseListener<YCRespData>() { // from class: com.czw.smartkit.user.ResetPwdActivity.5
                @Override // ycnet.runchinaup.core.abs.IResponseListener
                public void onSuccess(YCRespData yCRespData) {
                    ResetPwdActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.ResetPwdActivity.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ResetPwdActivity.this.dismissLoadingDialog();
                            new CounterUtils(60, ResetPwdActivity.this.auth_code, ResetPwdActivity.this.getString(R.string.get_auth_code)).startCounter();
                        }
                    });
                }
            });
        }
    }

    private void getPhoneCode() {
        if (vertyPhone()) {
            this.phoneOrEmail = this.et_item_1.getText().toString().trim();
            if (TextUtils.isEmpty(this.phoneOrEmail)) {
                return;
            }
            showLoadingDialog("");
            SMSSDK.registerEventHandler(new EventHandler() { // from class: com.czw.smartkit.user.ResetPwdActivity.4
                @Override // cn.smssdk.EventHandler
                public void afterEvent(int i, final int i2, Object obj) {
                    ResetPwdActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.ResetPwdActivity.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ResetPwdActivity.this.dismissLoadingDialog();
                            if (i2 == -1) {
                                new CounterUtils(60, ResetPwdActivity.this.auth_code, ResetPwdActivity.this.getString(R.string.get_auth_code)).startCounter();
                            } else {
                                ResetPwdActivity.this.toast(R.string.get_phone_code_error);
                            }
                        }
                    });
                }
            });
            SMSSDK.getVerificationCode(this.areaCode, this.phoneOrEmail);
        }
    }

    private void initShow() {
        if (this.type == 0) {
            this.et_item_1.setText("13631697178");
        } else {
            this.et_item_1.setText("635669470@qq.com");
        }
        this.et_item_3.setText("qqqqqq");
    }

    private void register(int i) {
        if (vertyRegister()) {
            if (i == 0) {
                authPhoneCode();
            } else {
                NetManager.getNetManager().resetPwdByEemail(this.phoneOrEmail, this.code, this.pwd, this.listener);
            }
        }
    }

    private boolean vertyPhone() {
        this.phoneOrEmail = this.et_item_1.getText().toString().trim();
        if (!TextUtils.isEmpty(this.phoneOrEmail)) {
            return true;
        }
        if (this.type == 0) {
            toast(R.string.not_phpne);
            return false;
        }
        toast(R.string.not_email);
        return false;
    }

    private boolean vertyRegister() {
        this.phoneOrEmail = this.et_item_1.getText().toString().trim();
        if (TextUtils.isEmpty(this.phoneOrEmail)) {
            if (this.type == 0) {
                toast(R.string.not_phpne);
            } else {
                toast(R.string.not_email);
            }
            return false;
        }
        this.code = this.et_item_2.getText().toString().trim();
        if (TextUtils.isEmpty(this.code)) {
            toast(R.string.not_code);
            return false;
        }
        this.pwd = this.et_item_3.getText().toString().trim();
        if (TextUtils.isEmpty(this.pwd)) {
            toast(R.string.not_pwd);
            return false;
        }
        if (this.pwd.length() >= 6) {
            return true;
        }
        toast(R.string.not_pwd_len);
        return false;
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.type = getIntent().getIntExtra("regType", 0);
        this.codeTv = (TextView) $View(R.id.codeTv);
        this.phoneLayout = $View(R.id.phoneLayout);
        if (this.type == 0) {
            this.codeTv.setVisibility(0);
            this.phoneLayout.setVisibility(0);
        } else {
            this.codeTv.setVisibility(8);
            this.phoneLayout.setVisibility(8);
        }
        this.titleBar.setTitle(R.string.reset_pwd_title);
        this.et_item_1 = (EditText) $View(R.id.hint_reg_type);
        this.et_item_1.setSelection(this.et_item_1.length());
        EditTextUtil.setFilter(this.et_item_1);
        this.et_item_2 = (EditText) $View(R.id.hint_reg_code);
        this.et_item_2.setSelection(this.et_item_2.length());
        EditTextUtil.setFilter(this.et_item_2);
        this.et_item_3 = (EditText) $View(R.id.et_pwd);
        this.et_item_3.setSelection(this.et_item_3.length());
        EditTextUtil.setFilter(this.et_item_3);
        this.auth_code = (TextView) $View(R.id.auth_code);
        this.auth_code.setOnClickListener(this);
        $View(R.id.sureBtn).setOnClickListener(this);
        this.codeTv.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.user.ResetPwdActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ResetPwdActivity.this.jumpFor(CountryCodeactivity.class, 100);
            }
        });
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_reset_pwd;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            CountryEntity countryEntity = (CountryEntity) intent.getSerializableExtra("code");
            this.areaCode = countryEntity.code;
            this.codeTv.setText(String.format("%s (+%s)", countryEntity.name, this.areaCode));
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.auth_code) {
            getCode(this.type);
        } else {
            if (id != R.id.sureBtn) {
                return;
            }
            register(this.type);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
