package com.czw.smartkit.setting;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import basecamera.module.activity.BaseCameraTakePhotoActivity;
import basecamera.module.cfg.CameraSateHelper;
import butterknife.BindView;
import com.czw.smartkit.MainApplication;
import com.czw.smartkit.R;
import com.czw.smartkit.TipsDialog;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.DataStruct;
import com.czw.smartkit.bleModule.DevFunction.DevFunctionHelper;
import com.czw.smartkit.bleModule.LostPlayUtil;
import com.czw.smartkit.bleModule.data.AlostLTO;
import com.czw.smartkit.bleModule.data.DevFunctionEntity;
import com.czw.smartkit.bleModule.data.RemindSetting;
import com.czw.smartkit.bleModule.data.UnitCfg;
import com.czw.smartkit.device.ClockListActivity;
import com.czw.smartkit.device.SearchDeviceActivity;
import com.czw.smartkit.net.domain.UserDTO;
import com.czw.smartkit.observerModule.UnitChangeHelper;
import com.czw.smartkit.preferenceModule.SharePreferenceAlost;
import com.czw.smartkit.preferenceModule.SharePreferenceLogin;
import com.czw.smartkit.preferenceModule.SharePreferenceLoginType;
import com.czw.smartkit.preferenceModule.SharePreferenceRemind;
import com.czw.smartkit.preferenceModule.SharePreferenceUnit;
import com.czw.smartkit.preferenceModule.SharePreferenceUser;
import com.czw.smartkit.sharedpreferences.domain.LoginInfo;
import com.czw.smartkit.sharedpreferences.domain.LoginType;
import com.czw.smartkit.user.LoginAcitivty;
import com.czw.smartkit.user.UpdatePwdActivity;
import com.czw.smartkit.user.UserInfoActivity;
import com.czw.smartkit.util.ActivityManager;
import com.czw.smartkit.views.popw.SingleScrollerPop;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.suke.widget.SwitchButton;
import java.util.ArrayList;
import npPermission.nopointer.core.RequestPermissionInfo;
import ycble.runchinaup.core.BleConnState;
import ycble.runchinaup.core.callback.BleConnCallback;

/* loaded from: classes.dex */
public class SettingActivity extends TitleActivity implements View.OnClickListener, BleConnCallback, DevFunctionHelper.DeviceFunctionCallback, CameraSateHelper.CameraCallback {

    @BindView(R.id.setting_item_update_pwd)
    View itemViewUpdatePwdLayout;

    @BindView(R.id.setting_item_update_pwd_line)
    View itemViewUpdatePwdLine;
    private View llHrLayout;
    private SwitchButton sbALost;
    private SwitchButton sbHandLight;

    @BindView(R.id.setting_item_clock)
    View setting_item_clock;

    @BindView(R.id.setting_item_clock_line)
    View setting_item_clock_line;

    @BindView(R.id.setting_item_hand_light)
    View setting_item_hand_light;

    @BindView(R.id.setting_item_hand_light_line)
    View setting_item_hand_light_line;

    @BindView(R.id.setting_item_long_sit)
    View setting_item_long_sit;

    @BindView(R.id.setting_item_long_sit_line)
    View setting_item_long_sit_line;
    private TextView tvLongSitValue;
    private TextView tvUnitValue;
    UserDTO userDTO = null;
    private BleManager bleManager = BleManager.getBleManager();
    LoginType loginType = null;
    UnitCfg unitCfg = null;

    private void findDevice() {
        this.bleManager.writeData(DataStruct.createFindBrand(true));
        new TipsDialog(this) { // from class: com.czw.smartkit.setting.SettingActivity.3
            @Override // com.czw.smartkit.TipsDialog
            public void onDialogClose() {
                super.onDialogClose();
                SettingActivity.this.bleManager.writeData(DataStruct.createFindBrand(false));
            }
        }.showDialog(R.string.find_device_tips, R.string.cancel);
    }

    private boolean isDeviceConn() {
        if (!this.bleManager.isConn()) {
            toast(R.string.not_conn);
            return false;
        }
        if (!this.bleManager.isSyncHistoryData()) {
            return true;
        }
        toast(R.string.is_sync);
        return false;
    }

