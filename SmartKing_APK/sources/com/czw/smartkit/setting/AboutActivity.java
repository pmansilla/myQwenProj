package com.czw.smartkit.setting;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.czw.net.NetRespListener;
import com.czw.net.NetResult;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.base.WebActivity;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.DataStruct;
import com.czw.smartkit.bleModule.data.AlarmClock;
import com.czw.smartkit.databaseModule.blood.BloodServiceImpl;
import com.czw.smartkit.databaseModule.hr.HrServiceImpl;
import com.czw.smartkit.databaseModule.ox.OxServiceImpl;
import com.czw.smartkit.databaseModule.sleep.SleepServiceImpl;
import com.czw.smartkit.databaseModule.step.StepServiceImpl;
import com.czw.smartkit.databaseModule.train.TrainServiceImpl;
import com.czw.smartkit.debugModule.DebugLogActivity;
import com.czw.smartkit.net.NetImpl;
import com.czw.smartkit.net.domain.UserDTO;
import com.czw.smartkit.netModule.NetCfg;
import com.czw.smartkit.preferenceModule.SharePreferenceAlost;
import com.czw.smartkit.preferenceModule.SharePreferenceClock;
import com.czw.smartkit.preferenceModule.SharePreferenceDevice;
import com.czw.smartkit.preferenceModule.SharePreferenceLogin;
import com.czw.smartkit.preferenceModule.SharePreferenceRemind;
import com.czw.smartkit.preferenceModule.SharePreferenceUnit;
import com.czw.smartkit.preferenceModule.SharePreferenceUser;
import com.czw.smartkit.update.AppUpdate;
import com.czw.smartkit.user.LoginAcitivty;
import com.czw.smartkit.user.UserUtil;
import com.czw.smartkit.util.ActivityManager;
import com.czw.smartkit.util.AppVersion;
import com.czw.utils.LogUtil;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

/* loaded from: classes.dex */
public class AboutActivity extends TitleActivity implements View.OnClickListener {
    private TextView appversionTv;
    private TextView firmwareTv;

