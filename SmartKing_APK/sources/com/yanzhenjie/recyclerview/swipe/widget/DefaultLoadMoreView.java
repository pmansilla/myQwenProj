package com.yanzhenjie.recyclerview.swipe.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yanzhenjie.loading.LoadingView;
import com.yanzhenjie.recyclerview.swipe.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

/* loaded from: classes2.dex */
public class DefaultLoadMoreView extends LinearLayout implements SwipeMenuRecyclerView.LoadMoreView, View.OnClickListener {
    private SwipeMenuRecyclerView.LoadMoreListener mLoadMoreListener;
    private LoadingView mLoadingView;
    private TextView mTvMessage;

    public DefaultLoadMoreView(Context context) {
        this(context, null);
    }

    public DefaultLoadMoreView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        setGravity(17);
        setVisibility(8);
        double d = getResources().getDisplayMetrics().density * 60.0f;
        Double.isNaN(d);
        setMinimumHeight((int) (d + 0.5d));
        inflate(getContext(), R.layout.recycler_swipe_view_load_more, this);
        this.mLoadingView = (LoadingView) findViewById(R.id.loading_view);
        this.mTvMessage = (TextView) findViewById(R.id.tv_load_more_message);
        this.mLoadingView.setCircleColors(ContextCompat.getColor(getContext(), R.color.recycler_swipe_color_loading_color1), ContextCompat.getColor(getContext(), R.color.recycler_swipe_color_loading_color2), ContextCompat.getColor(getContext(), R.color.recycler_swipe_color_loading_color3));
        setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mLoadMoreListener != null) {
            this.mLoadMoreListener.onLoadMore();
        }
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView.LoadMoreView
    public void onLoadError(int i, String str) {
        setVisibility(0);
        this.mLoadingView.setVisibility(8);
        this.mTvMessage.setVisibility(0);
        TextView textView = this.mTvMessage;
        if (TextUtils.isEmpty(str)) {
            str = getContext().getString(R.string.recycler_swipe_load_error);
        }
        textView.setText(str);
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView.LoadMoreView
    public void onLoadFinish(boolean z, boolean z2) {
        if (z2) {
            setVisibility(4);
            return;
        }
        setVisibility(0);
        if (z) {
            this.mLoadingView.setVisibility(8);
            this.mTvMessage.setVisibility(0);
            this.mTvMessage.setText(R.string.recycler_swipe_data_empty);
        } else {
            this.mLoadingView.setVisibility(8);
            this.mTvMessage.setVisibility(0);
            this.mTvMessage.setText(R.string.recycler_swipe_more_not);
        }
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView.LoadMoreView
    public void onLoading() {
        setVisibility(0);
        this.mLoadingView.setVisibility(0);
        this.mTvMessage.setVisibility(0);
        this.mTvMessage.setText(R.string.recycler_swipe_load_more_message);
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView.LoadMoreView
    public void onWaitToLoadMore(SwipeMenuRecyclerView.LoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
        setVisibility(0);
        this.mLoadingView.setVisibility(8);
        this.mTvMessage.setVisibility(0);
        this.mTvMessage.setText(R.string.recycler_swipe_click_load_more);
    }
}
