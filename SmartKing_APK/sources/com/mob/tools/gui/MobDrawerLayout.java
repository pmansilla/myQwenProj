package com.mob.tools.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes2.dex */
public class MobDrawerLayout extends ViewGroup {
    private static final int SNAP_VELOCITY = 500;
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private FrameLayout bodyContainer;
    private FrameLayout drawerContainer;
    private double drawerWidth;
    private float lastMotionX;
    private float lastMotionY;
    private OnDrawerStateChangeListener listener;
    private boolean lockScroll;
    private int maximumVelocity;
    private boolean opened;
    private Paint paint;
    private Scroller scroller;
    private int touchSlop;
    private int touchState;
    private DrawerType type;
    private VelocityTracker velocityTracker;

    /* loaded from: classes2.dex */
    public enum DrawerType {
        LEFT_COVER,
        RIGHT_COVER,
        LEFT_BOTTOM,
        RIGHT_BOTTOM,
        LEFT_PUSH,
        RIGHT_PUSH
    }

    /* loaded from: classes2.dex */
    public interface OnDrawerStateChangeListener {
        void onClosing(MobDrawerLayout mobDrawerLayout, int i);

        void onOpening(MobDrawerLayout mobDrawerLayout, int i);
    }

    public MobDrawerLayout(Context context) {
        super(context);
        init(context);
    }

    public MobDrawerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public MobDrawerLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void computeDrag(int i) {
        if (i >= 500) {
            switch (this.type) {
                case LEFT_COVER:
                case LEFT_BOTTOM:
                case LEFT_PUSH:
                    open();
                    return;
                case RIGHT_COVER:
                case RIGHT_BOTTOM:
                case RIGHT_PUSH:
                    close();
                    return;
                default:
                    return;
            }
        }
        if (i <= -500) {
            switch (this.type) {
                case LEFT_COVER:
                case LEFT_BOTTOM:
                case LEFT_PUSH:
                    close();
                    return;
                case RIGHT_COVER:
                case RIGHT_BOTTOM:
                case RIGHT_PUSH:
                    open();
                    return;
                default:
                    return;
            }
        }
        int i2 = 0;
        switch (this.type) {
            case LEFT_COVER:
            case LEFT_PUSH:
                i2 = this.drawerContainer.getRight();
                break;
            case RIGHT_COVER:
            case RIGHT_PUSH:
                i2 = getWidth() - this.drawerContainer.getLeft();
                break;
            case LEFT_BOTTOM:
                i2 = this.bodyContainer.getLeft();
                break;
            case RIGHT_BOTTOM:
                i2 = -this.bodyContainer.getLeft();
                break;
        }
        double width = getWidth();
        double d = this.drawerWidth;
        Double.isNaN(width);
        if (i2 >= ((int) (width * d)) / 2) {
            open();
        } else {
            close();
        }
    }

