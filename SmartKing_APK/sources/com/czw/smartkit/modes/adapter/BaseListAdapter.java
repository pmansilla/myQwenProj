package com.czw.smartkit.modes.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.czw.smartkit.modes.adapter.BaseListAdapter.Tag;
import com.czw.utils.ViewUtil;
import java.util.List;

/* loaded from: classes.dex */
public abstract class BaseListAdapter<D, T extends Tag> extends BaseAdapter {
    protected Context context;
    protected List<D> datas;
    protected DisplayMetrics dm = new DisplayMetrics();

    /* loaded from: classes.dex */
    public static class Tag {
        public View view;

        public Tag(View view) {
            this.view = view;
            view.setTag(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public <V extends View> V $View(int i) {
            return (V) this.view.findViewById(i);
        }
    }

    public BaseListAdapter(Context context, List<D> list) {
        this.datas = null;
        this.context = context;
        this.datas = list;
        ViewUtil.load(context);
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(this.dm);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.datas.size();
    }

    @Override // android.widget.Adapter
    public D getItem(int i) {
        return this.datas.get(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        Tag tag;
        D d = this.datas.get(i);
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(loadItemView(), viewGroup, false);
            tag = instanceTag(view);
        } else {
            tag = (Tag) view.getTag();
        }
        handDataAndView(tag, d, i);
        return view;
    }

    public abstract void handDataAndView(T t, D d, int i);

    public abstract T instanceTag(View view);

    public abstract int loadItemView();
}
