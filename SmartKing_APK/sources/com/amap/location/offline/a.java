package com.amap.location.offline;

/* compiled from: OfflineCloudConfig.java */
/* loaded from: classes.dex */
public class a implements IOfflineCloudConfig {
    public IOfflineCloudConfig a;

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public boolean clearAll() {
        if (this.a != null) {
            return this.a.clearAll();
        }
        return false;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public long getConfigTime() {
        if (this.a != null) {
            return this.a.getConfigTime();
        }
        return 0L;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public String[] getContentProviderList() {
        if (this.a != null) {
            return this.a.getContentProviderList();
        }
        return null;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public int getMaxNonWifiRequestTimes() {
        if (this.a != null) {
            return this.a.getMaxNonWifiRequestTimes();
        }
        return 5;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public int getMaxNumPerRequest() {
        if (this.a != null) {
            return this.a.getMaxNumPerRequest();
        }
        return 100;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public int getMaxRequestTimes() {
        if (this.a != null) {
            return this.a.getMaxRequestTimes();
        }
        return 10;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public int getMinWifiNum() {
        if (this.a != null) {
            return this.a.getMinWifiNum();
        }
        return 8;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public boolean getNeedFirstDownload() {
        if (this.a != null) {
            return this.a.getNeedFirstDownload();
        }
        return false;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public int getTrainingThreshold() {
        if (this.a != null) {
            return this.a.getTrainingThreshold();
        }
        return 6;
    }

    @Override // com.amap.location.offline.IOfflineCloudConfig
    public boolean isEnable() {
        if (this.a != null) {
            return this.a.isEnable();
        }
        return true;
    }
}
