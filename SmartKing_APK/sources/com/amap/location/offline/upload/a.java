package com.amap.location.offline.upload;

import android.content.Context;
import com.amap.location.common.HeaderConfig;
import com.amap.location.offline.IOfflineCloudConfig;
import com.amap.location.offline.OfflineConfig;
import com.amap.openapi.dk;
import com.amap.openapi.dl;
import com.amap.openapi.dp;
import com.amap.openapi.dq;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: UpTunnelWrapper.java */
/* loaded from: classes.dex */
public class a {
    public static void a(int i) {
        dl.a(i);
    }

    public static void a(int i, byte[] bArr) {
        dl.a(i, bArr);
    }

    public static void a(Context context, final OfflineConfig offlineConfig, IOfflineCloudConfig iOfflineCloudConfig) {
        if (offlineConfig.productId == 4 && offlineConfig.locEnable && iOfflineCloudConfig.isEnable()) {
            HeaderConfig.setProductId((byte) 4);
            HeaderConfig.setProductVerion(offlineConfig.productVersion);
            HeaderConfig.setProcessName(offlineConfig.packageName);
            com.amap.location.common.a.b(context, offlineConfig.adiu);
            HeaderConfig.setMapkey(offlineConfig.mapKey);
            HeaderConfig.setLicense(offlineConfig.license);
            dk dkVar = new dk();
            dkVar.f = offlineConfig.httpClient;
            dkVar.b = new dq() { // from class: com.amap.location.offline.upload.a.1
                @Override // com.amap.openapi.dq
                public final int a() {
                    return 10;
                }

                @Override // com.amap.openapi.dr
                public final long a(int i) {
                    if (OfflineConfig.this.uploadConfig != null) {
                        return OfflineConfig.this.uploadConfig.sizePerRequest;
                    }
                    return 1000L;
                }

                @Override // com.amap.openapi.dr
                public final long b(int i) {
                    return OfflineConfig.this.uploadConfig != null ? OfflineConfig.this.uploadConfig.maxSizePerDay : BootloaderScanner.TIMEOUT;
                }

                @Override // com.amap.openapi.dr
                public final void b() {
                }

                @Override // com.amap.openapi.dr
                public final long c() {
                    if (OfflineConfig.this.uploadConfig != null) {
                        return OfflineConfig.this.uploadConfig.bufferSize;
                    }
                    return 100L;
                }

                @Override // com.amap.openapi.dr
                public final boolean c(int i) {
                    if (i == 1) {
                        return true;
                    }
                    if (i != 0 || OfflineConfig.this.uploadConfig == null) {
                        return false;
                    }
                    return OfflineConfig.this.uploadConfig.nonWifiEnable;
                }

                @Override // com.amap.openapi.dr
                public final long d() {
                    if (OfflineConfig.this.uploadConfig != null) {
                        return OfflineConfig.this.uploadConfig.uploadPeriod;
                    }
                    return 300000L;
                }

                @Override // com.amap.openapi.dr
                public final long e() {
                    return OfflineConfig.this.uploadConfig != null ? OfflineConfig.this.uploadConfig.storePeriod : DateUtils.MILLIS_PER_MINUTE;
                }

                @Override // com.amap.openapi.dr
                public final int f() {
                    return 10000;
                }

                @Override // com.amap.openapi.dr
                public final long g() {
                    if (OfflineConfig.this.uploadConfig != null) {
                        return OfflineConfig.this.uploadConfig.maxDbSize;
                    }
                    return 100000L;
                }

                @Override // com.amap.openapi.dr
                public final long h() {
                    if (OfflineConfig.this.uploadConfig != null) {
                        return OfflineConfig.this.uploadConfig.expireTimeInDb;
                    }
                    return 864000000L;
                }
            };
            dkVar.a = new dp() { // from class: com.amap.location.offline.upload.a.2
                @Override // com.amap.openapi.dp
                public final long a() {
                    return 10L;
                }

                @Override // com.amap.openapi.dr
                public final long a(int i) {
                    if (OfflineConfig.this.uploadConfig != null) {
                        return OfflineConfig.this.uploadConfig.sizePerRequest;
                    }
                    return 1000L;
                }

                @Override // com.amap.openapi.dr
                public final long b(int i) {
                    return OfflineConfig.this.uploadConfig != null ? OfflineConfig.this.uploadConfig.maxSizePerDay : BootloaderScanner.TIMEOUT;
                }

                @Override // com.amap.openapi.dr
                public final void b() {
                }

                @Override // com.amap.openapi.dr
                public final long c() {
                    if (OfflineConfig.this.uploadConfig != null) {
                        return OfflineConfig.this.uploadConfig.bufferSize;
                    }
                    return 100L;
                }

                @Override // com.amap.openapi.dr
                public final boolean c(int i) {
                    if (i == 1) {
                        return true;
                    }
                    if (i != 0 || OfflineConfig.this.uploadConfig == null) {
                        return false;
                    }
                    return OfflineConfig.this.uploadConfig.nonWifiEnable;
                }

                @Override // com.amap.openapi.dr
                public final long d() {
                    if (OfflineConfig.this.uploadConfig != null) {
                        return OfflineConfig.this.uploadConfig.uploadPeriod;
                    }
                    return 300000L;
                }

                @Override // com.amap.openapi.dr
                public final long e() {
                    return OfflineConfig.this.uploadConfig != null ? OfflineConfig.this.uploadConfig.storePeriod : DateUtils.MILLIS_PER_MINUTE;
                }

                @Override // com.amap.openapi.dr
                public final int f() {
                    return 10000;
                }

                @Override // com.amap.openapi.dr
                public final long g() {
                    if (OfflineConfig.this.uploadConfig != null) {
                        return OfflineConfig.this.uploadConfig.maxDbSize;
                    }
                    return 100000L;
                }

                @Override // com.amap.openapi.dr
                public final long h() {
                    if (OfflineConfig.this.uploadConfig != null) {
                        return OfflineConfig.this.uploadConfig.expireTimeInDb;
                    }
                    return 864000000L;
                }
            };
            dl.a(context, dkVar);
        }
    }

    public static void a(OfflineConfig offlineConfig) {
        if (offlineConfig == null || offlineConfig.productId != 4) {
            return;
        }
        dl.a();
    }
}
