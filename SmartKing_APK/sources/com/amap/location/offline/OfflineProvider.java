package com.amap.location.offline;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.amap.location.common.model.AmapLoc;
import com.amap.location.common.model.FPS;
import com.amap.openapi.co;

/* loaded from: classes.dex */
public class OfflineProvider extends ContentProvider {
    private Context a;
    private OfflineManager b;

    private synchronized void a() {
        if (this.b == null) {
            this.b = OfflineManager.getInstance();
            this.b.init(this.a, new OfflineConfig(), new a());
        }
    }

    @Override // android.content.ContentProvider
    public int delete(@NonNull Uri uri, @Nullable String str, @Nullable String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    @Nullable
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        synchronized (this) {
            this.a = getContext().getApplicationContext();
        }
        return true;
    }

    @Override // android.content.ContentProvider
    @Nullable
    public Cursor query(@NonNull Uri uri, @Nullable String[] strArr, @Nullable String str, @Nullable String[] strArr2, @Nullable String str2) {
        int i;
        a();
        if (!this.b.isEnable()) {
            return co.a(false, (AmapLoc) null);
        }
        if (strArr2 == null || strArr2.length < 5) {
            return co.a(true, (AmapLoc) null);
        }
        String str3 = strArr2[0];
        FPS a = co.a(strArr2[1], strArr2[2]);
        try {
            i = Integer.parseInt(strArr2[4]);
        } catch (Exception unused) {
            i = 0;
        }
        return co.a(true, a != null ? this.b.getLocation(a, i, false, str3) : null);
    }

    @Override // android.content.ContentProvider
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String str, @Nullable String[] strArr) {
        a();
        if (!this.b.isEnable()) {
            return 0;
        }
        if (strArr != null && strArr.length >= 5) {
            String str2 = strArr[0];
            FPS a = co.a(strArr[1], strArr[2]);
            AmapLoc a2 = co.a(strArr[3]);
            if (a != null && a2 != null) {
                this.b.correctLocation(a, a2, str2);
            }
        }
        return 1;
    }
}
