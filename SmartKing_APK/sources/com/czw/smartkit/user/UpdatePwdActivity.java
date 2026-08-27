package com.czw.smartkit.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import com.amap.location.common.model.AmapLoc;
import com.czw.net.NetRespListener;
import com.czw.net.NetResult;
import com.czw.net.ProDialog;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.net.domain.UserDTO;
import com.czw.smartkit.preferenceModule.SharePreferenceLogin;
import com.czw.smartkit.preferenceModule.SharePreferenceUser;
import com.czw.smartkit.sharedpreferences.domain.LoginInfo;
import com.czw.smartkit.util.ActivityManager;
import com.czw.smartkit.util.CounterUtils;
import com.czw.smartkit.util.EditTextUtil;

/* loaded from: classes.dex */
public class UpdatePwdActivity extends TitleActivity {
    private EditText code_et;
    private Button ref_auth_code;
    private Button sureBtn;
    private TextView user_account;
    private EditText user_pwd;
    UserDTO userDTO = null;
    private LoginInfo loginInfo = null;
    private NetRespListener listener = new NetRespListener<NetResult>() { // from class: com.czw.smartkit.user.UpdatePwdActivity.4
        @Override // com.czw.net.NetRespListener
        public void onResponse(final NetResult netResult) {
            UpdatePwdActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.UpdatePwdActivity.4.1
                @Override // java.lang.Runnable
                public void run() {
                    if (netResult.getErrorCode() == 2) {
                        UpdatePwdActivity.this.toast(R.string.had_phone_rgister);
                    } else {
                        if (netResult.getErrorCode() != 0) {
                            UpdatePwdActivity.this.toast(netResult.getMsg());
                            return;
                        }
                        UpdatePwdActivity.this.toast(R.string.relogin);
                        ActivityManager.getInstance().closeAll();
                        UpdatePwdActivity.this.jump(LoginAcitivty.class);
                    }
                }
            });
        }
    };

    private void authPhoneCode() {
        SMSSDK.registerEventHandler(new EventHandler() { // from class: com.czw.smartkit.user.UpdatePwdActivity.3
            @Override // cn.smssdk.EventHandler
            public void afterEvent(int i, final int i2, Object obj) {
                UpdatePwdActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.UpdatePwdActivity.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (i2 == -1) {
                            UpdatePwdActivity.this.getNet().resetPwdByPhone(UpdatePwdActivity.this.loginInfo.getName(), UpdatePwdActivity.this.user_pwd.getText().toString().trim(), UpdatePwdActivity.this.listener);
                        } else {
                            UpdatePwdActivity.this.toast(R.string.sms_code_eror);
                        }
                    }
                });
            }
        });
        SMSSDK.submitVerificationCode("86", this.loginInfo.getName(), this.code_et.getText().toString().trim());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCode() {
        if (this.loginInfo.getName().contains("@")) {
            getEmailCode();
        } else {
            getPhoneCode();
        }
    }

    private void getEmailCode() {
        getNet().getEmailCode(this.loginInfo.getName(), AmapLoc.RESULT_TYPE_FUSED, new NetRespListener<NetResult>() { // from class: com.czw.smartkit.user.UpdatePwdActivity.6
            @Override // com.czw.net.NetRespListener
            public void onResponse(NetResult netResult) {
                UpdatePwdActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.UpdatePwdActivity.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        new CounterUtils(60, UpdatePwdActivity.this.ref_auth_code, UpdatePwdActivity.this.getString(R.string.get_auth_code)).startCounter();
                    }
                });
            }
        });
    }

    private void getPhoneCode() {
        final ProDialog proDialog = new ProDialog(this);
        proDialog.show();
        SMSSDK.registerEventHandler(new EventHandler() { // from class: com.czw.smartkit.user.UpdatePwdActivity.5
            @Override // cn.smssdk.EventHandler
            public void afterEvent(int i, final int i2, Object obj) {
                UpdatePwdActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.UpdatePwdActivity.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        proDialog.cancel();
                        if (i2 == -1) {
                            new CounterUtils(60, UpdatePwdActivity.this.ref_auth_code, UpdatePwdActivity.this.getString(R.string.get_auth_code)).startCounter();
                        } else {
                            UpdatePwdActivity.this.toast(R.string.get_phone_code_error);
                        }
                    }
                });
            }
        });
        SMSSDK.getVerificationCode("86", this.loginInfo.getName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePwd() {
        if (this.loginInfo.getName().contains("@")) {
            getNet().resetPwdByEemail(this.loginInfo.getName(), this.code_et.getText().toString().trim(), this.user_pwd.getText().toString().trim(), this.listener);
        } else {
            authPhoneCode();
        }
    }

    void initEvemt() {
        this.ref_auth_code.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.user.UpdatePwdActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UpdatePwdActivity.this.getCode();
            }
        });
        this.sureBtn.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.user.UpdatePwdActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UpdatePwdActivity.this.updatePwd();
            }
        });
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.update_pwd_title);
        this.user_account = (TextView) $View(R.id.user_account);
        this.user_pwd = (EditText) $View(R.id.user_pwd);
        EditTextUtil.setFilter(this.user_pwd);
        this.code_et = (EditText) $View(R.id.code_et);
        EditTextUtil.setFilter(this.code_et);
        this.ref_auth_code = (Button) $View(R.id.ref_auth_code);
        this.sureBtn = (Button) $View(R.id.sureBtn);
        this.userDTO = SharePreferenceUser.read();
        this.loginInfo = SharePreferenceLogin.read();
        this.user_account.setText(this.loginInfo.getName());
        initEvemt();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_update_pwd;
    }
}
