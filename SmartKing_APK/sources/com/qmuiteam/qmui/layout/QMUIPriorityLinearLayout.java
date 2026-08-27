package com.qmuiteam.qmui.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.qmuiteam.qmui.R;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class QMUIPriorityLinearLayout extends QMUILinearLayout {
    private ArrayList<View> mTempDisposableChildList;
    private ArrayList<View> mTempMiniWidthChildList;

    /* loaded from: classes2.dex */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        static final int PRIORITY_DISPOSABLE = 1;
        static final int PRIORITY_INCOMPRESSIBLE = 3;
        static final int PRIORITY_MINI_CONTENT_PROTECTION = 2;
        private int backupHeight;
        private int backupWidth;
        public int miniContentProtectionSize;
        private int priority;

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.priority = 2;
            this.miniContentProtectionSize = 0;
            this.backupWidth = Integer.MIN_VALUE;
            this.backupHeight = Integer.MIN_VALUE;
        }

        public LayoutParams(int i, int i2, float f) {
            super(i, i2, f);
            this.priority = 2;
            this.miniContentProtectionSize = 0;
            this.backupWidth = Integer.MIN_VALUE;
            this.backupHeight = Integer.MIN_VALUE;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.priority = 2;
            this.miniContentProtectionSize = 0;
            this.backupWidth = Integer.MIN_VALUE;
            this.backupHeight = Integer.MIN_VALUE;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUIPriorityLinearLayout_Layout);
            this.priority = obtainStyledAttributes.getInteger(R.styleable.QMUIPriorityLinearLayout_Layout_qmui_layout_priority, 2);
            this.miniContentProtectionSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIPriorityLinearLayout_Layout_qmui_layout_miniContentProtectionSize, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.priority = 2;
            this.miniContentProtectionSize = 0;
            this.backupWidth = Integer.MIN_VALUE;
            this.backupHeight = Integer.MIN_VALUE;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.priority = 2;
            this.miniContentProtectionSize = 0;
            this.backupWidth = Integer.MIN_VALUE;
            this.backupHeight = Integer.MIN_VALUE;
        }

        @TargetApi(19)
        public LayoutParams(LinearLayout.LayoutParams layoutParams) {
            super(layoutParams);
            this.priority = 2;
            this.miniContentProtectionSize = 0;
            this.backupWidth = Integer.MIN_VALUE;
            this.backupHeight = Integer.MIN_VALUE;
        }

        void backupOrRestore() {
            if (this.backupWidth == Integer.MIN_VALUE) {
                this.backupWidth = this.width;
            } else {
                this.width = this.backupWidth;
            }
            if (this.backupHeight == Integer.MIN_VALUE) {
                this.backupHeight = this.height;
            } else {
                this.height = this.backupHeight;
            }
        }

        public int getPriority(int i) {
            if (this.weight > 0.0f) {
                return 1;
            }
            if (i == 0) {
                if (this.width >= 0) {
                    return 3;
                }
            } else if (this.height >= 0) {
                return 3;
            }
            return this.priority;
        }

        public void setPriority(int i) {
            this.priority = i;
        }
    }

    public QMUIPriorityLinearLayout(Context context) {
        super(context);
        this.mTempMiniWidthChildList = new ArrayList<>();
        this.mTempDisposableChildList = new ArrayList<>();
    }

    public QMUIPriorityLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTempMiniWidthChildList = new ArrayList<>();
        this.mTempDisposableChildList = new ArrayList<>();
    }

    private int getVisibleChildCount() {
        int childCount = getChildCount();
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            if (getChildAt(i2).getVisibility() == 0) {
                i++;
            }
        }
        return i;
    }

    private void handleHorizontal(int i, int i2) {
        int size = (View.MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight();
        int mode = View.MeasureSpec.getMode(i);
        int visibleChildCount = getVisibleChildCount();
        if (mode == 0 || visibleChildCount == 0 || size <= 0) {
            return;
        }
        int handlePriorityIncompressible = handlePriorityIncompressible(i, i2);
        if (handlePriorityIncompressible >= size) {
            Iterator<View> it = this.mTempMiniWidthChildList.iterator();
            while (it.hasNext()) {
                View next = it.next();
                LayoutParams layoutParams = (LayoutParams) next.getLayoutParams();
                next.measure(View.MeasureSpec.makeMeasureSpec(layoutParams.miniContentProtectionSize, Integer.MIN_VALUE), i2);
                layoutParams.width = next.getMeasuredWidth();
            }
            Iterator<View> it2 = this.mTempDisposableChildList.iterator();
            while (it2.hasNext()) {
                ((LayoutParams) it2.next().getLayoutParams()).width = 0;
            }
            return;
        }
        int i3 = size - handlePriorityIncompressible;
        Iterator<View> it3 = this.mTempMiniWidthChildList.iterator();
        int i4 = 0;
        int i5 = 0;
        while (it3.hasNext()) {
            View next2 = it3.next();
            LayoutParams layoutParams2 = (LayoutParams) next2.getLayoutParams();
            next2.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), i2);
            int i6 = layoutParams2.leftMargin + layoutParams2.rightMargin;
            i4 += next2.getMeasuredWidth() + i6;
            i5 += Math.min(next2.getMeasuredWidth(), layoutParams2.miniContentProtectionSize) + i6;
        }
        if (i5 >= i3) {
            Iterator<View> it4 = this.mTempMiniWidthChildList.iterator();
            while (it4.hasNext()) {
                View next3 = it4.next();
                LayoutParams layoutParams3 = (LayoutParams) next3.getLayoutParams();
                layoutParams3.width = Math.min(next3.getMeasuredWidth(), layoutParams3.miniContentProtectionSize);
            }
            Iterator<View> it5 = this.mTempDisposableChildList.iterator();
            while (it5.hasNext()) {
                ((LayoutParams) it5.next().getLayoutParams()).width = 0;
            }
            return;
        }
        if (i4 < i3) {
            if (this.mTempDisposableChildList.isEmpty()) {
                return;
            }
            dispatchSpaceToDisposableChildList(this.mTempDisposableChildList, i3 - i4);
            return;
        }
        Iterator<View> it6 = this.mTempDisposableChildList.iterator();
        while (it6.hasNext()) {
            ((LayoutParams) it6.next().getLayoutParams()).width = 0;
        }
        if (i3 >= i4 || this.mTempMiniWidthChildList.isEmpty()) {
            return;
        }
        dispatchSpaceToMiniWidthChildList(this.mTempMiniWidthChildList, i3, i4);
    }

    private int handlePriorityIncompressible(int i, int i2) {
        int i3;
        int i4;
        int size = (View.MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight();
        int size2 = (View.MeasureSpec.getSize(i2) - getPaddingTop()) - getPaddingBottom();
        this.mTempMiniWidthChildList.clear();
        this.mTempDisposableChildList.clear();
        int orientation = getOrientation();
        int i5 = 0;
        for (int i6 = 0; i6 < getChildCount(); i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                layoutParams.backupOrRestore();
                int priority = layoutParams.getPriority(orientation);
                if (orientation == 0) {
                    i3 = layoutParams.leftMargin;
                    i4 = layoutParams.rightMargin;
                } else {
                    i3 = layoutParams.topMargin;
                    i4 = layoutParams.bottomMargin;
                }
                int i7 = i3 + i4;
                if (priority == 3) {
                    if (orientation == 0) {
                        if (layoutParams.width >= 0) {
                            i5 += layoutParams.width + i7;
                        } else {
                            childAt.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), i2);
                            i5 += childAt.getMeasuredWidth() + i7;
                        }
                    } else if (layoutParams.height >= 0) {
                        i5 += layoutParams.height + i7;
                    } else {
                        childAt.measure(i, View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE));
                        i5 += childAt.getMeasuredHeight() + i7;
                    }
                } else if (priority == 2) {
                    this.mTempMiniWidthChildList.add(childAt);
                } else if (layoutParams.weight == 0.0f) {
                    this.mTempDisposableChildList.add(childAt);
                }
            }
        }
        return i5;
    }

    private void handleVertical(int i, int i2) {
        int size = (View.MeasureSpec.getSize(i2) - getPaddingTop()) - getPaddingBottom();
        int mode = View.MeasureSpec.getMode(i2);
        int visibleChildCount = getVisibleChildCount();
        if (mode == 0 || visibleChildCount == 0 || size <= 0) {
            return;
        }
        int handlePriorityIncompressible = handlePriorityIncompressible(i, i2);
        if (handlePriorityIncompressible >= size) {
            Iterator<View> it = this.mTempMiniWidthChildList.iterator();
            while (it.hasNext()) {
                View next = it.next();
                LayoutParams layoutParams = (LayoutParams) next.getLayoutParams();
                next.measure(i, View.MeasureSpec.makeMeasureSpec(layoutParams.miniContentProtectionSize, Integer.MIN_VALUE));
                layoutParams.height = next.getMeasuredHeight();
            }
            Iterator<View> it2 = this.mTempDisposableChildList.iterator();
            while (it2.hasNext()) {
                ((LayoutParams) it2.next().getLayoutParams()).height = 0;
            }
            return;
        }
        int i3 = size - handlePriorityIncompressible;
        Iterator<View> it3 = this.mTempMiniWidthChildList.iterator();
        int i4 = 0;
        int i5 = 0;
        while (it3.hasNext()) {
            View next2 = it3.next();
            LayoutParams layoutParams2 = (LayoutParams) next2.getLayoutParams();
            next2.measure(i, View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE));
            int i6 = layoutParams2.topMargin + layoutParams2.bottomMargin;
            i4 += next2.getMeasuredHeight() + i6;
            i5 += Math.min(next2.getMeasuredHeight(), layoutParams2.miniContentProtectionSize) + i6;
        }
        if (i5 >= i3) {
            Iterator<View> it4 = this.mTempMiniWidthChildList.iterator();
            while (it4.hasNext()) {
                View next3 = it4.next();
                LayoutParams layoutParams3 = (LayoutParams) next3.getLayoutParams();
                layoutParams3.height = Math.min(next3.getMeasuredHeight(), layoutParams3.miniContentProtectionSize);
            }
            Iterator<View> it5 = this.mTempDisposableChildList.iterator();
            while (it5.hasNext()) {
                ((LayoutParams) it5.next().getLayoutParams()).height = 0;
            }
            return;
        }
        if (i4 < i3) {
            if (this.mTempDisposableChildList.isEmpty()) {
                return;
            }
            dispatchSpaceToDisposableChildList(this.mTempDisposableChildList, i3 - i4);
            return;
        }
        Iterator<View> it6 = this.mTempDisposableChildList.iterator();
        while (it6.hasNext()) {
            ((LayoutParams) it6.next().getLayoutParams()).width = 0;
        }
        if (i3 >= i4 || this.mTempMiniWidthChildList.isEmpty()) {
            return;
        }
        dispatchSpaceToMiniWidthChildList(this.mTempMiniWidthChildList, i3, i4);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && super.checkLayoutParams(layoutParams);
    }

    protected void dispatchSpaceToDisposableChildList(ArrayList<View> arrayList, int i) {
        Iterator<View> it = arrayList.iterator();
        while (it.hasNext()) {
            LayoutParams layoutParams = (LayoutParams) it.next().getLayoutParams();
            i = getOrientation() == 0 ? i - (layoutParams.leftMargin - layoutParams.rightMargin) : i - (layoutParams.topMargin - layoutParams.bottomMargin);
        }
        int max = Math.max(0, i / arrayList.size());
        Iterator<View> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            LayoutParams layoutParams2 = (LayoutParams) it2.next().getLayoutParams();
            if (getOrientation() == 0) {
                layoutParams2.width = max;
            } else {
                layoutParams2.height = max;
            }
        }
    }

    protected void dispatchSpaceToMiniWidthChildList(ArrayList<View> arrayList, int i, int i2) {
        int i3 = i2 - i;
        if (i3 > 0) {
            Iterator<View> it = arrayList.iterator();
            while (it.hasNext()) {
                LayoutParams layoutParams = (LayoutParams) it.next().getLayoutParams();
                if (getOrientation() == 0) {
                    layoutParams.width = Math.max(0, (int) (r0.getMeasuredWidth() - (i3 * ((((r0.getMeasuredWidth() + layoutParams.leftMargin) + layoutParams.rightMargin) * 1.0f) / i2))));
                } else {
                    layoutParams.height = Math.max(0, (int) (r0.getMeasuredHeight() - (i3 * ((((r0.getMeasuredHeight() + layoutParams.topMargin) + layoutParams.bottomMargin) * 1.0f) / i2))));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LinearLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.qmuiteam.qmui.layout.QMUILinearLayout, android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        if (getOrientation() == 0) {
            handleHorizontal(i, i2);
        } else {
            handleVertical(i, i2);
        }
        super.onMeasure(i, i2);
    }
}
