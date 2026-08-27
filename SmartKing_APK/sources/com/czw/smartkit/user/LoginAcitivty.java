package com.czw.smartkit.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import cn.droidlover.xrichtext.XRichText;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import com.czw.modes.pop.BottomFloatPop;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.base.WebActivity;
import com.czw.smartkit.entity.AgreeEntity;
import com.czw.smartkit.entity.CountryEntity;
import com.czw.smartkit.homeModule.MainActivity;
import com.czw.smartkit.mob.thridlogin.LoginHelper;
import com.czw.smartkit.mob.thridlogin.OnLoginListener;
import com.czw.smartkit.mob.thridlogin.ThridUserInfo;
import com.czw.smartkit.net.domain.UserDTO;
import com.czw.smartkit.netModule.NetCfg;
import com.czw.smartkit.netModule.NetManager;
import com.czw.smartkit.preferenceModule.SharePreferenceAgreeUse;
import com.czw.smartkit.preferenceModule.SharePreferenceCountry;
import com.czw.smartkit.preferenceModule.SharePreferenceLogin;
import com.czw.smartkit.preferenceModule.SharePreferenceLoginType;
import com.czw.smartkit.preferenceModule.SharePreferenceUser;
import com.czw.smartkit.sharedpreferences.domain.LoginInfo;
import com.czw.smartkit.sharedpreferences.domain.LoginType;
import com.czw.smartkit.util.EditTextUtil;
import com.czw.smartkit.util.YCAppUtils;
import com.czw.utils.LogUtil;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import java.util.HashMap;
import java.util.List;
import npPermission.nopointer.core.NpPermissionRequester;
import npPermission.nopointer.core.RequestPermissionInfo;
import ycnet.runchinaup.core.ycimpl.data.YCRespData;
import ycnet.runchinaup.core.ycimpl.data.YCRespListData;
import ycnet.runchinaup.core.ycimpl.response.YCResponseListener;

/* loaded from: classes.dex */
public class LoginAcitivty extends TitleActivity implements View.OnClickListener {

    @BindView(R.id.messageText)
    XRichText bottomTextTv;

    @BindView(R.id.checkbox_agreen)
    CheckBox checkBoxAgreen;
    private TextView codeTv;
    private CountryEntity countryEntity;
    private TextView loginType;
    private Button login_btn;
    private EditText login_name;
    private EditText login_value;
    private RelativeLayout phoneLayout;
    private boolean loginEmailType = false;
    private String areaCode = "86";
    String loginName = null;
    String loginValue = null;

