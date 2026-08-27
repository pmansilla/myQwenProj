package com.yanzhenjie.recyclerview.swipe.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class DefaultItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerWidth;
    private List<Integer> mViewTypeList;

    public DefaultItemDecoration(@ColorInt int i) {
        this(i, 2, 2, -1);
    }

    public DefaultItemDecoration(@ColorInt int i, int i2, int i3, int... iArr) {
        this.mViewTypeList = new ArrayList();
        this.mDivider = new ColorDrawable(i);
        this.mDividerWidth = i2;
        this.mDividerHeight = i3;
        for (int i4 : iArr) {
            this.mViewTypeList.add(Integer.valueOf(i4));
        }
    }

    private int getSpanCount(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return 1;
    }

    private boolean isFirstColumn(int i, int i2) {
        return i2 == 1 || i % i2 == 0;
    }

    private boolean isFirstRaw(int i, int i2) {
        return i < i2;
    }

    private boolean isLastColumn(int i, int i2) {
        return i2 == 1 || (i + 1) % i2 == 0;
    }

    private boolean isLastRaw(int i, int i2, int i3) {
        if (i2 == 1) {
            return i + 1 == i3;
        }
        int i4 = i3 % i2;
        int i5 = ((i3 - i4) / i2) + (i4 > 0 ? 1 : 0);
        int i6 = i + 1;
        int i7 = i6 % i2;
        return i7 == 0 ? i5 == i6 / i2 : i5 == ((i6 - i7) / i2) + 1;
    }

    public void drawHorizontal(Canvas canvas, RecyclerView recyclerView) {
        canvas.save();
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            int childAdapterPosition = recyclerView.getChildAdapterPosition(childAt);
            if (childAdapterPosition >= 0 && !this.mViewTypeList.contains(Integer.valueOf(recyclerView.getAdapter().getItemViewType(childAdapterPosition))) && !(childAt instanceof SwipeMenuRecyclerView.LoadMoreView)) {
                int left = childAt.getLeft();
                int bottom = childAt.getBottom();
                this.mDivider.setBounds(left, bottom, childAt.getRight(), this.mDividerHeight + bottom);
                this.mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }

    public void drawVertical(Canvas canvas, RecyclerView recyclerView) {
        canvas.save();
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            int childAdapterPosition = recyclerView.getChildAdapterPosition(childAt);
            if (childAdapterPosition >= 0 && !this.mViewTypeList.contains(Integer.valueOf(recyclerView.getAdapter().getItemViewType(childAdapterPosition))) && !(childAt instanceof SwipeMenuRecyclerView.LoadMoreView)) {
                int right = childAt.getRight();
                this.mDivider.setBounds(right, childAt.getTop(), this.mDividerWidth + right, childAt.getBottom());
                this.mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }

    @Override // android.support.v7.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        if (childAdapterPosition < 0) {
            return;
        }
        if (this.mViewTypeList.contains(Integer.valueOf(recyclerView.getAdapter().getItemViewType(childAdapterPosition)))) {
            rect.set(0, 0, 0, 0);
            return;
        }
        int spanCount = getSpanCount(recyclerView);
        int itemCount = recyclerView.getAdapter().getItemCount();
        boolean isFirstRaw = isFirstRaw(childAdapterPosition, spanCount);
        boolean isLastRaw = isLastRaw(childAdapterPosition, spanCount, itemCount);
        boolean isFirstColumn = isFirstColumn(childAdapterPosition, spanCount);
        boolean isLastColumn = isLastColumn(childAdapterPosition, spanCount);
        if (spanCount == 1) {
            if (isFirstRaw) {
                rect.set(0, 0, 0, this.mDividerHeight / 2);
                return;
            } else if (isLastRaw) {
                rect.set(0, this.mDividerHeight / 2, 0, 0);
                return;
            } else {
                rect.set(0, this.mDividerHeight / 2, 0, this.mDividerHeight / 2);
                return;
            }
        }
        if (isFirstRaw && isFirstColumn) {
            rect.set(0, 0, this.mDividerWidth / 2, this.mDividerHeight / 2);
            return;
        }
        if (isFirstRaw && isLastColumn) {
            rect.set(this.mDividerWidth / 2, 0, 0, this.mDividerHeight / 2);
            return;
        }
        if (isFirstRaw) {
            rect.set(this.mDividerWidth / 2, 0, this.mDividerWidth / 2, this.mDividerHeight / 2);
            return;
        }
        if (isLastRaw && isFirstColumn) {
            rect.set(0, this.mDividerHeight / 2, this.mDividerWidth / 2, 0);
            return;
        }
        if (isLastRaw && isLastColumn) {
            rect.set(this.mDividerWidth / 2, this.mDividerHeight / 2, 0, 0);
            return;
        }
        if (isLastRaw) {
            rect.set(this.mDividerWidth / 2, this.mDividerHeight / 2, this.mDividerWidth / 2, 0);
            return;
        }
        if (isFirstColumn) {
            rect.set(0, this.mDividerHeight / 2, this.mDividerWidth / 2, this.mDividerHeight / 2);
        } else if (isLastColumn) {
            rect.set(this.mDividerWidth / 2, this.mDividerHeight / 2, 0, this.mDividerHeight / 2);
        } else {
            rect.set(this.mDividerWidth / 2, this.mDividerHeight / 2, this.mDividerWidth / 2, this.mDividerHeight / 2);
        }
    }

    @Override // android.support.v7.widget.RecyclerView.ItemDecoration
    public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        drawHorizontal(canvas, recyclerView);
        drawVertical(canvas, recyclerView);
    }
}