    @BindView(R.id.debug_btn)
    TextView tvDebug;
    private UserDTO userDTO = null;
    private BleManager bleManager = BleManager.getBleManager();
    private int counter = 0;
    private Handler handler = new Handler();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.czw.smartkit.setting.AboutActivity$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements QMUIDialogAction.ActionListener {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.czw.smartkit.setting.AboutActivity$1$1, reason: invalid class name and collision with other inner class name */
        /* loaded from: classes.dex */
        public class RunnableC00371 implements Runnable {

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: com.czw.smartkit.setting.AboutActivity$1$1$1, reason: invalid class name and collision with other inner class name */
            /* loaded from: classes.dex */
            public class RunnableC00381 implements Runnable {
                RunnableC00381() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    BleManager.getBleManager().disConn();
                    AboutActivity.this.delete();
                    AboutActivity.this.getNet();
                    NetImpl.deleteUserData(AboutActivity.this.userDTO.getUserId(), new NetRespListener<NetResult>() { // from class: com.czw.smartkit.setting.AboutActivity.1.1.1.1
                        @Override // com.czw.net.NetRespListener
                        public void onResponse(NetResult netResult) {
                            if (netResult.getErrorCode() == 0) {
                                AboutActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.setting.AboutActivity.1.1.1.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ActivityManager.getInstance().closeAll();
                                        AboutActivity.this.jumpAndFish(LoginAcitivty.class);
                                    }
                                });
                            }
                        }
                    });
                }
            }

            RunnableC00371() {
            }

            @Override // java.lang.Runnable
            public void run() {
                BleManager.getBleManager().writeData(DataStruct.resetSys());
                AboutActivity.this.handler.postDelayed(new RunnableC00381(), 500L);
            }
        }

        AnonymousClass1() {
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
        public void onClick(QMUIDialog qMUIDialog, int i) {
            qMUIDialog.dismiss();
            AboutActivity.this.packAlarmClockData();
            AboutActivity.this.handler.postDelayed(new RunnableC00371(), 500L);
            AboutActivity.this.toast(R.string.resset_success);
        }
    }

    private void cfgAppUpgrade() {
        showLoadingDialog("");
        AppUpdate.getInstance().checkUpdate(true, new AppUpdate.CheckCallback() { // from class: com.czw.smartkit.setting.AboutActivity.3
            @Override // com.czw.smartkit.update.AppUpdate.CheckCallback
            public void onCheckFinish() {
                AboutActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.setting.AboutActivity.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AboutActivity.this.dismissLoadingDialog();
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void delete() {
        if (this.userDTO != null) {
            LogUtil.e("===删除数据》》》" + this.userDTO.getUserId());
            StepServiceImpl.getInstance().deleteUserAllData(UserUtil.getUid());
            SleepServiceImpl.getInstance().deleteUserAllData(UserUtil.getUid());
            HrServiceImpl.getInstance().deleteUserAllData(UserUtil.getUid());
            OxServiceImpl.getInstance().deleteUserAllData(UserUtil.getUid());
            BloodServiceImpl.getInstance().deleteUserAllData(UserUtil.getUid());
            TrainServiceImpl.getInstance().deleteUserAllData(UserUtil.getUid());
        } else {
            LogUtil.e("===没有删除数据");
        }
        SharePreferenceClock.clearAll();
        SharePreferenceUser.clearAll();
        SharePreferenceDevice.clear();
        SharePreferenceRemind.clearAll();
        SharePreferenceLogin.clearAll();
        SharePreferenceUnit.clearAll();
        SharePreferenceAlost.clearAll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void packAlarmClockData() {
        AlarmClock[] alarmClockArr = {new AlarmClock(), new AlarmClock(), new AlarmClock()};
        LogUtil.e("debug==第一个闹钟=>" + alarmClockArr[0].toString());
        BleManager.getBleManager().writeData(DataStruct.createClock(alarmClockArr));
    }

    private void resetDevice() {
        new QMUIDialog.MessageDialogBuilder(this).setTitle(getString(R.string.sure_reset_device)).addAction(getString(R.string.cancel), new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.setting.AboutActivity.2
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
            }
        }).addAction(0, getString(R.string.sure), 2, new AnonymousClass1()).create(2131755258).show();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.about_title);
        $View(R.id.item1).setOnClickListener(this);
        $View(R.id.item2).setOnClickListener(this);
        $View(R.id.item3).setOnClickListener(this);
        $View(R.id.item4).setOnClickListener(this);
        $View(R.id.item5).setOnClickListener(this);
        $View(R.id.item6).setOnClickListener(this);
        $View(R.id.logo_iv).setOnClickListener(this);
        this.firmwareTv = (TextView) $View(R.id.firmwareTv);
        this.appversionTv = (TextView) $View(R.id.appversionTv);
        this.appversionTv.setText(getString(R.string.app_name_sk) + " V " + AppVersion.getAppVersion(this).getVersionName());
        if (this.bleManager.getVersionStr().equals("0.0")) {
            this.firmwareTv.setText("V --");
        } else {
            this.firmwareTv.setText("V " + this.bleManager.getVersionStr());
        }
        this.userDTO = SharePreferenceUser.read();
        this.tvDebug.setVisibility(8);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.activity_about_layout;
    }

    @Override // android.view.View.OnClickListener
    @OnClick({R.id.debug_btn})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.debug_btn) {
            jump(DebugLogActivity.class);
            return;
        }
        if (id == R.id.logo_iv) {
            this.counter++;
            if (this.counter % 5 == 0) {
                this.tvDebug.setVisibility(0);
                return;
            }
            return;
        }
        switch (id) {
            case R.id.item1 /* 2131296520 */:
                jump(HelpInfoActivity.class);
                return;
            case R.id.item2 /* 2131296521 */:
                cfgAppUpgrade();
                return;
            case R.id.item3 /* 2131296522 */:
                if (BleManager.getBleManager().isConn()) {
                    jump(new Intent(this, (Class<?>) OTAActivity.class));
                    return;
                } else {
                    toast(R.string.not_conn);
                    return;
                }
            case R.id.item4 /* 2131296523 */:
                if (BleManager.getBleManager().isConn()) {
                    resetDevice();
                    return;
                } else {
                    toast(R.string.not_conn);
                    return;
                }
            case R.id.item5 /* 2131296524 */:
                Intent intent = new Intent(this, (Class<?>) WebActivity.class);
                intent.putExtra("title", getResources().getString(R.string.use_protocol_auth_message0));
                intent.putExtra(FileDownloadModel.URL, NetCfg.URL1);
                startActivity(intent);
                return;
            case R.id.item6 /* 2131296525 */:
                Intent intent2 = new Intent(this, (Class<?>) WebActivity.class);
                intent2.putExtra("title", getResources().getString(R.string.use_protocol_auth_message1));
                intent2.putExtra(FileDownloadModel.URL, NetCfg.URL2);
                startActivity(intent2);
                return;
            default:
                return;
        }
    }
}
