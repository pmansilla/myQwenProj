package com.qmuiteam.qmui.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.appcompat.R;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import com.wx.wheelview.common.WheelConstants;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class QMUIViewHelper {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private static final int[] APPCOMPAT_CHECK_ATTRS = {R.attr.colorPrimary};

    /* loaded from: classes2.dex */
    private static class ViewGroupHelper {
        private static final ThreadLocal<Matrix> sMatrix = new ThreadLocal<>();
        private static final ThreadLocal<RectF> sRectF = new ThreadLocal<>();

        private ViewGroupHelper() {
        }

        static void offsetDescendantMatrix(ViewParent viewParent, View view, Matrix matrix) {
            Object parent = view.getParent();
            if ((parent instanceof View) && parent != viewParent) {
                offsetDescendantMatrix(viewParent, (View) parent, matrix);
                matrix.preTranslate(-r0.getScrollX(), -r0.getScrollY());
            }
            matrix.preTranslate(view.getLeft(), view.getTop());
            if (view.getMatrix().isIdentity()) {
                return;
            }
            matrix.preConcat(view.getMatrix());
        }

        public static void offsetDescendantRect(ViewGroup viewGroup, View view, Rect rect) {
            Matrix matrix = sMatrix.get();
            if (matrix == null) {
                matrix = new Matrix();
                sMatrix.set(matrix);
            } else {
                matrix.reset();
            }
            offsetDescendantMatrix(viewGroup, view, matrix);
            RectF rectF = sRectF.get();
            if (rectF == null) {
                rectF = new RectF();
                sRectF.set(rectF);
            }
            rectF.set(rect);
            matrix.mapRect(rectF);
            rect.set((int) (rectF.left + 0.5f), (int) (rectF.top + 0.5f), (int) (rectF.right + 0.5f), (int) (rectF.bottom + 0.5f));
        }
    }

    public static Rect calcViewScreenLocation(View view) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        return new Rect(iArr[0], iArr[1], iArr[0] + view.getWidth(), iArr[1] + view.getHeight());
    }

    public static void checkAppCompatTheme(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
        boolean z = !obtainStyledAttributes.hasValue(0);
        obtainStyledAttributes.recycle();
        if (z) {
            throw new IllegalArgumentException("You need to use a Theme.AppCompat theme (or descendant) with the design library.");
        }
    }

    public static void clearValueAnimator(Animator animator) {
        if (animator != null) {
            animator.removeAllListeners();
            if (animator instanceof ValueAnimator) {
                ((ValueAnimator) animator).removeAllUpdateListeners();
            }
            if (Build.VERSION.SDK_INT >= 19) {
                animator.pause();
            }
            animator.cancel();
        }
    }

    public static void expendTouchArea(final View view, final int i) {
        if (view != null) {
            final View view2 = (View) view.getParent();
            view2.post(new Runnable() { // from class: com.qmuiteam.qmui.util.QMUIViewHelper.1
                @Override // java.lang.Runnable
                public void run() {
                    Rect rect = new Rect();
                    view.getHitRect(rect);
                    rect.left -= i;
                    rect.top -= i;
                    rect.right += i;
                    rect.bottom += i;
                    view2.setTouchDelegate(new TouchDelegate(rect, view));
                }
            });
        }
    }

    public static AlphaAnimation fadeIn(View view, int i, Animation.AnimationListener animationListener, boolean z) {
        if (view == null) {
            return null;
        }
        if (!z) {
            view.setAlpha(1.0f);
            view.setVisibility(0);
            return null;
        }
        view.setVisibility(0);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        alphaAnimation.setDuration(i);
        alphaAnimation.setFillAfter(true);
        if (animationListener != null) {
            alphaAnimation.setAnimationListener(animationListener);
        }
        view.startAnimation(alphaAnimation);
        return alphaAnimation;
    }

    public static AlphaAnimation fadeOut(final View view, int i, final Animation.AnimationListener animationListener, boolean z) {
        if (view == null) {
            return null;
        }
        if (!z) {
            view.setVisibility(8);
            return null;
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        alphaAnimation.setDuration(i);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.qmuiteam.qmui.util.QMUIViewHelper.5
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(8);
                if (animationListener != null) {
                    animationListener.onAnimationEnd(animation);
                }
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
                if (animationListener != null) {
                    animationListener.onAnimationRepeat(animation);
                }
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                if (animationListener != null) {
                    animationListener.onAnimationStart(animation);
                }
            }
        });
        view.startAnimation(alphaAnimation);
        return alphaAnimation;
    }

    public static View findViewFromViewStub(View view, int i, int i2) {
        if (view == null) {
            return null;
        }
        View findViewById = view.findViewById(i2);
        if (findViewById != null) {
            return findViewById;
        }
        ViewStub viewStub = (ViewStub) view.findViewById(i);
        if (viewStub == null) {
            return null;
        }
        View inflate = viewStub.inflate();
        return inflate != null ? inflate.findViewById(i2) : inflate;
    }

    public static View findViewFromViewStub(View view, int i, int i2, int i3) {
        if (view == null) {
            return null;
        }
        View findViewById = view.findViewById(i2);
        if (findViewById != null) {
            return findViewById;
        }
        ViewStub viewStub = (ViewStub) view.findViewById(i);
        if (viewStub == null) {
            return null;
        }
        if (viewStub.getLayoutResource() < 1 && i3 > 0) {
            viewStub.setLayoutResource(i3);
        }
        View inflate = viewStub.inflate();
        return inflate != null ? inflate.findViewById(i2) : inflate;
    }

    public static int generateViewId() {
        int i;
        int i2;
        if (Build.VERSION.SDK_INT >= 17) {
            return View.generateViewId();
        }
        do {
            i = sNextGeneratedId.get();
            i2 = i + 1;
            if (i2 > 16777215) {
                i2 = 1;
            }
        } while (!sNextGeneratedId.compareAndSet(i, i2));
        return i;
    }

    public static View getActivityRoot(Activity activity) {
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

    public static void getDescendantRect(ViewGroup viewGroup, View view, Rect rect) {
        rect.set(0, 0, view.getWidth(), view.getHeight());
        ViewGroupHelper.offsetDescendantRect(viewGroup, view, rect);
    }

    public static boolean getIsLastLineSpacingExtraError() {
        return Build.VERSION.SDK_INT < 21;
    }

    public static boolean isListViewAlreadyAtBottom(ListView listView) {
        View childAt;
        return (listView.getAdapter() == null || listView.getHeight() == 0 || listView.getLastVisiblePosition() != listView.getAdapter().getCount() - 1 || (childAt = listView.getChildAt(listView.getChildCount() - 1)) == null || childAt.getBottom() != listView.getHeight()) ? false : true;
    }

    public static void playBackgroundBlinkAnimation(View view, @ColorInt int i) {
        if (view == null) {
            return;
        }
        playViewBackgroundAnimation(view, i, new int[]{0, 255, 0}, WheelConstants.WHEEL_SCROLL_DELAY_DURATION);
    }

    public static Animator playViewBackgroundAnimation(final View view, @ColorInt int i, int[] iArr, int i2, final Runnable runnable) {
        int length = iArr.length - 1;
        ColorDrawable colorDrawable = new ColorDrawable(i);
        final Drawable background = view.getBackground();
        setBackgroundKeepingPadding(view, colorDrawable);
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        while (i3 < length) {
            i3++;
            arrayList.add(ObjectAnimator.ofInt(view.getBackground(), "alpha", iArr[i3], iArr[i3]));
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(i2);
        animatorSet.addListener(new Animator.AnimatorListener() { // from class: com.qmuiteam.qmui.util.QMUIViewHelper.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                QMUIViewHelper.setBackgroundKeepingPadding(view, background);
                if (runnable != null) {
                    runnable.run();
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }
        });
        animatorSet.playSequentially(arrayList);
        animatorSet.start();
        return animatorSet;
    }

    public static void playViewBackgroundAnimation(View view, int i, int i2, long j) {
        playViewBackgroundAnimation(view, i, i2, j, 0, 0, null);
    }

    public static void playViewBackgroundAnimation(final View view, @ColorInt int i, @ColorInt int i2, long j, int i3, int i4, final Runnable runnable) {
        final Drawable background = view.getBackground();
        setBackgroundColorKeepPadding(view, i);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(i, i2);
        valueAnimator.setDuration(j / (i3 + 1));
        valueAnimator.setRepeatCount(i3);
        valueAnimator.setRepeatMode(2);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.qmuiteam.qmui.util.QMUIViewHelper.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                QMUIViewHelper.setBackgroundColorKeepPadding(view, ((Integer) valueAnimator2.getAnimatedValue()).intValue());
            }
        });
        if (i4 != 0) {
            view.setTag(i4, valueAnimator);
        }
        valueAnimator.addListener(new Animator.AnimatorListener() { // from class: com.qmuiteam.qmui.util.QMUIViewHelper.4
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                QMUIViewHelper.setBackgroundKeepingPadding(view, background);
                if (runnable != null) {
                    runnable.run();
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }
        });
        valueAnimator.start();
    }

    public static void playViewBackgroundAnimation(View view, @ColorInt int i, int[] iArr, int i2) {
        playViewBackgroundAnimation(view, i, iArr, i2, null);
    }

    public static void requestApplyInsets(Window window) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            window.getDecorView().requestFitSystemWindows();
        } else if (Build.VERSION.SDK_INT >= 21) {
            window.getDecorView().requestApplyInsets();
        }
    }

    public static void safeSetImageViewSelected(ImageView imageView, boolean z) {
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            return;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        imageView.setSelected(z);
        if (drawable.getIntrinsicWidth() == intrinsicWidth && drawable.getIntrinsicHeight() == intrinsicHeight) {
            return;
        }
        imageView.requestLayout();
    }

    public static void setBackgroundColorKeepPadding(View view, @ColorInt int i) {
        int[] iArr = {view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()};
        view.setBackgroundColor(i);
        view.setPadding(iArr[0], iArr[1], iArr[2], iArr[3]);
    }

    public static void setBackgroundKeepingPadding(View view, int i) {
        setBackgroundKeepingPadding(view, view.getResources().getDrawable(i));
    }

    @TargetApi(16)
    public static void setBackgroundKeepingPadding(View view, Drawable drawable) {
        int[] iArr = {view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()};
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
        view.setPadding(iArr[0], iArr[1], iArr[2], iArr[3]);
    }

    public static ColorFilter setImageViewTintColor(ImageView imageView, @ColorInt int i) {
        LightingColorFilter lightingColorFilter = new LightingColorFilter(Color.argb(255, 0, 0, 0), i);
        imageView.setColorFilter(lightingColorFilter);
        return lightingColorFilter;
    }

    public static void setPaddingBottom(View view, int i) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), i);
    }

    public static void setPaddingLeft(View view, int i) {
        view.setPadding(i, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingRight(View view, int i) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), i, view.getPaddingBottom());
    }

    public static void setPaddingTop(View view, int i) {
        view.setPadding(view.getPaddingLeft(), i, view.getPaddingRight(), view.getPaddingBottom());
    }

    @Nullable
    public static TranslateAnimation slideIn(View view, int i, Animation.AnimationListener animationListener, boolean z, QMUIDirection qMUIDirection) {
        TranslateAnimation translateAnimation = null;
        if (view == null) {
            return null;
        }
        if (!z) {
            view.clearAnimation();
            view.setVisibility(0);
            return null;
        }
        switch (qMUIDirection) {
            case LEFT_TO_RIGHT:
                translateAnimation = new TranslateAnimation(1, -1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                break;
            case TOP_TO_BOTTOM:
                translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -1.0f, 1, 0.0f);
                break;
            case RIGHT_TO_LEFT:
                translateAnimation = new TranslateAnimation(1, 1.0f, 1, 0.0f, 1, 0.0f, 1, 0.0f);
                break;
            case BOTTOM_TO_TOP:
                translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
                break;
        }
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.setDuration(i);
        translateAnimation.setFillAfter(true);
        if (animationListener != null) {
            translateAnimation.setAnimationListener(animationListener);
        }
        view.setVisibility(0);
        view.startAnimation(translateAnimation);
        return translateAnimation;
    }

    @Nullable
    public static TranslateAnimation slideOut(final View view, int i, final Animation.AnimationListener animationListener, boolean z, QMUIDirection qMUIDirection) {
        TranslateAnimation translateAnimation = null;
        if (view == null) {
            return null;
        }
        if (!z) {
            view.clearAnimation();
            view.setVisibility(8);
            return null;
        }
        switch (qMUIDirection) {
            case LEFT_TO_RIGHT:
                translateAnimation = new TranslateAnimation(1, 0.0f, 1, 1.0f, 1, 0.0f, 1, 0.0f);
                break;
            case TOP_TO_BOTTOM:
                translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
                break;
            case RIGHT_TO_LEFT:
                translateAnimation = new TranslateAnimation(1, 0.0f, 1, -1.0f, 1, 0.0f, 1, 0.0f);
                break;
            case BOTTOM_TO_TOP:
                translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, -1.0f);
                break;
        }
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.setDuration(i);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.qmuiteam.qmui.util.QMUIViewHelper.6
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(8);
                if (animationListener != null) {
                    animationListener.onAnimationEnd(animation);
                }
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
                if (animationListener != null) {
                    animationListener.onAnimationRepeat(animation);
                }
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                if (animationListener != null) {
                    animationListener.onAnimationStart(animation);
                }
            }
        });
        view.startAnimation(translateAnimation);
        return translateAnimation;
    }
}
