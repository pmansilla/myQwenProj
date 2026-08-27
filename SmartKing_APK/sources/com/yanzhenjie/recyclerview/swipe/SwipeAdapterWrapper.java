package com.yanzhenjie.recyclerview.swipe;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.util.List;

/* loaded from: classes2.dex */
public class SwipeAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private RecyclerView.Adapter mAdapter;
    private LayoutInflater mInflater;
    private SwipeItemClickListener mSwipeItemClickListener;
    private SwipeMenuCreator mSwipeMenuCreator;
    private SwipeMenuItemClickListener mSwipeMenuItemClickListener;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    /* loaded from: classes2.dex */
    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SwipeAdapterWrapper(Context context, RecyclerView.Adapter adapter) {
        this.mInflater = LayoutInflater.from(context);
        this.mAdapter = adapter;
    }

    private int getContentItemCount() {
        return this.mAdapter.getItemCount();
    }

    private Class<?> getSupperClass(Class<?> cls) {
        Class<? super Object> superclass = cls.getSuperclass();
        return (superclass == null || superclass.equals(Object.class)) ? cls : getSupperClass(superclass);
    }

    public void addFooterView(View view) {
        this.mFootViews.put(getFooterItemCount() + 200000, view);
    }

    public void addFooterViewAndNotify(View view) {
        this.mFootViews.put(getFooterItemCount() + 200000, view);
        notifyItemInserted(((getHeaderItemCount() + getContentItemCount()) + getFooterItemCount()) - 1);
    }

    public void addHeaderView(View view) {
        this.mHeaderViews.put(getHeaderItemCount() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addHeaderViewAndNotify(View view) {
        this.mHeaderViews.put(getHeaderItemCount() + BASE_ITEM_TYPE_HEADER, view);
        notifyItemInserted(getHeaderItemCount() - 1);
    }

    public int getFooterItemCount() {
        return this.mFootViews.size();
    }

    public int getHeaderItemCount() {
        return this.mHeaderViews.size();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return getHeaderItemCount() + getContentItemCount() + getFooterItemCount();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return (isHeaderView(i) || isFooterView(i)) ? super.getItemId(i) : this.mAdapter.getItemId(i);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return isHeaderView(i) ? this.mHeaderViews.keyAt(i) : isFooterView(i) ? this.mFootViews.keyAt((i - getHeaderItemCount()) - getContentItemCount()) : this.mAdapter.getItemViewType(i - getHeaderItemCount());
    }

    public RecyclerView.Adapter getOriginAdapter() {
        return this.mAdapter;
    }

    public boolean isFooterView(int i) {
        return i >= getHeaderItemCount() + getContentItemCount();
    }

    public boolean isHeaderView(int i) {
        return i >= 0 && i < getHeaderItemCount();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List<Object> list) {
        if (isHeaderView(i) || isFooterView(i)) {
            return;
        }
        View view = viewHolder.itemView;
        if (view instanceof SwipeMenuLayout) {
            SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) view;
            int childCount = swipeMenuLayout.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = swipeMenuLayout.getChildAt(i2);
                if (childAt instanceof SwipeMenuView) {
                    ((SwipeMenuView) childAt).bindViewHolder(viewHolder);
                }
            }
        }
        this.mAdapter.onBindViewHolder(viewHolder, i - getHeaderItemCount(), list);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (this.mHeaderViews.get(i) != null) {
            return new ViewHolder(this.mHeaderViews.get(i));
        }
        if (this.mFootViews.get(i) != null) {
            return new ViewHolder(this.mFootViews.get(i));
        }
        final RecyclerView.ViewHolder onCreateViewHolder = this.mAdapter.onCreateViewHolder(viewGroup, i);
        if (this.mSwipeItemClickListener != null) {
            onCreateViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.yanzhenjie.recyclerview.swipe.SwipeAdapterWrapper.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    SwipeAdapterWrapper.this.mSwipeItemClickListener.onItemClick(view, onCreateViewHolder.getAdapterPosition());
                }
            });
        }
        if (this.mSwipeMenuCreator == null) {
            return onCreateViewHolder;
        }
        SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) this.mInflater.inflate(R.layout.recycler_swipe_view_item, viewGroup, false);
        SwipeMenu swipeMenu = new SwipeMenu(swipeMenuLayout, i);
        SwipeMenu swipeMenu2 = new SwipeMenu(swipeMenuLayout, i);
        this.mSwipeMenuCreator.onCreateMenu(swipeMenu, swipeMenu2, i);
        if (swipeMenu.getMenuItems().size() > 0) {
            SwipeMenuView swipeMenuView = (SwipeMenuView) swipeMenuLayout.findViewById(R.id.swipe_left);
            swipeMenuView.setOrientation(swipeMenu.getOrientation());
            swipeMenuView.createMenu(swipeMenu, swipeMenuLayout, this.mSwipeMenuItemClickListener, 1);
        }
        if (swipeMenu2.getMenuItems().size() > 0) {
            SwipeMenuView swipeMenuView2 = (SwipeMenuView) swipeMenuLayout.findViewById(R.id.swipe_right);
            swipeMenuView2.setOrientation(swipeMenu2.getOrientation());
            swipeMenuView2.createMenu(swipeMenu2, swipeMenuLayout, this.mSwipeMenuItemClickListener, -1);
        }
        ((ViewGroup) swipeMenuLayout.findViewById(R.id.swipe_content)).addView(onCreateViewHolder.itemView);
        try {
            Field declaredField = getSupperClass(onCreateViewHolder.getClass()).getDeclaredField("itemView");
            if (!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
            }
            declaredField.set(onCreateViewHolder, swipeMenuLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return onCreateViewHolder;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder viewHolder) {
        int adapterPosition = viewHolder.getAdapterPosition();
        if (isHeaderView(adapterPosition) || isFooterView(adapterPosition)) {
            return false;
        }
        return this.mAdapter.onFailedToRecycleView(viewHolder);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        int adapterPosition = viewHolder.getAdapterPosition();
        if (!isHeaderView(adapterPosition) && !isFooterView(adapterPosition)) {
            this.mAdapter.onViewAttachedToWindow(viewHolder);
            return;
        }
        ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
        if (layoutParams == null || !(layoutParams instanceof StaggeredGridLayoutManager.LayoutParams)) {
            return;
        }
        ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder viewHolder) {
        int adapterPosition = viewHolder.getAdapterPosition();
        if (isHeaderView(adapterPosition) || isFooterView(adapterPosition)) {
            return;
        }
        this.mAdapter.onViewDetachedFromWindow(viewHolder);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        int adapterPosition = viewHolder.getAdapterPosition();
        if (isHeaderView(adapterPosition) || isFooterView(adapterPosition)) {
            return;
        }
        this.mAdapter.onViewRecycled(viewHolder);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
        super.registerAdapterDataObserver(adapterDataObserver);
    }

    public void removeFooterViewAndNotify(View view) {
        int indexOfValue = this.mFootViews.indexOfValue(view);
        this.mFootViews.removeAt(indexOfValue);
        notifyItemRemoved(getHeaderItemCount() + getContentItemCount() + indexOfValue);
    }

    public void removeHeaderViewAndNotify(View view) {
        int indexOfValue = this.mHeaderViews.indexOfValue(view);
        this.mHeaderViews.removeAt(indexOfValue);
        notifyItemRemoved(indexOfValue);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void setHasStableIds(boolean z) {
        this.mAdapter.setHasStableIds(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSwipeItemClickListener(SwipeItemClickListener swipeItemClickListener) {
        this.mSwipeItemClickListener = swipeItemClickListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSwipeMenuCreator(SwipeMenuCreator swipeMenuCreator) {
        this.mSwipeMenuCreator = swipeMenuCreator;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSwipeMenuItemClickListener(SwipeMenuItemClickListener swipeMenuItemClickListener) {
        this.mSwipeMenuItemClickListener = swipeMenuItemClickListener;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
        super.unregisterAdapterDataObserver(adapterDataObserver);
    }
}
