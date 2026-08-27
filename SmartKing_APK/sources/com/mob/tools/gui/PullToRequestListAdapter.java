package com.mob.tools.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/* loaded from: classes2.dex */
public abstract class PullToRequestListAdapter extends PullToRequestBaseListAdapter {
    private PullToRequestBaseAdapter adapter;
    private boolean fling;
    private ScrollableListView listView;
    private OnListStopScrollListener osListener;
    private boolean pullUpReady;

    public PullToRequestListAdapter(PullToRequestView pullToRequestView) {
        super(pullToRequestView);
        this.listView = onNewListView(getContext());
        this.listView.setOnScrollListener(new AbsListView.OnScrollListener() { // from class: com.mob.tools.gui.PullToRequestListAdapter.1
            private int firstVisibleItem;
            private int visibleItemCount;

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                this.firstVisibleItem = i;
                this.visibleItemCount = i2;
                View childAt = absListView.getChildAt(i2 - 1);
                PullToRequestListAdapter.this.pullUpReady = i + i2 == i3 && childAt != null && childAt.getBottom() <= absListView.getBottom();
                PullToRequestListAdapter.this.onScroll(PullToRequestListAdapter.this.listView, i, i2, i3);
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                PullToRequestListAdapter.this.fling = i == 2;
                if (i == 0) {
                    if (PullToRequestListAdapter.this.osListener != null) {
                        PullToRequestListAdapter.this.osListener.onListStopScrolling(this.firstVisibleItem, this.visibleItemCount);
                    } else if (PullToRequestListAdapter.this.adapter != null) {
                        PullToRequestListAdapter.this.adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        this.adapter = new PullToRequestBaseAdapter(this);
        this.listView.setAdapter((ListAdapter) this.adapter);
    }

    @Override // com.mob.tools.gui.PullToRequestAdatper
    public Scrollable getBodyView() {
        return this.listView;
    }

    public ListView getListView() {
        return this.listView;
    }

    @Override // com.mob.tools.gui.PullToRequestBaseListAdapter
    public boolean isFling() {
        return this.fling;
    }

    @Override // com.mob.tools.gui.PullToRequestAdatper
    public boolean isPullDownReady() {
        return this.listView.isReadyToPull();
    }

    @Override // com.mob.tools.gui.PullToRequestAdatper
    public boolean isPullUpReady() {
        return this.pullUpReady;
    }

    @Override // com.mob.tools.gui.PullToRequestAdatper
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.adapter.notifyDataSetChanged();
    }

    protected ScrollableListView onNewListView(Context context) {
        return new ScrollableListView(context);
    }

    @Override // com.mob.tools.gui.PullToRequestBaseListAdapter
    public void onScroll(Scrollable scrollable, int i, int i2, int i3) {
    }

    public void setDivider(Drawable drawable) {
        this.listView.setDivider(drawable);
    }

    public void setDividerHeight(int i) {
        this.listView.setDividerHeight(i);
    }
}
