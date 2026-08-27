package com.amap.api.mapcore.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.amap.api.maps.offlinemap.DownLoadExpandListView;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import com.czw.smartkit.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/* compiled from: OfflineMapPage.java */
/* loaded from: classes.dex */
public class gr extends com.amap.api.offlineservice.a implements TextWatcher, View.OnTouchListener, AbsListView.OnScrollListener, OfflineMapManager.OfflineLoadedListener, OfflineMapManager.OfflineMapDownloadListener {
    private ImageView b;
    private RelativeLayout c;
    private DownLoadExpandListView d;
    private ListView e;
    private ExpandableListView f;
    private ImageView g;
    private ImageView h;
    private AutoCompleteTextView i;
    private RelativeLayout j;
    private RelativeLayout k;
    private ImageView l;
    private ImageView m;
    private RelativeLayout n;
    private gl p;
    private gk r;
    private gm s;
    private gn x;
    private List<OfflineMapProvince> o = new ArrayList();
    private OfflineMapManager q = null;
    private boolean t = true;
    private boolean u = true;
    private int v = -1;
    private long w = 0;
    private boolean y = true;

    private void j() {
        try {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.m.getLayoutParams();
            layoutParams.leftMargin = a(18.0f);
            this.m.setLayoutParams(layoutParams);
            this.i.setPadding(a(30.0f), 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void k() {
        l();
        this.s = new gm(this.o, this.q, this.a);
        this.e.setAdapter((ListAdapter) this.s);
    }

    private void l() {
        ArrayList<OfflineMapProvince> offlineMapProvinceList = this.q.getOfflineMapProvinceList();
        this.o.clear();
        this.o.add(null);
        ArrayList<OfflineMapCity> arrayList = new ArrayList<>();
        ArrayList<OfflineMapCity> arrayList2 = new ArrayList<>();
        ArrayList<OfflineMapCity> arrayList3 = new ArrayList<>();
        for (int i = 0; i < offlineMapProvinceList.size(); i++) {
            OfflineMapProvince offlineMapProvince = offlineMapProvinceList.get(i);
            if (offlineMapProvince.getCityList().size() != 1) {
                this.o.add(i + 1, offlineMapProvince);
            } else {
                String provinceName = offlineMapProvince.getProvinceName();
                if (provinceName.contains("香港")) {
                    arrayList2.addAll(offlineMapProvince.getCityList());
                } else if (provinceName.contains("澳门")) {
                    arrayList2.addAll(offlineMapProvince.getCityList());
                } else if (provinceName.contains("全国概要图")) {
                    arrayList3.addAll(0, offlineMapProvince.getCityList());
                } else {
                    arrayList3.addAll(offlineMapProvince.getCityList());
                }
            }
        }
        OfflineMapProvince offlineMapProvince2 = new OfflineMapProvince();
        offlineMapProvince2.setProvinceName("基本功能包+直辖市");
        offlineMapProvince2.setCityList(arrayList3);
        this.o.set(0, offlineMapProvince2);
        OfflineMapProvince offlineMapProvince3 = new OfflineMapProvince();
        offlineMapProvince3.setProvinceName("直辖市");
        offlineMapProvince3.setCityList(arrayList);
        OfflineMapProvince offlineMapProvince4 = new OfflineMapProvince();
        offlineMapProvince4.setProvinceName("港澳");
        offlineMapProvince4.setCityList(arrayList2);
        this.o.add(offlineMapProvince4);
    }

    private void m() {
        if (this.i == null || !this.i.isFocused()) {
            return;
        }
        this.i.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) this.a.getSystemService("input_method");
        if (inputMethodManager != null ? inputMethodManager.isActive() : false) {
            inputMethodManager.hideSoftInputFromWindow(this.i.getWindowToken(), 2);
        }
    }

    @Override // com.amap.api.offlineservice.a
    public void a() {
        View a = gt.a(this.a, R.array.nav_title, null);
        this.d = (DownLoadExpandListView) a.findViewById(R.dimen.DIMEN_103PX);
        this.d.setOnTouchListener(this);
        this.j = (RelativeLayout) a.findViewById(R.dimen.DIMEN_100PX);
        this.g = (ImageView) a.findViewById(R.dimen.DIMEN_102PX);
        this.j.setOnClickListener(this.a);
        this.k = (RelativeLayout) a.findViewById(R.dimen.DIMEN_105PX);
        this.h = (ImageView) a.findViewById(R.dimen.DIMEN_106PX);
        this.k.setOnClickListener(this.a);
        this.n = (RelativeLayout) a.findViewById(R.dimen.DIMEN_104PX);
        this.b = (ImageView) this.c.findViewById(R.dimen.DIMEN_11PX);
        this.b.setOnClickListener(this.a);
        this.m = (ImageView) this.c.findViewById(R.dimen.DIMEN_121PX);
        this.l = (ImageView) this.c.findViewById(R.dimen.DIMEN_123PX);
        this.l.setOnClickListener(new View.OnClickListener() { // from class: com.amap.api.mapcore.util.gr.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    gr.this.i.setText("");
                    gr.this.l.setVisibility(8);
                    gr.this.a(false);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) gr.this.m.getLayoutParams();
                    layoutParams.leftMargin = gr.this.a(95.0f);
                    gr.this.m.setLayoutParams(layoutParams);
                    gr.this.i.setPadding(gr.this.a(105.0f), 0, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.c.findViewById(R.dimen.DIMEN_124PX).setOnTouchListener(this);
        this.i = (AutoCompleteTextView) this.c.findViewById(R.dimen.DIMEN_122PX);
        this.i.addTextChangedListener(this);
        this.i.setOnTouchListener(this);
        this.e = (ListView) this.c.findViewById(R.dimen.DIMEN_126PX);
        this.f = (ExpandableListView) this.c.findViewById(R.dimen.DIMEN_125PX);
        this.f.addHeaderView(a);
        this.f.setOnTouchListener(this);
        this.f.setOnScrollListener(this);
        this.q = new OfflineMapManager(this.a, this);
        this.q.setOnOfflineLoadedListener(this);
        l();
        this.p = new gl(this.o, this.q, this.a);
        this.f.setAdapter(this.p);
        this.f.setOnGroupCollapseListener(this.p);
        this.f.setOnGroupExpandListener(this.p);
        this.f.setGroupIndicator(null);
        if (this.t) {
            this.h.setBackgroundResource(R.animator.design_appbar_state_list_animator);
            this.f.setVisibility(0);
        } else {
            this.h.setBackgroundResource(2130837508);
            this.f.setVisibility(8);
        }
        if (this.u) {
            this.g.setBackgroundResource(R.animator.design_appbar_state_list_animator);
            this.d.setVisibility(0);
        } else {
            this.g.setBackgroundResource(2130837508);
            this.d.setVisibility(8);
        }
    }

    @Override // com.amap.api.offlineservice.a
    public void a(View view) {
        try {
            int id = view.getId();
            if (id == R.dimen.DIMEN_11PX) {
                this.a.closeScr();
            } else if (id == R.dimen.DIMEN_100PX) {
                if (this.u) {
                    this.d.setVisibility(8);
                    this.g.setBackgroundResource(2130837508);
                    this.u = false;
                } else {
                    this.d.setVisibility(0);
                    this.g.setBackgroundResource(R.animator.design_appbar_state_list_animator);
                    this.u = true;
                }
            } else if (id == R.dimen.DIMEN_105PX) {
                if (this.t) {
                    this.p.b();
                    this.h.setBackgroundResource(2130837508);
                    this.t = false;
                } else {
                    this.p.a();
                    this.h.setBackgroundResource(R.animator.design_appbar_state_list_animator);
                    this.t = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void a(OfflineMapCity offlineMapCity) {
        try {
            if (this.x == null) {
                this.x = new gn(this.a, this.q);
            }
            this.x.a(offlineMapCity.getState(), offlineMapCity.getCity());
            this.x.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void a(boolean z) {
        if (z) {
            this.j.setVisibility(8);
            this.k.setVisibility(8);
            this.d.setVisibility(8);
            this.f.setVisibility(8);
            this.n.setVisibility(8);
            this.e.setVisibility(0);
            return;
        }
        this.j.setVisibility(0);
        this.k.setVisibility(0);
        this.n.setVisibility(0);
        this.d.setVisibility(this.u ? 0 : 8);
        this.f.setVisibility(this.t ? 0 : 8);
        this.e.setVisibility(8);
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
    }

    @Override // com.amap.api.offlineservice.a
    public boolean b() {
        try {
            if (this.e.getVisibility() == 0) {
                this.i.setText("");
                this.l.setVisibility(8);
                a(false);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.b();
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // com.amap.api.offlineservice.a
    public RelativeLayout c() {
        if (this.c == null) {
            this.c = (RelativeLayout) gt.a(this.a, R.array.smssdk_country_group_c, null);
        }
        return this.c;
    }

    @Override // com.amap.api.offlineservice.a
    public void d() {
        this.q.destroy();
    }

    public void i() {
        this.r = new gk(this.a, this, this.q, this.o);
        this.d.setAdapter(this.r);
        this.r.notifyDataSetChanged();
    }

    @Override // com.amap.api.maps.offlinemap.OfflineMapManager.OfflineMapDownloadListener
    public void onCheckUpdate(boolean z, String str) {
    }

    @Override // com.amap.api.maps.offlinemap.OfflineMapManager.OfflineMapDownloadListener
    public void onDownload(int i, int i2, String str) {
        switch (i) {
            case -1:
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                break;
            default:
                switch (i) {
                    case 101:
                        try {
                            Toast.makeText(this.a, "网络异常", 0).show();
                            this.q.pause();
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                }
        }
        if (i == 2) {
            this.r.a();
        }
        if (this.v == i) {
            if (System.currentTimeMillis() - this.w > 1200) {
                Log.i("SHIXIN", "UPDATE_DOWNLOAD_LIST");
                if (this.y) {
                    this.r.notifyDataSetChanged();
                }
                this.w = System.currentTimeMillis();
                return;
            }
            return;
        }
        if (this.p != null) {
            this.p.notifyDataSetChanged();
        }
        if (this.r != null) {
            this.r.notifyDataSetChanged();
        }
        if (this.s != null) {
            this.s.notifyDataSetChanged();
        }
        this.v = i;
    }

    @Override // com.amap.api.maps.offlinemap.OfflineMapManager.OfflineMapDownloadListener
    public void onRemove(boolean z, String str, String str2) {
        if (this.r != null) {
            this.r.b();
        }
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 2) {
            this.y = false;
        } else {
            this.y = true;
        }
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (TextUtils.isEmpty(charSequence)) {
            a(false);
            this.l.setVisibility(8);
            return;
        }
        this.l.setVisibility(0);
        ArrayList arrayList = new ArrayList();
        if (this.o != null && this.o.size() > 0) {
            ArrayList<OfflineMapCity> arrayList2 = new ArrayList();
            Iterator<OfflineMapProvince> it = this.o.iterator();
            while (it.hasNext()) {
                arrayList2.addAll(it.next().getCityList());
            }
            for (OfflineMapCity offlineMapCity : arrayList2) {
                String city = offlineMapCity.getCity();
                String pinyin = offlineMapCity.getPinyin();
                String jianpin = offlineMapCity.getJianpin();
                if (charSequence.length() == 1) {
                    if (jianpin.startsWith(String.valueOf(charSequence))) {
                        arrayList.add(offlineMapCity);
                    }
                } else if (jianpin.startsWith(String.valueOf(charSequence)) || pinyin.startsWith(String.valueOf(charSequence)) || city.startsWith(String.valueOf(charSequence))) {
                    arrayList.add(offlineMapCity);
                }
            }
        }
        if (arrayList.size() <= 0) {
            Toast.makeText(this.a, "未找到相关城市", 0).show();
            return;
        }
        a(true);
        Collections.sort(arrayList, new Comparator<OfflineMapCity>() { // from class: com.amap.api.mapcore.util.gr.2
            @Override // java.util.Comparator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public int compare(OfflineMapCity offlineMapCity2, OfflineMapCity offlineMapCity3) {
                char[] charArray = offlineMapCity2.getJianpin().toCharArray();
                char[] charArray2 = offlineMapCity3.getJianpin().toCharArray();
                return (charArray[0] >= charArray2[0] && charArray[1] >= charArray2[1]) ? 0 : 1;
            }
        });
        this.s.a(arrayList);
        this.s.notifyDataSetChanged();
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        m();
        if (view.getId() != R.dimen.DIMEN_122PX) {
            return false;
        }
        j();
        return false;
    }

    @Override // com.amap.api.maps.offlinemap.OfflineMapManager.OfflineLoadedListener
    public void onVerifyComplete() {
        k();
        i();
    }
}
