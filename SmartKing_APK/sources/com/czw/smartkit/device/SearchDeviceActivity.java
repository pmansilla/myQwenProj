package com.czw.smartkit.device;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import com.czw.smartkit.R;
import com.czw.smartkit.base.BaseBleScanActivity;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.SmartKingFilter;
import com.czw.smartkit.device.DeviceAdapter;
import com.czw.smartkit.preferenceModule.SharePreferenceDevice;
import com.czw.utils.ViewUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import ycble.runchinaup.core.BleConnState;
import ycble.runchinaup.core.BleScanner;
import ycble.runchinaup.core.callback.BleConnCallback;
import ycble.runchinaup.core.callback.ScanListener;
import ycble.runchinaup.device.BleDevice;

/* loaded from: classes.dex */
public class SearchDeviceActivity extends BaseBleScanActivity implements ScanListener<BleDevice>, BleConnCallback {
    private static final int MSG_SCAN_DEVICE = 2;
    private SwipeMenuRecyclerView myListView;
    private ProgressBar progress;
    private SwipeMenuRecyclerView scanListView;
    private DeviceAdapter scanAdapter = null;
    ArrayList<BleDevice> scanList = new ArrayList<>();
    private BleManager bleManager = BleManager.getBleManager();
    private ArrayList<BleDevice> myDevice = new ArrayList<>();
    private DeviceAdapter myAdapter = null;
    private HashSet<String> scanMacs = new HashSet<>();
    private BleScanner bleScanner = BleScanner.getInstance();
    private Handler handlerShowPro = new Handler() { // from class: com.czw.smartkit.device.SearchDeviceActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 2) {
                BleDevice bleDevice = (BleDevice) message.obj;
                if (SearchDeviceActivity.this.scanMacs.contains(bleDevice.getMac())) {
                    return;
                }
                SearchDeviceActivity.this.scanMacs.add(bleDevice.getMac());
                SearchDeviceActivity.this.scanList.add(bleDevice);
                SearchDeviceActivity.this.scanAdapter.notifyDataSetChanged();
            }
        }
    };
    private final SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() { // from class: com.czw.smartkit.device.SearchDeviceActivity.4
        @Override // com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
        public void onCreateMenu(SwipeMenu swipeMenu, SwipeMenu swipeMenu2, int i) {
            swipeMenu2.addMenuItem(new SwipeMenuItem(SearchDeviceActivity.this.getUI()).setBackgroundColor(Color.parseColor("#FF555B")).setText(R.string.delete).setTextColor(Color.parseColor("#FFFFFF")).setWidth(ViewUtil.dip2px(70.0f)).setHeight(-1));
        }
    };
    private final SwipeMenuItemClickListener listener = new SwipeMenuItemClickListener() { // from class: com.czw.smartkit.device.SearchDeviceActivity.5
        @Override // com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
        public void onItemClick(SwipeMenuBridge swipeMenuBridge) {
            if (swipeMenuBridge.getPosition() == 0) {
                SearchDeviceActivity.this.deleteMyDevice();
            }
            SearchDeviceActivity.this.myListView.smoothCloseMenu();
        }
    };
    private boolean isScan = false;
    private Handler handler = new Handler();

    /* JADX INFO: Access modifiers changed from: private */
    public void bondMyDevice(final int i) {
        new QMUIDialog.MessageDialogBuilder(this).setMessage(getString(R.string.bind_dialog_content)).addAction(getString(R.string.cancel), new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.device.SearchDeviceActivity.9
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i2) {
                qMUIDialog.dismiss();
            }
        }).addAction(0, getString(R.string.ok), 0, new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.device.SearchDeviceActivity.8
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i2) {
                qMUIDialog.dismiss();
                BleDevice bleDevice = SearchDeviceActivity.this.scanList.get(i);
                SearchDeviceActivity.this.scanMacs.add(bleDevice.getMac());
                SharePreferenceDevice.save(bleDevice);
                SearchDeviceActivity.this.stop();
                SearchDeviceActivity.this.refreshMyDevice();
                SearchDeviceActivity.this.bleManager.connDevice(bleDevice.getMac());
                SearchDeviceActivity.this.scanList.remove(i);
                SearchDeviceActivity.this.scanAdapter.notifyDataSetChanged();
            }
        }).create(2131755258).show();
    }

    private void clickScanDevice() {
        this.isScan = !this.isScan;
        if (!this.isScan) {
            stop();
            return;
        }
        startScan();
        this.titleBar.setRightText(R.string.stop_scan);
        this.progress.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteMyDevice() {
        new QMUIDialog.MessageDialogBuilder(this).setMessage(getString(R.string.unbind_dialog_content)).addAction(getString(R.string.cancel), new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.device.SearchDeviceActivity.11
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
            }
        }).addAction(0, getString(R.string.ok), 2, new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.device.SearchDeviceActivity.10
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
                BleDevice read = SharePreferenceDevice.read();
                if (read != null && !TextUtils.isEmpty(read.getMac())) {
                    SearchDeviceActivity.this.scanMacs.remove(read.getMac());
                }
                SharePreferenceDevice.clear();
                SearchDeviceActivity.this.bleManager.disConn();
                SearchDeviceActivity.this.refreshMyDevice();
            }
        }).create(2131755258).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void refreshMyDevice() {
        BleDevice read = SharePreferenceDevice.read();
        this.myDevice.clear();
        if (read != null) {
            this.myDevice.add(read);
            this.scanMacs.add(read.getMac());
        }
        if (this.myDevice.size() < 1) {
            $View(R.id.myLayout).setVisibility(8);
        } else {
            $View(R.id.myLayout).setVisibility(0);
        }
        this.myAdapter.notifyDataSetChanged();
    }

    private final void startScan() {
        this.scanMacs.clear();
        this.scanList.clear();
        this.scanAdapter.notifyDataSetChanged();
        this.progress.setVisibility(0);
        this.bleScanner.startScan();
        this.bleScanner.registerScanListener(this);
        this.titleBar.setRightText(R.string.stop_scan);
        this.handler.postDelayed(new Runnable() { // from class: com.czw.smartkit.device.SearchDeviceActivity.6
            @Override // java.lang.Runnable
            public void run() {
                SearchDeviceActivity.this.stop();
            }
        }, 10000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void stop() {
        this.isScan = false;
        this.handler.removeCallbacksAndMessages(null);
        this.bleScanner.unRegisterScanListener(this);
        this.bleScanner.stopScan();
        this.titleBar.setRightText(R.string.start_scan);
        this.progress.setVisibility(8);
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.bleScanner.setBleDeviceFilter(SmartKingFilter.getInstance());
        this.titleBar.setTitle(R.string.search_device_title);
        this.titleBar.setRightText(R.string.start_scan);
        this.progress = (ProgressBar) $View(R.id.progress);
        this.myListView = (SwipeMenuRecyclerView) $View(R.id.myListView);
        this.myAdapter = new DeviceAdapter(this, this.myDevice) { // from class: com.czw.smartkit.device.SearchDeviceActivity.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.czw.smartkit.device.DeviceAdapter, com.czw.smartkit.modes.adapter.RecycleAdapter
            public void handDataAndView(DeviceAdapter.ItemTag itemTag, BleDevice bleDevice, int i) {
                super.handDataAndView(itemTag, bleDevice, i);
                itemTag.deviceConnFlag.setVisibility(SearchDeviceActivity.this.bleManager.isConn() ? 0 : 8);
            }
        };
        this.myListView.setLayoutManager(new LinearLayoutManager(this));
        this.myListView.setSwipeMenuCreator(this.swipeMenuCreator);
        this.myListView.setSwipeMenuItemClickListener(this.listener);
        this.myListView.setAdapter(this.myAdapter);
        this.scanListView = (SwipeMenuRecyclerView) $View(R.id.scanListView);
        this.scanListView.setLayoutManager(new LinearLayoutManager(this));
        this.scanAdapter = new DeviceAdapter(this, this.scanList) { // from class: com.czw.smartkit.device.SearchDeviceActivity.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.czw.smartkit.device.DeviceAdapter, com.czw.smartkit.modes.adapter.RecycleAdapter
            public void handDataAndView(DeviceAdapter.ItemTag itemTag, BleDevice bleDevice, int i) {
                super.handDataAndView(itemTag, bleDevice, i);
                itemTag.deviceConnFlag.setVisibility(8);
            }

            @Override // com.czw.smartkit.device.DeviceAdapter
            protected void onItemClick(BleDevice bleDevice, int i) {
                if (SearchDeviceActivity.this.myDevice == null || SearchDeviceActivity.this.myDevice.size() <= 0) {
                    SearchDeviceActivity.this.bondMyDevice(i);
                } else {
                    SearchDeviceActivity.this.toast(R.string.please_delete_old_device);
                }
            }
        };
        this.scanListView.setAdapter(this.scanAdapter);
        refreshMyDevice();
        isCanScanDevice();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_search_device;
    }

    @Override // ycble.runchinaup.core.callback.BleConnCallback
    public void onConnState(final BleConnState bleConnState) {
        runOnUiThread(new Runnable() { // from class: com.czw.smartkit.device.SearchDeviceActivity.7
            @Override // java.lang.Runnable
            public void run() {
                BleConnState bleConnState2 = bleConnState;
                BleConnState bleConnState3 = BleConnState.CONNECTING;
                if (bleConnState == BleConnState.CONNECTED) {
                    SearchDeviceActivity.this.refreshMyDevice();
                } else if (bleConnState == BleConnState.CONNFAILURE) {
                    SearchDeviceActivity.this.toast(R.string.conn_failure_not_near);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.scanMacs.clear();
        this.bleManager.unRegisterConnCallback(this);
        this.bleScanner.unRegisterScanListener(this);
        this.bleScanner.stopScan();
    }

    @Override // ycble.runchinaup.core.callback.ScanListener
    public void onFailure(int i) {
    }

    @Override // com.czw.smartkit.base.BaseBleScanActivity, com.czw.smartkit.base.TitleActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.bleManager.registerConnCallback(this);
    }

    @Override // ycble.runchinaup.core.callback.ScanListener
    public synchronized void onScan(BleDevice bleDevice) {
        this.handlerShowPro.sendMessage(this.handler.obtainMessage(2, bleDevice));
    }

    @Override // com.czw.smartkit.base.TitleActivity
    public void onTitleRightClick(View view) {
        super.onTitleRightClick(view);
        if (isCanScanDevice()) {
            clickScanDevice();
        }
    }
}
