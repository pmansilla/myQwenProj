package ycble.runchinaup.ota.absimpl.nordic;

import android.content.Context;
import android.os.Build;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.ota.OTAState;
import ycble.runchinaup.ota.callback.OTACallback;

/* loaded from: classes2.dex */
public class DfuHelper {
    public static DfuHelper dfuHelper;
    private OTACallback otaCallback = null;
    private final DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter() { // from class: ycble.runchinaup.ota.absimpl.nordic.DfuHelper.1
        @Override // no.nordicsemi.android.dfu.DfuProgressListenerAdapter, no.nordicsemi.android.dfu.DfuProgressListener
        public void onDeviceConnecting(String str) {
            ycBleLog.e("R.string.dfu_status_connecting");
            if (DfuHelper.this.otaCallback != null) {
                DfuHelper.this.otaCallback.onCurrentState(OTAState.connecting);
            }
        }

        @Override // no.nordicsemi.android.dfu.DfuProgressListenerAdapter, no.nordicsemi.android.dfu.DfuProgressListener
        public void onDeviceDisconnecting(String str) {
            ycBleLog.e("R.string.dfu_status_disconnecting");
        }

        @Override // no.nordicsemi.android.dfu.DfuProgressListenerAdapter, no.nordicsemi.android.dfu.DfuProgressListener
        public void onDfuAborted(String str) {
            ycBleLog.e("R.string.dfu_status_aborted");
            if (DfuHelper.this.otaCallback != null) {
                DfuHelper.this.otaCallback.onFailure(11, "DfuAborted");
            }
        }

        @Override // no.nordicsemi.android.dfu.DfuProgressListenerAdapter, no.nordicsemi.android.dfu.DfuProgressListener
        public void onDfuCompleted(String str) {
            ycBleLog.e("R.string.dfu_status_completed");
            if (DfuHelper.this.otaCallback != null) {
                DfuHelper.this.otaCallback.onSuccess();
            }
        }

        @Override // no.nordicsemi.android.dfu.DfuProgressListenerAdapter, no.nordicsemi.android.dfu.DfuProgressListener
        public void onDfuProcessStarting(String str) {
            ycBleLog.e("R.string.dfu_status_starting");
            if (DfuHelper.this.otaCallback != null) {
                DfuHelper.this.otaCallback.onCurrentState(OTAState.starting);
            }
        }

        @Override // no.nordicsemi.android.dfu.DfuProgressListenerAdapter, no.nordicsemi.android.dfu.DfuProgressListener
        public void onEnablingDfuMode(String str) {
            ycBleLog.e("R.string.dfu_status_switching_to_dfu");
            if (DfuHelper.this.otaCallback != null) {
                DfuHelper.this.otaCallback.onCurrentState(OTAState.switching_to_dfu);
            }
        }

        @Override // no.nordicsemi.android.dfu.DfuProgressListenerAdapter, no.nordicsemi.android.dfu.DfuProgressListener
        public void onError(String str, int i, int i2, String str2) {
            ycBleLog.e("onError==" + i + ";" + str2);
            if (DfuHelper.this.otaCallback != null) {
                DfuHelper.this.otaCallback.onFailure(i, str2);
            }
        }

        @Override // no.nordicsemi.android.dfu.DfuProgressListenerAdapter, no.nordicsemi.android.dfu.DfuProgressListener
        public void onFirmwareValidating(String str) {
            ycBleLog.e("R.string.dfu_status_validating");
        }

        @Override // no.nordicsemi.android.dfu.DfuProgressListenerAdapter, no.nordicsemi.android.dfu.DfuProgressListener
        public void onProgressChanged(String str, int i, float f, float f2, int i2, int i3) {
            ycBleLog.e("R.string.dfu_uploading_percentage" + i);
            if (DfuHelper.this.otaCallback != null) {
                DfuHelper.this.otaCallback.onProgress(i);
            }
        }
    };

    private DfuHelper() {
    }

    public static DfuHelper getDfuHelper() {
        synchronized (Void.class) {
            if (dfuHelper == null) {
                synchronized (Void.class) {
                    dfuHelper = new DfuHelper();
                }
            }
        }
        return dfuHelper;
    }

    public void start(Context context, String str, String str2, String str3, OTACallback oTACallback, Class cls) {
        this.otaCallback = oTACallback;
        DfuServiceInitiator unsafeExperimentalButtonlessServiceInSecureDfuEnabled = new DfuServiceInitiator(str2).setDeviceName(str3).setKeepBond(false).setForceDfu(true).setPacketsReceiptNotificationsEnabled(Build.VERSION.SDK_INT < 23).setPacketsReceiptNotificationsValue(12).setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);
        unsafeExperimentalButtonlessServiceInSecureDfuEnabled.setZip(str);
        if (cls == null) {
            ycBleLog.e("dfuServiceImpl cant be null");
            return;
        }
        unsafeExperimentalButtonlessServiceInSecureDfuEnabled.setForeground(false);
        unsafeExperimentalButtonlessServiceInSecureDfuEnabled.start(context, cls);
        DfuServiceListenerHelper.registerProgressListener(context, this.mDfuProgressListener);
    }
}
