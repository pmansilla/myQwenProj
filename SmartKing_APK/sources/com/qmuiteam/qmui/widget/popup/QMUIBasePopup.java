package com.qmuiteam.qmui.widget.popup;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

/* loaded from: classes2.dex */
public abstract class QMUIBasePopup {
    private static final String TAG = "QMUIBasePopup";
    protected Context mContext;
    private PopupWindow.OnDismissListener mDismissListener;
    protected View mRootView;
    private RootView mRootViewWrapper;
    protected PopupWindow mWindow;
    protected WindowManager mWindowManager;
    protected Drawable mBackground = null;
    protected Point mScreenSize = new Point();
    protected int mWindowHeight = 0;
    protected int mWindowWidth = 0;
    private boolean mNeedCacheSize = true;

    /* loaded from: classes2.dex */
    public class RootView extends ViewGroup {
        public RootView(Context context) {
            super(context);
        }

        public RootView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        @Override // android.view.ViewGroup
        public void addView(View view) {
            if (getChildCount() > 0) {
                throw new RuntimeException("only support one child");
            }
            super.addView(view);
        }

        @Override // android.view.View
        protected void onConfigurationChanged(Configuration configuration) {
            if (QMUIBasePopup.this.mWindow != null && QMUIBasePopup.this.mWindow.isShowing()) {
                QMUIBasePopup.this.mWindow.dismiss();
            }
            QMUIBasePopup.this.onConfigurationChanged(configuration);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            if (getChildCount() == 0) {
                return;
            }
            View childAt = getChildAt(0);
            childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            if (getChildCount() == 0) {
                setMeasuredDimension(0, 0);
            }
            int size = View.MeasureSpec.getSize(i2);
            int makeWidthMeasureSpec = QMUIBasePopup.this.makeWidthMeasureSpec();
            int makeHeightMeasureSpec = QMUIBasePopup.this.makeHeightMeasureSpec();
            int size2 = View.MeasureSpec.getSize(makeHeightMeasureSpec);
            int mode = View.MeasureSpec.getMode(makeHeightMeasureSpec);
            if (size < size2) {
                makeHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, mode);
            }
            View childAt = getChildAt(0);
            childAt.measure(makeWidthMeasureSpec, makeHeightMeasureSpec);
            int i3 = QMUIBasePopup.this.mWindowWidth;
            int i4 = QMUIBasePopup.this.mWindowHeight;
            QMUIBasePopup.this.mWindowWidth = childAt.getMeasuredWidth();
            QMUIBasePopup.this.mWindowHeight = childAt.getMeasuredHeight();
            if (i3 != QMUIBasePopup.this.mWindowWidth || (i4 != QMUIBasePopup.this.mWindowHeight && QMUIBasePopup.this.mWindow.isShowing())) {
                QMUIBasePopup.this.onWindowSizeChange();
            }
            Log.i(QMUIBasePopup.TAG, "in measure: mWindowWidth = " + QMUIBasePopup.this.mWindowWidth + " ;mWindowHeight = " + QMUIBasePopup.this.mWindowHeight);
            setMeasuredDimension(QMUIBasePopup.this.mWindowWidth, QMUIBasePopup.this.mWindowHeight);
        }
    }

    public QMUIBasePopup(Context context) {
        this.mContext = context;
        this.mWindow = new PopupWindow(context);
        this.mWindow.setTouchInterceptor(new View.OnTouchListener() { // from class: com.qmuiteam.qmui.widget.popup.QMUIBasePopup.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 4) {
                    return false;
                }
                QMUIBasePopup.this.mWindow.dismiss();
                return false;
            }
        });
        this.mWindowManager = (WindowManager) context.getSystemService("window");
    }

    private void measureWindowSize() {
        this.mRootView.measure(makeWidthMeasureSpec(), makeHeightMeasureSpec());
        this.mWindowWidth = this.mRootView.getMeasuredWidth();
        this.mWindowHeight = this.mRootView.getMeasuredHeight();
        Log.i(TAG, "measureWindowSize: mWindowWidth = " + this.mWindowWidth + " ;mWindowHeight = " + this.mWindowHeight);
    }

    public void dimBehind(float f) {
        if (!isShowing()) {
            throw new RuntimeException("should call after method show() or in onShowEnd()");
        }
        View decorView = getDecorView();
        if (decorView != null) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) decorView.getLayoutParams();
            layoutParams.flags = 2;
            layoutParams.dimAmount = f;
            this.mWindowManager.updateViewLayout(decorView, layoutParams);
        }
    }

    public void dismiss() {
        this.mWindow.dismiss();
    }

    public View getDecorView() {
        try {
            return this.mWindow.getBackground() == null ? Build.VERSION.SDK_INT >= 23 ? (View) this.mWindow.getContentView().getParent() : this.mWindow.getContentView() : Build.VERSION.SDK_INT >= 23 ? (View) this.mWindow.getContentView().getParent().getParent() : (View) this.mWindow.getContentView().getParent();
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean isShowing() {
        return this.mWindow != null && this.mWindow.isShowing();
    }

    protected int makeHeightMeasureSpec() {
        return View.MeasureSpec.makeMeasureSpec(QMUIDisplayHelper.getScreenHeight(this.mContext), Integer.MIN_VALUE);
    }

    protected int makeWidthMeasureSpec() {
        return View.MeasureSpec.makeMeasureSpec(QMUIDisplayHelper.getScreenWidth(this.mContext), Integer.MIN_VALUE);
    }

    protected void onConfigurationChanged(Configuration configuration) {
    }

    protected void onDismiss() {
    }

    protected abstract Point onShowBegin(View view, View view2);

    protected void onShowConfig() {
        if (this.mRootViewWrapper == null) {
            throw new IllegalStateException("setContentView was not called with a view to display.");
        }
        if (this.mBackground == null) {
            this.mWindow.setBackgroundDrawable(new ColorDrawable(0));
        } else {
            this.mWindow.setBackgroundDrawable(this.mBackground);
        }
        this.mWindow.setWidth(-2);
        this.mWindow.setHeight(-2);
        this.mWindow.setTouchable(true);
        this.mWindow.setFocusable(true);
        this.mWindow.setOutsideTouchable(true);
        this.mWindow.setContentView(this.mRootViewWrapper);
        this.mWindowManager.getDefaultDisplay().getSize(this.mScreenSize);
    }

    protected void onShowEnd() {
    }

    protected abstract void onWindowSizeChange();

    public void setBackgroundDrawable(Drawable drawable) {
        this.mBackground = drawable;
    }

    public void setContentView(int i) {
        setContentView(((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(i, (ViewGroup) null));
    }

    public void setContentView(View view) {
        if (view == null) {
            throw new IllegalStateException("setContentView was not called with a view to display.");
        }
        this.mRootViewWrapper = new RootView(this.mContext);
        this.mRootViewWrapper.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.mRootView = view;
        this.mRootViewWrapper.addView(view);
        this.mWindow.setContentView(this.mRootViewWrapper);
        this.mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.qmuiteam.qmui.widget.popup.QMUIBasePopup.3
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                QMUIBasePopup.this.onDismiss();
                if (QMUIBasePopup.this.mDismissListener != null) {
                    QMUIBasePopup.this.mDismissListener.onDismiss();
                }
            }
        });
    }

    public void setNeedCacheSize(boolean z) {
        this.mNeedCacheSize = z;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.mDismissListener = onDismissListener;
    }

    public final void show(View view) {
        show(view, view);
    }

    public final void show(View view, View view2) {
        if (ViewCompat.isAttachedToWindow(view2)) {
            onShowConfig();
            if (this.mWindowWidth == 0 || this.mWindowHeight == 0 || !this.mNeedCacheSize) {
                measureWindowSize();
            }
            Point onShowBegin = onShowBegin(view, view2);
            this.mWindow.showAtLocation(view, 0, onShowBegin.x, onShowBegin.y);
            onShowEnd();
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.qmuiteam.qmui.widget.popup.QMUIBasePopup.2
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view3) {
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view3) {
                    if (QMUIBasePopup.this.isShowing()) {
                        QMUIBasePopup.this.dismiss();
                    }
                }
            });
        }
    }
}
