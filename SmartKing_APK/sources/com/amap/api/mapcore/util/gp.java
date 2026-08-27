package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.internal.view.SupportMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.DownloadProgressView;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.czw.smartkit.R;

/* compiled from: OfflineChild.java */
/* loaded from: classes.dex */
public class gp implements View.OnClickListener {
    private Context b;
    private TextView c;
    private TextView d;
    private ImageView e;
    private TextView f;
    private OfflineMapManager g;
    private OfflineMapCity h;
    private View k;
    private DownloadProgressView l;
    private int a = 0;
    private boolean i = false;
    private Handler j = new Handler() { // from class: com.amap.api.mapcore.util.gp.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                gp.this.a(message.arg1, message.arg2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public gp(Context context, OfflineMapManager offlineMapManager) {
        this.b = context;
        b();
        this.g = offlineMapManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i, int i2) throws Exception {
        if (this.a != 2 || i2 <= 3 || i2 >= 100) {
            this.l.setVisibility(8);
        } else {
            this.l.setVisibility(0);
            this.l.setProgress(i2);
        }
        switch (i) {
            case -1:
                e();
                return;
            case 0:
                if (this.a != 1) {
                    e(i2);
                    return;
                }
                this.e.setVisibility(8);
                this.f.setText("下载中");
                this.f.setTextColor(Color.parseColor("#4287ff"));
                return;
            case 1:
                d(i2);
                return;
            case 2:
                b(i2);
                return;
            case 3:
                c(i2);
                return;
            case 4:
                f();
                return;
            case 5:
                return;
            case 6:
                c();
                return;
            case 7:
                d();
                return;
            default:
                switch (i) {
                    case 101:
                    case 102:
                    case 103:
                        e();
                        return;
                    default:
                        return;
                }
        }
    }

    private void a(int i, int i2, boolean z) {
        if (this.h != null) {
            this.h.setState(i);
            this.h.setCompleteCode(i2);
        }
        Message message = new Message();
        message.arg1 = i;
        message.arg2 = i2;
        this.j.sendMessage(message);
    }

    private void b() {
        this.k = gt.a(this.b, R.array.smssdk_country_group_a, null);
        this.l = (DownloadProgressView) this.k.findViewById(R.dimen.DIMEN_115PX);
        this.c = (TextView) this.k.findViewById(R.dimen.DIMEN_110PX);
        this.d = (TextView) this.k.findViewById(R.dimen.DIMEN_114PX);
        this.e = (ImageView) this.k.findViewById(R.dimen.DIMEN_113PX);
        this.f = (TextView) this.k.findViewById(R.dimen.DIMEN_112PX);
        this.e.setOnClickListener(this);
    }

    private void b(int i) {
        if (this.a == 1) {
            this.e.setVisibility(8);
            this.f.setVisibility(0);
            this.f.setText("等待中");
            this.f.setTextColor(Color.parseColor("#4287ff"));
            return;
        }
        this.f.setVisibility(0);
        this.e.setVisibility(8);
        this.f.setTextColor(Color.parseColor("#4287ff"));
        this.f.setText("等待中");
    }

    private void c() {
        this.f.setVisibility(8);
        this.e.setVisibility(0);
        this.e.setImageResource(2130837506);
    }

    private void c(int i) {
        this.f.setVisibility(0);
        this.e.setVisibility(8);
        this.f.setTextColor(-7829368);
        this.f.setText("暂停");
    }

    private void d() {
        this.f.setVisibility(0);
        this.e.setVisibility(0);
        this.e.setImageResource(2130837506);
        this.f.setText("已下载-有更新");
    }

    private void d(int i) {
        if (this.a == 1) {
            return;
        }
        this.f.setVisibility(0);
        this.e.setVisibility(8);
        this.f.setText("解压中");
        this.f.setTextColor(Color.parseColor("#898989"));
    }

    private void e() {
        this.f.setVisibility(0);
        this.e.setVisibility(8);
        this.f.setTextColor(SupportMenu.CATEGORY_MASK);
        this.f.setText("下载出现异常");
    }

    private void e(int i) {
        if (this.h == null) {
            return;
        }
        this.f.setVisibility(0);
        this.f.setText("下载中");
        this.e.setVisibility(8);
        this.f.setTextColor(Color.parseColor("#4287ff"));
    }

    private void f() {
        this.f.setVisibility(0);
        this.e.setVisibility(8);
        this.f.setText("已下载");
        this.f.setTextColor(Color.parseColor("#898989"));
    }

    private synchronized void g() {
        this.g.pause();
        this.g.restart();
    }

    private synchronized boolean h() {
        try {
            this.g.downloadByCityName(this.h.getCity());
        } catch (AMapException e) {
            e.printStackTrace();
            Toast.makeText(this.b, e.getErrorMessage(), 0).show();
            return false;
        }
        return true;
    }

    public View a() {
        return this.k;
    }

    public void a(int i) {
        this.a = i;
    }

    public void a(OfflineMapCity offlineMapCity) {
        if (offlineMapCity != null) {
            this.h = offlineMapCity;
            this.c.setText(offlineMapCity.getCity());
            double size = offlineMapCity.getSize();
            Double.isNaN(size);
            double d = (int) (((size / 1024.0d) / 1024.0d) * 100.0d);
            Double.isNaN(d);
            this.d.setText(String.valueOf(d / 100.0d) + " M");
            a(this.h.getState(), this.h.getcompleteCode(), this.i);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        try {
            if (!fr.d(this.b)) {
                Toast.makeText(this.b, "无网络连接", 0).show();
                return;
            }
            if (this.h != null) {
                int state = this.h.getState();
                int i = this.h.getcompleteCode();
                if (state != 4) {
                    switch (state) {
                        case 0:
                            g();
                            c(i);
                            return;
                        case 1:
                            return;
                        default:
                            if (h()) {
                                b(i);
                                return;
                            } else {
                                e();
                                return;
                            }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
