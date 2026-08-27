package com.czw.smartkit.setting;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import butterknife.OnClick;
import com.czw.modes.net.OKHttpUtil;
import com.czw.smartkit.R;
import com.czw.smartkit.base.TitleActivity;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.bleModule.SmartKingFilter;
import com.czw.smartkit.bleModule.ota.DfuService;
import com.czw.smartkit.databaseModule.htxOta.HtxOtaFailureEntity;
import com.czw.smartkit.net.domain.FirmwareDTO;
import com.czw.smartkit.netModule.NetManager;
import com.czw.smartkit.preferenceModule.SharePreferenceDevice;
import com.czw.smartkit.preferenceModule.SharePreferenceHtxOTA;
import com.czw.smartkit.preferenceModule.SharedPrefereceWeather;
import com.czw.smartkit.views.ProgressView;
import com.czw.utils.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import ycble.runchinaup.core.BleDeviceFilter;
import ycble.runchinaup.core.BleScanner;
import ycble.runchinaup.core.callback.ScanListener;
import ycble.runchinaup.device.BleDevice;
import ycble.runchinaup.ota.FirmType;
import ycble.runchinaup.ota.OTAHelper;
import ycble.runchinaup.ota.OTAState;
import ycble.runchinaup.ota.callback.OTACallback;
import ycnet.runchinaup.core.ycimpl.data.YCRespListData;
import ycnet.runchinaup.core.ycimpl.response.YCResponseListener;

