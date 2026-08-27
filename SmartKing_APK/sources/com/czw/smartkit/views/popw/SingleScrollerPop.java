package com.czw.smartkit.views.popw;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.czw.smartkit.R;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class SingleScrollerPop implements View.OnClickListener {
    private static SingleScrollerPop pop;
    private TextView centerItem;
    private ClickCallback clickCallback;
    private View contentView;
    private Context context;
    private ArrayList<String> datas = new ArrayList<>();
    private TextView leftItem;
    public PopupWindow popupWindow;
    private TextView rightItem;
    private WheelView wheelview;

    /* loaded from: classes.dex */
    public static abstract class ClickCallback {
        public void onDismiss() {
        }

        public abstract void onSelect(int i);
    }

    public SingleScrollerPop(Context context) {
        this.context = context;
        initViewEvent(context);
    }

    public static SingleScrollerPop getPop(Context context) {
        synchronized (Void.class) {
            if (pop == null) {
                synchronized (Void.class) {
                    pop = new SingleScrollerPop(context);
                }
            }
        }
        return pop;
    }

    private void initViewEvent(Context context) {
        this.contentView = LayoutInflater.from(context).inflate(R.layout.pop_single_scroller, (ViewGroup) null);
        this.popupWindow = new PopupWindow(this.contentView, -1, -2, true);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setAlpha(128);
        this.popupWindow.setBackgroundDrawable(colorDrawable);
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.czw.smartkit.views.popw.SingleScrollerPop.1
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                if (SingleScrollerPop.this.clickCallback != null) {
                    SingleScrollerPop.this.clickCallback.onDismiss();
                }
                SingleScrollerPop.this.backgroundAlpha(1.0f);
            }
        });
        this.leftItem = (TextView) this.contentView.findViewById(R.id.leftItem);
        this.leftItem.setOnClickListener(this);
        this.rightItem = (TextView) this.contentView.findViewById(R.id.rightItem);
        this.rightItem.setOnClickListener(this);
        this.centerItem = (TextView) this.contentView.findViewById(R.id.centerItem);
        this.centerItem.setOnClickListener(this);
        this.wheelview = (WheelView) this.contentView.findViewById(R.id.wheelview);
        this.wheelview.setWheelAdapter(new ArrayWheelAdapter(context) { // from class: com.czw.smartkit.views.popw.SingleScrollerPop.2
        });
        this.wheelview.setSkin(WheelView.Skin.Holo);
        this.wheelview.setWheelSize(5);
        this.wheelview.setLoop(false);
    }

    public void backgroundAlpha(float f) {
        WindowManager.LayoutParams attributes = ((FragmentActivity) this.context).getWindow().getAttributes();
        attributes.alpha = f;
        ((FragmentActivity) this.context).getWindow().setAttributes(attributes);
    }

    public void dismiss() {
        this.popupWindow.dismiss();
        backgroundAlpha(1.0f);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.leftItem) {
            dismiss();
            if (this.clickCallback != null) {
                this.clickCallback.onDismiss();
                return;
            }
            return;
        }
        if (id != R.id.rightItem) {
            return;
        }
        dismiss();
        if (this.clickCallback != null) {
            this.wheelview.getSelectedItemPosition();
            Log.e("debug_index", this.wheelview.getCurrentPosition() + "==" + this.wheelview.getSelection() + "==" + this.wheelview.getSelectedItemPosition());
            this.clickCallback.onSelect(this.wheelview.getCurrentPosition());
        }
    }

    public SingleScrollerPop setCurrentValue(String str) {
        int size = this.datas.size();
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= size) {
                break;
            }
            if (this.datas.get(i2).equals(str)) {
                i = i2;
                break;
            }
            i2++;
        }
        this.wheelview.setSelection(i);
        return this;
    }

    public SingleScrollerPop showLeftText(int i) {
        this.leftItem.setText(i);
        return this;
    }

    public SingleScrollerPop showPicker(View view, ArrayList<String> arrayList, ClickCallback clickCallback) {
        this.clickCallback = clickCallback;
        this.popupWindow.showAtLocation(view, 81, 0, 0);
        backgroundAlpha(0.5f);
        this.datas.clear();
        this.datas.addAll(arrayList);
        this.wheelview.setWheelData(arrayList);
        return this;
    }

    public SingleScrollerPop showRightText(int i) {
        this.rightItem.setText(i);
        return this;
    }

    public SingleScrollerPop showTexts(int i, int i2, int i3) {
        this.leftItem.setText(i);
        this.centerItem.setText(i2);
        this.rightItem.setText(i3);
        return this;
    }

    public SingleScrollerPop showTitle(int i) {
        this.centerItem.setText(i);
        return this;
    }

    public SingleScrollerPop showTitleWithValue(int i, String str) {
        this.centerItem.setText(i);
        int size = this.datas.size();
        int i2 = size / 4;
        for (int i3 = 0; i3 < size; i3++) {
            if (this.datas.get(i3).equals(str)) {
                i2 = i3;
            }
        }
        this.wheelview.setSelection(i2);
        return this;
    }
}
