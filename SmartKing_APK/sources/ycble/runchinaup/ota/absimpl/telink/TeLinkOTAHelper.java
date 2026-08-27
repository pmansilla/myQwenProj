package ycble.runchinaup.ota.absimpl.telink;

import android.content.Context;
import android.util.Log;
import java.io.FileInputStream;
import java.io.IOException;
import ycble.runchinaup.device.BleDevice;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.ota.absimpl.telink.Device;
import ycble.runchinaup.ota.callback.OTACallback;
import ycble.runchinaup.util.BleUtil;

/* loaded from: classes2.dex */
public class TeLinkOTAHelper {
    private static final TeLinkOTAHelper ourInstance = new TeLinkOTAHelper();
    private long startTime = 0;
    private long endTime = 0;

    private TeLinkOTAHelper() {
    }

    public static TeLinkOTAHelper getInstance() {
        return ourInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] readFirmware(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            Log.e("debug==文件路径=>", str.toString());
            byte[] bArr = new byte[fileInputStream.available()];
            fileInputStream.read(bArr);
            Log.e("debug==文件", BleUtil.byte2HexStr(bArr));
            fileInputStream.close();
            return bArr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void startOTA(Context context, String str, final String str2, final OTACallback oTACallback) {
        Device device = new Device(new BleDevice("", str));
        device.setCallback(new Device.Callback() { // from class: ycble.runchinaup.ota.absimpl.telink.TeLinkOTAHelper.1
            @Override // ycble.runchinaup.ota.absimpl.telink.Device.Callback
            public void onConnected(Device device2) {
            }

            @Override // ycble.runchinaup.ota.absimpl.telink.Device.Callback
            public void onDisconnected(Device device2) {
            }

            @Override // ycble.runchinaup.ota.absimpl.telink.Device.Callback
            public void onOtaStateChanged(Device device2, int i) {
                switch (i) {
                    case 0:
                        if (oTACallback != null) {
                            oTACallback.onFailure(30, "failure");
                            return;
                        }
                        return;
                    case 1:
                        ycBleLog.e("成功");
                        TeLinkOTAHelper.this.endTime = System.currentTimeMillis();
                        ycBleLog.e("time:" + (((float) ((TeLinkOTAHelper.this.endTime - TeLinkOTAHelper.this.startTime) / 1000)) / 60.0f));
                        if (oTACallback != null) {
                            oTACallback.onSuccess();
                            return;
                        }
                        return;
                    case 2:
                        if (oTACallback != null) {
                            oTACallback.onProgress(device2.getOtaProgress());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }

            @Override // ycble.runchinaup.ota.absimpl.telink.Device.Callback
            public void onServicesDiscovered(final Device device2) {
                new Thread(new Runnable() { // from class: ycble.runchinaup.ota.absimpl.telink.TeLinkOTAHelper.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        byte[] readFirmware = TeLinkOTAHelper.this.readFirmware(str2);
                        TeLinkOTAHelper.this.startTime = System.currentTimeMillis();
                        device2.startOta(readFirmware);
                    }
                }).start();
            }
        });
        device.connect(context);
    }
}