/* loaded from: classes.dex */
public class OTAActivity extends TitleActivity implements ScanListener<BleDevice> {
    static final int MSG_AUTH_IN_OTA = 2;
    static final int MSG_DOWN_SUCCESS = 1;
    private TextView firmwareTv;
    private ProgressView progressView;
    File otaHexFile = null;
    private OTAHelper otaHelper = OTAHelper.getInstance();
    private BleManager bleManager = BleManager.getBleManager();
    private BleScanner bleScanner = BleScanner.getInstance();
    private FirmType firmType = null;
    private HtxOtaFailureEntity htxOtaFailureEntity = new HtxOtaFailureEntity();
    private boolean isInOtaStep = false;
    private Handler handler = new Handler() { // from class: com.czw.smartkit.setting.OTAActivity.6
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    OTAActivity.this.dismissLoadingDialog();
                    OTAActivity.this.showLoadingDialog(OTAActivity.this.getResources().getString(R.string.init_firmware));
                    postDelayed(new Runnable() { // from class: com.czw.smartkit.setting.OTAActivity.6.1
                        @Override // java.lang.Runnable
                        public void run() {
                            OTAActivity.this.connFlag = false;
                            OTAActivity.this.bleScanner.registerScanListener(OTAActivity.this);
                            OTAActivity.this.initScanFilter();
                            OTAActivity.this.bleScanner.startScan();
                            OTAActivity.this.bleManager.disConn();
                        }
                    }, 2000L);
                    OTAActivity.this.isInOtaStep = false;
                    sendEmptyMessageDelayed(2, 30000L);
                    return;
                case 2:
                    if (OTAActivity.this.isInOtaStep) {
                        return;
                    }
                    OTAActivity.this.dismissLoadingDialog();
                    OTAActivity.this.showFailDialogNotClose(OTAActivity.this.getResources().getString(R.string.ota_failure));
                    OTAActivity.this.reConn();
                    return;
                default:
                    return;
            }
        }
    };
    private boolean connFlag = true;
    OTACallback otaCallback = new AnonymousClass9();
    QMUIDialog reOtaDialog = null;

    /* renamed from: com.czw.smartkit.setting.OTAActivity$9, reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass9 extends OTACallback {
        AnonymousClass9() {
        }

        @Override // ycble.runchinaup.ota.callback.OTACallback
        public void onCurrentState(OTAState oTAState) {
            super.onCurrentState(oTAState);
        }

        @Override // ycble.runchinaup.ota.callback.OTACallback
        public void onFailure(int i, String str) {
            OTAActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.setting.OTAActivity.9.1
                @Override // java.lang.Runnable
                public void run() {
                    OTAActivity.this.dismissLoadingDialog();
                    if (OTAActivity.this.firmType == null || OTAActivity.this.firmType != FirmType.HTX) {
                        OTAActivity.this.showFailDialogNotClose(OTAActivity.this.getResources().getString(R.string.ota_failure));
                        OTAActivity.this.reConn();
                    } else {
                        OTAActivity.this.otaHelper.free();
                        OTAActivity.this.showReHtxOTADialog();
                    }
                }
            });
        }

        @Override // ycble.runchinaup.ota.callback.OTACallback
        public void onProgress(final int i) {
            OTAActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.setting.OTAActivity.9.3
                @Override // java.lang.Runnable
                public void run() {
                    OTAActivity.this.isInOtaStep = true;
                    OTAActivity.this.handler.removeMessages(2);
                    OTAActivity.this.dismissLoadingDialog();
                    OTAActivity.this.progressView.updateProgress(Float.valueOf(i).floatValue() / 100.0f);
                    LogUtil.e("deug====ota--->" + i);
                }
            });
        }

        @Override // ycble.runchinaup.ota.callback.OTACallback
        public void onSuccess() {
            OTAActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.setting.OTAActivity.9.2
                @Override // java.lang.Runnable
                public void run() {
                    SharedPrefereceWeather.clear();
                    OTAActivity.this.dismissLoadingDialog();
                    OTAActivity.this.isInOtaStep = true;
                    OTAActivity.this.handler.removeMessages(2);
                    OTAActivity.this.showSuccessDialog(OTAActivity.this.getResources().getString(R.string.ota_success));
                    OTAActivity.this.reConn();
                    OTAActivity.this.handler.postDelayed(new Runnable() { // from class: com.czw.smartkit.setting.OTAActivity.9.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            OTAActivity.this.finish();
                        }
                    }, 2000L);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downLoadFile(FirmwareDTO firmwareDTO) {
        this.bleManager.setOTAMode(true);
        this.bleManager.writeData(new byte[]{29, 1, 85, -86});
        String download_url = firmwareDTO.getDownload_url();
        final String file_name = firmwareDTO.getFile_name();
        OKHttpUtil.getInstance().downLoad(download_url, new Callback() { // from class: com.czw.smartkit.setting.OTAActivity.5
            @Override // com.squareup.okhttp.Callback
            public void onFailure(Request request, IOException iOException) {
            }

            @Override // com.squareup.okhttp.Callback
            public void onResponse(Response response) throws IOException {
                if (response.code() != 200) {
                    return;
                }
                InputStream byteStream = response.body().byteStream();
                File file = new File(OTAActivity.this.getExternalCacheDir(), OTAActivity.this.getString(R.string.app_name_sk));
                if (file.isDirectory()) {
                    for (File file2 : file.listFiles()) {
                        file2.delete();
                    }
                }
                File file3 = new File(OTAActivity.this.getExternalCacheDir(), "/smartking/firmware");
                if (!file3.exists()) {
                    file3.mkdirs();
                }
                OTAActivity.this.otaHexFile = new File(file3, file_name);
                LogUtil.e("debug-->otaFile" + OTAActivity.this.otaHexFile.getPath());
                if (OTAActivity.this.otaHexFile.exists() && OTAActivity.this.otaHexFile.length() > 0) {
                    OTAActivity.this.handler.sendEmptyMessage(1);
                    return;
                }
                Log.e("debug_firmware_file", OTAActivity.this.otaHexFile.getAbsolutePath());
                FileOutputStream fileOutputStream = new FileOutputStream(OTAActivity.this.otaHexFile);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = byteStream.read(bArr);
                    if (read == -1) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        OTAActivity.this.handler.sendEmptyMessage(1);
                        return;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initScanFilter() {
        this.bleScanner.setBleDeviceFilter(new BleDeviceFilter() { // from class: com.czw.smartkit.setting.OTAActivity.1
            @Override // ycble.runchinaup.core.BleDeviceFilter
            public synchronized boolean filter(BleDevice bleDevice) {
                if (bleDevice == null) {
                    return false;
                }
                if (TextUtils.isEmpty(bleDevice.getName())) {
                    return false;
                }
                if (!bleDevice.getName().equalsIgnoreCase("CZWDFU")) {
                    if (!bleDevice.getName().equalsIgnoreCase("CZWHTX_DFU")) {
                        return false;
                    }
                }
                return true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reConn() {
        this.bleManager.setOTAMode(false);
        this.bleScanner.setBleDeviceFilter(SmartKingFilter.getInstance());
        final BleDevice read = SharePreferenceDevice.read();
        if (read == null || TextUtils.isEmpty(read.getMac())) {
            return;
        }
        this.handler.postDelayed(new Runnable() { // from class: com.czw.smartkit.setting.OTAActivity.8
            @Override // java.lang.Runnable
            public void run() {
                OTAActivity.this.bleManager.connDevice(read.getMac());
            }
        }, 1800L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showReHtxOTADialog() {
        if (this.reOtaDialog == null) {
            this.reOtaDialog = new QMUIDialog.MessageDialogBuilder(this).setTitle(getString(R.string.agin_ota)).addAction(getString(R.string.cancel), new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.setting.OTAActivity.11
                @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
                public void onClick(QMUIDialog qMUIDialog, int i) {
                    qMUIDialog.dismiss();
                    OTAActivity.this.reOtaDialog.dismiss();
                    SharePreferenceHtxOTA.save(OTAActivity.this.htxOtaFailureEntity);
                }
            }).addAction(0, getString(R.string.ok), 2, new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.setting.OTAActivity.10
                @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
                public void onClick(QMUIDialog qMUIDialog, int i) {
                    OTAActivity.this.reOtaDialog.dismiss();
                    OTAActivity.this.progressView.updateProgress(0.0f);
                    SharePreferenceHtxOTA.save(OTAActivity.this.htxOtaFailureEntity);
                    OTAActivity.this.otaHelper.startOTA(OTAActivity.this, OTAActivity.this.otaHexFile.getPath(), new BleDevice("CZWHTX_DFU", OTAActivity.this.htxOtaFailureEntity.getOtaMac()), FirmType.HTX, OTAActivity.this.otaCallback);
                }
            }).create();
            this.reOtaDialog.setCancelable(false);
            this.reOtaDialog.setCanceledOnTouchOutside(false);
        }
        this.reOtaDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sureUpdateFirmware(final FirmwareDTO firmwareDTO) {
        new QMUIDialog.MessageDialogBuilder(this).setTitle(getString(R.string.find_new_firmware_title) + firmwareDTO.getVersion()).setMessage(getString(R.string.find_new_firmware_message)).addAction(getString(R.string.cancel), new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.setting.OTAActivity.4
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
            }
        }).addAction(0, getString(R.string.ok), 2, new QMUIDialogAction.ActionListener() { // from class: com.czw.smartkit.setting.OTAActivity.3
            @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.ActionListener
            public void onClick(QMUIDialog qMUIDialog, int i) {
                qMUIDialog.dismiss();
                OTAActivity.this.downLoadFile(firmwareDTO);
            }
        }).create(2131755258).show();
    }

    @OnClick({R.id.checkoutOtaBtn})
    public void checkoutFirmware(View view) {
        BleDevice read = SharePreferenceDevice.read();
        if (read == null || TextUtils.isEmpty(read.getName())) {
            return;
        }
        showLoadingDialog("");
        NetManager.getNetManager().getFirmware(read.getName(), new YCResponseListener<YCRespListData<FirmwareDTO>>() { // from class: com.czw.smartkit.setting.OTAActivity.2
            @Override // ycnet.runchinaup.core.abs.IResponseListener
            public void onSuccess(final YCRespListData<FirmwareDTO> yCRespListData) {
                OTAActivity.this.runOnUiThread(new Runnable() { // from class: com.czw.smartkit.setting.OTAActivity.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        OTAActivity.this.dismissLoadingDialog();
                        if (yCRespListData == null || yCRespListData.getData() == null || yCRespListData.getData().size() <= 0) {
                            OTAActivity.this.toast(R.string.already_last_version);
                        } else if (FirmwareDTO.canUpdateFirmare(OTAActivity.this.bleManager.getVersionStr(), ((FirmwareDTO) yCRespListData.getData().get(0)).getVersion())) {
                            OTAActivity.this.sureUpdateFirmware((FirmwareDTO) yCRespListData.getData().get(0));
                        } else {
                            OTAActivity.this.toast(R.string.already_last_version);
                        }
                    }
                });
            }
        });
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.titleBar.setTitle(R.string.ota_title);
        this.progressView = (ProgressView) $View(R.id.progress);
        this.firmwareTv = (TextView) $View(R.id.firmwareTv);
        this.firmwareTv.setText(getString(R.string.format_firmware, new Object[]{this.bleManager.getVersionStr()}));
        this.bleScanner.stopScan();
        initScanFilter();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.ui_ota;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.base.BaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.bleScanner.unRegisterScanListener(this);
        this.otaHelper.free();
    }

    @Override // ycble.runchinaup.core.callback.ScanListener
    public void onFailure(int i) {
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // ycble.runchinaup.core.callback.ScanListener
    public void onScan(final BleDevice bleDevice) {
        if (bleDevice == null) {
            return;
        }
        LogUtil.e("debug==>扫描到设备=" + bleDevice.toString());
        if (this.connFlag) {
            return;
        }
        this.bleScanner.stopScan();
        this.handler.postDelayed(new Runnable() { // from class: com.czw.smartkit.setting.OTAActivity.7
            @Override // java.lang.Runnable
            public void run() {
                LogUtil.e("debug==>开始ota" + bleDevice.getName());
                LogUtil.e("debug==>文件长度" + (((float) OTAActivity.this.otaHexFile.length()) / 1000.0f));
                String name = bleDevice.getName();
                if (name.contains("CZWDFU")) {
                    OTAActivity.this.firmType = FirmType.NORDIC;
                    OTAActivity.this.otaHelper.setDfuBaseService(DfuService.class);
                    OTAActivity.this.otaHelper.startOTA(OTAActivity.this, OTAActivity.this.otaHexFile.getPath(), bleDevice, FirmType.NORDIC, OTAActivity.this.otaCallback);
                    return;
                }
                if (name.contains("CZWHTX_DFU")) {
                    OTAActivity.this.firmType = FirmType.HTX;
                    OTAActivity.this.htxOtaFailureEntity.setOtaMac(bleDevice.getMac());
                    OTAActivity.this.otaHelper.startOTA(OTAActivity.this, OTAActivity.this.otaHexFile.getPath(), bleDevice, FirmType.HTX, OTAActivity.this.otaCallback);
                }
            }
        }, 3000L);
    }
}
