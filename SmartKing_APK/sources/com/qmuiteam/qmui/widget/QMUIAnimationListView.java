package com.qmuiteam.qmui.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.support.v4.util.LongSparseArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public class QMUIAnimationListView extends ListView {
    private static final float DEFAULT_OFFSET_DURATION_UNIT = 0.5f;
    private static final long DURATION_ALPHA = 300;
    private static final long DURATION_OFFSET_MAX = 1000;
    private static final long DURATION_OFFSET_MIN = 0;
    private static final String TAG = "QMUIAnimationListView";
    protected final Set<Long> mAfterVisible;
    private int mAnimationManipulateDurationLimit;
    protected final Set<Long> mBeforeVisible;
    private ValueAnimator mChangeDisappearAnimator;
    private long mChangeDisappearPlayTime;
    protected final LongSparseArray<View> mDetachViewsMap;
    private boolean mIsAnimating;
    private long mLastManipulateTime;
    private float mOffsetDurationUnit;
    private Interpolator mOffsetInterpolator;
    private boolean mOpenChangeDisappearAnimation;
    private final List<Manipulator> mPendingManipulations;
    private final List<Manipulator> mPendingManipulationsWithoutAnimation;
    protected final LongSparseArray<Integer> mPositionMap;
    private ListAdapter mRealAdapter;
    protected final LongSparseArray<Integer> mTopMap;
    private WrapperAdapter mWrapperAdapter;

    /* loaded from: classes2.dex */
    private abstract class ManipulateAnimatorListener implements Animator.AnimatorListener {
        private ManipulateAnimatorListener() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* loaded from: classes2.dex */
    public interface Manipulator<T extends ListAdapter> {
        void manipulate(T t);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class WrapperAdapter extends BaseAdapter {
        private ListAdapter mAdapter;
        private boolean mShouldNotifyChange = true;
        private final DataSetObserver mObserver = new DataSetObserver() { // from class: com.qmuiteam.qmui.widget.QMUIAnimationListView.WrapperAdapter.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                if (WrapperAdapter.this.mShouldNotifyChange) {
                    WrapperAdapter.this.notifyDataSetChanged();
                }
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                WrapperAdapter.this.notifyDataSetInvalidated();
            }
        };
        private boolean mIsAnimationEnabled = false;

        public WrapperAdapter(ListAdapter listAdapter) {
            this.mAdapter = listAdapter;
            this.mAdapter.registerDataSetObserver(this.mObserver);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.mAdapter.getCount();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return this.mAdapter.getItem(i);
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return this.mAdapter.getItemId(i);
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getItemViewType(int i) {
            return this.mAdapter.getItemViewType(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            return this.mAdapter.getView(i, view, viewGroup);
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getViewTypeCount() {
            return this.mAdapter.getViewTypeCount();
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public boolean hasStableIds() {
            boolean hasStableIds = this.mAdapter.hasStableIds();
            this.mIsAnimationEnabled = hasStableIds;
            return hasStableIds;
        }

        public boolean isAnimationEnabled() {
            return this.mIsAnimationEnabled;
        }

        public void setShouldNotifyChange(boolean z) {
            this.mShouldNotifyChange = z;
        }
    }

    public QMUIAnimationListView(Context context) {
        this(context, null);
    }

    public QMUIAnimationListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTopMap = new LongSparseArray<>();
        this.mPositionMap = new LongSparseArray<>();
        this.mDetachViewsMap = new LongSparseArray<>();
        this.mBeforeVisible = new HashSet();
        this.mAfterVisible = new HashSet();
        this.mPendingManipulations = new ArrayList();
        this.mPendingManipulationsWithoutAnimation = new ArrayList();
        this.mChangeDisappearPlayTime = 0L;
        this.mIsAnimating = false;
        this.mAnimationManipulateDurationLimit = 0;
        this.mLastManipulateTime = 0L;
        this.mOffsetDurationUnit = 0.5f;
        this.mOffsetInterpolator = new LinearInterpolator();
        this.mOpenChangeDisappearAnimation = false;
        init();
    }

    public QMUIAnimationListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTopMap = new LongSparseArray<>();
        this.mPositionMap = new LongSparseArray<>();
        this.mDetachViewsMap = new LongSparseArray<>();
        this.mBeforeVisible = new HashSet();
        this.mAfterVisible = new HashSet();
        this.mPendingManipulations = new ArrayList();
        this.mPendingManipulationsWithoutAnimation = new ArrayList();
        this.mChangeDisappearPlayTime = 0L;
        this.mIsAnimating = false;
        this.mAnimationManipulateDurationLimit = 0;
        this.mLastManipulateTime = 0L;
        this.mOffsetDurationUnit = 0.5f;
        this.mOffsetInterpolator = new LinearInterpolator();
        this.mOpenChangeDisappearAnimation = false;
        init();
    }

    @TargetApi(21)
    public QMUIAnimationListView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTopMap = new LongSparseArray<>();
        this.mPositionMap = new LongSparseArray<>();
        this.mDetachViewsMap = new LongSparseArray<>();
        this.mBeforeVisible = new HashSet();
        this.mAfterVisible = new HashSet();
        this.mPendingManipulations = new ArrayList();
        this.mPendingManipulationsWithoutAnimation = new ArrayList();
        this.mChangeDisappearPlayTime = 0L;
        this.mIsAnimating = false;
        this.mAnimationManipulateDurationLimit = 0;
        this.mLastManipulateTime = 0L;
        this.mOffsetDurationUnit = 0.5f;
        this.mOffsetInterpolator = new LinearInterpolator();
        this.mOpenChangeDisappearAnimation = false;
        init();
    }

    private void doAnimation() {
        setEnabled(false);
        setClickable(false);
        doPreLayoutAnimation(new ManipulateAnimatorListener() { // from class: com.qmuiteam.qmui.widget.QMUIAnimationListView.1
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                QMUIAnimationListView.this.mWrapperAdapter.notifyDataSetChanged();
                QMUIAnimationListView.this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.qmuiteam.qmui.widget.QMUIAnimationListView.1.1
                    @Override // android.view.ViewTreeObserver.OnPreDrawListener
                    public boolean onPreDraw() {
                        QMUIAnimationListView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                        QMUIAnimationListView.this.doPostLayoutAnimation();
                        return true;
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00ad  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00b0 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void doPostLayoutAnimation() {
        /*
            Method dump skipped, instructions count: 258
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qmuiteam.qmui.widget.QMUIAnimationListView.doPostLayoutAnimation():void");
    }

    private void doPreLayoutAnimation(Animator.AnimatorListener animatorListener) {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mTopMap.size(); i++) {
            long keyAt = this.mTopMap.keyAt(i);
            if (getPositionForId(keyAt) < 0) {
                Animator deleteAnimator = getDeleteAnimator(getChildAt(this.mPositionMap.get(keyAt).intValue()));
                this.mPositionMap.remove(keyAt);
                animatorSet.play(deleteAnimator);
                arrayList.add(Long.valueOf(keyAt));
            }
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            this.mTopMap.remove(((Long) arrayList.get(i2)).longValue());
        }
        if (this.mOpenChangeDisappearAnimation) {
            for (int i3 = 0; i3 < this.mPositionMap.size(); i3++) {
                View childAt = getChildAt(this.mPositionMap.valueAt(i3).intValue());
                ViewCompat.setHasTransientState(childAt, true);
                this.mDetachViewsMap.put(this.mPositionMap.keyAt(i3), childAt);
            }
        }
        if (animatorSet.getChildAnimations().isEmpty()) {
            animatorListener.onAnimationEnd(animatorSet);
        } else {
            animatorSet.addListener(animatorListener);
            animatorSet.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishAnimation() {
        this.mWrapperAdapter.setShouldNotifyChange(true);
        this.mChangeDisappearAnimator = null;
        if (this.mOpenChangeDisappearAnimation) {
            for (int i = 0; i < this.mDetachViewsMap.size(); i++) {
                this.mDetachViewsMap.valueAt(i).setAlpha(1.0f);
            }
            this.mDetachViewsMap.clear();
        }
        this.mIsAnimating = false;
        setEnabled(true);
        setClickable(true);
        manipulatePending();
    }

    private long getOffsetDuration(int i, int i2) {
        return Math.max(0L, Math.min(Math.abs(i - i2) * this.mOffsetDurationUnit, 1000L));
    }

    private void init() {
        setWillNotDraw(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void manipulatePending() {
        if (!this.mPendingManipulationsWithoutAnimation.isEmpty()) {
            this.mIsAnimating = true;
            Iterator<Manipulator> it = this.mPendingManipulationsWithoutAnimation.iterator();
            while (it.hasNext()) {
                it.next().manipulate(this.mRealAdapter);
            }
            this.mPendingManipulationsWithoutAnimation.clear();
            this.mWrapperAdapter.notifyDataSetChanged();
            post(new Runnable() { // from class: com.qmuiteam.qmui.widget.QMUIAnimationListView.5
                @Override // java.lang.Runnable
                public void run() {
                    QMUIAnimationListView.this.mIsAnimating = false;
                    QMUIAnimationListView.this.manipulatePending();
                }
            });
            return;
        }
        if (this.mPendingManipulations.isEmpty()) {
            return;
        }
        this.mIsAnimating = true;
        prepareAnimation();
        Iterator<Manipulator> it2 = this.mPendingManipulations.iterator();
        while (it2.hasNext()) {
            it2.next().manipulate(this.mRealAdapter);
        }
        this.mPendingManipulations.clear();
        doAnimation();
    }

    private void prepareAnimation() {
        this.mTopMap.clear();
        this.mPositionMap.clear();
        this.mBeforeVisible.clear();
        this.mAfterVisible.clear();
        this.mDetachViewsMap.clear();
        this.mWrapperAdapter.setShouldNotifyChange(false);
        int childCount = getChildCount();
        int firstVisiblePosition = getFirstVisiblePosition();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            long itemId = this.mWrapperAdapter.getItemId(firstVisiblePosition + i);
            this.mTopMap.put(itemId, Integer.valueOf(childAt.getTop()));
            this.mPositionMap.put(itemId, Integer.valueOf(i));
        }
        for (int i2 = 0; i2 < firstVisiblePosition; i2++) {
            this.mBeforeVisible.add(Long.valueOf(this.mWrapperAdapter.getItemId(i2)));
        }
        int count = this.mWrapperAdapter.getCount();
        for (int i3 = firstVisiblePosition + childCount; i3 < count; i3++) {
            this.mAfterVisible.add(Long.valueOf(this.mWrapperAdapter.getItemId(i3)));
        }
    }

    protected ObjectAnimator alphaObjectAnimator(View view, final boolean z, long j, boolean z2) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "alpha", z ? new float[]{0.0f, 1.0f} : new float[]{1.0f, 0.0f});
        ofFloat.setDuration(j);
        if (z2) {
            final WeakReference weakReference = new WeakReference(view);
            ofFloat.addListener(new ManipulateAnimatorListener() { // from class: com.qmuiteam.qmui.widget.QMUIAnimationListView.6
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (weakReference.get() != null) {
                        ((View) weakReference.get()).setAlpha(z ? 0.0f : 1.0f);
                    }
                }
            });
        }
        return ofFloat;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return isEnabled() && super.dispatchTouchEvent(motionEvent);
    }

    protected Animator getAddAnimator(View view, int i, int i2, int i3, int i4) {
        view.setAlpha(0.0f);
        view.clearAnimation();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alphaObjectAnimator(view, true, 50L, false));
        if (i3 != i) {
            animatorSet.play(getOffsetAnimator(view, i3, i));
        }
        animatorSet.setStartDelay(view.getHeight() * this.mOffsetDurationUnit);
        return animatorSet;
    }

    protected long getChangeDisappearDuration() {
        return getHeight() * this.mOffsetDurationUnit;
    }

    protected Animator getDeleteAnimator(View view) {
        return alphaObjectAnimator(view, false, DURATION_ALPHA, true);
    }

    protected Animator getOffsetAnimator(View view, int i, int i2) {
        return getOffsetAnimator(view, i, i2, getOffsetDuration(i, i2));
    }

    protected Animator getOffsetAnimator(View view, int i, int i2, long j) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "translationY", i - i2, 0.0f);
        ofFloat.setDuration(j);
        ofFloat.setInterpolator(this.mOffsetInterpolator);
        return ofFloat;
    }

    public float getOffsetDurationUnit() {
        return this.mOffsetDurationUnit;
    }

    protected int getPositionForId(long j) {
        for (int i = 0; i < this.mWrapperAdapter.getCount(); i++) {
            if (this.mWrapperAdapter.getItemId(i) == j) {
                return i;
            }
        }
        return -1;
    }

    public ListAdapter getRealAdapter() {
        return this.mRealAdapter;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T extends ListAdapter> void manipulate(Manipulator<T> manipulator) {
        Log.i(TAG, "manipulate");
        if (!this.mWrapperAdapter.isAnimationEnabled()) {
            manipulateWithoutAnimation(manipulator);
            return;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        boolean z = uptimeMillis - this.mLastManipulateTime > ((long) this.mAnimationManipulateDurationLimit);
        this.mLastManipulateTime = uptimeMillis;
        if (this.mIsAnimating) {
            if (z) {
                this.mPendingManipulations.add(manipulator);
                return;
            } else {
                this.mPendingManipulationsWithoutAnimation.add(manipulator);
                return;
            }
        }
        if (!z) {
            manipulator.manipulate(this.mRealAdapter);
            this.mWrapperAdapter.notifyDataSetChanged();
        } else {
            this.mIsAnimating = true;
            prepareAnimation();
            manipulator.manipulate(this.mRealAdapter);
            doAnimation();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T extends ListAdapter> void manipulateWithoutAnimation(Manipulator<T> manipulator) {
        Log.i(TAG, "manipulateWithoutAnimation");
        if (this.mIsAnimating) {
            this.mPendingManipulationsWithoutAnimation.add(manipulator);
        } else {
            manipulator.manipulate(this.mRealAdapter);
            this.mWrapperAdapter.notifyDataSetChanged();
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        int intValue;
        super.onDraw(canvas);
        if (this.mOpenChangeDisappearAnimation && this.mChangeDisappearAnimator != null && this.mChangeDisappearAnimator.isStarted() && this.mDetachViewsMap.size() > 0 && this.mIsAnimating) {
            while (i < this.mDetachViewsMap.size()) {
                long keyAt = this.mDetachViewsMap.keyAt(i);
                View valueAt = this.mDetachViewsMap.valueAt(i);
                int positionForId = getPositionForId(keyAt);
                int i2 = (int) (((float) this.mChangeDisappearPlayTime) / this.mOffsetDurationUnit);
                if (positionForId < getFirstVisiblePosition()) {
                    intValue = this.mTopMap.get(keyAt).intValue() - i2;
                    i = intValue < (-valueAt.getHeight()) ? i + 1 : 0;
                    valueAt.layout(0, intValue, valueAt.getWidth(), valueAt.getHeight() + intValue);
                    valueAt.setAlpha(1.0f - ((((float) this.mChangeDisappearPlayTime) * 1.0f) / ((float) getChangeDisappearDuration())));
                    drawChild(canvas, valueAt, getDrawingTime());
                } else {
                    intValue = this.mTopMap.get(keyAt).intValue() + i2;
                    if (intValue > getHeight()) {
                    }
                    valueAt.layout(0, intValue, valueAt.getWidth(), valueAt.getHeight() + intValue);
                    valueAt.setAlpha(1.0f - ((((float) this.mChangeDisappearPlayTime) * 1.0f) / ((float) getChangeDisappearDuration())));
                    drawChild(canvas, valueAt, getDrawingTime());
                }
            }
        }
    }

    @Override // android.widget.AdapterView
    public void setAdapter(ListAdapter listAdapter) {
        this.mRealAdapter = listAdapter;
        this.mWrapperAdapter = new WrapperAdapter(this.mRealAdapter);
        super.setAdapter((ListAdapter) this.mWrapperAdapter);
    }

    public void setAnimationManipulateDurationLimit(int i) {
        this.mAnimationManipulateDurationLimit = i;
    }

    public void setOffsetDurationUnit(float f) {
        this.mOffsetDurationUnit = f;
    }

    public void setOffsetInterpolator(Interpolator interpolator) {
        this.mOffsetInterpolator = interpolator;
    }

    public void setOpenChangeDisappearAnimation(boolean z) {
        this.mOpenChangeDisappearAnimation = z;
    }
}
