package ycble.runchinaup.ota.absimpl.freqchip;

import android.content.Context;
import ycble.runchinaup.ota.callback.OTACallback;

/* loaded from: classes2.dex */
public class FreqchipOTAHelper {
    private static final FreqchipOTAHelper ourInstance = new FreqchipOTAHelper();
    private OTAImpl otaImpl = null;

    private FreqchipOTAHelper() {
    }

    public static FreqchipOTAHelper getInstance() {
        return ourInstance;
    }

    public void startOTA(Context context, String str, String str2, OTACallback oTACallback) {
        this.otaImpl = new OTAImpl();
        this.otaImpl.setFilePath(str2);
        this.otaImpl.setOtaCallback(oTACallback);
        this.otaImpl.connDevice(str);
    }
}
