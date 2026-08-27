package com.amap.location.offline;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.model.FPS;
import com.amap.openapi.co;
import java.util.LinkedList;
import java.util.List;
import me.panpf.sketch.uri.ContentUriModel;

/* compiled from: OfflineRemoteProxy.java */
/* loaded from: classes.dex */
public class b {
    private Context a;
    private IOfflineCloudConfig b;
    private ProviderInfo d;
    private List<String> c = new LinkedList();
    private ContentValues e = new ContentValues();

    public b(Context context, OfflineConfig offlineConfig, IOfflineCloudConfig iOfflineCloudConfig) {
        this.a = context;
        this.b = iOfflineCloudConfig;
        a(offlineConfig, iOfflineCloudConfig);
    }

    private void a() {
        this.d = null;
        if (this.c.isEmpty()) {
            return;
        }
        this.c.remove(0);
    }

    private void a(OfflineConfig offlineConfig, IOfflineCloudConfig iOfflineCloudConfig) {
        this.c.clear();
        int i = 0;
        if (iOfflineCloudConfig != null && iOfflineCloudConfig.getContentProviderList() != null) {
            String[] contentProviderList = iOfflineCloudConfig.getContentProviderList();
            int length = contentProviderList.length;
            while (i < length) {
                this.c.add(contentProviderList[i]);
                i++;
            }
            return;
        }
        if (offlineConfig == null || offlineConfig.contentProviderList == null) {
            return;
        }
        String[] strArr = offlineConfig.contentProviderList;
        int length2 = strArr.length;
        while (i < length2) {
            this.c.add(strArr[i]);
            i++;
        }
    }

    public co.a a(FPS fps, int i, String str) {
        Cursor cursor;
        while (a(str)) {
            Cursor cursor2 = null;
            r0 = null;
            r0 = null;
            co.a aVar = null;
            try {
                cursor = this.a.getContentResolver().query(Uri.parse(ContentUriModel.SCHEME + this.d.authority), null, null, co.a(str, fps, null, i), null);
                try {
                    co.a a = co.a(cursor);
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception unused) {
                        }
                    }
                    aVar = a;
                } catch (Exception unused2) {
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception unused3) {
                        }
                    }
                    if (aVar == null) {
                    }
                    a();
                } catch (Throwable th) {
                    th = th;
                    cursor2 = cursor;
                    if (cursor2 != null) {
                        try {
                            cursor2.close();
                        } catch (Exception unused4) {
                        }
                    }
                    throw th;
                }
            } catch (Exception unused5) {
                cursor = null;
            } catch (Throwable th2) {
                th = th2;
            }
            if (aVar == null && aVar.a) {
                return aVar;
            }
            a();
        }
        return new co.a();
    }

    public void a(OfflineConfig offlineConfig) {
        a(offlineConfig, this.b);
    }

    public boolean a(FPS fps, AmapLoc amapLoc, String str) {
        boolean z = false;
        if (!a(str)) {
            return false;
        }
        try {
            if (this.a.getContentResolver().update(Uri.parse(ContentUriModel.SCHEME + this.d.authority), this.e, null, co.a(str, fps, amapLoc, 0)) == 1) {
                z = true;
            }
        } catch (Exception unused) {
        }
        if (z) {
            return true;
        }
        a();
        return a(str);
    }

    public boolean a(String str) {
        if (this.d != null) {
            if (str == null || !str.equals(this.d.packageName)) {
                return true;
            }
            a();
        }
        while (!this.c.isEmpty()) {
            try {
                ProviderInfo resolveContentProvider = this.a.getPackageManager().resolveContentProvider(this.c.get(0), 0);
                if (resolveContentProvider != null && (str == null || !str.equals(resolveContentProvider.packageName))) {
                    this.d = resolveContentProvider;
                    return true;
                }
            } catch (Exception unused) {
            }
            this.c.remove(0);
        }
        return false;
    }
}
