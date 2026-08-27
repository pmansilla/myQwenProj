package com.amap.location.offline;

import com.amap.location.common.network.IHttpClient;
import com.amap.location.offline.upload.UploadConfig;

/* loaded from: classes.dex */
public class OfflineConfig {
    public static final byte PRODUCT_FLP = 1;
    public static final byte PRODUCT_NLP = 2;
    public static final byte PRODUCT_OPEN_SDK = 4;
    public static final byte PRODUCT_SDK_AMAP = 0;
    public static final byte PRODUCT_SDK_AUTO = 3;
    public static final byte PRODUCT_UNKNOWN = -1;
    public static boolean sUseTestNet = false;
    public byte productId = -1;
    public String packageName = "";
    public String productVersion = "";
    public String adiu = "";
    public String imei = "";
    public String imsi = "";
    public String uuid = "";
    public String license = "";
    public String mapKey = "";
    public boolean locEnable = true;
    public String[] contentProviderList = null;
    public ILocateLogRecorder mLocateLogRecorder = null;
    public IHttpClient httpClient = null;
    public UploadConfig uploadConfig = null;
    public ICoordinateConverter coordinateConverter = null;

    /* loaded from: classes.dex */
    public interface ICoordinateConverter {
        double[] wgsToGcj(double[] dArr);
    }

    /* loaded from: classes.dex */
    public interface ILocateLogRecorder {
        void onLocateSuccess(byte[] bArr);
    }
}
