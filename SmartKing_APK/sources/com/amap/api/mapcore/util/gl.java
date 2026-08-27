package com.amap.api.mapcore.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import com.czw.smartkit.R;
import java.util.List;

/* compiled from: OfflineListAdapter.java */
/* loaded from: classes.dex */
public class gl extends BaseExpandableListAdapter implements ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnGroupExpandListener {
    private boolean[] a;
    private int b = -1;
    private List<OfflineMapProvince> c;
    private OfflineMapManager d;
    private Context e;

    /* compiled from: OfflineListAdapter.java */
    /* loaded from: classes.dex */
    public final class a {
        public gp a;

        public a() {
        }
    }

    public gl(List<OfflineMapProvince> list, OfflineMapManager offlineMapManager, Context context) {
        this.c = null;
        this.c = list;
        this.d = offlineMapManager;
        this.e = context;
        this.a = new boolean[list.size()];
    }

    private boolean a(int i) {
        return (i == 0 || i == getGroupCount() - 1) ? false : true;
    }

    public void a() {
        this.b = -1;
        notifyDataSetChanged();
    }

    public void b() {
        this.b = 0;
        notifyDataSetChanged();
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int i, int i2) {
        return null;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        a aVar;
        if (view != null) {
            aVar = (a) view.getTag();
        } else {
            aVar = new a();
            gp gpVar = new gp(this.e, this.d);
            gpVar.a(1);
            View a2 = gpVar.a();
            aVar.a = gpVar;
            a2.setTag(aVar);
            view = a2;
        }
        aVar.a.a(this.c.get(i).getCityList().get(i2));
        return view;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int i) {
        return a(i) ? this.c.get(i).getCityList().size() : this.c.get(i).getCityList().size();
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int i) {
        return this.c.get(i).getProvinceName();
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        return this.b == -1 ? this.c.size() : this.b;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int i) {
        return i;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = (RelativeLayout) gt.a(this.e, R.array.smssdk_country_group_b, null);
        }
        TextView textView = (TextView) view.findViewById(R.dimen.DIMEN_116PX);
        ImageView imageView = (ImageView) view.findViewById(R.dimen.DIMEN_117PX);
        textView.setText(this.c.get(i).getProvinceName());
        if (this.a[i]) {
            imageView.setImageDrawable(gt.a().getDrawable(2130837509));
        } else {
            imageView.setImageDrawable(gt.a().getDrawable(2130837510));
        }
        return view;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return true;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    @Override // android.widget.ExpandableListView.OnGroupCollapseListener
    public void onGroupCollapse(int i) {
        this.a[i] = false;
    }

    @Override // android.widget.ExpandableListView.OnGroupExpandListener
    public void onGroupExpand(int i) {
        this.a[i] = true;
    }
}
