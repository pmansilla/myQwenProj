package com.czw.smartkit.dialog.pop.smartking;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.czw.smartkit.R;
import com.czw.smartkit.dialog.pop.RootPop;
import com.wx.wheelview.common.WheelConstants;

/* loaded from: classes.dex */
public class TopFloatPop extends RootPop {
    private static TopFloatPop floatPop;
    public ClickCallback clickCallback;
    private View item1;
    private View item2;
    private View item3;

    /* loaded from: classes.dex */
    public static abstract class ClickCallback {
        public abstract void onClick(int i);

        public void onDismiss() {
        }
    }

    public TopFloatPop(Context context) {
        super(context);
        this.clickCallback = null;
    }

    public static TopFloatPop getPop(Context context) {
        synchronized (Void.class) {
            if (floatPop == null) {
                synchronized (Void.class) {
                    floatPop = new TopFloatPop(context);
                }
            }
        }
        return floatPop;
    }

    @Override // com.czw.smartkit.dialog.pop.RootPop
    protected void initSelf() {
        this.contentView = LayoutInflater.from(this.context).inflate(loadLayout(), (ViewGroup) null);
        this.popupWindow = new PopupWindow(this.contentView, -1, WheelConstants.WHEEL_SCROLL_DELAY_DURATION, true);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setAlpha(128);
        this.popupWindow.setBackgroundDrawable(colorDrawable);
        this.popupWindow.setAnimationStyle(R.style.anim_menu_topbar);
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.czw.smartkit.dialog.pop.smartking.TopFloatPop.1
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                if (TopFloatPop.this.clickCallback != null) {
                    TopFloatPop.this.clickCallback.onDismiss();
                }
                TopFloatPop.this.backgroundAlpha(1.0f);
            }
        });
        this.item1 = $View(R.id.type_1);
        this.item1.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.dialog.pop.smartking.TopFloatPop.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TopFloatPop.this.dismiss();
                if (TopFloatPop.this.clickCallback != null) {
                    TopFloatPop.this.clickCallback.onClick(0);
                }
            }
        });
        this.item2 = $View(R.id.type_2);
        this.item2.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.dialog.pop.smartking.TopFloatPop.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TopFloatPop.this.dismiss();
                if (TopFloatPop.this.clickCallback != null) {
                    TopFloatPop.this.clickCallback.onClick(1);
                }
            }
        });
        this.item3 = $View(R.id.type_3);
        this.item3.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.dialog.pop.smartking.TopFloatPop.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TopFloatPop.this.dismiss();
                if (TopFloatPop.this.clickCallback != null) {
                    TopFloatPop.this.clickCallback.onClick(2);
                }
            }
        });
    }

    @Override // com.czw.smartkit.dialog.pop.RootPop
    protected int loadLayout() {
        return R.layout.pop_top_float_type;
    }

    public TopFloatPop showPicker(View view, ClickCallback clickCallback) {
        this.clickCallback = clickCallback;
        this.popupWindow.showAtLocation(view, 49, 0, (int) this.context.getResources().getDimension(R.dimen.DIMEN_120PX));
        return this;
    }
}
