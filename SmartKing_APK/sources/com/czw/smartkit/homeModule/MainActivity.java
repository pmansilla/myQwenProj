package com.czw.smartkit.homeModule;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.czw.modes.widget.smartking.BatteryView;
import com.czw.smartkit.MainApplication;
import com.czw.smartkit.R;
import com.czw.smartkit.absimpl.DisplayListenerImpl;
import com.czw.smartkit.absimpl.OnPageChangeListenerImpl;
import com.czw.smartkit.base.BasePermissionActivity;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.DataStruct;
import com.czw.smartkit.bleModule.DevFunction.DevFunctionHelper;
import com.czw.smartkit.bleModule.SyncCallback;
import com.czw.smartkit.bleModule.battery.BatteryHelper;
import com.czw.smartkit.bleModule.data.DevFunctionEntity;
import com.czw.smartkit.device.SearchDeviceActivity;
import com.czw.smartkit.homeModule.fragment.FragmentMeasure;
import com.czw.smartkit.homeModule.fragment.FragmentSleep;
import com.czw.smartkit.homeModule.fragment.FragmentStep;
import com.czw.smartkit.homeModule.fragment.FragmentTrain;
import com.czw.smartkit.homeModule.syncData.SyncDataService;
import com.czw.smartkit.net.domain.UserDTO;
import com.czw.smartkit.preferenceModule.SharePreferenceDevice;
import com.czw.smartkit.preferenceModule.SharePreferenceUser;
import com.czw.smartkit.preferenceModule.SharedPrefereceWeather;
import com.czw.smartkit.setting.SettingActivity;
import com.czw.smartkit.update.AppUpdate;
import com.czw.smartkit.util.SkUtils;
import com.czw.utils.LogUtil;
import com.czw.utils.ViewUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import java.util.ArrayList;
import java.util.List;
import me.panpf.sketch.SketchImageView;
import me.panpf.sketch.process.CircleImageProcessor;
import me.panpf.sketch.process.ImageProcessor;
import me.panpf.sketch.request.ErrorCause;
import npPermission.nopointer.core.RequestPermissionInfo;
import ycble.runchinaup.aider.PushAiderHelper;
import ycble.runchinaup.core.BleConnState;
import ycble.runchinaup.core.BleScanner;
import ycble.runchinaup.core.callback.BleConnCallback;
import ycble.runchinaup.device.BleDevice;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes.dex */
public class MainActivity extends BasePermissionActivity implements View.OnClickListener, BatteryHelper.BatteryCallback, BleConnCallback, DevFunctionHelper.DeviceFunctionCallback, SyncCallback {
    private static final int REQUEST_ENABLE_BT = 16;
    private TextView batteryTv;
    private BatteryView batteryView;
    private ImageView ble_conn_flag;
    private TextView ble_conn_text;
    private MyPagerAdapter mAdapter;
    private String[] mTitles;
    private SlidingTabLayout stb_title;
    private View syncTipLayout;
    private SketchImageView userHeader;
    private ViewPager viewPager;
    private FragmentStep fragmentStep = new FragmentStep();
    private FragmentSleep fragmentSleep = new FragmentSleep();
    private FragmentTrain page3 = new FragmentTrain();
    private FragmentMeasure page4 = new FragmentMeasure();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private BleManager bleManager = BleManager.getBleManager();
    private int lastIndex = 0;
    private Handler handlerView = new Handler() { // from class: com.czw.smartkit.homeModule.MainActivity.1
        @Override // android.os.Handler
        public synchronized void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                DevFunctionEntity devFunctionEntity = (DevFunctionEntity) message.obj;
                if (devFunctionEntity == null) {
                    return;
                }
                if (!devFunctionEntity.isSupportHr() && !devFunctionEntity.isSupportOx() && !devFunctionEntity.isSupportBlood()) {
                    if (MainActivity.this.lastIndex >= 3) {
                        MainActivity.this.lastIndex = 0;
                    }
                    MainActivity.this.stb_title.setCurrentTab(MainActivity.this.lastIndex, true);
                    if (MainActivity.this.mFragments.contains(MainActivity.this.page4)) {
                        MainActivity.this.mFragments.remove(MainActivity.this.page4);
                    }
                    MainActivity.this.mAdapter.notifyDataSetChanged();
                    MainActivity.this.mTitles = MainActivity.this.getResources().getStringArray(R.array.nav_title_no_measure);
                    MainActivity.this.stb_title.setViewPager(MainActivity.this.viewPager, MainActivity.this.mTitles);
                }
                if (!MainActivity.this.mFragments.contains(MainActivity.this.page4)) {
                    MainActivity.this.mFragments.add(MainActivity.this.page4);
                }
                MainActivity.this.mAdapter.notifyDataSetChanged();
                MainActivity.this.mTitles = MainActivity.this.getResources().getStringArray(R.array.nav_title);
                MainActivity.this.stb_title.setViewPager(MainActivity.this.viewPager, MainActivity.this.mTitles);
            }
        }
    };
    QMUIDialog notificationPermissionDialog = null;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return MainActivity.this.mFragments.size();
        }

        @Override // android.support.v4.app.FragmentPagerAdapter
        public Fragment getItem(int i) {
            return (Fragment) MainActivity.this.mFragments.get(i);
        }
    }

    private void gotoSettingPermission() {
        if (this.notificationPermissionDialog == null) {
            this.notificationPermissionDialog = new QMUIDialog.MessageDialogBuilder(this).setMessage(getString(R.string.main_notification_permission_text)).addAction(getString(R.string.cancel), new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.homeModule.MainActivity.11
                @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
                public void onClick(QMUIDialog qMUIDialog, int i) {
                    qMUIDialog.dismiss();
                }
            }).addAction(0, getString(R.string.sure), 0, new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.homeModule.MainActivity.10
                @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
                public void onClick(QMUIDialog qMUIDialog, int i) {
                    qMUIDialog.dismiss();
                    PushAiderHelper.getAiderHelper().goToSettingNotificationAccess(MainApplication.getContext());
                }
            }).create(2131755258);
            this.notificationPermissionDialog.setCanceledOnTouchOutside(false);
            this.notificationPermissionDialog.setCancelable(false);
        }
        this.notificationPermissionDialog.show();
    }

    private void initPages() {
        this.mTitles = getResources().getStringArray(R.array.nav_title);
        this.mFragments.add(this.fragmentStep);
        this.mFragments.add(this.fragmentSleep);
        this.mFragments.add(this.page3);
        this.mFragments.add(this.page4);
        this.viewPager.setOffscreenPageLimit(3);
        this.mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(this.mAdapter);
        this.stb_title.setViewPager(this.viewPager, this.mTitles);
        this.viewPager.addOnPageChangeListener(new OnPageChangeListenerImpl() { // from class: com.czw.smartkit.homeModule.MainActivity.4
            @Override // com.czw.smartkit.absimpl.OnPageChangeListenerImpl, android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                MainActivity.this.onPageSelect(i);
            }
        });
    }

    private void refreshSyncTip(final boolean z) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.MainActivity.9
            @Override // java.lang.Runnable
            public void run() {
                MainActivity.this.syncTipLayout.setVisibility(z ? 0 : 8);
            }
        });
    }

    private void requestPermission() {
        RequestPermissionInfo requestPermissionInfo = new RequestPermissionInfo();
        requestPermissionInfo.setPermissionTitle($str(R.string.all_permission_title));
        requestPermissionInfo.setPermissionMessage($str(R.string.all_permission_title_message));
        requestPermissionInfo.setPermissionCancelText($str(android.R.string.cancel));
        requestPermissionInfo.setPermissionSureText($str(android.R.string.ok));
        requestPermissionInfo.setAgainPermissionMessage($str(R.string.all_permission_title_message));
        requestPermissionInfo.setAgainPermissionCancelText($str(android.R.string.cancel));
        requestPermissionInfo.setAgainPermissionSureText($str(android.R.string.ok));
        requestPermissionInfo.setPermissionArr(new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.READ_CONTACTS", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECEIVE_SMS", "android.permission.READ_SMS", "android.permission.READ_CALL_LOG"});
        requestPermission(requestPermissionInfo);
    }

    private void updateHeaderr() {
        UserDTO read = SharePreferenceUser.read();
        if (read == null) {
            return;
        }
        this.userHeader.getOptions().setProcessor((ImageProcessor) CircleImageProcessor.getInstance());
        this.userHeader.displayImage(read.getPhoto());
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.ble_conn_flag = (ImageView) $View(R.id.ble_conn_flag);
        this.ble_conn_text = (TextView) $View(R.id.ble_conn_text);
        this.batteryTv = (TextView) $View(R.id.batteryTv);
        this.userHeader = (SketchImageView) $View(R.id.userHeader);
        this.batteryView = (BatteryView) $View(R.id.batteryView);
        this.syncTipLayout = $View(R.id.syncTipLayout);
        this.stb_title = (SlidingTabLayout) $View(R.id.stb_title);
        this.viewPager = (ViewPager) $View(R.id.viewpager);
        this.userHeader.getOptions().setProcessor((ImageProcessor) CircleImageProcessor.getInstance());
        this.userHeader.setDisplayListener(new DisplayListenerImpl() { // from class: com.czw.smartkit.homeModule.MainActivity.2
            @Override // com.czw.smartkit.absimpl.DisplayListenerImpl, me.panpf.sketch.request.Listener
            public void onError(ErrorCause errorCause) {
                MainActivity.this.userHeader.displayResourceImage(R.mipmap.logo);
            }
        });
        $View(R.id.userHeader).setOnClickListener(this);
        $View(R.id.conn_layout).setOnClickListener(this);
        $View(R.id.shareBtn).setOnClickListener(this);
        initPages();
        updateHeaderr();
        DevFunctionHelper.getInstance().registerDeviceFunctionCallback(this);
        this.bleManager.registerConnCallback(this);
        BatteryHelper.getInstance().registerCallback(this);
        this.bleManager.registerSyncCallback(this);
        SharedPrefereceWeather.clear();
        this.handlerView.postDelayed(new Runnable() { // from class: com.czw.smartkit.homeModule.MainActivity.3
            @Override // java.lang.Runnable
            public void run() {
                MainActivity.this.startService(new Intent(MainActivity.this, (Class<?>) SyncDataService.class));
            }
        }, 1200L);
        AppUpdate.getInstance().checkUpdate(false, null);
        if (PushAiderHelper.getAiderHelper().isNotifyEnable(MainApplication.getContext())) {
            return;
        }
        gotoSettingPermission();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity
    public void insertInit() {
        super.insertInit();
        requestPermission();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.activity_main_layout;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 200 && i == 16 && i2 != -1) {
            finish();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.conn_layout) {
            jump(SearchDeviceActivity.class);
        } else if (id == R.id.shareBtn) {
            SkUtils.showShare(getUI(), ViewUtil.getCacheBitmapFromView($View(R.id.act_win)));
        } else {
            if (id != R.id.userHeader) {
                return;
            }
            jump(SettingActivity.class);
        }
    }

    @Override // ycble.runchinaup.core.callback.BleConnCallback
    public void onConnState(BleConnState bleConnState) {
        updateConnFlag(bleConnState == BleConnState.CONNECTED);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.bleManager.unRegisterConnCallback(this);
        BatteryHelper.getInstance().unRegisterCallback(this);
        DevFunctionHelper.getInstance().unRegisterDeviceFunctionCallback(this);
        this.bleManager.unRegisterSyncCallback(this);
    }

    @Override // com.czw.smartkit.bleModule.battery.BatteryHelper.BatteryCallback
    public void onGetBattery(int i) {
        updateBattery(i);
    }

    @Override // com.czw.smartkit.bleModule.DevFunction.DevFunctionHelper.DeviceFunctionCallback
    public synchronized void onGetFunction(final DevFunctionEntity devFunctionEntity) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.MainActivity.5
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.e("debug==设备功能==>" + new Gson().toJson(devFunctionEntity));
                MainActivity.this.handlerView.sendMessage(MainActivity.this.handlerView.obtainMessage(1, devFunctionEntity));
            }
        });
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (isExit(i)) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    protected void onPageSelect(int i) {
        this.lastIndex = i;
        if (this.fragmentStep != null) {
            this.fragmentStep.requestLocation(false);
        }
        switch (i) {
            case 0:
                this.lastIndex = 0;
                this.bleManager.writeData(DataStruct.createHistorySportData(0));
                if (this.fragmentStep != null) {
                    this.fragmentStep.requestLocation(true);
                    return;
                }
                return;
            case 1:
                this.lastIndex = 1;
                return;
            case 2:
                this.lastIndex = 2;
                return;
            case 3:
                this.lastIndex = 3;
                this.bleManager.writeData(DataStruct.createHistoryCheckData());
                return;
            default:
                return;
        }
    }

    @Override // com.czw.smartkit.base.BasePermissionActivity, npPermission.nopointer.core.callback.PermissionCallback
    public void onPermissionsGranted(int i, List<String> list) {
        super.onPermissionsGranted(i, list);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (BleScanner.getInstance().isEnabled()) {
            boolean isConn = this.bleManager.isConn();
            updateHeaderr();
            updateConnFlag(isConn);
            updateBattery(BatteryHelper.getInstance().getBattery());
            onGetFunction(DevFunctionHelper.getInstance().getDevFunctionEntity());
        } else {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 16);
        }
        BleDevice read = SharePreferenceDevice.read();
        if (read != null && !TextUtils.isEmpty(read.getMac())) {
            BleManager.getBleManager().connDevice(read.getMac());
        }
        onPageSelect(this.lastIndex);
    }

    @Override // com.czw.smartkit.bleModule.SyncCallback
    public void onSyncDataing() {
        refreshSyncTip(true);
    }

    @Override // com.czw.smartkit.bleModule.SyncCallback
    public void onSyncFinish() {
        refreshSyncTip(false);
        if (this.fragmentStep != null) {
            ycBleLog.e("数据同步完成请求定位 ，获取天气");
            this.fragmentStep.requestLocation(true);
        }
    }

    protected void toastMsg() {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.MainActivity.6
            @Override // java.lang.Runnable
            public void run() {
                MainActivity.this.toast(R.string.is_sync_data);
            }
        });
    }

    public final void updateBattery(final int i) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.MainActivity.8
            @Override // java.lang.Runnable
            public void run() {
                if (i <= 0) {
                    MainActivity.this.batteryTv.setText("-%");
                    MainActivity.this.batteryView.updateShow(0);
                    return;
                }
                MainActivity.this.updateConnFlag(true);
                MainActivity.this.batteryTv.setText(i + "%");
                MainActivity.this.batteryView.updateShow(Integer.valueOf(i).intValue());
            }
        });
    }

    public final void updateConnFlag(final boolean z) {
        LogUtil.e("debug===isConn》》" + z);
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.homeModule.MainActivity.7
            @Override // java.lang.Runnable
            public void run() {
                MainActivity.this.ble_conn_flag.setImageResource(z ? R.mipmap.ic_ble_conn : R.mipmap.ic_ble_disconn);
                if (z) {
                    MainActivity.this.ble_conn_text.setText(R.string.had_conn);
                } else if (BleManager.getBleManager().getBleConnState() == BleConnState.CONNECTING || BleManager.getBleManager().getBleConnState() == BleConnState.SEARCH_ING) {
                    MainActivity.this.ble_conn_text.setText(R.string.pro_connect_ing);
                } else {
                    MainActivity.this.ble_conn_text.setText(R.string.not_conn);
                }
                if (z) {
                    return;
                }
                MainActivity.this.batteryTv.setText("-%");
                MainActivity.this.batteryView.updateShow(0);
            }
        });
    }
}
