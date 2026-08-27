package ycble.runchinaup.ota.absimpl.htx;

import android.content.Context;
import ycble.runchinaup.ota.callback.OTACallback;

/* loaded from: classes2.dex */
public class HTXOTAHelper {
    private static final HTXOTAHelper ourInstance = new HTXOTAHelper();
    private String appFilePath;
    private String deviceMac;
    private HTXAppOTA appOTA = null;
    private OTACallback otaCallback = null;

    private HTXOTAHelper() {
    }

    public static HTXOTAHelper getInstance() {
        return ourInstance;
    }

    public void free() {
        if (this.appOTA != null) {
            this.appOTA.free();
        }
    }

    public OTACallback getOtaCallback() {
        return this.otaCallback;
    }

    public void setAppFilePath(String str) {
        this.appFilePath = str;
    }

    public void setDeviceMac(String str) {
        this.deviceMac = str;
    }

    public void setOtaCallback(OTACallback oTACallback) {
        this.otaCallback = oTACallback;
    }

    public void startOTA(Context context) {
        this.appOTA = new HTXAppOTA();
        this.appOTA.setOtaCallback(this.otaCallback);
        this.appOTA.setAppFileStringPath(this.appFilePath);
        this.appOTA.setmDeviceAddress(this.deviceMac);
        this.appOTA.startOTA(context);
    }
}
