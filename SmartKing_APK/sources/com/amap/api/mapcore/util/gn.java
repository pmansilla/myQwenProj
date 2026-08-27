package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.czw.smartkit.R;

/* compiled from: BottomDialog.java */
/* loaded from: classes.dex */
public class gn extends go implements View.OnClickListener {
    private OfflineMapManager a;
    private View b;
    private TextView c;
    private TextView d;
    private TextView e;
    private TextView f;
    private int g;
    private String h;

    public gn(Context context, OfflineMapManager offlineMapManager) {
        super(context);
        this.a = offlineMapManager;
    }

    @Override // com.amap.api.mapcore.util.go
    protected void a() {
        this.b = gt.a(getContext(), R.array.nav_title_no_measure, null);
        setContentView(this.b);
        this.b.setOnClickListener(new View.OnClickListener() { // from class: com.amap.api.mapcore.util.gn.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                gn.this.dismiss();
            }
        });
        this.c = (TextView) this.b.findViewById(R.dimen.DIMEN_107PX);
        this.d = (TextView) this.b.findViewById(R.dimen.DIMEN_108PX);
        this.d.setText("暂停下载");
        this.e = (TextView) this.b.findViewById(R.dimen.DIMEN_109PX);
        this.f = (TextView) this.b.findViewById(R.dimen.DIMEN_10PX);
        this.d.setOnClickListener(this);
        this.e.setOnClickListener(this);
        this.f.setOnClickListener(this);
    }

    public void a(int i, String str) {
        this.c.setText(str);
        if (i == 0) {
            this.d.setText("暂停下载");
            this.d.setVisibility(0);
            this.e.setText("取消下载");
        }
        if (i == 2) {
            this.d.setVisibility(8);
            this.e.setText("取消下载");
        } else if (i == -1 || i == 101 || i == 102 || i == 103) {
            this.d.setText("继续下载");
            this.d.setVisibility(0);
        } else if (i == 3) {
            this.d.setVisibility(0);
            this.d.setText("继续下载");
            this.e.setText("取消下载");
        } else if (i == 4) {
            this.e.setText("删除");
            this.d.setVisibility(8);
        }
        this.g = i;
        this.h = str;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        try {
            int id = view.getId();
            if (id != R.dimen.DIMEN_108PX) {
                if (id != R.dimen.DIMEN_109PX) {
                    if (id == R.dimen.DIMEN_10PX) {
                        dismiss();
                        return;
                    }
                    return;
                } else {
                    if (TextUtils.isEmpty(this.h)) {
                        return;
                    }
                    this.a.remove(this.h);
                    dismiss();
                    return;
                }
            }
            if (this.g == 0) {
                this.d.setText("继续下载");
                this.a.pause();
            } else if (this.g == 3 || this.g == -1 || this.g == 101 || this.g == 102 || this.g == 103) {
                this.d.setText("暂停下载");
                this.a.downloadByCityName(this.h);
            }
            dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
