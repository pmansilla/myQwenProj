package com.loc;

import com.amap.location.offline.IOfflineCloudConfig;

/* compiled from: OfflineCloudConfig.java */
/* loaded from: classes.dex */
public final class cn implements IOfflineCloudConfig {
    @Override // com.amap.location.offline.IOfflineCloudConfig
    public final boolean clearAll() {
        return false;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public final long getConfigTime() {
        return 0L;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public final String[] getContentProviderList() {
        return null;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public final int getMaxNonWifiRequestTimes() {
        return 5;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public final int getMaxNumPerRequest() {
        return 100;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public final int getMaxRequestTimes() {
        return 10;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public final int getMinWifiNum() {
        return 8;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public final boolean getNeedFirstDownload() {
        return false;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public final int getTrainingThreshold() {
        return 6;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public final boolean isEnable() {
        return true;
    }
}
