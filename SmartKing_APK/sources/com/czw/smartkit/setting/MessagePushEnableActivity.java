package com.czw.smartkit.setting;

import android.view.View;
import android.widget.TextView;
import com.czw.smartkit.MainApplication;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.DataStruct;
import com.czw.smartkit.bleModule.data.RemindSetting;
import com.czw.smartkit.preferenceModule.SharePreferenceRemind;
import com.czw.utils.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.suke.widget.SwitchButton;
import npPermission.nopointer.core.NpPermissionRequester;
import npPermission.nopointer.core.RequestPermissionInfo;
import npPermission.nopointer.core.callback.PermissionDialogCallback;
import npPermission.nopointer.utils.PermissionPageUtils;
import ycble.runchinaup.aider.PushAiderHelper;

/* loaded from: classes.dex */
public class MessagePushEnableActivity extends TitleActivity implements SwitchButton.OnCheckedChangeListener {
    private static final int TYPE_CALL = 2;
    private static final int TYPE_MESSAGE = 1;
    SwitchButton enable_call;
    SwitchButton enable_facebook;
    SwitchButton enable_kakao;
    SwitchButton enable_line;
    SwitchButton enable_linked;
    SwitchButton enable_message;
    SwitchButton enable_qq;
    SwitchButton enable_skype;
    SwitchButton enable_twitter;
    SwitchButton enable_viber;
    SwitchButton enable_weixin;
    SwitchButton enable_whatsapp;
    TextView permissionState;
    private PushAiderHelper aiderHelper = null;
    private RemindSetting remindSet = null;
    private int perRequestType = 0;
    private boolean jumpToPermissionSetting = false;
    private QMUIDialog qmuiDialog = null;

    private boolean hasCallPermission() {
        return NpPermissionRequester.hasPermissions(this, "android.permission.READ_CONTACTS", "android.permission.READ_PHONE_STATE", "android.permission.READ_CALL_LOG");
    }

    private boolean hasMessagePermission() {
        return NpPermissionRequester.hasPermissions(this, "android.permission.READ_SMS", "android.permission.RECEIVE_SMS");
    }

