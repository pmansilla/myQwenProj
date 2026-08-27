package com.yanzhenjie.recyclerview.swipe;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.yanzhenjie.recyclerview.swipe.touch.DefaultItemTouchHelper;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMovementListener;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemStateChangedListener;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultLoadMoreView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class SwipeMenuRecyclerView extends RecyclerView {
    private static final int INVALID_POSITION = -1;
    public static final int LEFT_DIRECTION = 1;
    public static final int RIGHT_DIRECTION = -1;
    private boolean allowSwipeDelete;
    private boolean isAutoLoadMore;
    private boolean isLoadError;
    private boolean isLoadMore;
    private RecyclerView.AdapterDataObserver mAdapterDataObserver;
    private SwipeAdapterWrapper mAdapterWrapper;
    private boolean mDataEmpty;
    private DefaultItemTouchHelper mDefaultItemTouchHelper;
    private int mDownX;
    private int mDownY;
    private List<View> mFooterViewList;
    private boolean mHasMore;
    private List<View> mHeaderViewList;
    private LoadMoreListener mLoadMoreListener;
    private LoadMoreView mLoadMoreView;
    protected SwipeMenuLayout mOldSwipedLayout;
    protected int mOldTouchedPosition;
    protected int mScaleTouchSlop;
    private int mScrollState;
    private SwipeItemClickListener mSwipeItemClickListener;
    private SwipeMenuCreator mSwipeMenuCreator;
    private SwipeMenuItemClickListener mSwipeMenuItemClickListener;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface DirectionMode {
    }

    /* loaded from: classes2.dex */
    private static class ItemClick implements SwipeItemClickListener {
        private SwipeItemClickListener mCallback;
        private SwipeMenuRecyclerView mRecyclerView;

        public ItemClick(SwipeMenuRecyclerView swipeMenuRecyclerView, SwipeItemClickListener swipeItemClickListener) {
            this.mRecyclerView = swipeMenuRecyclerView;
            this.mCallback = swipeItemClickListener;
        }

        @Override // com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener
        public void onItemClick(View view, int i) {
            int headerItemCount = i - this.mRecyclerView.getHeaderItemCount();
            if (headerItemCount >= 0) {
                this.mCallback.onItemClick(view, headerItemCount);
            }
        }
    }

    /* loaded from: classes2.dex */
    public interface LoadMoreListener {
        void onLoadMore();
    }

    /* loaded from: classes2.dex */
    public interface LoadMoreView {
        void onLoadError(int i, String str);

        void onLoadFinish(boolean z, boolean z2);

        void onLoading();

        void onWaitToLoadMore(LoadMoreListener loadMoreListener);
    }

    /* loaded from: classes2.dex */
    private static class MenuItemClick implements SwipeMenuItemClickListener {
        private SwipeMenuItemClickListener mCallback;
        private SwipeMenuRecyclerView mRecyclerView;

        public MenuItemClick(SwipeMenuRecyclerView swipeMenuRecyclerView, SwipeMenuItemClickListener swipeMenuItemClickListener) {
            this.mRecyclerView = swipeMenuRecyclerView;
            this.mCallback = swipeMenuItemClickListener;
        }

        @Override // com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
        public void onItemClick(SwipeMenuBridge swipeMenuBridge) {
            int adapterPosition = swipeMenuBridge.getAdapterPosition() - this.mRecyclerView.getHeaderItemCount();
            if (adapterPosition >= 0) {
                swipeMenuBridge.mAdapterPosition = adapterPosition;
                this.mCallback.onItemClick(swipeMenuBridge);
            }
        }
    }

    public SwipeMenuRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeMenuRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SwipeMenuRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOldTouchedPosition = -1;
        this.allowSwipeDelete = false;
        this.mAdapterDataObserver = new RecyclerView.AdapterDataObserver() { // from class: com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView.2
            @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
            public void onChanged() {
                SwipeMenuRecyclerView.this.mAdapterWrapper.notifyDataSetChanged();
            }

            @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeChanged(int i2, int i3) {
                SwipeMenuRecyclerView.this.mAdapterWrapper.notifyItemRangeChanged(i2 + SwipeMenuRecyclerView.this.getHeaderItemCount(), i3);
            }

            @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeChanged(int i2, int i3, Object obj) {
                SwipeMenuRecyclerView.this.mAdapterWrapper.notifyItemRangeChanged(i2 + SwipeMenuRecyclerView.this.getHeaderItemCount(), i3, obj);
            }

            @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeInserted(int i2, int i3) {
                SwipeMenuRecyclerView.this.mAdapterWrapper.notifyItemRangeInserted(i2 + SwipeMenuRecyclerView.this.getHeaderItemCount(), i3);
            }

            @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeMoved(int i2, int i3, int i4) {
                SwipeMenuRecyclerView.this.mAdapterWrapper.notifyItemMoved(i2 + SwipeMenuRecyclerView.this.getHeaderItemCount(), i3 + SwipeMenuRecyclerView.this.getHeaderItemCount());
            }

            @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeRemoved(int i2, int i3) {
                SwipeMenuRecyclerView.this.mAdapterWrapper.notifyItemRangeRemoved(i2 + SwipeMenuRecyclerView.this.getHeaderItemCount(), i3);
            }
        };
        this.mHeaderViewList = new ArrayList();
        this.mFooterViewList = new ArrayList();
        this.mScrollState = -1;
        this.isLoadMore = false;
        this.isAutoLoadMore = true;
        this.isLoadError = false;
        this.mDataEmpty = true;
        this.mHasMore = false;
        this.mScaleTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    private void checkAdapterExist(String str) {
        if (this.mAdapterWrapper != null) {
            throw new IllegalStateException(str);
        }
    }

    private void dispatchLoadMore() {
        if (this.isLoadError) {
            return;
        }
        if (!this.isAutoLoadMore) {
            if (this.mLoadMoreView != null) {
                this.mLoadMoreView.onWaitToLoadMore(this.mLoadMoreListener);
            }
        } else {
            if (this.isLoadMore || this.mDataEmpty || !this.mHasMore) {
                return;
            }
            this.isLoadMore = true;
            if (this.mLoadMoreView != null) {
                this.mLoadMoreView.onLoading();
            }
            if (this.mLoadMoreListener != null) {
                this.mLoadMoreListener.onLoadMore();
            }
        }
    }

    private View getSwipeMenuView(View view) {
        if (view instanceof SwipeMenuLayout) {
            return view;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(view);
        while (!arrayList.isEmpty()) {
            View view2 = (View) arrayList.remove(0);
            if (view2 instanceof ViewGroup) {
                if (view2 instanceof SwipeMenuLayout) {
                    return view2;
                }
                ViewGroup viewGroup = (ViewGroup) view2;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    arrayList.add(viewGroup.getChildAt(i));
                }
            }
        }
        return view;
    }

    private boolean handleUnDown(int i, int i2, boolean z) {
        int i3 = this.mDownX - i;
        int i4 = this.mDownY - i2;
        if (Math.abs(i3) > this.mScaleTouchSlop && Math.abs(i3) > Math.abs(i4)) {
            return false;
        }
        if (Math.abs(i4) >= this.mScaleTouchSlop || Math.abs(i3) >= this.mScaleTouchSlop) {
            return z;
        }
        return false;
    }

    private void initializeItemTouchHelper() {
        if (this.mDefaultItemTouchHelper == null) {
            this.mDefaultItemTouchHelper = new DefaultItemTouchHelper();
            this.mDefaultItemTouchHelper.attachToRecyclerView(this);
        }
    }

    public void addFooterView(View view) {
        this.mFooterViewList.add(view);
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.addFooterViewAndNotify(view);
        }
    }

    public void addHeaderView(View view) {
        this.mHeaderViewList.add(view);
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.addHeaderViewAndNotify(view);
        }
    }

    public int getFooterItemCount() {
        if (this.mAdapterWrapper == null) {
            return 0;
        }
        return this.mAdapterWrapper.getFooterItemCount();
    }

    public int getHeaderItemCount() {
        if (this.mAdapterWrapper == null) {
            return 0;
        }
        return this.mAdapterWrapper.getHeaderItemCount();
    }

    public int getItemViewType(int i) {
        if (this.mAdapterWrapper == null) {
            return 0;
        }
        return this.mAdapterWrapper.getItemViewType(i);
    }

    public RecyclerView.Adapter getOriginAdapter() {
        if (this.mAdapterWrapper == null) {
            return null;
        }
        return this.mAdapterWrapper.getOriginAdapter();
    }

    public boolean isItemViewSwipeEnabled() {
        initializeItemTouchHelper();
        return this.mDefaultItemTouchHelper.isItemViewSwipeEnabled();
    }

    public boolean isLongPressDragEnabled() {
        initializeItemTouchHelper();
        return this.mDefaultItemTouchHelper.isLongPressDragEnabled();
    }

    public void loadMoreError(int i, String str) {
        this.isLoadMore = false;
        this.isLoadError = true;
        if (this.mLoadMoreView != null) {
            this.mLoadMoreView.onLoadError(i, str);
        }
    }

    public final void loadMoreFinish(boolean z, boolean z2) {
        this.isLoadMore = false;
        this.isLoadError = false;
        this.mDataEmpty = z;
        this.mHasMore = z2;
        if (this.mLoadMoreView != null) {
            this.mLoadMoreView.onLoadFinish(z, z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v7.widget.RecyclerView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.getOriginAdapter().unregisterAdapterDataObserver(this.mAdapterDataObserver);
        }
        super.onDetachedFromWindow();
    }

    @Override // android.support.v7.widget.RecyclerView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z;
        ViewParent parent;
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
        if (this.allowSwipeDelete) {
            return onInterceptTouchEvent;
        }
        boolean z2 = true;
        if (motionEvent.getPointerCount() > 1) {
            return true;
        }
        int action = motionEvent.getAction();
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        switch (action) {
            case 0:
                this.mDownX = x;
                this.mDownY = y;
                int childAdapterPosition = getChildAdapterPosition(findChildViewUnder(x, y));
                if (childAdapterPosition == this.mOldTouchedPosition || this.mOldSwipedLayout == null || !this.mOldSwipedLayout.isMenuOpen()) {
                    z = false;
                } else {
                    this.mOldSwipedLayout.smoothCloseMenu();
                    z = true;
                }
                if (z) {
                    this.mOldSwipedLayout = null;
                    this.mOldTouchedPosition = -1;
                    return z;
                }
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = findViewHolderForAdapterPosition(childAdapterPosition);
                if (findViewHolderForAdapterPosition == null) {
                    return z;
                }
                View swipeMenuView = getSwipeMenuView(findViewHolderForAdapterPosition.itemView);
                if (!(swipeMenuView instanceof SwipeMenuLayout)) {
                    return z;
                }
                this.mOldSwipedLayout = (SwipeMenuLayout) swipeMenuView;
                this.mOldTouchedPosition = childAdapterPosition;
                return z;
            case 1:
            case 3:
                break;
            case 2:
                onInterceptTouchEvent = handleUnDown(x, y, onInterceptTouchEvent);
                if (this.mOldSwipedLayout != null && (parent = getParent()) != null) {
                    int i = this.mDownX - x;
                    boolean z3 = i > 0 && (this.mOldSwipedLayout.hasRightMenu() || this.mOldSwipedLayout.isLeftCompleteOpen());
                    boolean z4 = i < 0 && (this.mOldSwipedLayout.hasLeftMenu() || this.mOldSwipedLayout.isRightCompleteOpen());
                    if (!z3 && !z4) {
                        z2 = false;
                    }
                    parent.requestDisallowInterceptTouchEvent(z2);
                    break;
                } else {
                    return onInterceptTouchEvent;
                }
            default:
                return onInterceptTouchEvent;
        }
        return handleUnDown(x, y, onInterceptTouchEvent);
    }

    @Override // android.support.v7.widget.RecyclerView
    public void onScrollStateChanged(int i) {
        this.mScrollState = i;
    }

    @Override // android.support.v7.widget.RecyclerView
    public void onScrolled(int i, int i2) {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null && (layoutManager instanceof LinearLayoutManager)) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int itemCount = layoutManager.getItemCount();
            if (itemCount > 0 && itemCount == linearLayoutManager.findLastVisibleItemPosition() + 1) {
                if (this.mScrollState == 1 || this.mScrollState == 2) {
                    dispatchLoadMore();
                    return;
                }
                return;
            }
            return;
        }
        if (layoutManager == null || !(layoutManager instanceof StaggeredGridLayoutManager)) {
            return;
        }
        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        int itemCount2 = layoutManager.getItemCount();
        if (itemCount2 <= 0) {
            return;
        }
        int[] findLastCompletelyVisibleItemPositions = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
        if (itemCount2 == findLastCompletelyVisibleItemPositions[findLastCompletelyVisibleItemPositions.length - 1] + 1) {
            if (this.mScrollState == 1 || this.mScrollState == 2) {
                dispatchLoadMore();
            }
        }
    }

    @Override // android.support.v7.widget.RecyclerView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 2:
                if (this.mOldSwipedLayout != null && this.mOldSwipedLayout.isMenuOpen()) {
                    this.mOldSwipedLayout.smoothCloseMenu();
                    break;
                }
                break;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void removeFooterView(View view) {
        this.mFooterViewList.remove(view);
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.removeFooterViewAndNotify(view);
        }
    }

    public void removeHeaderView(View view) {
        this.mHeaderViewList.remove(view);
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.removeHeaderViewAndNotify(view);
        }
    }

    @Override // android.support.v7.widget.RecyclerView
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.getOriginAdapter().unregisterAdapterDataObserver(this.mAdapterDataObserver);
        }
        if (adapter == null) {
            this.mAdapterWrapper = null;
        } else {
            adapter.registerAdapterDataObserver(this.mAdapterDataObserver);
            this.mAdapterWrapper = new SwipeAdapterWrapper(getContext(), adapter);
            this.mAdapterWrapper.setSwipeItemClickListener(this.mSwipeItemClickListener);
            this.mAdapterWrapper.setSwipeMenuCreator(this.mSwipeMenuCreator);
            this.mAdapterWrapper.setSwipeMenuItemClickListener(this.mSwipeMenuItemClickListener);
            if (this.mHeaderViewList.size() > 0) {
                Iterator<View> it = this.mHeaderViewList.iterator();
                while (it.hasNext()) {
                    this.mAdapterWrapper.addHeaderView(it.next());
                }
            }
            if (this.mFooterViewList.size() > 0) {
                Iterator<View> it2 = this.mFooterViewList.iterator();
                while (it2.hasNext()) {
                    this.mAdapterWrapper.addFooterView(it2.next());
                }
            }
        }
        super.setAdapter(this.mAdapterWrapper);
    }

    public void setAutoLoadMore(boolean z) {
        this.isAutoLoadMore = z;
    }

    public void setItemViewSwipeEnabled(boolean z) {
        initializeItemTouchHelper();
        this.allowSwipeDelete = z;
        this.mDefaultItemTouchHelper.setItemViewSwipeEnabled(z);
    }

    @Override // android.support.v7.widget.RecyclerView
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView.1
                @Override // android.support.v7.widget.GridLayoutManager.SpanSizeLookup
                public int getSpanSize(int i) {
                    if (SwipeMenuRecyclerView.this.mAdapterWrapper.isHeaderView(i) || SwipeMenuRecyclerView.this.mAdapterWrapper.isFooterView(i)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(i - SwipeMenuRecyclerView.this.getHeaderItemCount());
                    }
                    return 1;
                }
            });
        }
        super.setLayoutManager(layoutManager);
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }

    public void setLoadMoreView(LoadMoreView loadMoreView) {
        this.mLoadMoreView = loadMoreView;
    }

    public void setLongPressDragEnabled(boolean z) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelper.setLongPressDragEnabled(z);
    }

    public void setOnItemMoveListener(OnItemMoveListener onItemMoveListener) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelper.setOnItemMoveListener(onItemMoveListener);
    }

    public void setOnItemMovementListener(OnItemMovementListener onItemMovementListener) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelper.setOnItemMovementListener(onItemMovementListener);
    }

    public void setOnItemStateChangedListener(OnItemStateChangedListener onItemStateChangedListener) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelper.setOnItemStateChangedListener(onItemStateChangedListener);
    }

    public void setSwipeItemClickListener(SwipeItemClickListener swipeItemClickListener) {
        if (swipeItemClickListener == null) {
            return;
        }
        checkAdapterExist("Cannot set item click listener, setAdapter has already been called.");
        this.mSwipeItemClickListener = new ItemClick(this, swipeItemClickListener);
    }

    public void setSwipeMenuCreator(SwipeMenuCreator swipeMenuCreator) {
        if (swipeMenuCreator == null) {
            return;
        }
        checkAdapterExist("Cannot set menu creator, setAdapter has already been called.");
        this.mSwipeMenuCreator = swipeMenuCreator;
    }

    public void setSwipeMenuItemClickListener(SwipeMenuItemClickListener swipeMenuItemClickListener) {
        if (swipeMenuItemClickListener == null) {
            return;
        }
        checkAdapterExist("Cannot set menu item click listener, setAdapter has already been called.");
        this.mSwipeMenuItemClickListener = new MenuItemClick(this, swipeMenuItemClickListener);
    }

    public void smoothCloseMenu() {
        if (this.mOldSwipedLayout == null || !this.mOldSwipedLayout.isMenuOpen()) {
            return;
        }
        this.mOldSwipedLayout.smoothCloseMenu();
    }

    public void smoothOpenLeftMenu(int i) {
        smoothOpenMenu(i, 1, 200);
    }

    public void smoothOpenLeftMenu(int i, int i2) {
        smoothOpenMenu(i, 1, i2);
    }

    public void smoothOpenMenu(int i, int i2, int i3) {
        if (this.mOldSwipedLayout != null && this.mOldSwipedLayout.isMenuOpen()) {
            this.mOldSwipedLayout.smoothCloseMenu();
        }
        int headerItemCount = i + getHeaderItemCount();
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = findViewHolderForAdapterPosition(headerItemCount);
        if (findViewHolderForAdapterPosition != null) {
            View swipeMenuView = getSwipeMenuView(findViewHolderForAdapterPosition.itemView);
            if (swipeMenuView instanceof SwipeMenuLayout) {
                this.mOldSwipedLayout = (SwipeMenuLayout) swipeMenuView;
                if (i2 == -1) {
                    this.mOldTouchedPosition = headerItemCount;
                    this.mOldSwipedLayout.smoothOpenRightMenu(i3);
                } else if (i2 == 1) {
                    this.mOldTouchedPosition = headerItemCount;
                    this.mOldSwipedLayout.smoothOpenLeftMenu(i3);
                }
            }
        }
    }

    public void smoothOpenRightMenu(int i) {
        smoothOpenMenu(i, -1, 200);
    }

    public void smoothOpenRightMenu(int i, int i2) {
        smoothOpenMenu(i, -1, i2);
    }

    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelper.startDrag(viewHolder);
    }

    public void startSwipe(RecyclerView.ViewHolder viewHolder) {
        initializeItemTouchHelper();
        this.mDefaultItemTouchHelper.startSwipe(viewHolder);
    }

    public void useDefaultLoadMore() {
        DefaultLoadMoreView defaultLoadMoreView = new DefaultLoadMoreView(getContext());
        addFooterView(defaultLoadMoreView);
        setLoadMoreView(defaultLoadMoreView);
    }
}
