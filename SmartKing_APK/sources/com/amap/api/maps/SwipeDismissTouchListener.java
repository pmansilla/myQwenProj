package com.amap.api.maps;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public class SwipeDismissTouchListener implements View.OnTouchListener {
    public static final int A_HALF = 2;
    public static final int MIN_VIEW_WIDTH = 2;
    public static final int ONE_THIRD = 3;
    private int a;
    private int b;
    private int c;
    private long d;
    private View e;
    private DismissCallbacks f;
    private int g = 1;
    private float h;
    private float i;
    private boolean j;
    private int k;
    private Object l;
    private VelocityTracker m;
    private float n;
    private boolean o;
    private boolean p;

    /* loaded from: classes.dex */
    public interface DismissCallbacks {
        boolean canDismiss(Object obj);

        void onDismiss(View view, Object obj);

        void onNotifySwipe();
    }

    public SwipeDismissTouchListener(View view, Object obj, DismissCallbacks dismissCallbacks) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(view.getContext());
        this.a = viewConfiguration.getScaledTouchSlop();
        this.b = viewConfiguration.getScaledMinimumFlingVelocity() * 16;
        this.c = viewConfiguration.getScaledMaximumFlingVelocity();
        this.d = view.getContext().getResources().getInteger(R.integer.config_shortAnimTime);
        this.e = view;
        this.l = obj;
        this.f = dismissCallbacks;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f.onDismiss(this.e, this.l);
        final ViewGroup.LayoutParams layoutParams = this.e.getLayoutParams();
        final int height = this.e.getHeight();
        ValueAnimator duration = ValueAnimator.ofInt(height, 1).setDuration(this.d);
        duration.addListener(new AnimatorListenerAdapter() { // from class: com.amap.api.maps.SwipeDismissTouchListener.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SwipeDismissTouchListener.this.e.setAlpha(0.0f);
                SwipeDismissTouchListener.this.e.setTranslationX(0.0f);
                layoutParams.height = height;
                SwipeDismissTouchListener.this.e.setLayoutParams(layoutParams);
            }
        });
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.amap.api.maps.SwipeDismissTouchListener.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                SwipeDismissTouchListener.this.e.setLayoutParams(layoutParams);
            }
        });
        duration.start();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0178, code lost:
    
        if (r10.m.getXVelocity() > 0.0f) goto L73;
     */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouch(android.view.View r11, android.view.MotionEvent r12) {
        /*
            Method dump skipped, instructions count: 514
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.maps.SwipeDismissTouchListener.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }
}