    private void fastRegister() {
        showLoadingDialog("");
        NetManager.getNetManager().fastRegister(new YCResponseListener<YCRespData<UserDTO>>() { // from class: com.czw.smartkit.user.LoginAcitivty.6
            @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
            public void onError(int i, final String str) {
                super.onError(i, str);
                LoginAcitivty.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.LoginAcitivty.6.2
                    @Override // java.lang.Runnable
                    public void run() {
                        LoginAcitivty.this.dismissLoadingDialog();
                        LoginAcitivty.this.toast(str);
                    }
                });
            }

            @Override // ycnet.runchinaup.core.abs.IResponseListener
            public void onSuccess(final YCRespData<UserDTO> yCRespData) {
                LoginAcitivty.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.LoginAcitivty.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LoginAcitivty.this.dismissLoadingDialog();
                        LoginAcitivty.this.loginSuccess();
                        SharePreferenceLogin.clearAll();
                        SharePreferenceUser.save((UserDTO) yCRespData.getData());
                        SharePreferenceLoginType.save(LoginType.Visitor);
                        LoginAcitivty.this.jumpAndFish(MainActivity.class);
                    }
                });
            }
        });
    }

    private void login() {
        if (verty(true)) {
            if (this.loginEmailType) {
                SharePreferenceCountry.clearAll();
            } else {
                SharePreferenceCountry.save(this.countryEntity);
            }
            showLoadingDialog("");
            NetManager.getNetManager().login(this.loginName, this.loginValue, this.areaCode, new YCResponseListener<YCRespListData<UserDTO>>() { // from class: com.czw.smartkit.user.LoginAcitivty.2
                @Override // ycnet.runchinaup.core.ycimpl.response.YCResponseListener, ycnet.runchinaup.core.abs.IResponseListener
                public void onError(final int i, final String str) {
                    LoginAcitivty.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.user.LoginAcitivty.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            LoginAcitivty.this.dismissLoadingDialog();
                            if (i == 3) {
                                LoginAcitivty.this.toast(R.string.account_or_pwd_error);
                            } else {
                                LoginAcitivty.this.toast(str);
                            }
                        }
                    });
                }

                @Override // ycnet.runchinaup.core.abs.IResponseListener
                public void onSuccess(YCRespListData<UserDTO> yCRespListData) {
                    LoginAcitivty.this.dismissLoadingDialog();
                    LoginAcitivty.this.loginSuccess();
                    SharePreferenceLogin.save(new LoginInfo(LoginAcitivty.this.loginName, LoginAcitivty.this.loginValue));
                    SharePreferenceUser.save(yCRespListData.getData().get(0));
                    SharePreferenceLoginType.save(LoginType.User);
                    LoginAcitivty.this.jumpAndFinsh(MainActivity.class);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginSuccess() {
        AgreeEntity agreeEntity = new AgreeEntity();
        agreeEntity.setAgree(true);
        SharePreferenceAgreeUse.save(agreeEntity);
    }

    private void showFindPwdType() {
        BottomFloatPop.getPop(getUI()).showPicker($View(R.id.acv_win), new BottomFloatPop.ClickCallback() { // from class: com.czw.smartkit.user.LoginAcitivty.4
            @Override // com.czw.modes.pop.BottomFloatPop.ClickCallback
            public void onClick(int i) {
                LoginAcitivty.this.startActivity(new Intent(LoginAcitivty.this.getUI(), (Class<?>) ResetPwdActivity.class).putExtra("regType", i));
            }
        }).showTxt(R.string.reset_phone, R.string.reset_email);
    }

    private void showRegisterType() {
        if (YCAppUtils.isChainess()) {
            BottomFloatPop.getPop(getUI()).showPicker($View(R.id.acv_win), new BottomFloatPop.ClickCallback() { // from class: com.czw.smartkit.user.LoginAcitivty.3
                @Override // com.czw.modes.pop.BottomFloatPop.ClickCallback
                public void onClick(int i) {
                    BottomFloatPop.getPop(LoginAcitivty.this.getUI()).dismiss();
                    LoginAcitivty.this.startActivity(new Intent(LoginAcitivty.this.getUI(), (Class<?>) RegisterActivity.class).putExtra("regType", i));
                }
            }).showTxt(R.string.reg_phone, R.string.reg_email);
        } else {
            startActivity(new Intent(getUI(), (Class<?>) RegisterActivity.class).putExtra("regType", 1));
        }
    }

    private void thridLogin(String str) {
        LogUtil.e("debug==>platfrom====>" + str);
        new LoginHelper().login(this, str).setOnLoginListener(new OnLoginListener() { // from class: com.czw.smartkit.user.LoginAcitivty.5
            @Override // com.czw.smartkit.mob.thridlogin.OnLoginListener
            public boolean onLogin(String str2, HashMap<String, Object> hashMap) {
                LogUtil.e("debug onLogin" + hashMap.toString());
                return false;
            }

            @Override // com.czw.smartkit.mob.thridlogin.OnLoginListener
            public boolean onRegister(ThridUserInfo thridUserInfo) {
                LogUtil.e("debug onRegister" + thridUserInfo.toString());
                return false;
            }
        });
    }

    private boolean verty(boolean z) {
        if (z) {
            this.loginName = this.login_name.getText().toString().trim();
            if (TextUtils.isEmpty(this.loginName)) {
                toast(R.string.not_login_name);
                return false;
            }
            this.loginValue = this.login_value.getText().toString().trim();
            if (TextUtils.isEmpty(this.loginValue)) {
                toast(R.string.not_login_value);
                return false;
            }
        }
        if (this.checkBoxAgreen.isChecked()) {
            return true;
        }
        toast(R.string.pls_agreen);
        return false;
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.countryEntity = SharePreferenceCountry.read();
        this.titleBar.disableLeftImage();
        this.titleBar.setTitle(R.string.app_name_sk);
        this.codeTv = (TextView) $View(R.id.codeTv);
        this.phoneLayout = (RelativeLayout) $View(R.id.phoneLayout);
        this.login_name = (EditText) $View(R.id.login_name);
        EditTextUtil.setFilter(this.login_name);
        this.login_value = (EditText) $View(R.id.login_value);
        EditTextUtil.setFilter(this.login_value);
        this.login_btn = (Button) $View(R.id.login_btn);
        this.login_btn.setOnClickListener(this);
        $View(R.id.login_qq).setOnClickListener(this);
        $View(R.id.login_weixin).setOnClickListener(this);
        $View(R.id.login_sina).setOnClickListener(this);
        $View(R.id.to_register).setOnClickListener(this);
        $View(R.id.to_findPwd).setOnClickListener(this);
        $View(R.id.skinLogin).setOnClickListener(this);
        $View(R.id.codeTv).setOnClickListener(this);
        this.loginType = (TextView) $View(R.id.loginType);
        this.loginType.setOnClickListener(this);
        if (this.countryEntity != null) {
            this.loginType.setText(R.string.login_email);
            this.phoneLayout.setVisibility(0);
            this.codeTv.setText(String.format("%s (+%s)", this.countryEntity.name, this.countryEntity.code));
            this.login_name.setHint(R.string.phone);
        }
        this.bottomTextTv.callback(new XRichText.Callback() { // from class: com.czw.smartkit.user.LoginAcitivty.1
            @Override // cn.droidlover.xrichtext.XRichText.Callback
            public void onFix(XRichText.ImageHolder imageHolder) {
                if (imageHolder.getPosition() % 3 == 0) {
                    imageHolder.setStyle(XRichText.Style.LEFT);
                } else if (imageHolder.getPosition() % 3 == 1) {
                    imageHolder.setStyle(XRichText.Style.CENTER);
                } else {
                    imageHolder.setStyle(XRichText.Style.RIGHT);
                }
            }

            @Override // cn.droidlover.xrichtext.XRichText.Callback
            public void onImageClick(List<String> list, int i) {
            }

            @Override // cn.droidlover.xrichtext.XRichText.Callback
            public boolean onLinkClick(String str) {
                char c;
                Intent intent = new Intent(LoginAcitivty.this, (Class<?>) WebActivity.class);
                int hashCode = str.hashCode();
                if (hashCode != -538326102) {
                    if (hashCode == 1011196905 && str.equals(NetCfg.URL2)) {
                        c = 1;
                    }
                    c = 65535;
                } else {
                    if (str.equals(NetCfg.URL1)) {
                        c = 0;
                    }
                    c = 65535;
                }
                switch (c) {
                    case 0:
                        intent.putExtra("title", LoginAcitivty.this.getResources().getString(R.string.use_protocol_auth_message0));
                        break;
                    case 1:
                        intent.putExtra("title", LoginAcitivty.this.getResources().getString(R.string.use_protocol_auth_message1));
                        break;
                }
                intent.putExtra(FileDownloadModel.URL, str);
                LoginAcitivty.this.startActivity(intent);
                return true;
            }
        }).text(getResources().getString(R.string.i_have_read_and_agreed) + "<a href='http://sk.runchinaup.com/index.php/home/common/serviceAgree.html'>" + getResources().getString(R.string.use_protocol_auth_message0) + "</a>" + getResources().getString(R.string.and) + "<a href='http://sk.runchinaup.com/index.php/home/common/privacy.html'>" + getResources().getString(R.string.use_protocol_auth_message1) + "</a>");
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.activity_login_layout;
    }

    protected RequestPermissionInfo loadPermissionsConfig() {
        RequestPermissionInfo requestPermissionInfo = new RequestPermissionInfo();
        requestPermissionInfo.setPermissionTitle(getResources().getString(R.string.need_permission));
        requestPermissionInfo.setPermissionMessage(getResources().getString(R.string.permission_fast_login));
        requestPermissionInfo.setPermissionCancelText(getResources().getString(R.string.cancel));
        requestPermissionInfo.setPermissionSureText(getResources().getString(R.string.sure));
        requestPermissionInfo.setAgainPermissionTitle(getResources().getString(R.string.need_permission));
        requestPermissionInfo.setAgainPermissionMessage(getResources().getString(R.string.permission_fast_login));
        requestPermissionInfo.setAgainPermissionCancelText(getResources().getString(R.string.cancel));
        requestPermissionInfo.setAgainPermissionSureText(getResources().getString(R.string.sure));
        requestPermissionInfo.setPermissionArr(new String[]{"android.permission.READ_PHONE_STATE", "android.permission.WRITE_EXTERNAL_STORAGE"});
        return requestPermissionInfo;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            this.countryEntity = (CountryEntity) intent.getSerializableExtra("code");
            this.areaCode = this.countryEntity.code;
            this.codeTv.setText(String.format("%s (+%s)", this.countryEntity.name, this.areaCode));
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.codeTv /* 2131296379 */:
                jumpFor(CountryCodeactivity.class, 100);
                return;
            case R.id.loginType /* 2131296596 */:
                this.loginEmailType = !this.loginEmailType;
                if (this.loginEmailType) {
                    this.loginType.setText(R.string.login_phone);
                    this.login_name.setHint(R.string.email);
                    this.phoneLayout.setVisibility(8);
                    return;
                } else {
                    this.loginType.setText(R.string.login_email);
                    this.login_name.setHint(R.string.phone);
                    this.phoneLayout.setVisibility(0);
                    return;
                }
            case R.id.login_btn /* 2131296597 */:
                login();
                return;
            case R.id.login_qq /* 2131296599 */:
                thridLogin(QQ.NAME);
                return;
            case R.id.login_sina /* 2131296600 */:
                thridLogin(SinaWeibo.NAME);
                return;
            case R.id.login_weixin /* 2131296602 */:
                thridLogin(Wechat.NAME);
                return;
            case R.id.skinLogin /* 2131296785 */:
                if (verty(false)) {
                    if (NpPermissionRequester.hasPermissions(this, "android.permission.READ_PHONE_STATE", "android.permission.WRITE_EXTERNAL_STORAGE")) {
                        fastRegister();
                        return;
                    } else {
                        requestPermission(loadPermissionsConfig());
                        return;
                    }
                }
                return;
            case R.id.to_findPwd /* 2131296900 */:
                showFindPwdType();
                return;
            case R.id.to_register /* 2131296901 */:
                showRegisterType();
                return;
            default:
                return;
        }
    }

    @Override // com.czw.smartkit.base.BasePermissionActivity, npPermission.nopointer.core.callback.PermissionCallback
    public void onGetAllPermission() {
        super.onGetAllPermission();
        LogUtil.e("获取了所有的权限");
        fastRegister();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.TitleActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        LoginInfo read = SharePreferenceLogin.read();
        if (read != null) {
            if (!TextUtils.isEmpty(read.getName())) {
                this.login_name.setText(read.getName());
                this.login_name.setSelection(this.login_name.length());
            }
            if (TextUtils.isEmpty(read.getValue())) {
                return;
            }
            this.login_value.setText(read.getValue());
            this.login_value.setSelection(this.login_value.length());
        }
    }
}