    public static ArrayList<String> laodSitTime() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(MainApplication.getContext().getString(R.string.close));
        for (int i = 10; i < 250; i += 10) {
            arrayList.add(i + "");
        }
        return arrayList;
    }

    private final void loadIcons() {
        $View(R.id.setting_item_user_info).setOnClickListener(this);
        $View(R.id.setting_item_add_devie).setOnClickListener(this);
        $View(R.id.setting_item_update_pwd).setOnClickListener(this);
        $View(R.id.setting_item_unit_set).setOnClickListener(this);
        $View(R.id.setting_item_alost).setOnClickListener(this);
        $View(R.id.setting_item_clock).setOnClickListener(this);
        $View(R.id.setting_item_find_device).setOnClickListener(this);
        $View(R.id.setting_item_take_photo).setOnClickListener(this);
        $View(R.id.setting_item_remind_set).setOnClickListener(this);
        $View(R.id.setting_item_hand_light).setOnClickListener(this);
        $View(R.id.setting_item_long_sit).setOnClickListener(this);
        $View(R.id.setting_item_hr_set).setOnClickListener(this);
        $View(R.id.setting_item_about_us).setOnClickListener(this);
        $View(R.id.exitBtn).setOnClickListener(this);
        final AlostLTO read = SharePreferenceAlost.read();
        this.sbALost.setChecked(read.isEnable());
        this.sbALost.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() { // from class: com.czw.smartkit.setting.SettingActivity.4
            @Override // com.suke.widget.SwitchButton.OnCheckedChangeListener
            public void onCheckedChanged(SwitchButton switchButton, boolean z) {
                read.setEnable(z);
                SharePreferenceAlost.save(read);
                if (!z) {
                    LostPlayUtil.getLostPlayUtil().stop();
                }
                SettingActivity.this.bleManager.writeData(new byte[]{112, read.isEnable() ? (byte) 1 : (byte) 0});
            }
        });
        final RemindSetting read2 = SharePreferenceRemind.read();
        this.sbHandLight.setChecked(read2.lightScreen);
        this.sbHandLight.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() { // from class: com.czw.smartkit.setting.SettingActivity.5
            @Override // com.suke.widget.SwitchButton.OnCheckedChangeListener
            public void onCheckedChanged(SwitchButton switchButton, boolean z) {
                read2.lightScreen = z;
                SettingActivity.this.bleManager.writeData(DataStruct.createRemindEnable(read2));
                SharePreferenceRemind.save(read2);
            }
        });
        UnitCfg read3 = SharePreferenceUnit.read();
        if (read3 == null) {
            read3 = new UnitCfg();
        }
        this.tvUnitValue.setText(read3.getIndex() == 0 ? R.string.unit_china : R.string.unit_english);
        if (!read2.longSitEnable) {
            this.tvLongSitValue.setText(R.string.close);
            return;
        }
        this.tvLongSitValue.setText(read2.longSitTime + SQLBuilder.BLANK + getString(R.string.unit_minute));
    }

    public static ArrayList<String> loadUnits() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(MainApplication.getApp().getString(R.string.unit_china_string));
        arrayList.add(MainApplication.getApp().getString(R.string.unit_english_string));
        return arrayList;
    }

    private void refreshHrItemLayout(final DevFunctionEntity devFunctionEntity) {
        if (devFunctionEntity == null) {
            return;
        }
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.setting.SettingActivity.9
            @Override // java.lang.Runnable
            public void run() {
                SettingActivity.this.llHrLayout.setVisibility(devFunctionEntity.isSupportHr() ? 0 : 8);
                if (devFunctionEntity.getPlatform() != 3) {
                    SettingActivity.this.setting_item_clock.setVisibility(0);
                    SettingActivity.this.setting_item_clock_line.setVisibility(0);
                    SettingActivity.this.setting_item_hand_light.setVisibility(0);
                    SettingActivity.this.setting_item_hand_light_line.setVisibility(0);
                    SettingActivity.this.setting_item_long_sit.setVisibility(0);
                    SettingActivity.this.setting_item_long_sit_line.setVisibility(0);
                    return;
                }
                SettingActivity.this.setting_item_clock.setVisibility(8);
                SettingActivity.this.setting_item_clock_line.setVisibility(8);
                SettingActivity.this.setting_item_hand_light.setVisibility(8);
                SettingActivity.this.setting_item_hand_light_line.setVisibility(8);
                SettingActivity.this.setting_item_long_sit.setVisibility(8);
                SettingActivity.this.setting_item_long_sit_line.setVisibility(8);
            }
        });
    }

    private void refreshSwitchButton(final boolean z) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.setting.SettingActivity.8
            @Override // java.lang.Runnable
            public void run() {
                SettingActivity.this.sbALost.setEnabled(z);
                SettingActivity.this.sbHandLight.setEnabled(z);
            }
        });
    }

    private void setLongSitTime() {
        String string;
        final RemindSetting read = SharePreferenceRemind.read();
        SingleScrollerPop showPicker = SingleScrollerPop.getPop(getUI()).showPicker($View(R.id.acv_win), laodSitTime(), new SingleScrollerPop.ClickCallback() { // from class: com.czw.smartkit.setting.SettingActivity.1
            @Override // com.czw.smartkit.views.popw.SingleScrollerPop.ClickCallback
            public void onSelect(int i) {
                if (i == 0) {
                    SettingActivity.this.tvLongSitValue.setText(R.string.close);
                    read.longSitEnable = false;
                    SharePreferenceRemind.save(read);
                } else {
                    int intValue = Integer.valueOf(SettingActivity.laodSitTime().get(i)).intValue();
                    SettingActivity.this.tvLongSitValue.setText(intValue + SQLBuilder.BLANK + SettingActivity.this.getString(R.string.unit_minute));
                    read.longSitEnable = true;
                    read.longSitTime = intValue;
                    SharePreferenceRemind.save(read);
                }
                SettingActivity.this.bleManager.writeData(DataStruct.createRemindEnable(read));
            }
        });
        if (read.longSitEnable) {
            string = read.longSitTime + "";
        } else {
            string = getString(R.string.close);
        }
        showPicker.showTitleWithValue(R.string.long_sit_time, string);
    }

    private void setUnitSet() {
        this.unitCfg = SharePreferenceUnit.read();
        if (this.unitCfg == null) {
            this.unitCfg = new UnitCfg();
        }
        SingleScrollerPop.getPop(getUI()).showPicker($View(R.id.acv_win), loadUnits(), new SingleScrollerPop.ClickCallback() { // from class: com.czw.smartkit.setting.SettingActivity.2
            @Override // com.czw.smartkit.views.popw.SingleScrollerPop.ClickCallback
            public void onSelect(int i) {
                SettingActivity.this.unitCfg.setIndex(i);
                if (i == 0) {
                    SettingActivity.this.tvUnitValue.setText(R.string.unit_china);
                } else {
                    SettingActivity.this.tvUnitValue.setText(R.string.unit_english);
                }
                SharePreferenceUnit.save(SettingActivity.this.unitCfg);
                UnitChangeHelper.getInstance().notifyUnitChange(SettingActivity.this.unitCfg);
                SettingActivity.this.bleManager.writeData(DataStruct.unitSet(i));
            }
        }).showTitleWithValue(R.string.settting_item_unit, loadUnits().get(this.unitCfg.getIndex()));
    }

    private void sureExit() {
        new QMUIDialog.MessageDialogBuilder(this).setTitle(getString(R.string.logout)).addAction(getString(R.string.cancel), new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.setting.SettingActivity.7
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
            }
        }).addAction(0, getString(R.string.sure), 2, new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.setting.SettingActivity.6
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
                ActivityManager.getInstance().closeAll();
                SettingActivity.this.showLoadingDialog("");
                SharePreferenceUser.clearAll();
                SharePreferenceLoginType.clearAll();
                LoginInfo read = SharePreferenceLogin.read();
                if (read != null && !TextUtils.isEmpty(read.getName())) {
                    read.setValue("");
                    SharePreferenceLogin.save(read);
                }
                SettingActivity.this.bleManager.disConn();
                SettingActivity.this.dismissLoadingDialog();
                SettingActivity.this.jumpAndFinsh(LoginAcitivty.class);
            }
        }).create(2131755258).show();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.setting_title);
        this.sbALost = (SwitchButton) $View(R.id.setting_item_alost_sb);
        this.sbHandLight = (SwitchButton) $View(R.id.setting_item_handlight_sb);
        this.llHrLayout = $View(R.id.setting_item_hr_set);
        this.tvUnitValue = (TextView) $View(R.id.setting_item_unit_value_tv);
        this.tvLongSitValue = (TextView) $View(R.id.setting_item_longsit_value_tv);
        this.userDTO = SharePreferenceUser.read();
        this.bleManager.registerConnCallback(this);
        DevFunctionHelper.getInstance().registerDeviceFunctionCallback(this);
        loadIcons();
        refreshSwitchButton(this.bleManager.isConn());
        refreshHrItemLayout(DevFunctionHelper.getInstance().getDevFunctionEntity());
        this.loginType = SharePreferenceLoginType.read();
        if (this.loginType == null) {
            this.itemViewUpdatePwdLayout.setVisibility(8);
            this.itemViewUpdatePwdLine.setVisibility(8);
            return;
        }
        if (this.loginType != LoginType.User) {
            this.itemViewUpdatePwdLayout.setVisibility(8);
            this.itemViewUpdatePwdLine.setVisibility(8);
            return;
        }
        this.itemViewUpdatePwdLayout.setVisibility(0);
        this.itemViewUpdatePwdLine.setVisibility(0);
        LoginInfo read = SharePreferenceLogin.read();
        if (read == null || TextUtils.isEmpty(read.getName())) {
            this.itemViewUpdatePwdLayout.setVisibility(8);
            this.itemViewUpdatePwdLine.setVisibility(8);
        }
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.activity_settting_layout;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exitBtn /* 2131296446 */:
                sureExit();
                return;
            case R.id.setting_item_about_us /* 2131296743 */:
                jump(AboutActivity.class);
                return;
            case R.id.setting_item_add_devie /* 2131296744 */:
                jump(SearchDeviceActivity.class);
                return;
            case R.id.setting_item_alost /* 2131296745 */:
                isDeviceConn();
                return;
            case R.id.setting_item_clock /* 2131296747 */:
                if (isDeviceConn()) {
                    jump(ClockListActivity.class);
                    return;
                }
                return;
            case R.id.setting_item_find_device /* 2131296749 */:
                if (isDeviceConn()) {
                    findDevice();
                    return;
                }
                return;
            case R.id.setting_item_hand_light /* 2131296750 */:
            default:
                return;
            case R.id.setting_item_hr_set /* 2131296753 */:
                if (isDeviceConn()) {
                    jump(HrSettingActivity.class);
                    return;
                }
                return;
            case R.id.setting_item_long_sit /* 2131296767 */:
                if (isDeviceConn()) {
                    setLongSitTime();
                    return;
                }
                return;
            case R.id.setting_item_remind_set /* 2131296770 */:
                if (isDeviceConn()) {
                    jump(MessagePushEnableActivity.class);
                    return;
                }
                return;
            case R.id.setting_item_take_photo /* 2131296771 */:
                if (isDeviceConn()) {
                    requestCameraPermission();
                    return;
                }
                return;
            case R.id.setting_item_unit_set /* 2131296772 */:
                if (isDeviceConn()) {
                    setUnitSet();
                    return;
                }
                return;
            case R.id.setting_item_update_pwd /* 2131296774 */:
                if (this.userDTO == null || this.userDTO.getUserId().equals("000")) {
                    toast(R.string.not_login);
                    return;
                } else {
                    jump(UpdatePwdActivity.class);
                    return;
                }
            case R.id.setting_item_user_info /* 2131296776 */:
                if (this.userDTO == null || this.userDTO.getUserId().equals("000")) {
                    toast(R.string.not_login);
                    return;
                } else {
                    jump(UserInfoActivity.class);
                    return;
                }
        }
    }

    @Override // ycble.runchinaup.core.callback.BleConnCallback
    public void onConnState(BleConnState bleConnState) {
        refreshSwitchButton(bleConnState == BleConnState.CONNECTED);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.bleManager.unRegisterConnCallback(this);
        DevFunctionHelper.getInstance().unRegisterDeviceFunctionCallback(this);
        CameraSateHelper.getInstance().unRegisterCallback(this);
    }

    @Override // com.czw.smartkit.base.BasePermissionActivity, npPermission.nopointer.core.callback.PermissionCallback
    public void onGetAllPermission() {
        super.onGetAllPermission();
        this.bleManager.writeData(DataStruct.createTakePhoto(true));
        jumpFor(BaseCameraTakePhotoActivity.class, 100);
    }

    @Override // com.czw.smartkit.bleModule.DevFunction.DevFunctionHelper.DeviceFunctionCallback
    public void onGetFunction(DevFunctionEntity devFunctionEntity) {
        refreshHrItemLayout(devFunctionEntity);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.TitleActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        CameraSateHelper.getInstance().registerCallback(this);
    }

    @Override // basecamera.module.cfg.CameraSateHelper.CameraCallback
    public void onTakePhotoFailure(int i) {
    }

    @Override // basecamera.module.cfg.CameraSateHelper.CameraCallback
    public void onTakePhotoSuccess(final String str) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.setting.SettingActivity.10
            @Override // java.lang.Runnable
            public void run() {
                SettingActivity.this.toast(SettingActivity.this.getResources().getString(R.string.camera_save_success) + str);
            }
        });
    }

    public void requestCameraPermission() {
        RequestPermissionInfo requestPermissionInfo = new RequestPermissionInfo();
        requestPermissionInfo.setPermissionArr(new String[]{"android.permission.CAMERA"});
        requestPermissionInfo.setPermissionTitle($str(R.string.permission_agin_title));
        requestPermissionInfo.setPermissionMessage($str(R.string.take_photo_message));
        requestPermissionInfo.setPermissionCancelText(getString(android.R.string.cancel));
        requestPermissionInfo.setPermissionSureText(getString(android.R.string.ok));
        requestPermissionInfo.setAgainPermissionMessage(getString(R.string.take_photo_message));
        requestPermissionInfo.setAgainPermissionSureText(getString(android.R.string.ok));
        requestPermissionInfo.setAgainPermissionCancelText(getString(android.R.string.cancel));
        requestPermission(requestPermissionInfo);
    }
}