    private void dragToLeft(int i) {
        switch (this.type) {
            case LEFT_COVER:
                int right = this.drawerContainer.getRight();
                if (right > 0) {
                    int i2 = right - i;
                    if (i2 < 0) {
                        i2 = 0;
                    }
                    double width = getWidth();
                    double d = this.drawerWidth;
                    Double.isNaN(width);
                    this.drawerContainer.layout(i2 - ((int) (width * d)), 0, i2, getHeight());
                    return;
                }
                return;
            case RIGHT_COVER:
                int right2 = this.drawerContainer.getRight();
                int width2 = getWidth();
                if (right2 > width2) {
                    int i3 = right2 - i;
                    if (i3 < width2) {
                        i3 = width2;
                    }
                    double width3 = getWidth();
                    double d2 = this.drawerWidth;
                    Double.isNaN(width3);
                    this.drawerContainer.layout(i3 - ((int) (width3 * d2)), 0, i3, getHeight());
                    return;
                }
                return;
            case LEFT_BOTTOM:
                int left = this.bodyContainer.getLeft();
                if (left > 0) {
                    int i4 = left - i;
                    if (i4 < 0) {
                        i4 = 0;
                    }
                    this.bodyContainer.layout(i4, 0, getWidth() + i4, getHeight());
                    return;
                }
                return;
            case LEFT_PUSH:
                int right3 = this.drawerContainer.getRight();
                if (right3 > 0) {
                    int i5 = right3 - i;
                    if (i5 < 0) {
                        i5 = 0;
                    }
                    double width4 = getWidth();
                    double d3 = this.drawerWidth;
                    Double.isNaN(width4);
                    int i6 = i5 - ((int) (width4 * d3));
                    int width5 = getWidth() + i5;
                    this.drawerContainer.layout(i6, 0, i5, getHeight());
                    this.bodyContainer.layout(i5, 0, width5, getHeight());
                    return;
                }
                return;
            case RIGHT_BOTTOM:
                int left2 = this.bodyContainer.getLeft();
                double d4 = -getWidth();
                double d5 = this.drawerWidth;
                Double.isNaN(d4);
                int i7 = (int) (d4 * d5);
                if (left2 > i7) {
                    int i8 = left2 - i;
                    if (i8 < i7) {
                        i8 = i7;
                    }
                    this.bodyContainer.layout(i8, 0, getWidth() + i8, getHeight());
                    return;
                }
                return;
            case RIGHT_PUSH:
                int right4 = this.drawerContainer.getRight();
                int width6 = getWidth();
                if (right4 > width6) {
                    int i9 = right4 - i;
                    if (i9 < width6) {
                        i9 = width6;
                    }
                    double width7 = getWidth();
                    double d6 = this.drawerWidth;
                    Double.isNaN(width7);
                    int i10 = i9 - ((int) (width7 * d6));
                    int width8 = i10 - getWidth();
                    this.drawerContainer.layout(i10, 0, i9, getHeight());
                    this.bodyContainer.layout(width8, 0, i10, getHeight());
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void dragToRight(int i) {
        switch (this.type) {
            case LEFT_COVER:
                int left = this.drawerContainer.getLeft();
                if (left < 0) {
                    int i2 = i + left;
                    if (i2 > 0) {
                        i2 = 0;
                    }
                    double width = getWidth();
                    double d = this.drawerWidth;
                    Double.isNaN(width);
                    this.drawerContainer.layout(i2, 0, ((int) (width * d)) + i2, getHeight());
                    return;
                }
                return;
            case RIGHT_COVER:
                int left2 = this.drawerContainer.getLeft();
                int width2 = getWidth();
                if (left2 < width2) {
                    int i3 = i + left2;
                    if (i3 > width2) {
                        i3 = width2;
                    }
                    double width3 = getWidth();
                    double d2 = this.drawerWidth;
                    Double.isNaN(width3);
                    this.drawerContainer.layout(i3, 0, ((int) (width3 * d2)) + i3, getHeight());
                    return;
                }
                return;
            case LEFT_BOTTOM:
                int left3 = this.bodyContainer.getLeft();
                double width4 = getWidth();
                double d3 = this.drawerWidth;
                Double.isNaN(width4);
                int i4 = (int) (width4 * d3);
                if (left3 < i4) {
                    int i5 = i + left3;
                    if (i5 > i4) {
                        i5 = i4;
                    }
                    this.bodyContainer.layout(i5, 0, getWidth() + i5, getHeight());
                    return;
                }
                return;
            case LEFT_PUSH:
                int left4 = this.drawerContainer.getLeft();
                if (left4 < 0) {
                    int i6 = i + left4;
                    if (i6 > 0) {
                        i6 = 0;
                    }
                    double width5 = getWidth();
                    double d4 = this.drawerWidth;
                    Double.isNaN(width5);
                    int i7 = ((int) (width5 * d4)) + i6;
                    int width6 = getWidth() + i7;
                    this.drawerContainer.layout(i6, 0, i7, getHeight());
                    this.bodyContainer.layout(i7, 0, width6, getHeight());
                    return;
                }
                return;
            case RIGHT_BOTTOM:
                int left5 = this.bodyContainer.getLeft();
                if (left5 < 0) {
                    int i8 = i + left5;
                    if (i8 > 0) {
                        i8 = 0;
                    }
                    this.bodyContainer.layout(i8, 0, getWidth() + i8, getHeight());
                    return;
                }
                return;
            case RIGHT_PUSH:
                int left6 = this.bodyContainer.getLeft();
                if (left6 < 0) {
                    int i9 = i + left6;
                    if (i9 > 0) {
                        i9 = 0;
                    }
                    int width7 = getWidth() + i9;
                    double width8 = getWidth();
                    double d5 = this.drawerWidth;
                    Double.isNaN(width8);
                    this.bodyContainer.layout(i9, 0, width7, getHeight());
                    this.drawerContainer.layout(width7, 0, ((int) (width8 * d5)) + width7, getHeight());
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void drawShadow(Canvas canvas) {
        switch (this.type) {
            case LEFT_COVER:
                int right = this.drawerContainer.getRight();
                if (right > 0) {
                    float f = right;
                    float f2 = right + 25;
                    this.paint.setShader(new LinearGradient(f, 0.0f, f2, 0.0f, Integer.MIN_VALUE, 0, Shader.TileMode.CLAMP));
                    canvas.drawRect(f, 0.0f, f2, getHeight(), this.paint);
                    return;
                }
                return;
            case RIGHT_COVER:
                int left = this.drawerContainer.getLeft();
                if (left < getWidth()) {
                    float f3 = left - 25;
                    float f4 = left;
                    this.paint.setShader(new LinearGradient(f3, 0.0f, f4, 0.0f, 0, Integer.MIN_VALUE, Shader.TileMode.CLAMP));
                    canvas.drawRect(f3, 0.0f, f4, getHeight(), this.paint);
                    return;
                }
                return;
            case LEFT_BOTTOM:
            case LEFT_PUSH:
            default:
                int left2 = this.bodyContainer.getLeft();
                if (left2 > 0) {
                    float f5 = left2 - 25;
                    float f6 = left2;
                    this.paint.setShader(new LinearGradient(f5, 0.0f, f6, 0.0f, 0, Integer.MIN_VALUE, Shader.TileMode.CLAMP));
                    canvas.drawRect(f5, 0.0f, f6, getHeight(), this.paint);
                    return;
                }
                return;
            case RIGHT_BOTTOM:
            case RIGHT_PUSH:
                int right2 = this.bodyContainer.getRight();
                if (right2 < getWidth()) {
                    float f7 = right2;
                    float f8 = right2 + 25;
                    this.paint.setShader(new LinearGradient(f7, 0.0f, f8, 0.0f, Integer.MIN_VALUE, 0, Shader.TileMode.CLAMP));
                    canvas.drawRect(f7, 0.0f, f8, getHeight(), this.paint);
                    return;
                }
                return;
        }
    }

    private void init(Context context) {
        this.scroller = SmoothScroller.DEFAULT.getScroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.touchSlop = viewConfiguration.getScaledTouchSlop();
        this.maximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.type = DrawerType.LEFT_COVER;
        this.drawerWidth = 0.8d;
        this.touchState = 0;
        this.paint = new Paint();
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.mob.tools.gui.MobDrawerLayout.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
            }
        };
        this.bodyContainer = new FrameLayout(context);
        this.bodyContainer.setOnClickListener(onClickListener);
        this.drawerContainer = new FrameLayout(context);
        this.drawerContainer.setOnClickListener(onClickListener);
        addView(this.bodyContainer);
        addView(this.drawerContainer);
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x001c A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean isClose() {
        /*
            r4 = this;
            int[] r0 = com.mob.tools.gui.MobDrawerLayout.AnonymousClass2.$SwitchMap$com$mob$tools$gui$MobDrawerLayout$DrawerType
            com.mob.tools.gui.MobDrawerLayout$DrawerType r1 = r4.type
            int r1 = r1.ordinal()
            r0 = r0[r1]
            r1 = 1
            r2 = 0
            switch(r0) {
                case 1: goto L34;
                case 2: goto L27;
                case 3: goto L1e;
                case 4: goto L1e;
                case 5: goto L10;
                case 6: goto L10;
                default: goto Lf;
            }
        Lf:
            goto L3d
        L10:
            android.widget.FrameLayout r0 = r4.bodyContainer
            int r0 = r0.getRight()
            int r3 = r4.getWidth()
            if (r0 != r3) goto L3d
        L1c:
            r2 = 1
            goto L3d
        L1e:
            android.widget.FrameLayout r0 = r4.bodyContainer
            int r0 = r0.getLeft()
            if (r0 != 0) goto L3d
            goto L1c
        L27:
            android.widget.FrameLayout r0 = r4.drawerContainer
            int r0 = r0.getLeft()
            int r3 = r4.getWidth()
            if (r0 != r3) goto L3d
            goto L1c
        L34:
            android.widget.FrameLayout r0 = r4.drawerContainer
            int r0 = r0.getRight()
            if (r0 != 0) goto L3d
            goto L1c
        L3d:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.gui.MobDrawerLayout.isClose():boolean");
    }

    private void switchDrawer(boolean z, boolean z2) {
        int left;
        int i;
        int i2;
        this.bodyContainer.clearFocus();
        this.drawerContainer.clearFocus();
        double d = 0.0d;
        switch (this.type) {
            case LEFT_COVER:
            case LEFT_PUSH:
                left = this.drawerContainer.getLeft();
                if (!z) {
                    double d2 = -getWidth();
                    double d3 = this.drawerWidth;
                    Double.isNaN(d2);
                    d = d2 * d3;
                }
                i = (int) d;
                i2 = left;
                break;
            case RIGHT_COVER:
            case RIGHT_PUSH:
                int width = getWidth();
                int left2 = this.drawerContainer.getLeft();
                if (z) {
                    double d4 = width;
                    double d5 = this.drawerWidth;
                    Double.isNaN(d4);
                    d = d4 * d5;
                }
                i = width - ((int) d);
                i2 = left2;
                break;
            case LEFT_BOTTOM:
                left = this.bodyContainer.getLeft();
                if (z) {
                    double width2 = getWidth();
                    double d6 = this.drawerWidth;
                    Double.isNaN(width2);
                    d = width2 * d6;
                }
                i = (int) d;
                i2 = left;
                break;
            case RIGHT_BOTTOM:
                left = this.bodyContainer.getLeft();
                if (z) {
                    double d7 = -getWidth();
                    double d8 = this.drawerWidth;
                    Double.isNaN(d7);
                    d = d7 * d8;
                }
                i = (int) d;
                i2 = left;
                break;
            default:
                i = 0;
                i2 = 0;
                break;
        }
        this.scroller.abortAnimation();
        if (i2 != i) {
            this.scroller.startScroll(i2, 0, i - i2, 0, z2 ? 0 : 100);
        }
        invalidate();
    }

    public void close() {
        close(false);
    }

    public void close(boolean z) {
        switchDrawer(false, z);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (!this.scroller.computeScrollOffset()) {
            if (isClose()) {
                this.opened = false;
                return;
            } else {
                this.opened = true;
                return;
            }
        }
        switch (this.type) {
            case LEFT_COVER:
            case RIGHT_COVER:
                int currX = this.scroller.getCurrX();
                double width = getWidth();
                double d = this.drawerWidth;
                Double.isNaN(width);
                this.drawerContainer.layout(currX, 0, ((int) (width * d)) + currX, getHeight());
                break;
            case LEFT_BOTTOM:
            case RIGHT_BOTTOM:
                int currX2 = this.scroller.getCurrX();
                this.bodyContainer.layout(currX2, 0, getWidth() + currX2, getHeight());
                break;
            case LEFT_PUSH:
                int width2 = getWidth();
                int currX3 = this.scroller.getCurrX();
                double d2 = width2;
                double d3 = this.drawerWidth;
                Double.isNaN(d2);
                int i = ((int) (d2 * d3)) + currX3;
                this.drawerContainer.layout(currX3, 0, i, getHeight());
                this.bodyContainer.layout(i, 0, width2 + i, getHeight());
                break;
            case RIGHT_PUSH:
                int width3 = getWidth();
                int currX4 = this.scroller.getCurrX();
                double d4 = width3;
                double d5 = this.drawerWidth;
                Double.isNaN(d4);
                this.bodyContainer.layout(currX4 - width3, 0, currX4, getHeight());
                this.drawerContainer.layout(currX4, 0, ((int) (d4 * d5)) + currX4, getHeight());
                break;
        }
        postInvalidate();
        if (this.listener == null || this.scroller.getFinalX() == this.scroller.getStartX()) {
            return;
        }
        int currX5 = ((this.scroller.getCurrX() - this.scroller.getStartX()) * 100) / (this.scroller.getFinalX() - this.scroller.getStartX());
        if (this.opened) {
            this.listener.onClosing(this, currX5);
        } else {
            this.listener.onOpening(this, currX5);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        FrameLayout frameLayout;
        FrameLayout frameLayout2;
        long drawingTime = getDrawingTime();
        int i = AnonymousClass2.$SwitchMap$com$mob$tools$gui$MobDrawerLayout$DrawerType[this.type.ordinal()];
        if (i != 6) {
            switch (i) {
                case 1:
                case 2:
                    break;
                default:
                    frameLayout = this.drawerContainer;
                    frameLayout2 = this.bodyContainer;
                    break;
            }
            drawChild(canvas, frameLayout, drawingTime);
            drawChild(canvas, frameLayout2, drawingTime);
            drawShadow(canvas);
        }
        frameLayout = this.bodyContainer;
        frameLayout2 = this.drawerContainer;
        drawChild(canvas, frameLayout, drawingTime);
        drawChild(canvas, frameLayout2, drawingTime);
        drawShadow(canvas);
    }

    public boolean isOpened() {
        return this.opened;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.lockScroll) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 2 && this.touchState != 0) {
            return true;
        }
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.addMovement(motionEvent);
        switch (action) {
            case 0:
                this.lastMotionX = motionEvent.getX();
                this.lastMotionY = motionEvent.getY();
                this.touchState = !this.scroller.isFinished() ? 1 : 0;
                break;
            case 1:
            case 3:
                if (this.velocityTracker != null) {
                    this.velocityTracker.recycle();
                    this.velocityTracker = null;
                }
                this.touchState = 0;
                break;
            case 2:
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                int abs = (int) Math.abs(x - this.lastMotionX);
                if (((int) Math.abs(y - this.lastMotionY)) < abs && abs > this.touchSlop) {
                    this.touchState = 1;
                    this.lastMotionX = x;
                    break;
                }
                break;
        }
        return this.touchState != 0;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int i6 = i4 - i2;
        double d = i5;
        double d2 = this.drawerWidth;
        Double.isNaN(d);
        int i7 = (int) (d * d2);
        if (!isOpened()) {
            switch (this.type) {
                case LEFT_COVER:
                    this.bodyContainer.layout(0, 0, i5, i6);
                    this.drawerContainer.layout(-i7, 0, 0, i6);
                    return;
                case RIGHT_COVER:
                    this.bodyContainer.layout(0, 0, i5, i6);
                    this.drawerContainer.layout(i5, 0, i7 + i5, i6);
                    return;
                case LEFT_BOTTOM:
                    this.bodyContainer.layout(0, 0, i5, i6);
                    this.drawerContainer.layout(0, 0, i7, i6);
                    return;
                case LEFT_PUSH:
                    this.bodyContainer.layout(0, 0, i5, i6);
                    this.drawerContainer.layout(-i7, 0, 0, i6);
                    return;
                case RIGHT_BOTTOM:
                    this.bodyContainer.layout(0, 0, i5, i6);
                    this.drawerContainer.layout(i5 - i7, 0, i5, i6);
                    return;
                case RIGHT_PUSH:
                    this.bodyContainer.layout(0, 0, i5, i6);
                    this.drawerContainer.layout(i5, 0, i7 + i5, i6);
                    return;
                default:
                    return;
            }
        }
        switch (this.type) {
            case LEFT_COVER:
                this.bodyContainer.layout(0, 0, i5, i6);
                this.drawerContainer.layout(0, 0, i7, i6);
                return;
            case RIGHT_COVER:
                this.bodyContainer.layout(0, 0, i5, i6);
                this.drawerContainer.layout(i5 - i7, 0, i5, i6);
                return;
            case LEFT_BOTTOM:
                this.bodyContainer.layout(i7, 0, i5 + i7, i6);
                this.drawerContainer.layout(0, 0, i7, i6);
                return;
            case LEFT_PUSH:
                this.bodyContainer.layout(i7, 0, i5 + i7, i6);
                this.drawerContainer.layout(0, 0, i7, i6);
                return;
            case RIGHT_BOTTOM:
                int i8 = -i7;
                int i9 = i5 - i7;
                this.bodyContainer.layout(i8, 0, i9, i6);
                this.drawerContainer.layout(i9, 0, i5, i6);
                return;
            case RIGHT_PUSH:
                int i10 = -i7;
                int i11 = i5 - i7;
                this.bodyContainer.layout(i10, 0, i11, i6);
                this.drawerContainer.layout(i11, 0, i5, i6);
                return;
            default:
                return;
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(measuredHeight, 1073741824);
        this.bodyContainer.measure(makeMeasureSpec, makeMeasureSpec2);
        double d = measuredWidth;
        double d2 = this.drawerWidth;
        Double.isNaN(d);
        this.drawerContainer.measure(View.MeasureSpec.makeMeasureSpec((int) (d * d2), 1073741824), makeMeasureSpec2);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x007c, code lost:
    
        return true;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r4) {
        /*
            r3 = this;
            android.view.VelocityTracker r0 = r3.velocityTracker
            if (r0 != 0) goto La
            android.view.VelocityTracker r0 = android.view.VelocityTracker.obtain()
            r3.velocityTracker = r0
        La:
            android.view.VelocityTracker r0 = r3.velocityTracker
            r0.addMovement(r4)
            int r0 = r4.getAction()
            r1 = 1
            switch(r0) {
                case 0: goto L65;
                case 1: goto L3d;
                case 2: goto L18;
                case 3: goto L3d;
                default: goto L17;
            }
        L17:
            goto L7c
        L18:
            int r0 = r3.touchState
            if (r0 == r1) goto L26
            boolean r0 = r3.onInterceptTouchEvent(r4)
            if (r0 == 0) goto L7c
            int r0 = r3.touchState
            if (r0 != r1) goto L7c
        L26:
            float r4 = r4.getX()
            float r0 = r3.lastMotionX
            float r0 = r0 - r4
            int r0 = (int) r0
            if (r0 >= 0) goto L35
            int r0 = -r0
            r3.dragToRight(r0)
            goto L3a
        L35:
            if (r0 <= 0) goto L3a
            r3.dragToLeft(r0)
        L3a:
            r3.lastMotionX = r4
            goto L7c
        L3d:
            int r4 = r3.touchState
            if (r4 != r1) goto L61
            android.view.VelocityTracker r4 = r3.velocityTracker
            if (r4 == 0) goto L61
            android.view.VelocityTracker r4 = r3.velocityTracker
            r0 = 1000(0x3e8, float:1.401E-42)
            int r2 = r3.maximumVelocity
            float r2 = (float) r2
            r4.computeCurrentVelocity(r0, r2)
            android.view.VelocityTracker r4 = r3.velocityTracker
            float r4 = r4.getXVelocity()
            int r4 = (int) r4
            r3.computeDrag(r4)
            android.view.VelocityTracker r4 = r3.velocityTracker
            r4.recycle()
            r4 = 0
            r3.velocityTracker = r4
        L61:
            r4 = 0
            r3.touchState = r4
            goto L7c
        L65:
            int r0 = r3.touchState
            if (r0 == 0) goto L7c
            android.widget.Scroller r0 = r3.scroller
            boolean r0 = r0.isFinished()
            if (r0 != 0) goto L76
            android.widget.Scroller r0 = r3.scroller
            r0.abortAnimation()
        L76:
            float r4 = r4.getX()
            r3.lastMotionX = r4
        L7c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.gui.MobDrawerLayout.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void open() {
        open(false);
    }

    public void open(boolean z) {
        switchDrawer(true, z);
    }

    public void setBody(View view) {
        if (ResHelper.isEqual(this.bodyContainer.getChildCount() == 0 ? null : this.bodyContainer.getChildAt(0), view)) {
            return;
        }
        this.bodyContainer.removeAllViews();
        this.bodyContainer.addView(view);
    }

    public void setDrawer(View view) {
        if (ResHelper.isEqual(this.drawerContainer.getChildCount() == 0 ? null : this.drawerContainer.getChildAt(0), view)) {
            return;
        }
        this.drawerContainer.removeAllViews();
        this.drawerContainer.addView(view);
    }

    public void setDrawerType(DrawerType drawerType) {
        if (drawerType == null) {
            drawerType = DrawerType.LEFT_COVER;
        }
        if (this.type != drawerType) {
            this.type = drawerType;
            switch (drawerType) {
                case LEFT_COVER:
                case RIGHT_COVER:
                    this.drawerContainer.bringToFront();
                    break;
                default:
                    this.bodyContainer.bringToFront();
                    break;
            }
            postInvalidate();
        }
    }

    public void setDrawerWidth(double d) {
        if (d < 0.0d) {
            d = 0.800000011920929d;
        }
        if (d > 1.0d) {
            d = 1.0d;
        }
        if (this.drawerWidth != d) {
            this.drawerWidth = d;
            postInvalidate();
        }
    }

    public void setLockScroll(boolean z) {
        this.lockScroll = z;
    }

    public void setOnDrawerStateChangeListener(OnDrawerStateChangeListener onDrawerStateChangeListener) {
        this.listener = onDrawerStateChangeListener;
    }
}
