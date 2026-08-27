package com.czw.modes.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.czw.modes.adapter.RecycleAdapter.RecycleTag;
import com.czw.utils.ViewUtil;
import java.util.List;

/* loaded from: classes.dex */
public abstract class RecycleAdapter<D, H extends RecycleTag> extends RecyclerView.Adapter<H> {
    protected Context context;
    protected List<D> data;
    protected DisplayMetrics dm = new DisplayMetrics();
    private LayoutInflater layoutInflater;

    /* loaded from: classes.dex */
    public static class RecycleTag extends RecyclerView.ViewHolder {
        public View view;

        public RecycleTag(View view) {
            super(view);
            this.view = view;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public <V extends View> V $View(int i) {
            return (V) this.view.findViewById(i);
        }
    }

    public RecycleAdapter(Context context, List<D> list) {
        this.data = null;
        this.context = null;
        this.data = list;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        ViewUtil.load(context);
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(this.dm);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.data.size();
    }

    public abstract void handDataAndView(H h, D d, int i);

    public abstract H instanceTag(View view);

    public abstract int loadItemView();

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(H h, int i) {
        handDataAndView(h, this.data.get(i), i);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public H onCreateViewHolder(ViewGroup viewGroup, int i) {
        return instanceTag(this.layoutInflater.inflate(loadItemView(), viewGroup, false));
    }
}
