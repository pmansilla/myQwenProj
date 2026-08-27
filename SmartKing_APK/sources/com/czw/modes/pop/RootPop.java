package com.czw.modes.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.czw.R;

/* loaded from: classes.dex */
public abstract class RootPop {
    protected View contentView;
    protected Context context;
    protected PopupWindow popupWindow;

    public RootPop(Context context) {
        this.context = context;
        _init_(context);
    }

    private void _init_(Context context) {
        this.contentView = LayoutInflater.from(context).inflate(loadLayout(), (ViewGroup) null);
        this.popupWindow = new PopupWindow(this.contentView, -1, -2, true);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setAlpha(128);
        this.popupWindow.setBackgroundDrawable(colorDrawable);
        initSelf();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T extends View> T $View(int i) {
        return (T) this.contentView.findViewById(i);
    }

    protected String $str(int i) {
        return this.context.getString(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void backgroundAlpha(float f) {
        WindowManager.LayoutParams attributes = ((FragmentActivity) this.context).getWindow().getAttributes();
        attributes.alpha = f;
        ((FragmentActivity) this.context).getWindow().setAttributes(attributes);
    }

    public void dismiss() {
        this.popupWindow.dismiss();
        backgroundAlpha(1.0f);
    }

    protected abstract void initSelf();

    protected abstract int loadLayout();
}
