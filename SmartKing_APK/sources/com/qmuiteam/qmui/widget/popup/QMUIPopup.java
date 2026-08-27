package com.qmuiteam.qmui.widget.popup;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.layout.IQMUILayout;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public class QMUIPopup extends QMUIBasePopup {
    public static final int ANIM_AUTO = 4;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int DIRECTION_BOTTOM = 1;
    public static final int DIRECTION_NONE = 2;
    public static final int DIRECTION_TOP = 0;
    protected int mAnimStyle;
    protected int mArrowCenter;
    protected ImageView mArrowDown;
    protected ImageView mArrowUp;
    protected int mDirection;
    private int mOffsetX;
    private int mOffsetYWhenBottom;
    private int mOffsetYWhenTop;
    private int mPopupLeftRightMinMargin;
    private int mPopupTopBottomMinMargin;
    private int mPreferredDirection;
    protected int mX;
    protected int mY;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Direction {
    }

    public QMUIPopup(Context context) {
        this(context, 2);
    }

    public QMUIPopup(Context context, int i) {
        super(context);
        this.mX = -1;
        this.mY = -1;
        this.mPopupLeftRightMinMargin = 0;
        this.mPopupTopBottomMinMargin = 0;
        this.mOffsetX = 0;
        this.mOffsetYWhenTop = 0;
        this.mOffsetYWhenBottom = 0;
        this.mAnimStyle = 4;
        this.mPreferredDirection = i;
        this.mDirection = this.mPreferredDirection;
    }

    private void calculatePosition(View view) {
        if (view == null) {
            this.mX = (this.mScreenSize.x - this.mWindowWidth) / 2;
            this.mY = (this.mScreenSize.y - this.mWindowHeight) / 2;
            this.mDirection = 2;
            return;
        }
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        this.mArrowCenter = iArr[0] + (view.getWidth() / 2);
        if (this.mArrowCenter < this.mScreenSize.x / 2) {
            if (this.mArrowCenter - (this.mWindowWidth / 2) > this.mPopupLeftRightMinMargin) {
                this.mX = this.mArrowCenter - (this.mWindowWidth / 2);
            } else {
                this.mX = this.mPopupLeftRightMinMargin;
            }
        } else if (this.mArrowCenter + (this.mWindowWidth / 2) < this.mScreenSize.x - this.mPopupLeftRightMinMargin) {
            this.mX = this.mArrowCenter - (this.mWindowWidth / 2);
        } else {
            this.mX = (this.mScreenSize.x - this.mPopupLeftRightMinMargin) - this.mWindowWidth;
        }
        this.mDirection = this.mPreferredDirection;
        switch (this.mPreferredDirection) {
            case 0:
                this.mY = iArr[1] - this.mWindowHeight;
                if (this.mY < this.mPopupTopBottomMinMargin) {
                    this.mY = iArr[1] + view.getHeight();
                    this.mDirection = 1;
                    return;
                }
                return;
            case 1:
                this.mY = iArr[1] + view.getHeight();
                if (this.mY > (this.mScreenSize.y - this.mPopupTopBottomMinMargin) - this.mWindowHeight) {
                    this.mY = iArr[1] - this.mWindowHeight;
                    this.mDirection = 0;
                    return;
                }
                return;
            case 2:
                this.mY = iArr[1];
                return;
            default:
                return;
        }
    }

    private void setAnimationStyle(int i, int i2) {
        if (this.mArrowUp != null) {
            i2 -= this.mArrowUp.getMeasuredWidth() / 2;
        }
        boolean z = this.mDirection == 0;
        switch (this.mAnimStyle) {
            case 1:
                this.mWindow.setAnimationStyle(z ? R.style.QMUI_Animation_PopUpMenu_Left : R.style.QMUI_Animation_PopDownMenu_Left);
                return;
            case 2:
                this.mWindow.setAnimationStyle(z ? R.style.QMUI_Animation_PopUpMenu_Right : R.style.QMUI_Animation_PopDownMenu_Right);
                return;
            case 3:
                this.mWindow.setAnimationStyle(z ? R.style.QMUI_Animation_PopUpMenu_Center : R.style.QMUI_Animation_PopDownMenu_Center);
                return;
            case 4:
                int i3 = i / 4;
                if (i2 <= i3) {
                    this.mWindow.setAnimationStyle(z ? R.style.QMUI_Animation_PopUpMenu_Left : R.style.QMUI_Animation_PopDownMenu_Left);
                    return;
                } else if (i2 <= i3 || i2 >= i3 * 3) {
                    this.mWindow.setAnimationStyle(z ? R.style.QMUI_Animation_PopUpMenu_Right : R.style.QMUI_Animation_PopDownMenu_Right);
                    return;
                } else {
                    this.mWindow.setAnimationStyle(z ? R.style.QMUI_Animation_PopUpMenu_Center : R.style.QMUI_Animation_PopDownMenu_Center);
                    return;
                }
            default:
                return;
        }
    }

    private void setViewVisibility(View view, boolean z) {
        if (view != null) {
            view.setVisibility(z ? 0 : 4);
        }
    }

    private void showArrow() {
        ImageView imageView;
        switch (this.mDirection) {
            case 0:
                setViewVisibility(this.mArrowDown, true);
                setViewVisibility(this.mArrowUp, false);
                imageView = this.mArrowDown;
                break;
            case 1:
                setViewVisibility(this.mArrowUp, true);
                setViewVisibility(this.mArrowDown, false);
                imageView = this.mArrowUp;
                break;
            case 2:
                setViewVisibility(this.mArrowDown, false);
                setViewVisibility(this.mArrowUp, false);
            default:
                imageView = null;
                break;
        }
        if (imageView != null) {
            int measuredWidth = this.mArrowUp.getMeasuredWidth();
            ((ViewGroup.MarginLayoutParams) imageView.getLayoutParams()).leftMargin = (this.mArrowCenter - this.mX) - (measuredWidth / 2);
        }
    }

    public ViewGroup.LayoutParams generateLayoutParam(int i, int i2) {
        return new FrameLayout.LayoutParams(i, i2);
    }

    @LayoutRes
    protected int getRootLayout() {
        return R.layout.qmui_popup_layout;
    }

    protected int getRootLayoutRadius(Context context) {
        return QMUIDisplayHelper.dp2px(context, 5);
    }

    @Override // com.qmuiteam.qmui.widget.popup.QMUIBasePopup
    protected Point onShowBegin(View view, View view2) {
        calculatePosition(view2);
        showArrow();
        setAnimationStyle(this.mScreenSize.x, this.mArrowCenter);
        return new Point(this.mX + this.mOffsetX, this.mY + (this.mDirection == 0 ? this.mOffsetYWhenTop : this.mDirection == 1 ? this.mOffsetYWhenBottom : 0));
    }

    @Override // com.qmuiteam.qmui.widget.popup.QMUIBasePopup
    protected void onWindowSizeChange() {
    }

    public void setAnimStyle(int i) {
        this.mAnimStyle = i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.qmuiteam.qmui.widget.popup.QMUIBasePopup
    public void setContentView(View view) {
        Drawable background = view.getBackground();
        QMUIFrameLayout qMUIFrameLayout = view;
        if (background != null) {
            if (view instanceof IQMUILayout) {
                ((IQMUILayout) view).setRadius(getRootLayoutRadius(this.mContext));
                qMUIFrameLayout = view;
            } else {
                QMUIFrameLayout qMUIFrameLayout2 = new QMUIFrameLayout(this.mContext);
                qMUIFrameLayout2.setRadius(getRootLayoutRadius(this.mContext));
                qMUIFrameLayout2.addView(view);
                qMUIFrameLayout = qMUIFrameLayout2;
            }
        }
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this.mContext).inflate(getRootLayout(), (ViewGroup) null, false);
        this.mArrowDown = (ImageView) frameLayout.findViewById(R.id.arrow_down);
        this.mArrowUp = (ImageView) frameLayout.findViewById(R.id.arrow_up);
        ((FrameLayout) frameLayout.findViewById(R.id.box)).addView(qMUIFrameLayout);
        super.setContentView(frameLayout);
    }

    public void setPopupLeftRightMinMargin(int i) {
        this.mPopupLeftRightMinMargin = i;
    }

    public void setPopupTopBottomMinMargin(int i) {
        this.mPopupTopBottomMinMargin = i;
    }

    public void setPositionOffsetX(int i) {
        this.mOffsetX = i;
    }

    public void setPositionOffsetYWhenBottom(int i) {
        this.mOffsetYWhenBottom = i;
    }

    public void setPositionOffsetYWhenTop(int i) {
        this.mOffsetYWhenTop = i;
    }

    public void setPreferredDirection(int i) {
        this.mPreferredDirection = i;
    }
}
