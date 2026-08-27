package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.amap.api.mapcore.util.ed;
import com.amap.api.maps.model.Tile;
import com.amap.api.maps.model.TileProvider;

/* compiled from: AbstractImageFetcher.java */
/* loaded from: classes.dex */
public class fs extends ft {
    private TileProvider e;

    public fs(Context context, int i, int i2) {
        super(context, i, i2);
        this.e = null;
        a(context);
    }

    private void a(Context context) {
        b(context);
    }

    private void b(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
            return;
        }
        activeNetworkInfo.isConnectedOrConnecting();
    }

    private Bitmap c(ed.a aVar) {
        try {
            Tile tile = this.e.getTile(aVar.a, aVar.b, aVar.c);
            if (tile == null || tile == TileProvider.NO_TILE) {
                return null;
            }
            return BitmapFactory.decodeByteArray(tile.data, 0, tile.data.length);
        } catch (Throwable unused) {
            return null;
        }
    }

    @Override // com.amap.api.mapcore.util.ft, com.amap.api.mapcore.util.fu
    protected Bitmap a(Object obj) {
        return c((ed.a) obj);
    }

    public void a(TileProvider tileProvider) {
        this.e = tileProvider;
    }
}
