package com.amap.location.offline.upload;

import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;
import org.apache.commons.lang.time.DateUtils;

/* loaded from: classes.dex */
public class UploadConfig {
    public long bufferSize = 100;
    public long maxDbSize = 100000;
    public long expireTimeInDb = 864000000;
    public long storePeriod = DateUtils.MILLIS_PER_MINUTE;
    public long uploadPeriod = DateUtils.MILLIS_PER_MINUTE;
    public long sizePerRequest = 1000;
    public long maxSizePerDay = BootloaderScanner.TIMEOUT;
    public boolean nonWifiEnable = false;
}
