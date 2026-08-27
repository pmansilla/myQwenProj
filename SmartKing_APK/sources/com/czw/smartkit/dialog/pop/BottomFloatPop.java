package com.czw.smartkit.dialog.pop;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.czw.smartkit.R;

/* loaded from: classes.dex */
public class BottomFloatPop extends RootPop {
    private static BottomFloatPop floatPop;
    public ClickCallback clickCallback;
    private TextView item1;
    private TextView item2;

    /* loaded from: classes.dex */
    public static abstract class ClickCallback {
        public abstract void onClick(int i);

        public void onDismiss() {
        }
    }

    public BottomFloatPop(Context context) {
        super(context);
        this.clickCallback = null;
    }

    public static BottomFloatPop getPop(Context context) {
        synchronized (Void.class) {
            if (floatPop == null) {
                synchronized (Void.class) {
                    floatPop = new BottomFloatPop(context);
                }
            }
        }
        return floatPop;
    }

    @Override // com.czw.smartkit.dialog.pop.RootPop
    protected void initSelf() {
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.czw.smartkit.dialog.pop.BottomFloatPop.1
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                if (BottomFloatPop.this.clickCallback != null) {
                    BottomFloatPop.this.clickCallback.onDismiss();
                }
                BottomFloatPop.this.backgroundAlpha(1.0f);
            }
        });
        this.item1 = (TextView) $View(R.id.type_1);
        this.item1.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.dialog.pop.BottomFloatPop.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BottomFloatPop.this.dismiss();
                if (BottomFloatPop.this.clickCallback != null) {
                    BottomFloatPop.this.clickCallback.onClick(0);
                }
            }
        });
        this.item2 = (TextView) $View(R.id.type_2);
        this.item2.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.dialog.pop.BottomFloatPop.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BottomFloatPop.this.dismiss();
                if (BottomFloatPop.this.clickCallback != null) {
                    BottomFloatPop.this.clickCallback.onClick(1);
                }
            }
        });
        $View(R.id.cancel_type).setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.dialog.pop.BottomFloatPop.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BottomFloatPop.this.dismiss();
                if (BottomFloatPop.this.clickCallback != null) {
                    BottomFloatPop.this.clickCallback.onDismiss();
                }
            }
        });
    }

    @Override // com.czw.smartkit.dialog.pop.RootPop
    protected int loadLayout() {
        return R.layout.pop_bottom_float_type;
    }

    public BottomFloatPop showPicker(View view, ClickCallback clickCallback) {
        backgroundAlpha(0.5f);
        this.clickCallback = clickCallback;
        this.popupWindow.showAtLocation(view, 81, 0, 0);
        return this;
    }

    public BottomFloatPop showTxt(int i, int i2) {
        this.item1.setText(i);
        this.item2.setText(i2);
        return this;
    }
}