    private void initEvent() {
        this.permissionState = (TextView) $View(R.id.permissionTv);
        this.enable_call = (SwitchButton) $View(R.id.enable_call);
        this.enable_call.setOnCheckedChangeListener(this);
        this.enable_call.setChecked(this.remindSet.callEnable);
        this.enable_message = (SwitchButton) $View(R.id.enable_message);
        this.enable_message.setOnCheckedChangeListener(this);
        this.enable_message.setChecked(this.remindSet.messageEnable);
        this.enable_qq = (SwitchButton) $View(R.id.enable_qq);
        this.enable_qq.setOnCheckedChangeListener(this);
        this.enable_qq.setChecked(this.remindSet.qqEnable);
        this.enable_weixin = (SwitchButton) $View(R.id.enable_weixin);
        this.enable_weixin.setOnCheckedChangeListener(this);
        this.enable_weixin.setChecked(this.remindSet.wechatEnable);
        this.enable_facebook = (SwitchButton) $View(R.id.enable_facebook);
        this.enable_facebook.setOnCheckedChangeListener(this);
        this.enable_facebook.setChecked(this.remindSet.facebookEnable);
        this.enable_twitter = (SwitchButton) $View(R.id.enable_twitter);
        this.enable_twitter.setOnCheckedChangeListener(this);
        this.enable_twitter.setChecked(this.remindSet.twitterEnable);
        this.enable_whatsapp = (SwitchButton) $View(R.id.enable_whatsapp);
        this.enable_whatsapp.setOnCheckedChangeListener(this);
        this.enable_whatsapp.setChecked(this.remindSet.whatappEnable);
        this.enable_linked = (SwitchButton) $View(R.id.enable_linked);
        this.enable_linked.setOnCheckedChangeListener(this);
        this.enable_linked.setChecked(this.remindSet.linkedEnable);
        this.enable_skype = (SwitchButton) $View(R.id.enable_skype);
        this.enable_skype.setOnCheckedChangeListener(this);
        this.enable_skype.setChecked(this.remindSet.skypeEnable);
        this.enable_line = (SwitchButton) $View(R.id.enable_line);
        this.enable_line.setOnCheckedChangeListener(this);
        this.enable_line.setChecked(this.remindSet.lineEnable);
        this.enable_kakao = (SwitchButton) $View(R.id.enable_kakao);
        this.enable_kakao.setOnCheckedChangeListener(this);
        this.enable_kakao.setChecked(this.remindSet.kakaoEnable);
        this.enable_viber = (SwitchButton) $View(R.id.enable_viber);
        this.enable_viber.setOnCheckedChangeListener(this);
        this.enable_viber.setChecked(this.remindSet.viberEnable);
        $View(R.id.permissionLayout).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.setting.MessagePushEnableActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MessagePushEnableActivity.this.aiderHelper.goToSettingNotificationAccess(MainApplication.getContext());
            }
        });
        $View(R.id.aiderFunctionTv).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.setting.MessagePushEnableActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MessagePushEnableActivity.this.aiderHelper.goToSettingAccessibility(MainApplication.getContext());
            }
        });
        $View(R.id.appPermissionLayout).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.setting.MessagePushEnableActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PermissionPageUtils.jumpPermissionPage(MessagePushEnableActivity.this);
            }
        });
        this.perRequestType = 0;
        if (this.remindSet.callEnable && !hasCallPermission()) {
            this.perRequestType |= 2;
        }
        if (this.remindSet.messageEnable && !hasMessagePermission()) {
            this.perRequestType |= 1;
        }
        if (this.perRequestType != 0) {
            requestPermission();
        }
    }

    private void packData() {
        SharePreferenceRemind.save(this.remindSet);
        BleManager.getBleManager().writeData(DataStruct.createRemindEnable(this.remindSet));
    }

    private void requestPermission() {
        RequestPermissionInfo requestPermissionInfo = new RequestPermissionInfo();
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.permission_pre_text));
        if ((this.perRequestType & 1) == 1) {
            sb.append(getResources().getString(R.string.permission_message_text));
        }
        if ((this.perRequestType & 2) == 2) {
            if ((this.perRequestType & 1) == 1) {
                sb.append("、");
            }
            sb.append(getResources().getString(R.string.permission_call_text));
        }
        requestPermissionInfo.setPermissionTitle("");
        requestPermissionInfo.setPermissionMessage(sb.toString());
        requestPermissionInfo.setPermissionCancelText($str(android.R.string.cancel));
        requestPermissionInfo.setPermissionSureText($str(android.R.string.ok));
        requestPermissionInfo.setAgainPermissionTitle("");
        requestPermissionInfo.setAgainPermissionMessage(sb.toString());
        requestPermissionInfo.setAgainPermissionCancelText($str(android.R.string.cancel));
        requestPermissionInfo.setAgainPermissionSureText($str(android.R.string.ok));
        if (this.perRequestType == 3) {
            requestPermissionInfo.setPermissionArr(new String[]{"android.permission.READ_PHONE_STATE", "android.permission.READ_CONTACTS", "android.permission.READ_CALL_LOG", "android.permission.RECEIVE_SMS", "android.permission.READ_SMS"});
        } else if (this.perRequestType == 2) {
            requestPermissionInfo.setPermissionArr(new String[]{"android.permission.READ_PHONE_STATE", "android.permission.READ_CONTACTS", "android.permission.READ_CALL_LOG"});
        } else if (this.perRequestType == 1) {
            requestPermissionInfo.setPermissionArr(new String[]{"android.permission.RECEIVE_SMS", "android.permission.READ_SMS"});
        }
        requestPermissionInfo.setPermissionDialogCallback(new PermissionDialogCallback() { // from class: com.czw.smartkit.setting.MessagePushEnableActivity.6
            @Override // npPermission.nopointer.core.callback.PermissionDialogCallback
            public void onCancel(boolean z) {
                if (MessagePushEnableActivity.this.perRequestType == 2 || MessagePushEnableActivity.this.perRequestType == 3) {
                    MessagePushEnableActivity.this.enable_call.setChecked(false);
                }
            }

            @Override // npPermission.nopointer.core.callback.PermissionDialogCallback
            public void onSure(boolean z) {
                MessagePushEnableActivity.this.jumpToPermissionSetting = true;
            }
        });
        requestPermission(requestPermissionInfo);
    }

    private void showNeedPermissionDialog() {
        requestPermission();
    }

    private void showNotMessageWithHelp() {
        new QMUIDialog.MessageDialogBuilder(this).setTitle(getString(R.string.with_no_message_title)).setMessage(getString(R.string.with_no_message_content)).addAction(getString(R.string.not_setting), new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.setting.MessagePushEnableActivity.5
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
            }
        }).addAction(0, getString(R.string.go_to_setting), 2, new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.setting.MessagePushEnableActivity.4
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                PushAiderHelper.getAiderHelper().goToSettingAccessibility(MainApplication.getContext());
            }
        }).create().show();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.setting_item_8);
        this.titleBar.setRightImage(R.mipmap.icon_question);
        this.remindSet = SharePreferenceRemind.read();
        if (this.remindSet == null) {
            this.remindSet = new RemindSetting();
        }
        LogUtil.e("提醒开关:" + this.remindSet.toString());
        this.aiderHelper = PushAiderHelper.getAiderHelper();
        initEvent();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_msg_push_enable;
    }

    @Override // com.suke.widget.SwitchButton.OnCheckedChangeListener
    public void onCheckedChanged(SwitchButton switchButton, boolean z) {
        switch (switchButton.getId()) {
            case R.id.enable_call /* 2131296426 */:
                this.remindSet.callEnable = z;
                if (this.remindSet.callEnable && !hasCallPermission()) {
                    this.perRequestType |= 2;
                    showNeedPermissionDialog();
                    break;
                } else {
                    this.perRequestType ^= 2;
                    break;
                }
                break;
            case R.id.enable_facebook /* 2131296427 */:
                this.remindSet.facebookEnable = z;
                break;
            case R.id.enable_kakao /* 2131296428 */:
                this.remindSet.kakaoEnable = z;
                break;
            case R.id.enable_line /* 2131296429 */:
                this.remindSet.lineEnable = z;
                break;
            case R.id.enable_linked /* 2131296430 */:
                this.remindSet.linkedEnable = z;
                break;
            case R.id.enable_message /* 2131296431 */:
                this.remindSet.messageEnable = z;
                if (this.remindSet.messageEnable && !hasMessagePermission()) {
                    this.perRequestType |= 1;
                    showNeedPermissionDialog();
                    break;
                } else {
                    this.perRequestType ^= 1;
                    break;
                }
                break;
            case R.id.enable_qq /* 2131296432 */:
                this.remindSet.qqEnable = z;
                break;
            case R.id.enable_skype /* 2131296433 */:
                this.remindSet.skypeEnable = z;
                break;
            case R.id.enable_twitter /* 2131296434 */:
                this.remindSet.twitterEnable = z;
                break;
            case R.id.enable_viber /* 2131296435 */:
                this.remindSet.viberEnable = z;
                break;
            case R.id.enable_weixin /* 2131296436 */:
                this.remindSet.wechatEnable = z;
                break;
            case R.id.enable_whatsapp /* 2131296437 */:
                this.remindSet.whatappEnable = z;
                break;
        }
        packData();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.TitleActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.aiderHelper.isNotifyEnable(MainApplication.getContext())) {
            this.permissionState.setText(R.string.state_open);
            this.aiderHelper.start(MainApplication.getApp());
        } else {
            this.permissionState.setText(R.string.state_close);
        }
        if (this.jumpToPermissionSetting) {
            LogUtil.e("跳入设置界面返回后");
            if (this.remindSet.callEnable && !hasCallPermission()) {
                this.enable_call.setChecked(false);
                this.perRequestType ^= 2;
            }
            if (this.remindSet.messageEnable && !hasMessagePermission()) {
                this.enable_message.setChecked(false);
                this.perRequestType ^= 1;
            }
            this.jumpToPermissionSetting = false;
        }
    }

    @Override // com.czw.smartkit.base.TitleActivity
    public void onTitleRightClick(View view) {
        super.onTitleRightClick(view);
        showNotMessageWithHelp();
    }
}
