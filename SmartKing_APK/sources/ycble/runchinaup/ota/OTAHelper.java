package ycble.runchinaup.ota;

import android.content.Context;
import no.nordicsemi.android.dfu.DfuBaseService;
import ycble.runchinaup.device.BleDevice;
import ycble.runchinaup.log.ycBleLog;
import ycble.runchinaup.ota.absimpl.freqchip.FreqchipOTAHelper;
import ycble.runchinaup.ota.absimpl.htx.HTXOTAHelper;
import ycble.runchinaup.ota.absimpl.nordic.DfuHelper;
import ycble.runchinaup.ota.absimpl.telink.TeLinkOTAHelper;
import ycble.runchinaup.ota.callback.OTACallback;

/* loaded from: classes2.dex */
public class OTAHelper {
    private static final OTAHelper ourInstance = new OTAHelper();
    public Class<? extends DfuBaseService> dfuBaseService;

    private OTAHelper() {
    }

    public static OTAHelper getInstance() {
        return ourInstance;
    }

    public void free() {
        HTXOTAHelper.getInstance().free();
    }

    public void setDfuBaseService(Class<? extends DfuBaseService> cls) {
        this.dfuBaseService = cls;
    }

    public void startOTA(Context context, String str, String str2, String str3, FirmType firmType, OTACallback oTACallback) {
        switch (firmType) {
            case HTX:
                ycBleLog.e("开始汉天下的ota======>");
                HTXOTAHelper hTXOTAHelper = HTXOTAHelper.getInstance();
                hTXOTAHelper.setAppFilePath(str);
                hTXOTAHelper.setDeviceMac(str2);
                hTXOTAHelper.setOtaCallback(oTACallback);
                hTXOTAHelper.startOTA(context);
                return;
            case TELINK:
                TeLinkOTAHelper.getInstance().startOTA(context, str2, str, oTACallback);
                return;
            case FREQCHIP:
                FreqchipOTAHelper.getInstance().startOTA(context, str2, str, oTACallback);
                return;
            default:
                DfuHelper.getDfuHelper().start(context, str, str2, str3, oTACallback, this.dfuBaseService);
                return;
        }
    }

    public void startOTA(Context context, String str, BleDevice bleDevice, FirmType firmType, OTACallback oTACallback) {
        ycBleLog.e("startOTA======>");
        ycBleLog.e("firmType======>" + firmType);
        ycBleLog.e("filePath======>" + str);
        ycBleLog.e("bleDevice======>" + bleDevice);
        ycBleLog.e("otaCallback======>" + oTACallback);
        startOTA(context, str, bleDevice.getMac(), bleDevice.getName(), firmType, oTACallback);
    }
}
