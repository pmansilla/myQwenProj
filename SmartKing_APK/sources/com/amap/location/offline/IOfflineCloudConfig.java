package com.amap.location.offline;

/* loaded from: classes.dex */
public interface IOfflineCloudConfig {
    boolean clearAll();

    long getConfigTime();

    String[] getContentProviderList();

    int getMaxNonWifiRequestTimes();

    int getMaxNumPerRequest();

    int getMaxRequestTimes();

    int getMinWifiNum();

    boolean getNeedFirstDownload();

    int getTrainingThreshold();

    boolean isEnable();
}
