package com.amap.api.mapcore.util;

import android.app.Activity;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.amap.api.maps.offlinemap.OfflineMapActivity;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import java.util.ArrayList;
import java.util.List;

/* compiled from: SearchListAdapter.java */
/* loaded from: classes.dex */
public class gm extends BaseAdapter {
    private List<OfflineMapCity> a = new ArrayList();
    private OfflineMapManager b;
    private Activity c;

    /* compiled from: SearchListAdapter.java */
    /* loaded from: classes.dex */
    public final class a {
        public TextView a;
        public TextView b;
        public TextView c;
        public ImageView d;

        public a() {
        }
    }

    public gm(List<OfflineMapProvince> list, OfflineMapManager offlineMapManager, OfflineMapActivity offlineMapActivity) {
        this.b = offlineMapManager;
        this.c = offlineMapActivity;
    }

    public void a(List<OfflineMapCity> list) {
        this.a = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.a.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.a.get(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00e1 A[Catch: Exception -> 0x00f9, TryCatch #1 {Exception -> 0x00f9, blocks: (B:2:0x0000, B:4:0x000a, B:9:0x0054, B:11:0x0083, B:13:0x00a6, B:14:0x00a9, B:18:0x00ad, B:19:0x00ba, B:20:0x00c7, B:21:0x00d4, B:22:0x00e1, B:23:0x00ee, B:28:0x004e), top: B:1:0x0000 }] */
    @Override // android.widget.Adapter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.view.View getView(int r7, android.view.View r8, android.view.ViewGroup r9) {
        /*
            Method dump skipped, instructions count: 280
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.gm.getView(int, android.view.View, android.view.ViewGroup):android.view.View");
    }
}
